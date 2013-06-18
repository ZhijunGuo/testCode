package com.example.galaxy08.http.service;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.example.galaxy08.entity.Resume;
import com.example.galaxy08.http.GalaxyApi;
import com.example.galaxy08.http.RequestParames;
import com.example.galaxy08.http.ResponseCode;
import com.example.galaxy08.http.exception.GalaxyHttpException;
import com.example.galaxy08.http.model.BaseResponse;

/**
 * WorkHttpService  执行http请求的intentservice
 * 2013/04/15
 * 
 */
public class WorkHttpService extends AbstractHttpService {

	public static final String ACTION_REQUEST_LOGIN = "com.jingwei.card.http.action.login";

	public static final String ACTION_REQUEST_BIND_USERNAME = "com.jingwei.card.http.action.bindusername";

	public static final String ACTION_REQUEST_CONFIRM_BIND_USERNAME = "com.jingwei.card.http.action.confirmbindusername";

	public static final String ACTION_REQUEST_VALIDATE_PASSWORD = "com.jingwei.card.http.action.validatepassword";

	public static final String ACTION_REQUEST_LOGOUT = "com.jingwei.card.http.action.logout";

	public static final String ACTION_REQUEST_GET_TEXT_PUSH = "com.jingwei.card.http.action.gettextpush";

	public static final String ACTION_REQUEST_EXPORT_CARDS = "com.jingwei.card.http.action.exportcards";

	public static final String ACTION_REQUEST_GET_GROUP = "com.jingwei.card.http.action.getgroup";

	public static final String ACTION_REQUEST_SEARCH_BY_MOBILE = "com.jingwei.card.http.action.searchbymobile";

	public static final String ACTION_REQUEST_COLLECT_MY_CARD = "com.jingwei.card.http.action.collectmycard";

	public static final String ACTION_REQUEST_ShARE_CARD = "com.jingwei.card.http.action.sharecard";

	public static final String ACTION_REQUEST_SWAPE_CARD = "com.jingwei.card.http.action.swapcard";

	public static final String ACTION_REQUEST_SWAPE_BATCH = "com.jingwei.card.http.action.swapbatch";

	public static final String ACTION_REQUEST_ALIKE_MYCARD = "com.jingwei.card.http.action.alikemycard";

	public static final String ACTION_REQUEST_OFFLINE_MESSAGE = "com.jingwei.card.action.obtainofflinemessage";

	public static final String ACTION_REQUEST_NEAR = "com.jingwei.card.http.action.lbsnear";

	public static final String ACTION_REQUEST_NEAR_PAGE = "com.jingwei.card.http.action.nearpage";

	public static final String ACTION_REQUEST_HIDE = "com.jingwei.card.http.action.hide";

	public static final String ACTION_REQUEST_SHAKE = "com.jingwei.card.http.action.shake";

	public static final String ACTION_REQUEST_SHAKE_PAGE = "com.jingwei.card.http.action.shakepage";

	public static final String ACTION_REQUEST_ERASE_POSITION = "com.jingwei.card.http.action.eraseposition";

	public static final String ACTION_UPLOAD_SMS_FILE = "com.jingwei.card.http.action.sms.upload";

	public static final String ACTION_UPLOAD_SIGNATURE = "com.jingwei.card.http.action.signature";

	public static final String ACTION_REQUEST_MESSAGE_MARK_AS_READ_SINGLE = "com.jingwei.card.http.action.message.mark.single";

	public static final String ACTION_REQUEST_MESSAGE_MARK_AS_READ_ALL = "com.jingwei.card.http.action.message.mark.all";

	public static final String ACTION_MESSAGE_SHARD_CARD = "com.jingwei.card.http.action.message.sharecard";

	public static final String ACTION_UPLOAD_CONTACTS = "com.jingwei.card.http.action.uploadcontacts";

	public static final String ACTION_MATCH_CONTACTS = "com.jingwei.card.http.action.matchcontacts";

	public static final String ACTION_IMPORT_CARD = "com.jingwei.card.http.action.importcard";

