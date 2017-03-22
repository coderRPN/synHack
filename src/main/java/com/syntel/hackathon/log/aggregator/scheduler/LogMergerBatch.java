package com.syntel.hackathon.log.aggregator.scheduler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.syntel.hackathon.log.aggregator.log.model.LogObject;
import com.syntel.hackathon.log.aggregator.log.service.LogService;

@Component
public class LogMergerBatch {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private ElasticsearchOperations es;

    @Autowired
    private LogService logService;
    
    @Value("${elasticsearch.logFilepath}")
    private String logDirectory;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    @Scheduled(fixedRate = 10000    )
    public void sendMailToCustomers() {
        // String logFileDir = logDirectory.toString().trim();

        logger.info("sendMailToCustomers Job ran at "
            + dateFormat.format(new Date()));

        // List all the new files -

        List<String> results = new ArrayList<String>();
        
        logger.info("@@@@@Log file directory : " + logDirectory);
           
            File[] files = new File(logDirectory.toString().trim()).listFiles();

            logger.info("Number of log files :  " + files.length);

            for (File file : files) {
                if (file.isFile()) {

                    String prevDateStr = "";

                    try {
                        FileInputStream fstream = new FileInputStream(file);
                        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
                        String strLine;

                        StringBuilder strMerger = new StringBuilder();
                        /* read log line by line */
                        while ((strLine = br.readLine()) != null) {
                            /* parse strLine to obtain what you want */

                            strMerger.append(strLine);
                            StringTokenizer tokenizer = new StringTokenizer(strLine, " ");

                            String dateStr = "";
                            int counter = 0;
                            if (tokenizer.countTokens() > 1) {
                                while (tokenizer.hasMoreTokens() && counter < 2) {
                                    dateStr += tokenizer.nextToken();
                                    counter++;
                                }
                            }
                            if (dateStr != null && !dateStr.isEmpty()) {
                                prevDateStr = dateStr;
                                logService.save(new LogObject(UUID.randomUUID().toString() ,strMerger.toString(),dateStr));
                                // reset the log counter
                                strMerger = new StringBuilder();
                            }

                        }
                        fstream.close();
                    } catch (Exception e) {
                        System.err.println("Error: " + e.getMessage());
                    }

                }
            }

            printElasticSearchInfo();

    }
    
    //useful for info
    private void printElasticSearchInfo() {

        logger.info("--ElasticSearch-->");
        Client client = es.getClient();
        Map<String, String> asMap = client.settings().getAsMap();

//        asMap.forEach((k, v) -> {
  //          logger.info(k + " = " + v);
    //    });
      
        Set printValues = asMap.entrySet();
        
        for (Object object : printValues) {
            logger.info("", object);
        }
        
        
        logger.info("<--ElasticSearch--");
    }

}
