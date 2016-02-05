package com.khh.gjun.security.apputility;

/**
 * Created by student on 2015/12/17.
 */
public class TempProblemList implements java.io.Serializable{
    private String TempRecordID;
    private String TempLocation;
    private String TempTime;
    private String Temperature;
    private String Wet;
    private String HandleState;
    private int TempNotify;
    private String HandleContent;

    public String getHandleContent() {
        return HandleContent;
    }

    public void setHandleContent(String handleContent) {
        HandleContent = handleContent;
    }

    public int getTempNotify() {
        return TempNotify;
    }

    public void setTempNotify(int tempNotify) {
        TempNotify = tempNotify;
    }

    public String getTempRecordID() {
        return TempRecordID;
    }

    public void setTempRecordID(String tempRecordID) {
        TempRecordID = tempRecordID;
    }

    public String getTempLocation() {
        return TempLocation;
    }

    public void setTempLocation(String tempLocation) {
        TempLocation = tempLocation;
    }

    public String getTempTime() {
        return TempTime;
    }

    public void setTempTime(String tempTime) {
        TempTime = tempTime;
    }

    public String getTemperature() {
        return Temperature;
    }

    public void setTemperature(String temperature) {
        Temperature = temperature;
    }

    public String getWet() {
        return Wet;
    }

    public void setWet(String wet) {
        Wet = wet;
    }

    public String getHandleState() {
        return HandleState;
    }

    public void setHandleState(String handleState) {
        HandleState = handleState;
    }
}
