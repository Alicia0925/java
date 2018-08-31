package cn.itrip.service.hotelcomment;

import cn.itrip.beans.pojo.Comment;
import cn.itrip.beans.pojo.Hotel;
import cn.itrip.beans.pojo.Image;
import cn.itrip.beans.vo.comment.ScoreCommentVO;
import cn.itrip.common.BigDecimalUtil;
import cn.itrip.dao.comment.CommentMapper;
import cn.itrip.dao.hotelorder.HotelOrderMapper;
import cn.itrip.dao.image.ImageMapper;
import cn.itrip.service.image.ImageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service
public class HotelCommentServiceImpl implements HotelCommentService {
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private ImageMapper imageMapper;
    @Resource
    private HotelOrderMapper hotelOrderMapper;

    @Override
    public boolean addHotelComment(Comment comment, List<Image> images) throws Exception {
        if (null != comment) {
            //计算综合评分，综合评分=(设施+卫生+位置+服务)/4
            float score = 0;
            int sum = comment.getFacilitiesScore() + comment.getHygieneScore() + comment.getPositionScore() + comment.getServiceScore();
            score = BigDecimalUtil.OperationASMD(sum, 4, BigDecimalUtil.BigDecimalOprations.divide, 1, BigDecimal.ROUND_DOWN).floatValue();
            //对结果四舍五入
            comment.setScore(Math.round(score));
            Long commentId = 0L;
            if (commentMapper.insertSelective(comment) > 0) {
                commentId = comment.getId();

                if (null != images && images.size() > 0 && commentId > 0) {
                    for (Image image : images) {
                        image.setTargetId(commentId);
                        imageMapper.insertSelective(image);
                    }
                }
                //更新订单表-订单状态为4（已评论）
                hotelOrderMapper.updateHotelOrderStatus(comment.getOrderId(), comment.getCreatedBy());
                return true;
            }
        }
        return false;
    }

    //根据酒店的id查询并计算所有点评的位置、设施、服务、卫生和综合评分
    @Override
    public ScoreCommentVO getCommentAvgScore(Long hotelId) throws Exception {
        return commentMapper.getCommentAvgScore(hotelId);
    }

    //根据酒店id查询各类评论数量
    @Override
    public Integer getCommentCount(Comment comment) throws Exception {
        return commentMapper.getCommentCount(comment);
    }
}
