package com.duck.yellowduck.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;

@Configuration
@PropertySource({"classpath:wallet.properties"})
@ConfigurationProperties(prefix="com.duck.yellowduck")
@Order(5)
public class AppConfig {}
