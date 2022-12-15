package co.epam.securitytask.configuration;

import co.epam.securitytask.core.model.User;
import co.epam.securitytask.infraestructure.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class CustomAuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
    private final Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationFailureListener.class);
    private final UserRepository userRepository;

    public CustomAuthenticationFailureListener(UserRepository userRepository) {
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
