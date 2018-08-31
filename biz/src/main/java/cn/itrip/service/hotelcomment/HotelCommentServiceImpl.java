package cn.itrip.service.hotelcomment;

import cn.itrip.beans.pojo.Comment;
import cn.itrip.beans.vo.comment.ScoreCommentVO;
import cn.itrip.dao.comment.CommentMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
        return commentMapper.getCommentAvgScore(hotelId);
    }
}
