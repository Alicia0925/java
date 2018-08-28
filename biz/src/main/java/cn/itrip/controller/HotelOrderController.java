package cn.itrip.controller;

import cn.itrip.beans.dto.Dto;
import cn.itrip.beans.pojo.User;
import cn.itrip.beans.vo.order.SearchOrderVO;
import cn.itrip.common.DtoUtil;
import cn.itrip.common.Page;
import cn.itrip.common.ValidationToken;
import cn.itrip.service.userlinkuser.HotelOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 错误码：
 * 100501-100600
 */
@Controller
@Api(value = "API", basePath = "/http://api.itrap.com/api")
@RequestMapping(value = "/api/hotelorder")
public class HotelOrderController {
    @Resource
    private HotelOrderService hotelOrderService;
    @Resource
    private ValidationToken validationToken;

/**
 * 个人订单列表，并分页显示
 *
 * */
    @ApiOperation(value = "根据个人订单列表，并分页显示", httpMethod = "POST",
            protocols = "HTTP", produces = "application/json",
            response = Dto.class, notes = "根据条件查询个人订单列表，并分页显示" +
            "<p>订单类型(orderType)（-1：全部订单 0:旅游订单 1:酒店订单 2：机票订单）：</p>" +
            "<p>订单状态(orderStatus)（0：待支付 1:已取消 2:支付成功 3:已消费 4：已点评）：</p>" +
            "<p>对于页面tab条件：</p>" +
            "<p>全部订单（orderStatus：-1）</p>" +
            "<p>未出行（orderStatus：2）</p>" +
            "<p>待付款（orderStatus：0）</p>" +
            "<p>待评论（orderStatus：3）</p>" +
            "<p>已取消（orderStatus：1）</p>" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>错误码：</p>" +
            "<p>100501 : 请传递参数：orderType </p>" +
            "<p>100502 : 请传递参数：orderStatus </p>" +
            "<p>100503 : 获取个人订单列表错误 </p>" +
            "<p>100000 : token失效，请重登录 </p>")
    @RequestMapping(value = "/getpersonalorderlist", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Dto<Object> getPersonalOrderList(@RequestBody SearchOrderVO searchOrderVO,
                                            HttpServletRequest request) {
        Integer orderType = searchOrderVO.getOrderType();
        Integer orderStatus = searchOrderVO.getOrderStatus();
        String token = request.getHeader("token");
        User currentUser = validationToken.getCurrentUser(token);
        Dto dto = null;
        if (null != currentUser) {
            if (null == orderType && "".equals(orderType)) {
                return DtoUtil.returnFail("请传递参数：orderType", "100501");
            }
            if (null == orderStatus && "".equals(orderStatus)) {
                return DtoUtil.returnFail("请传递参数：orderStatus", "100502");
            }
            Map<String, Object> param = new HashMap<>();
            param.put("orderType", orderType == -1 ? null : orderType);
            param.put("orderStatus", orderStatus == -1 ? null : orderStatus);
            param.put("userId", currentUser.getId());
            param.put("orderNo", searchOrderVO.getOrderNo());
            param.put("linkUserName", searchOrderVO.getLinkUserName());
            param.put("startDate", searchOrderVO.getStartDate());
            param.put("endDate", searchOrderVO.getEndDate());
            try {
                Page page = hotelOrderService.getPageByMap(param,
                        searchOrderVO.getPageNo(),
                        searchOrderVO.getPageSize());
                dto = DtoUtil.returnSuccess("获取个人订单列表成功", page);
            } catch (Exception e) {
                e.printStackTrace();
                dto = DtoUtil.returnFail("获取个人订单列表错误", "100503");
            }
        } else {
            dto = DtoUtil.returnFail("token失效，请重登录", "100000");
        }
        return dto;

    }


}
