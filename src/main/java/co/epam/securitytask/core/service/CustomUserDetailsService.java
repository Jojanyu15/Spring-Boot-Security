package co.epam.securitytask.core.service;

import co.epam.securitytask.core.model.User;
import co.epam.securitytask.infraestructure.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Value("${security.max-failed-logins}")
    private int maxRetries;

    private final UserRepository repository;

    @Autowired
    public CustomUserDetailsService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) {
        User user = findAndGetUser(email);
        if (!user.isAccountNonLocked() && !isUserReadyToUnlock(user)) {
            throw new LockedException("El usuario ha alcanzado el máximo de intentos, porfavor espere cinco minutos");
        } else if (!user.isAccountNonLocked() && isUserReadyToUnlock(user)) {
            user.setIsNonLocked(true);
            user.setLoginRetries(0);
            repository.save(user);
        }

        if (user.getLoginRetries() >= maxRetries) {
            user.setIsNonLocked(false);
            repository.save(user);
        }
        return buildUserDetailsFromUser(user);
    }

    private static boolean isUserReadyToUnlock(User user) {
        return TimeUnit.MILLISECONDS.toMinutes(new Date().getTime() - user.getLastFailureLoginDate().getTime()) > 5;
    }

    private org.springframework.security.core.userdetails.User buildUserDetailsFromUser(User user) {
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                true,
                true,
                true,
                user.isAccountNonLocked(),
                getAuthorities(user));
    }

    private User findAndGetUser(String email) {
        return repository.findByEmail(email).orElseThrow(() -> new BadCredentialsException("Not found: " + email));
    }

    private List<? extends GrantedAuthority> getAuthorities(User user) {
        return user.getAuthorities().stream().toList();
    }

}