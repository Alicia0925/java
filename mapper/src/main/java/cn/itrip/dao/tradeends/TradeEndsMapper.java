package cn.itrip.dao.tradeends;

import cn.itrip.beans.pojo.TradeEnds;

public interface TradeEndsMapper {





    //以下是自动生成CURD
    int deleteByPrimaryKey(Long id);

    int insert(TradeEnds record);

    int insertSelective(TradeEnds record);

    TradeEnds selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TradeEnds record);

    int updateByPrimaryKey(TradeEnds record);

    void insertTradeEnds(TradeEnds tradeEnds);
}