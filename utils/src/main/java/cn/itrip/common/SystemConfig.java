package cn.itrip.common;

public class SystemConfig {
    private String smsAccountSid;
    private String smsAuthToken;
    private String smsAppID;
    private String smsServerIP;
    private String smsServerPort;

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
}
