package cn.itrip.controller;

import cn.itrip.beans.dto.Dto;
import cn.itrip.beans.pojo.*;
import cn.itrip.beans.vo.order.*;
import cn.itrip.beans.vo.store.StoreVO;
import cn.itrip.common.*;
import cn.itrip.service.hotel.HotelService;
import cn.itrip.service.hotelroom.RoomService;
import cn.itrip.service.order.HotelOrderService;
import cn.itrip.service.order.HotelTempStoreService;
import cn.itrip.service.order.OrderLinkUserService;
import cn.itrip.service.order.TradeEndsService;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Resource
    private HotelTempStoreService  tempStoreService;
//
//
//    @Resource
//    private TradeEndsService  tradeEndsService;

    @Resource
    private OrderLinkUserService orderLinkUserService;
    @Resource
    private HotelService hotelService;


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

    /**
     * 生成订单
     * 错误码：
     * 100505 : 生成订单失败
     * 100506 : 不能提交空，请填写订单信息
     * 100507 : 库存不足
     * 100000 : token失效，请重登录
     * */
    @RequestMapping(value = "/addhotelorder", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Dto<Object> addHotelOrder(@RequestBody AddHotelOrderVO addHotelOrderVO, HttpServletRequest request) {
        Dto<Object> dto = new Dto<Object>();
        String token  = request.getHeader("token");
        User currentUser = validationToken.getCurrentUser(token);
        Map<String, Object> validateStoreMap = new HashMap<>();
        validateStoreMap.put("startTime", addHotelOrderVO.getCheckInDate());
        validateStoreMap.put("endTime", addHotelOrderVO.getCheckOutDate());
        validateStoreMap.put("hotelId", addHotelOrderVO.getHotelId());
        validateStoreMap.put("roomId", addHotelOrderVO.getRoomId());
        validateStoreMap.put("count", addHotelOrderVO.getCount());
        List<UserLinkUser> linkUserList = addHotelOrderVO.getLinkUser();
        if(EmptyUtils.isEmpty(currentUser)){
            return DtoUtil.returnFail("token失效，请重登录", "100000");
        }
        try {
            //判断库存是否充足
            Boolean flag = tempStoreService.validateRoomStore(validateStoreMap);
            if (flag && null != addHotelOrderVO) {
                //计算订单的预定天数
                Integer days = DateUtil.getBetweenDates(
                        addHotelOrderVO.getCheckInDate(), addHotelOrderVO.getCheckOutDate()
                ).size()-1;
                if(days<=0){
                    return DtoUtil.returnFail("退房日期必须大于入住日期", "100505");
                }
                HotelOrder hotelOrder = new HotelOrder();
                hotelOrder.setId(addHotelOrderVO.getId());
                hotelOrder.setUserId(currentUser.getId());
                hotelOrder.setOrderType(addHotelOrderVO.getOrderType());
                hotelOrder.setHotelId(addHotelOrderVO.getHotelId());
                hotelOrder.setHotelName(addHotelOrderVO.getHotelName());
                hotelOrder.setRoomId(addHotelOrderVO.getRoomId());
                hotelOrder.setCount(addHotelOrderVO.getCount());
                hotelOrder.setCheckInDate(addHotelOrderVO.getCheckInDate());
                hotelOrder.setCheckOutDate(addHotelOrderVO.getCheckOutDate());
                hotelOrder.setNoticePhone(addHotelOrderVO.getNoticePhone());
                hotelOrder.setNoticeEmail(addHotelOrderVO.getNoticeEmail());
                hotelOrder.setSpecialRequirement(addHotelOrderVO.getSpecialRequirement());
                hotelOrder.setIsNeedInvoice(addHotelOrderVO.getIsNeedInvoice());
                hotelOrder.setInvoiceHead(addHotelOrderVO.getInvoiceHead());
                hotelOrder.setInvoiceType(addHotelOrderVO.getInvoiceType());
                hotelOrder.setCreatedBy(currentUser.getId());
                StringBuilder linkUserName = new StringBuilder();
                int size = linkUserList.size();
                for (int i = 0; i < size; i++) {
                    if (i != size - 1) {
                        linkUserName.append(linkUserList.get(i).getLinkUserName() + ",");
                    } else {
                        linkUserName.append(linkUserList.get(i).getLinkUserName());
                    }
                }
                hotelOrder.setLinkUserName(linkUserName.toString());
                hotelOrder.setBookingDays(days);
                if (token.startsWith("token:PC")) {
                    hotelOrder.setBookType(0);
                } else if (token.startsWith("token:MOBILE")) {
                    hotelOrder.setBookType(1);
                } else {
                    hotelOrder.setBookType(2);
                }
                //支付之前生成的订单的初始状态为未支付
                hotelOrder.setOrderStatus(0);
                try {
                    //生成订单号：机器码 +日期+（MD5）（商品IDs+毫秒数+1000000的随机数）
                    StringBuilder md5String = new StringBuilder();
                    md5String.append(hotelOrder.getHotelId());
                    md5String.append(hotelOrder.getRoomId());
                    md5String.append(System.currentTimeMillis());
                    md5String.append(Math.random() * 1000000);
                    String md5 = MD5.getMd5(md5String.toString(), 6);

                    //生成订单编号
                    StringBuilder orderNo = new StringBuilder();
                    orderNo.append(systemConfig.getMachineCode());
                    orderNo.append(DateUtil.format(new Date(), "yyyyMMddHHmmss"));
                    orderNo.append(md5);
                    hotelOrder.setOrderNo(orderNo.toString());
                    //计算订单的总金额
                    hotelOrder.setPayAmount(hotelOrderService.getOrderPayAmount(days * addHotelOrderVO.getCount(), addHotelOrderVO.getRoomId()));

                    Map<String, String> map = hotelOrderService.addHotelOrder(hotelOrder, linkUserList);
                    DtoUtil.returnSuccess();
                    dto = DtoUtil.returnSuccess("生成订单成功", map);
                } catch (Exception e) {
                    e.printStackTrace();
                    dto = DtoUtil.returnFail("生成订单失败", "100505");
                }
            } else if (flag && null == addHotelOrderVO) {
                dto = DtoUtil.returnFail("不能提交空，请填写订单信息", "100506");
            } else {
                dto = DtoUtil.returnFail("库存不足", "100507");
            }
            return dto;
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常", "100508");
        }
    }


    /**
     * 生成订单前,获取预订信息
     * 错误码：
     * 100000 : token失效，请重登录
     * 100510 : hotelId不能为空
     * 100511 : roomId不能为空
     * 100512 : 暂时无房
     * 100513 : 系统异常
     * */
    @RequestMapping(value = "/getpreorderinfo", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Dto<RoomStoreVO> getPreOrderInfo(@RequestBody ValidateRoomStoreVO validateRoomStoreVO, HttpServletRequest request) {
        String token  = request.getHeader("token");
        User currentUser = validationToken.getCurrentUser(token);
        Hotel hotel = null;
        HotelRoom room = null;
        RoomStoreVO roomStoreVO = null;
        try {
            if (EmptyUtils.isEmpty(currentUser)) {
                return DtoUtil.returnFail("token失效，请重登录", "100000");
            }
            if (EmptyUtils.isEmpty(validateRoomStoreVO.getHotelId())) {
                return DtoUtil.returnFail("hotelId不能为空", "100510");
            } else if (EmptyUtils.isEmpty(validateRoomStoreVO.getRoomId())) {
                return DtoUtil.returnFail("roomId不能为空", "100511");
            } else {
                //库存房间信息
                roomStoreVO = new RoomStoreVO();
                hotel = hotelService.getHotelWithBLOBsById(validateRoomStoreVO.getHotelId());
                room = roomService.getHotelRoomById(validateRoomStoreVO.getRoomId());
                roomStoreVO.setCheckInDate(validateRoomStoreVO.getCheckInDate());
                roomStoreVO.setCheckOutDate(validateRoomStoreVO.getCheckOutDate());
                roomStoreVO.setHotelName(hotel.getHotelName());
                roomStoreVO.setRoomId(room.getId());
                roomStoreVO.setPrice(room.getRoomPrice());
                roomStoreVO.setHotelId(validateRoomStoreVO.getHotelId());

                //查询房间库存 list
                Map param = new HashMap();
                param.put("startTime", validateRoomStoreVO.getCheckInDate());
                param.put("endTime", validateRoomStoreVO.getCheckOutDate());
                param.put("roomId", validateRoomStoreVO.getRoomId());
                param.put("hotelId", validateRoomStoreVO.getHotelId());
                List<StoreVO> storeVOList = tempStoreService.queryRoomStore(param);

                roomStoreVO.setCount(1);
                if (EmptyUtils.isNotEmpty(storeVOList)) {
                    roomStoreVO.setStore(storeVOList.get(0).getStore());
                } else {
                    return DtoUtil.returnFail("暂时无房", "100512");
                }
                return DtoUtil.returnSuccess("获取成功", roomStoreVO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常", "100513");
        }
    }


    /**
     * 验证是否有房
     *
     * 错误码：
     * 100000 : token失效，请重登录
     * 100515 : hotelId不能为空
     * 100516 : roomId不能为空
     * 100517 : 系统异常
     *
     * */

    @RequestMapping(value = "/validateroomstore", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Dto<Map<String, Boolean>> validateRoomStore(@RequestBody ValidateRoomStoreVO validateRoomStoreVO, HttpServletRequest request) {
        try {
            String token  = request.getHeader("token");
            User currentUser = validationToken.getCurrentUser(token);
            if (EmptyUtils.isEmpty(currentUser)) {
                return DtoUtil.returnFail("token失效，请重登录", "100000");
            }
            if (EmptyUtils.isEmpty(validateRoomStoreVO.getHotelId())) {
                return DtoUtil.returnFail("hotelId不能为空", "100515");
            } else if (EmptyUtils.isEmpty(validateRoomStoreVO.getRoomId())) {
                return DtoUtil.returnFail("roomId不能为空", "100516");
            } else {
                Map param = new HashMap();
                param.put("startTime", validateRoomStoreVO.getCheckInDate());
                param.put("endTime", validateRoomStoreVO.getCheckOutDate());
                param.put("roomId", validateRoomStoreVO.getRoomId());
                param.put("hotelId", validateRoomStoreVO.getHotelId());
                param.put("count", validateRoomStoreVO.getCount());
                boolean flag = tempStoreService.validateRoomStore(param);
                Map<String, Boolean> map = new HashMap<String, Boolean>();
                map.put("flag", flag);
                return DtoUtil.returnSuccess("操作成功", map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常", "100517");
        }
    }


    /**
     * 支付成功后查询订单信息
     * 错误码：
     * 100000 : token失效，请重登录
     * 100519 : id不能为空
     * 100520 : 获取数据失败
     *
     * */
    @RequestMapping(value = "/querysuccessorderinfo/{id}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Dto<Map<String, Boolean>> querySuccessOrderInfo(@PathVariable Long id, HttpServletRequest request) {
        String token = request.getHeader("token");
        User currentUser = validationToken.getCurrentUser(token);
        if (EmptyUtils.isEmpty(currentUser)) {
            return DtoUtil.returnFail("token失效，请重登录", "100000");
        }
        if (EmptyUtils.isEmpty(id)) {
            return DtoUtil.returnFail("id不能为空", "100519");
        }
        try {
            HotelOrder order = hotelOrderService.getItripHotelOrderById(id);
            if (EmptyUtils.isEmpty(order)) {
                return DtoUtil.returnFail("没有查询到相应订单", "100519");
            }
            HotelRoom room = roomService.getHotelRoomById(order.getRoomId());
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("id", order.getId());
            resultMap.put("orderNo", order.getOrderNo());
            resultMap.put("payType", order.getPayType());
            resultMap.put("payAmount", order.getPayAmount());
            resultMap.put("hotelName", order.getHotelName());
            resultMap.put("roomTitle", room.getRoomTitle());
            return DtoUtil.returnSuccess("获取数据成功", resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("获取数据失败", "100520");
        }
    }
//
    /***
     * 10分钟执行一次 刷新订单的状态 不对外公布
     */
    @Scheduled(cron = "*0 0/10 * * * ?")
    public void flushCancelOrderStatus() {
        try {
            boolean flag = hotelOrderService.flushOrderStatus(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 2小时执行一次 刷新订单的状态 不对外公布
     */
    @Scheduled(cron = "0 0 0/2 * * ?")
    public void flushOrderStatus() {
        try {
            boolean flag = hotelOrderService.flushOrderStatus(2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
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
