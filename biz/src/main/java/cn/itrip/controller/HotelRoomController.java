package cn.itrip.controller;


import cn.itrip.beans.dto.Dto;
import cn.itrip.beans.pojo.HotelRoom;
import cn.itrip.beans.pojo.Image;
import cn.itrip.beans.pojo.LabelDic;
import cn.itrip.beans.vo.hotel.SearchHotelRoomVO;
import cn.itrip.common.DateUtil;
import cn.itrip.common.DtoUtil;
import cn.itrip.common.EmptyUtils;
import cn.itrip.common.ErrorCode;
import cn.itrip.service.hotelroom.HotelRoomService;
import cn.itrip.service.image.ImageService;
import cn.itrip.service.labeldic.LabelDicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

@Controller
@Api(value = "API", basePath = "/http://api.itrap.com/api")
@RequestMapping(value = "/api/hotelroom")
public class HotelRoomController {
    @Resource
    private LabelDicService labelDicService;

    @Resource
    private HotelRoomService hotelRoomService;

    @Resource
    private ImageService imageService;


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
            return DtoUtil.returnFail("获取床型失败", ErrorCode.BIZ_GETBEDTYPE_ERROR);
        }
    }


    @ApiOperation(value = "查询酒店房间列表", httpMethod = "POST",
            protocols = "HTTP", produces = "application/json",
            response = Dto.class, notes = "查询酒店房间列表" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>错误码：</p>" +
            "<p>100303 : 酒店id不能为空,酒店入住及退房时间不能为空,入住时间不能大于退房时间</p>" +
            "<p>100304 : 系统异常</p>")
    @RequestMapping(value = "/queryhotelroombyhotel", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Dto<List<HotelRoom>> queryHotelRoomByHotel(@RequestBody SearchHotelRoomVO vo) {
        List<List<HotelRoom>> hotelRoomVOList = null;
        try {
            if (EmptyUtils.isEmpty(vo.getHotelId())) {
                return DtoUtil.returnFail("酒店ID不能为空", ErrorCode.BIZ_UNKNOWN_HOTELID);
            }
            if (EmptyUtils.isEmpty(vo.getStartDate()) || EmptyUtils.isEmpty(vo.getEndDate())) {
                return DtoUtil.returnFail("必须填写酒店入住及退房时间", ErrorCode.BIZ_UNKNOWN_HOTELID_HOTELTIME);
            }
            if (EmptyUtils.isNotEmpty(vo.getStartDate()) && EmptyUtils.isNotEmpty(vo.getEndDate())) {
                if (vo.getStartDate().getTime() > vo.getEndDate().getTime()) {
                    return DtoUtil.returnFail("入住时间不能大于退房时间", ErrorCode.BIZ_UNKNOWN_HOTELID_HOTELTIME);
                }
                List<Date> dates = DateUtil.getBetweenDates(vo.getStartDate(), vo.getEndDate());
                vo.setTimesList(dates);
            }

            List<HotelRoom> originalRoomList = hotelRoomService.getHotelRoomListByQuery(vo);
            hotelRoomVOList = new ArrayList();
            for (HotelRoom roomVO : originalRoomList) {
                List<HotelRoom> tempList = new ArrayList<HotelRoom>();
                tempList.add(roomVO);
                hotelRoomVOList.add(tempList);
            }
            return DtoUtil.returnSuccess("获取成功", hotelRoomVOList);
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("获取酒店房型列表失败", ErrorCode.BIZ_GETHOTELROOM_ERROR);
        }
    }

    @ApiOperation(value = "根据targetId查询酒店房型图片(type=1)", httpMethod = "GET",
            protocols = "HTTP", produces = "application/json",
            response = Dto.class, notes = "根据酒店房型ID查询酒店房型图片" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>错误码：</p>" +
            "<p>100301 : 获取酒店房型图片失败 </p>" +
            "<p>100302 : 酒店房型id不能为空</p>")
    @RequestMapping(value = "/getimg/{targetId}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Dto<Object> getImgByTargetId(@ApiParam(required = true, name = "targetId", value = "酒店房型ID") @PathVariable String targetId) {
        Dto<Object> dto = null;
        if (null != targetId && !"".equals(targetId)) {
            List<Image> itripImageVOList = null;
            try {
                itripImageVOList = imageService.getImageList(Long.parseLong(targetId));
                dto = DtoUtil.returnSuccess("获取酒店图片房型成功", itripImageVOList);
            } catch (Exception e) {
                e.printStackTrace();
                dto = DtoUtil.returnFail("获取酒店房型图片失败", ErrorCode.BIZ_GETIMG_ERROR);
            }
        } else {
            dto = DtoUtil.returnFail("酒店房型id不能为空", ErrorCode.BIZ_UNKNOWN_TARGETID);
        }
        return dto;
    }


}
