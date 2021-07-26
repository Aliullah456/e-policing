package com.example.abcd;

public class criminal_record {
    String file_uri,criminal_name,criminal_cnic,criminal_phone,criminal_address,criminal_age,complaint_id;
    public criminal_record(){

    }

    public criminal_record(String file_uri, String criminal_name, String criminal_cnic, String criminal_phone, String criminal_address, String criminal_age, String complaint_id) {
        this.file_uri = file_uri;
        this.criminal_name = criminal_name;
        this.criminal_cnic = criminal_cnic;
        this.criminal_phone = criminal_phone;
        this.criminal_address = criminal_address;
        this.criminal_age = criminal_age;
        this.complaint_id = complaint_id;
    }

    public String getFile_uri() {
        return file_uri;
    }

    public void setFile_uri(String file_uri) {
        this.file_uri = file_uri;
    }

    public String getCriminal_name() {
        return criminal_name;
    }

    public void setCriminal_name(String criminal_name) {
        this.criminal_name = criminal_name;
    }

    public String getCriminal_cnic() {
        return criminal_cnic;
    }

    public void setCriminal_cnic(String criminal_cnic) {
        this.criminal_cnic = criminal_cnic;
    }

    public String getCriminal_phone() {
        return criminal_phone;
    }

    public void setCriminal_phone(String criminal_phone) {
        this.criminal_phone = criminal_phone;
    }

    public String getCriminal_address() {
        return criminal_address;
    }

    public void setCriminal_address(String criminal_address) {
        this.criminal_address = criminal_address;
    }

    public String getCriminal_age() {
        return criminal_age;
    }

    public void setCriminal_age(String criminal_age) {
        this.criminal_age = criminal_age;
    }

    public String getComplaint_id() {
        return complaint_id;
    }

    public void setComplaint_id(String complaint_id) {
        this.complaint_id = complaint_id;
    }
}
