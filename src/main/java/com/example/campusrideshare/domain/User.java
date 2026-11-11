package com.example.campusrideshare.domain;

import jakarta.persistence.*;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="users")
@NullMarked
public class User implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable=false, unique=true)
    private String username;
    @Column(nullable=false)
    private String password;
    @Column(nullable=false)
    private String fullName;
    @Column(nullable=false)
    private String email;
    @Column(nullable=false)
    private Department department;
    @Column(nullable=false)
    private String role;

    public User() {}

    public User(String username, String password, String fullName, String email, Department department, String role) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.department = department;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {return List.of(new SimpleGrantedAuthority(role));}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public @Nullable String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        return id != null && id.equals(((User) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "User{" +
                ", Name='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", department='" + department + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
