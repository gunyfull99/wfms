package com.wfms.security;

//import com.wfms.config.ResponseError;

import com.google.firebase.database.util.JsonMapper;
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
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true ,securedEnabled = true,
        jsr250Enabled = true)
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
        auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder());
    }

//        @Override
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
        http.cors().and().csrf().disable().exceptionHandling().authenticationEntryPoint(((request, response, accessDeniedException) -> {
                    globalExceptionHandler.handleAuthorization(response);
                })).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeHttpRequests(authorize -> authorize
                        .antMatchers("/users/login").permitAll()
                        .antMatchers("/users/sendmailpassword").permitAll()
                        .antMatchers("/project/list").hasAnyAuthority("ADMIN")
                        .antMatchers("/project/list-by-lead").hasAnyAuthority("ADMIN","PM")
                        .antMatchers("/project/create-project").hasAnyAuthority("ADMIN")
                        .antMatchers("/project/update-project").hasAnyAuthority("ADMIN")
                        .antMatchers("/project/start").hasAnyAuthority("ADMIN","PM")
                        .antMatchers("/project/close").hasAnyAuthority("ADMIN","PM")
                        .antMatchers("/project/remove-user-in-project").hasAnyAuthority("ADMIN")
                        .antMatchers("/project/add-user-to-project").hasAnyAuthority("ADMIN")
                        .antMatchers("/workflow-step/create-work-flow-step").hasAnyAuthority("PM")
                        .antMatchers("/workflow-step/update-work-flow-step").hasAnyAuthority("PM")
                        .antMatchers("/workflow/create-work-flow").hasAnyAuthority("PM")
                        .antMatchers("/workflow/update-work-flow").hasAnyAuthority("PM")
                        .antMatchers("/sprint/create-sprint").hasAnyAuthority("PM")
                        .antMatchers("/sprint/update-sprint").hasAnyAuthority("PM")
                        .antMatchers("/sprint/complete-sprint").hasAnyAuthority("PM")
                        .antMatchers("/users/admin/changepass").hasAnyAuthority("ADMIN")
                        .antMatchers("/users/role/save").hasAnyAuthority("ADMIN")
                        .antMatchers("/users/role/addtoUsers").hasAnyAuthority("ADMIN")
                        .antMatchers("/users/role/deleteroleUsers").hasAnyAuthority("ADMIN")
                        .antMatchers("/users/blockusers").hasAnyAuthority("ADMIN")

                        .anyRequest().authenticated());
        http.addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource()
    {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
