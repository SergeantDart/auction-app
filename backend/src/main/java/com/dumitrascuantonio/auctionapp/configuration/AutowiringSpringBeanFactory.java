package com.dumitrascuantonio.auctionapp.configuration;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

public class AutowiringSpringBeanFactory extends SpringBeanJobFactory implements ApplicationContextAware {

    @Autowired
    private transient AutowireCapableBeanFactory autowireCapableBeanFactory;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
    }

    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        final Object job = super.createJobInstance(bundle);
        autowireCapableBeanFactory.autowireBean(job);
        return job;
    }
}
