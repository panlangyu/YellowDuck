package com.duck.yellowduck.configuration;

import com.duck.yellowduck.interceptor.TokenInterceptor;
import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig
        extends WebMvcConfigurerAdapter
{
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        registry.addResourceHandler(new String[] { "/admin/**" }).setCacheControl(CacheControl.maxAge(1L, TimeUnit.SECONDS).cachePublic());
    }

    @Bean
    public HandlerInterceptor getTokenHeader()
    {
        return new TokenInterceptor();
    }
}
