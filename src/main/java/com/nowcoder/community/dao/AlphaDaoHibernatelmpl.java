package com.nowcoder.community.dao;

import org.springframework.stereotype.Repository;

@Repository("hibernate")
public class AlphaDaoHibernatelmpl implements AlphaDao{

    @Override
    public String select() {
        return "Hibernate";
    }
}
