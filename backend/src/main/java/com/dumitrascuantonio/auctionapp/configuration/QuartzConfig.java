package com.dumitrascuantonio.auctionapp.configuration;

import java.util.Calendar;
import java.util.Properties;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronTrigger;
import org.quartz.DailyTimeIntervalTrigger;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

@Configuration
@Slf4j
public class QuartzConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private DataSource dataSource;

    @Bean
    public SpringBeanJobFactory springBeanJobFactory() {
        AutowiringSpringBeanFactory jobFactory = new AutowiringSpringBeanFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }
    @Bean
    public SchedulerFactoryBean scheduler (Trigger... triggers) {
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        Properties properties = new Properties();
        properties.setProperty("org.quartz.scheduler.instanceName", "instanceName");
        properties.setProperty("org.quartz.scheduler.instanceId", "instance");
        schedulerFactory.setConfigLocation(new ClassPathResource("quartz.properties"));
        schedulerFactory.setOverwriteExistingJobs(true);
        schedulerFactory.setAutoStartup(true);
        schedulerFactory.setQuartzProperties(properties);
        schedulerFactory.setDataSource(dataSource);
        schedulerFactory.setJobFactory(springBeanJobFactory());
        schedulerFactory.setWaitForJobsToCompleteOnShutdown(true);
        schedulerFactory.setTriggers(triggers);
        return schedulerFactory;
    }

    private static SimpleTriggerFactoryBean createTrigger(JobDetail jobDetail, Long pollFrequencyMs, String triggerName) {
        SimpleTriggerFactoryBean simpleTriggerFactory = new SimpleTriggerFactoryBean();
        simpleTriggerFactory.setJobDetail(jobDetail);
        simpleTriggerFactory.setStartDelay(0L);
        simpleTriggerFactory.setRepeatInterval(pollFrequencyMs);
        simpleTriggerFactory.setName(triggerName);
        simpleTriggerFactory.setRepeatCount(DailyTimeIntervalTrigger.REPEAT_INDEFINITELY);
        simpleTriggerFactory.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);
        return simpleTriggerFactory;
    }

    public static CronTriggerFactoryBean createCronTrigger(JobDetail jobDetail, String cronExpression, String triggerName) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        CronTriggerFactoryBean cronTriggerFactory = new CronTriggerFactoryBean();
        cronTriggerFactory.setJobDetail(jobDetail);
        cronTriggerFactory.setCronExpression(cronExpression);
        cronTriggerFactory.setStartTime(calendar.getTime());
        cronTriggerFactory.setStartDelay(0L);
        cronTriggerFactory.setName(triggerName);
        cronTriggerFactory.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
        return cronTriggerFactory;
    }

    public static JobDetailFactoryBean createJobDetail(Class jobClass, String jobName) {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setName(jobName);
        jobDetailFactory.setJobClass(jobClass);
        jobDetailFactory.setDurability(true);
        return jobDetailFactory;
    }

}
