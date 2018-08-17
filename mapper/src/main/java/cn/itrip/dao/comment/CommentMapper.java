package cn.itrip.dao.comment;

import cn.itrip.beans.pojo.Comment;

public interface CommentMapper {

    //以下是自动生成CURD
    int deleteByPrimaryKey(Long id);

    int insert(Comment record);

    int insertSelective(Comment record);

    Comment selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKeyWithBLOBs(Comment record);

    int updateByPrimaryKey(Comment record);
}