package com.plumdevs.plumjob.entity;

import java.time.LocalDate;
import java.util.Date;

public class RecruitmentItem {

    String positon;
    String company;
    String status;
    String description;

    LocalDate startDate;

    RecruitmentItem(){
        positon = "";
        company = "";
        status = "";
        startDate = LocalDate.now(); //temp, later fetch from database
    }

    public RecruitmentItem(String positon, String company, String status) {
        this.positon = positon;
        this.company = company;
        this.status = status;
        startDate = LocalDate.now(); //temp, later fetch from database
    }

    public RecruitmentItem(String positon, String company, String status, String description) {
        this.positon = positon;
        this.company = company;
        this.status = status;
        this.description = description;
        startDate = LocalDate.now(); //temp, later fetch from database
    }

    public RecruitmentItem(String positon, String company, String status, String description, LocalDate startDate) {
        this.positon = positon;
        this.company = company;
        this.status = status;
        this.description = description;
        this.startDate = startDate; //temp, later fetch from database
    }

    public String getPositon() {
        return positon;
    }

    public void setPositon(String positon) {
        this.positon = positon;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
}
