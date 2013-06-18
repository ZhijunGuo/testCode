package com.example.galaxy08.http;

import com.example.galaxy08.finals.SysConstants;

/**
 * 所有网络api地址 2013/04/15
 * 
 */
public class UrlConstants {

	public static final String HOST = SysConstants.SERVER;

	public static final String HTTPS_HOST = SysConstants.SERVER_HTTPS;

	/***************************************************************************/
	/***************************** 注册登陆 ************************************/
	/***************************************************************************/
	// 登陆
	public static final String LOGIN = HOST + "login";
	// 登陆
	public static final String REQUEST_NEW_LOGIN = HTTPS_HOST + "newlogin";
	// 注册
	public static final String REGISTER = HOST + "register";
	// 注册使用账户
	public static final String REGISTER_TRIAL = HOST + "register/trial";
	// 注册流程第二步：设置密码、生成账号并登录
	public static final String REGISTER_CONFIRM = HOST + "register/confirm";
	// 注册流程第二步：设置密码、生成账号并登录
	public static final String VERIFY_CODE = HOST + "register/verifycode";

	public static final String REQUEST_CONFIRM = HTTPS_HOST
			+ "register/confirm";

	// 删除试用账号
	public static final String DELETE_TRIAL = HOST + "deletetrial";
	// 找回密码第一步：根据登录名类型发送验证码
	public static final String FIND_PASSWORD_NEW = HTTPS_HOST
			+ "newfindpassword";
	// 找回密码第二步：校验验证码，设置新密码。
	public static final String RESET_PASSWORD_NEW = HOST + "newresetpassword";
	// 修改密码
	public static final String MODIFY_PASSWORD = HOST + "modifypassword";
	// 更改登录名
	public static final String TO_BIND_USERNAME = HOST + "tobindusername";
	// 邮箱或手机号登录
	public static final String LOGIN_NEW = HOST + "newlogin";

	// 登出
	public static final String LOGOUT = HOST + "logout";

	// 验证密码
	public static final String VALIDATE_PASSWORD = HOST + "validatepassword";

	// 发送验证码
	public static final String REQUEST_VERIFY = HTTPS_HOST
			+ "register/verifycode";

	// 找回密码 重置密码-- 老接口，已经弃用
	public static final String GETPASSWORD_RESETPASSWORD = HTTPS_HOST
			+ "newfindpassword";

	// 找回密码 重置密码
	public static final String GETPASSWORD_RESETPASSWORD_NEW = HTTPS_HOST
			+ "newresetpassword";

	// 绑定微博账号完成 校验验证码、生成账号并登录
	public static final String REQUEST_WB_CONFIRM = HTTPS_HOST
			+ "register/wb-confirm";
	
	//微博注册成功  ---发一条微博
	public static final String REQUEST_WB_SEND = HTTPS_HOST + "register/wb-send";

	// 用手机号注册
	public static final String PHONENUMBER_REGISTER = HTTPS_HOST
			+ "register/register";

	public static final String ACTIVE_CLIENT = HOST + "statistics/activeClient";

	// 修改密码
	public static final String REQUEST_PASSWORD_RESET = HTTPS_HOST
			+ "modifypassword"; 

	// 第3方登陆接口
	public static final String REQUEST_PASSWORD_FORTHIRD = HTTPS_HOST
			+ "setpasswordforthird"; 

	// 设置我的个性签名
	public static final String SIGNATURE = HOST + "signature";

	// 获取某个联系人的个性签名
	public static final String GET_SIGNATURE = HOST + "getSignature";

	// 获取用户排名信息
	public static final String USER_RANK = HOST + "rank/userrank";

	// 将试用账户中的名片信息并入指定正式账户
	public static final String MERGE_ACCOUNT = HOST + "mergeaccount";

	// 获取logo图片url
	public static final String LOAD_PIC = HOST + "loadpic";

