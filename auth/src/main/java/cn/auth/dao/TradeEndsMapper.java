package cn.auth.dao;

import cn.auth.entity.TradeEnds;

public interface TradeEndsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TradeEnds record);

    int insertSelective(TradeEnds record);

    TradeEnds selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TradeEnds record);

    int updateByPrimaryKey(TradeEnds record);
}