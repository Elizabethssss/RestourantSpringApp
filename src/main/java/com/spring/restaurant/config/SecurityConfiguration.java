package com.spring.restaurant.config;

import com.spring.restaurant.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/styles/*", "/js/*", "/img/*", "/libs/*", "/authorization", "/login", "/signUp").permitAll()
                .antMatchers("/user/**", "/admin/**").hasAnyAuthority("ADMIN")
                .antMatchers("/user/**").hasAnyAuthority("USER")
                .anyRequest().authenticated()

                .and()
                .formLogin()
                .loginPage("/")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .successForwardUrl("/index").permitAll()

                .and()
                .logout()
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                .permitAll()

                .and()
                .exceptionHandling().accessDeniedPage("/")

                .and()
                .csrf()
                .disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(userService);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/static/**");
    }
}
