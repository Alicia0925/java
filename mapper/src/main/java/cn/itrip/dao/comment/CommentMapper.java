package cn.itrip.dao.comment;

import cn.itrip.beans.pojo.Comment;
import cn.itrip.beans.vo.comment.ScoreCommentVO;
import org.apache.ibatis.annotations.Param;

public interface CommentMapper {

    /**
     * 根据酒店的id查询并计算所有点评的位置、设施、服务、卫生和综合评分
     * @param hotelId 条件是酒店id
     * @return 返回结果封装到ScoreCommentVO中
     * @throws Exception 有异常抛出
     */
    ScoreCommentVO getCommentAvgScore(@Param(value = "hotelId") Long hotelId) throws Exception;




    //以下是自动生成CURD
    int deleteByPrimaryKey(Long id);

    int insert(Comment record);

    int insertSelective(Comment record);

    Comment selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKeyWithBLOBs(Comment record);

    int updateByPrimaryKey(Comment record);
}