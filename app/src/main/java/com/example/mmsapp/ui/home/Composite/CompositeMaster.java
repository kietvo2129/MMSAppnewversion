package com.example.mmsapp.ui.home.Composite;

public class CompositeMaster {

    String code,start_dt,end_dt,type,no;
    public CompositeMaster(String code, String start_dt, String end_dt, String type, String no) {
        this.code = code;
        this.start_dt = start_dt;
        this.end_dt = end_dt;
        this.type = type;
        this.no = no;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStart_dt() {
        return start_dt;
    }

    public void setStart_dt(String start_dt) {
        this.start_dt = start_dt;
    }

    public String getEnd_dt() {
        return end_dt;
    }

    public void setEnd_dt(String end_dt) {
        this.end_dt = end_dt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
