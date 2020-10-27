package com.example.mmsapp.ui.home.Composite;

public class CompositeMaster {

    String code, start_dt, end_dt, type, no, name, use_yn, pmid, staff_tp;

    public CompositeMaster(String code, String start_dt, String end_dt, String type, String no, String name, String use_yn, String pmid, String staff_tp) {
        this.code = code;
        this.start_dt = start_dt;
        this.end_dt = end_dt;
        this.type = type;
        this.no = no;
        this.name = name;
        this.use_yn = use_yn;
        this.pmid = pmid;
        this.staff_tp = staff_tp;
    }

    public String getPmid() {
        return pmid;
    }

    public void setPmid(String pmid) {
        this.pmid = pmid;
    }

    public String getStaff_tp() {
        return staff_tp;
    }

    public void setStaff_tp(String staff_tp) {
        this.staff_tp = staff_tp;
    }

    public String getUse_yn() {
        return use_yn;
    }

    public void setUse_yn(String use_yn) {
        this.use_yn = use_yn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
