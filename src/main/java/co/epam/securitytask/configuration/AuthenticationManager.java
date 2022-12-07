package co.epam.securitytask.configuration;

import co.epam.securitytask.infraestructure.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
public class AuthenticationManager {
    @Autowired
    private DataSource dataSource;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth, PasswordEncoder passwordEncoder) throws Exception {
        auth.jdbcAuthentication().passwordEncoder(passwordEncoder).dataSource(dataSource);
             /*   .withUser(User.withUsername("admin@test.com")
                        .password("test")
                        .roles("ADMIN"))
                .withUser(User.withUsername("user@test.com")
                        .password("test")
                        .roles("USER"));
                /*.usersByUsernameQuery("select email,password "
                        + "from user "
                        + "where email = ?")
                .authoritiesByUsernameQuery("select user.email, role.name from user " +
                        "inner join users_roles on users_roles.user_id = user.id inner join role on users_roles.role_id = role.id");*/
    }
}
