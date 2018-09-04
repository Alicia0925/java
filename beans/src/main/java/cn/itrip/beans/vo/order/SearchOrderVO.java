
package cn.itrip.beans.vo.order;

        import com.fasterxml.jackson.annotation.JsonFormat;
        import io.swagger.annotations.ApiModel;
        import io.swagger.annotations.ApiModelProperty;

        import java.io.Serializable;
        import java.util.Date;

/**
 * 前端输入-查询个人订单搜索条件VO
 *
 */
@ApiModel(value = "ItripSearchOrderVO",description = "搜索个人订单VO")
public class SearchOrderVO implements Serializable {

    @ApiModelProperty("[非必填] 订单号")
    private String orderNo;

    @ApiModelProperty("[非必填] 联系人")
    private String linkUserName;

    @ApiModelProperty("[非必填] 预定时间（start）")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @ApiModelProperty("[非必填] 预定时间（end）")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    @ApiModelProperty("[必填] 页面容量")
    private Integer pageSize;

    @ApiModelProperty("[必填] 页码")
    private Integer pageNo;

    @ApiModelProperty("[必填，注：接收数字类型] 订单状态（-1：全部订单 0:待付款 1:已取消 2：未出行 3:待评论 ）")
    private Integer orderStatus;

    @ApiModelProperty("[必填，注：接收数字类型] 订单类型（-1：全部订单 0:旅游订单 1:酒店订单 2：机票订单 ）")
    private Integer orderType;

    private Integer offset;

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {

        this.offset = offset;
    }

    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        if(orderType==-1){
            orderType=null;
        }
        this.orderType = orderType;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        if(orderStatus==-1){
            orderStatus=null;
        }
        this.orderStatus = orderStatus;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getLinkUserName() {
        return linkUserName;
    }

    public void setLinkUserName(String linkUserName) {
        this.linkUserName = linkUserName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNo() {

        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }
}