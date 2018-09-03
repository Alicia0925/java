package cn.itrip.dao.tradeends;

import cn.itrip.beans.pojo.TradeEnds;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TradeEndsMapper {
  TradeEnds getTradeEndsById(@Param(value = "id") Long id)throws Exception;

     List<TradeEnds> getTradeEndsListByMap(Map<String,Object> param)throws Exception;

     Integer getTradeEndsCountByMap(Map<String,Object> param)throws Exception;

     Integer insertTradeEnds(TradeEnds tradeEnds)throws Exception;

     Integer updateTradeEnds(Map<String,Object> param)throws Exception;

     Integer deleteTradeEndsById(@Param(value = "id") Long id)throws Exception;




    //以下是自动生成CURD


    int insert(TradeEnds record);

    int updateByPrimaryKeySelective(TradeEnds record);

    int updateByPrimaryKey(TradeEnds record);


}