package com.syntel.hackathon.log.aggregator.controller;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.syntel.hackathon.log.aggregator.model.LogObject;
import com.syntel.hackathon.log.aggregator.model.SearchObject;
import com.syntel.hackathon.log.aggregator.service.LogService;

@RestController
public class SearchController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private LogService logService;

    @CrossOrigin(origins = "*")
    @RequestMapping("/search")
    public List<LogObject> getSearchResults(@Valid @RequestBody SearchObject searchObject, Errors errors) throws JsonParseException, JsonMappingException, IOException {

        if (StringUtils.isNotBlank(searchObject.getDate()) && StringUtils.isNotBlank(searchObject.getSearchString())) {
            LOGGER.info("Request received to find by date and text. Request contents, " + searchObject);
            return logService.findByTextAndDate(searchObject);
        } else if (StringUtils.isNotBlank(searchObject.getSearchString())) {
            LOGGER.info("Request received to find by text. Request contents, " + searchObject);
            return logService.findByLineOftext(searchObject.getSearchString());
        } else if (StringUtils.isNotBlank(searchObject.getDate())) {
            LOGGER.info("Request received to find by date. Request contents, " + searchObject);
            return logService.findByReleaseDate(searchObject.getDate());
        } else {
            LOGGER.info("Request received to find all. Request contents, " + searchObject);
            return logService.findAll();
        }
    }

    @RequestMapping("/esinfo")
    public void printEsInfo() {
        logService.printElasticSearchInfo();
    }

}