	public static final String ACTION_DELETE_CHAT_MESSAGE = "com.jingwei.card.http.action.message.delete.single";

	public static final String ACTION_DELETE_CHAT_MESSAGE_BATCH = "com.jingwei.card.http.action.message.delete.batch";

	// 联系人短信邀请
	public static final String ACTION_INVITE_CONTACTS = "com.jingwei.card.http.action.contactsinvite";

	// 收藏的名片短信邀请
	public static final String ACTION_INVITE_CARD = "com.jingwei.card.http.action.cardinvite";

	public static final String ACTION_UPDATE_NOTIFY = "com.jingwei.card.http.action.updatenotify";

	public static final String ACTION_GREET_SEND = "com.jingwei.card.http.action.send";

	public static final String ACTION_CARD_FRONT_POST = "com.jingwei.card.http.action.frontPost";

	public static final String ACTION_CARD_BACK_POST = "com.jingwei.card.http.action.backPost";

	public static final String ACTION_LOAD_REGARDS_POST = "com.jingwei.card.http.action.loadRegards";
	// 微博绑定解除
	public static final String ACTION_WEIBO_UNBIND = "com.jingwei.card.weibo.unbind";

	// 获取当前账户微博基本信息
	public static final String ACTION_WEIBO_THIRD_INFO = "com.jingwei.card.weibo.third-info";

	// 新浪微博绑定
	public static final String ACTION_BIND_SINAWEIBO_POST = "mobile.jingwei.com.account.wbbind";

	// 更改新浪微博绑定
	public static final String ACTION_CHANGE_SINAWEIBO_POST = "mobile.jingwei.com.account.wbchange";

	// 微博绑定 -- 邮箱注册
	public static final String ACTION_WEIBO_BIND_EMAIL = "com.jingwei.card.weiboBindEmail";
	// 微博绑定 -- 校验验证码、生成账号并登录
	public static final String ACTION_WEIBO_CONFIRM = "com.jingwei.card.weibo.confirm";
	
	//发一条微博
	public static final String ACTION_WEIBO_SEND = "com.jingwei.card.sendweibo";
	
	//微博登陆
	public static final String ACTION_WEIBO_LOGIN = "com.jingwei.card.weiboLongin";

	// 第三方登陆--设置密码
	public static final String ACTION_SETPASSWORD_THIRD = "com.jingwei.card.setPassword.third";

	// 修改密码
	public static final String ACTION_CHANGE_PASSWORD = "com.jingwei.card.changePassword";

	// 验证密码
	public static final String ACTION_VERIFY_PASSWORD = "com.jingwei.card.verifyPassword";

	// 获取用户 的新浪微博账号信息
	public static final String ACTION_GETSINA_USERINFO_GET = "api.weibo.com.users.show";
	
	public static final String ACTION_ADD_WORKINFO_POST = "com.jingwei.card.http.action.addWorkInfo";

	public static final String ACTION_DELETE_WORKINFO_POST = "com.jingwei.card.http.action.delWorkInfo";

	public static final String ACTION_UPDATE_WORKINFO_POST = "com.jingwei.card.http.action.updateWorkInfo";

	public static final String ACTION_CARD_STATUS = "com.jingwei.card.status";

	// 找回密码 通过手机号码获取验证码
	public static final String ACTION_GETBACK_PASSWORD_MOBILE_CODE = "com.jingwei.card.getBackPassword.mobile.code";

	// 找回密码 发送验证码进行身份验证
	public static final String ACTION_GETBACK_PASSWORD_VERIFY_CODE = "com.jingwei.card.verify.code";

	// 找回密码 重置密码
	public static final String ACTION_GETPASSWORD_RESETPASSWORD = "com.jingwei.card.resetPassword";

	// 使用手机号注册
	public static final String ACTION_REGISTER_PHONENUMBER = "com.jingwei.card.register4phoneNumber";

	// 注册验证
	public static final String ACTION_REGISTER_CHECKCODE = "com.jingwei.card.registergCheckCode";

