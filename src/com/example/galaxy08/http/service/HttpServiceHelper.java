package com.example.galaxy08.http.service;

import java.io.File;

import android.content.Context;
import android.content.Intent;

import com.example.galaxy08.entity.Resume;
import com.example.galaxy08.http.RequestParames;

/**
 * 
 */
public class HttpServiceHelper {

	/*******************************************************************/
	/******************** 登陆注册 *******************************/
	/*******************************************************************/
	// 登陆
	public static RequestHandler doLogin(Context context, String username,
			String password, HttpRequestCallBack receiver) {
		final Intent intent = new Intent(context, WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_REQUEST_LOGIN);
		//AbstractHttpService?
		intent.putExtra(AbstractHttpService.INTENT_EXTRA_RECEIVER,
				receiver == null ? null : receiver.getResultReceiver());
		//手机号 密码
		intent.putExtra(RequestParames.MOBILE, username);
		intent.putExtra(RequestParames.PASSWORD, password);
		context.startService(intent);
		return receiver;
	}

	// 修改登陆名
	public static RequestHandler bindUserName(Context context, String userId,
			String oldusername, String newusername, HttpRequestCallBack receiver) {
		final Intent intent = new Intent(context, WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_REQUEST_BIND_USERNAME);
		intent.putExtra(AbstractHttpService.INTENT_EXTRA_RECEIVER,
				receiver == null ? null : receiver.getResultReceiver());
		intent.putExtra(RequestParames.USER_ID, userId);
		intent.putExtra(RequestParames.OLD_USERNAME, oldusername);
		intent.putExtra(RequestParames.NEW_USERNAME, newusername);
		context.startService(intent);
		return receiver;
	}

	/**
	 * 绑定新浪微博
	 * 
	 * @param context
	 * @param wbuserId
	 * @param wbName
	 * @param accessToken
	 * @param receiver
	 * @return
	 */
	public static RequestHandler bindSinaWeibo(Context context,
			String wbuserId, String userId, String wbName, String accessToken,
			HttpRequestCallBack receiver) {
		final Intent intent = new Intent(context, WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_BIND_SINAWEIBO_POST);
		intent.putExtra(AbstractHttpService.INTENT_EXTRA_RECEIVER,
				receiver == null ? null : receiver.getResultReceiver());
		intent.putExtra(RequestParames.USER_ID, userId);
		intent.putExtra(RequestParames.WB_USERID, wbuserId);
		intent.putExtra(RequestParames.WB_NAME, wbName);
		intent.putExtra(RequestParames.ACCESS_TOKEN, accessToken);
		context.startService(intent);
		return receiver;

	}

	/**
	 * 更改绑定的新浪微博用户
	 * 
	 * @param applicationContext
	 * @param uid
	 * @param weiboName
	 * @param token
	 * @param receiver
	 * @return
	 */
	public static RequestHandler changeSinaWeibo(Context applicationContext,
			String uid, String weiboName, String userId, String token,
			HttpRequestCallBack receiver) {
		final Intent intent = new Intent(applicationContext,
				WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_CHANGE_SINAWEIBO_POST);
		intent.putExtra(AbstractHttpService.INTENT_EXTRA_RECEIVER,
				receiver == null ? null : receiver.getResultReceiver());
		intent.putExtra(RequestParames.CHANGE_WBUSSR_ID, uid);
		intent.putExtra(RequestParames.USER_ID, userId);
		intent.putExtra(RequestParames.CHANGE_WBUSSR_NAME, weiboName);
		intent.putExtra(RequestParames.ACCESS_TOKEN, token);
		applicationContext.startService(intent);
		return receiver;
	}

	// 确认验证码
	public static RequestHandler confirmBindUserName(Context context,
			String userId, String newusername, String verfyCode,
			HttpRequestCallBack receiver) {
		final Intent intent = new Intent(context, WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_REQUEST_CONFIRM_BIND_USERNAME);
		intent.putExtra(AbstractHttpService.INTENT_EXTRA_RECEIVER,
				receiver == null ? null : receiver.getResultReceiver());
		intent.putExtra(RequestParames.USER_ID, userId);
		intent.putExtra(RequestParames.VERIFY_CODE, verfyCode);
		intent.putExtra(RequestParames.NEW_USERNAME, newusername);
		context.startService(intent);
		return receiver;
	}

	// 验证密码
	public static RequestHandler validatePassword(Context context,
			String userId, String password, HttpRequestCallBack receiver) {
		final Intent intent = new Intent(context, WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_REQUEST_VALIDATE_PASSWORD);
		intent.putExtra(AbstractHttpService.INTENT_EXTRA_RECEIVER,
				receiver == null ? null : receiver.getResultReceiver());
		intent.putExtra(RequestParames.USER_ID, userId);
		intent.putExtra(RequestParames.PASSWORD, password);
		context.startService(intent);
		return receiver;
	}

