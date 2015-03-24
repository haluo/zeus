package com.soa.zeus.service.domain;

/**
 * Created by shizhizhong on 15/2/10.
 */
public class User extends BaseDomain{

    private Integer id;
    private String wxId;
    private String phone;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWxId() {
        return wxId;
    }

    public void setWxId(String wxId) {
        this.wxId = wxId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
