package com.syntel.hackathon.log.aggregator.scheduler;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;

import com.syntel.hackathon.log.aggregator.model.LogObject;
import com.syntel.hackathon.log.aggregator.service.LogService;

@Configurable(dependencyCheck=true, autowire=Autowire.BY_TYPE)
public class DirectoryWatcher implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private LogService logService;

    @Value("${elasticsearch.logFilepath}")
    private String logDirectory;

    @Value("${date.pattern}")
    private String datePattern;

    private Pattern pattern;
    private String fileName;
    private long fileSize;
    private long lastModifiedDate;
    private RandomAccessFile randomAccessFile;
    private WatchService watcher;
    private Map<WatchKey, Path> keys;
    private boolean recursive;
    private boolean trace = false;

    private void register(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        if (trace) {
            Path prev = keys.get(key);
            if (prev == null) {
                System.out.format("register: %s\n", dir);
            } else {
                if (!dir.equals(prev)) {
                    System.out.format("update: %s -> %s\n", prev, dir);
                }
            }
        }
        keys.put(key, dir);
    }

    private void initialize(Path dir, boolean recursive) throws IOException {
        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<WatchKey, Path>();
        this.recursive = recursive;

        if (recursive) {
            System.out.format("Scanning %s ...\n", dir);
            registerAll(dir);
            System.out.println("Done.");
        } else {
            register(dir);
        }

        // enable trace after initial registration
        this.trace = true;
        processEvents();
    }

    private void registerAll(final Path start) throws IOException {
        // register directory and sub-directories
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                register(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    @SuppressWarnings("unchecked")
    static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>) event;
    }

    void processEvents() throws IOException {
        for (;;) {

            // wait for key to be signalled
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                return;
            }

            Path dir = keys.get(key);
            if (dir == null) {
                System.err.println("WatchKey not recognized!!");
                continue;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind kind = event.kind();

                // TBD - provide example of how OVERFLOW event is handled
                if (kind == OVERFLOW) {
                    continue;
                }

                // Context for directory entry event is the file name of entry
                WatchEvent<Path> ev = cast(event);
                Path name = ev.context();
                Path child = dir.resolve(name);

                // print out event
                System.out.format("%s: %s\n", event.kind().name(), child);

                if(kind == ENTRY_MODIFY && child.toFile().isFile()) {
                    indexIfModified(child.toFile());
                }
                // if directory is created, and watching recursively, then
                // register it and its sub-directories
                if (recursive && (kind == ENTRY_CREATE)) {
                    try {
                        if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
                            registerAll(child);
                        }
                    } catch (IOException x) {
                        // ignore to keep sample readbale
                    }
                }
            }

            // reset key and remove from set if directory no longer accessible
            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);

                // all directories are inaccessible
                if (keys.isEmpty()) {
                    break;
                }
            }
        }
    }

    private void indexIfModified(File file) throws IOException {
        if (file.getName().equals(fileName) && file.lastModified() == lastModifiedDate && file.length() == fileSize) {
            // file not modified no need to index
        } else if (file.getName().equals(fileName) && file.lastModified() > lastModifiedDate
            && file.length() > fileSize) {
            // file got updated index the changes only
            byte[] delta = new byte[(int) (file.length() - fileSize)];
            randomAccessFile.readFully(delta);
            storeFileMetaData(file);
            indexLogFile(delta);
        }

    }

    private void storeFileMetaData(File file) throws IOException {
        this.fileName = file.getName();
        this.lastModifiedDate = file.lastModified();
        this.fileSize = file.length();

        randomAccessFile.seek(fileSize);
    }

    private void indexLogFile(byte[] delta) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(delta);
            BufferedReader br = new BufferedReader(new InputStreamReader(bis));
            String lineOfText = "";

            StringBuilder strMerger = new StringBuilder();
            /* read log line by line */
            while ((lineOfText = br.readLine()) != null) {
                /* parse strLine to obtain what you want */

                strMerger.append(lineOfText).append(" ");
                String dateStr = getDate(lineOfText);
                if (dateStr != null && !dateStr.isEmpty()) {
                    logService.save(new LogObject(UUID.randomUUID().toString(), strMerger.toString(), dateStr));
                    // reset the log counter
                    strMerger = new StringBuilder();
                }
            }
            bis.close();
        } catch (Exception e) {
            logger.error("Error: " + e);
        }
    }

    private String getDate(String desc) {
        int count = 0;
        String[] allMatches = new String[2];
        Matcher m = pattern.matcher(desc);
        while (m.find()) {
            allMatches[count] = m.group();
            count++;
        }
        return allMatches[0];
    }

    @Override
    public void run() {
        Path dir = Paths.get(logDirectory.trim()).toAbsolutePath();
        pattern = Pattern.compile(datePattern);
        try {
            File initialLogFile = new File(logDirectory.toString().trim()).listFiles()[0];
            randomAccessFile = new RandomAccessFile(initialLogFile, "r");
            storeFileMetaData(initialLogFile);
            indexLogFile(FileUtils.readFileToByteArray(initialLogFile));
            initialize(dir, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
