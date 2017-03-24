package com.syntel.hackathon.log.aggregator.util;

public interface LogAggregatorConstants {
    String ES_INDEX_NAME = "log_aggregator";
    String ES_INDEX_TYPE = "logs";
    int ES_HITS_SIZE = 10000;
    String SEARCH_FILED_1 = "lineOftext";
    String SEARCH_FIELD_2 = "releaseDate";
}