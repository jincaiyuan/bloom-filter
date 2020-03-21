package com.rxy.redislock.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sun.nio.ch.ThreadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ThreadPoolConfig {

    @Value("${thread.core}")
    private int core;

    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(core);
    }
}
