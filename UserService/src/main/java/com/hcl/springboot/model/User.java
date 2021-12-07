package com.hcl.springboot.model;

import lombok.Data;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.io.Serializable;

@Entity
//@Table(name = "users_table")
@Table(name = "user_table")
@Data   // useful annotation to reduce boilerplate getter/setter
public class User {

    public User() {

    }

    public void setUserId(int id) {
        this.id = id;
    }

    //        implements Serializable {
    // -----------------------------------------
    //    ATTRIBUTES
    // -----------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @NonNull
    @Column(name = "email")
    private String email;

    @NonNull
    @Column(name = "password")
    private String password;

    @NonNull
    @Column(name = "is_admin")
    private boolean isAdmin;

    @NonNull
    @Column(name = "username")
    private String username;

    @NonNull
    @Column(name = "first_name")
    private String firstName;

    @NonNull
    @Column(name = "last_name")
    private String lastName;

//    @Column(name = "dest_id")
//    private String destId;

    // -----------------------------------------
    //    CONSTRUCTORS
    // -----------------------------------------
//    public User(Integer userId) {
//        this.userId = userId;
//
//        email = null;
//        password = null;
//        username = null;
//        firstName = null;
//    }
//
//    public User(@NonNull String email,
//                @NonNull String password, boolean isAdmin, Integer userId) {
//        this(userId, email, password, false, null, null, null);
//    }
//
//    public User(Integer userId, @NonNull String email,
//                @NonNull String password,
//                boolean isAdmin,
//                @NonNull String username,
//                @NonNull String firstName,
//                @NonNull String lastName) {
//        this.userId = userId;
//        this.email = email;
//        this.password = password;
//        this.isAdmin = isAdmin;
//        this.username = username;
//        this.firstName = firstName;
//        this.lastName = lastName;
//    }

//
//    public User() {
//        this(null,null,null,false,null,null,null);
//    }

    // -----------------------------------------
    //    GETTERS / SETTERS
    // -----------------------------------------
    //  MINIMUM SPEC

    public @NotNull String getEmail() {
        return email;
    }

    public void setEmail(@NotNull String email) {
        this.email = email;
    }

    public @NotNull String getPassword() {
        return password;
    }

    public void setPassword(@NotNull String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public int getUserId() {
        return id;
    }


    public @NotNull String getUsername() {
        return username;
    }

    public void setUsername(@NotNull String username) {
        this.username = username;
    }

    public @NotNull String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotNull String firstName) {
        this.firstName = firstName;
    }

    public @NotNull String getLastName() {
        return lastName;
    }

    public void setLastName(@NotNull String lastName) {
        this.lastName = lastName;
    }

//    @Override
//    public String toString() {
//        return "User{" +
//                "userId=" + id +
//                ", email='" + email + '\'' +
//                ", password='" + password + '\'' +
//                ", isAdmin=" + isAdmin +
//                ", username='" + username + '\'' +
//                ", firstName='" + firstName + '\'' +
//                ", lastName='" + lastName + '\'' +
//                '}';
//    }

//    @Override
//    public String toString() {
//        return "User{" +
//                "id=" + id +
//                ", email='" + email + '\'' +
//                ", password='" + password + '\'' +
//                ", isAdmin=" + isAdmin +
//                ", username='" + username + '\'' +
//                ", firstName='" + firstName + '\'' +
//                ", lastName='" + lastName + '\'' +
//                '}';
//    }
}