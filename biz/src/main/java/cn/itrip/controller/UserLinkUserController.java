
package cn.itrip.controller;

import cn.itrip.beans.dto.Dto;
import cn.itrip.beans.pojo.User;
import cn.itrip.beans.pojo.UserLinkUser;
import cn.itrip.beans.vo.UserLinkUserVo;
import cn.itrip.beans.vo.userlink.AddUserLinkUserVO;
import cn.itrip.beans.vo.userlink.ModifyUserLinkUserVO;
import cn.itrip.beans.vo.userlink.SearchUserLinkUserVO;
import cn.itrip.common.DtoUtil;
import cn.itrip.common.EmptyUtils;
import cn.itrip.common.ValidationToken;
import cn.itrip.service.order.OrderLinkUserService;
import cn.itrip.service.userlinkuser.UserLinkUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 用户信息Controller
 * <p>
 * 包括API接口：
 * 1、根据UserId、联系人姓名查询常用联系人接口
 * 2、指定用户下添加联系人
 * 3、修改指定用户下的联系人信息
 * 4、删除指定用户下的联系人信息
 * <p>
 * 注：错误码（100401 ——100500）
 */

@Controller
@RequestMapping("/api/userinfo")
public class UserLinkUserController {
    @Resource
    private UserLinkUserService userLinkUserService;
    @Resource
    private ValidationToken validationToken;
    @Resource
    private OrderLinkUserService orderLinkUserService;


    /**
     * 查询常用联系人信息(可根据联系人姓名进行模糊查询)
     * <p>
     * 错误码：
     * 100401 : 获取常用联系人信息失败
     * 100000 : token失效，请重登录
     */
    @RequestMapping(value = "/queryuserlinkuser", method = RequestMethod.POST)
    public @ResponseBody
    Dto getUserLinkUserList(@RequestBody SearchUserLinkUserVO searchUserLinkUserVO, HttpServletRequest request) {
        String token = request.getHeader("token");
        User currentUser = validationToken.getCurrentUser(token);
        List<UserLinkUser> linkUsers = null;
        String linkUserName = (null == searchUserLinkUserVO) ? null : searchUserLinkUserVO.getLinkUserName();
        Dto dto = new Dto();
        if (null != currentUser) {
            try {
                linkUsers = userLinkUserService.getUserLinkUser(currentUser.getId(), linkUserName);
                return DtoUtil.returnSuccess("获取常用联系人信息成功", linkUsers);
            } catch (Exception e) {
                e.printStackTrace();
                return DtoUtil.returnFail("获取常用联系人信息失败", "100401");
            }
        } else {
            return DtoUtil.returnFail("token失效，请重新登录", "100000");

        }

    }


