package com.ecommerce.project.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "username"), @UniqueConstraint(columnNames = "email")})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank
    @Size(max = 20)
    private String username;

    @Email
    @Size(max = 50)
    private String email;


    @Size(max = 128)
    @NotBlank
    private String password;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE},orphanRemoval = true)
    private Set<Product> products;

    @ToString.Exclude
    @OneToOne(mappedBy = "user", cascade = { CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private Cart cart;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST,CascadeType.MERGE}, orphanRemoval = true)
    //@JoinTable(name = "user_address",joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = @JoinColumn(name = "address_id"))
    private List<Address> addresses = new ArrayList<>();



}
