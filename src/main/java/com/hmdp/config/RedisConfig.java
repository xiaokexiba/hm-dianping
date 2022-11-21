package com.hmdp.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * redis配置类
 *
 * @author xoke
 * @date 2022/11/21
 */
@Configuration
public class RedisConfig {
    @Bean
    public RedissonClient redissonClient() {
        // 配置类
        Config config = new Config();
        // 添加redis地址，这里添加了单点地址
        // 可以使用config.useClusterServer()添加集群地址
        config.useSingleServer()
                .setAddress("redis://192.168.31.138:6379").setPassword("123123");
        // 创建客户端
        return Redisson.create(config);
    }
}
