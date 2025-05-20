package com.plumdevs.plumjob.config;

import com.plumdevs.plumjob.UI.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends VaadinWebSecurity {


    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        setLoginView(http, LoginView.class);
        http.formLogin(form -> form
                .successHandler((request, response, authentication) -> {
                    response.sendRedirect("/active");
                })
        ) .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
        );
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        //dataSource.setUrl("jdbc:mysql://localhost:3306/Plum");
        //dataSource.setUsername("root");
        //dataSource.setPassword("Crash1234#");
        //dataSource.setUrl("jdbc:mysql://database-2.cfosus26aipm.eu-north-1.rds.amazonaws.com/plum_test");
        //dataSource.setUsername("plum_user_1");
        //dataSource.setPassword("s&H2Lp-q$aaWr");
        dataSource.setUrl(System.getenv("DB_URL"));
        dataSource.setUsername(System.getenv("DB_USER"));
        dataSource.setPassword(System.getenv("DB_PASSWORD"));

        return dataSource;
    }

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {

        return new JdbcUserDetailsManager(dataSource);
    }
}
