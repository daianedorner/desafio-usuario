package com.usuario.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
@EnableWebSecurity
public class AuthorizationServerConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		JWTAuthenticationFilter customFilter = new JWTAuthenticationFilter(this.jwtTokenUtil);

		CharacterEncodingFilter filter = new CharacterEncodingFilter();
		filter.setEncoding("UTF-8");
		filter.setForceEncoding(true);
		http.addFilterBefore(filter, CsrfFilter.class);

		http.exceptionHandling().authenticationEntryPoint(new MyAuthenticationEntryPoint());
		http.csrf().disable().authorizeRequests().antMatchers("/usuario/acessar").permitAll().antMatchers("/usuario")
				.permitAll().antMatchers("/swagger-ui.html").permitAll().anyRequest().authenticated().and()
				// filtra outras requisições para verificar a presença do JWT no header
				.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
	}

}
