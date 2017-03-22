package com.syntel.hackathon.log.aggregator.scheduler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.syntel.hackathon.log.aggregator.log.model.LogObject;
import com.syntel.hackathon.log.aggregator.log.service.LogService;

@Component
public class LogMergerBatch {

    @Autowired
    private ElasticsearchOperations es;

    @Autowired
    private LogService logService;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    @Scheduled(fixedRate = 10000    )
    public void sendMailToCustomers() {

        System.out.println("sendMailToCustomers Job ran at "
            + dateFormat.format(new Date()));

        // List all the new files -

        List<String> results = new ArrayList<String>();

        File[] files = new File("/Users/R900671/synHack/log").listFiles();
        System.out.println("Number of log files :  " + files.length);

        for (File file : files) {
            if (file.isFile()) {
                results.add(file.getName());
                System.out.println("Indexing in progress for : " + file.getName());
                
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String line;
                    Integer counter = 0;
                    while ((line = br.readLine()) != null) {
                      System.out.println("---- --" + line);
                      logService.save(new LogObject("1001" + counter,line, "23-FEB-2017"));
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                }
                
            }
        }
        
        
        printElasticSearchInfo();

        
       // logService.save(new LogObject("1002", "Apache Lucene Basics", "Rambabu Posa", "13-MAR-2017"));
       // logService.save(new LogObject("1003", "Apache Solr Basics", "Rambabu Posa", "21-MAR-2017"));


    }
    
    //useful for debug
    private void printElasticSearchInfo() {

        System.out.println("--ElasticSearch-->");
        Client client = es.getClient();
        Map<String, String> asMap = client.settings().getAsMap();

//        asMap.forEach((k, v) -> {
  //          System.out.println(k + " = " + v);
    //    });
      
        Set printValues = asMap.entrySet();
        
        for (Object object : printValues) {
            System.out.println(object);
        }
        
        
        System.out.println("<--ElasticSearch--");
    }

}
