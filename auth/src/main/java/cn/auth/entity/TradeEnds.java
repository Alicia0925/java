package cn.auth.entity;

//订单支付完成后，系统需对该订单进行后续处理，如减库存等。处理完成后，删除此表中的订单记录
public class TradeEnds {
    private Long id;//订单ID
    private String orderNo;//订单编号(注意非支付宝交易编号tradeNo)

    private Boolean flag;//标识字段(默认0：未处理；1：处理中)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }
}