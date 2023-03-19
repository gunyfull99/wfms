package com.wfms.security;

//import com.wfms.config.ResponseError;

import com.wfms.exception.GlobalExceptionHandler;
import com.wfms.filter.AuthorizationFilter;
import com.wfms.service.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private AuthorizationFilter authorizationFilter;
    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;


    //    @Autowired
//    private ResponseError r;
//
//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;
//
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService);
    }

    //    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(authenticationProvider());
//    }
//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userDetailsService());
//        authProvider.setPasswordEncoder(passwordEncoder());
//
//        return authProvider;
//    }
    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration/**", "/swagger-ui.html", "/webjars/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
//                .authorizeRequests()
//                .antMatchers("/users/login").permitAll()
//                .anyRequest()
//                .denyAll()
////                .and().exceptionHandling().accessDeniedHandler(((request, response, accessDeniedException) -> {
////                    globalExceptionHandler.handleConflict(response);
////                }))
//                .and().exceptionHandling().authenticationEntryPoint(((request, response, accessDeniedException) -> {
//                    globalExceptionHandler.handleAuthorization(response);
//                }))
                .authorizeHttpRequests(authorize -> authorize
                        .antMatchers("/users/login").permitAll()
                        .antMatchers("/users/sendmailpassword").permitAll()
                       // .antMatchers("/project/list").hasRole("ADMIN")/
                        .anyRequest().authenticated()).exceptionHandling().authenticationEntryPoint(((request, response, accessDeniedException) -> {
                    globalExceptionHandler.handleAuthorization(response);
                })).and()
                .logout()
                .logoutSuccessUrl("/users/login")
                .logoutUrl("/users/logout").
                clearAuthentication(true).
                invalidateHttpSession(true)
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();
        http.addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public CorsFilter corsFilter() {
//        UrlBasedCorsConfigurationSource source =
//                new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true);
//        config.addAllowedOrigin("*");
//        config.addAllowedHeader("*");
//        config.addAllowedMethod("*");
//        source.registerCorsConfiguration("/**", config);
//        return new CorsFilter((CorsConfigurationSource) source);
//    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
