package com.sda.auction.config;

import com.sda.auction.service.UserDetailsSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // == fields ==
    private final UserDetailsSecurityService userDetailsSecurityService;

    // == constructor ==
    @Autowired
    public SecurityConfig(UserDetailsSecurityService userDetailsSecurityService) {
        this.userDetailsSecurityService = userDetailsSecurityService;
    }

    // == methods ==
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .authorizeRequests()
                .antMatchers("/register").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/aroma-template/**").permitAll()
                .antMatchers("/gif/**").permitAll()
                .antMatchers("/addProduct").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login")
                .defaultSuccessUrl("/home") // will redirect to home page after login
                .and()
//                .rememberMe()
//                    .key("rem-me-key")
//                    .rememberMeParameter("remember") // it is the name of the checkbox
//                    .rememberMeCookieName("remember-me-cookie") // remember-me-cookie must be same as deleteCookie
//                    .tokenValiditySeconds(6000)
//                    .and()
                .logout()
//                    .deleteCookies("remember-me-cookie")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login")
                .permitAll();

    }

    @Autowired
    public void globalConfig(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsSecurityService)
                .passwordEncoder(bCryptPasswordEncoder());
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