	// 登出
	public static RequestHandler logout(Context context, String userId,
			HttpRequestCallBack receiver) {
		final Intent intent = new Intent(context, WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_REQUEST_LOGOUT);
		intent.putExtra(AbstractHttpService.INTENT_EXTRA_RECEIVER,
				receiver == null ? null : receiver.getResultReceiver());
		intent.putExtra(RequestParames.USER_ID, userId);
		context.startService(intent);
		return receiver;
	}

	public static RequestHandler getTextPush(Context context, String userid,
			String count, HttpRequestCallBack receiver) {
		final Intent intent = new Intent(context, WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_REQUEST_GET_TEXT_PUSH);
		intent.putExtra(AbstractHttpService.INTENT_EXTRA_RECEIVER,
				receiver == null ? null : receiver.getResultReceiver());
		intent.putExtra(RequestParames.USER_ID, userid);
		intent.putExtra(RequestParames.COUNT, count);
		context.startService(intent);
		return receiver;
	}


	public static RequestHandler doUploadCard() {
		// TODO
		return null;
	}

	public static RequestHandler doSwapCard(Context context, String userId,
			String target, String cardId, boolean disPlayPhone,
			boolean disPlayMobile, String referString,
			HttpRequestCallBack receiver) {
		final Intent intent = new Intent(context, WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_REQUEST_SWAPE_CARD);
		intent.putExtra(AbstractHttpService.INTENT_EXTRA_RECEIVER,
				receiver == null ? null : receiver.getResultReceiver());

		intent.putExtra(RequestParames.USER_ID, userId);
		intent.putExtra(RequestParames.TARGET, target);
		intent.putExtra(RequestParames.CARD_ID, cardId);
		intent.putExtra(RequestParames.DISPLAY_PHONE, disPlayPhone);
		intent.putExtra(RequestParames.DISPLAY_MOBILE, disPlayMobile);
		intent.putExtra(RequestParames.SWAP_CARD_REFER, referString);
		context.startService(intent);
		return receiver;
	}

	public static RequestHandler doSwapCardsBatch(Context context,
			String userId, String targetIds, HttpRequestCallBack receiver) {
		final Intent intent = new Intent(context, WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_REQUEST_SWAPE_BATCH);
		intent.putExtra(AbstractHttpService.INTENT_EXTRA_RECEIVER,
				receiver == null ? null : receiver.getResultReceiver());

		intent.putExtra(RequestParames.USER_ID, userId);
		intent.putExtra(RequestParames.TARGET_IDS, targetIds);
		context.startService(intent);
		return receiver;
	}

	/**
	 * 分享名片
	 * 
	 * @param context
	 * @param userId
	 * @param cardid
	 * @param tCardId
	 * @param receiver
	 */
	public static RequestHandler doShareCard(Context context, String userId,
			String cardId, String tCardId, HttpRequestCallBack receiver) {
		final Intent intent = new Intent(context, WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_REQUEST_ShARE_CARD);
		intent.putExtra(AbstractHttpService.INTENT_EXTRA_RECEIVER,
				receiver == null ? null : receiver.getResultReceiver());

		intent.putExtra(RequestParames.USER_ID, userId);
		intent.putExtra(RequestParames.SCARD_ID, cardId);
		intent.putExtra(RequestParames.TCARD_IDS, tCardId);
		context.startService(intent);
		return receiver;
	}

	public static RequestHandler doCollectMyCard(Context context,
			String userId, HttpRequestCallBack receiver) {
		final Intent intent = new Intent(context, WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_REQUEST_COLLECT_MY_CARD);
		intent.putExtra(AbstractHttpService.INTENT_EXTRA_RECEIVER,
				receiver == null ? null : receiver.getResultReceiver());

		intent.putExtra(RequestParames.USER_ID, userId);
		context.startService(intent);
		return receiver;
	}

	// 修改分组名
	public static RequestHandler doModifyGroup(Context context, String userId,
			String oldGrpupId, String oldName, String newName,
			HttpRequestCallBack receiver) {
		final Intent intent = new Intent(context, WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_REQUEST_LOGIN);
		intent.putExtra(AbstractHttpService.INTENT_EXTRA_RECEIVER,
				receiver == null ? null : receiver.getResultReceiver());

		intent.putExtra(RequestParames.USER_ID, userId);
		intent.putExtra(RequestParames.GROUP_ID, oldGrpupId);
		intent.putExtra(RequestParames.OLD_NAME, oldName);
		intent.putExtra(RequestParames.NEW_NAME, newName);
		context.startService(intent);
		return receiver;
	}

	public static RequestHandler doAlikeMyCard(Context context, String userId,
			HttpRequestCallBack receiver) {
		final Intent intent = new Intent(context, WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_REQUEST_ALIKE_MYCARD);
		intent.putExtra(AbstractHttpService.INTENT_EXTRA_RECEIVER,
				receiver == null ? null : receiver.getResultReceiver());

		intent.putExtra(RequestParames.USER_ID, userId);
		context.startService(intent);
		return receiver;
	}

