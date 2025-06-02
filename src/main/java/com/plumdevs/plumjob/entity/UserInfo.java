package com.plumdevs.plumjob.entity;

import jakarta.persistence.*;

@Entity
public class UserInfo {

    @Id
    @Column
    String username;

    @Column
    String firstName;

    @Column
    String lastName;

    @Column
    String email;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "profile_picture", columnDefinition = "LONGBLOB")
    private byte[] profilePicture;

    @Column(name = "profile_picture_type", length = 50)
    private String profilePictureType;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public byte[] getProfilePicture() { return profilePicture; }

    public void setProfilePicture(byte[] profilePicture) { this.profilePicture = profilePicture; }

    public String getProfilePictureType() { return profilePictureType; }

    public void setProfilePictureType(String profilePictureType) { this.profilePictureType = profilePictureType; }
}