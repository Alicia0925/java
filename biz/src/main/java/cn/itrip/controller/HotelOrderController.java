package cn.itrip.controller;

import cn.itrip.beans.dto.Dto;
import cn.itrip.beans.pojo.HotelOrder;
import cn.itrip.beans.pojo.HotelRoom;
import cn.itrip.beans.pojo.User;
import cn.itrip.beans.vo.order.*;
import cn.itrip.common.*;
import cn.itrip.service.hotelroom.RoomService;
import cn.itrip.service.order.HotelOrderService;
import cn.itrip.service.order.HotelTempStoreService;
import cn.itrip.service.order.OrderLinkUserService;
import cn.itrip.service.order.TradeEndsService;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 错误码：
 * 100501-100600
 */
@Controller
@RequestMapping(value = "/api/hotelorder")
public class HotelOrderController {
    @Resource
    private HotelOrderService hotelOrderService;
    @Resource
    private ValidationToken validationToken;
    @Resource
    private RoomService roomService;

    @Resource
    private SystemConfig systemConfig;

//    @Resource
//    private HotelTempStoreService hotelTempStoreService;
//
//
//    @Resource
//    private TradeEndsService itripTradeEndsService;

    @Resource
    private OrderLinkUserService orderLinkUserService;


    /**
     * 个人订单列表，并分页显示
     */

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
            searchOrderVO.setUserId(currentUser.getId());
            searchOrderVO.setOffset((searchOrderVO.getPageNo() - 1) * searchOrderVO.getPageSize());
            try {
                Page page = hotelOrderService.getPageByMap(searchOrderVO);
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

    /**
     * 根据订单ID查看个人订单详情:
     *
     * 错误码:
     * 100525 : 请传递参数：orderId
     * 100526 : 没有相关订单信息
     * 100527 : 获取个人订单信息错误
     * 100000 : token失效，请重登录
     * */
    @RequestMapping(value = "/getpersonalorderinfo/{orderId}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Dto<Object> getPersonalOrderInfo(@PathVariable String orderId,
                                            HttpServletRequest request) {
        Dto<Object> dto = null;
        String token  = request.getHeader("token");
        User currentUser = validationToken.getCurrentUser(token);
        if (null != currentUser) {
            if (null == orderId || "".equals(orderId)) {
                return DtoUtil.returnFail("请传递参数：orderId", "100525");
            }
            try {
                HotelOrder hotelOrder = hotelOrderService.getItripHotelOrderById(Long.valueOf(orderId));
                if (null != hotelOrder) {
                    PersonalHotelOrderVo personalHotelOrderVo = new PersonalHotelOrderVo();
                    personalHotelOrderVo.setId(hotelOrder.getId());
                    personalHotelOrderVo.setBookType(hotelOrder.getBookType());
                    personalHotelOrderVo.setCreationDate(hotelOrder.getCreationDate());
                    personalHotelOrderVo.setOrderNo(hotelOrder.getOrderNo());
                    //查询预订房间的信息
                    HotelRoom room = roomService.getHotelRoomById(hotelOrder.getRoomId());
                    if (EmptyUtils.isNotEmpty(room)) {
                        personalHotelOrderVo.setRoomPayType(room.getPayType());
                    }
                    Integer orderStatus = hotelOrder.getOrderStatus();
                    personalHotelOrderVo.setOrderStatus(orderStatus);
                    //订单状态（0：待支付 1:已取消 2:支付成功 3:已消费 4:已点评）
                    //{"1":"订单提交","2":"订单支付","3":"支付成功","4":"入住","5":"订单点评","6":"订单完成"}
                    //{"1":"订单提交","2":"订单支付","3":"订单取消"}

                    if (orderStatus == 1) {
                        personalHotelOrderVo.setOrderProcess(JSONArray.parse(systemConfig.getOrderProcessCancel()));
                        personalHotelOrderVo.setProcessNode("3");
                    } else if (orderStatus == 0) {
                        personalHotelOrderVo.setOrderProcess(JSONArray.parse(systemConfig.getOrderProcessOK()));
                        personalHotelOrderVo.setProcessNode("2");//订单支付
                    } else if (orderStatus == 2) {
                        personalHotelOrderVo.setOrderProcess(JSONArray.parse(systemConfig.getOrderProcessOK()));
                        personalHotelOrderVo.setProcessNode("3");//支付成功（未出行）
                    } else if (orderStatus == 3) {
                        personalHotelOrderVo.setOrderProcess(JSONArray.parse(systemConfig.getOrderProcessOK()));
                        personalHotelOrderVo.setProcessNode("5");//订单点评
                    } else if (orderStatus == 4) {
                        personalHotelOrderVo.setOrderProcess(JSONArray.parse(systemConfig.getOrderProcessOK()));
                        personalHotelOrderVo.setProcessNode("6");//订单完成
                    } else {
                        personalHotelOrderVo.setOrderProcess(null);
                        personalHotelOrderVo.setProcessNode(null);
                    }
                    personalHotelOrderVo.setPayAmount(hotelOrder.getPayAmount());
                    personalHotelOrderVo.setPayType(hotelOrder.getPayType());
                    personalHotelOrderVo.setNoticePhone(hotelOrder.getNoticePhone());
                    dto = DtoUtil.returnSuccess("获取个人订单信息成功", personalHotelOrderVo);
                } else {
                    dto = DtoUtil.returnFail("没有相关订单信息", "100526");
                }
            } catch (Exception e) {
                e.printStackTrace();
                dto = DtoUtil.returnFail("获取个人订单信息错误", "100527");
            }
        } else {
            dto = DtoUtil.returnFail("token失效，请重登录", "100000");
        }
        return dto;
    }
    /**
     * 根据订单ID查看个人订单详情-房型相关信息
     *
     * 错误码：
     * 100529 : 请传递参数：orderId
     * 100530 : 没有相关订单房型信息
     * 100531 : 获取个人订单房型信息错误
     * 支持支付类型(roomPayType)： 1 : 在线付 , 2 : 线下付 , 3 : 不限
     * 100000 : token失效，请重登录
     * */
    @RequestMapping(value = "/getpersonalorderroominfo/{orderId}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Dto<Object> getPersonalOrderRoomInfo(
            @PathVariable String orderId,
            HttpServletRequest request) {
        Dto<Object> dto = null;
        String tokenString = request.getHeader("token");
        User currentUser = validationToken.getCurrentUser(tokenString);
        if (null != currentUser) {
            if (null == orderId || "".equals(orderId)) {
                return DtoUtil.returnFail("请传递参数：orderId", "100529");
            }
            try {
                PersonalOrderRoomVo vo = hotelOrderService.getHotelOrderRoomById(Long.valueOf(orderId));
                if (null != vo) {
                    dto = DtoUtil.returnSuccess("获取个人订单房型信息成功", vo);
                } else {
                    dto = DtoUtil.returnFail("没有相关订单房型信息", "100530");
                }
            } catch (Exception e) {
                e.printStackTrace();
                dto = DtoUtil.returnFail("获取个人订单房型信息错误", "100531");
            }
        } else {
            dto = DtoUtil.returnFail("token失效，请重登录", "100000");
        }
        return dto;
    }


    /**
     * 根据订单ID获取订单信息
     * 错误码：
     * 100533 : 没有查询到相应订单
     * 100534 : 系统异常
     * */

    @RequestMapping(value = "/queryOrderById/{orderId}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Dto<Object> queryOrderById( @PathVariable Long orderId,HttpServletRequest request) {
        ModifyHotelOrderVO  modifyHotelOrderVO = null;
        try {
            String token  = request.getHeader("token");
            User currentUser = validationToken.getCurrentUser(token);
            if(EmptyUtils.isEmpty(currentUser)){
                return DtoUtil.returnFail("token失效，请重登录", "100000");
            }
            HotelOrder order = hotelOrderService.getItripHotelOrderById(orderId);
            if (EmptyUtils.isEmpty(order)) {
                return DtoUtil.returnFail("100533", "没有查询到相应订单");
            }
            modifyHotelOrderVO = new  ModifyHotelOrderVO();
            BeanUtils.copyProperties(order, modifyHotelOrderVO);
            List<OrderLinkUserVo> orderLinkUserList =  orderLinkUserService.getOrderLinkUserListByOrderId(order.getId());
            modifyHotelOrderVO.setOrderLinkUserList(orderLinkUserList);
            return DtoUtil.returnSuccess("获取订单成功", modifyHotelOrderVO);
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常", "100534");
        }
    }



//
//    @ApiOperation(value = "生成订单", httpMethod = "POST",
//            protocols = "HTTP", produces = "application/json",
//            response = Dto.class, notes = "生成订单" +
//            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
//            "<p>错误码：</p>" +
//            "<p>100505 : 生成订单失败 </p>" +
//            "<p>100506 : 不能提交空，请填写订单信息 </p>" +
//            "<p>100507 : 库存不足 </p>" +
//            "<p>100000 : token失效，请重登录</p>")
//    @RequestMapping(value = "/addhotelorder", method = RequestMethod.POST, produces = "application/json")
//    @ResponseBody
//    public Dto<Object> addHotelOrder(@RequestBody AddHotelOrderVO itripAddHotelOrderVO, HttpServletRequest request) {
//        Dto<Object> dto = new Dto<Object>();
//        String tokenString = request.getHeader("token");
//        logger.debug("token name is from header : " + tokenString);
//        ItripUser currentUser = validationToken.getCurrentUser(tokenString);
//        Map<String, Object> validateStoreMap = new HashMap<String, Object>();
//        validateStoreMap.put("startTime", itripAddHotelOrderVO.getCheckInDate());
//        validateStoreMap.put("endTime", itripAddHotelOrderVO.getCheckOutDate());
//        validateStoreMap.put("hotelId", itripAddHotelOrderVO.getHotelId());
//        validateStoreMap.put("roomId", itripAddHotelOrderVO.getRoomId());
//        validateStoreMap.put("count", itripAddHotelOrderVO.getCount());
//        List<ItripUserLinkUser> linkUserList = itripAddHotelOrderVO.getLinkUser();
//        if(EmptyUtils.isEmpty(currentUser)){
//            return DtoUtil.returnFail("token失效，请重登录", "100000");
//        }
//        try {
//            //判断库存是否充足
//            Boolean flag = itripHotelTempStoreService.validateRoomStore(validateStoreMap);
//            if (flag && null != itripAddHotelOrderVO) {
//                //计算订单的预定天数
//                Integer days = DateUtil.getBetweenDates(
//                        itripAddHotelOrderVO.getCheckInDate(), itripAddHotelOrderVO.getCheckOutDate()
//                ).size()-1;
//                if(days<=0){
//                    return DtoUtil.returnFail("退房日期必须大于入住日期", "100505");
//                }
//                ItripHotelOrder itripHotelOrder = new ItripHotelOrder();
//                itripHotelOrder.setId(itripAddHotelOrderVO.getId());
//                itripHotelOrder.setUserId(currentUser.getId());
//                itripHotelOrder.setOrderType(itripAddHotelOrderVO.getOrderType());
//                itripHotelOrder.setHotelId(itripAddHotelOrderVO.getHotelId());
//                itripHotelOrder.setHotelName(itripAddHotelOrderVO.getHotelName());
//                itripHotelOrder.setRoomId(itripAddHotelOrderVO.getRoomId());
//                itripHotelOrder.setCount(itripAddHotelOrderVO.getCount());
//                itripHotelOrder.setCheckInDate(itripAddHotelOrderVO.getCheckInDate());
//                itripHotelOrder.setCheckOutDate(itripAddHotelOrderVO.getCheckOutDate());
//                itripHotelOrder.setNoticePhone(itripAddHotelOrderVO.getNoticePhone());
//                itripHotelOrder.setNoticeEmail(itripAddHotelOrderVO.getNoticeEmail());
//                itripHotelOrder.setSpecialRequirement(itripAddHotelOrderVO.getSpecialRequirement());
//                itripHotelOrder.setIsNeedInvoice(itripAddHotelOrderVO.getIsNeedInvoice());
//                itripHotelOrder.setInvoiceHead(itripAddHotelOrderVO.getInvoiceHead());
//                itripHotelOrder.setInvoiceType(itripAddHotelOrderVO.getInvoiceType());
//                itripHotelOrder.setCreatedBy(currentUser.getId());
//                StringBuilder linkUserName = new StringBuilder();
//                int size = linkUserList.size();
//                for (int i = 0; i < size; i++) {
//                    if (i != size - 1) {
//                        linkUserName.append(linkUserList.get(i).getLinkUserName() + ",");
//                    } else {
//                        linkUserName.append(linkUserList.get(i).getLinkUserName());
//                    }
//                }
//                itripHotelOrder.setLinkUserName(linkUserName.toString());
//                itripHotelOrder.setBookingDays(days);
//                if (tokenString.startsWith("token:PC")) {
//                    itripHotelOrder.setBookType(0);
//                } else if (tokenString.startsWith("token:MOBILE")) {
//                    itripHotelOrder.setBookType(1);
//                } else {
//                    itripHotelOrder.setBookType(2);
//                }
//                //支付之前生成的订单的初始状态为未支付
//                itripHotelOrder.setOrderStatus(0);
//                try {
//                    //生成订单号：机器码 +日期+（MD5）（商品IDs+毫秒数+1000000的随机数）
//                    StringBuilder md5String = new StringBuilder();
//                    md5String.append(itripHotelOrder.getHotelId());
//                    md5String.append(itripHotelOrder.getRoomId());
//                    md5String.append(System.currentTimeMillis());
//                    md5String.append(Math.random() * 1000000);
//                    String md5 = MD5.getMd5(md5String.toString(), 6);
//
//                    //生成订单编号
//                    StringBuilder orderNo = new StringBuilder();
//                    orderNo.append(systemConfig.getMachineCode());
//                    orderNo.append(DateUtil.format(new Date(), "yyyyMMddHHmmss"));
//                    orderNo.append(md5);
//                    itripHotelOrder.setOrderNo(orderNo.toString());
//                    //计算订单的总金额
//                    itripHotelOrder.setPayAmount(itripHotelOrderService.getOrderPayAmount(days * itripAddHotelOrderVO.getCount(), itripAddHotelOrderVO.getRoomId()));
//
//                    Map<String, String> map = itripHotelOrderService.itriptxAddItripHotelOrder(itripHotelOrder, linkUserList);
//                    DtoUtil.returnSuccess();
//                    dto = DtoUtil.returnSuccess("生成订单成功", map);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    dto = DtoUtil.returnFail("生成订单失败", "100505");
//                }
//            } else if (flag && null == itripAddHotelOrderVO) {
//                dto = DtoUtil.returnFail("不能提交空，请填写订单信息", "100506");
//            } else {
//                dto = DtoUtil.returnFail("库存不足", "100507");
//            }
//            return dto;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return DtoUtil.returnFail("系统异常", "100508");
//        }
//    }
//
//    @ApiOperation(value = "生成订单前,获取预订信息", httpMethod = "POST",
//            protocols = "HTTP", produces = "application/json",
//            response = Dto.class, notes = "生成订单前,获取预订信息" +
//            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
//            "<p>错误码：</p>" +
//            "<p>100000 : token失效，请重登录 </p>" +
//            "<p>100510 : hotelId不能为空</p>" +
//            "<p>100511 : roomId不能为空</p>" +
//            "<p>100512 : 暂时无房</p>" +
//            "<p>100513 : 系统异常</p>")
//    @RequestMapping(value = "/getpreorderinfo", method = RequestMethod.POST, produces = "application/json")
//    @ResponseBody
//    public Dto<RoomStoreVO> getPreOrderInfo(@RequestBody ValidateRoomStoreVO validateRoomStoreVO, HttpServletRequest request) {
//        String tokenString = request.getHeader("token");
//        ItripUser currentUser = validationToken.getCurrentUser(tokenString);
//        ItripHotel hotel = null;
//        ItripHotelRoom room = null;
//        RoomStoreVO roomStoreVO = null;
//        try {
//            if (EmptyUtils.isEmpty(currentUser)) {
//                return DtoUtil.returnFail("token失效，请重登录", "100000");
//            }
//            if (EmptyUtils.isEmpty(validateRoomStoreVO.getHotelId())) {
//                return DtoUtil.returnFail("hotelId不能为空", "100510");
//            } else if (EmptyUtils.isEmpty(validateRoomStoreVO.getRoomId())) {
//                return DtoUtil.returnFail("roomId不能为空", "100511");
//            } else {
//                roomStoreVO = new RoomStoreVO();
//                hotel = hotelService.getItripHotelById(validateRoomStoreVO.getHotelId());
//                room = roomService.getItripHotelRoomById(validateRoomStoreVO.getRoomId());
//                Map param = new HashMap();
//                param.put("startTime", validateRoomStoreVO.getCheckInDate());
//                param.put("endTime", validateRoomStoreVO.getCheckOutDate());
//                param.put("roomId", validateRoomStoreVO.getRoomId());
//                param.put("hotelId", validateRoomStoreVO.getHotelId());
//                roomStoreVO.setCheckInDate(validateRoomStoreVO.getCheckInDate());
//                roomStoreVO.setCheckOutDate(validateRoomStoreVO.getCheckOutDate());
//                roomStoreVO.setHotelName(hotel.getHotelName());
//                roomStoreVO.setRoomId(room.getId());
//                roomStoreVO.setPrice(room.getRoomPrice());
//                roomStoreVO.setHotelId(validateRoomStoreVO.getHotelId());
//                List<StoreVO> storeVOList = tempStoreService.queryRoomStore(param);
//                roomStoreVO.setCount(1);
//                if (EmptyUtils.isNotEmpty(storeVOList)) {
//                    roomStoreVO.setStore(storeVOList.get(0).getStore());
//                } else {
//                    return DtoUtil.returnFail("暂时无房", "100512");
//                }
//                return DtoUtil.returnSuccess("获取成功", roomStoreVO);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return DtoUtil.returnFail("系统异常", "100513");
//        }
//    }
//
//    @ApiOperation(value = "修改订房日期验证是否有房", httpMethod = "POST",
//            protocols = "HTTP", produces = "application/json",
//            response = Dto.class, notes = "修改订房日期验证是否有房" +
//            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
//            "<p>错误码：</p>" +
//            "<p>100000 : token失效，请重登录 </p>" +
//            "<p>100515 : hotelId不能为空</p>" +
//            "<p>100516 : roomId不能为空</p>" +
//            "<p>100517 : 系统异常</p>")
//    @RequestMapping(value = "/validateroomstore", method = RequestMethod.POST, produces = "application/json")
//    @ResponseBody
//    public Dto<Map<String, Boolean>> validateRoomStore(@RequestBody ValidateRoomStoreVO validateRoomStoreVO, HttpServletRequest request) {
//        try {
//            String tokenString = request.getHeader("token");
//            ItripUser currentUser = validationToken.getCurrentUser(tokenString);
//            if (EmptyUtils.isEmpty(currentUser)) {
//                return DtoUtil.returnFail("token失效，请重登录", "100000");
//            }
//            if (EmptyUtils.isEmpty(validateRoomStoreVO.getHotelId())) {
//                return DtoUtil.returnFail("hotelId不能为空", "100515");
//            } else if (EmptyUtils.isEmpty(validateRoomStoreVO.getRoomId())) {
//                return DtoUtil.returnFail("roomId不能为空", "100516");
//            } else {
//                Map param = new HashMap();
//                param.put("startTime", validateRoomStoreVO.getCheckInDate());
//                param.put("endTime", validateRoomStoreVO.getCheckOutDate());
//                param.put("roomId", validateRoomStoreVO.getRoomId());
//                param.put("hotelId", validateRoomStoreVO.getHotelId());
//                param.put("count", validateRoomStoreVO.getCount());
//                boolean flag = tempStoreService.validateRoomStore(param);
//                Map<String, Boolean> map = new HashMap<String, Boolean>();
//                map.put("flag", flag);
//                return DtoUtil.returnSuccess("操作成功", map);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return DtoUtil.returnFail("系统异常", "100517");
//        }
//    }
//
//    @ApiOperation(value = "支付成功后查询订单信息", httpMethod = "POST",
//            protocols = "HTTP", produces = "application/json",
//            response = Dto.class, notes = "支付成功后查询订单信息" +
//            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
//            "<p>错误码：</p>" +
//            "<p>100000 : token失效，请重登录 </p>" +
//            "<p>100519 : id不能为空</p>" +
//            "<p>100520 : 获取数据失败</p>")
//    @RequestMapping(value = "/querysuccessorderinfo/{id}", method = RequestMethod.GET, produces = "application/json")
//    @ResponseBody
//    public Dto<Map<String, Boolean>> querySuccessOrderInfo(@PathVariable Long id, HttpServletRequest request) {
//        String tokenString = request.getHeader("token");
//        ItripUser currentUser = validationToken.getCurrentUser(tokenString);
//        if (EmptyUtils.isEmpty(currentUser)) {
//            return DtoUtil.returnFail("token失效，请重登录", "100000");
//        }
//        if (EmptyUtils.isEmpty(id)) {
//            return DtoUtil.returnFail("id不能为空", "100519");
//        }
//        try {
//            ItripHotelOrder order = itripHotelOrderService.getItripHotelOrderById(id);
//            if (EmptyUtils.isEmpty(order)) {
//                return DtoUtil.returnFail("没有查询到相应订单", "100519");
//            }
//            ItripHotelRoom room = roomService.getItripHotelRoomById(order.getRoomId());
//            Map<String, Object> resultMap = new HashMap<>();
//            resultMap.put("id", order.getId());
//            resultMap.put("orderNo", order.getOrderNo());
//            resultMap.put("payType", order.getPayType());
//            resultMap.put("payAmount", order.getPayAmount());
//            resultMap.put("hotelName", order.getHotelName());
//            resultMap.put("roomTitle", room.getRoomTitle());
//            return DtoUtil.returnSuccess("获取数据成功", resultMap);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return DtoUtil.returnFail("获取数据失败", "100520");
//        }
//    }
//
//    /***
//     * 10分钟执行一次 刷新订单的状态 不对外公布
//     */
//    @Scheduled(cron = "*0 0/10 * * * ?")
//    public void flushCancelOrderStatus() {
//        try {
//            boolean flag = itripHotelOrderService.flushOrderStatus(1);
//            logger.info(flag ? "刷取订单成功" : "刷单失败");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /***
//     * 2小时执行一次 刷新订单的状态 不对外公布
//     */
//    @Scheduled(cron = "0 0 0/2 * * ?")
//    public void flushOrderStatus() {
//        try {
//            logger.info("刷单程序开始执行.......");
//            boolean flag = itripHotelOrderService.flushOrderStatus(2);
//            logger.info("刷单程序执行完毕.......");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @ApiOperation(value = "修改订单的支付方式和状态", httpMethod = "POST",
//            protocols = "HTTP", produces = "application/json",
//            response = Dto.class, notes = "修改订单的支付方式和状态" +
//            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
//            "<p>错误码：</p>" +
//            "<p>100521 : 对不起，此房间不支持线下支付</p>" +
//            "<p>100522 : 修改订单失败</p>" +
//            "<p>100523 : 不能提交空，请填写订单信息 </p>" +
//            "<p>100000 : token失效，请重新登录</p>")
//    @RequestMapping(value = "/updateorderstatusandpaytype", method = RequestMethod.POST, produces = "application/json")
//    @ResponseBody
//    public Dto<Map<String, Boolean>> updateOrderStatusAndPayType(@RequestBody ModifyHotelOrderVO itripModifyHotelOrderVO, HttpServletRequest request) {
//        String tokenString = request.getHeader("token");
//        logger.debug("token name is from header : " + tokenString);
//        ItripUser currentUser = validationToken.getCurrentUser(tokenString);
//        if (null != currentUser && null != itripModifyHotelOrderVO) {
//            try {
//                ItripHotelOrder itripHotelOrder = new ItripHotelOrder();
//                itripHotelOrder.setId(itripModifyHotelOrderVO.getId());
//                //设置支付状态为：支付成功
//                itripHotelOrder.setOrderStatus(2);
//                itripHotelOrder.setPayType(itripModifyHotelOrderVO.getPayType());
//                itripHotelOrder.setModifiedBy(currentUser.getId());
//                itripHotelOrder.setModifyDate(new Date(System.currentTimeMillis()));
//                itripHotelOrderService.itriptxModifyItripHotelOrder(itripHotelOrder);
//            } catch (Exception e) {
//                e.printStackTrace();
//                return DtoUtil.returnFail("修改订单失败", "100522");
//            }
//            return DtoUtil.returnSuccess("修改订单成功");
//        } else if (null != currentUser && null == itripModifyHotelOrderVO) {
//            return DtoUtil.returnFail("不能提交空，请填写订单信息", "100523");
//        } else {
//            return DtoUtil.returnFail("token失效，请重新登录", "100000");
//        }
//    }


//    @ApiOperation(value = "根据订单ID查看个人订单详情", httpMethod = "GET",
//            protocols = "HTTP", produces = "application/json",
//            response = Dto.class, notes = "根据订单ID查看个人订单详情" +
//            "<p>订单状态(orderStatus)（0：待支付 1:已取消 2:支付成功 3:已消费 4：已点评）：</p>" +
//            "<p>订单流程：</p>" +
//            "<p>订单状态(0：待支付 2:支付成功 3:已消费 4:已点评)的流程：{\"1\":\"订单提交\",\"2\":\"订单支付\",\"3\":\"支付成功\",\"4\":\"入住\",\"5\":\"订单点评\",\"6\":\"订单完成\"}</p>" +
//            "<p>订单状态(1:已取消)的流程：{\"1\":\"订单提交\",\"2\":\"订单支付\",\"3\":\"订单取消\"}</p>" +
//            "<p>支持支付类型(roomPayType)：{\"1\":\"在线付\",\"2\":\"线下付\",\"3\":\"不限\"}</p>" +
//            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
//            "<p>错误码：</p>" +
//            "<p>100525 : 请传递参数：orderId </p>" +
//            "<p>100526 : 没有相关订单信息 </p>" +
//            "<p>100527 : 获取个人订单信息错误 </p>" +
//            "<p>100000 : token失效，请重登录 </p>")



//    @ApiOperation(value = "扫描中间表,执行库存更新操作", httpMethod = "GET",
//            protocols = "HTTP", produces = "application/json",
//            response = Dto.class, notes = "扫描中间表,执行库存更新操作" +
//            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
//            "<p>错误码：</p>" +
//            "<p>100535 : 没有查询到相应记录 </p>" +
//            "<p>100536 : 系统异常 </p>")
//    @RequestMapping(value = "/scanTradeEnd", method = RequestMethod.GET, produces = "application/json")
//    @ResponseBody
//    public Dto<Object> scanTradeEnd() {
//        Map param = new HashMap();
//        List<ItripTradeEnds> tradeEndses = null;
//        try {
//            param.put("flag", 1);
//            param.put("oldFlag", 0);
//            itripTradeEndsService.itriptxModifyItripTradeEnds(param);
//            tradeEndses = itripTradeEndsService.getItripTradeEndsListByMap(param);
//            if (EmptyUtils.isNotEmpty(tradeEndses)) {
//                for (ItripTradeEnds ends : tradeEndses) {
//                    Map<String, Object> orderParam = new HashMap<String, Object>();
//                    orderParam.put("orderNo", ends.getOrderNo());
//                    List<ItripHotelOrder> orderList = itripHotelOrderService.getItripHotelOrderListByMap(orderParam);
//                    for (ItripHotelOrder order : orderList) {
//                        Map<String, Object> roomStoreMap = new HashMap<String, Object>();
//                        roomStoreMap.put("startTime", order.getCheckInDate());
//                        roomStoreMap.put("endTime", order.getCheckOutDate());
//                        roomStoreMap.put("count", order.getCount());
//                        roomStoreMap.put("roomId", order.getRoomId());
//                        tempStoreService.updateRoomStore(roomStoreMap);
//                    }
//                }
//                param.put("flag", 2);
//                param.put("oldFlag", 1);
//                itripTradeEndsService.itriptxModifyItripTradeEnds(param);
//                return DtoUtil.returnSuccess();
//            }else{
//                return DtoUtil.returnFail("100535", "没有查询到相应记录");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return DtoUtil.returnFail("系统异常", "100536");
//        }
//    }


}
