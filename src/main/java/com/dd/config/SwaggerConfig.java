package com.dd.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dd.DeloitteassignmentApplication;

import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
public class SwaggerConfig  extends BaseSwaggerConfiguration{  
	
	@Override
	public String getBasePackageName() {
		return DeloitteassignmentApplication.class.getPackage().getName();
	}

	@Bean
	UiConfiguration uiConfig() {
		return new UiConfiguration("validatorUrl", 
				"none",
				"alpha", 
				"schema",
				UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS, 
				false, 
				true, 
				60000L); 
	}
}