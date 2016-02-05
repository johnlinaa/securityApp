package com.khh.gjun.security.apputility;

/**
 * Created by student on 2015/12/17.
 */
public class JsonAll implements java.io.Serializable{
    //JS1.
    //要跟json的名稱對應(BossMsg)
    //RFID
    private String RFID_ID;
    private String Number;


    //BOSSWritemsg
    private String UserID;
    private String UserName;
    private String MemoContent;
    private String MemoTime;
    private String MemoID;

    //跟json的名稱對應(事件報表詳細)

    private String EeventName;
    private String EeventContent;
    private String EventLevel;
    private String EventIsNot;

    //跟json的名稱對應(tempproblemlist)
    private String TempLocation;
    private String TempTime;
    private String Temperature;
    private String Wet;
    private String TempError;
    //tempproblemlist詳細
    private String HandleState;
    private String HandleTime;
    private String HandleContent;


    public String getRFID_ID() {
        return RFID_ID;
    }

    public void setRFID_ID(String RFID_ID) {
        this.RFID_ID = RFID_ID;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getMemoID() {
        return MemoID;
    }

    public void setMemoID(String memoID) {
        MemoID = memoID;
    }

    public String getTempError() {
        return TempError;
    }

    public void setTempError(String tempError) {
        TempError = tempError;
    }

    public String getHandleTime() {
        return HandleTime;
    }

    public void setHandleTime(String handleTime) {
        HandleTime = handleTime;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getMemoContent() {
        return MemoContent;
    }

    public void setMemoContent(String memoContent) {
        MemoContent = memoContent;
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

    public String getHandleContent() {
        return HandleContent;
    }

    public void setHandleContent(String handleContent) {
        HandleContent = handleContent;
    }

    public String getMemoTime() {
        return MemoTime;
    }

    public void setMemoTime(String memoTime) {
        MemoTime = memoTime;
    }



}
