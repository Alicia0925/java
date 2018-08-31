package cn.itrip.service.hotelcomment;

import cn.itrip.beans.pojo.Comment;

public interface HotelCommentService {
    /**
     * 添加评论
     *
     * */
    boolean addHotelComment(Comment comment)throws Exception;
}
