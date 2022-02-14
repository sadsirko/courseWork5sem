package com.javamaster.springjpapostgres.business.service;

import com.javamaster.springjpapostgres.persistence.repository.RefreshRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
public class RefreshService {
    @Autowired
    private final RefreshRepository refreshRepository;


    public RefreshService(RefreshRepository refreshRepository) {
        this.refreshRepository = refreshRepository;
    }
    public void refresh(){
        refreshRepository.refresh();
    }

    public Date getLastDate(){
        return refreshRepository.lastCashDate();
    }
}
