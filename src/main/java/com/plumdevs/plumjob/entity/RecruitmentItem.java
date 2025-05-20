package com.plumdevs.plumjob.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.util.Date;

@Entity
public class RecruitmentItem {

    @Id
    int history_id;

    String position;
    String company;
    String stage;
    String description;

    LocalDate user_start_Date;

    RecruitmentItem(){
        position = "";
        company = "";
        stage = "";
        user_start_Date = LocalDate.now(); //temp, later fetch from database
    }

    public RecruitmentItem(String positon, String company, String stage) {
        this.position = positon;
        this.company = company;
        this.stage = stage;
        this.user_start_Date = LocalDate.now(); //temp, later fetch from database
    }

    public RecruitmentItem(String positon, String company, String stage, String description) {
        this.position = positon;
        this.company = company;
        this.stage = stage;
        this.description = description;
        user_start_Date = LocalDate.now(); //temp, later fetch from database
    }

    public RecruitmentItem(String positon, String company, String stage, String description, LocalDate startDate) {
        this.position = positon;
        this.company = company;
        this.stage = stage;
        this.description = description;
        this.user_start_Date = startDate; //temp, later fetch from database
    }

    public int getHistory_id() {
        return history_id;
    }

    public void setHistory_id(int history_id) {
        this.history_id = history_id;
    }

    public String getPositon() {
        return position;
    }

    public void setPositon(String positon) {
        this.position = positon;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public LocalDate getStartDate() {
        return user_start_Date;
    }

    public void setStartDate(LocalDate user_start_Date) {
        this.user_start_Date = user_start_Date;
    }

}
