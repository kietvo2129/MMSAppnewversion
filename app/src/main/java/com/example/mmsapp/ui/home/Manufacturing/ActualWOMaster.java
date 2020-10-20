package com.example.mmsapp.ui.home.Manufacturing;

import android.view.View;

public class ActualWOMaster {

    String id_actual,name,date,product,item_vcd,remark,
            reg_id,reg_dt,chg_id,chg_dt;

    int defect,target,actual;

    private View.OnClickListener requestBtnClickListener;
    public ActualWOMaster(String id_actual, String name, String date, int defect, int target, String product, String item_vcd, int actual, String remark, String reg_id, String reg_dt, String chg_id, String chg_dt) {
        this.id_actual = id_actual;
        this.name = name;
        this.date = date;
        this.defect = defect;
        this.target = target;
        this.product = product;
        this.item_vcd = item_vcd;
        this.actual = actual;
        this.remark = remark;
        this.reg_id = reg_id;
        this.reg_dt = reg_dt;
        this.chg_id = chg_id;
        this.chg_dt = chg_dt;
    }

    public View.OnClickListener getRequestBtnClickListener() {
        return requestBtnClickListener;
    }

    public void setRequestBtnClickListener(View.OnClickListener requestBtnClickListener) {
        this.requestBtnClickListener = requestBtnClickListener;
    }
    public String getId_actual() {
        return id_actual;
    }

    public void setId_actual(String id_actual) {
        this.id_actual = id_actual;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public int getDefect() {
        return defect;
    }

    public void setDefect(int defect) {
        this.defect = defect;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public int getActual() {
        return actual;
    }

    public void setActual(int actual) {
        this.actual = actual;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getItem_vcd() {
        return item_vcd;
    }

    public void setItem_vcd(String item_vcd) {
        this.item_vcd = item_vcd;
    }



    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getReg_id() {
        return reg_id;
    }

    public void setReg_id(String reg_id) {
        this.reg_id = reg_id;
    }

    public String getReg_dt() {
        return reg_dt;
    }

    public void setReg_dt(String reg_dt) {
        this.reg_dt = reg_dt;
    }

    public String getChg_id() {
        return chg_id;
    }

    public void setChg_id(String chg_id) {
        this.chg_id = chg_id;
    }

    public String getChg_dt() {
        return chg_dt;
    }

    public void setChg_dt(String chg_dt) {
        this.chg_dt = chg_dt;
    }
}
