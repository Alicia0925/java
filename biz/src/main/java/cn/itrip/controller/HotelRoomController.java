package cn.itrip.controller;


import cn.itrip.beans.dto.Dto;
import cn.itrip.beans.pojo.LabelDic;
import cn.itrip.common.DtoUtil;
import cn.itrip.service.labeldic.LabelDicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@Api(value = "API", basePath = "/http://api.itrap.com/api")
@RequestMapping(value = "/api/hotelroom")
public class HotelRoomController {
    @Resource
    private LabelDicService labelDicService;


    @ApiOperation(value = "查询酒店房间床型列表", httpMethod = "GET",
            protocols = "HTTP", produces = "application/json",
            response = Dto.class, notes = "查询酒店床型列表" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>错误码：</p>" +
            "<p>100305 : 获取酒店房间床型失败</p>")
    @RequestMapping(value = "/queryhotelroombed", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Dto<Object> queryHotelRoomBed() {
        try {
            List<LabelDic> itripLabelDicList = labelDicService.getLabelDicS(1L);
            return DtoUtil.returnSuccess("获取成功", itripLabelDicList);
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("获取床型失败", "100305");
        }
    }


}
