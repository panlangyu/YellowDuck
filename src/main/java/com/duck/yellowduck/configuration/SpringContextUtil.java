package com.duck.yellowduck.configuration;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtil
        implements ApplicationContextAware
{
    private static ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext)
    {
        applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext()
    {
        return applicationContext;
    }

    public static Object getBean(String name)
            throws BeansException
    {
        return applicationContext.getBean(name);
    }

    public static Object getBean(Class clazz)
            throws BeansException
    {
        return applicationContext.getBean(clazz);
    }
}
