package com.example.galaxy08.http;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;

import com.example.galaxy08.SysApplication;
import com.example.galaxy08.app.PreferenceWrapper;
import com.example.galaxy08.http.exception.GalaxyHttpException;
import com.example.galaxy08.http.model.BaseResponse;
import com.example.galaxy08.http.model.GalaxyResponse;
import com.example.galaxy08.tool.DebugLog;
import com.example.galaxy08.tool.SystemInfo;
import com.example.galaxy08.tool.Tool;
import com.loopj.android.http.RequestParams;

/**
 * http接口 每个接口对应2个同名重载方法，Bundle参数方法供http service调用， 字符串参数方法供常规方法调用。
 * 
 */
public class GalaxyApi extends BaseGalaxyApi {

    public static final String TAG = "GalaxyApi";

    private static final int MAX_RETRY_COUNT = 3;

    /**
     * 激活 用于运营分析
     * 
     * @return
     * @throws IOException
     * @throws GalaxyHttpException
     */
//    public static GalaxyResponse activeClient() throws IOException, GalaxyHttpException {
//        return doPost(UrlConstants.ACTIVE_CLIENT, new ArrayList<NameValuePair>(), GalaxyResponse.class);
//    }

    /**
     * 获取logo页图片url
     * 
     * @param type
     * @return
     * @throws IOException
     * @throws GalaxyHttpException
     */
    public static GalaxyResponse getLogoImg(String type) throws IOException, GalaxyHttpException {
        List<NameValuePair> list = new LinkedList<NameValuePair>();
        list.add(new BasicNameValuePair(RequestParames.TYPE, type));
        return doPost(UrlConstants.LOAD_PIC, list, GalaxyResponse.class);
    }

    /************************************************************/
    /** * 注册登陆模块 */
    /************************************************************/

    /**
     * 登陆
     * 
     * @param params
     * @return
     * @throws IOException
     * @throws GalaxyHttpException
     */
    public static GalaxyResponse login(Bundle params) throws IOException, GalaxyHttpException {
        String mobile = params.getString(RequestParames.MOBILE);
        String password = params.getString(RequestParames.PASSWORD);
        return login(mobile, password);
    }
    // 登陆2
    public static GalaxyResponse login(String mobile, String password) throws IOException, GalaxyHttpException {
        String secret = MD5Util.md5(mobile + MD5Util.md5(password) + Tool.getKey());
        List<NameValuePair> list = new LinkedList<NameValuePair>();
        list.add(new BasicNameValuePair(RequestParames.USER_NAME, mobile));
        list.add(new BasicNameValuePair(RequestParames.SECRET, secret));
        return doPost(UrlConstants.REQUEST_NEW_LOGIN, list, GalaxyResponse.class);
    }

    // 登出1
    public static GalaxyResponse logout(String userId) throws IOException, GalaxyHttpException {
        List<NameValuePair> list = new LinkedList<NameValuePair>();
        list.add(new BasicNameValuePair(RequestParames.USER_ID, userId));
        return doPost(UrlConstants.LOGOUT, list, GalaxyResponse.class);
    }

    // 登出2
    public static GalaxyResponse logout(Bundle params) throws IOException, GalaxyHttpException {
        String userid = params.getString(RequestParames.USER_ID);
        return logout(userid);
    }

    public static GalaxyResponse backToFront(String userId, boolean auto) throws IOException, GalaxyHttpException {
        List<NameValuePair> list = new LinkedList<NameValuePair>();
        list.add(new BasicNameValuePair(RequestParames.USER_ID, userId));
        list.add(new BasicNameValuePair(RequestParames.AUTO, String.valueOf(auto)));
        return doPost(UrlConstants.BACK_TO_FRONT, list, GalaxyResponse.class);
    }