	public static final String BACK_TO_FRONT = HOST + "statistics/backtofront";

	public static final String UPLOAD_SIGN = HOST + "signature";

	public static final String UPDATE_NOTIFY = HOST + "card/noteMycardUpdate";

	// 查询验证记录当前状态，是否已经识别或无法识别；如果识别则返回对应的名片记录
	public static final String QUERY_CARD = HOST + "querycard";

	// 修改登陆手机号
	public static final String BIND_USERNAME = HOST + "tobindusername";

	// 修改登陆验证码
	public static final String CONFIRM_BIND_USERNAME = HOST
			+ "confirmbindusername";

	// 用户上传头像
	public static final String UPLOAD_IMG = HOST + "uploadImg";

	// 更新自己名片
	public static final String Upload_MY_Card = HOST + "uploadMyCard";

	// 获取收藏名片的更新
	public static final String QUERY_UPDATE = HOST + "queryupdate";

	// 关联名片和分组,将多张名片加入一个分支
	public static final String RELATED_GROUP = HOST + "relatedgroup";

	// 获取某个用户的分组
	public static final String GET_GROUPS = HOST + "getgroup";

	// 修改分组名
	public static final String MODIFY_GROUP_NAME = HOST + "modifygroup";

	// 邀请开关是否已打开
	public static final String INVITE_ACTIVE = HOST + "activeinvite";

	// 获取机器识别的数据
	public static final String QUERY_MD = HOST + "query/md";

	// 获取名片拥有者是否收藏了我的名片
	public static final String IS_MY_CARD_STORED = HOST + "ref";

	// 获取所有收藏了我的名片的人数量
	public static final String COLLECT_MY_COUNT = HOST + "collectmycount";

	// 意见建议反馈
	public static final String SUGGEST = HOST + "suggest";

	// 获取原图档地址
	public static final String ORIGINAL_PIC = HOST + "originalpic";

	// 将我的好友名片分享给其他好友
	public static final String SHARE_CARD = HOST + "sharecard";

	// 获取头像的原始大图
	public static final String ORIGINAL_HEAD_PIC = HOST + "originalHeadPic";

	// 设置用户的隐私设置
	public static final String SET_USER_PRIVACY = HOST + "userprivacy";

	// 设置某名片的可能更新的数据
	public static final String GET_SYNC = HOST + "getsync";

	// 同意或拒绝更新这种名片
	public static final String APPLYSYNC = HOST + "applysync";

	// 分段获取用户所有的名片数据,注意分段获取的时候必须保证同一次分段数据的timestamp必须一致
	public static final String QUERY_SECTION = HOST + "querysection";

	// 消息列表中点击消息需要获取消息发送方在我的名片列表中的cardId
	public static final String GET_CARDID_IN_MY_LIST = HOST + "myListCardId";

	/*** 消息中心 ***/
	public static final String SWAP_CARD = HOST + "swapcard";

	public static final String SWAP_BATCH = HOST + "swapBatch";

	public static final String LETTER_LIST = HOST + "letter/letterList";

	public static final String MESSAGE_MARK_AS_READ_SINGLE = HOST
			+ "letter/readFlag";

	public static final String MESSAGE_MARK_AS_READ_ALL = HOST
			+ "letter/readFlagBatch";

	public static final String CHAT_AUDIO_UPLOAD = "http://42.96.186.145/sixin/audio";
//			SysConstants.SERVER_UPLOAD
//			+ (SysConstants.SERVER_UPLOAD.equals(SysConstants.SERVER) ? "upload/sixin/audio"
//					: "sixin/audio");

	public static final String CHAT_IMAGE_UPLOAD = "http://42.96.186.145/sixin/image";
	//public static final String CHAT_IMAGE_UPLOAD = SysConstants.SERVER_UPLOAD
		//	+ (SysConstants.SERVER_UPLOAD.equals(SysConstants.SERVER) ? "upload/sixin/image"
			//		: "sixin/image");

