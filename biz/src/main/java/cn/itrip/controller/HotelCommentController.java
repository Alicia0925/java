package cn.itrip.controller;

import cn.itrip.beans.dto.Dto;
import cn.itrip.beans.pojo.*;
import cn.itrip.beans.vo.LabelDicVO;
import cn.itrip.beans.vo.comment.AddCommentVO;
import cn.itrip.beans.vo.comment.HotelDescVO;
import cn.itrip.beans.vo.comment.ScoreCommentVO;
import cn.itrip.beans.vo.comment.SearchCommentVO;
import cn.itrip.beans.vo.hotel.HotelVO;
import cn.itrip.common.*;
import cn.itrip.service.hotel.HotelService;
import cn.itrip.service.hotelcomment.HotelCommentService;
import cn.itrip.service.image.ImageService;
import cn.itrip.service.labeldic.LabelDicService;
import cn.itrip.service.order.HotelOrderService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 评论Controller
 * <p>
 * 包括API接口：
 * 1、根据type 和target id 查询评论照片
 * 2、据酒店id查询酒店平均分（总体评分、位置评分、设施评分、服务评分、卫生评分）
 * 3、根据酒店id查询评论数量
 * 4、根据评论类型查询评论 分页
 * 5、上传评论图片
 * 6、删除评论图片
 * 7、新增评论信息
 * 8、查看个人评论信息
 * 9、查询出游类型列表
 * 10、新增评论信息页面获取酒店相关信息（酒店名称、酒店图片、酒店星级）
 * <p>
 * 注：错误码（100001 ——100100）
 */

@Controller
@RequestMapping("/api/comment")
public class HotelCommentController {
    @Resource
    private HotelService hotelService;
    @Resource
    private ValidationToken validationToken;
    @Resource
    private LabelDicService labelDicService;
    @Resource
    private HotelCommentService hotelCommentService;
    @Resource
    private CommonsMultipartResolver multipartResolver;
    @Resource
    private SystemConfig systemConfig;

    @Resource
    private ImageService imageService;
    @Resource
    private HotelOrderService hotelOrderService;
    private SFTPUtils sftpUtils = SFTPUtils.getInstance();


    /**
     * 新增评论信息页面内获取酒店相关信息（酒店名称、酒店星级）
     * 错误码： 100021 : 获取酒店相关信息错误
     */

