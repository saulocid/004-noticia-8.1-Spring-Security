package com.EGG.security1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.EGG.security1.services.UsuarioServices;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurity {

        @Autowired
        public UsuarioServices usuarioServices;

        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
                auth.userDetailsService(usuarioServices).passwordEncoder(new BCryptPasswordEncoder());
        }

        // @Bean
        // public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //         http
        //                         .authorizeHttpRequests((authz) -> authz
        //                                         .requestMatchers("/**").permitAll())
        //                         .formLogin((login) -> login
        //                                         .loginPage("/login")
        //                                         .usernameParameter("Username"))
        //                         .logout((logout) -> logout
        //                                         .logoutUrl("/logout"))
        //                         .csrf((csrf) -> csrf.disable());
        //         return http.build();
        // }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                .authorizeHttpRequests((authz) ->
                    authz
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Solo los usuarios con rol ADMIN pueden acceder a /admin/**
                        .requestMatchers("/journal/**").hasRole("JOURNAL") // Solo los usuarios con rol JOURNAL pueden acceder a /journal/**
                        .requestMatchers("/user/**").hasRole("USER") // Solo los usuarios con rol USER pueden acceder a /user/**
                        .anyRequest().permitAll() // Permitir el acceso a todas las demás rutas
                )
                .formLogin((login) ->
                    login
                        .loginPage("/login")
                        .usernameParameter("Username")
                )
                .logout((logout) -> logout
                    .logoutUrl("/logout"))
                .csrf((csrf) -> csrf.disable());
            return http.build();
        }

}
