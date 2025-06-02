package com.plumdevs.plumjob.service;

import com.plumdevs.plumjob.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserService {

    @Autowired
    UserInfoRepository userInfoRepository;
    JdbcUserDetailsManager userDetailsManager;

    public Integer emailExists(String email) {
        return userInfoRepository.emailExists(email);
    }
    public void addUserInfo(String username, String firstName, String lastName, String email) {
        userInfoRepository.addUserInfo(username, firstName, lastName, email);
    }

    //TODO; figure out and transition to services
    public String getUsername(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails)principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    public String getUserFirstName(String username) {
        return userInfoRepository.getUserFirstNameByUsername(username);
    }

    public String getUserLastName(String username) {
        return userInfoRepository.getUserLastNameByUsername(username);
    }


    public String getUserEmail(String username) {
        return userInfoRepository.getUserEmailByUsername(username);
    }

    @Transactional
    public void updateProfilePicture(String username, byte[] profilePicture, String profilePictureType) {
        userInfoRepository.updateProfilePicture(username, profilePicture, profilePictureType);
    }

    public byte[] getProfilePicture(String username) {
        return userInfoRepository.findProfilePictureByUsername(username);
    }

    public String getProfilePictureType(String username) {
        return userInfoRepository.findProfilePictureTypeByUsername(username);
    }

    public boolean userExists(String username) {
        return userInfoRepository.findUsername(username) != null;
    }
}