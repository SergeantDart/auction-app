package com.dumitrascuantonio.auctionapp.configuration;

import com.dumitrascuantonio.auctionapp.job.LotCompletionCheckJob;
import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

@Configuration
public class QuartzSubmitJobConfig {

    private static final String CRON_PATTERN = "0 0/1 * ? * * *";

    @Bean(name = "jobCompletionCheck")
    public JobDetailFactoryBean jobCompletionCheck() {
        return QuartzConfig.createJobDetail(LotCompletionCheckJob.class, "Lot Completion Check Job");
    }

    @Bean(name = "lotCompletionCheckTrigger")
    public CronTriggerFactoryBean lotCompletionCheckTrigger(@Qualifier("jobCompletionCheck") JobDetail jobDetail) {
        return  QuartzConfig.createCronTrigger(jobDetail, CRON_PATTERN, "Lot Completion Check Trigger");
    }

}