	// public static RequestHandler obtainOfflineMessage(Context context, String
	// userid, int sequence,
	// HttpRequestCallBack receiver) {
	// obtainOfflineMessage(context, userid, sequence, 200, receiver);
	// return receiver;
	// }
	//
	// public static RequestHandler obtainOfflineMessage(Context context, String
	// userid, int sequence, int count,
	// HttpRequestCallBack receiver) {
	// final Intent intent = new Intent(context, WorkHttpService.class);
	// intent.setAction(WorkHttpService.ACTION_REQUEST_OFFLINE_MESSAGE);
	// intent.putExtra(AbstractHttpService.INTENT_EXTRA_RECEIVER,
	// receiver==null?null:receiver.getResultReceiver());
	// intent.putExtra(RequestParames.USER_ID, userid);
	// intent.putExtra(RequestParames.SEQUENCE, sequence);
	// intent.putExtra(RequestParames.COUNT, count);
	// context.startService(intent);
	// return receiver;
	// }

	public static RequestHandler uploadMySignature(Context context,
			String userId, String signature, HttpRequestCallBack receiver) {
		final Intent intent = new Intent(context, WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_UPLOAD_SIGNATURE);
		intent.putExtra(AbstractHttpService.INTENT_EXTRA_RECEIVER,
				receiver == null ? null : receiver.getResultReceiver());
		intent.putExtra(RequestParames.USER_ID, userId);
		intent.putExtra(RequestParames.SIGNATURE, signature);
		context.startService(intent);
		return receiver;
	}

	public static RequestHandler sendUpdataMessage(Context context,
			String userId, String data, HttpRequestCallBack receiver) {
		final Intent intent = new Intent(context, WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_UPDATE_NOTIFY);
		intent.putExtra(AbstractHttpService.INTENT_EXTRA_RECEIVER,
				receiver == null ? null : receiver.getResultReceiver());
		intent.putExtra(RequestParames.USER_ID, userId);
		intent.putExtra(RequestParames.DATA, data);
		context.startService(intent);
		return receiver;
	}

	/**
	 * 提交我的经纬度，获取附近的人
	 * 
	 * @param context
	 * @param userId
	 * @param lon
	 * @param lat
	 * @param receiver
	 */
	public static RequestHandler lbsNear(Context context, String userId,
			String lon, String lat, HttpRequestCallBack receiver) {
		final Intent intent = new Intent(context, WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_REQUEST_NEAR);
		intent.putExtra(AbstractHttpService.INTENT_EXTRA_RECEIVER,
				receiver == null ? null : receiver.getResultReceiver());
		intent.putExtra(AbstractHttpService.INTENT_EXTRA_RETRY, true);
		intent.putExtra(RequestParames.USER_ID, userId);
		intent.putExtra(RequestParames.LON, lon);
		intent.putExtra(RequestParames.LAT, lat);
		context.startService(intent);
		return receiver;
	}

	/**
	 * v3版本分页获取附近的人
	 * 
	 * @param context
	 * @param userId
	 * @param lon
	 * @param lat
	 * @param page
	 * @param receiver
	 */
	public static RequestHandler nearPage(Context context, String userId,
			String lon, String lat, int page, HttpRequestCallBack receiver) {
		final Intent intent = new Intent(context, WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_REQUEST_NEAR_PAGE);
		intent.putExtra(AbstractHttpService.INTENT_EXTRA_RECEIVER,
				receiver == null ? null : receiver.getResultReceiver());
		intent.putExtra(RequestParames.USER_ID, userId);
		intent.putExtra(RequestParames.LON, lon);
		intent.putExtra(RequestParames.LAT, lat);
		intent.putExtra(RequestParames.PAGE, page);
		context.startService(intent);
		return receiver;
	}

	public static RequestHandler hide(Context context, String userId,
			boolean show, HttpRequestCallBack receiver) {
		final Intent intent = new Intent(context, WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_REQUEST_HIDE);
		intent.putExtra(AbstractHttpService.INTENT_EXTRA_RECEIVER,
				receiver == null ? null : receiver.getResultReceiver());
		intent.putExtra(RequestParames.USER_ID, userId);
		intent.putExtra(RequestParames.SHOW, String.valueOf(show));
		context.startService(intent);
		return receiver;
	}

	/**
	 * 摇一摇
	 * 
	 * @param context
	 * @param userId
	 * @param lon
	 * @param lat
	 * @param receiver
	 */
	public static RequestHandler lbsShake(Context context, String userId,
			String lon, String lat, HttpRequestCallBack receiver) {
		final Intent intent = new Intent(context, WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_REQUEST_SHAKE);
		intent.putExtra(AbstractHttpService.INTENT_EXTRA_RECEIVER,
				receiver == null ? null : receiver.getResultReceiver());
		intent.putExtra(AbstractHttpService.INTENT_EXTRA_RETRY, true);
		intent.putExtra(RequestParames.USER_ID, userId);
		intent.putExtra(RequestParames.LON, lon);
		intent.putExtra(RequestParames.LAT, lat);
		context.startService(intent);
		return receiver;
	}

