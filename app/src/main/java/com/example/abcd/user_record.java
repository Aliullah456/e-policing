package com.example.abcd;

public class user_record {
    String policeid, fullname, designation, policestation,ispolice, CNIC, email, mobilenumber, address, user_id, password, profileimage;

    public user_record() {
    }

    public user_record(String policeid, String fullname, String designation, String policestation, String ispolice, String CNIC, String email, String mobilenumber, String address, String user_id, String password, String profileimage) {
        this.policeid = policeid;
        this.fullname = fullname;
        this.designation = designation;
        this.policestation = policestation;
        this.ispolice = ispolice;
        this.CNIC = CNIC;
        this.email = email;
        this.mobilenumber = mobilenumber;
        this.address = address;
        this.user_id = user_id;
        this.password = password;
        this.profileimage = profileimage;
    }

    public String getPoliceid() {
        return policeid;
    }

    public void setPoliceid(String policeid) {
        this.policeid = policeid;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getPolicestation() {
        return policestation;
    }

    public void setPolicestation(String policestation) {
        this.policestation = policestation;
    }

    public String getIspolice() {
        return ispolice;
    }

    public void setIspolice(String ispolice) {
        this.ispolice = ispolice;
    }

    public String getCNIC() {
        return CNIC;
    }

    public void setCNIC(String CNIC) {
        this.CNIC = CNIC;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobilenumber() {
        return mobilenumber;
    }

    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }
}