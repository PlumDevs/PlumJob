package com.plumdevs.plumjob.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity //Do not change names, entity variable names have to be the same as in the database object
public class DiagramLink {

    @Id
    public int status_history_id;

    //@Column(name = "from_status")
    public String from_status; //TODO: CHECK IF THERE IS NO ISSUE WITH APPLIED-APPLIED, EVENTUALLY CAN DELETE THE APPLIED-APPLIED?

    //@Column(name = "to_status")
    public String to_status;

    //@Column(name = "weight")
    public int weight;

    public DiagramLink() {
        status_history_id = 0;
        from_status = "";
        to_status = "";
        weight = 0;
    }

    public DiagramLink(int history_status_id, String from, String to, int weight) {
        this.status_history_id = history_status_id;;
        this.from_status = from;
        this.to_status = to;
        this.weight = weight;
    }

    public DiagramLink(String from, String to, int weight) {
        this.from_status = from;
        this.to_status = to;
        this.weight = weight;
    }

    public int getStatus_history_id() {
        return status_history_id;
    }

    public void setStatus_history_id(int status_history_id) {
        this.status_history_id = status_history_id;
    }

    public String getFrom() {
        return from_status;
    }

    public void setFrom(String from) {
        this.from_status = from;
    }

    public String getTo() {
        return to_status;
    }

    public void setTo(String to) {
        this.to_status = to;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