	/**
	 * 分页获取摇一摇结果
	 * 
	 * @param context
	 * @param userId
	 * @param id
	 * @param page
	 * @param receiver
	 */
	public static RequestHandler shakePage(Context context, String userId,
			String id, int page, HttpRequestCallBack receiver) {
		final Intent intent = new Intent(context, WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_REQUEST_SHAKE_PAGE);
		intent.putExtra(AbstractHttpService.INTENT_EXTRA_RECEIVER,
				receiver == null ? null : receiver.getResultReceiver());
		intent.putExtra(RequestParames.USER_ID, userId);
		intent.putExtra(RequestParames.ID, id);
		intent.putExtra(RequestParames.PAGE, page);
		context.startService(intent);
		return receiver;
	}

	public static RequestHandler erasePosition(Context context, String userId,
			HttpRequestCallBack receiver) {
		final Intent intent = new Intent(context, WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_REQUEST_ERASE_POSITION);
		intent.putExtra(AbstractHttpService.INTENT_EXTRA_RECEIVER,
				receiver == null ? null : receiver.getResultReceiver());
		intent.putExtra(RequestParames.USER_ID, userId);
		context.startService(intent);
		return receiver;
	}

	/**
	 * 通过手机号搜索联系人
	 * 
	 * @param context
	 * @param userId
	 * @param mobile
	 * @param receiver
	 */
	public static RequestHandler searchByMobile(Context context, String userId,
			String mobile, HttpRequestCallBack receiver) {
		final Intent intent = new Intent(context, WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_REQUEST_SEARCH_BY_MOBILE);
		intent.putExtra(AbstractHttpService.INTENT_EXTRA_RECEIVER,
				receiver == null ? null : receiver.getResultReceiver());
		intent.putExtra(RequestParames.USER_ID, userId);
		intent.putExtra(RequestParames.MOBILE, mobile);
		context.startService(intent);
		return receiver;
	}

	public static RequestHandler doUploadContacts(Context context,
			String userId, String data, HttpRequestCallBack receiver) {
		final Intent intent = new Intent(context, WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_UPLOAD_CONTACTS);
		intent.putExtra(AbstractHttpService.INTENT_EXTRA_RECEIVER,
				receiver == null ? null : receiver.getResultReceiver());
		intent.putExtra(RequestParames.USER_ID, userId);
		intent.putExtra(RequestParames.DATA, data);
		context.startService(intent);
		return receiver;
	}

	// 通讯录匹配
	public static RequestHandler doMactchContacts(Context context,
			String userId, String data, HttpRequestCallBack receiver) {
		final Intent intent = new Intent(context, WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_MATCH_CONTACTS);
		intent.putExtra(AbstractHttpService.INTENT_EXTRA_RECEIVER,
				receiver == null ? null : receiver.getResultReceiver());
		intent.putExtra(RequestParames.USER_ID, userId);
		intent.putExtra(RequestParames.DATA, data);
		context.startService(intent);
		return receiver;
	}

	// 通讯录匹配
	public static RequestHandler doImportCard(Context context, String userId,
			String data, HttpRequestCallBack receiver) {
		final Intent intent = new Intent(context, WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_IMPORT_CARD);
		intent.putExtra(AbstractHttpService.INTENT_EXTRA_RECEIVER,
				receiver == null ? null : receiver.getResultReceiver());
		intent.putExtra(RequestParames.USER_ID, userId);
		intent.putExtra(RequestParames.DATA, data);
		context.startService(intent);
		return receiver;
	}

	public static RequestHandler doPostSMSFile(Context context, String userid,
			File file, HttpRequestCallBack receiver) {
		final Intent intent = new Intent(context, WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_UPLOAD_SMS_FILE);
		intent.putExtra(AbstractHttpService.INTENT_EXTRA_RECEIVER,
				receiver == null ? null : receiver.getResultReceiver());
		intent.putExtra(RequestParames.USER_ID, userid);
		intent.putExtra(RequestParames.FILE, file);
		context.startService(intent);
		return receiver;
	}

	public static RequestHandler doPostMarkAsReadSingle(Context context,
			String userid, int sequence, HttpRequestCallBack receiver) {
		final Intent intent = new Intent(context, WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_REQUEST_MESSAGE_MARK_AS_READ_SINGLE);
		intent.putExtra(AbstractHttpService.INTENT_EXTRA_RECEIVER,
				receiver == null ? null : receiver.getResultReceiver());
		intent.putExtra(RequestParames.USER_ID, userid);
		intent.putExtra(RequestParames.SEQUENCE, sequence);
		context.startService(intent);
		return receiver;
	}

	public static RequestHandler doPostMarkAsReadAll(Context context,
			String userid, int sequence, HttpRequestCallBack receiver) {
		final Intent intent = new Intent(context, WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_REQUEST_MESSAGE_MARK_AS_READ_ALL);
		intent.putExtra(AbstractHttpService.INTENT_EXTRA_RECEIVER,
				receiver == null ? null : receiver.getResultReceiver());
		intent.putExtra(RequestParames.USER_ID, userid);
		intent.putExtra(RequestParames.SEQUENCE, sequence);
		context.startService(intent);
		return receiver;
	}

