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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
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
       /* if (alreadySetup) {
            return;
        }
        Privilege viewInfoPrivilege = createPrivilegeIfNotFound("VIEW_INFO");
        Privilege viewAdminPrivilege = createPrivilegeIfNotFound("VIEW_ADMIN");
        List<Privilege> godPrivileges = Arrays.asList(viewInfoPrivilege, viewAdminPrivilege);
        createRoleIfNotFound("GOD", godPrivileges);
        createRoleIfNotFound("ADMIN", Collections.singletonList(viewAdminPrivilege));
        createRoleIfNotFound("USER", Collections.singletonList(viewInfoPrivilege));

        Role godRoles = roleRepository.findByName("GOD");
        Role userRole = roleRepository.findByName("USER");
        Role adminRole = roleRepository.findByName("ADMIN");


        User godUser = new User("god@test.com", passwordEncoder.encode("test"), true,Collections.singletonList(godRoles));
        User adminUser = new User("admin@test.com", passwordEncoder.encode("test"), true,Collections.singletonList(adminRole));
        User normalUser = new User("user@test.com", passwordEncoder.encode("test"),true, Collections.singletonList(userRole));
        System.out.println(normalUser);
        userRepository.save(normalUser);
        userRepository.save(godUser);
        userRepository.save(adminUser);
        alreadySetup = true;*/
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
    public Role createRoleIfNotFound(
            String name, Collection<Privilege> privileges) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }
}
