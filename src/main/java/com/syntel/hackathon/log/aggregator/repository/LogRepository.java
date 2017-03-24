package com.syntel.hackathon.log.aggregator.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.syntel.hackathon.log.aggregator.model.LogObject;

public interface LogRepository extends ElasticsearchRepository<LogObject, String> {

    List<LogObject> findByLineOftext(String lineOftext);

    List<LogObject> findByReleaseDate(String date);

}