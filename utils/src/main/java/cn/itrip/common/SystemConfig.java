package cn.itrip.common;

public class SystemConfig {
    private String smsAccountSid;
    private String smsAuthToken;
    private String smsAppID;
    private String smsServerIP;
    private String smsServerPort;
    private String fileUploadPathString;
    private String visitImgUrlString;
    private String machineCode;
    private String orderProcessOK;
    private String orderProcessCancel;
    private String localUploadPath;

    public String getLocalUploadPath() {
        return localUploadPath;
    }

    public void setLocalUploadPath(String localUploadPath) {
        this.localUploadPath = localUploadPath;
    }

    public void setSmsAccountSid(String smsAccountSid) {
        this.smsAccountSid = smsAccountSid;
    }

    public String getSmsAccountSid() {
        return smsAccountSid;
    }

    public void setSmsAuthToken(String smsAuthToken) {
        this.smsAuthToken = smsAuthToken;
    }

    public String getSmsAuthToken() {
        return smsAuthToken;
    }

    public void setSmsAppID(String smsAppID) {
        this.smsAppID = smsAppID;
    }

    public String getSmsAppID() {
        return smsAppID;
    }

    public void setSmsServerIP(String smsServerIP) {
        this.smsServerIP = smsServerIP;
    }

    public String getSmsServerIP() {
        return smsServerIP;
    }

    public void setSmsServerPort(String smsServerPort) {
        this.smsServerPort = smsServerPort;
    }

    public String getSmsServerPort() {
        return smsServerPort;
    }

    public void setFileUploadPathString(String fileUploadPathString) {
        this.fileUploadPathString = fileUploadPathString;
    }

    public String getFileUploadPathString() {
        return fileUploadPathString;
    }

    public void setVisitImgUrlString(String visitImgUrlString) {
        this.visitImgUrlString = visitImgUrlString;
    }

    public String getVisitImgUrlString() {
        return visitImgUrlString;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setOrderProcessOK(String orderProcessOK) {
        this.orderProcessOK = orderProcessOK;
    }

    public String getOrderProcessOK() {
        return orderProcessOK;
    }

    public void setOrderProcessCancel(String orderProcessCancel) {
        this.orderProcessCancel = orderProcessCancel;
    }

    public String getOrderProcessCancel() {
        return orderProcessCancel;
    }
}
