package com.kry.servicepoller.configuration;

import com.kry.servicepoller.service.ServicePollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableScheduling
public class SchedulingConfiguration implements SchedulingConfigurer {
    /* Configure Dynamic Scheduler with a Delay from Properties  */

    @Autowired
    private ServicePollService servicePoolService;

    @Bean
    public Executor taskExecutor() {
        return Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * configureTasks : Used for Polling the services with a delay set in properties
     *
     * @param taskRegistrar
     */

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
        taskRegistrar.addTriggerTask(
                new Runnable() {
                    @Override
                    public void run() {
                        servicePoolService.poolService();
                    }
                },
                new Trigger() {
                    @Override
                    public Date nextExecutionTime(TriggerContext context) {
                        Optional<Date> lastCompletionTime =
                                Optional.ofNullable(context.lastCompletionTime());
                        Instant nextExecutionTime =
                                lastCompletionTime.orElseGet(Date::new).toInstant()
                                        .plusMillis(servicePoolService.getDelay());
                        return Date.from(nextExecutionTime);
                    }
                }
        );
    }
}
