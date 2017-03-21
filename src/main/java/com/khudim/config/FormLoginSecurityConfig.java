package com.khudim.config;


import com.khudim.dao.person.PersonRole;
import com.khudim.dao.person.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class FormLoginSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PersonService personService;

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/").hasAnyRole(PersonRole.ADMIN.role(), PersonRole.USER.role()).antMatchers("/admin/*").hasRole(PersonRole.ADMIN.role())
                .and()
                .formLogin()
                .loginPage("/login").permitAll().successForwardUrl("/login")
                .and()
                .rememberMe().rememberMeParameter("remember-me").rememberMeCookieName("remember-me").tokenRepository(persistentTokenRepository())
                .and()
                .csrf().disable()
                .logout()
                .logoutSuccessUrl("/login?logout");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
        auth.userDetailsService(personService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
        db.setDataSource(dataSource);
        return db;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(personService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationTrustResolver getAuthenticationTrustResolver() {
        return new AuthenticationTrustResolverImpl();
    }

    @Bean
    public SavedRequestAwareAuthenticationSuccessHandler
    savedRequestAwareAuthenticationSuccessHandler() {
        SavedRequestAwareAuthenticationSuccessHandler auth = new SavedRequestAwareAuthenticationSuccessHandler();
        auth.setTargetUrlParameter("targetUrl");
        return auth;
    }

}