	public static final String SHARE_CARD_MESSAGE = HOST + "letter/sharecard";

	public static final String CHAT_SEND_MESSAGE = HOST + "letter/send";

	public static final String CHAT_DELETE_MESSAGE = HOST + "letter/delete";

	public static final String CHAT_DELETE_MESSAGE_BATCH = HOST
			+ "letter/batchDelete";

	public static final String CHAT_OBTAIN_UPDATE_MESSAGE = HOST
			+ "letter/greetList";

	// 获取未读消息数量
	public static final String MSGC = HOST + "msgc";

	// 获取未读消息列表
	public static final String MESSAGE = HOST + "message";

	// 获取未读消息列表
	public static final String MSG_COUNT = HOST + "msgcount";

	// 获取我发出的消息列表
	public static final String MSML = HOST + "msml";
	// 批量处理请求交换名片的响应
	public static final String SWAP_CARDR_ESP_BATCH = HOST
			+ "swap_card_resp_batch";

	// 获取未读消息列表
	public static final String MSG_LIST = HOST + "msglist";

	// 清空本地消息时需告诉服务器删除消息
	public static final String DEL_MSG = HOST + "delmsg";

	// 消息列表中点击消息需要获取消息发送方在我的名片列表中的cardId
	public static final String REF_MYLIST_CARDID = HOST + "refMyListCardId";

	public static final String SEARCH_BY_MOBILE = HOST + "getContact";

	/** 联系人上传 ***/
	// 上传不匹配
	public static final String UPLOAD_CONTACTS = HOST + "contacts-upload";
	// 上传并匹配
	public static final String MATCH_CONTACTS = HOST + "contacts-match";

	public static final String CONTACT_INVITE = HOST + "contactsinvite";

	public static final String CARD_INVITE = HOST + "invitesms";

	// 群发消息接口
	public static final String GROUP_MESSAGE = HOST + "greet/groupmsg";

	public static final String GREET_REPLY_SEND = HOST + "greet/reply";

	// 解除微博绑定
	public static final String UNBIND_WEIBO = HOST + "account/wb-unbind";

	// 获取当前账户绑定的第三方信息
	public static final String THIRD_INFO_WEIBO = HOST + "account/third-info";

	/*****郭智军****/
	//获取短信验证码
	public static final String GET_SECURITY_CODE = "http://42.96.186.145:8088/isadmin/api/user/securityCode";
	//用户注册
	public static final String USER_REGISTER = "http://42.96.186.145:8088/isadmin/api/user/register";
	//激活账户
	public static final String ACTIVATE_ACCOUNT = "http://42.96.186.145:8088/isadmin/api/user/activate";
	//设置密码
	public static final String SET_PASSWD = "http://42.96.186.145:8088/isadmin/api/user/setPassword";
	//用户登陆
	public static final String USER_LOGIN = "http://42.96.186.145:8088/isadmin/api/user/login";
	//获取用户所有的邀请
	public static final String GET_INVITATIONS= "http://42.96.186.145:8088/isadmin/api/user/invitations";
	//加入公司
	public static final String JOIN_COMPANY = "http://42.96.186.145:8088/isadmin/api/user/joinCompany";
	//获取通讯录好友列表
	public static final String GET_FRIENDS = "http://42.96.186.145:8088/queryfriends";
	//通过http接口发私信
	public static final String SEND_MESSAGE_HTTP = "http://42.96.186.145:8088/letter/send";
	//根据接收者获得用户的离线信息
	public static final String OBTAIN_MESSAGE_HTTP = "http://42.96.186.145:8088/letter/letterList";
	//根据发送者ID和持有的seq批量进行已读标记
	public static final String READFLAG_BATCH = "http://42.96.186.145:8088/letter/readFlagBatch";
	//私信删除(单个)
	public static final String DELETE_CHAT_MESSAGE = "http://42.96.186.145:8088/letter/delete";
}
