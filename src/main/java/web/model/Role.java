package web.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN, USER;

    @Override
    public String getAuthority() {
        return name();
    }

    @Override
    public String toString() {
        return name();
    }

    public String getRole() {
        return name();
    }
}
