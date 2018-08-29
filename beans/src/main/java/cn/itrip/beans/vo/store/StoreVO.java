package cn.itrip.beans.vo.store;

import java.io.Serializable;
import java.util.Date;

/**
 * 验证房间库存时，返回的库存列表VO
 *
 */
public class StoreVO implements Serializable {

    private Long roomId;

    private Date date;

    private Integer store;

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getStore() {
        return store;
    }

    public void setStore(Integer store) {
        this.store = store;
    }
}