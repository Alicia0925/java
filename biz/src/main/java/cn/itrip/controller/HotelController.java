package cn.itrip.controller;


import cn.itrip.beans.dto.Dto;
import cn.itrip.beans.pojo.HotelWithBLOBs;
import cn.itrip.beans.pojo.LabelDic;
import cn.itrip.beans.vo.AreaDicVO;
import cn.itrip.beans.vo.hotel.HotelVideoDescVO;
import cn.itrip.beans.vo.hotel.SearchDetailsHotelVO;
import cn.itrip.beans.vo.hotel.SearchFacilitiesHotelVO;
import cn.itrip.beans.vo.hotel.SearchPolicyHotelVO;
import cn.itrip.common.DtoUtil;
import cn.itrip.common.EmptyUtils;
import cn.itrip.common.ErrorCode;
import cn.itrip.service.areadic.AreaDicService;
import cn.itrip.service.hotel.HotelService;
import cn.itrip.service.labeldic.LabelDicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;



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
        List<AreaDicVO> itripAreaDicVOs = null;
        try {
            if (EmptyUtils.isNotEmpty(type)) {
                itripAreaDicVOs = areaDicService.getAreaDicList(type);
            } else {
                DtoUtil.returnFail("type不能为空", ErrorCode.BIZ_UNKNOWN_TYPE);
            }
        } catch (Exception e) {
            DtoUtil.returnFail("系统异常", ErrorCode.BIZ_SYSTEM_ERROR);
            e.printStackTrace();
        }
        return DtoUtil.returnDataSuccess(itripAreaDicVOs);
    }



    @ApiOperation(value = "查询酒店特色列表", httpMethod = "GET",
            protocols = "HTTP", produces = "application/json",
            response = Dto.class, notes = "获取酒店特色(用于查询页列表)" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>错误码: </p>" +
            "<p>10205: 系统异常,获取失败</p>")
    @RequestMapping(value = "/queryhotelfeature", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public Dto<LabelDic> queryHotelFeature() {
        List<LabelDic> itripAreaDicVOs = null;
        try {
            itripAreaDicVOs = labelDicService.getLabelDicS(16L);
        } catch (Exception e) {
            DtoUtil.returnFail("酒店特色列表获取失败", ErrorCode.BIZ_SYSTEM_ERROR);
            e.printStackTrace();
        }
        return DtoUtil.returnDataSuccess(itripAreaDicVOs);
    }


    @ApiOperation(value = "查询商圈", httpMethod = "GET",
            protocols = "HTTP", produces = "application/json",
            response = Dto.class, notes = "根据城市查询商圈" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>错误码：</p>" +
            "<p>10203 : cityId不能为空 </p>" +
            "<p>10204 : 系统异常,获取失败</p>")
    @RequestMapping(value = "/querytradearea/{cityId}" +
            "", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public Dto<AreaDicVO> queryTradeArea(@PathVariable Long cityId) {
        List<AreaDicVO> itripAreaDicVOs = null;
        try {
            if (EmptyUtils.isNotEmpty(cityId)) {
                itripAreaDicVOs = areaDicService.getAreaDicListByCityId(cityId);
            } else {
                DtoUtil.returnFail("cityId不能为空", ErrorCode.BIZ_UNKNOWN_CITYID);
            }
        } catch (Exception e) {
            DtoUtil.returnFail("城市商圈获取失败", ErrorCode.BIZ_GETTRADINGAREA_ERROR);
            e.printStackTrace();
        }
        return DtoUtil.returnDataSuccess(itripAreaDicVOs);
    }


    @ApiOperation(value = "根据酒店id查询酒店特色、商圈、酒店名称", httpMethod = "GET",
            protocols = "HTTP", produces = "application/json",
            response = Dto.class, notes = "根据酒店id查询酒店特色、商圈、酒店名称（视频文字描述）" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>错误码：</p>" +
            "<p>100214 : 获取酒店视频文字描述失败 </p>" +
            "<p>100215 : 酒店id不能为空</p>")
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
                dto = DtoUtil.returnFail("获取酒店视频文字描述失败", ErrorCode.BIZ_GETHOTELDESC_ERROR);
            }
        } else {
            dto = DtoUtil.returnFail("酒店id不能为空", ErrorCode.BIZ_UNKNOWN_HOTELID);
        }
        return dto;
    }

    @ApiOperation(value = "根据酒店id查询酒店特色和介绍", httpMethod = "GET",
            protocols = "HTTP", produces = "application/json",
            response = Dto.class, notes = "根据酒店id查询酒店特色和介绍" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>10210: 酒店id不能为空</p>" +
            "<p>10211: 系统异常,获取失败</p>")
    @RequestMapping(value = "/queryhoteldetails/{id}", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public Dto<SearchFacilitiesHotelVO> queryHotelDetails(
            @ApiParam(required = true, name = "id", value = "酒店ID")
            @PathVariable Long id) {
        List<SearchDetailsHotelVO> itripSearchDetailsHotelVOList = null;
        try {
            if (EmptyUtils.isNotEmpty(id)) {
                itripSearchDetailsHotelVOList = hotelService.getHotelDetails(id);
                return DtoUtil.returnDataSuccess(itripSearchDetailsHotelVOList);
            } else {
                return DtoUtil.returnFail("酒店id不能为空", ErrorCode.BIZ_UNKNOWN_HOTELID1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常,获取酒店介绍失败", ErrorCode.BIZ_GETDETAILS_ERROR);
        }
    }

    @ApiOperation(value = "根据酒店id查询酒店设施", httpMethod = "GET",
            protocols = "HTTP", produces = "application/json",
            response = Dto.class, notes = "根据酒店id查询酒店设施" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>10206: 酒店id不能为空</p>" +
            "<p>10207: 系统异常,获取失败</p>")
    @RequestMapping(value = "/queryhotelfacilities/{id}", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public Dto<SearchFacilitiesHotelVO> queryHotelFacilities(
            @ApiParam(required = true, name = "id", value = "酒店ID")
            @PathVariable Long id) {
        SearchFacilitiesHotelVO searchFacilitiesHotelVO = new SearchFacilitiesHotelVO();
        try {
            if (EmptyUtils.isNotEmpty(id)) {
                //获取酒店的所有信息
                HotelWithBLOBs hotelWithBLOBs = hotelService.getHotelWithBLOBsById(id);
                //将酒店的设施信息封装到VO中
                searchFacilitiesHotelVO.setFacilities(hotelWithBLOBs.getFacilities());
                return DtoUtil.returnDataSuccess(searchFacilitiesHotelVO.getFacilities());
            } else {
                return DtoUtil.returnFail("酒店id不能为空", ErrorCode.BIZ_UNKNOWN_HOTELID2);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常,获取酒店设施信息失败", ErrorCode.BIZ_GETHOTELFACILITIES_ERROR);
        }
    }

    @ApiOperation(value = "根据酒店id查询酒店政策", httpMethod = "GET",
            protocols = "HTTP", produces = "application/json",
            response = Dto.class, notes = "根据酒店id查询酒店政策" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>10208: 酒店id不能为空</p>" +
            "<p>10209: 系统异常,获取失败</p>")
    @RequestMapping(value = "/queryhotelpolicy/{id}", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public Dto<SearchFacilitiesHotelVO> queryHotelPolicy(
            @ApiParam(required = true, name = "id", value = "酒店ID")
            @PathVariable Long id) {
        SearchPolicyHotelVO searchPolicyHotelVO= new SearchPolicyHotelVO();
        try {
            if (EmptyUtils.isNotEmpty(id)) {
                //获取酒店的所有信息
                HotelWithBLOBs hotelWithBLOBs = hotelService.getHotelWithBLOBsById(id);
                //将酒店的政策信息封装到VO中
                searchPolicyHotelVO.setHotelPolicy(hotelWithBLOBs.getHotelPolicy());
                return DtoUtil.returnDataSuccess(searchPolicyHotelVO.getHotelPolicy());
            } else {
                return DtoUtil.returnFail("酒店id不能为空", ErrorCode.BIZ_UNKNOWN_HOTELID3);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常,获取酒店政策信息失败", ErrorCode.BIZ_GETHOTELPOLICY_ERROR);
        }
    }





}
