package cn.itrip.beans.vo.store;

import java.io.Serializable;
import java.util.Date;

/**
 * 验证房间库存时，返回的库存列表VO
 */
public class StoreVO implements Serializable {
    //房间ID
    private Long roomId;
    //库存记录日期
    private Date recordDate;

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    //房间库存
    private Integer store;

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Integer getStore() {
        return store;
    }

    public void setStore(Integer store) {
        this.store = store;
    }
}
