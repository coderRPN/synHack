package com.syntel.hackathon.log.aggregator.log.repository;

import com.syntel.hackathon.log.aggregator.log.model.LogObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface BookRepository extends ElasticsearchRepository<LogObject, String> {

    List<LogObject> findByLineOftext(String lineOftext);

}