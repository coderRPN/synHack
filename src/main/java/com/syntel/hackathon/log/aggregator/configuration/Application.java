package com.syntel.hackathon.log.aggregator.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.syntel.hackathon.log.aggregator.scheduler.DirectoryWatcher;

import de.invesdwin.instrument.DynamicInstrumentationLoader;

@SpringBootApplication
@ComponentScan(basePackages = {"com.syntel.hackathon.log.aggregator"})
public class Application {

    public static void main(String args[]) {
        DynamicInstrumentationLoader.waitForInitialized(); //dynamically attach java agent to jvm if not already present
        DynamicInstrumentationLoader.initLoadTimeWeavingContext(); //weave al
        SpringApplication.run(Application.class, args);
        new Thread(new DirectoryWatcher()).start();
    }

}