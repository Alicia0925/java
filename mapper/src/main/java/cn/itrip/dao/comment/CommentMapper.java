package cn.itrip.dao.comment;

import cn.itrip.beans.pojo.Comment;
import cn.itrip.beans.vo.comment.ListCommentVO;
import cn.itrip.beans.vo.comment.ScoreCommentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CommentMapper {

    /**
     * 根据酒店的id查询并计算所有点评的位置、设施、服务、卫生和综合评分
     * @param hotelId 条件是酒店id
     * @return 返回结果封装到ScoreCommentVO中
     * @throws Exception 有异常抛出
     */
    ScoreCommentVO selectCommentAvgScore(@Param(value = "hotelId") Long hotelId) throws Exception;

    /**
     * 根据条件查询各类评论数量
     * @param param 参数封装到map中
     * @return 返回数量
     * @throws Exception 有异常抛出
     */
    Integer selectCommentCount(Map<String,Object> param)throws Exception;

    /**
     * 根据评论类型查询评论列表，并分页显示
     * @param param 参数封装到Map中
     * @return 返回结果集放到List
     * @throws Exception 有异常抛出
     */
    List<ListCommentVO> selectCommentListByMap(Map<String,Object> param)throws Exception;





    //以下是自动生成CURD
    int deleteByPrimaryKey(Long id);

    int insert(Comment record);

    int insertSelective(Comment record);

    Comment selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKeyWithBLOBs(Comment record);

    int updateByPrimaryKey(Comment record);
}