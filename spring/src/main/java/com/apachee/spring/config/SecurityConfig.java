package com.apachee.spring.config;

import com.apachee.mysql.CustomUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Bean
    UserDetailsService customUserService(){
        return new CustomUserService();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                // 这里配置的也只是get方式提交的请求不需要拦截
                .antMatchers("/css/**", "/js/**").permitAll()
                .antMatchers("/users/**").hasAnyRole("ROLE_ADMIN","ROLE_USER")
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").successForwardUrl("/index").failureUrl("/login?error").permitAll()
                .and()
                .csrf().disable()
                // 前后端分离的csrf cookie 配置，允许js提取cookie
//                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                .and()
                .logout().permitAll();
    }

    // 配置security 登录用户，这个和application.properties 文件配置冲突
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 内存用户配置
//        auth.inMemoryAuthentication()
//                .withUser("admin").password(encoder.encode("123456")).roles("admin")
//                .and()
//                .withUser("sosog").password(encoder.encode("123456")).roles("dep")
//                .and()
//                .passwordEncoder(encoder);
        // 数据库用户配置
        auth.userDetailsService(customUserService()).passwordEncoder(encoder);
    }
}
