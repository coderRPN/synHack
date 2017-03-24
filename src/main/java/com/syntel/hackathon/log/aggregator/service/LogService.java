package com.syntel.hackathon.log.aggregator.service;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.syntel.hackathon.log.aggregator.model.LogObject;
import com.syntel.hackathon.log.aggregator.model.SearchObject;

public interface LogService {

    LogObject save(LogObject book);

    void delete(LogObject book);

    LogObject findOne(String id);

    List<LogObject> findAll();

    List<LogObject> findByLineOftext(String lineOftext);

    List<LogObject> findByReleaseDate(String date);

    List<LogObject> findByTextAndDate(SearchObject searchObject) throws JsonParseException, JsonMappingException, IOException;

    void printElasticSearchInfo();

}