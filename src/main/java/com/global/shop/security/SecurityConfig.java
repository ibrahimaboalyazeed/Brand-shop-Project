package com.global.shop.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	
	String [] PUBLIC_END_POINTS = {"/api/v1/**"};
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
    private JwtUnAuthResponse jwtUnAuthResponse;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}
	
	   @Override
	    protected void configure(HttpSecurity http) throws Exception {
	        http.cors().and().csrf().disable()
	            .exceptionHandling()
	            .and().httpBasic()
	            .authenticationEntryPoint(jwtUnAuthResponse)
	            .and()
	            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	            .and()
	            .authorizeRequests()
	            .antMatchers(PUBLIC_END_POINTS).permitAll()
	            .antMatchers("/swagger-ui.html","/swagger-ui/**").permitAll() // Allow unauthenticated access to Swagger UI
	            .anyRequest().authenticated()
	            .and()
	            .addFilterBefore(authFilter(), UsernamePasswordAuthenticationFilter.class);
	    }
	   
	   @Override
		public void configure(WebSecurity web) throws Exception {
			web.ignoring().antMatchers("/swagger-ui/**", "/v3/api-docs/**");
		}
	   
	   
	   @Override
	    @Bean
	    protected AuthenticationManager authenticationManager() throws Exception {
	        // TODO Auto-generated method stub
	        return super.authenticationManager();
	    }

	    @Bean
	    public AuthFilter authFilter() {
	        return new AuthFilter();
	    }
	    
	    @Bean
	    public PasswordEncoder passwordEncoder() {
	    	return new BCryptPasswordEncoder();
	    }

}
