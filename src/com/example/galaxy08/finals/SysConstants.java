package com.example.galaxy08.finals;

/*
 * 系统服务器上的具体设置(需要实时进行修改) 2013/04/15 
 */
public class SysConstants {
	// 服务器更改为 gc.changxiang.com
	public static final String SERVER = "http://42.96.186.145/";
	public static final String SERVER_HTTPS = "https://42.96.186.145/";
	public static final String SERVER_SIXIN = "42.96.186.145";
	public static final String SERVER_UPLOAD = "http://42.96.186.145/";

	// 履历控制
	public static final boolean hasResume = false;
	// 设置语言
	public static boolean isEnglishVersion = false;

	// 是否显示检查更新条目
	public static final boolean showUpdatePreference = true;

	public static final String CONNECT_METHOD_GET = "GET";

	public static final String CONNECT_METHOD_POST = "POST";

	public static final String FORMAT_JSON = "JSON";

	public static final String FORMAT_XML = "XML";

	public static final String ITEM_SEPARATOR = "@@@";

	public static String GROUP_SEPARATOR = ",";

	public static final String MOBILE_SEPARATOR = ITEM_SEPARATOR;

	public static final String APP_ID = "1";

	// public static final String FROM_ID = "100000";

	public static int ZIP_BUFFER_SIZE = 1024;

	public static int MAX_MOBILE_COUNT = 2;

	public static int MAX_DEP_COUNT = 2;

	public static int MAX_TITLE_COUNT = 4;

	public static int MAX_WORK_NUMBER_COUNT = 3;

	public static int MAX_EMAIL_COUNT = 2;

	public static int MAX_COMPANY_COUNT = 2;

	public static int MAX_ADDRESS_COUNT = 2;

	public static int MAX_LABLE_LEN = 21;

	public static int ALARM_REMINDER_DAY = 7;

	public static int ALARM_REMINDER_HOUR_IN_DAY = 15;

	public static final int HTTP_CONNECT_TIMEOUT = 20000;
	public static final int HTTP_UPLOAD_READ_TIMEOUT = 20 * 1000;
	public static final int HTTP_READ_TIMEOUT = 30 * 1000;
	public static final int CLICK_REQUEST_TIMEOUT = 15000;
	public static final int CAMERA_COMPRESS_RATIO = 80;
	public static final int CAMERA_2G_COMPRESS_RATIO = 60;

	// login state
	public static int LOGIN_STATE_MOBILE = 0;
	public static int LOGIN_STATE_EMAIL = 1;
	public static int LOGIN_STATE_SINA_MOBILE = 2;
	public static int LOGIN_STATE_SINA_EMAIL = 3;

	// 接口url
	public static final String REQUEST_GET_GROUP = SERVER + "getgroup";

	public static final String REQUEST_QUERY_NEW = SERVER + "querynew";

	public static final String REQUEST_RELATED_GROUP = SERVER + "relatedgroup";

	public static final String REQUEST_RELATED_MGROUP = SERVER + "relatedmg";

	public static final String REQUEST_ALL = SERVER + "all";

	public static final String REQUEST_UPLOAD_IMG = SERVER + "uploadImg";

	public static final String REQUEST_QUERY_UPDATE = SERVER + "queryupdate";

	public static final String REQUEST_ADD_GROUP = SERVER + "addgroup";

	public static final String REQUEST_DEL_GROUP = SERVER + "delgroup";

	public static final String REQUEST_MODIFY_GROUP = SERVER + "modifygroup";

	public static final String REQUEST_UPLOAD_FRONT_REVERSE = SERVER
			+ "uploadFrontReverse";

	public static final String REQUEST_ACTIVEINVITE = SERVER + "activeinvite";

	public static final String REQUEST_QUERY_MD = SERVER + "query/md";

	public static final String REQUEST_REF = SERVER + "ref";

	public static final String REQUEST_COLLECTMY = SERVER + "collectmy";

	public static final String REQUEST_COLLECTMY_COUNT = SERVER
			+ "collectmycount";

	public static final String REQUEST_GET_SIGNATURE = SERVER + "getSignature";