	public static RequestHandler shareCardMessage(Context context,
			String userid, String cardid, String targetid,
			HttpRequestCallBack receiver) {
		final Intent intent = new Intent(context, WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_MESSAGE_SHARD_CARD);
		intent.putExtra(AbstractHttpService.INTENT_EXTRA_RECEIVER,
				receiver == null ? null : receiver.getResultReceiver());
		intent.putExtra(RequestParames.USER_ID, userid);
		intent.putExtra(RequestParames.SCARD_ID, cardid);
		intent.putExtra(RequestParames.TARGET_IDS, targetid);
		context.startService(intent);
		return receiver;
	}

	public static RequestHandler inviteContactBySMS(Context context,
			String userid, String contextid, HttpRequestCallBack receiver) {
		final Intent intent = new Intent(context, WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_INVITE_CONTACTS);
		intent.putExtra(AbstractHttpService.INTENT_EXTRA_RECEIVER,
				receiver == null ? null : receiver.getResultReceiver());
		intent.putExtra(RequestParames.USER_ID, userid);
		intent.putExtra(RequestParames.CONTEXT_ID, contextid);
		context.startService(intent);
		return receiver;
	}

	public static RequestHandler inviteCardBySMS(Context context,
			String userid, String cardId, HttpRequestCallBack receiver) {
		final Intent intent = new Intent(context, WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_INVITE_CARD);
		intent.putExtra(AbstractHttpService.INTENT_EXTRA_RECEIVER,
				receiver == null ? null : receiver.getResultReceiver());
		intent.putExtra(RequestParames.USER_ID, userid);
		intent.putExtra(RequestParames.CARD_ID, cardId);
		context.startService(intent);
		return receiver;
	}
/*
 * 	sendGreet 发送祝福 暂时不需要 2013/04/15
 */
//	public static RequestHandler sendGreet(Context context, String userID,
//			String template, String text, String voicePath, int audioTime,
//			String cardIds, ArrayList<ContactForSend> contactsSendList,
//			HttpRequestCallBack receiver) {
//		Intent intent = new Intent(context, WorkHttpService.class);
//		intent.setAction(WorkHttpService.ACTION_GREET_SEND);
//		intent.putExtra(RequestParames.USER_ID, userID);
//		intent.putExtra(RequestParames.TEMPLATE, template);
//		intent.putExtra(RequestParames.TEXT, text);
//		intent.putExtra(RequestParames.VOICE_PATH, voicePath);
//		intent.putExtra(RequestParames.AUDIO_TIME, audioTime);
//		intent.putExtra(RequestParames.CARD_IDS, cardIds);
//		intent.putExtra(RequestParames.CONTACT, contactsSendList);
//		intent.putExtra(AbstractHttpService.INTENT_EXTRA_RECEIVER,
//				receiver == null ? null : receiver.getResultReceiver());
//		context.startService(intent);
//		return receiver;
//	}

	public static RequestHandler doPostDeleteChatMessage(Context context,
			String userid, int sequence, HttpRequestCallBack receiver) {
		final Intent intent = new Intent(context, WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_DELETE_CHAT_MESSAGE);
		intent.putExtra(AbstractHttpService.INTENT_EXTRA_RECEIVER,
				receiver == null ? null : receiver.getResultReceiver());
		intent.putExtra(RequestParames.USER_ID, userid);
		intent.putExtra(RequestParames.SEQUENCE, sequence);
		context.startService(intent);
		return receiver;
	}

	public static RequestHandler doPostDeleteChatMessageBatch(Context context,
			String userid, String targetid, int sequence,
			HttpRequestCallBack receiver) {
		final Intent intent = new Intent(context, WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_DELETE_CHAT_MESSAGE_BATCH);
		intent.putExtra(AbstractHttpService.INTENT_EXTRA_RECEIVER,
				receiver == null ? null : receiver.getResultReceiver());
		intent.putExtra(RequestParames.USER_ID, userid);
		intent.putExtra(RequestParames.SEQUENCE, sequence);
		intent.putExtra(RequestParames.SENDER, targetid);
		context.startService(intent);
		return receiver;
	}

	// post the front side of the card
	public static RequestHandler doPostCard(Context context, String userid,
			String cardType, String cardId, File file,
			HttpRequestCallBack receiver) {
		final Intent intent = new Intent(context, WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_CARD_FRONT_POST);
		intent.putExtra(AbstractHttpService.INTENT_EXTRA_RECEIVER,
				receiver == null ? null : receiver.getResultReceiver());
		intent.putExtra(RequestParames.USER_ID, userid);
		intent.putExtra(RequestParames.CARD_TYPE, cardType);
		intent.putExtra(RequestParames.CARD_ID, cardId);
		intent.putExtra(RequestParames.FILE, file);
		context.startService(intent);
		return receiver;
	}

