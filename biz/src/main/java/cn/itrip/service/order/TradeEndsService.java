package cn.itrip.service.order;

import cn.itrip.beans.pojo.TradeEnds;

import java.util.List;
import java.util.Map;

public interface TradeEndsService {



    void modifyTradeEnds(Map param);

    List<TradeEnds> getTradeEndsListByMap(Map param);
}
