package cn.itrip.controller;


import cn.itrip.beans.dto.Dto;
import cn.itrip.beans.pojo.LabelDic;
import cn.itrip.beans.vo.AreaDicVO;
import cn.itrip.beans.vo.hotel.HotelVideoDescVO;
import cn.itrip.common.DtoUtil;
import cn.itrip.common.EmptyUtils;
import cn.itrip.common.ErrorCode;
import cn.itrip.service.areadic.AreaDicService;
import cn.itrip.service.hotel.HotelService;
import cn.itrip.service.labeldic.LabelDicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 酒店信息Controller
 * <p/>
 * 包括API接口：
 * 1、根据酒店id查询酒店拓展属性
 * 2、根据酒店id查询酒店介绍，酒店政策，酒店设施
 * 3、根据酒店id查询酒店特色属性列表
 * 4、根据type 和target id 查询酒店图片
 * 5、查询热门城市
 * 6、查询热门商圈列表（搜索-酒店列表）
 * 7、查询数据字典特色父级节点列表（搜索-酒店列表）
 * 8、根据酒店id查询酒店特色、商圈、酒店名称（视频文字描述）
 * <p/>
 * 注：错误码（100201 ——100300）
 * <p/>
 */
@Controller
@Api(value = "API", basePath = "/http://api.itrap.com/api")
@RequestMapping(value = "/api/hotel")
public class HotelController {

    @Resource
    private AreaDicService areaDicService;

    @Resource
    private LabelDicService labelDicService;

    @Resource
    private HotelService hotelService;


    /**
     * 查询国内外的热门城市
     *
     * @param type 1:国内 2:国外
     * @return 区域字典表的VO
     */
    @ApiOperation(value = "查询热门城市", httpMethod = "GET",
            protocols = "HTTP", produces = "application/json",
            response = Dto.class, notes = "查询国内、国外的热门城市(1:国内 2:国外)" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>错误码：</p>" +
            "<p>10201 : type不能为空(1:国内 2:国外) </p>" +
            "<p>10202 : 系统异常,获取失败</p>")
    @RequestMapping(value = "/queryhotcity/{type}", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public Dto<AreaDicVO> queryHotCity(@PathVariable Integer type) {
        List<AreaDicVO> areaDicVOs = null;
        try {
            if (EmptyUtils.isNotEmpty(type)) {
                areaDicVOs = areaDicService.getAreaDicList(type);
            } else {
                DtoUtil.returnFail("type不能为空", ErrorCode.BIZ_UNKNOWN_TYPE);
            }
        } catch (Exception e) {
            DtoUtil.returnFail("系统异常", ErrorCode.BIZ_SYSTEM_ERROR);
            e.printStackTrace();
        }
        return DtoUtil.returnDataSuccess(areaDicVOs);
    }

    /**
     * 查询酒店特色列表
     *
     * @return 返回标签字典表集合
     */
    @ApiOperation(value = "查询酒店特色列表", httpMethod = "GET",
            protocols = "HTTP", produces = "application/json",
            response = Dto.class, notes = "获取酒店特色(用于查询页列表)" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>错误码: </p>" +
            "<p>10202: 系统异常,获取失败</p>")
    @RequestMapping(value = "/queryhotelfeature", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public Dto<LabelDic> queryHotelFeature() {
        List<LabelDic> labelDicVOList = null;
        try {
            labelDicVOList = labelDicService.getLabelDicS(16L);
        } catch (Exception e) {
            DtoUtil.returnFail("系统异常", ErrorCode.BIZ_SYSTEM_ERROR);
            e.printStackTrace();
        }
        return DtoUtil.returnDataSuccess(labelDicVOList);
    }

    /**
     * 查询商圈
     * @param cityId 根据城市查询商圈
     * @return 区域字典表的VO
     */
    @ApiOperation(value = "查询商圈", httpMethod = "GET",
            protocols = "HTTP", produces = "application/json",
            response = Dto.class, notes = "根据城市查询商圈" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>错误码：</p>" +
            "<p>10203 : cityId不能为空 </p>" +
            "<p>10202 : 系统异常,获取失败</p>")
    @RequestMapping(value = "/querytradearea/{cityId}" +
            "", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public Dto<AreaDicVO> queryTradeArea(@PathVariable Long cityId) {
        List<AreaDicVO> areaDicVOList = null;
        try {
            if (EmptyUtils.isNotEmpty(cityId)) {
                areaDicVOList = areaDicService.getAreaDicListByCityId(cityId);
            } else {
                DtoUtil.returnFail("cityId不能为空", ErrorCode.BIZ_UNKNOWN_PARENT);
            }
        } catch (Exception e) {
            DtoUtil.returnFail("系统异常", ErrorCode.BIZ_SYSTEM_ERROR);
            e.printStackTrace();
        }
        return DtoUtil.returnDataSuccess(areaDicVOList);
    }


    @ApiOperation(value = "根据酒店id查询酒店特色、商圈、酒店名称", httpMethod = "GET",
            protocols = "HTTP", produces = "application/json",
            response = Dto.class, notes = "根据酒店id查询酒店特色、商圈、酒店名称（视频文字描述）" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>错误码：</p>" +
            "<p>10301 : 获取酒店视频文字描述失败 </p>" +
            "<p>10302 : 酒店id不能为空</p>")
    @RequestMapping(value = "/getvideodesc/{hotelId}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Dto<Object> getVideoDescByHotelId(@ApiParam(required = true, name = "hotelId", value = "酒店ID")
                                             @PathVariable String hotelId) {
        Dto<Object> dto = null;
        if (EmptyUtils.isNotEmpty(hotelId)) {
            HotelVideoDescVO hotelVideoDescVO = null;
            try {
                hotelVideoDescVO = hotelService.getVideoDescByHotelId(Long.parseLong(hotelId) );
                dto = DtoUtil.returnSuccess("获取酒店视频文字描述成功", hotelVideoDescVO);
            } catch (Exception e) {
                e.printStackTrace();
                dto = DtoUtil.returnFail("获取酒店视频文字描述失败", "10301");
            }

        } else {
            dto = DtoUtil.returnFail("酒店id不能为空", "10302");
        }
        return dto;
    }


}
