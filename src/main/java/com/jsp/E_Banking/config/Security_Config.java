package com.jsp.E_Banking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class Security_Config {
	
	private String[] swaggerPaths = { "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**", "/webjars/**" };
	
	
    @Bean
	SecurityFilterChain security(HttpSecurity httpSecurity)throws Exception{
    	httpSecurity.authorizeHttpRequests(x->x.requestMatchers(swaggerPaths).permitAll().anyRequest().authenticated());
		httpSecurity.formLogin(x->x.disable());//from login disabling
		httpSecurity.httpBasic(x->x.disable());// basic login pop msg disabled
		return httpSecurity.build();
	}

}
