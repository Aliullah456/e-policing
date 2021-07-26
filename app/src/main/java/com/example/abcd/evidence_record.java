package com.example.abcd;

public class evidence_record {
    private String complaint_id;
    private String file_uri;
    public evidence_record(){

    }

    public evidence_record(String complaint_id, String file_uri) {
        this.complaint_id = complaint_id;
        this.file_uri = file_uri;
    }

    public String getComplaint_id() {
        return complaint_id;
    }

    public void setComplaint_id(String complaint_id) {
        this.complaint_id = complaint_id;
    }

    public String getFile_uri() {
        return file_uri;
    }

    public void setFile_uri(String file_uri) {
        this.file_uri = file_uri;
    }
}