    /**
     * 修改登陆名
     * 
     * @throws GalaxyHttpException
     * @throws IOException
     */
    public static GalaxyResponse bindUsername(String userid, String oldusername, String newusername)
            throws IOException, GalaxyHttpException {
        List<NameValuePair> list = new ArrayList<NameValuePair>(3);
        list.add(new BasicNameValuePair(RequestParames.USER_ID, userid));
        list.add(new BasicNameValuePair(RequestParames.OLD_USERNAME, oldusername));
        list.add(new BasicNameValuePair(RequestParames.NEW_USERNAME, newusername));
        return doPost(UrlConstants.BIND_USERNAME, list, GalaxyResponse.class);
    }


    /**
     * 改登陆名
     * 
     * @throws GalaxyHttpException
     * @throws IOException
     */
    public static GalaxyResponse bindUsername(Bundle params) throws IOException, GalaxyHttpException {
        String userId = params.getString(RequestParames.USER_ID);
        String oldusername = params.getString(RequestParames.OLD_USERNAME);
        String newusername = params.getString(RequestParames.NEW_USERNAME);

        List<NameValuePair> list = new ArrayList<NameValuePair>(3);
        list.add(new BasicNameValuePair(RequestParames.USER_ID, userId));
        list.add(new BasicNameValuePair(RequestParames.OLD_USERNAME, oldusername));
        list.add(new BasicNameValuePair(RequestParames.NEW_USERNAME, newusername));
        return bindUsername(userId, oldusername, newusername);
    }

    /**
     * 确认验证码
     * 
     * @throws GalaxyHttpException
     * @throws IOException
     */
    public static GalaxyResponse confirmBindUserName(Bundle params) throws IOException, GalaxyHttpException {
        String userId = params.getString(RequestParames.USER_ID);
        String newusername = params.getString(RequestParames.NEW_USERNAME);
        String verfyCode = params.getString(RequestParames.VERIFY_CODE);

        List<NameValuePair> list = new ArrayList<NameValuePair>(3);
        list.add(new BasicNameValuePair(RequestParames.USER_ID, userId));
        list.add(new BasicNameValuePair(RequestParames.NEW_USERNAME, newusername));
        list.add(new BasicNameValuePair(RequestParames.VERIFY_CODE, verfyCode));
        return doPost(UrlConstants.CONFIRM_BIND_USERNAME, list, GalaxyResponse.class);
    }

    // 查询验证记录当前状态，是否已经识别或无法识别；如果识别则返回对应的名片记录。
    public static GalaxyResponse queryCard(String userId, String[] verifyId) throws IOException, GalaxyHttpException {
        List<NameValuePair> list = new LinkedList<NameValuePair>();
        list.add(new BasicNameValuePair(RequestParames.USER_ID, userId));
        list.add(new BasicNameValuePair(RequestParames.VERIFY_ID, Arrays.toString(verifyId)));
        return doPost(UrlConstants.QUERY_CARD, list, GalaxyResponse.class);
    }


    /**
     * 获取离线消息
     * 
     * @param userid
     *            用户id
     * @param sequence
     *            最新一条message的序列
     * @param count
     *            获取条数
     * @return
     * @throws IOException
     * @throws GalaxyHttpException
     */
    public static GalaxyResponse obtainOfflineMessage(String userid, int sequence, int count) throws IOException,
            GalaxyHttpException {
        List<NameValuePair> list = new ArrayList<NameValuePair>(3);
        list.add(new BasicNameValuePair(RequestParames.USER_ID, userid));
        list.add(new BasicNameValuePair(RequestParames.SEQUENCE, sequence + ""));
        list.add(new BasicNameValuePair(RequestParames.COUNT, count + ""));
        return doPost(UrlConstants.LETTER_LIST, list, GalaxyResponse.class);
    }

    /**
     * 上传文件接口
     * 
     * @param userid
     *            用户id
     * @param file
     *            上传文件
     * @return
     * @throws IOException
     * @throws GalaxyHttpException
     */
    public static GalaxyResponse uploadFile(String userid, File file) throws IOException, GalaxyHttpException {
        List<NameValuePair> list = new ArrayList<NameValuePair>(3);
        list.add(new BasicNameValuePair(RequestParames.USER_ID, userid));
        return doPost(UrlConstants.CHAT_AUDIO_UPLOAD, list, GalaxyResponse.class, file);
    }