	public static final String REQUEST_HANDLE_UPDATEMSG = SERVER
			+ "msg/reqfletter";

	public static final String REQUEST_GET_BLESS = SERVER
			+ "greet/loadTemplateList";

	public static final String REQUEST_GET_GREET = SERVER
			+ "greet/loadTemplate";

	public static final String REQUEST_GET_RANK = SERVER + "rank/userrank";

	public static final String REQUEST_SETPRIVACY = SERVER + "setprivacy";

	public static final String REQUEST_SUGGEST = SERVER + "app/suggest";

	public static final String REQUEST_ORIGINALPIC = SERVER + "originalpic";

	public static final String REQUEST_ORIGINAL_HEAD_PIC = SERVER
			+ "originalHeadPic";

	public static final String REQUEST_ORIGINAL_HEAD_PIC_NEW_INTERFACE = "img/userOriginalHeadPic";

	public static final String REQUEST_USER_PRIVACY = SERVER + "userprivacy";

	public static final String REQUEST_GET_SYNC = SERVER + "getsync";

	public static final String REQUEST_APPLY_SYNC = SERVER + "applysync";

	public static final String REQUEST_QUERY_SECTION = SERVER + "querysection";

	public static final String REQUEST_MERGE_ACCOUNT = SERVER + "mergeaccount";

	/******************** 注册登录 ************************/
	public static final String REQUEST_LOGIN = SERVER_HTTPS + "login";

	public static final String REQUEST_NEW_LOGIN = SERVER_HTTPS + "newlogin";

	public static final String REQUEST_WB_LOGIN = SERVER_HTTPS + "wb-login";

	public static final String REQUEST_WB_SEND = SERVER_HTTPS
			+ "register/wb-send";

	// add facebook and linkedin interface
	public static final String REQUEST_FB_LOGIN = SERVER_HTTPS + "fb-login";

	public static final String REQUEST_LK_LOGIN = SERVER_HTTPS + "lk-login";

	public static final String REQUEST_VALIDATESINAWEIBO = SERVER_HTTPS
			+ "validatesinaweibo";

	public static final String REQUEST_TOREGISTER = SERVER_HTTPS + "toregister";

	public static final String REQUEST_REGISTER = SERVER_HTTPS
			+ "register/register";

	public static final String REQUEST_FB_REGISTER = SERVER_HTTPS
			+ "register/fb-register";

	public static final String REQUEST_LK_REGISTER = SERVER_HTTPS
			+ "register/lk-register";

	public static final String REQUEST_WB_REGISTER = SERVER_HTTPS
			+ "register/wb-register";

	public static final String REQUEST_WB_CONFIRM = SERVER_HTTPS
			+ "register/wb-confirm";

	public static final String REQUEST_VERIFY = SERVER_HTTPS
			+ "register/verifycode";

	public static final String REQUEST_CONFIRM = SERVER_HTTPS
			+ "register/confirm";

	public static final String REQUEST_TRIAL = SERVER_HTTPS + "register/trial";

	public static final String REQUEST_FIND_PASSWORD = SERVER_HTTPS
			+ "findpassword";

	public static final String REQUEST_NEWFIND_PASSWORD = SERVER_HTTPS
			+ "newfindpassword";

	public static final String REQUEST_RESET_PASSWORD = SERVER_HTTPS
			+ "resetpassword";

	public static final String REQUEST_FINDPSD = SERVER_HTTPS + "findpsd";

	public static final String REQUEST_RESET_PSD = SERVER_HTTPS + "resetpsd";

	public static final String REQUEST_NEWRESET_PSD = SERVER_HTTPS
			+ "newresetpassword";

	public static final String REQUEST_REG = SERVER_HTTPS + "reg";

	public static final String REQUEST_CONFIRMREG = SERVER_HTTPS + "confirmreg";

	public static final String REQUEST_PASSWORD_RESET = SERVER_HTTPS
			+ "modifypassword"; // 修改密码

	public static final String REQUEST_PASSWORD_FORTHIRD = SERVER_HTTPS
			+ "setpasswordforthird"; // 第3方登陆接口

