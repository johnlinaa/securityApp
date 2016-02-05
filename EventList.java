package com.khh.gjun.security.apputility;

import java.io.Serializable;

/**
 * Created by student on 2015/12/8.
 */
public class EventList implements Serializable{
    private int eventID;
    private int RFID_ID;
    private int userID;
    private String UserName;
    private String location;

    private String eventName;
    private String eventTime;
    private String eventLocation;
    private String eventLevel;
    private String isNot;
    private String eventContent;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventLevel() {
        return eventLevel;
    }

    public void setEventLevel(String eventLevel) {
        this.eventLevel = eventLevel;
    }

    public String getIsNot() {
        return isNot;
    }

    public void setIsNot(String isNot) {
        this.isNot = isNot;
    }


    public String getEventContent() {
        return eventContent;
    }

    public void setEventContent(String eventContent) {
        this.eventContent = eventContent;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public int getRFID_ID() {
        return RFID_ID;
    }

    public void setRFID_ID(int RFID_ID) {
        this.RFID_ID = RFID_ID;
    }
}
