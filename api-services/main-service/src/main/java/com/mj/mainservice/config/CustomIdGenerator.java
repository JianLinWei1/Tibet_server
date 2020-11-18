package com.mj.mainservice.config;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.jian.common.util.SnowFlake;
import org.springframework.stereotype.Component;

/**
 * @auther JianLinWei
 * @date 2020-10-04 14:28
 */
@Component
public class CustomIdGenerator implements IdentifierGenerator {
    private final SnowFlake snowFlake = new SnowFlake(1, 1);

    @Override
    public Long nextId(Object entity) {

        return snowFlake.nextId();
    }
}