	public static final String REQUEST_TOBINDMOBILE = SERVER_HTTPS
			+ "tobindusername";

	public static final String REQUEST_CONFIRMBINDMOBILE = SERVER_HTTPS
			+ "confirmbindusername";

	public static final String REQUEST_VERIFY_PASSWORD = SERVER_HTTPS
			+ "validatepassword";

	public static final String REQUEST_DOWNLOAD_LOGOIMAGE = SERVER + "loadpic";

	public static final String REQUEST_UPLOA_UERBEHAVOR_STATICS = SERVER
			+ "statistics/userbehavior";

	// 消息中心
	public static final String REQUEST_MSGC = SERVER + "msgc";

	public static final String REQUEST_MESSAGE = SERVER + "message";

	public static final String REQUEST_REQUIRE = SERVER + "require";

	public static final String REQUEST_EXCHANGE = SERVER + "exchange";

	public static final String REQUEST_MSML = SERVER + "msml";

	public static final String IMAGE_URL = SERVER + "iamgeurl";

	public static final String REQUEST_MSG_COUNT_NEW = SERVER
			+ "message/new-count";

	public static final String REQUEST_MSG_LIST = SERVER + "msglist";

	public static final String REQUEST_MSG_LIST_NEW = SERVER + "message/list";

	public static final String REQUEST_DEL_MSG_NEW = SERVER + "message/delete";

	public static final String REQUEST_DEL_CONTACT = SERVER + "friend/del";

	// 版本更新
	public static final String REQUEST_VERSION = SERVER + "app/ver";
	public static final String REQUEST_VERSION_DESC = SERVER
			+ "app/versiondesc";
	// 激活统计
	public static final String REQUEST_ACTIVECLIENT = SERVER
			+ "statistics/activeClient";

	public static final String REQUEST_BACKTOFRONT = SERVER
			+ "statistics/backtofront";

	/**
	 * TouristWithoutNetID
	 */
	public static final String TOURIST_NO_INTERNET = "000000000";
	/**
	 * upLoadBehaviour
	 */
	public static final String UPLOAD_USERBEHAVIOUR = "uploaduserbehaviour";

	public static final String ACTION_HEARTBEAT = "client_heartbeat";

	public static Boolean isChating = false; // 是否处于私信界面
	public static String emotionzhengze = "f0[0-9]{2}|f10[0-7]"; // 正则表达式，用来判断消息内是否有表情

	public static final String IMAGE_OTHER_SIDE = "_back";
	public static final String IMAGE_FORMAT = ".jpg";

	public static final String TOURISTOR_USER_NAME = "17600000000";

	public static String FILEHEAD = "file://";

	public static String M_FROM = "messageedit";

	public static final int PASSWORD_FORTHIRD = 1000869; // 为第三方设置密码的参数传递所用标记
															// 表示是为第三方设置密码

	public static final int FROM_CHANGEMODIFY = 1000868; // 为第三方设置密码的参数传递所用标记
															// 表示是从修改手机号码点击进入的

	public static final String BROADCAST_ACTION_OVER_QUOTA = "com.galaxy.card.action.overquota";

	public static final String BROADCAST_ACTION_RE_SUCESS = "com.galaxy.card.action.reshootsucess";

	public static final String BROADCAST_ACTION_CLOASE_HOME_PAGE = "com.galaxy.card.close.homepage";

	public static final int SHOW_POPUPWINDOW = 1000;

	public static final int DISMISS_POPUPWINDOW = 1001;

	// /////////////////////////////////////////
	public static final String NEWEDIT = "newedit"; // xinjianmingpian

	public static final String LOCAL_OCR = "singleshoot";

	public static final String SINGLE_SHOOT = "sshoot";

	public static final String BATCH_SHOOT = "cshoot";

	public static final String SINGLE_SIDE = "1";

	public static final String DOUBLE_SIDE = "2";

	public static final int TOURISTOR_IMAGE_MAX = 4;

	public static final String VERIFY_SINA_KEY = "activity"; // 验证微博 intnet
																// 传值的key

	public static final String VERIFY_SINA_VALUE = "verifySina";
}
