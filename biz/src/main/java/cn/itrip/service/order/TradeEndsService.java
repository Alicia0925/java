package cn.itrip.service.order;

import cn.itrip.beans.pojo.TradeEnds;
import cn.itrip.common.Page;

import java.util.List;
import java.util.Map;

public interface TradeEndsService {


     TradeEnds getTradeEndsById(Long id)throws Exception;

     List<TradeEnds>	getTradeEndsListByMap(Map<String,Object> param)throws Exception;

     Integer getTradeEndsCountByMap(Map<String,Object> param)throws Exception;

     Integer addTradeEnds(TradeEnds tradeEnds)throws Exception;

     Integer modifyTradeEnds(Map<String,Object> param)throws Exception;

     Integer deleteTradeEndsById(Long id)throws Exception;

     Page<TradeEnds> queryTradeEndsPageByMap(Map<String,Object> param, Integer pageNo, Integer pageSize)throws Exception;
}
