package com.doan.AppTuyenDung.Config;


import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import org.springframework.web.filter.OncePerRequestFilter;

import com.doan.AppTuyenDung.Services.JWTUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class JWTAuthFilter extends OncePerRequestFilter {
	@Autowired 
	private JWTUtils jwtUtils;
	
	@Autowired
    private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		final String autheHeader = request.getHeader("Authorization"); 
		final String jwtToken;
        final String phoneNumber;
		
        if (autheHeader == null || !autheHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		jwtToken = autheHeader.substring(7);
        phoneNumber = jwtUtils.extractUserName(jwtToken);
		
        if(phoneNumber != null && SecurityContextHolder.getContext().getAuthentication()==null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(phoneNumber);
			
			if(jwtUtils.isTokenValid(jwtToken, userDetails)) {
				SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
				UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				securityContext.setAuthentication(token);
				SecurityContextHolder.setContext(securityContext);
			}

            logger.info("Authorization Header: " + autheHeader);
            logger.info("Extracted phoneNumber: " + phoneNumber);
            logger.info("Is Token Valid: " + jwtUtils.isTokenValid(jwtToken, userDetails));
		}
		filterChain.doFilter(request, response);
	}
	
}