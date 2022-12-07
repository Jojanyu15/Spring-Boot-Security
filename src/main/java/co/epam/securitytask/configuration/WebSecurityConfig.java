package co.epam.securitytask.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/admin/**")
                .hasRole("ADMIN")
                .antMatchers("/about")
                .hasRole("USER")
                .antMatchers("/login")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/perform_login")
                .defaultSuccessUrl("/about", true)
                .failureUrl("/login?error=true")
               // .failureHandler(AuthenticationFailureListener())
                .and()
                .logout()
                .logoutUrl("/index")
                .deleteCookies("JSESSIONID");
               // .logoutSuccessHandler(logoutSuccessHandler());
      /*  http.formLogin(form ->
                        form.loginPage("/login")
                                .failureUrl("/login-error").permitAll())
                .authorizeRequests()
                .antMatchers("/admin").hasRole("ADMIN")
                .antMatchers("/about").hasAnyRole("USER", "ADMIN", "GOD")
                .antMatchers("/user").hasRole("USER")
                .anyRequest().authenticated()
                .and().httpBasic()
                .and()
                .exceptionHandling()
                .accessDeniedPage("/403.html")
                .and()
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutSuccessUrl("/index.html");*/
        return http.build();
    }


}