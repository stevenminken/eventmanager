package nl.novi.eventmanager900102055.config;

import nl.novi.eventmanager900102055.filter.JwtRequestFilter;
import nl.novi.eventmanager900102055.services.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    public final CustomUserDetailsService customUserDetailsService;

    private final JwtRequestFilter jwtRequestFilter;

    public SpringSecurityConfig(CustomUserDetailsService customUserDetailsService, JwtRequestFilter jwtRequestFilter) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    // PasswordEncoderBean. Deze kun je overal in je applicatie injecteren waar nodig.
    // Je kunt dit ook in een aparte configuratie klasse zetten.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Authenticatie met customUserDetailsService en passwordEncoder
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }
    // Authorizatie met jwt
    @Bean
    protected SecurityFilterChain filter (HttpSecurity http) throws Exception {

        http
                .httpBasic().disable()
                .cors().and()
                .authorizeHttpRequests()

                // Wanneer je deze uncomments, staat je hele security open. Je hebt dan alleen nog een jwt nodig.
                .requestMatchers("/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/users").permitAll()

                .requestMatchers("/authenticate").permitAll()
                .requestMatchers("/authenticated").authenticated()

                .requestMatchers(HttpMethod.GET, "/users/**", "/events/**", "/tickets/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.POST, "/events/**", "/tickets/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.PUT, "/users/**", "/events/**", "/tickets/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.DELETE, "/users/**", "/events/**", "/tickets/**").hasAnyRole("ADMIN", "USER")

                .requestMatchers(HttpMethod.GET, "/users/**", "/transactions/**", "/events/**", "/locations/**", "/artists/**", "/tickets/**")
                .hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/users/**", "/transactions/**", "/events/**", "/locations/**", "/artists/**", "/tickets/**")
                .hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/users/**", "/transactions/**", "/events/**", "/locations/**", "/artists/**", "/tickets/**")
                .hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/users/**", "/transactions/**", "/events/**", "/locations/**", "/artists/**", "/tickets/**")
                .hasRole("ADMIN")

                .anyRequest().denyAll()
                .and()
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

}