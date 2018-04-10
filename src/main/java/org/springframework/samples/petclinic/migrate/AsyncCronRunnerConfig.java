/*
 * Software property of Acquisio. Copyright 2003-2018.
 */
package org.springframework.samples.petclinic.migrate;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author Gibran
 */
@Configuration
@EnableScheduling
@EnableAsync
public class AsyncCronRunnerConfig {

    @Bean(name = "ConsistencyCheckThread")
    public Executor ConsistencyCheckerThread() {
        return new ThreadPoolTaskExecutor();
    }

    @Bean(name = "ShadowWriteThread")
    public Executor ShadowWriteThread() {
        return new ThreadPoolTaskExecutor();
    }

    @Bean(name = "ShadowReadThread")
    public Executor ShadowReadThread() {
        return new ThreadPoolTaskExecutor();
    }

}
