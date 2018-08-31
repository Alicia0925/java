package cn.itrip.service.hotelcomment;

import cn.itrip.beans.pojo.Comment;
import cn.itrip.beans.vo.comment.ListCommentVO;
import cn.itrip.beans.vo.comment.ScoreCommentVO;
import cn.itrip.common.Constants;
import cn.itrip.common.EmptyUtils;
import cn.itrip.common.Page;
import cn.itrip.dao.comment.CommentMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class HotelCommentServiceImpl implements HotelCommentService {
    @Resource
    private CommentMapper commentMapper;

    @Override
    public boolean addHotelComment(Comment comment)throws Exception {
        return commentMapper.insertSelective(comment)>0;
    }

    //根据酒店的id查询并计算所有点评的位置、设施、服务、卫生和综合评分
    @Override
    public ScoreCommentVO getCommentAvgScore(Long hotelId) throws Exception {
        return commentMapper.selectCommentAvgScore(hotelId);
    }

    //根据酒店id查询各类评论数量
    @Override
    public Integer getCommentCount(Map<String,Object> param) throws Exception {
        return commentMapper.selectCommentCount(param);
    }

    //根据评论类型查询评论列表，并分页显示
    @Override
    public Page<ListCommentVO> getCommentPageByMap(Map<String, Object> param, Integer pageNo, Integer pageSize) throws Exception {
        Integer total = commentMapper.selectCommentCount(param);
        pageNo = EmptyUtils.isEmpty(pageNo) ? Constants.CURRENT_PAGE : pageNo;
        pageSize = EmptyUtils.isEmpty(pageSize) ? Constants.PAGE_SIZE : pageSize;
        Page page = new Page(pageNo, pageSize, total);
        param.put("beginPos", page.getBeginPos());
        param.put("pageSize", page.getPageSize());
        List<ListCommentVO> itripCommentList = commentMapper.selectCommentListByMap(param);
        page.setRows(itripCommentList);
        return page;
    }
}