	// 设置密码
	public static final String ACTION_REGISTER_SETPASSWORD = "com.jingwei.card.registerg.setpassword";

	// 重试次数
	private static final int TIME_RETRY = 3;

	@Override
	protected void onCustomHandleIntent(Intent intent) {
		Bundle resultData = new Bundle();

		ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		if (info == null || !info.isConnected()) {
			sendResult(intent, null, CODE_NEWORK_UNAVAIABLE);
			return;
		}

		sendResult(intent, null, CODE_START);

		// try {
		//
		// BaseResponse response = handleRequestAction(intent);
		// resultData.putSerializable(BUNDLE_EXTRA_RESULT_DATA, response);
		// sendResult(
		// intent,
		// resultData,
		// response.getStatus().equals(ResponseCode.SUCCESS) ? CODE_SUCCESS
		// : CODE_FAILURE);
		// } catch (Exception e) {
		// e.printStackTrace();
		// resultData.putSerializable(BUNDLE_EXTRA_RESULT_DATA, e);
		// sendResult(intent, resultData, CODE_ERROR);
		// }

		boolean retry = intent.getBooleanExtra(
				AbstractHttpService.INTENT_EXTRA_RETRY, false);
		int timesTried = 1;
		while (timesTried <= TIME_RETRY) {
			try {

				BaseResponse response = handleRequestAction(intent);
				resultData.putSerializable(BUNDLE_EXTRA_RESULT_DATA, response);
				sendResult(
						intent,
						resultData,
						response.getStatus()==0 ? CODE_SUCCESS
								: CODE_FAILURE);
				break;
			} catch (Exception e) {
				e.printStackTrace();
				timesTried++;

				if (!retry || timesTried > 3) {
					resultData.putSerializable(BUNDLE_EXTRA_RESULT_DATA, e);
					sendResult(intent, resultData, CODE_ERROR);
					break;
				}
			}
		}

		sendResult(intent, null, CODE_FINISHE);
	}

