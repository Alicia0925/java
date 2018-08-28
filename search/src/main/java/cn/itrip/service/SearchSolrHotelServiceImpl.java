package cn.itrip.service;

import cn.itrip.beans.vo.hotel.HotelVO;
import cn.itrip.beans.vo.hotel.SearchHotelVO;
import cn.itrip.common.Constants;
import cn.itrip.common.EmptyUtils;
import cn.itrip.common.Page;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * solr搜索酒店的Service 实现类
 *
 */
@Service
public class SearchSolrHotelServiceImpl implements SearchSolrHotelService {

    @Resource
    private HttpSolrClient httpSolrClient;


    /**
     * 根据条件查询酒店(分页)
     * @param vo 前端输入-查询酒店搜索条件v
     * @param curPage 页码
     * @param pageSize  每页行数
     * @return 返回结果封装到page中
     */
    @Override
    public Page<HotelVO> searchHotelPage(SearchHotelVO vo, Integer curPage, Integer pageSize) throws Exception {
        //查询条件封装到语句中
        SolrQuery query = new SolrQuery("*:*");

        StringBuffer tempQuery = new StringBuffer();

        int tempFlag = 0;
        //判断搜索条件是否为空
        if (EmptyUtils.isNotEmpty(vo)) {
            //判断输入目的地（国家，或是省，或是城市）是否为空
            if (EmptyUtils.isNotEmpty(vo.getDestination())) {
                tempQuery.append(" destination :" + vo.getDestination());
                tempFlag = 1;
            }
            //酒店星级
            if (EmptyUtils.isNotEmpty(vo.getHotelLevel())) {
                query.addFilterQuery("hotelLevel:" + vo.getHotelLevel() + "");
            }
            //关键词
            if (EmptyUtils.isNotEmpty(vo.getKeywords())) {
                if (tempFlag == 1) {
                    tempQuery.append(" AND keyword :" + vo.getKeywords());
                } else {
                    tempQuery.append(" keyword :" + vo.getKeywords());
                }
            }
            //酒店特征
            if (EmptyUtils.isNotEmpty(vo.getFeatureIds())) {
                StringBuffer stringBuffer = new StringBuffer("(");
                int flag = 0;
                String featureIdArray[] = vo.getFeatureIds().split(",");
                for (String featureId : featureIdArray) {
                    if (flag == 0) {
                        stringBuffer.append(" featureIds:" + "*," + featureId + ",*");
                    } else {
                        stringBuffer.append(" OR featureIds:" + "*," + featureId + ",*");
                    }
                    flag++;
                }
                stringBuffer.append(")");
                query.addFilterQuery(stringBuffer.toString());
            }
            //区域
            if (EmptyUtils.isNotEmpty(vo.getTradeAreaIds())) {
                StringBuffer stringBuffer = new StringBuffer("(");
                int flag = 0;
                String tradeAreaIdArray[] = vo.getTradeAreaIds().split(",");
                for (String tradeAreaId : tradeAreaIdArray) {
                    if (flag == 0) {
                        stringBuffer.append(" tradingAreaIds:" + "*," + tradeAreaId + ",*");
                    } else {
                        stringBuffer.append(" OR tradingAreaIds:" + "*," + tradeAreaId + ",*");
                    }
                    flag++;
                }
                stringBuffer.append(")");
                query.addFilterQuery(stringBuffer.toString());
            }
            //最高价格
            if (EmptyUtils.isNotEmpty(vo.getMaxPrice())) {
                query.addFilterQuery("minPrice:" + "[* TO " + vo.getMaxPrice() + "]");
            }
            //最低价格
            if (EmptyUtils.isNotEmpty(vo.getMinPrice())) {
                query.addFilterQuery("minPrice:" + "[" + vo.getMinPrice() + " TO *]");
            }
            //升序规则
            if (EmptyUtils.isNotEmpty(vo.getAscSort())) {
                query.addSort(vo.getAscSort(), SolrQuery.ORDER.asc);
            }
            //降序规则
            if (EmptyUtils.isNotEmpty(vo.getDescSort())) {
                query.addSort(vo.getDescSort(), SolrQuery.ORDER.desc);
            }
        }

        if (EmptyUtils.isNotEmpty(tempQuery.toString())) {
            query.setQuery(tempQuery.toString());
        }

        //设置起始页数
        Integer rows = EmptyUtils.isEmpty(pageSize) ? Constants.PAGE_SIZE : pageSize;
        Integer currPage = (EmptyUtils.isEmpty(curPage) ? Constants.CURRENT_PAGE - 1 : curPage - 1);
        Integer start = currPage * rows;
        query.setStart(start);
        //一页显示多少条
        query.setRows(rows);
        QueryResponse queryResponse = httpSolrClient.query(query);

        SolrDocumentList docs = queryResponse.getResults();
        List<HotelVO> hotelVOList = queryResponse.getBeans(HotelVO.class);

        Page<HotelVO> page = new Page(currPage+1, query.getRows(), new Long(docs.getNumFound()).intValue());
        page.setRows(hotelVOList);
        return page;
    }

    /**
     * 根据热门城市查询酒店
     * @param cityId 城市id
     * @param pageSize 每页行数
     * @return 返回结果封装到VO中
     */
    @Override
    public List<HotelVO> searchHotelsByHotCity(Integer cityId, Integer pageSize) throws Exception {
        SolrQuery query = new SolrQuery("*:*");
        if (EmptyUtils.isNotEmpty(cityId)) {
            query.addFilterQuery("cityId:" + cityId);
        } else {
            return null;
        }
        QueryResponse queryResponse = httpSolrClient.query(query);
        List<HotelVO> hotelVOList = queryResponse.getBeans(HotelVO.class);
        return hotelVOList;
    }
}
