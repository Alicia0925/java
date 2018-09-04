package cn.itrip.controller;

import cn.itrip.beans.dto.Dto;
import cn.itrip.beans.vo.hotel.HotelVO;
import cn.itrip.beans.vo.hotel.SearchHotCityVO;
import cn.itrip.beans.vo.hotel.SearchHotelVO;
import cn.itrip.common.DtoUtil;
import cn.itrip.common.EmptyUtils;
import cn.itrip.common.ErrorCode;
import cn.itrip.common.Page;
import cn.itrip.service.SearchSolrHotelService;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@Controller
@RequestMapping("/api/hotellist")
public class HotelsController {
    @Resource
    private SearchSolrHotelService searchSolrHotelService;


    /**
     * 搜索酒店分页
     * @param vo 获取客户端请求的条件
     * @return 结果封装到dto中
     */
    @ApiOperation(value = "查询酒店分页", httpMethod = "POST",
            protocols = "HTTP", produces = "application/json",
            response = Dto.class, notes = "查询酒店分页(用于查询酒店列表)" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>错误码: </p>" +
            "<p>20001: 系统异常,获取失败</p>" +
            "<p>20002: 目的地不能为空</p>")
    @RequestMapping(value = "/searchItripHotelPage", produces = "application/json", method = RequestMethod.POST)
    @ResponseBody
    public Dto<Page<HotelVO>> searchHotelsPage(@RequestBody SearchHotelVO vo) {
        Page page = null;
        if (EmptyUtils.isEmpty(vo) || EmptyUtils.isEmpty(vo.getDestination())) {
            return DtoUtil.returnFail("目的地不能为空", ErrorCode.SEARCH_UNKNOWN_DESTINATION);
        }
        try {
            page = searchSolrHotelService.searchHotelPage(vo, vo.getPageNo(), vo.getPageSize());
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常,获取失败", ErrorCode.SEARCH_SYSTEM_ERROR);
        }
        return DtoUtil.returnDataSuccess(page);
    }

    @ApiOperation(value = "根据热门城市查询酒店列表", httpMethod = "POST",
            protocols = "HTTP", produces = "application/json",
            response = Dto.class, notes = "根据热门城市查询酒店列表" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>错误码: </p>" +
            "<p>20003: 城市id不能为空</p>")
    @RequestMapping(value = "/searchItripHotelListByHotCity", produces = "application/json", method = RequestMethod.POST)
    @ResponseBody
    public Dto<Page<HotelVO>> searchHotelsByHotCity(@RequestBody SearchHotCityVO vo) throws Exception {
        if (EmptyUtils.isEmpty(vo) || EmptyUtils.isEmpty(vo.getCityId())) {
            return DtoUtil.returnFail("城市id不能为空", ErrorCode.SEARCH_UNKNOWN_CITYID);
        }
        List list = searchSolrHotelService.searchHotelsByHotCity(vo.getCityId(), vo.getCount());
        return DtoUtil.returnDataSuccess(list);
    }


}
