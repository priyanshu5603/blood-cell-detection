package com.scm.majProj.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter; 

@Entity(name = "user")
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails {
    @Column(name = "user_name", nullable = false)
    private String firstName;
    private String lastName;
    @Id
    private String userId;
    private String password;
    private String email;
    private String phoneNumber;
    private boolean enabled = true;
    private boolean emailVerified = false;
    private boolean phoneVerified = false;

    @Enumerated(value = EnumType.STRING)
    private Providers provider = Providers.SELF;
    private String providerId = null;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roleList = new ArrayList<>();
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //makes decision on what roles to give to the user
        //admin or user
        Collection<SimpleGrantedAuthority> roles =  roleList.stream().map(role-> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
        return roles;
    }
    @Override
    public String getUsername() {
       return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }
    @Override 
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public String getPassword() {
        return this.password;
    }
    
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    
}
