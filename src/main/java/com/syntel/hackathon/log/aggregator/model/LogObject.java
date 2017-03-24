package com.syntel.hackathon.log.aggregator.model;

import static com.syntel.hackathon.log.aggregator.util.LogAggregatorConstants.ES_INDEX_NAME;
import static com.syntel.hackathon.log.aggregator.util.LogAggregatorConstants.ES_INDEX_TYPE;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = ES_INDEX_NAME, type = ES_INDEX_TYPE)
public class LogObject {

    @Id
    private String id;

    private String lineOftext;

    private String releaseDate;

    public LogObject() {
    }

    public LogObject(String id, String lineOftext, String releaseDate) {
        this.id = id;
        this.lineOftext = lineOftext;
        this.releaseDate = releaseDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLineOftext() {
        return lineOftext;
    }

    public void setLineOftext(String lineOftext) {
        this.lineOftext = lineOftext;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
