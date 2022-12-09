package co.epam.securitytask.configuration;

import co.epam.securitytask.infraestructure.repository.PrivilegeRepository;
import co.epam.securitytask.infraestructure.repository.RoleRepository;
import co.epam.securitytask.infraestructure.repository.UserRepository;
import co.epam.securitytask.core.model.Privilege;
import co.epam.securitytask.core.model.Role;
import co.epam.securitytask.core.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    public static final String VIEW_INFO = "VIEW_INFO";
    public static final String VIEW_ADMIN = "VIEW_ADMIN";
    boolean alreadySetup = false;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PrivilegeRepository privilegeRepository;

    private final PasswordEncoder passwordEncoder;

    public SetupDataLoader(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }
        Privilege viewInfoPrivilege = createPrivilegeIfNotFound(VIEW_INFO);
        Privilege viewAdminPrivilege = createPrivilegeIfNotFound(VIEW_ADMIN);
        createRoleIfNotFound(VIEW_ADMIN, Collections.singletonList(viewAdminPrivilege));
        createRoleIfNotFound(VIEW_INFO, Collections.singletonList(viewInfoPrivilege));
        Role userRole = roleRepository.findByName(VIEW_INFO);
        Role adminRole = roleRepository.findByName(VIEW_ADMIN);
        User adminUser = new User("admin@test.com", passwordEncoder.encode("test"), true, 0, Collections.singletonList(adminRole));
        User normalUser = new User("user@test.com", passwordEncoder.encode("test"), true, 0, Collections.singletonList(userRole));
        userRepository.save(normalUser);
        userRepository.save(adminUser);
        alreadySetup = true;
    }

    @Transactional
    public Privilege createPrivilegeIfNotFound(String name) {
        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    public void createRoleIfNotFound(String name, Collection<Privilege> privileges) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
    }
}
