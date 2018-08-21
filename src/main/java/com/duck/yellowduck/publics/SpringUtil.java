package com.duck.yellowduck.publics;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;

    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException
    {
        if (applicationContext == null) {
            applicationContext = applicationContext;
        }
    }

    public static ApplicationContext getApplicationContext()
    {
        return applicationContext;
    }

    public static Object getBean(String name)
    {
        return getApplicationContext().getBean(name);
    }

    public static <T> T getBean(Class<T> clazz)
    {
        return (T)getApplicationContext().getBean(clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz)
    {
        return (T)getApplicationContext().getBean(name, clazz);
    }
}
