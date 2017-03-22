package com.syntel.hackathon.log.aggregator.log.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "log_aggregator", type = "logs")
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
        return "Book{" +
                "id='" + id + '\'' +
                ", lineOftext='" + lineOftext + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                '}';
    }
}
