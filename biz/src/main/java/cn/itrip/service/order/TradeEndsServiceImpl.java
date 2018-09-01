package cn.itrip.service.order;

import cn.itrip.beans.pojo.TradeEnds;
import cn.itrip.dao.tradeends.TradeEndsMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
@Service
public class TradeEndsServiceImpl implements TradeEndsService {
    @Resource
    private  TradeEndsMapper tradeEndsMapper;



    @Override
    public void modifyTradeEnds(Map param) {

    }

    @Override
    public List<TradeEnds> getTradeEndsListByMap(Map param) {
        return null;
    }
}
