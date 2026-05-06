package com.brais.gymtrack.client;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.brais.gymtrack.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "client_profiles")
public class ClientProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Long clientId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(name = "last_name_1", nullable = false)
    private String lastName1;

    @Column(name = "last_name_2")
    private String lastName2;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column
    private String goal;

    @Column(name = "profile_picture_url")
    private String profilePictureUrl;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;


    public ClientProfile(){}
    
    public ClientProfile(
            User user,
            String name,
            String lastName1, 
            String lastName2, 
            LocalDate birthDate,
            String goal,
            String profilePictureUrl
    ) {
        this.user = user;
        this.name = name;
        this.lastName1 = lastName1;
        this.lastName2 = lastName2;
        this.birthDate = birthDate;
        this.goal = goal;
        this.profilePictureUrl = profilePictureUrl;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    //Getters and Setters
    public Long getClientId() {
        return clientId;
    }

    public User getUser() {
        return user;
    }

    public String getName() {
        return name;
    }

    public String getLastName1() {
        return lastName1;
    }

    public String getLastName2() {
        return lastName2;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getGoal() {
        return goal;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName1(String lastName1) {
        this.lastName1 = lastName1;
    }

    public void setLastName2(String lastName2) {
        this.lastName2 = lastName2;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }
}
