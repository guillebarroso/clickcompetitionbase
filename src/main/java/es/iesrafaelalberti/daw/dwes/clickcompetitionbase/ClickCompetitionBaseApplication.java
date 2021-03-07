package es.iesrafaelalberti.daw.dwes.clickcompetitionbase;

import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.security.JWTAuthorizationFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@SpringBootApplication
public class ClickCompetitionBaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClickCompetitionBaseApplication.class, args);
    }

    @EnableWebSecurity
    @Configuration
    class WebSecurityConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
//          Solo puse algunas urls de prueba para comprobar que funcionaba
            http.cors().and().csrf().disable()
                    .addFilterAfter(new JWTAuthorizationFilter(getApplicationContext()), UsernamePasswordAuthenticationFilter.class)
                    .authorizeRequests()
                    .antMatchers("/login/**").permitAll()
                    .antMatchers("/logout/**").permitAll()
                    .antMatchers("/players/**").authenticated()
                    .antMatchers("/add/team/**").hasAnyRole("ADMIN")
                    .antMatchers("/").permitAll();

        }

        @Bean
        public PasswordEncoder getPasswordEncoder() {
            return new BCryptPasswordEncoder();
        }

    }

}