package com.sjf.open.json;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xiaosi on 16-7-13.
 */
public class ClickDetail{
    private String business;
    private String url;

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "ClickDetail{" +
                "business='" + business + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
