package co.epam.securitytask.configuration;

import co.epam.securitytask.infraestructure.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Autowired
    private UserRepository userRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/user").hasRole("VIEW_INFO")
                .antMatchers("/admin").hasRole("VIEW_ADMIN")
                .antMatchers("/about").permitAll()
                .and().formLogin()
                .successHandler(new CustomAuthenticationSuccessHandler(userRepository))
                .and().logout();
        return http.build();
    }


}