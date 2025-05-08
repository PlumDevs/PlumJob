package com.plumdevs.plumjob.entity;

import jakarta.persistence.*;

@Entity
public class User {

    @Id
    @Column
    String username;

}
