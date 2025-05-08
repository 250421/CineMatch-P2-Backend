package com.revature.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name="user", schema = "p2")
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="username", nullable = false, unique = true)
    private String username;

    @Column(name="password", nullable = false)
    private String password;

    @Column(name="role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(Integer id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        return Objects.equals(getId(), other.getId())
                && Objects.equals(getUsername(), other.getUsername())
                && Objects.equals(getPassword(), other.getPassword())
                && Objects.equals(getRole(), other.getRole());
    }

    @Override
    public String toString() {
        return "User{" +
                "id = " + id +
                ", username = '" + username + '\'' +
                ", role = '" + role + '\'' +
                '}';
    }
}