    /**
     * 上传文件，并直接返回服务器路径
     * 
     * @param userid
     *            用户id
     * @param file
     *            上传文件
     * @return
     * @throws IOException
     * @throws GalaxyHttpException
     */
    public static String uploadLocalFile(String userid, File file) throws IOException, GalaxyHttpException {
        DebugLog.logd("XMPP", "uploadLocalFile");
        GalaxyResponse response = uploadFile(userid, file);
        if (!"0".equals(response.getStatus()))
            throw new GalaxyHttpException("response error");
        return response.getUrl();
    }


    public static GalaxyResponse markAsReadSingle(String userid, int sequence) throws IOException, GalaxyHttpException {
        List<NameValuePair> list = new ArrayList<NameValuePair>(3);
        list.add(new BasicNameValuePair(RequestParames.USER_ID, userid));
        list.add(new BasicNameValuePair(RequestParames.SEQUENCE, sequence + ""));
        return doPost(UrlConstants.MESSAGE_MARK_AS_READ_SINGLE, list, GalaxyResponse.class);
    }

    public static GalaxyResponse markAsReadAll(String userid, int sequence) throws IOException, GalaxyHttpException {
        List<NameValuePair> list = new ArrayList<NameValuePair>(3);
        list.add(new BasicNameValuePair(RequestParames.USER_ID, userid));
        // list.add(new BasicNameValuePair(RequestParames.SENDER, targetid));
        list.add(new BasicNameValuePair(RequestParames.SEQUENCE, String.valueOf(sequence)));
        return doPost(UrlConstants.MESSAGE_MARK_AS_READ_ALL, list, GalaxyResponse.class);
    }

    public static String uploadChatImage(String userid, File image) throws IOException, GalaxyHttpException {
        List<NameValuePair> list = new ArrayList<NameValuePair>(3);
        list.add(new BasicNameValuePair(RequestParames.USER_ID, userid));
        GalaxyResponse response = uploadFile(UrlConstants.CHAT_IMAGE_UPLOAD, list, image);
        if (response == null || !"0".equals(response.getStatus()))
            throw new GalaxyHttpException("response error:" + response.getMessage());
        return response.getUrl();
    }

    public static GalaxyResponse uploadChatAudio(String userid, File audio) throws IOException, GalaxyHttpException {
        List<NameValuePair> list = new ArrayList<NameValuePair>(3);
        list.add(new BasicNameValuePair(RequestParames.USER_ID, userid));
        GalaxyResponse response = uploadFile(UrlConstants.CHAT_AUDIO_UPLOAD, list, audio);
        return response;
    }

    private static GalaxyResponse uploadFile(String url, List<NameValuePair> list, File file) throws IOException,
            GalaxyHttpException {
        for (int retry = 0; retry < MAX_RETRY_COUNT; retry++) {
            try {
                return doPost(url + "?ts=" + System.currentTimeMillis(), list, GalaxyResponse.class, file);
            } catch (Exception e) {
                DebugLog.logd(TAG, "upload file error", e);
                DebugLog.logd(TAG, "retry count:" + retry);
            }
        }
        return null;
    }

    public static GalaxyResponse shareCardMessage(String userid, String cardid, String targetIds) throws IOException,
            GalaxyHttpException {
        List<NameValuePair> list = new ArrayList<NameValuePair>(4);
        list.add(new BasicNameValuePair(RequestParames.USER_ID, userid));
        list.add(new BasicNameValuePair(RequestParames.SCARD_ID, cardid));
        list.add(new BasicNameValuePair(RequestParames.TARGET_IDS, targetIds));
        list.add(new BasicNameValuePair("shareFrom", "frletter"));

        for (int retry = 0; retry < MAX_RETRY_COUNT; retry++) {
            try {
                return doPost(UrlConstants.SHARE_CARD_MESSAGE, list, GalaxyResponse.class);
            } catch (Exception e) {
                DebugLog.logd(TAG, "shareCardMessage", e);
                DebugLog.logd(TAG, "retry count:" + retry);
            }
        }
        return null;
    }

