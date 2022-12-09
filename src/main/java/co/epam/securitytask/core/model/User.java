package co.epam.securitytask.core.model;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String email;
    private String password;
    private boolean isNonLocked;
    private int loginRetries;
    private Date lastFailureLoginDate;
    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;



    public User(String email, String password, boolean isNonLocked, Collection<Role> roles) {
        this.email = email;
        this.password = password;
        this.isNonLocked = isNonLocked;
        this.roles = roles;
    }

    public User(String email, String password, boolean isNonLocked, int loginRetries, Collection<Role> roles) {
        this.email = email;
        this.password = password;
        this.isNonLocked = isNonLocked;
        this.loginRetries = loginRetries;
        this.roles = roles;
    }

    public User() {
    }
    public Date getLastFailureLoginDate() {
        return lastFailureLoginDate;
    }

    public void setLastFailureLoginDate(Date lastLogin) {
        this.lastFailureLoginDate = lastLogin;
    }
    public void setIsNonLocked(boolean locked) {
        this.isNonLocked = locked;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList();
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public int getLoginRetries() {
        return loginRetries;
    }

    public void setLoginRetries(int loginRetries) {
        this.loginRetries = loginRetries;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                '}';
    }
}