	// post the back side of the card
	public static RequestHandler doPostReverseCard(Context context,
			String userid, String cardType, String cardId, File file,
			HttpRequestCallBack receiver) {
		final Intent intent = new Intent(context, WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_CARD_BACK_POST);
		intent.putExtra(AbstractHttpService.INTENT_EXTRA_RECEIVER,
				receiver == null ? null : receiver.getResultReceiver());
		intent.putExtra(RequestParames.USER_ID, userid);
		intent.putExtra(RequestParames.CARD_TYPE, cardType);
		intent.putExtra(RequestParames.CARD_ID, cardId);
		intent.putExtra(RequestParames.FILE, file);
		context.startService(intent);
		return receiver;
	}

	// 解除绑定新浪微博
	public static RequestHandler unbindWeibo(Context context, String userid,
			HttpRequestCallBack receiver) {
		final Intent intent = new Intent(context, WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_WEIBO_UNBIND);
		intent.putExtra(AbstractHttpService.INTENT_EXTRA_RECEIVER,
				receiver == null ? null : receiver.getResultReceiver());
		intent.putExtra(RequestParames.USER_ID, userid);
		context.startService(intent);
		return receiver;
	}

	// 获取当前账户绑定的第三方信息
	public static RequestHandler getThirdInfo(Context context, String userid,
			HttpRequestCallBack receiver) {
		final Intent intent = new Intent(context, WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_WEIBO_THIRD_INFO);
		intent.putExtra(AbstractHttpService.INTENT_EXTRA_RECEIVER,
				receiver == null ? null : receiver.getResultReceiver());
		intent.putExtra(RequestParames.USER_ID, userid);
		context.startService(intent);
		return receiver;
	}

	public static void doLoadRegards(Context context, String userid,
			HttpRequestCallBack receiver) {
		final Intent intent = new Intent(context, WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_LOAD_REGARDS_POST);
		intent.putExtra(AbstractHttpService.INTENT_EXTRA_RECEIVER,
				receiver == null ? null : receiver.getResultReceiver());
		intent.putExtra(RequestParames.USER_ID, userid);
		context.startService(intent);
	}

	// 增加履历
	public static void doAddWorkInfo(Context context, Resume resume,
			HttpRequestCallBack receiver) {
		final Intent intent = new Intent(context, WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_ADD_WORKINFO_POST);
		intent.putExtra(AbstractHttpService.INTENT_EXTRA_RECEIVER,
				receiver == null ? null : receiver.getResultReceiver());
		intent.putExtra("resume", resume);
		context.startService(intent);
	}

	// 删除履历
	public static void doDelWorkInfo(Context context, String userId,
			String workId, HttpRequestCallBack receiver) {
		final Intent intent = new Intent(context, WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_DELETE_WORKINFO_POST);
		intent.putExtra(AbstractHttpService.INTENT_EXTRA_RECEIVER,
				receiver == null ? null : receiver.getResultReceiver());
		intent.putExtra(RequestParames.USER_ID, userId);
		intent.putExtra(RequestParames.WORK_ID, workId);
		context.startService(intent);
	}

	// 更新履历
	public static void doUpdateWorkInfo(Context context, Resume resume,
			HttpRequestCallBack receiver) {
		final Intent intent = new Intent(context, WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_UPDATE_WORKINFO_POST);
		intent.putExtra(AbstractHttpService.INTENT_EXTRA_RECEIVER,
				receiver == null ? null : receiver.getResultReceiver());
		intent.putExtra("resume", resume);
		context.startService(intent);
	}

	// 获取名片的状态信息
	public static RequestHandler getCardStatus(Context context, String userId,
			String cardId, HttpRequestCallBack receiver) {
		final Intent intent = new Intent(context, WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_CARD_STATUS);
		intent.putExtra(AbstractHttpService.INTENT_EXTRA_RECEIVER,
				receiver == null ? null : receiver.getResultReceiver());
		intent.putExtra(RequestParames.USER_ID, userId);
		intent.putExtra(RequestParames.TARGET_ID, cardId);
		context.startService(intent);
		return receiver;
	}

	// 找回密码 通过手机号码获取验证码
	public static RequestHandler getBackPassword4mobile2code(
			Context applicationContext, String userName,
			HttpRequestCallBack httpRequestCallBack) {

		final Intent intent = new Intent(applicationContext,
				WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_GETBACK_PASSWORD_MOBILE_CODE);
		intent.putExtra(
				AbstractHttpService.INTENT_EXTRA_RECEIVER,
				httpRequestCallBack == null ? null : httpRequestCallBack
						.getResultReceiver());
		intent.putExtra(RequestParames.USER_NAME, userName);
		applicationContext.startService(intent);
		return httpRequestCallBack;
	}

	// 找回密码 发送验证码
	public static RequestHandler verifyCode(Context applicationContext,
			String username, String verifyCode,
			HttpRequestCallBack httpRequestCallBack) {
		final Intent intent = new Intent(applicationContext,
				WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_GETBACK_PASSWORD_VERIFY_CODE);
		intent.putExtra(
				AbstractHttpService.INTENT_EXTRA_RECEIVER,
				httpRequestCallBack == null ? null : httpRequestCallBack
						.getResultReceiver());
		intent.putExtra(RequestParames.USER_NAME, username);
		intent.putExtra(RequestParames.VERIFY_CODE, verifyCode);
		applicationContext.startService(intent);
		return httpRequestCallBack;

	}

