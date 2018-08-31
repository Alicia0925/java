package cn.itrip.service.hotelcomment;

import cn.itrip.beans.pojo.Comment;
import cn.itrip.beans.vo.comment.ListCommentVO;
import cn.itrip.beans.vo.comment.ScoreCommentVO;
import cn.itrip.common.Page;

import java.util.Map;

public interface HotelCommentService {
    /**
     * 添加评论
     *
     * */
    boolean addHotelComment(Comment comment)throws Exception;

    //根据酒店的id查询并计算所有点评的位置、设施、服务、卫生和综合评分
    ScoreCommentVO getCommentAvgScore(Long hotelId) throws Exception;

    //根据条件查询各类评论数量
    Integer getCommentCount(Map<String,Object> param)throws Exception;

    //根据评论类型查询评论列表，并分页显示
    Page<ListCommentVO> getCommentPageByMap(Map<String, Object> param, Integer pageNo, Integer pageSize)throws Exception;
}
