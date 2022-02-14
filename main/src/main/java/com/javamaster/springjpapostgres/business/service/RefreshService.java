package com.javamaster.springjpapostgres.business.service;

import com.javamaster.springjpapostgres.persistence.repository.RefreshRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RefreshService {
    @Autowired
    private final RefreshRepository refreshRepository;
    private final int CASH_COOLDOWN = 24;

    public RefreshService(RefreshRepository refreshRepository) {
        this.refreshRepository = refreshRepository;
    }
    public void refresh(){
        refreshRepository.refresh();
    }

    public Date getLastDate(){
        return refreshRepository.lastCashDate();
    }
    public double hoursAfterLog() {
        Date now = new Date();
        Date date = getLastDate();
        double diff = now.getTime() - date.getTime();
        double hours = diff / 3600000;
        return hours;
    }

    public boolean isCooldownGone() {
        System.out.println(hoursAfterLog());
        boolean isGone = hoursAfterLog() > CASH_COOLDOWN;
        return isGone;
    }

}
