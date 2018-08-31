package cn.itrip.service.hotelcomment;

import cn.itrip.beans.pojo.Comment;
import cn.itrip.beans.pojo.Image;
import cn.itrip.beans.vo.comment.ScoreCommentVO;

import java.util.List;

public interface HotelCommentService {
    /**
     * 添加评论
     *
     * */
    boolean addHotelComment(Comment comment,List<Image> images)throws Exception;

    //根据酒店的id查询并计算所有点评的位置、设施、服务、卫生和综合评分
    ScoreCommentVO getCommentAvgScore(Long hotelId) throws Exception;

    //根据酒店id查询各类评论数量
    Integer getCommentCount(Comment comment)throws Exception;
}
