package com.example.abcd;

public class complainrecord {
    String Trackid;
    String complainer_id;
    String crime_catogry;
    String crime;
    String detail;
    String region;
    String sector;
    String address;
    String date;
    String time;
    String status;
    String created_date;
    public complainrecord(){}
    public complainrecord(String trackid, String complainer_id, String crime_catogry, String crime, String detail, String region, String sector, String address, String date, String time, String status, String created_date) {
        Trackid = trackid;
        this.complainer_id = complainer_id;
        this.crime_catogry = crime_catogry;
        this.crime = crime;
        this.detail = detail;
        this.region = region;
        this.sector = sector;
        this.address = address;
        this.date = date;
        this.time = time;
        this.status = status;
        this.created_date = created_date;
    }

    public String getTrackid() {
        return Trackid;
    }

    public void setTrackid(String trackid) {
        Trackid = trackid;
    }

    public String getComplainer_id() {
        return complainer_id;
    }

    public void setComplainer_id(String complainer_id) {
        this.complainer_id = complainer_id;
    }

    public String getCrime_catogry() {
        return crime_catogry;
    }

    public void setCrime_catogry(String crime_catogry) {
        this.crime_catogry = crime_catogry;
    }

    public String getCrime() {
        return crime;
    }

    public void setCrime(String crime) {
        this.crime = crime;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }
}
