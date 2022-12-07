package co.epam.securitytask.core.service;

import co.epam.securitytask.core.model.User;
import co.epam.securitytask.infraestructure.repository.RoleRepository;
import co.epam.securitytask.infraestructure.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    public User registerNewUserAccount(final User accountDto) {
        if (emailExists(accountDto.getEmail())) {
            throw new IllegalArgumentException("There is an account with that email address: " + accountDto.getEmail());
        }
        final User user = new User();
        user.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        user.setEmail(accountDto.getEmail());
        user.setRoles(Collections.singletonList(roleRepository.findByName("USER")));
        return userRepository.save(user);
    }

    public void saveRegisteredUser(final User user) {
        userRepository.save(user);
    }

    public User findUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    private boolean emailExists(final String email) {
        return userRepository.findByEmail(email) != null;
    }

}