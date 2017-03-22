package com.syntel.hackathon.log.aggregator.configuration;

import java.util.Map;
import java.util.Set;

import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

import com.syntel.hackathon.log.aggregator.log.model.LogObject;
import com.syntel.hackathon.log.aggregator.log.service.LogService;

@SpringBootApplication
@ComponentScan(basePackages = {"com.syntel.hackathon.log.aggregator"})
public class Application implements CommandLineRunner {

    @Autowired
    private ElasticsearchOperations es;

    @Autowired
    private LogService logService;

    public static void main(String args[]) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

     //   printElasticSearchInfo();

       // logService.save(new LogObject("1001", "Elasticsearch Basics", "Rambabu Posa", "23-FEB-2017"));
       // logService.save(new LogObject("1002", "Apache Lucene Basics", "Rambabu Posa", "13-MAR-2017"));
    //    logService.save(new LogObject("1003", "Apache Solr Basics", "Rambabu Posa", "21-MAR-2017"));

        //fuzzey search
       // Page<LogObject> books = (Page<LogObject>) logService.findByLineOftext("Elasticsearch");

        //List<Book> books = logService.findByTitle("Elasticsearch Basics");

      ///  for (LogObject book : books) {
       //     System.out.println(book);
       // }
        
       // books.forEach(x -> System.out.println(x));


    }

   /* //useful for debug
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
    }*/

}