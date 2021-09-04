package com.usuario.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.servlet.HandlerMapping;

public class JWTAuthenticationFilter extends GenericFilterBean {

	private JwtTokenUtil jwtTokenUtil;

	public JWTAuthenticationFilter(JwtTokenUtil tokenProvider) {
		this.jwtTokenUtil = tokenProvider;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		String jwt = this.resolveToken(httpServletRequest);

		Long id = getParameterId(httpServletRequest);

		if (StringUtils.hasText(jwt)) {
			if (this.jwtTokenUtil.validateToken(jwt, id)) {
				Authentication authentication = JwtTokenUtil.getAuthentication((HttpServletRequest) request);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}

		chain.doFilter(request, response);

		this.resetAuthenticationAfterRequest();
	}

	private void resetAuthenticationAfterRequest() {
		SecurityContextHolder.getContext().setAuthentication(null);
	}

	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			String jwt = bearerToken.substring(7, bearerToken.length());
			return jwt;
		}
		return null;
	}

	private Long getParameterId(HttpServletRequest request) {
		Long id = null;
		String urlTail = new AntPathMatcher().extractPathWithinPattern("/{id}/**", request.getRequestURI());
		if (urlTail != null && !urlTail.isEmpty() && urlTail.indexOf("consultar") >= 0) {
			urlTail = urlTail.replace("consultar/", "");
			id = Long.valueOf(urlTail);
		}
		return id;
	}

}
