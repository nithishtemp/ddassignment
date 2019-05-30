package com.dd.config;

import java.util.ArrayList;

import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2; 


@Configuration
@EnableSwagger2
public abstract class BaseSwaggerConfiguration implements EnvironmentAware  {  

	public static final String DEFAULT_INCLUDE_PATTERN = "/.*"; 

	private RelaxedPropertyResolver propertyResolver; 

    public void setEnvironment(Environment environment) { 
        this.propertyResolver = new RelaxedPropertyResolver(environment, "swagger."); 
    }
	
    @Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .apiInfo(apiInfo())
          .select()
          .apis(RequestHandlerSelectors.basePackage(getBasePackageName()))   
          .paths(PathSelectors.regex(DEFAULT_INCLUDE_PATTERN))
          .build();                                           
    }
    

    public abstract String getBasePackageName();
    
    
    public ApiInfo apiInfo() { 
    	
    	@SuppressWarnings("rawtypes")
		ApiInfo apiinfo=	new ApiInfo( 
                propertyResolver.getProperty("title"), 
                propertyResolver.getProperty("description"), 
                "v1", 
                propertyResolver.getProperty("termsOfServiceUrl"), 
                new Contact(propertyResolver.getProperty("contact.name"), 
                		propertyResolver.getProperty("contact.url"),
                		propertyResolver.getProperty("contact.email")), 	
                propertyResolver.getProperty("license"), 
                propertyResolver.getProperty("licenseUrl"), new ArrayList<VendorExtension>());    	
    	return apiinfo; 
    } 
}