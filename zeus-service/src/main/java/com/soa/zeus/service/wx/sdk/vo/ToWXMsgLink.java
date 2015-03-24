package com.soa.zeus.service.wx.sdk.vo;

/**
 * Created by shizhizhong on 14-10-13.
 */
public class ToWXMsgLink extends ToWXMsg {

    private String Title;
    private String Description;
    private String Url;


    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }
}
