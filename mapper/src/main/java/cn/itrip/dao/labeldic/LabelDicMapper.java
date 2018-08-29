package cn.itrip.dao.labeldic;

import cn.itrip.beans.pojo.LabelDic;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabelDicMapper {

    /**
     * 查询父类ID是酒店特色的标签字典列表(parentId是酒店特色16)
     * @return 返回标签字典表集合
     * @throws Exception 若有异常抛出
     */
    List<LabelDic> selectLabelDicS() throws Exception;



    //以下是自动生成CURD
    int deleteByPrimaryKey(Long id);

    int insert(LabelDic record);

    int insertSelective(LabelDic record);

    LabelDic selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(LabelDic record);

    int updateByPrimaryKey(LabelDic record);
}