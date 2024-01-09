package com.proyecto.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SpringBootSecurity implements WebSecurityConfigurer {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public void init(SecurityBuilder builder) throws Exception {}

    @Override
    public void configure(SecurityBuilder builder) throws Exception {
        HttpSecurity http = builder.http();

        configurarAutenticación(http);
        configurarAutorización(http);
    }

    private void configurarAutenticación(HttpSecurity http) throws Exception {
        http.formLogin(Customizer.withDefaults())
                .loginPage("/usuario/login")
                .permitAll()
                .defaultSuccessUrl("/usuario/acceder");
    }

    private void configurarAutorización(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .antMatchers("/administrador/**").hasRole("ADMIN")
                .antMatchers("/productos/**").hasRole("ADMIN")
                .anyRequest().authenticated()  // Requerir autenticación para todas las demás solicitudes
        ;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