    /**
     * 新增常用联系人信息
     * 错误码：
     * 100411 : 新增常用联系人失败
     * 100412 : 不能提交空，请填写常用联系人信息
     * 100000 : token失效，请重登录
     */
    @RequestMapping(value = "/adduserlinkuser", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Dto<Object> addUserLinkUser(@RequestBody AddUserLinkUserVO addUserLinkUserVO, HttpServletRequest request) {
        String token = request.getHeader("token");
        User currentUser = validationToken.getCurrentUser(token);
        if (null != currentUser && null != addUserLinkUserVO) {
            UserLinkUser userLinkUser = new UserLinkUser();
            userLinkUser.setLinkUserName(addUserLinkUserVO.getLinkUserName());
            userLinkUser.setLinkIdCardType(addUserLinkUserVO.getLinkIdCardType());
            userLinkUser.setLinkIdCard(addUserLinkUserVO.getLinkIdCard());
            userLinkUser.setUserId(currentUser.getId());
            userLinkUser.setLinkPhone(addUserLinkUserVO.getLinkPhone());
            userLinkUser.setCreatedBy(currentUser.getId());
            userLinkUser.setCreationDate(new Date(System.currentTimeMillis()));
            try {
                userLinkUserService.addUserLinkUser(userLinkUser);
            } catch (Exception e) {
                e.printStackTrace();
                return DtoUtil.returnFail("新增常用联系人失败", "100411");
            }
            return DtoUtil.returnSuccess("新增常用联系人成功");
        } else if (null != currentUser && null == addUserLinkUserVO) {
            return DtoUtil.returnFail("不能提交空，请填写常用联系人信息", "100412");
        } else {
            return DtoUtil.returnFail("token失效，请重新登录", "100000");
        }
    }


    /**
     * 修改常用联系人信息
     * 错误码：
     * 100421 : 修改常用联系人失败
     * 100422 : 不能提交空，请填写常用联系人信息
     * 100000 : token失效，请重登录
     */
    @RequestMapping(value = "/modifyuserlinkuser", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Dto<Object> updateUserLinkUser(@RequestBody ModifyUserLinkUserVO itripModifyUserLinkUserVO, HttpServletRequest request) {
        String tokenString = request.getHeader("token");
        User currentUser = validationToken.getCurrentUser(tokenString);
        if (null != currentUser && null != itripModifyUserLinkUserVO) {
            UserLinkUser userLinkUser = new UserLinkUser();
            userLinkUser.setId(itripModifyUserLinkUserVO.getId());
            userLinkUser.setLinkUserName(itripModifyUserLinkUserVO.getLinkUserName());
            userLinkUser.setLinkIdCardType(itripModifyUserLinkUserVO.getLinkIdCardType());
            userLinkUser.setLinkIdCard(itripModifyUserLinkUserVO.getLinkIdCard());
            userLinkUser.setUserId(currentUser.getId());
            userLinkUser.setLinkPhone(itripModifyUserLinkUserVO.getLinkPhone());
            userLinkUser.setModifiedBy(currentUser.getId());
            userLinkUser.setModifyDate(new Date(System.currentTimeMillis()));
            try {
                userLinkUserService.modifyUserLinkUserById(userLinkUser);
            } catch (Exception e) {
                e.printStackTrace();
                return DtoUtil.returnFail("修改常用联系人失败", "100421");
            }
            return DtoUtil.returnSuccess("修改常用联系人成功");
        } else if (null != currentUser && null == itripModifyUserLinkUserVO) {
            return DtoUtil.returnFail("不能提交空，请填写常用联系人信息", "100422");
        } else {
            return DtoUtil.returnFail("token失效，请重新登录", "100000");
        }
    }

    /**
     * (页面无法发送参数？？
     * 批量删除常用联系人信息)
     *  删除单个常用联系人信息
     * 错误码：
     * 100431 : 所选的常用联系人中有与某条待支付的订单关联的项，无法删除
     * 100432 : 删除常用联系人失败
     * 100433 : 请选择要删除的常用联系人
     * 100000 : token失效，请重登录
     */


    @RequestMapping(value = "/deluserlinkuser", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Dto<Object> delUserLinkUserById(Long ids, HttpServletRequest request) {
        String token = request.getHeader("token");
        User currentUser = validationToken.getCurrentUser(token);
        if (null != currentUser && EmptyUtils.isNotEmpty(ids)) {
            try {
                //查询该用户所有未支付订单的旅客id
                List<Long> linkUserIds = orderLinkUserService.getOrderLinkUserIdByOrderId(currentUser.getId(), 0);

                if (linkUserIds.contains(ids)) {
                    return DtoUtil.returnFail("所选的常用联系人中有与某条待支付的订单关联的项，无法删除", "100431");
                } else {
                    UserLinkUser userLinkUser = new UserLinkUser();
                    userLinkUser.setId(ids);
                    userLinkUserService.deleteUserLinkUserById(userLinkUser);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return DtoUtil.returnFail("删除常用联系人失败", "100432");
            }
            return DtoUtil.returnSuccess("删除常用联系人成功");
        } else if (null != currentUser && EmptyUtils.isEmpty(ids)) {
            return DtoUtil.returnFail("请选择要删除的常用联系人", "100433");
        } else {
            return DtoUtil.returnFail("token失效，请重新登录", "100000");
        }
    }
}
