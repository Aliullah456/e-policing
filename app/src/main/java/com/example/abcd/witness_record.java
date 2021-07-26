package com.example.abcd;

public class witness_record {
    String file_uri,witness_name,witness_cnic,witness_phone,witness_address,witness_age,complaint_id;
    public witness_record(){}

    public witness_record(String file_uri, String witness_name, String witness_cnic, String witness_phone, String witness_address, String witness_age, String complaint_id) {
        this.file_uri = file_uri;
        this.witness_name = witness_name;
        this.witness_cnic = witness_cnic;
        this.witness_phone = witness_phone;
        this.witness_address = witness_address;
        this.witness_age = witness_age;
        this.complaint_id = complaint_id;
    }

    public String getFile_uri() {
        return file_uri;
    }

    public void setFile_uri(String file_uri) {
        this.file_uri = file_uri;
    }

    public String getWitness_name() {
        return witness_name;
    }

    public void setWitness_name(String witness_name) {
        this.witness_name = witness_name;
    }

    public String getWitness_cnic() {
        return witness_cnic;
    }

    public void setWitness_cnic(String witness_cnic) {
        this.witness_cnic = witness_cnic;
    }

    public String getWitness_phone() {
        return witness_phone;
    }

    public void setWitness_phone(String witness_phone) {
        this.witness_phone = witness_phone;
    }

    public String getWitness_address() {
        return witness_address;
    }

    public void setWitness_address(String witness_address) {
        this.witness_address = witness_address;
    }

    public String getWitness_age() {
        return witness_age;
    }

    public void setWitness_age(String witness_age) {
        this.witness_age = witness_age;
    }

    public String getComplaint_id() {
        return complaint_id;
    }

    public void setComplaint_id(String complaint_id) {
        this.complaint_id = complaint_id;
    }
}
