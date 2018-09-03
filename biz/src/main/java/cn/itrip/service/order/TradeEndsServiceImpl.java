package cn.itrip.service.order;

import cn.itrip.beans.pojo.TradeEnds;
import cn.itrip.common.Constants;
import cn.itrip.common.EmptyUtils;
import cn.itrip.common.Page;
import cn.itrip.dao.tradeends.TradeEndsMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
@Service
public class TradeEndsServiceImpl implements TradeEndsService {
    @Resource
    private  TradeEndsMapper tradeEndsMapper;

    public TradeEnds getTradeEndsById(Long id)throws Exception{
        return tradeEndsMapper.getTradeEndsById(id);
    }

    public List<TradeEnds>	getTradeEndsListByMap(Map<String,Object> param)throws Exception{
        return tradeEndsMapper.getTradeEndsListByMap(param);
    }

    public Integer getTradeEndsCountByMap(Map<String,Object> param)throws Exception{
        return tradeEndsMapper.getTradeEndsCountByMap(param);
    }

    public Integer addTradeEnds(TradeEnds tradeEnds)throws Exception{
        return tradeEndsMapper.insertTradeEnds(tradeEnds);
    }

    public Integer modifyTradeEnds(Map<String,Object> param)throws Exception{
        return tradeEndsMapper.updateTradeEnds(param);
    }

    public Integer deleteTradeEndsById(Long id)throws Exception{
        return tradeEndsMapper.deleteTradeEndsById(id);
    }

    public Page<TradeEnds> queryTradeEndsPageByMap(Map<String,Object> param, Integer pageNo, Integer pageSize)throws Exception{
        Integer total = tradeEndsMapper.getTradeEndsCountByMap(param);
        pageNo = EmptyUtils.isEmpty(pageNo) ? Constants.CURRENT_PAGE : pageNo;
        pageSize = EmptyUtils.isEmpty(pageSize) ? Constants.PAGE_SIZE : pageSize;
        Page page = new Page(pageNo, pageSize, total);
        param.put("beginPos", page.getBeginPos());
        param.put("pageSize", page.getPageSize());
        List<TradeEnds> tradeEndsList = tradeEndsMapper.getTradeEndsListByMap(param);
        page.setRows(tradeEndsList);
        return page;
    }



}
