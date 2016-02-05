package com.khh.gjun.security.apputility;

/**
 * Created by student on 2015/12/17.
 */
public class NoHandle implements java.io.Serializable{
    private String EeventName;
    private String EeventContent;
    private String EventLevel;
    private String EventIsNot;
    private String EventID;
    private String EventLocation;
    private String EventTime;

    public String getEventLocation() {
        return EventLocation;
    }

    public void setEventLocation(String eventLocation) {
        EventLocation = eventLocation;
    }

    public String getEventTime() {
        return EventTime;
    }

    public void setEventTime(String eventTime) {
        EventTime = eventTime;
    }

    public String getEventID() {
        return EventID;
    }

    public void setEventID(String eventID) {
        EventID = eventID;
    }

    public String getEeventName() {
        return EeventName;
    }

    public void setEeventName(String eeventName) {
        EeventName = eeventName;
    }

    public String getEeventContent() {
        return EeventContent;
    }

    public void setEeventContent(String eeventContent) {
        EeventContent = eeventContent;
    }

    public String getEventLevel() {
        return EventLevel;
    }

    public void setEventLevel(String eventLevel) {
        EventLevel = eventLevel;
    }

    public String getEventIsNot() {
        return EventIsNot;
    }

    public void setEventIsNot(String eventIsNot) {
        EventIsNot = eventIsNot;
    }
}
