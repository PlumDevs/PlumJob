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
    //String description;

    //LocalDate startDate;

    RecruitmentItem(){
        position = "";
        company = "";
        stage = "";
        //startDate = LocalDate.now(); //temp, later fetch from database
    }

    public RecruitmentItem(String positon, String company, String stage) {
        this.position = positon;
        this.company = company;
        this.stage = stage;
        //startDate = LocalDate.now(); //temp, later fetch from database
    }

    public RecruitmentItem(String positon, String company, String stage, String description) {
        this.position = positon;
        this.company = company;
        this.stage = stage;
        //this.description = description;
        //startDate = LocalDate.now(); //temp, later fetch from database
    }

    public RecruitmentItem(String positon, String company, String stage, String description, LocalDate startDate) {
        this.position = positon;
        this.company = company;
        this.stage = stage;
        //this.description = description;
        //this.startDate = startDate; //temp, later fetch from database
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

    /*public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

     */

    /*
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

     */
}
