

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.llh.quarts.conf;

import java.util.ArrayList;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
/**
 *
 * @author lorenzolince
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
 @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.llh.quarts"))
                .paths(PathSelectors.ant("/api/**"))
                .build()
                .apiInfo(getApiInfo());
    }

    private ApiInfo getApiInfo() {
        return new ApiInfo("Example schedule",
                "Example enable or disable schedule ",
                "1.0",
                "https://pa.linkedin.com/in/lorenzo-lince-453b5433/",
                new Contact("Lorenzo Lince", "https://pa.linkedin.com/in/lorenzo-lince-453b5433/", "lorenzolince@gmail.com"),
                "01-SNAPSHOT", null,
                new ArrayList<VendorExtension>());
    }
}
