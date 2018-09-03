package cn.itrip.common;

public class SystemConfig {
    private String smsAccountSid;//云通信短信平台账户Account Sid
    private String smsAuthToken;//云通信短信平台账户Auth Toke
    private String smsAppID;//云通信短信平台账户App ID
    private String smsServerIP;//云通信短信平台Server IP
    private String smsServerPort;//云通信短信平台Server Port
    private String fileUploadPathString;//文件上传路径，通过properties文件进行配置
    private String visitImgUrlString;//上传文件访问URL，通过properties文件进行配置
    private String machineCode;//生成订单的机器码，通过properties文件进行配置
    private String orderProcessOK;//
    private String orderProcessCancel;//
    private String localUploadPath;//
    private String tradeEndsUrl;//在线支付交易完成通知后续处理接口的地址
    private Boolean tradeUseProxy;//支付模块发送Get请求是否使用代理
    private String tradeProxyHost;//代理主机
    private Integer tradeProxyPort;//代理端口


    public String getTradeEndsUrl() {
        return tradeEndsUrl;
    }

    public void setTradeEndsUrl(String tradeEndsUrl) {
        this.tradeEndsUrl = tradeEndsUrl;
    }

    public Boolean getTradeUseProxy() {
        return tradeUseProxy;
    }

    public void setTradeUseProxy(Boolean tradeUseProxy) {
        this.tradeUseProxy = tradeUseProxy;
    }

    public String getTradeProxyHost() {
        return tradeProxyHost;
    }

    public void setTradeProxyHost(String tradeProxyHost) {
        this.tradeProxyHost = tradeProxyHost;
    }

    public Integer getTradeProxyPort() {
        return tradeProxyPort;
    }

    public void setTradeProxyPort(Integer tradeProxyPort) {
        this.tradeProxyPort = tradeProxyPort;
    }

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
