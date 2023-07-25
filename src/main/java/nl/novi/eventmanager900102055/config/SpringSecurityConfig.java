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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    @Bean
    protected SecurityFilterChain filter(HttpSecurity http) throws Exception {

        http
                .httpBasic().disable()
                .cors().and()
                .csrf().disable()
                .authorizeHttpRequests()

                // Uncomment for developer mode
//                .requestMatchers("/**").permitAll()

                .requestMatchers("/authenticate").permitAll()
                .requestMatchers("/authenticated").permitAll()
                .requestMatchers(HttpMethod.GET, "/events/*").hasAnyRole("ADMIN", "USER")
                .requestMatchers("/artists/**", "/transactions/**", "/locations/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/users/**", "/tickets/**", "/events/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/users/create_user", "/tickets/create_ticket", "/tickets/tickets_user", "/events/find_event_by_name").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.POST, "/users/**", "/tickets/**", "/events/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/users/update_user").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.PUT, "/users/**", "/tickets/**", "/events/**").hasAnyRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/users/delete_user").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.DELETE, "/users/**", "/tickets/**", "/events/**").hasRole("ADMIN")

                .anyRequest().denyAll()
                .and()
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

}