package com.syntel.hackathon.log.aggregator.log.service;

import com.syntel.hackathon.log.aggregator.log.model.LogObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface LogService {

    LogObject save(LogObject book);

    void delete(LogObject book);

    LogObject findOne(String id);

    Iterable<LogObject> findAll();

    List<LogObject> findByLineOftext(String lineOftext);

}