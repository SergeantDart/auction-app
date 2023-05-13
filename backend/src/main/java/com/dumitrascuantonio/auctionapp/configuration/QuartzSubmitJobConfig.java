package com.dumitrascuantonio.auctionapp.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

@Configuration
public class QuartzSubmitJobConfig {

    private static final String CRON_PATTERN = "0 0/1 * ? * * *";

    public JobDetailFactoryBean jobCompletionCheck() {
        return null;
    }

    public CronTriggerFactoryBean lotCompletionCheckTrigger() {
        return null;
    }
}
