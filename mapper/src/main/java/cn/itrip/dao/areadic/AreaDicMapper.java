package cn.itrip.dao.areadic;

import cn.itrip.beans.pojo.AreaDic;
import cn.itrip.beans.vo.AreaDicVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AreaDicMapper {

    /**
     * 查询国内外的热门城市
     * @param isChina 是否是中国
     * @return 返回结果封装到VO中
     * @throws Exception 若有异常抛出
     */
    List<AreaDicVO> selectAreaDicList(@Param(value = "isChina")Integer isChina) throws Exception;



    //以下是自动生成CURD
    int deleteByPrimaryKey(Long id);

    int insert(AreaDic record);

    int insertSelective(AreaDic record);

    AreaDic selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AreaDic record);

    int updateByPrimaryKey(AreaDic record);
}