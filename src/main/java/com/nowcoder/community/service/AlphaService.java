package com.nowcoder.community.service;

import com.nowcoder.community.dao.AlphaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class AlphaService {
    @Autowired
    private AlphaDao alphaDao;

    public AlphaService() {
        System.out.println("construction alphaservice");
    }

    @PostConstruct
    public void init() {
        System.out.println("initilization alphaservice");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("Destroy alphaservice");
    }

    public String find() {
        return alphaDao.select();
    }




}
