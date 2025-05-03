package com.plumdevs.plumjob.entity;

public class RecruitmentItem {

    String positon;
    String company;
    String status;

    RecruitmentItem(){
        positon = "";
        company = "";
        status = "";

    }

    public RecruitmentItem(String positon, String company, String status) {
        this.positon = positon;
        this.company = company;
        this.status = status;
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
}
