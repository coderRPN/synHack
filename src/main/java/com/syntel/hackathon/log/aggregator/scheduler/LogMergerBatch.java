package com.syntel.hackathon.log.aggregator.scheduler;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.syntel.hackathon.log.aggregator.model.LogObject;
import com.syntel.hackathon.log.aggregator.service.LogService;

@Component
public class LogMergerBatch {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private LogService logService;

    @Value("${elasticsearch.logFilepath}")
    private String logDirectory;

    @Value("${date.pattern}")
    private String datePattern;

    private Pattern pattern;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
    private String fileName;
    private long fileSize;
    private long lastModifiedDate;
    private RandomAccessFile randomAccessFile;

    @PostConstruct
    void init() throws IOException {
        pattern = Pattern.compile(datePattern);
        File initialLogFile = new File(logDirectory.toString().trim()).listFiles()[0];
        randomAccessFile = new RandomAccessFile(initialLogFile, "r");
        storeFileMetaData(initialLogFile);
        indexLogFile(FileUtils.readFileToByteArray(initialLogFile));
    }

    @Scheduled(fixedRate = 1000)
    public void indexLogFile() throws IOException {

//        logger.info("Log indexing job last ran at " + dateFormat.format(new Date()));

//        logger.info("Log file directory : " + logDirectory);

        File[] files = new File(logDirectory.toString().trim()).listFiles();

//        logger.info("Number of log files :  " + files.length);

        for (File file : files) {
            if (file.isFile()) {
                indexIfModified(file);
            }
        }
    }

    private void indexIfModified(File file) throws IOException {
        if (file.getName().equals(fileName) && file.lastModified() == lastModifiedDate && file.length() == fileSize) {
            // file not modified no need to index
        } else if (file.getName().equals(fileName) && file.lastModified() > lastModifiedDate && file.length() > fileSize) {
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

}
