package cn.itrip.common;

/**
 * 系统错误编码，根据业务定义如下
 * <br/>
 * 酒店主业务biz：1开头（10000）<br/>
 * 评论：10001 ——10100<br/>
 * 酒店详情：10101 ——10200<br/>
 * 订单：10201 ——10400<br/>
 * 搜索search：2开头（20000）<br/>
 * 认证auth：3开头（30000）<br/>
 * 支付trade：4开头（40000）<br/>
 * @author hduser
 *
 */
public class ErrorCode {

	/*认证模块错误码-start*/
	public final static String AUTH_UNKNOWN="30000";
	public final static String AUTH_USER_ALREADY_EXISTS="30001";//用户已存在
	public final static String AUTH_AUTHENTICATION_FAILED="30002";//认证失败
	public final static String AUTH_PARAMETER_ERROR="30003";//用户名密码参数错误，为空
	public final static String AUTH_ACTIVATE_FAILED="30004";//邮件注册，激活失败
	public final static String AUTH_REPLACEMENT_FAILED="30005";//置换token失败
	public final static String AUTH_TOKEN_INVALID="30006";//token无效
	public static final String AUTH_ILLEGAL_USERCODE = "30007";//非法的用户名
	public static final String AUTH_ILLEGAL_EMAIL = "30008";//非法的邮箱格式

	
	/*认证模块错误码-end*/


	/*search模块错误码-start*/
	public final static String SEARCH_SYSTEM_ERROR="20001";//系统异常，获取失败
	public final static String SEARCH_UNKNOWN_DESTINATION="20002";//目的地不能为空
	public final static String SEARCH_UNKNOWN_CITYID="20002";//城市id不能为空

	/*search模块错误码-end*/


	/*biz模块错误码-start*/
	public final static String BIZ_UNKNOWN_TYPE="10201";//type不能为空(1:国内 2:国外)
	public final static String BIZ_SYSTEM_ERROR="10205";//酒店特色列表获取失败
	public final static String BIZ_UNKNOWN_CITYID="10203";//cityId不能为空
	public final static String BIZ_GETTRADINGAREA_ERROR="10204";//城市商圈获取失败
	public final static String BIZ_GETHOTELDESC_ERROR="100214";//获取酒店视频文字描述失败
	public final static String BIZ_UNKNOWN_HOTELID="100215";//酒店id不能为空
	public final static String BIZ_GETBEDTYPE_ERROR="100305";//获取床型失败
	public final static String BIZ_UNKNOWN_HOTELID_HOTELTIME="100303";//酒店id不能为空,酒店入住及退房时间不能为空,入住时间不能大于退房时间
	public final static String BIZ_GETHOTELROOM_ERROR="100304";//获取酒店房型列表失败
	public final static String BIZ_UNKNOWN_HOTELID1="10210";//酒店id不能为空
	public final static String BIZ_GETDETAILS_ERROR="10211";//系统异常,获取酒店介绍失败
	public final static String BIZ_UNKNOWN_HOTELID2="10206";//酒店id不能为空
	public final static String BIZ_GETHOTELFACILITIES_ERROR="10207";//系统异常,获取酒店设施信息失败
	public final static String BIZ_UNKNOWN_HOTELID3="10208";//酒店id不能为空
	public final static String BIZ_GETHOTELPOLICY_ERROR="10209";//系统异常,获取酒店政策信息失败
	public final static String BIZ_GETHOTELSCORE_ERROR="100001";//获取评分失败
	public final static String BIZ_UNKNOWN_HOTELID4="100002";//酒店id不能为空
	public final static String BIZ_GETALLCOMMENT_ERROR="100014";//获取酒店总评论数失败
	public final static String BIZ_GETIMGCOMMENT_ERROR="100015";//获取酒店有图片评论数失败
	public final static String BIZ_GETISNOTOKCOMMENT_ERROR="100016";//获取酒店有待改善评论数失败
	public final static String BIZ_GETISOKCOMMENT_ERROR="100017";//获取酒店值得推荐评论数失败
	public final static String BIZ_UNKNOWN_HOTELID5="100018";//参数hotelId为空
	public final static String BIZ_GETIMG_ERROR="100301";//获取酒店房型图片失败
	public final static String BIZ_UNKNOWN_TARGETID="100302";//酒店房型id不能为空
	public final static String BIZ_GETCOMMENTLIST_ERROR="100020";//获取评论列表错误
    public final static String BIZ_GETCOMMENTIMG_ERROR="100012";//获取评论图片失败
    public final static String BIZ_UNKNOWN_COMMENTID="100013";//评论id不能为空

	/*biz模块错误码-end*/
}
