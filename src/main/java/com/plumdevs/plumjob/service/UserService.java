package com.plumdevs.plumjob.service;

import com.plumdevs.plumjob.repository.UserInfoRepository;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserInfoRepository userInfoRepository;
    private final AuthenticationContext authenticationContext;

    @Autowired
    public UserService(UserInfoRepository userInfoRepository, AuthenticationContext authenticationContext) {
        this.userInfoRepository = userInfoRepository;
        this.authenticationContext = authenticationContext;
    }

    public boolean isLoggedIn() {
        return authenticationContext.getPrincipalName().isPresent();
    }

    public String getUsername() {
        //return authenticationContext.getPrincipalName() + "";
        return (((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
    }

    public Integer emailExists(String email) {
        return userInfoRepository.emailExists(email);
    }

    public void addUserInfo(String username, String firstName, String lastName, String email) {
        userInfoRepository.addUserInfo(username, firstName, lastName, email);
    }
}
