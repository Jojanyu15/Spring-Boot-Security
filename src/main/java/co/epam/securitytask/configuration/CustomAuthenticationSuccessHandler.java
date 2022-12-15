package co.epam.securitytask.configuration;

import co.epam.securitytask.core.model.Role;
import co.epam.securitytask.core.model.User;
import co.epam.securitytask.infraestructure.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    private final UserRepository userRepository;
    Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public CustomAuthenticationSuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        LOGGER.info("Login success for " + authentication.getName());
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(()->new UsernameNotFoundException("Not found "));
        resetRetries(user);
        if(user.getRoles().stream().map(Role::getName).toList().contains("VIEW_ADMIN")){
            redirectStrategy.sendRedirect(request,response,"/admin");
        }else{
            redirectStrategy.sendRedirect(request,response,"/info");
        }
    }

    private void resetRetries(User user) {
        user.setLoginRetries(0);
        userRepository.save(user);
    }
}
