package cn.itrip.beans.vo.comment;

import java.io.Serializable;

/**
 * 新增评论的酒店描述vo
 * */
public class HotelDescVO implements Serializable {

    private Long hotelId;

    private String hotelName;

    private Integer hotelLevel;

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public Integer getHotelLevel() {
        return hotelLevel;
    }

    public void setHotelLevel(Integer hotelLevel) {
        this.hotelLevel = hotelLevel;
    }
}
