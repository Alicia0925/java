package cn.itrip.beans.pojo;

//酒店字段拓展表
public class HotelWithBLOBs extends Hotel {
    private String details;//酒店介绍（保存附文本）

    private String facilities;//酒店设施

    private String hotelPolicy;//酒店政策

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getFacilities() {
        return facilities;
    }

    public void setFacilities(String facilities) {
        this.facilities = facilities;
    }

    public String getHotelPolicy() {
        return hotelPolicy;
    }

    public void setHotelPolicy(String hotelPolicy) {
        this.hotelPolicy = hotelPolicy;
    }
}