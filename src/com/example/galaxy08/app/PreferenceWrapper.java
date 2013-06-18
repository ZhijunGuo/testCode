/**
 * 
 */

package com.example.galaxy08.app;

/**
 * 包装对app  SharedPreferences的使用
 * 2013/04/15
 */
import java.util.Map;

import com.example.galaxy08.util.StringUtils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceWrapper {
	
	/**
	 * 2013/06/04
	 */
	public static final String AUTOLOGIN = "autoLogin";
	public static final String MOBILE = "mobile";
	public static final String COMPANYACCOUNT = "companyaccount";
	public static final String MD5_PASSWORD = "md5Password";
	public static final String TOKEN = "token";
	public static final String USER_ID = "userID";
	public static final String LAST_UPDATE_TIME = "lastUpdateTime";
	public static final String CHAT_MESSAGE_SEQUENCE = "chat_message_squence";
	/****/

	//记录群组
	public static final String GROUP_NUM = "group_num";
	
    private static final String SEPARATOR = "_";

    private static final String PREF_NAME = "galaxysetting";
    
    public static final String TOURISTS_MODE_USERID = "touristsModeUserID";
    
    public static final String USER_NAME = "username";

    public static final String LOGIN_MODE = "loginMode";

    public static final String ACTIVE = "active";
    
    public static final String RE_BLESS = "rebless";
    
    public static final String TEMPLATE_TYPE = "_template_type";
    
    //版本升级
    public static final String VERSION = "version";
    
    public static final String NEW_VERSION = "newversion";
    
    public static final String VERSION_DESC = "versiondesc";
    
    public static final String VERSION_URL = "versionurl";
    
    public static final String VERSION_UPDATE = "versionupdate";
    
    public static final String VERSION_TYPE = "versiontype";
    
    
    public static final String ACTIVE_CLIENT = "activeclient";
    
    public static final String INVITE = "invite";
    
    public static final String WAIT = "wait";
    
    public static final String SORT = "sort";
    
    public static final String TIME_STAMP = "timestamp";

    public static final String NOTIFICATION = "notification";
    
    public static final String  PREFFERENCE_KEY_ACTIVE_APPLICATION = "PREFFERENCE_KEY_ACTIVE_APPLICATION";
    
    public static final String RING = "ring";

    public static final String SHAKE = "shake";
    
    public static final String LOGIN = "login";

    public static final String TAKE = "take";

    public static final String LEAD = "lead";
    
    public static final String TAKE_FINISH = "takefinish";
    
    public static final String IS_FROM_CAMERA = "isFromCamera";
    
    public static final String IS_UPDATE_VERSION = "mIsUpdateVersion";
    
    public static final String COUNTRY_CODE = "countryCode";
    
    public static final String UPLOAD_PHOTO = "uploadphoto";

    public static final String PHOTO_REMOTE_PATH = "photoRemotePath";

    public static final String REGISTE = "registe";
    
    public static final String FIND_SECRET_SIGN="find_secret_new";
    
    public static final String SYSTEM_CAMERA_COUNT = "system_count";
    
    public static final String TOURISTOR_QUIT = "tourist_quit";


    public static final String SOUND_ENABLE = "sound_enable";

    // 是否随即播放音效
    public static final String SOUND_RANDOM = "sound_random";

    public static final String SHARE_PROMPT = "shareprompt";

    public static final String CONTACT_LEAD = "contact_lead";
    
    public static final String CONTACT_UPOAD_LAST_MAXID = "upload_maxid";
    
    public static final String CUSTOMER_CAMERA = "customer_camera";
    
    public static final String CAMERA_BATCH = "batchcamera";
    
    public static final String MIAN_GUIDE = "mainguide";
    
    public static final String MIAN_TAKE_GUIDE = "maintakeguide";
    
    public static final String DETAIL_GUIDE = "detailguide";
    
    public static final String TAKE_GUIDE = "takeguide";
    
    public static final String NEARBY_GUIDE = "nearbyguide";
    
    public static final String OWNER_COUNT = "ownerCount";
    
    public static final String NEXT_COUNT = "nextCount";
    
    public static final String CUR_PERCENT = "curPercent";
    
    public static final String NEXT_PERCENT = "nextPercent";
    
    public static final String WEIBO_NAME = "weiboName";
    
    public static final String CONTACT_LEAD_SKIP_TIME = "contact_lead_skip_time";
    
    public static final String SHOWED_GUIDE_IMPORT_CARD = "showed_guide_import_card";
    public static final String APP_FIRST_START_TIME = "app_first_start_time"; 
    
    
    public static final String CONTACT_ADD_FROM_CONTACT_USED = "add_from_contact_used";
    
    public static final String CHAT_MESSAGE_VERSION = "cmv";
    
    public static final String NEED_UPDATE_DATABASE_OPERATION = "NEED_UPDATE_DATABASE_OPERATION";
    
    
    public static final String MAIN_LIST_SORT_TYPE = "MAIN_LIST_SORT_TYPE";
    
    public static final String HIDE_BUBBLE_MSG_COUNT = "hide_bubble_msg_count";

    private static SharedPreferences pref;

    private static SharedPreferences.Editor editor;

    public static void initialize(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public static boolean contains(String key) {
        return pref.contains(key);
    }

    public static Map<String, ?> getAll() {
        return pref.getAll();
    }

    public static boolean get(String key, boolean defValue) {
        return pref.getBoolean(key, defValue);
    }

    public static float get(String key, float defValue) {
        return pref.getFloat(key, defValue);
    }

    public static int get(String key, int defValue) {
        return pref.getInt(key, defValue);
    }

    public static long get(String key, long defValue) {
        return pref.getLong(key, defValue);
    }

    public static String get(String key, String defValue) {
        return pref.getString(key, defValue);
    }

    public static boolean get(String userid, String key, boolean defValue) {
        return pref.getBoolean(StringUtils.concatWith(SEPARATOR, userid, key), defValue);
    }

    public static float get(String userid, String key, float defValue) {
        return pref.getFloat(StringUtils.concatWith(SEPARATOR, userid, key), defValue);
    }

    public static int get(String userid, String key, int defValue) {
        return pref.getInt(StringUtils.concatWith(SEPARATOR, userid, key), defValue);
    }

    public static long get(String userid, String key, long defValue) {
        return pref.getLong(StringUtils.concatWith(SEPARATOR, userid, key), defValue);
    }

    public static String get(String userid, String key, String defValue) {
        return pref.getString(StringUtils.concatWith(SEPARATOR, userid, key), defValue);
    }

    public static void put(String key, boolean value) {
        editor.putBoolean(key, value);
    }

    public static void put(String key, float value) {
        editor.putFloat(key, value);
    }

    public static void put(String key, int value) {
        editor.putInt(key, value);
    }

    public static void put(String key, long value) {
        editor.putLong(key, value);
    }

    public static void put(String key, String value) {
        editor.putString(key, value);
    }

    public static void put(String userid, String key, boolean value) {
        editor.putBoolean(StringUtils.concatWith(SEPARATOR, userid, key), value);
    }

    public static void put(String userid, String key, float value) {
        editor.putFloat(StringUtils.concatWith(SEPARATOR, userid, key), value);
    }

    public static void put(String userid, String key, int value) {
        editor.putInt(StringUtils.concatWith(SEPARATOR, userid, key), value);
    }

    public static void put(String userid, String key, long value) {
        editor.putLong(StringUtils.concatWith(SEPARATOR, userid, key), value);
    }

    public static void put(String userid, String key, String value) {
        editor.putString(StringUtils.concatWith(SEPARATOR, userid, key), value);
    }

    public static void commit() {
        editor.commit();
    }

    public static void clear() {
        editor.clear();
    }

    public static void remove(String key) {
        editor.remove(key);
    }
    
    public static void remove(String userid, String key) {
        editor.remove(StringUtils.concatWith(SEPARATOR, userid, key));
    }
}