	// 找回密码 重置密码 ---老界面的接口。。已经弃用
	public static RequestHandler resetPassword(Context applicationContext,
			String username, HttpRequestCallBack httpRequestCallBack) {
		final Intent intent = new Intent(applicationContext,
				WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_GETPASSWORD_RESETPASSWORD);
		intent.putExtra(
				AbstractHttpService.INTENT_EXTRA_RECEIVER,
				httpRequestCallBack == null ? null : httpRequestCallBack
						.getResultReceiver());
		intent.putExtra(RequestParames.USER_NAME, username);
		applicationContext.startService(intent);
		return httpRequestCallBack;

	}

	// 找回密码 重置密码
	public static RequestHandler resetPassword(Context applicationContext,
			String username, String verifyCode, String password,
			String passwordConfirm, HttpRequestCallBack httpRequestCallBack) {
		final Intent intent = new Intent(applicationContext,
				WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_GETPASSWORD_RESETPASSWORD);
		intent.putExtra(
				AbstractHttpService.INTENT_EXTRA_RECEIVER,
				httpRequestCallBack == null ? null : httpRequestCallBack
						.getResultReceiver());
		intent.putExtra(RequestParames.USER_NAME, username);
		intent.putExtra(RequestParames.VERIFY_CODE, verifyCode);
		intent.putExtra(RequestParames.PASSWORD, password);
		intent.putExtra(RequestParames.REPEAT_PASSWORD, passwordConfirm);
		applicationContext.startService(intent);
		return httpRequestCallBack;
	}

	// 使用手机号注册
	public static RequestHandler registerByMobile(Context applicationContext,
			String userName, String secret,
			HttpRequestCallBack httpRequestCallBack) {
		final Intent intent = new Intent(applicationContext,
				WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_REGISTER_PHONENUMBER);
		intent.putExtra(
				AbstractHttpService.INTENT_EXTRA_RECEIVER,
				httpRequestCallBack == null ? null : httpRequestCallBack
						.getResultReceiver());
		intent.putExtra(RequestParames.USER_NAME, userName);
		intent.putExtra(RequestParames.SECRET, secret);
		applicationContext.startService(intent);
		return httpRequestCallBack;
	}

	public static RequestHandler registergCheckCode(Context applicationContext,
			String userName, String verifyCode,
			HttpRequestCallBack httpRequestCallBack) {
		final Intent intent = new Intent(applicationContext,
				WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_REGISTER_CHECKCODE);
		intent.putExtra(
				AbstractHttpService.INTENT_EXTRA_RECEIVER,
				httpRequestCallBack == null ? null : httpRequestCallBack
						.getResultReceiver());
		intent.putExtra(RequestParames.USER_NAME, userName);
		intent.putExtra(RequestParames.VERIFY_CODE, verifyCode);
		applicationContext.startService(intent);
		return httpRequestCallBack;

	}

	public static RequestHandler setpassWord(Context applicationContext,
			String username, String password, String passwordConfirm,
			String verifyCode, String touristUerIdString,
			HttpRequestCallBack httpRequestCallBack) {
		final Intent intent = new Intent(applicationContext,
				WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_REGISTER_SETPASSWORD);
		intent.putExtra(
				AbstractHttpService.INTENT_EXTRA_RECEIVER,
				httpRequestCallBack == null ? null : httpRequestCallBack
						.getResultReceiver());
		intent.putExtra(RequestParames.USER_NAME, username);
		intent.putExtra(RequestParames.VERIFY_CODE, verifyCode);
		intent.putExtra(RequestParames.PASSWORD, password);
		intent.putExtra(RequestParames.REPEAT_PASSWORD, passwordConfirm);
		intent.putExtra(RequestParames.TRIAL_USERID, touristUerIdString);
		applicationContext.startService(intent);
		return httpRequestCallBack;
	}

	// 新浪微博绑定邮箱
	public static RequestHandler weiboBindEmail(Context applicationContext,
			String userName, String weiboUserId, String secret,
			String accesstoken, HttpRequestCallBack httpRequestCallBack) {

		final Intent intent = new Intent(applicationContext,
				WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_WEIBO_BIND_EMAIL);
		intent.putExtra(
				AbstractHttpService.INTENT_EXTRA_RECEIVER,
				httpRequestCallBack == null ? null : httpRequestCallBack
						.getResultReceiver());
		intent.putExtra(RequestParames.USER_NAME, userName);
		intent.putExtra(RequestParames.WB_USERID, weiboUserId);
		intent.putExtra(RequestParames.SECRET, secret);
		intent.putExtra(RequestParames.ACCESS_TOKEN, accesstoken);
		applicationContext.startService(intent);
		return httpRequestCallBack;
	}