    public static boolean sendXmppMessage(String userid, String to, String type, String body, String duration) {
        List<NameValuePair> list = new ArrayList<NameValuePair>(4);
        list.add(new BasicNameValuePair(RequestParames.USER_ID, userid));
        list.add(new BasicNameValuePair("to", to));
        list.add(new BasicNameValuePair("type", type));
        list.add(new BasicNameValuePair("body", body));
        list.add(new BasicNameValuePair("duration", duration));

        for (int retry = 0; retry < MAX_RETRY_COUNT; retry++) {
            try {
                GalaxyResponse response = doPost(UrlConstants.CHAT_SEND_MESSAGE, list, GalaxyResponse.class);
                return response != null && "0".equals(response.getStatus());
            } catch (Exception e) {
                DebugLog.logd(TAG, "sendXmppMessage error", e);
                DebugLog.logd(TAG, "retry count:" + retry);
            }
        }
        return false;
    }

    public static GalaxyResponse obtainGroup(String userid) throws IOException, GalaxyHttpException {
        List<NameValuePair> list = new ArrayList<NameValuePair>(1);
        list.add(new BasicNameValuePair(RequestParames.USER_ID, userid));
        return doPost(UrlConstants.GET_GROUPS, list, GalaxyResponse.class);
    }

    public static GalaxyResponse sendGreetReply(String userid, String templateid, String text, String voicePath, int audioTime, String targetid)
            throws IOException, GalaxyHttpException {
        List<NameValuePair> list = new ArrayList<NameValuePair>(4);
        list.add(new BasicNameValuePair(RequestParames.USER_ID, userid));
        list.add(new BasicNameValuePair(RequestParames.TEMPLATE, templateid));
        list.add(new BasicNameValuePair(RequestParames.TEXT, text));
        list.add(new BasicNameValuePair(RequestParames.AUDIO_TIME, String.valueOf(audioTime)));
        list.add(new BasicNameValuePair(RequestParames.TARGET_ID, targetid));
        
        File file = null;
        if(voicePath != null){
        	file = new File(voicePath);
        }
		if (file == null || !file.exists()) {
			return doPost(UrlConstants.GREET_REPLY_SEND, list, GalaxyResponse.class);
		} else {
			return doPost(UrlConstants.GREET_REPLY_SEND, list, GalaxyResponse.class, file);
		}
    }

    public static GalaxyResponse deleteChatMessage(String userid, int sequence) throws IOException, GalaxyHttpException {
        List<NameValuePair> list = new ArrayList<NameValuePair>(3);
        list.add(new BasicNameValuePair(RequestParames.USER_ID, userid));
        list.add(new BasicNameValuePair(RequestParames.SEQUENCE, sequence + ""));
        return doPost(UrlConstants.CHAT_DELETE_MESSAGE, list, GalaxyResponse.class);
    }

    public static GalaxyResponse deleteChatMessageBatch(String userid, String targetid, int sequence)
            throws IOException, GalaxyHttpException {
        List<NameValuePair> list = new ArrayList<NameValuePair>(3);
        list.add(new BasicNameValuePair(RequestParames.USER_ID, userid));
        list.add(new BasicNameValuePair(RequestParames.SENDER, targetid));
        list.add(new BasicNameValuePair(RequestParames.SEQUENCE, String.valueOf(sequence)));
        return doPost(UrlConstants.CHAT_DELETE_MESSAGE_BATCH, list, GalaxyResponse.class);
    }

