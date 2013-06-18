package com.example.galaxy08.http.model;

import java.util.ArrayList;
import java.util.List;

import com.example.galaxy08.entity.Invite;

/**
 * ResponseDataBean 服务器返回来的数据结构 2013/04/15
 */
public class ResponseDataBean {

	private String token;

	private String userId;

	private String setPassword;

	private String groupId;

	private String countryCode;
	
	/**
	 * 用户接受到的邀请
	 */
	private ArrayList<Invite> invites;

	// 当前配额
	private String curQuota;

	// 修改后的groupId
	private String newGroupId;

	private String groupName;

	// 名片原始图片的地址
	private String picurl;

	private String path;

	// 头像大图地址
	private String largepic;

	// 待验证记录的id
	private int verifyId;

	private String verifyCode;

	private int img;

	private int lastupdateTime;

	// 数量
	private int size;

	private int count;

	// 操作结果 true or false
	private boolean result;

	private int total;

	private int pos;

	private String timestamp;

	// 已删除的名片
	private String[] delete;

	// 无法识别的id数组
	private String[] notocr;

	private List<ResponseMessageBean> letters;

	private String datetime;

	private String id;

	private int meters;

	private int type;

	private String uid;

	private String[] position;

	private String targetId;

	// private List<ResponseCollectMyBean> contact;

	private boolean find;

	private boolean isMyself;

	private String overQuotaUrl;

	private String overQuotaText;


	private int unmatchCount;

	private int matchCount;

	private String regardlink;

	// 增加履历
	private String workId;

	private String voiceUrl;

	// private WeiboInfoBean sinaWeibo;

	// 增加卡片的状态,0:未请求,1:已请求,2:已交换
	private String reqStatus;

	// 登陆-- 用户名称
	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getReqStatus() {
		return reqStatus;
	}

	public void setReqStatus(String reqStatus) {
		this.reqStatus = reqStatus;
	}

	public String getVoiceUrl() {
		return voiceUrl;
	}

	public void setVoiceUrl(String voiceUrl) {
		this.voiceUrl = voiceUrl;
	}

	public String getWorkId() {
		return workId;
	}

	public void setWorkId(String workId) {
		this.workId = workId;
	}

	public int getUnmatchCount() {

		return unmatchCount;
	}

	public void setUnmatchCount(int unmatchCount) {
		this.unmatchCount = unmatchCount;
	}

	public int getMatchCount() {
		return matchCount;
	}

	public void setMatchCount(int matchCount) {
		this.matchCount = matchCount;
	}

	public ResponseDataBean() {

	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getVerifyId() {
		return verifyId;
	}

	public void setVerifyId(int verifyId) {
		this.verifyId = verifyId;
	}

	public String[] getNotocr() {
		return notocr;
	}

	public void setNotocr(String[] notocr) {
		this.notocr = notocr;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getNewGroupId() {
		return newGroupId;
	}

	public void setNewGroupId(String newGroupId) {
		this.newGroupId = newGroupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getPicurl() {
		return picurl;
	}

	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}

	public String getLargepic() {
		return largepic;
	}

	public void setLargepic(String largepic) {
		this.largepic = largepic;
	}

	public int getImg() {
		return img;
	}

	public void setImg(int img) {
		this.img = img;
	}

	public int getLastupdateTime() {
		return lastupdateTime;
	}

	public void setLastupdateTime(int lastupdateTime) {
		this.lastupdateTime = lastupdateTime;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String[] getDelete() {
		return delete;
	}

	public void setDelete(String[] delete) {
		this.delete = delete;
	}

	public List<ResponseMessageBean> getLetters() {
		return letters;
	}

	public void setLetters(List<ResponseMessageBean> letters) {
		this.letters = letters;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getMeters() {
		return meters;
	}

	public void setMeters(int meters) {
		this.meters = meters;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getOverQuotaUrl() {
		return overQuotaUrl;
	}

	void setOverQuotaUrl(String url) {
		this.overQuotaUrl = url;
	}

	public String getOverQuotaText() {
		return overQuotaText;
	}

	public void setOverQuotaText(String text) {
		this.overQuotaText = text;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String[] getPosition() {
		return position;
	}

	public void setPosition(String[] position) {
		this.position = position;
	}

	public boolean isFind() {
		return find;
	}

	public void setFind(boolean find) {
		this.find = find;
	}

	public boolean isMyself() {
		return isMyself;
	}

	public void setMyself(boolean isMyself) {
		this.isMyself = isMyself;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getSetPassword() {
		return setPassword;
	}

	public void setSetPassword(String setPassword) {
		this.setPassword = setPassword;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCurQuota() {
		return curQuota;
	}

	public void setCurQuota(String curQuota) {
		this.curQuota = curQuota;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public String getRegardlink() {
		return regardlink;
	}

	public void setRegardlink(String regardlink) {
		this.regardlink = regardlink;
	}

	public ArrayList<Invite> getInvites() {
		return invites;
	}

	public void setInvites(ArrayList<Invite> invites) {
		this.invites = invites;
	}
}
