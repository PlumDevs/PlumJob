package com.plumdevs.plumjob.service;

import com.plumdevs.plumjob.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;


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
}
