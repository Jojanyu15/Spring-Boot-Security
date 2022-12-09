package co.epam.securitytask.configuration;

import co.epam.securitytask.core.model.User;
import co.epam.securitytask.infraestructure.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.security.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;

@Component
public class CustomAuthenticationFailureHandler implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
    private final Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationFailureHandler.class);
    private final UserRepository userRepository;

    public CustomAuthenticationFailureHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        LOGGER.info("Bad credentials, adding retry...");
        User user = getByEmail(event);
        if (user.isAccountNonLocked()) {
            user.setLoginRetries(user.getLoginRetries() + 1);
            user.setLastFailureLoginDate(new Date(event.getTimestamp()));
            userRepository.save(user);
        }
    }

    private User getByEmail(AuthenticationFailureBadCredentialsEvent event) {
        return userRepository.findByEmail(event.getAuthentication().getName()).orElseThrow(() -> new UsernameNotFoundException("Not found"));
    }

}
