package web.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "users")
public class User implements UserDetails {
//    public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

//    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
//    @CollectionTable(name = "roles", joinColumns = @JoinColumn(name = "user_name"))
//    @CollectionTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"))
 //   @CollectionTable(name = "users_roles", joinColumns = @JoinColumn(name = "role_id"))
//    @Enumerated(EnumType.STRING)
//    private Set<Role> roles = new HashSet<>();



    @ManyToMany
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Roles> roles;


    public User() {
    }

    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public User(String firstName, String lastName, String email, String login, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.login = login;
        this.password = password;
    }

    public User(Long id, String firstName, String lastName, String email, String login, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.login = login;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String getUsername() {
        return login;
    }

    public void setUserName(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities;
//                new ArrayList<GrantedAuthority>();

        authorities = new ArrayList<GrantedAuthority>(roles.size());

        for (Roles role : roles)
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
//        return getRoles();
        return authorities;
    }

//    public void setRoles(<Role> roles) {
//        this.roles = roles;
//    }
//
//    public Set<Roles> getRoles() {
//        return (Set<Roles>) roles;
//    }

//    public void setRoles(<Roles> roles) {
//        this.roles = roles;
//    }

    public Collection<Roles> getRoles() {
        return roles;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
