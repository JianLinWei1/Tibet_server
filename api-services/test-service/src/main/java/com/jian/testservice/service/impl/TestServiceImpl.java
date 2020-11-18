package com.jian.testservice.service.impl;

import com.jian.testservice.entity.Test;
import com.jian.testservice.entity.TestMonogo;
import com.jian.testservice.mapper.TestMapper;
import com.jian.testservice.service.TestService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service
public class TestServiceImpl  implements TestService {
    @Resource
    private TestMapper testMapper;
    @Resource
    private MongoTemplate mongoTemplate;

    @Transactional
    @Override
    public Test test() {
        TestMonogo tm = new TestMonogo();

        tm.setName("uuuu");

        mongoTemplate.insert(tm);
        Test test = testMapper.selectById(2);

        return test;
    }
}
