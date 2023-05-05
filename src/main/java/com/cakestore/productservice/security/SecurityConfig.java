package com.cakestore.productservice.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;


@EnableWebSecurity
@Configuration
public class SecurityConfig{
	
	@Value("${auth0.audiences}")
	private String audience;

	@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
	private String issuer;
	
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
        .antMatchers(HttpMethod.GET,"/cakes").hasAnyAuthority("SCOPE_read:cakes","SCOPE_write:cakes")
        .antMatchers(HttpMethod.GET,"/cakes/**").hasAnyAuthority("SCOPE_read:cakes","SCOPE_write:cakes")
        .antMatchers(HttpMethod.POST,"/cakes").hasAnyAuthority("SCOPE_write:cakes")
        .antMatchers(HttpMethod.PUT,"/cakes/**").hasAnyAuthority("SCOPE_write:cakes")
        .antMatchers(HttpMethod.PATCH,"/cakes/**").hasAnyAuthority("SCOPE_write:cakes")
        .antMatchers("/swagger-ui/**").permitAll()
        .antMatchers("/h2-console/**").permitAll().and().exceptionHandling().accessDeniedHandler(new JWTAuthenticationFailureHandler())
        .and().cors()
        .and().oauth2ResourceServer().jwt();
        http.csrf().disable();
        return http.build();
    }
  
    
    @Bean
    JwtDecoder jwtDecoder() {
       
        NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder)
                JwtDecoders.fromOidcIssuerLocation(issuer);

        OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator(audience);
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);

        jwtDecoder.setJwtValidator(withAudience);

        return jwtDecoder;
    }
    
}