	// 执行网络api调用
	protected BaseResponse handleRequestAction(Intent intent)
			throws IOException, GalaxyHttpException {
		String action = intent.getAction();
		Bundle params = intent.getExtras();

		BaseResponse response = null;
		if (action.equals(ACTION_REQUEST_LOGIN)) {
			response = GalaxyApi.login(params);
		} else if (action.equals(ACTION_REQUEST_BIND_USERNAME)) {
			response = GalaxyApi.bindUsername(params);
		} else if (action.equals(ACTION_REQUEST_CONFIRM_BIND_USERNAME)) {
			response = GalaxyApi.confirmBindUserName(params);
		} else if (action.equals(ACTION_REQUEST_LOGOUT)) {
			response = GalaxyApi.logout(params);
		}  else if (action.equals(ACTION_REQUEST_GET_GROUP)) {
			response = GalaxyApi.obtainGroup(params
					.getString(RequestParames.USER_ID));
		} else if (ACTION_REQUEST_OFFLINE_MESSAGE.equals(action)) {
			response = GalaxyApi.obtainOfflineMessage(
					params.getString(RequestParames.USER_ID),
					params.getInt(RequestParames.SEQUENCE),
					params.getInt(RequestParames.COUNT));
		} else if (action.equals(ACTION_REQUEST_NEAR)) {
			//response = JingweiCardApi.lbsNear(params);
		} else if (action.equals(ACTION_REQUEST_NEAR_PAGE)) {
			//response = JingweiCardApi.nearPager(params);
		} else if (action.equals(ACTION_REQUEST_SHAKE)) {
			//response = JingweiCardApi.lbsShake(params);
		} else if (action.equals(ACTION_REQUEST_SHAKE_PAGE)) {
			///response = JingweiCardApi.shakePage(params);
		} else if (action.equals(ACTION_REQUEST_ERASE_POSITION)) {
			//response = JingweiCardApi.erasePosition(params);
		} else if (action.equals(ACTION_IMPORT_CARD)) {
			//response = JingweiCardApi.importCard(params);
		} else if (ACTION_UPLOAD_SMS_FILE.equals(action)) {
			response = GalaxyApi.uploadFile(
					params.getString(RequestParames.USER_ID),
					(File) params.getSerializable(RequestParames.FILE));
		}  else if (ACTION_REQUEST_MESSAGE_MARK_AS_READ_SINGLE.equals(action)) {
			response = GalaxyApi.markAsReadSingle(
					params.getString(RequestParames.USER_ID),
					params.getInt(RequestParames.SEQUENCE));
		} else if (ACTION_REQUEST_MESSAGE_MARK_AS_READ_ALL.equals(action)) {
			response = GalaxyApi.markAsReadAll(
					params.getString(RequestParames.USER_ID),
					params.getInt(RequestParames.SEQUENCE));
		} else if (ACTION_MESSAGE_SHARD_CARD.equals(action)) {
			response = GalaxyApi.shareCardMessage(
					params.getString(RequestParames.USER_ID),
					params.getString(RequestParames.SCARD_ID),
					params.getString(RequestParames.TARGET_IDS));
		} else if (ACTION_GREET_SEND.equals(action)) {
			//response = JingweiCardApi.sendGreet(params);
		} else if (ACTION_DELETE_CHAT_MESSAGE.equals(action)) {
			response = GalaxyApi.deleteChatMessage(
					params.getString(RequestParames.USER_ID),
					params.getInt(RequestParames.SEQUENCE));
		} else if (ACTION_DELETE_CHAT_MESSAGE_BATCH.equals(action)) {
			response = GalaxyApi.deleteChatMessageBatch(
					params.getString(RequestParames.USER_ID),
					params.getString(RequestParames.SENDER),
					params.getInt(RequestParames.SEQUENCE));
		}else if (ACTION_WEIBO_UNBIND.equals(action)) {
			String userID = params.getString(RequestParames.USER_ID);
			response = GalaxyApi.unbindWeibo(userID);
		} else if (ACTION_WEIBO_THIRD_INFO.equals(action)) {
			String userID = params.getString(RequestParames.USER_ID);
			response = GalaxyApi.getThirdInfo(userID);
		} else if (ACTION_GETBACK_PASSWORD_MOBILE_CODE.equals(action)) {
			String userName = params.getString(RequestParames.USER_NAME);
			response = GalaxyApi.getFindPassword(userName);
		} else if (ACTION_GETBACK_PASSWORD_VERIFY_CODE.equals(action)) {
			String userName = params.getString(RequestParames.USER_NAME);
			String verifyCode = params.getString(RequestParames.VERIFY_CODE);
			response = GalaxyApi.sendVerifyCode(userName, verifyCode);
		} else if (ACTION_REGISTER_CHECKCODE.equals(action)) {
			String userName = params.getString(RequestParames.USER_NAME);
			String verifyCode = params.getString(RequestParames.VERIFY_CODE);
			response = GalaxyApi.sendVerifyCode(userName, verifyCode);
			// response = JingweiCardApi.registerCheckCode(userName,
			// verifyCode);
		} else if (ACTION_SETPASSWORD_THIRD.equals(action)) {
			String userId = params.getString(RequestParames.USER_ID);
			String password = params.getString(RequestParames.PASSWORD);
			String passwordConfirm = params
					.getString(RequestParames.REPEAT_PASSWORD);
			String token = params.getString(RequestParames.TOKEN);
			response = GalaxyApi.setPassword4third(userId, password,
					passwordConfirm, token);
		} else if (ACTION_CHANGE_PASSWORD.equals(action)) {
			String userId = params.getString(RequestParames.USER_ID);
			String password = params.getString(RequestParames.PASSWORD);
			String passwordConfirm = params
					.getString(RequestParames.REPEAT_PASSWORD);
			String token = params.getString(RequestParames.TOKEN);
			String oldPassword = params.getString(RequestParames.OLD_PASSWORD);
			response = GalaxyApi.changePassword(userId, password,
					passwordConfirm, token, oldPassword);
		} 
		return response;
	}
}