    /**
     * 获取需要升级的消息
     * 
     * @param userid
     *            用户id
     * @param sequence
     *            最小一条message的序列
     * @param count
     *            获取条数
     * @return
     * @throws IOException
     * @throws GalaxyHttpException
     */
    public static GalaxyResponse obtainUpdatedMessage(String userid, int sequence) throws IOException, GalaxyHttpException {
        List<NameValuePair> list = new ArrayList<NameValuePair>(3);
        list.add(new BasicNameValuePair(RequestParames.USER_ID, userid));
        list.add(new BasicNameValuePair(RequestParames.SEQUENCE, sequence + ""));
        return doPost(UrlConstants.CHAT_OBTAIN_UPDATE_MESSAGE, list, GalaxyResponse.class);
    }

	public static BaseResponse unbindWeibo(String userID) throws IOException, GalaxyHttpException {
		List<NameValuePair> list = new ArrayList<NameValuePair>(1);
		list.add(new BasicNameValuePair(RequestParames.USER_ID, userID));
		return doPost(UrlConstants.UNBIND_WEIBO, list, GalaxyResponse.class);
	}

	public static BaseResponse getThirdInfo(String userid) throws IOException, GalaxyHttpException {
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair(RequestParames.USER_ID, userid));
		return doPost(UrlConstants.THIRD_INFO_WEIBO, list, GalaxyResponse.class);
	}
	
	//获取验证码
	public static GalaxyResponse getFindPassword(String userName)throws IOException, GalaxyHttpException {
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair(RequestParames.USER_NAME, userName));
		return doPost(UrlConstants.FIND_PASSWORD_NEW, list, GalaxyResponse.class);
	}
	//发送验证码
	public static BaseResponse sendVerifyCode(String userName,
			String verifyCode) throws IOException, GalaxyHttpException {
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair(RequestParames.USER_NAME, userName));
		list.add(new BasicNameValuePair(RequestParames.VERIFY_CODE, verifyCode));
		return doPost(UrlConstants.REQUEST_VERIFY, list, GalaxyResponse.class);
	}

	// 第3方登陆接口
	public static BaseResponse setPassword4third(String userId,
			String password, String passwordConfirm, String token) throws IOException, GalaxyHttpException {
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair(RequestParames.USER_ID, userId));
		list.add(new BasicNameValuePair(RequestParames.PASSWORD, password));
		list.add(new BasicNameValuePair(RequestParames.REPEAT_PASSWORD, passwordConfirm));
		list.add(new BasicNameValuePair(RequestParames.TOKEN, token));
		return doPost(UrlConstants.REQUEST_PASSWORD_FORTHIRD, list, GalaxyResponse.class);
	}

	// 修改密码
	public static BaseResponse changePassword(String userId, String password,
			String passwordConfirm, String token, String oldPassword) throws IOException, GalaxyHttpException {
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair(RequestParames.USER_ID, userId));
		list.add(new BasicNameValuePair(RequestParames.PASSWORD, password));
		list.add(new BasicNameValuePair(RequestParames.REPEAT_PASSWORD, passwordConfirm));
		list.add(new BasicNameValuePair(RequestParames.TOKEN, token));
		list.add(new BasicNameValuePair(RequestParames.OLD_PASSWORD, oldPassword));
		return doPost(UrlConstants.REQUEST_PASSWORD_RESET, list, GalaxyResponse.class);
	}

	
	public static  BaseResponse getAudioFile(String path) throws GalaxyHttpException, IOException {
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		//list.add(new BasicNameValuePair(RequestParames.TOKEN, PreferenceWrapper.get(RequestParames.TOKEN, "")));
        //list.addAll(createMobileInfo());
		return doPost(path, list, GalaxyResponse.class);
	}
	
	/**
	 * 用户注册：获取验证码
	 * 功能：给参数里的用户手机号码发送包含验证码的短信。
	 * 参数：
	 * 1 mobile ： 用户手机号码
	 * 2 secret : 登录名加密，可预防非法注册来源。MD5(username+key)
	 * @throws GalaxyHttpException 
	 * @throws IOException
	 * 郭智军 2013/05/28 
	 */
	public static void getSecurityCode(String mobile,String secret,IJsonHandler handler) {
		
		RequestParams params = new RequestParams();
    	params.put("mobile",mobile);
    	params.put("secret",secret);
		JwHttpClient.post(UrlConstants.GET_SECURITY_CODE, params, handler);
	}
	/**
	 * 用户使用手机号和密码登录
	 * 参数：
	 * 1 comAccount : 公司账号
	 * 2 mobile : 手机号
	 * 3 password : 密码的MD5值
	 * 郭智军
	 * 2013/06/13
	 */
	
	public static void userLogin(String comAccount,String mobile,String password,IJsonHandler handler){
    	RequestParams params = new RequestParams();
    	params.put("comAccount",comAccount);
    	params.put("userAccount",mobile);
    	params.put("password",password);
		JwHttpClient.post(UrlConstants.USER_LOGIN, params, handler);
	}

	/**
	 * 设置密码：为已经激活的账号设置密码
	 * 参数：
	 * 1 comAccount : 公司账号
	 * 2 mobile : 手机号
	 * 3 token : 用户token
	 * 4 password : 密码
	 * 附加：
	 * (1) model : 手机型号
	 * (2) uniqid : 设备的唯一标识，如果无则传“-1”
	 * (3) os : 手机操作系统型号及版本
	 * (4) screen : 屏幕尺寸
	 * (5) mac : MAC地址
	 * (6) from : 渠道编号
	 * (7) token : token
	 * 郭智军
	 * 2013/06/13
	 */
	public static void setPasswd(String comAccount,String mobile,String token,String password,IJsonHandler handler) {
		RequestParams params = new RequestParams();
    	params.put("comAccount",comAccount);
    	params.put("mobile",mobile);
    	params.put("token",token);
    	params.put("password",password);
    	SystemInfo info = SysApplication.getSystemInfo();
    	params.put("model",info.getModel());
    	params.put("uniqid",info.getUniqid());
    	params.put("os",info.getOs());
    	params.put("screen",info.getScreen());
    	params.put("mac",info.getMac());
    	params.put("from",String.valueOf(info.getFrom()));
    	JwHttpClient.post(UrlConstants.SET_PASSWD, params, handler);
	}
	
	/**
	 * 根据用户账户、token，获取所有邀请过该用户的公司信息。
	 * 参数：
	 * 1 mobile : 手机号
	 * 2 token : token
	 * 郭智军
	 * 2013/05/31
	 * @throws Exception 
	 */
	
	public static void getInvitations(String mobile,String token,IJsonHandler handler){
		RequestParams params = new RequestParams();
    	params.put("mobile",mobile);
    	params.put("token",token);
    	JwHttpClient.post(UrlConstants.GET_INVITATIONS, params, handler);
	}
	
	
	/**
	 * 加入公司
	 * 参数：
	 * 1 mobile : 手机号
	 * 2 token : token
	 * 3 cid : 公司id
	 * 郭智军
	 * 2013/05/31
	 */
	
	public static void joinCompany(String mobile,String cid,IJsonHandler handler){
		RequestParams params = new RequestParams();
    	params.put("mobile",mobile);
    	params.put("cid",cid);
    	params.put("token", PreferenceWrapper.get(PreferenceWrapper.TOKEN, "0"));
    	JwHttpClient.post(UrlConstants.JOIN_COMPANY, params, handler);
	}
	
	/**
	 * 获取通讯录好友列表
	 * 1 token : 权限控制
	 * 2 userId : 用户id
	 * 3 lastupdatetime : 上次更新时间,初始为0
	 * 郭智军
	 * 2013/06/04
	 */
	public static void getFriends(IJsonHandler handler){
		RequestParams params = new RequestParams();
    	params.put("token",PreferenceWrapper.get(PreferenceWrapper.TOKEN, "0"));
    	params.put("userId",PreferenceWrapper.get(PreferenceWrapper.USER_ID,"0"));
    	params.put("lastupdatetime",PreferenceWrapper.get(PreferenceWrapper.LAST_UPDATE_TIME,"0"));
    	JwHttpClient.post(UrlConstants.GET_FRIENDS, params, handler);
	}
	
	/**
	 * 通过http接口发私信
	 * 1 to : 接收人ID
	 * 2 type : 例如text、radio之类的
	 * 3 body : 消息体
	 * 4 _type : 群组就传groupChat
	 * 附加？
	 * 郭智军
	 * 2013/06/04
	 */
	public static void sendMessageHttp(String to,String type,String body,
			String _type,IJsonHandler handler){
		RequestParams params = new RequestParams();
    	params.put("token",PreferenceWrapper.get(PreferenceWrapper.TOKEN, "0"));
    	params.put("userId",PreferenceWrapper.get(PreferenceWrapper.USER_ID,"0"));
    	params.put("to",to);
    	params.put("type",type);
    	params.put("body",body);
    	params.put("_type",_type);
    	JwHttpClient.post(UrlConstants.GET_FRIENDS, params, handler);
	}
	
	/**
	 * 根据接收者获得用户的离线信息
	 * 1 userId : 用户id
	 * 2 seq : 消息标识
	 * 3 ts : 取群消息用的
	 * 郭智军
	 * 2013/06/04	
	 */
	public static void obtainOfflineMessage(String ts,IJsonHandler handler){
		RequestParams params = new RequestParams();
    	params.put("token",PreferenceWrapper.get(PreferenceWrapper.TOKEN, "0"));
    	params.put("userId",PreferenceWrapper.get(PreferenceWrapper.USER_ID,"0"));
    	params.put("seq",PreferenceWrapper.get(PreferenceWrapper.CHAT_MESSAGE_SEQUENCE, "0"));
    	params.put("ts",ts);
    	JwHttpClient.post(UrlConstants.GET_FRIENDS, params, handler);
	}
	
	/**
	 * 根据发送者ID和持有的seq批量进行已读标记
	 * 参数：
	 * 1 userId : 用户id
	 * 2 seq : 持有的消息标识
	 * 郭智军
	 * 2013/06/04
	 */
	public static void markReadFlagBatch(int seq,IJsonHandler handler){
		RequestParams params = new RequestParams();
    	params.put("token",PreferenceWrapper.get(PreferenceWrapper.TOKEN, "0"));
    	params.put("userId",PreferenceWrapper.get(PreferenceWrapper.USER_ID,"0"));
    	params.put("seq",String.valueOf(seq));
    	JwHttpClient.post(UrlConstants.READFLAG_BATCH, params, handler);
	}
	/**
	 * 私信删除(单个)
	 * 参数：
	 * 1 seq : 要删除消息的seq
	 * 2 Receiver : 接收人id
	 * 郭智军
	 * 2013/06/04
	 */
	public static void deleteChatMessage(int seq,String Receiver,IJsonHandler handler){
		RequestParams params = new RequestParams();
    	params.put("token",PreferenceWrapper.get(PreferenceWrapper.TOKEN, "0"));
    	params.put("userId",PreferenceWrapper.get(PreferenceWrapper.USER_ID,"0"));
    	params.put("seq",String.valueOf(seq));
    	JwHttpClient.post(UrlConstants.DELETE_CHAT_MESSAGE, params, handler);
	}
	/**
	 * 使用公司账号、手机号、验证码激活用户账号
	 * 参数：
	 * 1 comAccount : 公司账号
	 * 2 mobile : 手机号
	 * 3 securityCode : 短信验证码
	 * 郭智军
	 * 06/13
	 */
	public static void activateAccount(String comAccount,
			String mobile,String securityCode,IJsonHandler handler){
		RequestParams params = new RequestParams();
    	params.put("comAccount",comAccount);
    	params.put("mobile",mobile);
    	params.put("securityCode",securityCode);
    	JwHttpClient.post(UrlConstants.ACTIVATE_ACCOUNT, params, handler);
	}
	/**
	 * 
	 */
}
