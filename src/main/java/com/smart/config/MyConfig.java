package com.smart.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class MyConfig {
	
	@Bean
	public UserDetailsService getUserDetailsService() {
		
		return new UserDetailServiceImpl();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider= new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(this.getUserDetailsService());
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		
		return daoAuthenticationProvider;
		
	}
	
	
	
	 @Bean
	 public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	         http
         .authorizeHttpRequests(authorize -> authorize
	                .requestMatchers("/admin/**").hasRole("ADMIN")
	                .requestMatchers("/user/**").hasRole("USER")
	                .anyRequest().permitAll()
	            )
	            .formLogin(form->form
	            		.loginPage("/signin")//custom login page
	            		.loginProcessingUrl("/dologin")
	            		.defaultSuccessUrl("/user/index")
	            		) 
	            .csrf(csrf -> csrf.disable());;

//	            http.csrf().disable()
//	            .authorizeHttpRequests()
//	            .requestMatchers("/admin/**").hasRole("ADMIN")
//	            .requestMatchers("/user/**").hasRole("USER")
//	            .anyRequest().permitAll()
//	            .and()
//	            .formLogin();
	        return http.build();
	    }


}