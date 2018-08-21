package com.duck.yellowduck.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Profile({"dev", "test"})
public class MySwagger2
        extends WebMvcConfigurerAdapter
{
    @Bean
    public Docket createRestApi()
    {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select().apis(RequestHandlerSelectors.basePackage("pro.bechat.wallet")).paths(PathSelectors.any()).build();
    }

    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        registry.addResourceHandler(new String[] { "/**" }).addResourceLocations(new String[] { "classpath:/META-INF/resources/" }).setCachePeriod(Integer.valueOf(0));
        registry.addResourceHandler(new String[] { "/static/**" }).addResourceLocations(new String[] { "classpath:/static/" }).setCachePeriod(Integer.valueOf(0));
    }

    private ApiInfo apiInfo()
    {
        return new ApiInfoBuilder().title("Spring Boot������Swagger2����RESTful APIs").description("����Spring Boot����������������http://blog.didispace.com/").termsOfServiceUrl("http://blog.didispace.com/").contact("������DD").version("1.0").build();
    }
}