    @RequestMapping(value = "/gethoteldesc/{hotelId}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Dto getHotelDesc(@PathVariable("hotelId") Long hotelId) {
        try {
            if (null != hotelId) {
                Hotel hotelInfo = hotelService.getHotelWithBLOBsById(hotelId);
                HotelDescVO hotelDescVO = new HotelDescVO();
                hotelDescVO.setHotelName(hotelInfo.getHotelName());
                hotelDescVO.setHotelLevel(hotelInfo.getHotelType());
                hotelDescVO.setHotelId(hotelId);
                return DtoUtil.returnDataSuccess(hotelDescVO);
            } else {
                return DtoUtil.returnFail("获取酒店相关信息错误", "100021");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("获取酒店相关信息错误", "100021");

        }

    }

    /**
     * 获取出游类型
     * 错误码：100019 : 获取旅游类型列表错误
     */
    @RequestMapping(value = "/gettraveltype", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Dto getTravelType() {
        Long pId = 107L;
        try {
            List<LabelDic> labelDics = labelDicService.getLabelDicS(pId);
            if (null != labelDics) {
                return DtoUtil.returnDataSuccess(labelDics);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("获取旅游类型列表错误", "100019");
        }
        return DtoUtil.returnFail("获取旅游类型列表错误", "100019");

    }

    /**
     * 添加评论
     * 若有图片需上传图片路径
     * <p>
     * 错误码：
     * 100003 : 新增评论失败
     * 100004 : 不能提交空，请填写评论信息
     * 100005 : 新增评论，订单ID不能为空
     * 100000 : token失效，请重登录
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Dto addComment(@RequestBody AddCommentVO addCommentVO, HttpServletRequest request) {
        String token = request.getHeader("token");
        User currentUser = validationToken.getCurrentUser(token);
        if (null != currentUser && null == addCommentVO) {
            return DtoUtil.returnFail("不能提交空，请填写评论信息", "100004");
        } else if (null != currentUser && null != addCommentVO) {
            if (addCommentVO.getOrderId() == null || addCommentVO.getOrderId() == 0) {
                return DtoUtil.returnFail("新增评论，订单ID不能为空", "100005");
            }
            Comment comment = new Comment();
            comment.setContent(addCommentVO.getContent());
            comment.setCreatedBy(currentUser.getId());
            comment.setCreationDate(new Date(System.currentTimeMillis()));
            comment.setOrderId(addCommentVO.getOrderId());
            comment.setHotelId(addCommentVO.getHotelId());
            comment.setPositionScore(addCommentVO.getPositionScore());
            comment.setFacilitiesScore(addCommentVO.getFacilitiesScore());
            comment.setHygieneScore(addCommentVO.getHygieneScore());
            comment.setIsOk(addCommentVO.getIsOk());
            comment.setServiceScore(addCommentVO.getServiceScore());
            comment.setIsOk(addCommentVO.getIsOk());
            comment.setIsHavingImg(addCommentVO.getIsHavingImg());
            comment.setUserId(currentUser.getId());
            comment.setTripMode(addCommentVO.getHotelId());
            comment.setProductId(addCommentVO.getProductId());
            comment.setProductType(addCommentVO.getProductType());
            List<Image> images = new ArrayList<>();
            try {
                    hotelCommentService.addHotelComment(comment);
                if (addCommentVO.getIsHavingImg() == 1) {
                    Image[] img = addCommentVO.getImages();
                    for (int i = 0; i < img.length; i++) {
                        img[i].setPosition(i + 1);
                        img[i].setType("2");
                        img[i].setCreatedBy(currentUser.getId());
                        img[i].setCreationDate(comment.getCreationDate());
                        img[i].setTargetId(comment.getId());
                        images.add(img[i]);
                    }

                    imageService.addImgs(images);

                    hotelOrderService.modifyOrderStatus(addCommentVO.getOrderId(),currentUser.getId());
                    return DtoUtil.returnSuccess("新增评论成功");
                }

            } catch (Exception e) {
                e.printStackTrace();
                return DtoUtil.returnFail("新增评论失败", "100003");
            }

        }
        return DtoUtil.returnFail("token失效，请重登录", "100000");


    }

    /**
     * 图片上传
     * 上传评论图片，最多支持4张图片同时上传，格式为：jpg、jpeg、png，大小不超过5M
     * 注意：input file 中的name不可重复 e:g : file1 、 file2 、 fileN
     * <p>
     * 错误码：
     * 100006 : 文件上传失败
     * 100007 : 上传的文件数不正确，必须是大于1小于等于4
     * 100008 : 请求的内容不是上传文件的类型
     * 100009 : 文件大小超限
     */

    @RequestMapping("/upload")
    @ResponseBody
    public Dto<Object> uploadPic(HttpServletRequest request, HttpServletResponse response) throws IllegalStateException, IOException {
        Dto<Object> dto = null;
        List<String> dataList = new ArrayList<>();

        //配置文件中已配置多文件解析器，判断 request 是否有文件上传,即多部分请求
        if (multipartResolver.isMultipart(request)) {
            //转换成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            int fileCount = 0;
            try {
                fileCount = multiRequest.getFileMap().size();
            } catch (Exception e) {
                return DtoUtil.returnFail("文件大小超限", "100009");
            }

            if (fileCount > 0 && fileCount <= 4) {
                String token = multiRequest.getHeader("token");
                User currentUser = validationToken.getCurrentUser(token);
                if (null != currentUser) {
                    //取得request中的所有文件名
                    Iterator<String> iterator = multiRequest.getFileNames();

                    while (iterator.hasNext()) {
                        try {
                            //取得上传文件
                            MultipartFile file = multiRequest.getFile(iterator.next());
                            if (file != null) {
                                //取得当前上传文件的文件名称
                                String myFileName = file.getOriginalFilename();
                                //如果名称不为“”,说明该文件存在，否则说明该文件不存在
                                if (myFileName.trim() != "" && (myFileName.toLowerCase().contains(".jpg")
                                        || myFileName.toLowerCase().contains(".jpeg")
                                        || myFileName.toLowerCase().contains(".png")
                                )) {


                                    //重命名上传后的文件名
                                    //命名规则：用户id+当前时间+随机数
                                    String suffixString = myFileName.substring(file.getOriginalFilename().indexOf("."));
                                    String fileName = currentUser.getId() + "-" + System.currentTimeMillis() + "-" + ((int) (Math.random() * 10000000)) + suffixString;
                                    //定义上传路径
                                    String path = (systemConfig.getLocalUploadPath()) + File.separator + fileName;
                                    File localFile = new File(path);
                                    file.transferTo(localFile);

                                    sftpUtils.upload(systemConfig.getFileUploadPathString(), path);
                                    dataList.add(systemConfig.getVisitImgUrlString() + fileName);
                                }
                            }
                        } catch (Exception e) {
                            continue;
                        }
                    }
                    dto = DtoUtil.returnSuccess("文件上传成功", dataList);
                } else {
                    dto = DtoUtil.returnFail("文件上传失败", "100006");
                }
            } else {
                dto = DtoUtil.returnFail("上传的文件数不正确，必须是大于1小于等于4", "100007");
            }
        } else {
            dto = DtoUtil.returnFail("请求的内容不是上传文件的类型", "100008");
        }
        return dto;
    }

    /**
     * 图片删除接口
     * 错误码：
     * 100010 : 文件不存在，删除失败
     * 00000 : token失效，请重登录
     */
    @RequestMapping(value = "/delpic", produces = "application/json", method = RequestMethod.POST)
    @ResponseBody

    public Dto<Object> delPic(@RequestParam String imgName, HttpServletRequest request) throws IllegalStateException, IOException {
        String token = request.getHeader("token");
        User currentUser = validationToken.getCurrentUser(token);
        Dto<Object> dto = null;
        if (null != currentUser) {
            try {
                sftpUtils.delete(systemConfig.getFileUploadPathString(), imgName);
                dto = DtoUtil.returnSuccess("删除成功");
            } catch (Exception e) {
                dto = DtoUtil.returnFail("文件不存在，删除失败", "100010");
            }

        } else {
            dto = DtoUtil.returnFail("token失效，请重登录", "100000");
        }
        return dto;
    }

    /**
     * 获取评论图片
     * <p>
     * 错误码：
     * 100012：获取评论图片失败
     * 100013：评论id不能为空
     */
    @RequestMapping(value = "/getimg/{targetId}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Dto getImgByTargetId(@PathVariable Long targetId) {
        if (null != targetId) {
            List<Image> list = null;
            try {
                list = imageService.getImgListByConditions(targetId, "2");
                return DtoUtil.returnSuccess("获取评论图片成功", list);
            } catch (Exception e1) {
                e1.printStackTrace();
                return DtoUtil.returnFail("获取评论图片失败", ErrorCode.BIZ_GETCOMMENTIMG_ERROR);
            }
        } else {
            return DtoUtil.returnFail("评论id不能为空", ErrorCode.BIZ_UNKNOWN_COMMENTID);
        }
    }


    @ApiOperation(value = "据酒店id查询酒店平均分", httpMethod = "GET",
            protocols = "HTTP", produces = "application/json",
            response = Dto.class, notes = "总体评分、位置评分、设施评分、服务评分、卫生评分" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>错误码：</p>" +
            "<p>100001 : 获取评分失败 </p>" +
            "<p>100002 : hotelId不能为空</p>")
    @RequestMapping(value = "/gethotelscore/{hotelId}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Dto<Object> getHotelScore(@ApiParam(required = true, name = "hotelId", value = "酒店ID")
                                     @PathVariable String hotelId) {
        Dto<Object> dto = new Dto<Object>();
        if (null != hotelId && !"".equals(hotelId)) {
            ScoreCommentVO itripScoreCommentVO = null;
            try {
                itripScoreCommentVO = hotelCommentService.getCommentAvgScore(Long.valueOf(hotelId));
                dto = DtoUtil.returnSuccess("获取评分成功", itripScoreCommentVO);
            } catch (Exception e) {
                e.printStackTrace();
                dto = DtoUtil.returnFail("获取评分失败", ErrorCode.BIZ_GETHOTELSCORE_ERROR);
            }
        } else {
            dto = DtoUtil.returnFail("hotelId不能为空", ErrorCode.BIZ_UNKNOWN_HOTELID4);
        }
        return dto;
    }


    @ApiOperation(value = "根据酒店id查询各类评论数量", httpMethod = "GET",
            protocols = "HTTP", produces = "application/json",
            response = Dto.class, notes = "根据酒店id查询评论数量（全部评论、值得推荐、有待改善、有图片）" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>错误码：</p>" +
            "<p>100014 : 获取酒店总评论数失败 </p>" +
            "<p>100015 : 获取酒店有图片评论数失败</p>" +
            "<p>100016 : 获取酒店有待改善评论数失败</p>" +
            "<p>100017 : 获取酒店值得推荐评论数失败</p>" +
            "<p>100018 : 参数hotelId为空</p>")
    @RequestMapping(value = "/getcount/{hotelId}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Dto<Object> getCommentCountByType(@ApiParam(required = true, name = "hotelId", value = "酒店ID")
                                             @PathVariable String hotelId) {
        Dto<Object> dto = new Dto<Object>();
        Integer count = 0;
        Map<String, Integer> countMap = new HashMap<String, Integer>();
        Map<String, Object> param = new HashMap<String, Object>();
        if (null != hotelId && !"".equals(hotelId)) {
            param.put("hotelId", hotelId);
            count = getCommentCountByQuery(param);
            if (count != -1) {
                countMap.put("allcomment", count);
            } else {
                return DtoUtil.returnFail("获取酒店总评论数失败", ErrorCode.BIZ_GETALLCOMMENT_ERROR);
            }
            //0：有待改善 1：值得推荐
            param.put("isOk", 1);
            count = getCommentCountByQuery(param);
            if (count != -1) {
                countMap.put("isok", count);
            } else {
                return DtoUtil.returnFail("获取酒店值得推荐评论数失败", ErrorCode.BIZ_GETISOKCOMMENT_ERROR);
            }
            //0：有待改善 1：值得推荐
            param.put("isOk", 0);
            count = getCommentCountByQuery(param);
            if (count != -1) {
                countMap.put("improve", count);
            } else {
                return DtoUtil.returnFail("获取酒店有待改善评论数失败", ErrorCode.BIZ_GETISNOTOKCOMMENT_ERROR);
            }

            //0:无图片 1:有图片
            param.put("isHavingImg", 1);
            param.put("isOk", null);
            count = getCommentCountByQuery(param);
            if (count != -1) {
                countMap.put("havingimg", count);
            } else {
                return DtoUtil.returnFail("获取酒店有图片评论数失败", ErrorCode.BIZ_GETIMGCOMMENT_ERROR);
            }

        } else {
            return DtoUtil.returnFail("参数hotelId为空", ErrorCode.BIZ_UNKNOWN_HOTELID5);
        }
        dto = DtoUtil.returnSuccess("获取酒店各类评论数成功", countMap);
        return dto;
    }

    public Integer getCommentCountByQuery(Map<String, Object> param) {
        Integer count = -1;
        try {
            count = hotelCommentService.getCommentCount(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }


    @ApiOperation(value = "根据评论类型查询评论列表，并分页显示", httpMethod = "POST",
            protocols = "HTTP",produces = "application/json",
            response = Dto.class,notes = "根据评论类型查询评论列表，并分页显示"+
            "<p>参数数据e.g：</p>" +
            "<p>全部评论：{\"hotelId\":10,\"isHavingImg\":-1,\"isOk\":-1,\"pageSize\":5,\"pageNo\":1}</p>" +
            "<p>有图片：{\"hotelId\":10,\"isHavingImg\":1,\"isOk\":-1,\"pageSize\":5,\"pageNo\":1}</p>" +
            "<p>值得推荐：{\"hotelId\":10,\"isHavingImg\":-1,\"isOk\":1,\"pageSize\":5,\"pageNo\":1}</p>" +
            "<p>有待改善：{\"hotelId\":10,\"isHavingImg\":-1,\"isOk\":0,\"pageSize\":5,\"pageNo\":1}</p>" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>错误码：</p>"+
            "<p>100020 : 获取评论列表错误 </p>")
    @RequestMapping(value = "/getcommentlist",method=RequestMethod.POST,produces = "application/json")
    @ResponseBody
    public Dto<Object> getCommentList(@RequestBody SearchCommentVO searchCommentVO){
        Dto<Object> dto = new Dto<Object>();
        Map<String,Object> param=new HashMap<>();
        if(searchCommentVO.getIsOk() == -1){
            searchCommentVO.setIsOk(null);
        }
        if(searchCommentVO.getIsHavingImg() == -1){
            searchCommentVO.setIsHavingImg(null);
        }
        param.put("hotelId",searchCommentVO.getHotelId());
        param.put("isHavingImg",searchCommentVO.getIsHavingImg());
        param.put("isOk",searchCommentVO.getIsOk());
        try{
            Page page = hotelCommentService.getCommentPageByMap(param,
                    searchCommentVO.getPageNo(),
                    searchCommentVO.getPageSize());
            dto = DtoUtil.returnDataSuccess(page);
        }catch (Exception e){
            e.printStackTrace();
            dto = DtoUtil.returnFail("获取评论列表错误",ErrorCode.BIZ_GETCOMMENTLIST_ERROR);
        }
        return dto;
    }




}
