package co.epam.securitytask.configuration;

import co.epam.securitytask.core.model.User;
import co.epam.securitytask.infraestructure.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomAuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private final UserRepository userRepository;

    public CustomAuthenticationSuccessListener(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationSuccessListener.class);

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        LOGGER.info("Login success for "+event.getAuthentication().getName());
        Optional<User> user = userRepository.findByEmail(event.getAuthentication().getName());
        if(user.isPresent()){
            user.get().setLoginRetries(0);
            userRepository.save(user.get());
        }
    }

}
