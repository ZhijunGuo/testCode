
package com.example.galaxy08.tool;

import java.util.UUID;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.example.galaxy08.app.PreferenceWrapper;

//import com.jingwei.card.R;
//import com.jingwei.card.app.PreferenceWrapper;
//import com.jingwei.card.finals.SysConstants;

/**
 * 设备信息封装类。依赖PreferenceWrapper， PreferenceWrapper需在之前初始化
 */
public class SystemInfo {
    private static final String TAG = SystemInfo.class.getSimpleName();

    private static final String UID_PREF = "uniqid_pref";

    public static final String PARAM_UNIQID = "uniqid";

    public static final String PARAM_MODEL = "model";

    public static final String PARAM_APPID = "appid";

    public static final String PARAM_MAC = "mac";

    public static final String PARAM_OS = "os";

    public static final String PARAM_SCREEN = "screen";

    public static final String PARAM_FROM = "from";

    public static final String PARAM_VERSION = "version";

    public static final String PARAM_VERSIONCODE = "versioncode";

    private String mModel;

    private String mAppid;

    private String mMac;

    private String mOs;

    private String mUniqid;

    private String mScreen;

    private int mFrom;

    private String mVersion;

    private String mVersionCode;

    private String mManufacturer;

    private Context mContext;

    public SystemInfo(Context context) {
        this.mContext = context;
    }

    public void init() {
        this.mModel = Tool.getMOBILE_MODEL();
        this.mOs = String.format("Android%s", Tool.getSYSTEM());
        this.mMac = readMac();
        this.mAppid = "1";
        this.mUniqid = readUniqid();
        this.mScreen = Tool.getWindowSize(mContext);
        
        //渠道？？
        this.mFrom = 142;//mContext.getString(R.string.from_id);
        this.mVersion = Tool.getAPPVersion(mContext);
        this.mVersionCode = Tool.getAPPVersionCode(mContext);
        this.mManufacturer = Tool.getManufacturer();
    }

    private String readMac() {
        return Tool.getLocalMacAddress(mContext);
    }

    /**
     * 获取应用的UID， 优先读设备的IEMI， 如果IMEI不存在,则试着取mac值，如果仍然为空则动态生成一个UID存入sharepreference
     * 
     * @param context
     * @return
     */
    private String readUniqid() {
        String uid = null;
        try {
            TelephonyManager telMgr = (TelephonyManager)mContext
                    .getSystemService(Context.TELEPHONY_SERVICE);
            uid = telMgr.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
            DebugLog.logw(TAG, "telephone model not exist");
        }

        if (!TextUtils.isEmpty(uid) && uid.indexOf("0000000") < 0) {
            DebugLog.logw(TAG, "read imei from telephone success : " + uid);
            return uid;
        }

        uid = Tool.getLocalMacAddress(mContext);
        if(!TextUtils.isEmpty(uid)){
            PreferenceWrapper.put(UID_PREF, uid);
            PreferenceWrapper.commit();
            DebugLog.logw(TAG, "read uniqid from mac success : " + uid);
            return uid;
        }
        
        uid = PreferenceWrapper.get(UID_PREF, "");
        if(TextUtils.isEmpty(uid)){
            uid = UUID.randomUUID().toString().toLowerCase().replaceAll("-", "");
            PreferenceWrapper.put(UID_PREF, uid);
            PreferenceWrapper.commit();
            DebugLog.logw(TAG, "read uniqid by new create : " + uid);
        }else{
            DebugLog.logw(TAG, "read uniqid from sharepreference : " + uid);
        }
        return uid;
    }

    public String getModel() {
        return mModel;
    }

    public String getAppid() {
        return mAppid;
    }

    /**
     * 返回mac，如果当前为空，会参数重新获取
     * 
     * @return
     */
    public String getMac() {
        if (TextUtils.isEmpty(mMac)) {
            mMac = Tool.getLocalMacAddress(mContext);
        }
        
        if(TextUtils.isEmpty(mMac)){
            mMac = readUniqid();
        }
        return mMac;
    }

    public String getOs() {
        return mOs;
    }

    public String getUniqid() {
        return mUniqid;
    }

    public String getScreen() {
        return mScreen;
    }

    public int getFrom() {
        return mFrom;
    }

    public String getVersion() {
        return mVersion;
    }

    public String getVersionCode() {
        return mVersionCode;
    }

    public String getManufacturer() {
        return mManufacturer;
    }

}
