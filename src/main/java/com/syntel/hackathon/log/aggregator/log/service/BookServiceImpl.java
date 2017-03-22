package com.syntel.hackathon.log.aggregator.log.service;

import com.syntel.hackathon.log.aggregator.log.model.LogObject;
import com.syntel.hackathon.log.aggregator.log.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements LogService {

    private BookRepository bookRepository;

    @Autowired
    public void setBookRepository(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public LogObject save(LogObject book) {
        return bookRepository.save(book);
    }

    public void delete(LogObject book) {
        bookRepository.delete(book);
    }

    public LogObject findOne(String id) {
        return bookRepository.findOne(id);
    }

    public Iterable<LogObject> findAll() {
        return bookRepository.findAll();
    }


    public List<LogObject> findByLineOftext(String lineOftext) {
        return bookRepository.findByLineOftext(lineOftext);
    }

   
}