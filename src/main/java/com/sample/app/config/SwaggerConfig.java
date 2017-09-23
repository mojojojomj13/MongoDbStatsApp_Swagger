package com.sample.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
    public Docket transactionsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.sample.app.controller"))
                .paths(regex("/.*"))
                .build()
                .apiInfo(metaData());
    }
	
    private ApiInfo metaData() {
        ApiInfo apiInfo = new ApiInfo(
                "Transactions API",
                "Transactions API for getting and saving Transaction data and statistics",
                "1.0",
                "Terms of service",
                new Contact("Prithvish Mukherjee", "http://localhost:8080/swagger-ui.html", "mojo.jojo.mj13@gmail.com"),
               "Transactions API",
                "http://localhost:8080/v2/api-docs");
        return apiInfo;
    }
}