	 //   绑定微博账号完成   校验验证码、生成账号并登录
	public static RequestHandler weiboConfirm(Context applicationContext,
			String username, String weiboUserId, String weiboName,
			String accesstoken, String verifyCode,
			HttpRequestCallBack httpRequestCallBack) {

		final Intent intent = new Intent(applicationContext,
				WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_WEIBO_CONFIRM);
		intent.putExtra(
				AbstractHttpService.INTENT_EXTRA_RECEIVER,
				httpRequestCallBack == null ? null : httpRequestCallBack
						.getResultReceiver());
		intent.putExtra(RequestParames.USER_NAME, username);
		intent.putExtra(RequestParames.WB_NAME, weiboName);
		intent.putExtra(RequestParames.WB_USERID, weiboUserId);
		intent.putExtra(RequestParames.ACCESS_TOKEN, accesstoken);
		intent.putExtra(RequestParames.VERIFY_CODE, verifyCode);
		applicationContext.startService(intent);
		return httpRequestCallBack;

	}

	public static RequestHandler setPassword4third(Context applicationContext,
			String userId, String password, String passwordConfirm,
			String token, HttpRequestCallBack httpRequestCallBack) {
		
		final Intent intent = new Intent(applicationContext,
				WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_SETPASSWORD_THIRD);
		intent.putExtra(
				AbstractHttpService.INTENT_EXTRA_RECEIVER,
				httpRequestCallBack == null ? null : httpRequestCallBack
						.getResultReceiver());
		intent.putExtra(RequestParames.USER_ID, userId);
		intent.putExtra(RequestParames.PASSWORD, password);
		intent.putExtra(RequestParames.REPEAT_PASSWORD, passwordConfirm);
		intent.putExtra(RequestParames.TOKEN, token);
		applicationContext.startService(intent);
		return httpRequestCallBack;

	}

	public static RequestHandler changePassword(Context applicationContext,
			String userId, String password, String passwordConfirm,
			String token, String oldPassword,
			HttpRequestCallBack httpRequestCallBack) {
		final Intent intent = new Intent(applicationContext,
				WorkHttpService.class);
		
		intent.setAction(WorkHttpService.ACTION_CHANGE_PASSWORD);
		intent.putExtra(
				AbstractHttpService.INTENT_EXTRA_RECEIVER,
				httpRequestCallBack == null ? null : httpRequestCallBack
						.getResultReceiver());
		
		intent.putExtra(RequestParames.USER_ID, userId);
		intent.putExtra(RequestParames.PASSWORD, password);
		intent.putExtra(RequestParames.REPEAT_PASSWORD, passwordConfirm);
		intent.putExtra(RequestParames.TOKEN, token);
		intent.putExtra(RequestParames.OLD_PASSWORD, oldPassword);
		applicationContext.startService(intent);
		return httpRequestCallBack;
	}

	//验证密码
	public static RequestHandler validatePassword(Context applicationContext,
			String password, String userId, String token,
			HttpRequestCallBack httpRequestCallBack) {
		final Intent intent = new Intent(applicationContext,
				WorkHttpService.class);
		
		intent.setAction(WorkHttpService.ACTION_VERIFY_PASSWORD);
		intent.putExtra(
				AbstractHttpService.INTENT_EXTRA_RECEIVER,
				httpRequestCallBack == null ? null : httpRequestCallBack
						.getResultReceiver());
		
		intent.putExtra(RequestParames.USER_ID, userId);
		intent.putExtra(RequestParames.PASSWORD, password);
		intent.putExtra(RequestParames.TOKEN, token);
		applicationContext.startService(intent);
		return httpRequestCallBack;
	}

	//发送一条微博
	public static RequestHandler sendWeibo(Context applicationContext,
			String accessToken, HttpRequestCallBack httpRequestCallBack) {
		
		final Intent intent = new Intent(applicationContext,
				WorkHttpService.class);
		
		intent.setAction(WorkHttpService.ACTION_VERIFY_PASSWORD);
		intent.putExtra(
				AbstractHttpService.INTENT_EXTRA_RECEIVER,
				httpRequestCallBack == null ? null : httpRequestCallBack
						.getResultReceiver());
		intent.putExtra(RequestParames.ACCESS_TOKEN, accessToken);
		applicationContext.startService(intent);
		return httpRequestCallBack;
	}

	//微博登陆
	public static RequestHandler weiboLogin(Context applicationContext, String weiboUserId,
			String weiboName, String accesstoken, String secret,
			HttpRequestCallBack httpRequestCallBack) {
		
		final Intent intent = new Intent(applicationContext,
				WorkHttpService.class);
		intent.setAction(WorkHttpService.ACTION_WEIBO_LOGIN);
		intent.putExtra(
				AbstractHttpService.INTENT_EXTRA_RECEIVER,
				httpRequestCallBack == null ? null : httpRequestCallBack
						.getResultReceiver());
		intent.putExtra(RequestParames.WB_NAME, weiboName);
		intent.putExtra(RequestParames.WB_USERID, weiboUserId);
		intent.putExtra(RequestParames.ACCESS_TOKEN, accesstoken);
		intent.putExtra(RequestParames.SECRET, secret);
		applicationContext.startService(intent);
		return httpRequestCallBack;
	}

}
