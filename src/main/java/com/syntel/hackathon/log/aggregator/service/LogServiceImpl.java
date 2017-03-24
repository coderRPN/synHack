package com.syntel.hackathon.log.aggregator.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.index.query.MatchQueryBuilder.Operator;
import org.elasticsearch.index.query.MultiMatchQueryBuilder.Type;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.syntel.hackathon.log.aggregator.model.LogObject;
import com.syntel.hackathon.log.aggregator.model.SearchObject;
import com.syntel.hackathon.log.aggregator.repository.LogRepository;
import static com.syntel.hackathon.log.aggregator.util.LogAggregatorConstants.*;

@Service
public class LogServiceImpl implements LogService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private Client esClient;

    @Override
    public LogObject save(LogObject logObject) {
        return logRepository.save(logObject);
    }

    @Override
    public void delete(LogObject logObject) {
        logRepository.delete(logObject);
    }

    @Override
    public LogObject findOne(String id) {
        return logRepository.findOne(id);
    }

    @Override
    public List<LogObject> findAll() {
        return Lists.newArrayList(logRepository.findAll());
    }

    @Override
    public List<LogObject> findByLineOftext(String lineOftext) {
        return logRepository.findByLineOftext(lineOftext);
    }

    @Override
    public List<LogObject> findByReleaseDate(String date) {
        return logRepository.findByReleaseDate(date);
    }

    @Override
    public List<LogObject> findByTextAndDate(SearchObject searchObject) throws JsonParseException, JsonMappingException, IOException {
        List<LogObject> logObjects = new ArrayList<LogObject>();
        BytesReference query = QueryBuilders
            .multiMatchQuery(searchObject.getDate().concat(" ").concat(searchObject.getSearchString()), SEARCH_FILED_1)
            .field(SEARCH_FIELD_2)
            .type(Type.BEST_FIELDS)
            .operator(Operator.AND)
            .buildAsBytes();
        SearchResponse searchResponse = esClient.prepareSearch(ES_INDEX_NAME).setSize(ES_HITS_SIZE)
            .setQuery(query).execute().actionGet();
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        for (SearchHit searchHit : searchHits) {
            String sourceAsString = searchHit.getSourceAsString();
            if (sourceAsString != null) {
                ObjectMapper mapper = new ObjectMapper();
                logObjects.add(mapper.readValue(sourceAsString, LogObject.class));
            }
        }
        return logObjects;
    }

    @Override
    @SuppressWarnings(value = { "rawtypes" })
    public void printElasticSearchInfo() {

        logger.info("--ElasticSearch-->");
        Map<String, String> asMap = esClient.settings().getAsMap();

        asMap.forEach((k, v) -> {
            logger.info(k + " = " + v);
        });

        Set printValues = asMap.entrySet();

        for (Object object : printValues) {
            logger.info("", object);
        }

        logger.info("<--ElasticSearch--");
    }

}