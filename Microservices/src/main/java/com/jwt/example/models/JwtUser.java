package com.jwt.example.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString   
@Entity
@Table(name= "user_table")

public class JwtUser implements UserDetails {


    @Id
    @JsonProperty(access = Access.WRITE_ONLY)
    private String  userId;

    @JsonProperty(access = Access.WRITE_ONLY)
    private String name;

    @JsonProperty(access = Access.WRITE_ONLY)
    private String email;

    @JsonProperty(access = Access.WRITE_ONLY)
    private String password;

    private String about;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    @JsonProperty(access = Access.WRITE_ONLY)
    public String getUsername() {
        return this.email;
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
}
