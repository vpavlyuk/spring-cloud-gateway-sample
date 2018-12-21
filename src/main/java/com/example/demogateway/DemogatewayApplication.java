package com.example.demogateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.handler.RoutePredicateHandlerMapping;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

@RestController
@SpringBootApplication
public class DemogatewayApplication {

	@Bean
	public CorsConfiguration corsConfiguration(RoutePredicateHandlerMapping routePredicateHandlerMapping) {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowedOrigins(Collections.unmodifiableList(Arrays.asList(CorsConfiguration.ALL)));
		corsConfiguration.setAllowedMethods(Arrays.asList(
				HttpMethod.POST.name(),
				HttpMethod.GET.name(),
				HttpMethod.OPTIONS.name(),
				HttpMethod.DELETE.name(),
				HttpMethod.PUT.name(),
				HttpMethod.PATCH.name()
		));
		corsConfiguration.setAllowedHeaders(Collections.unmodifiableList(Arrays.asList(CorsConfiguration.ALL)));
		corsConfiguration.setMaxAge(3600L);
		corsConfiguration.setAllowCredentials(true);
		routePredicateHandlerMapping.setCorsConfigurations(
				new HashMap<String, CorsConfiguration>() {{ put("/**", corsConfiguration); }});
		return corsConfiguration;
	}

	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		//@formatter:off
		return builder.routes()
				.route("path_backend", r -> r.path("/v2/version")
						.uri("http://localhost:38492/v2/version"))
				.route("path_httpbin", r -> r.path("/get")
						.uri("http://httpbin.org/get"))
				.build();
		//@formatter:on
	}

	@Bean
	SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) throws Exception {
		return http.httpBasic().and()
				.csrf().disable()
				.authorizeExchange()
				.pathMatchers("/anything/**").authenticated()
				.anyExchange().permitAll()
				.and()
				.build();
	}

	@Bean
	public MapReactiveUserDetailsService reactiveUserDetailsService() {
		UserDetails user = User.withDefaultPasswordEncoder().username("user").password("password").roles("USER").build();
		return new MapReactiveUserDetailsService(user);
	}

	public static void main(String[] args) {
		SpringApplication.run(DemogatewayApplication.class, args);
	}
}
