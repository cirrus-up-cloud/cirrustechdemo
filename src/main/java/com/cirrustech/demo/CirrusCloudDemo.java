package com.cirrustech.demo;

import com.google.common.base.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

@SpringBootApplication
@EnableSwagger2
public class CirrusCloudDemo {

    private static final Logger LOG = LoggerFactory.getLogger(CirrusCloudDemo.class);

    public static void main(String[] args) {

        LOG.info("Starting application... ");
        LOG.info("Active Spring profile: " + System.getProperty("spring.profiles.active"));
        SpringApplication.run(CirrusCloudDemo.class, args);
    }

    @Bean
    public Docket createDocketForSwaggerIntegration() {

        return new Docket(DocumentationType.SWAGGER_2)
                .select().apis(RequestHandlerSelectors.any())
                .paths(getPaths()).build()
                .useDefaultResponseMessages(false);
    }

    public Predicate<String> getPaths() {

        return or(regex("/hello*"),
                regex("/upload*"));
    }
}