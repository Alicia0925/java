package cn.itrip.service;

import cn.itrip.beans.vo.hotel.HotelVO;
import cn.itrip.beans.vo.hotel.SearchHotelVO;
import cn.itrip.common.Page;


import java.util.List;

/**
 * solr搜索酒店的Service
 * Created by XX on 17-5-7.
 */
public interface SearchSolrHotelService {
    /**
     * 根据条件查询酒店(分页)
     * @param vo 前端输入-查询酒店搜索条件v
     * @param curPage 页码
     * @param pageSize  每页行数
     * @return 返回结果封装到page中
     */
    Page<HotelVO> searchHotelPage(SearchHotelVO vo, Integer curPage, Integer pageSize) throws Exception;

    /**
     * 根据热门城市查询酒店
     * @param cityId 城市id
     * @param pageSize 每页行数
     * @return 返回结果封装到VO中
     */
    List<HotelVO> searchHotelsByHotCity(Integer cityId, Integer pageSize) throws Exception;

}
