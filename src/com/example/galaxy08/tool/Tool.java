package com.example.galaxy08.tool;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;

import com.example.galaxy08.SysApplication;



public class Tool {

    private Context context;
    //?
    private static String keyStr = "a92a32bcbae61c4f09da0eff0ff66acd6dba";
    
    public static String ILLEGAL_INPUT_PATTER = "[^.,@_*\"';%]*";

    public static String SQL_REPLACE_PATTER = "[*_\"%@]";
    
    public static int TITLE_HEIGHT = 50;

    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static SimpleDateFormat dateFormatTime = new SimpleDateFormat("HH:mm");

    // static SimpleDateFormat dateFormatDate = new
    // SimpleDateFormat("yyyy-MM-dd");
    //static SimpleDateFormat dateFormatDate = new SimpleDateFormat("yyyy年MM月dd日");

    static SimpleDateFormat dateFormatWeek = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 获得全球唯一码UUID
     */
    public static String getUUID() {

        UUID uuid = UUID.randomUUID();

        String str = uuid.toString();

        String temp = str.substring(0, 7) + str.substring(9, 13) + str.substring(14, 18) + str.substring(19, 23)
                + str.substring(24);

        return temp;
    }

    // 时间戳
    public static String nowTime() {
        return System.currentTimeMillis() + "";
    }

    // 时间
    public static String time() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(new Date().getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(c.getTime());
    }

    /**
     * 验证手机号是否有效是有效号码
     * 
     * @param mobile
     * @return
     */
    public static boolean validateMoblie(String mobile) {
    	return !TextUtils.isEmpty(mobile ) && TextUtils.isDigitsOnly(mobile) && mobile.length() >=8 && mobile.length() <=11;
//        Pattern pattern = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
//        Matcher m = pattern.matcher(mobile);
//        return m.matches();
    }
    
    /**
     * 验证中国大陆手机号： 以1开头的11位的数字
     * @param mobile
     * @return
     */
    private static boolean validateChinaMoblie(String mobile) {
        Pattern pattern = Pattern.compile("1[0-9]{10}");
        Matcher m = pattern.matcher(mobile);
        return m.matches();
    }
    
    public static boolean validateUSMobile(String mobile){
    	Pattern pattern = Pattern.compile ("^[2-9]\\d{2}[2-9]\\d{2}\\d{4}$");
  	    Matcher m = pattern.matcher(mobile);
  	    return m.matches();
    }
    
    /**
     * 根据国家码来判断手机号是否符合规则。
     * 香港手机号：8位数字 
		澳门手机号：8位数字 
		台湾手机号：9或10位数字 
		大陆手机号：1开头的11位数字 
     * @param mobile 手机号码
     * @param city  国家码
     * @return
     */
    public static boolean validateMoblie4City(String mobile, String city) {
    	Integer intCity = cityString2int(city);
    	boolean result;
    	if (null != intCity){
    		
    		switch (intCity) {
    		case 86:
    			result = validateChinaMoblie(mobile);
    			break;
    			
    		case 852:
    			result = TextUtils.isDigitsOnly(mobile) && mobile.length() == 8 ;
    			break;
    			
    		case 853:
    			result = TextUtils.isDigitsOnly(mobile) && mobile.length() == 8 ;
    			break;
    			
    		case 886:
    			result = TextUtils.isDigitsOnly(mobile) && mobile.length() >8 && mobile.length() <=10;
    			break;
    			
    		default:
    			result = validateMoblie(mobile);
    			break;
    		}
    	}else{
    		result = validateMoblie(mobile);
		}
		return result;
    }
    
    /**
     * 将字符形式的国家码转换成数字形式。若果格式不国家码不是String类型的数字。返回null
     * @param city
     * @return
     */
	private static Integer cityString2int(String city) {
		//将国家码转为数字
    	city = city.trim();
    	city = city.substring(1, city.length());
    	Integer intCity;
		try {
			intCity = Integer.parseInt(city);
		} catch (Exception e) {
			intCity = null;
		}
		return intCity;
	}

	/**
	 * 判断邮箱格式是否正确
	 * \\-\\—\\– ：这三个横线都不一样，虽然第一个和第三个显示上看起来差不多
	 * @param data
	 * @return
	 */
    public static boolean validateEmail(String data) {
    	if(TextUtils.isEmpty(data)){
    		return false;
    	}
//        Pattern pattern = Pattern
//                .compile("^([a-z0-9A-Z]+[-_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
        Pattern pattern = Pattern
                .compile("^([a-zA-Z0-9_\\-\\—\\–\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-\\—\\–]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher matcher = pattern.matcher(data);
        boolean b = matcher.matches();

        return b;
    }

    public static boolean isMediaMounted() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static String getToday() {
        Date date = new Date();
        long timenow = date.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

        String today = dateFormat.format(new Date(timenow));

        DebugLog.logd("today=" + today);
        return today;
    }

    public static String formatTime(String timeMillis) {
        String fromatDate = null;
        Pattern pattern = Pattern.compile("[0-9]*");
        // 与数据库老数据兼容，判断是否已格式化过
        if (pattern.matcher(timeMillis).matches()) {
            Date date = new Date(Long.parseLong(timeMillis));
            fromatDate = dateFormat.format(date);
        }
        else {
            fromatDate = timeMillis;
        }
        return fromatDate;
    }

    /*
     * 将日期格式为"YYYY-MM-dd"的字符串转换为毫秒数，如果格式不符原样返回
     */
    public static String parseDate(String date) {
        String timeMillis = null;
        try {
            timeMillis = String.valueOf(dateFormat.parse(date).getTime());
        } catch (ParseException e) {
            timeMillis = date;
        }
        return timeMillis;
    }

    public static String parseDatelongToString(long timeMillis) {
        Date data = new Date(timeMillis);
        return dateFormat.format(data);
    }

    public static String parseDateTime(long timeMillis) {
        Date data = new Date(timeMillis);
        return dateFormatTime.format(data);
    }

//    public static String parseDateWeek(Context context, long timeMillis) throws ParseException {
//        Date data = new Date(timeMillis);
//        String date = dateFormatWeek.format(data);
//
//        String dayNames[] = { context.getString(R.string.Sunday), 
//        					  context.getString(R.string.Monday),
//        					  context.getString(R.string.Tuesday),
//        					  context.getString(R.string.Wednesday),
//        					  context.getString(R.string.Thursday),
//        					  context.getString(R.string.Friday),
//        					  context.getString(R.string.Saturday)};
//        Date d = dateFormatWeek.parse(date);
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(d);
//        int day = cal.get(Calendar.DAY_OF_WEEK);
//        return dayNames[day - 1];
//    }
    
    public static boolean todayIsFestival(){
    	Date begin, end;
    	long nowTime = System.currentTimeMillis();
		try {
			begin = dateFormatWeek.parse("2012-09-20");
			end = dateFormatWeek.parse("2012-10-08");
			return nowTime >= begin.getTime() && nowTime <= end.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    	    	
    	return false;
    }
    public static String getPath(Uri uri,Context context)  

    {  
         try
         {
       	  String[] projection = {MediaStore.Images.Media.DATA };  

             Cursor cursor = ((Activity) context).managedQuery(uri, projection, null, null, null);  

           int column_index = cursor  

                 .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);  

             cursor.moveToFirst();  

            return cursor.getString(column_index);  
         }
           
        catch(Exception e)
        {
       	 return  null;
        }
    }  
    public static boolean afterOct8(){
    	Date end;
    	long nowTime = System.currentTimeMillis();
		try {
			end = dateFormatWeek.parse("2012-10-08");
			return nowTime > end.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    	    	
    	return false;
    }

//    public static String parseDateDate(long timeMillis) {
//        Date data = new Date(timeMillis);
//        return dateFormatDate.format(data);
//    }

    public static String getCurrentTime() {
        Date curDate = new Date(System.currentTimeMillis());
        return dateFormat.format(curDate);
    }
/*
    public static String getMm(Context context, long mTime) {
        Date data = new Date(mTime);
        int hour = data.getHours();
        if (android.text.format.DateFormat.is24HourFormat(context)) {
            if (hour >= 1 && hour <= 5)
                return R.string.dawn+"";
            else if (hour >= 6 && hour <= 8)
                return R.string.morning+"";
            else if (hour >= 9 && hour <= 11)
                return R.string.am+"";
            else if (hour >= 12 && hour <= 18)
                return R.string.pm+"";
            else if (hour >= 19 && hour <= 23 || hour == 0)
                return R.string.night+"";
            else
                return "";
        }
        else {
            Calendar c = Calendar.getInstance();
            if (c.get(Calendar.AM_PM) == 0) {
                return R.string.am+"";
            }
            else {
                return R.string.pm+"";
            }
        }
    }
*/
    public static String getCardTime() {
        Date date = new Date();
        long timenow = date.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String today = dateFormat.format(new Date(timenow));

        DebugLog.logd("today=" + today);
        return today;
    }

//    public static String TimeStampDate(String messtime,Boolean returndate) {
//        Context context = JwApplication.mContext;
////        String dawn = context.getString(R.string.dawn);
////        String am = context.getString(R.string.am);
////        String nooning = context.getString(R.string.nooning);
////        String pm = context.getString(R.string.pm);
////        String night = context.getString(R.string.night);
//        String today = context.getString(R.string.today);
//        String yestoday = context.getString(R.string.yestoday);
//        if (TextUtils.isEmpty(messtime)) {
//            messtime = "0";
//        }
//        Date date = new Date();
//        long timenow = date.getTime();
//        String showdate = "";
//        try {
//            Long timemess = Long.parseLong(messtime);
//            String year = new java.text.SimpleDateFormat("yyyy").format(new java.util.Date(timemess));
//            String time = new java.text.SimpleDateFormat("HH:mm").format(new java.util.Date(timemess));
//            String timeArea = new java.text.SimpleDateFormat("HH").format(new java.util.Date(timemess));
//            String yearnow = new java.text.SimpleDateFormat("yyyy").format(new java.util.Date(timenow));
//            String yearmess = new java.text.SimpleDateFormat("yyyy").format(new java.util.Date(timemess));
//            String monthnow = new java.text.SimpleDateFormat("MM").format(new java.util.Date(timenow));
//            String monthmess = new java.text.SimpleDateFormat("MM").format(new java.util.Date(timemess));
//            String daynow = new java.text.SimpleDateFormat("dd").format(new java.util.Date(timenow));
//            String daymess = new java.text.SimpleDateFormat("dd").format(new java.util.Date(timemess));
//            String week = new java.text.SimpleDateFormat("E").format(new java.util.Date(timemess));
//            int nowyear = Integer.parseInt(yearnow);
//            int messyear = Integer.parseInt(yearmess);
//            int nowmonth = Integer.parseInt(monthnow);
//            int messmonth = Integer.parseInt(monthmess);
//            int nowday = Integer.parseInt(daynow);
//            int messday = Integer.parseInt(daymess);
//            int timePart = Integer.parseInt(timeArea);
//            if(returndate){
////                    if (timePart >= 0&&timePart<6) {
////                        showdate = year + "-" + messmonth + "-" + messday + "  " +  dawn +" "+time;
////                    }else if(timePart>=6&&timePart<12){
////                        showdate = year + "-" + messmonth + "-" + messday + "  " + am+" "+time;
////                    }else if(timePart>=12&&timePart<13){
////                    	showdate = year + "-" + messmonth + "-" + messday + "  " + nooning+" "+time;
////                    }else if(timePart>=13&&timePart<18){
////                    	showdate = year + "-" + messmonth + "-" + messday + "  " + pm+" "+time;
////                    }else if(timePart>=18&&timePart<24){
////                    	showdate = year + "-" + messmonth + "-" + messday + "  " + night+" "+time;
////                    }
//            	showdate = year + "-" + messmonth + "-" + messday +" "+time;
//
//            }else{
//            	if (nowyear != messyear) {
////                  if (timePart >= 0&&timePart<6) {
////                     showdate = year + "-" + messmonth + "-" + messday + "  " +  dawn +" "+time;
////                  }else if(timePart>=6&&timePart<12){
////                     showdate = year + "-" + messmonth + "-" + messday + "  " + am+" "+time;
////                  }else if(timePart>=12&&timePart<13){
////              	   showdate = year + "-" + messmonth + "-" + messday + "  " + nooning+" "+time;
////                  }else if(timePart>=13&&timePart<18){
////              	   showdate = year + "-" + messmonth + "-" + messday + "  " + pm+" "+time;
////                  }else if(timePart>=18&&timePart<24){
////              	   showdate = year + "-" + messmonth + "-" + messday + "  " + night+" "+time;
////                  }
//            		showdate = year + "-" + messmonth + "-" + messday +" "+time;
//                }
//                else if ((nowmonth == messmonth) && (nowday == messday)) { 
////                	if (timePart >= 0&&timePart<6) {
////                        showdate = dawn +" "+time;
////                    }else if(timePart>=6&&timePart<12){
////                        showdate = am+" "+time;
////                    }else if(timePart>=12&&timePart<13){
////                    	showdate = nooning+" "+time;
////                    }else if(timePart>=13&&timePart<18){
////                    	showdate = pm+" "+time;
////                    }else if(timePart>=18&&timePart<24){
////                    	showdate = night+" "+time;
////                    }
//            		showdate = today +" "+ time;            	 
//                }
//                else if ((nowmonth == messmonth) && (nowday - messday == 1)) {
////                	if (timePart >= 0&&timePart<6) {
////                        showdate = yestoday + " " + dawn +" "+time;
////                    }else if(timePart>=6&&timePart<12){
////                        showdate = yestoday + " " + am+" "+time;
////                    }else if(timePart>=12&&timePart<13){
////                    	showdate = yestoday + " " + nooning+" "+time;
////                    }else if(timePart>=13&&timePart<18){
////                    	showdate = yestoday + " " + pm+" "+time;
////                    }else if(timePart>=18&&timePart<24){
////                    	showdate = yestoday + " " + night+" "+time;
////                    }
//                	showdate = yestoday + " " + time;
//                }
//                else if ((nowmonth == messmonth) && (nowday - messday >= 2) && (nowday - messday <= 5)) {
////                	if (timePart >= 0&&timePart<6) {
////                        showdate = week + " " + dawn +" "+time;
////                    }else if(timePart>=6&&timePart<12){
////                        showdate = week + " " + am+" "+time;
////                    }else if(timePart>=12&&timePart<13){
////                    	showdate = week + " " + nooning+" "+time;
////                    }else if(timePart>=13&&timePart<18){
////                    	showdate = week + " " + pm+" "+time;
////                    }else if(timePart>=18&&timePart<24){
////                    	showdate = week + " " + night+" "+time;
////                    }
//                	showdate = week +" "+time;
//                }
//                else {
////                	if (timePart >= 0&&timePart<6) {
////                        showdate = year + "-" + messmonth + "-" + messday + "  " +  dawn +" "+time;
////                    }else if(timePart>=6&&timePart<12){
////                        showdate = year + "-" + messmonth + "-" + messday + "  " + am+" "+time;
////                    }else if(timePart>=12&&timePart<13){
////                    	showdate = year + "-" + messmonth + "-" + messday + "  " + nooning+" "+time;
////                    }else if(timePart>=13&&timePart<18){
////                    	showdate = year + "-" + messmonth + "-" + messday + "  " + pm+" "+time;
////                    }else if(timePart>=18&&timePart<24){
////                    	showdate = year + "-" + messmonth + "-" + messday + "  " + night+" "+time;
////                    }
//                	showdate = year + "-" + messmonth + "-" + messday +" "+time;
//                }
//            }
//            
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        return showdate;
//    }
    
    public static String TimeGetDay(String messtime ) {
        Context context = SysApplication.application.getApplicationContext();
        
        if (TextUtils.isEmpty(messtime)) {
            messtime = "0";
        }
        Date date = new Date();
        long timenow = date.getTime();
        String showdate = "";
        try {
            Long timemess = Long.parseLong(messtime);
            String year = new java.text.SimpleDateFormat("yyyy").format(new java.util.Date(timemess));
            String time = new java.text.SimpleDateFormat("HH:mm").format(new java.util.Date(timemess));
            String timeArea = new java.text.SimpleDateFormat("HH").format(new java.util.Date(timemess));
            String yearnow = new java.text.SimpleDateFormat("yyyy").format(new java.util.Date(timenow));
            String yearmess = new java.text.SimpleDateFormat("yyyy").format(new java.util.Date(timemess));
            String monthnow = new java.text.SimpleDateFormat("MM").format(new java.util.Date(timenow));
            String monthmess = new java.text.SimpleDateFormat("MM").format(new java.util.Date(timemess));
            String daynow = new java.text.SimpleDateFormat("dd").format(new java.util.Date(timenow));
            String daymess = new java.text.SimpleDateFormat("dd").format(new java.util.Date(timemess));
            String week = new java.text.SimpleDateFormat("E").format(new java.util.Date(timemess));
            int nowyear = Integer.parseInt(yearnow);
            int messyear = Integer.parseInt(yearmess);
            int nowmonth = Integer.parseInt(monthnow);
            int messmonth = Integer.parseInt(monthmess);
            int nowday = Integer.parseInt(daynow);
            int messday = Integer.parseInt(daymess);
            showdate = year + "-" + messmonth + "-" + messday;
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return showdate;
    }

    /**
     * 汉字转换位汉语拼音，英文字符不变
     * 
     * @param chines
     *            汉字
     * @return 拼音
     */
//    public static String converterToSpell(String chines) {
//        if (TextUtils.isEmpty(chines)) {
//            return null;
//        }
//        String pinyinName = "";
//        char[] nameChar = chines.toCharArray();
//        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
//        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
//        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
//        // defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
//        StringBuffer retuBuf = new StringBuffer();
//        StringBuffer breBuf = new StringBuffer();
//        for (int i = 0; i < nameChar.length; i++) {
//            // if (nameChar[i] > 128) {
//            if (isHanzi("" + nameChar[i])) {
//                try {
//                    retuBuf.append(",");
//                    pinyinName += PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0];
//                    retuBuf.append(pinyinName);
//                    breBuf.append(pinyinName.substring(0, 1));
//                    retuBuf.append(",");
//                    retuBuf.append(breBuf);
//                    // return retuBuf.toString();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            else {
//                pinyinName += nameChar[i];
//            }
//        }
//        return pinyinName.toString();
//    }

    private static boolean isHanzi(CharSequence input) {
        String regEx = "[\\u4e00-\\u9fa5]";
        Pattern pattern = Pattern.compile(regEx);
        Matcher m = pattern.matcher(input);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /** 判断网络连接是否存在 */
    public static boolean hasInternet(Context activity) {

        ConnectivityManager manager = (ConnectivityManager) activity

        .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = manager.getActiveNetworkInfo();

        if (info == null || !info.isConnected()) {
            return false;
        }
        if (info.isRoaming()) {
            return true;
        }
        return true;
    }

    // 隐藏软键盘
    public static void hideSoftKeyBoard(Activity activity) {
    	if(activity != null && activity.getCurrentFocus() != null){
    		((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(activity
    				.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    	}

    }

    public static String getKey() {
        return "a92a32bcbae61c4f09da0eff0ff66acd6dba";
    }

    public static Bitmap getBitmapFromFile(String filePath, int inSampleSize) {

        if (filePath == null) {
            return null;
        }

        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        file = null;

        Bitmap bitmap = null;

        try {
            FileInputStream fis = new FileInputStream(filePath);

            byte[] bytes = new byte[fis.available()];
            fis.read(bytes);

            if (inSampleSize > 0) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = inSampleSize;
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
            }
            else {
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            }

            bytes = null;
        } catch (OutOfMemoryError eo) {
            eo.printStackTrace();
        } catch (FileNotFoundException ef) {
            ef.printStackTrace();
        } catch (IOException ei) {
            ei.printStackTrace();
        }

        return bitmap;
    }

    /**
     * 根据设定的分辨率对图片进行压缩处理
     * 
     * @param maxWidth
     *            图片的宽度
     * @param maxHeight
     *            图片的高度
     * @return
     */
    public static Bitmap optimizeBitmap(byte[] resource, int maxWidth, int maxHeight) {

        if (resource == null || maxWidth <= 0 || maxHeight <= 0) {
            DebugLog.loge("Tool optimizeBitmap return: resource = " + resource + " maxWidth = " + maxWidth
                    + " maxHeight = " + maxHeight);
            return null;
        }
        Bitmap result = null;
        int length = resource.length;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        result = BitmapFactory.decodeByteArray(resource, 0, length, options);
        int widthRatio = (int) Math.ceil(options.outWidth / maxWidth);
        int heightRatio = (int) Math.ceil(options.outHeight / maxHeight);
        if (widthRatio > 1 || heightRatio > 1) {
            if (widthRatio > heightRatio) {
                options.inSampleSize = widthRatio;
            }
            else {
                options.inSampleSize = heightRatio;
            }
        }

        options.inJustDecodeBounds = false;
        try {
            result = BitmapFactory.decodeByteArray(resource, 0, length, options);
        } catch (OutOfMemoryError e) {
            System.gc();
            e.printStackTrace();
            DebugLog.loge("error=" + e.toString());
            options.inSampleSize *= 4;
            try {
                result = BitmapFactory.decodeByteArray(resource, 0, length, options);
            } catch (OutOfMemoryError e1) {
                e.printStackTrace();
                DebugLog.loge("error1 =" + e1.toString());
                System.gc();
            }
        }
        return result;
    }
    /**
     * 根据设定的分辨率对图片进行压缩处理
     * 
     * @param maxWidth
     *            图片的宽度
     * @param maxHeight
     *            图片的高度
     * @return
     */
    public static Bitmap optimizeBitmapByFile(File file, int maxWidth, int maxHeight) {

    	boolean isRotate = false;
        if (file == null || maxWidth <= 0 || maxHeight <= 0) {
//            DebugLog.loge("Tool optimizeBitmap return: resource = " + resource + " maxWidth = " + maxWidth
//                    + " maxHeight = " + maxHeight);
            return null;
        }
        Bitmap result = null;
        //int length = resource.length;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        result = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        int widthRatio = (int) Math.ceil(options.outWidth / maxWidth);
        int heightRatio = (int) Math.ceil(options.outHeight / maxHeight);
        if (widthRatio > 1 || heightRatio > 1) {
            if (widthRatio > heightRatio) {
                options.inSampleSize = widthRatio;
            }
            else {
            	isRotate = true;
                options.inSampleSize = heightRatio;
            }
        }

        options.inJustDecodeBounds = false;
        try {
            result = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        } catch (OutOfMemoryError e) {
            System.gc();
            e.printStackTrace();
            DebugLog.loge("error=" + e.toString());
            options.inSampleSize *= 3;
            try {
                result = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            } catch (OutOfMemoryError e1) {
                e.printStackTrace();
                DebugLog.loge("error1 =" + e1.toString());
                System.gc();
            }
        }
        try
        {
        	if(isRotate)
            {
            	result = rotateBitmap(result,-90);
            }
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
        
        return result;
    }
    public static void getMergedFile(String fileTop, String fileBottom, String outFile, int quality, int size) {
        Bitmap outBitmap = null;

        BitmapFactory.Options opts = new BitmapFactory.Options();
        if (size > 0) {
            opts.inSampleSize = size;
        }

        Bitmap bitmapTop = BitmapFactory.decodeFile(fileTop, opts);
        Bitmap bitmapBottom = BitmapFactory.decodeFile(fileBottom, opts);
        outBitmap = getMergedBitmap(bitmapTop, bitmapBottom);

        /*
         * // for mengtian test begin // ?it can not work, return -1003 String
         * mengtian_name = outFile.replace(".jpg", ".tiff"); File mtFile = new
         * File(mengtian_name); mtFile.delete(); byte[] bytes =
         * Bitmap2Bytes(outBitmap); int getResizeRatio =
         * BCRCloudAPI.getResizeRatio(bytes); int rtnCode =
         * JNISDK_TIFF.Binary2TiffByBuffer(bytes, bytes.length, getResizeRatio,
         * mtFile.getAbsolutePath().getBytes()); DebugLog.loge("test",
         * "mengtian_name=" + mengtian_name); DebugLog.loge("test", "rtnCode=" +
         * rtnCode); // for mengtian end
         */

        File file = new File(outFile);
        try {
            FileOutputStream outStream = new FileOutputStream(file);
            outBitmap.compress(CompressFormat.JPEG, quality, outStream);
            outStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outBitmap != null) {
                outBitmap.recycle();
                outBitmap = null;
            }
        }
    }

    /**
     * file1和file2放到file命名的文件夹里面
     * 
     * @param file1
     * @param file2
     */
    public static String getMergedFileFolder(String file1) {
        DebugLog.logd("test", "file1=" + file1);
        String folderPath = file1.substring(0, file1.lastIndexOf(".jpg"));

        DebugLog.logd("test", "folderPath=" + folderPath);
        File file = new File(folderPath);
        if (file.exists()) {
            return file.getAbsolutePath();
        }
        boolean bMakeDir = file.mkdirs();
        DebugLog.logd("test", "bMakeDir=" + bMakeDir);
        if (bMakeDir) {
            return file.getAbsolutePath();
        }
        else {
            return null;
        }
    }

    public static String moveFile(String srcFile, String destFolder) {
        DebugLog.logd("test", "srcFile=" + srcFile);
        DebugLog.logd("test", "destFolder=" + destFolder);
        File file = new File(srcFile);
        if (!file.exists()) {
            return null;
        }
        File folder = new File(destFolder);
        if (!folder.exists()) {
            return null;
        }
        String fileName = srcFile.substring(srcFile.lastIndexOf(File.separator));
        File renamedFile = new File(destFolder + File.separator + fileName);
        file.renameTo(renamedFile);

        String outFilePath = renamedFile.getAbsolutePath();
        DebugLog.logd("test", "outFilePath=" + outFilePath);
        return outFilePath;
    }
    public static String renameFile(String srcFile, String destFile) {
        DebugLog.logd("test", "srcFile=" + srcFile);
        DebugLog.logd("test", "destFile=" + destFile);
        try
        {
        	File file = new File(srcFile);
            if (!file.exists()) {
                return null;
            }
            File renamedFile = new File(destFile);
            file.renameTo(renamedFile);
            String outFilePath = renamedFile.getAbsolutePath();
            DebugLog.logd("test", "outFilePath=" + outFilePath);
            return outFilePath;
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	return srcFile;

        }
        
    }

    private static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    public static void getMergedFile(String fileTop, String fileBottom, String outFile, int quality) {
        getMergedFile(fileTop, fileBottom, outFile, 100, 0);
    }

    /*
     * fileTop : The file path of the top edge of merged bitmap fileBottom : The
     * file path of the bottom edge of merged bitmap outFile : The file path of
     * the merged file
     * 
     * remark: no need to delete fileTop and fileBottom file
     */
    public static void getMergedFile(String fileTop, String fileBottom, String outFile) {
        getMergedFile(fileTop, fileBottom, outFile, 100);
    }

    public static Bitmap getMergedBitmap(Bitmap bitmapTop, Bitmap bitmapBottom) {
        Bitmap outBitmap = null;

        final int width = bitmapTop.getWidth();
        final int height = bitmapTop.getHeight();
        outBitmap = Bitmap.createBitmap(width, height * 2, Bitmap.Config.RGB_565);

        // Merge picture
        Canvas canvas = new Canvas(outBitmap);
        canvas.drawBitmap(bitmapTop, 0, 0, null);
        canvas.drawBitmap(bitmapBottom, 0, height, null);

        bitmapTop.recycle();
        bitmapTop = null;
        bitmapBottom.recycle();
        bitmapBottom = null;

        return outBitmap;
    }

    /**
     * 旋转图像至width > height
     * 
     * @param bitmap
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap bitmap) {
        if (bitmap != null && bitmap.getWidth() < bitmap.getHeight()) {
            final Matrix matrix = new Matrix();
            matrix.setRotate(90);
            try {
                Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                if (bitmap != bitmap2) {
                    bitmap.recycle();
                    bitmap = bitmap2;
                }
            } catch (OutOfMemoryError ex) {
                System.gc();
                ex.printStackTrace();
                DebugLog.loge("error =" + ex.toString());
            }
        }
        return bitmap;
    }

    /**
     * 旋转度数
     * 
     * @param bitmap
     * @param ratate
     *            度数
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int ratate) {
        final Matrix matrix = new Matrix();
        matrix.setRotate(ratate);
        try {
            Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            if (bitmap != bitmap2) {
                bitmap.recycle();
                bitmap = bitmap2;
            }
        } catch (OutOfMemoryError ex) {
            ex.printStackTrace();
            DebugLog.loge("error =" + ex.toString());
            System.gc();
        }
        return bitmap;
    }

    public static File rotateBitmap(File file, int ratate) {
        if (file == null) {
            return null;
        }
        FileInputStream fis = null;
        Bitmap result = null;
        try {
            fis = new FileInputStream(file.getAbsolutePath());
            byte[] resource = new byte[fis.available()];
            fis.read(resource);

            int length = resource.length;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            result = BitmapFactory.decodeByteArray(resource, 0, length, options);
            // result == null
            if (options.outHeight > options.outWidth) {
                options.inJustDecodeBounds = false;
                try {
                    result = BitmapFactory.decodeByteArray(resource, 0, length, options);
                } catch (OutOfMemoryError e) {
                    System.gc();
                    e.printStackTrace();
                    DebugLog.loge("error=" + e.toString());
                    options.inSampleSize *= 3;
                    try {
                        result = BitmapFactory.decodeByteArray(resource, 0, length, options);
                    } catch (OutOfMemoryError e1) {
                        e.printStackTrace();
                        DebugLog.loge("error1 =" + e1.toString());
                        System.gc();
                    }
                }
                final Matrix matrix = new Matrix();
                matrix.setRotate(ratate);
                try {
                    Bitmap bitmap2 = Bitmap.createBitmap(result, 0, 0, result.getWidth(), result.getHeight(), matrix,
                            true);
                    if (result != bitmap2) {
                        result.recycle();
                        result = bitmap2;
                    }
                } catch (OutOfMemoryError ex) {
                    System.gc();
                    ex.printStackTrace();
                    DebugLog.loge("error =" + ex.toString());
                }
                if (result != null) {
                    file.delete();
                    FileOutputStream out = new FileOutputStream(file.getAbsolutePath());
                    result.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.close();
                    out = null;
                    result.recycle();
                    result = null;
                }
            }
            else if (result != null) {
                result.recycle();
                result = null;
            }
        } catch (Exception e) {
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fis = null;
            }
            if (result != null) {
                result.recycle();
                result = null;
            }
        }
        return file;
    }

    /**
     * 获取版本号
     */
    public static String getAPPVersion(Context context) {
        String ver = "1.0";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo info = packageManager.getPackageInfo(context.getPackageName(), 0);

            if (info.versionName != null) {
                ver = info.versionName;
            }
            else if (info.versionCode > 0) {
                ver = String.valueOf(info.versionCode);
            }
        } catch (Exception e) {
        }

        return ver;
    }

    /**
     * 获取版本号
     */
    public static String getAPPVersionCode(Context context) {
        String ver = "1.0";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo info = packageManager.getPackageInfo(context.getPackageName(), 0);

            if (info.versionCode > 0) {
            	ver = String.valueOf(info.versionCode);
            }
            else if (info.versionName != null) {
            	ver = info.versionName;
            }
        } catch (Exception e) {
        }

        return ver;
    }

    /**
     * 获取手机型号
     * 
     * @return
     */
    public static String getMOBILE_MODEL() {
        return android.os.Build.MODEL == null ? "-1" : android.os.Build.MODEL;
    }

    /**
     * 获取设备系统版本
     */
    public static String getSYSTEM() {
        return android.os.Build.VERSION.RELEASE == null ? "-1" : android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取厂商
     * 
     * @return
     */
    public static String getManufacturer() {
        return android.os.Build.MANUFACTURER == null ? "-1" : android.os.Build.MANUFACTURER;
    }

    /**
     * 获取屏幕尺寸
     */
    public static String getWindowSize(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();

        float density = dm.density; // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
        int densityDPI = dm.densityDpi; // 屏幕密度（每寸像素：120/160/240/320）
        float xdpi = dm.xdpi;
        float ydpi = dm.ydpi;

        int screenWidth = dm.widthPixels; // 屏幕宽（像素，如：480px）
        int screenHeight = dm.heightPixels; // 屏幕高（像素，如：800px）

        return screenWidth + "," + screenHeight;
    }

    /**
     * 获取MAC地址
     * 
     * @return
     */
    public static String getLocalMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        if (TextUtils.isEmpty(info.getMacAddress())) {
            return "";
        }
        else {
            return info.getMacAddress().replaceAll(":", "");
        }
    }

    public void finish(Context context) {
        ((Activity) context).finish();
    }

    /**
     * 根据传入的分隔符参数将字符串分割并传入数组;
     * 
     * @param regex
     *            :分隔符;
     * @param string
     *            :字符串;
     * @return
     */
    public static String[] split(String regex, String string) {
        if (regex == null || string == null) {
            return null;
        }

        Vector<String> vector = new Vector<String>();
        int index = string.indexOf(regex);

        if (index == -1) {
            vector.addElement(string);
        }
        else {
            while (index != -1) {
                vector.addElement(string.substring(0, index));
                string = string.substring(index + 1, string.length());
                index = string.indexOf(regex);
            }

            if (index != string.length() - 1) {
                vector.addElement(string);
            }
        }

        final String[] array = new String[vector.size()];
        vector.copyInto(array);
        vector = null;

        return array;
    }
    
    /**
     * 字符串按字节截取
     * 
     * @param str
     *            原字符
     * @param len
     *            截取长度
     * @param elide
     *            省略符等追加到截取完的后边
     * @return String
     */
    public static String splitString(String str, int len, String elide) {
        if (str == null) {
            return "";
        }
        byte[] strByte = null;
        try {
            strByte = str.getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int strLen = strByte.length;
        // int elideLen = (elide.trim().length() == 0) ? 0 :
        // elide.getBytes().length;
        if (len >= strLen || len < 1) {
            return str;
        }
        // if (len - elideLen > 0) {
        // len = len - elideLen;
        // }
        int count = 0;
        for (int i = 0; i < len; i++) {
            int value = (int) strByte[i];
            if (value < 0) {
                count++;
            }
        }
        if (count % 2 != 0) {
            len = (len == 1) ? len + 1 : len - 1;
        }
        String aString = "";
        try {
            aString = new String(strByte, 0, len, "GBK") + elide.trim();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return aString;
    }

    public void getFile(final Context context, final String strPath) {
        try {
            Runnable r = new Runnable() {

                @Override
                public void run() {
                    try {
                        getDataSource(context, strPath);
                    } catch (Exception e) {
                    }
                }
            };
            new Thread(r).start();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void getDataSource(Context context, String appPath) throws Exception {
        // if (!URLUtil.isNetworkUrl(appPath)) {
        // Error URL
        // System.out.println("连接错误");
        // ToastUtil.makeText(context, context.getString(R.string.neturlerror),
        // 0).show();
        // } else {

        final String fileName = "galaxyCard.apk";
        File tmpFile = new File("/sdcard/galaxy");

        if (!tmpFile.exists()) {
            tmpFile.mkdir();
        }
        URL myURL = new URL(appPath);
        URLConnection conn = myURL.openConnection();
        conn.connect();
        InputStream is = conn.getInputStream();

        if (is == null) {
            throw new RuntimeException("Stream is null");
        }
        // create temp Filexz
        File myTempFile = new File("/sdcard/galaxy/" + fileName);
        // get the path of the tem file
        // m_currentTempFilePath = myTempFile.getAbsolutePath();
        // write the file to temp file
        FileOutputStream fos = new FileOutputStream(myTempFile);
        byte buf[] = new byte[128];
        do {
            int numread = is.read(buf);
            if (numread <= 0) {
                break;
            }
            fos.write(buf, 0, numread);

        } while (true);
        // open app file
        openFile(context, myTempFile);
        try {
            is.close();
        } catch (Exception ex) {
        }

        // }
    }

    /* get the MimeType of app */
    public static String getMIMEType(File f) {
        String type = "";
        String fName = f.getName();
        String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();

        if (end.equals("apk")) {
            type = "application/vnd.android.package-archive";
        }
        else {
            type = "*/*";
        }

        return type;
    }

    // open file on mobile device
    public static void openFile(Context context, File f) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        /* get the MimeType */
        String type = getMIMEType(f);
        intent.setDataAndType(Uri.fromFile(f), type);
        context.startActivity(intent);
    }

    /* delete file */
    public static void delFile(String fileName) {
    	if(fileName != null)
    	{
    		try
    		{
    			File myFile = new File(fileName);
                if (myFile.exists()) {
                    myFile.delete();
                }
    		}
    		catch(Exception e)
    		{
    			e.printStackTrace();
    		}
    	}   
    }

    public static void dismissPopWindow(Activity activity, PopupWindow popWindow) {
    	if (popWindow != null && ! popWindow.isShowing() && !activity.isFinishing())
    		popWindow.dismiss();
    	    popWindow = null;
    }
    public static void dismissDialog(Activity activity, Dialog dialog) {
        if (dialog != null && dialog.isShowing() && !activity.isFinishing())
            dialog.dismiss();
    }

    public static void showDialog(Activity activity, Dialog dialog) {
        if (dialog != null && !dialog.isShowing() && !activity.isFinishing())
            dialog.show();
    }

    public static boolean containsNotEnglish(String inStr) {
        byte[] bytes = inStr.getBytes();

        if (bytes.length > inStr.length()) {
            return true;
        }
        return false;
    }

    public synchronized static String combineStrings(String... args) {
        if (args == null || args.length == 0)
            return "";
        StringBuffer sb = new StringBuffer(args.length);
        for (String arg : args) {
            sb.append(arg);
        }
        return sb.toString();
    }

    public static boolean saveToLocal(InputStream is, File file) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = is instanceof BufferedInputStream?(BufferedInputStream)is: new BufferedInputStream(is);
            bos = new BufferedOutputStream(new FileOutputStream(file));

            byte[] buffer = new byte[8192];
            int length = 0;
            while ((length = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, length);
            }
            bos.flush();
            return true;
        } catch (Exception e) {
            DebugLog.logd("SaveToLocal", "save error:" + file.getAbsolutePath(), e);
            return false;
        } finally {
            if (bis != null)
                try {
                    bis.close();
                } catch (IOException e) {
                }
            if (bos != null)
                try {
                    bos.close();
                } catch (IOException e) {
                }
        }
    }

    public static String getChatLargeImage(String image) {
        String result;
        int pos = image.lastIndexOf('.');
        if (pos != -1) {
            result = Tool.combineStrings(image.substring(0, pos), "_orig", image.substring(pos));
        }
        else
            result = Tool.combineStrings(image, "_orig");
        return result;
    }

    /**
     * 根据设定的分辨率对图片进行压缩处理
     * 
     * @param maxWidth
     *            图片的宽度
     * @param maxHeight
     *            图片的高度
     * @return
     */
    public static Bitmap optimizeBitmap(String path, int maxWidth, int maxHeight) {

        if (path == null || maxWidth <= 0 || maxHeight <= 0) {
            DebugLog.loge("Tool optimizeBitmap return: resource = " + path + " maxWidth = " + maxWidth
                    + " maxHeight = " + maxHeight);
            return null;
        }
        Bitmap result = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        result = BitmapFactory.decodeFile(path, options);

        int widthRatio = (int) Math.ceil(options.outWidth / maxWidth);
        int heightRatio = (int) Math.ceil(options.outHeight / maxHeight);
        if (widthRatio > 1 || heightRatio > 1) {
            if (widthRatio > heightRatio) {
                options.inSampleSize = widthRatio;
            }
            else {
                options.inSampleSize = heightRatio;
            }
        }

        options.inJustDecodeBounds = false;
        try {
            result = BitmapFactory.decodeFile(path, options);
        } catch (OutOfMemoryError e) {
            System.gc();
            e.printStackTrace();
            DebugLog.loge("error=" + e.toString());
            options.inSampleSize *= 3;
            try {
                result = BitmapFactory.decodeFile(path, options);
            } catch (OutOfMemoryError e1) {
                e.printStackTrace();
                DebugLog.loge("error1 =" + e1.toString());
                System.gc();
            }
        }
        return result;
    }

    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }
    
    public static boolean isNetworkServiceAvailable(Context context){
//    	String login_mode = PreferenceWrapper.get(PreferenceWrapper.LOGIN_MODE, "0");
    	//String login = PreferenceWrapper.get(PreferenceWrapper.LOGIN, "0");
    	return hasInternet(context);// && !"0".equals(login);// && !"5".equals(login_mode);
    }
    
    public static Bitmap roundBitmap(Bitmap bitmap, int outputWidth, int outputHeight,float[] radii) {
    	if(outputWidth==0 || outputHeight==0) return null;
        try {
			Bitmap output = Bitmap.createBitmap(outputWidth, outputHeight, Config.ARGB_8888);
			
			Paint paintForRound = new Paint();
			paintForRound.setAntiAlias(true);
			paintForRound.setColor(0xff424242);
			paintForRound.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			
			if(output==null) return null;
			Canvas canvas = new Canvas(output);

			final Rect rect = new Rect(0, 0,outputWidth, outputHeight);
			final RectF rectF = new RectF(rect);

			canvas.drawARGB(0, 0, 0, 0);
			paintForRound.setXfermode(null);
			
			Path path = new Path();
			path.addRoundRect(rectF, radii, Path.Direction.CW);
			canvas.drawPath(path, paintForRound);

			paintForRound.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			
			int bmpWidth = bitmap.getWidth();
			int bmpHeight = bitmap.getHeight();
			
			Rect srcRect;
			
			
			if(bmpWidth >= bmpHeight){
				float srcHeight = bmpHeight;
				float scale =srcHeight/outputHeight;
				float srcWidth = outputWidth*scale;
				srcRect = new Rect( (int)(bmpWidth/2-srcWidth/2),0,(int)(bmpWidth/2+srcWidth/2),bmpHeight);
			}else{
				float srcWidth = bmpWidth;
				float scale =srcWidth/outputWidth;
				float srcHeight = outputHeight*scale;
				srcRect = new Rect(0, (int)(bmpHeight/2-srcHeight/2),bmpWidth,(int)(bmpHeight/2+srcHeight/2));
			}
			canvas.drawBitmap(bitmap,srcRect, rect, paintForRound);
			bitmap=null;
			return output;
		} catch (java.lang.OutOfMemoryError e) {
			DebugLog.logd("CornerWebImageView", "OutOfMemoryError", e);
			return null;
		}
    }
    
    /**
     * 判断顺序为：姓名、手机（或 邮箱 ，二者有一即可 ）、职位、公司
     * @return
     */
//    public static boolean isMycardComplete() {
//    	Card card = Cards.queryCard(JwApplication.mContext, "userid=" + PreferenceWrapper.get("userID", "0") + " AND cardtype=1");
//    	String name = card.firstName + card.lastName + card.enFirstName + card.enMiddleName + card.enLastName;
////        if (TextUtils.isEmpty(card.firstName) && TextUtils.isEmpty(card.lastName) && TextUtils.isEmpty(card.enFirstName)
////                && TextUtils.isEmpty(card.enMiddleName) && TextUtils.isEmpty(card.enLastName) || (TextUtils.isEmpty(card.position))
////                || (TextUtils.isEmpty(card.company))) {
////            return false;
////        } else 
//    	if (TextUtils.isEmpty(name) || TextUtils.isEmpty(name.trim())
//        		|| ((TextUtils.isEmpty(card.mobile) || TextUtils.isEmpty(card.mobile.trim()))
//        				&& (TextUtils.isEmpty(card.email) || TextUtils.isEmpty(card.email.trim())))
//        		|| TextUtils.isEmpty(card.position) || TextUtils.isEmpty(card.position.trim())
//                || TextUtils.isEmpty(card.company) || TextUtils.isEmpty(card.company.trim())){
//        	return false;
//        } else {
//            return true;
//        }
//    }
    
//    public static boolean myNameExist() {
//    	Card card = Cards.queryCard(JwApplication.mContext, "userid=" + PreferenceWrapper.get("userID", "0") + " AND cardtype=1");
//        if (TextUtils.isEmpty(card.firstName) && TextUtils.isEmpty(card.lastName) && TextUtils.isEmpty(card.enFirstName)
//                && TextUtils.isEmpty(card.enMiddleName) && TextUtils.isEmpty(card.enLastName)) {
//            return false;
//        }
//        else {
//            return true;
//        }
//    }
    
    public static Bitmap makeBitmapWithSize(String path, int sampleSize){
    	BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inSampleSize = sampleSize;
		try {
			return BitmapFactory.decodeFile(path, opts);
		} catch (java.lang.OutOfMemoryError e) {
			DebugLog.logd("OutOfMemoryError", "makeBitmap sample size:"+sampleSize, e);
			return null;
		}
    }

    public static int UNCONSTRAINED = -1;
    
	public static Bitmap makeBitmap(String path, int maxNumOfPixels)
			throws OutOfMemoryError {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		if (options.mCancel || options.outWidth == -1
				|| options.outHeight == -1) {
			return null;
		}
		options.inSampleSize = computeSampleSize(options, 520, maxNumOfPixels);

		options.inJustDecodeBounds = false;

		options.inDither = false;
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		return BitmapFactory.decodeFile(path, options);
	}

	/*
	 * Compute the sample size as a function of minSideLength and
	 * maxNumOfPixels. minSideLength is used to specify that minimal width or
	 * height of a bitmap. maxNumOfPixels is used to specify the maximal size in
	 * pixels that is tolerable in terms of memory usage.
	 * 
	 * The function returns a sample size based on the constraints. Both size
	 * and minSideLength can be passed in as IImage.UNCONSTRAINED, which
	 * indicates no care of the corresponding constraint. The functions prefers
	 * returning a sample size that generates a smaller bitmap, unless
	 * minSideLength = IImage.UNCONSTRAINED.
	 * 
	 * Also, the function rounds up the sample size to a power of 2 or multiple
	 * of 8 because BitmapFactory only honors sample size this way. For example,
	 * BitmapFactory downsamples an image by 2 even though the request is 3. So
	 * we round up the sample size to avoid OOM.
	 */
	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		// Log.d(TAG, "ratio:"+w+"x"+h);

		int lowerBound = (maxNumOfPixels == UNCONSTRAINED) ? 1 : (int) Math
				.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == UNCONSTRAINED) ? 128 : (int) Math
				.min(Math.floor(w / minSideLength), Math.floor(h
						/ minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == UNCONSTRAINED)
				&& (minSideLength == UNCONSTRAINED)) {
			return 1;
		} else if (minSideLength == UNCONSTRAINED) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}
    
	public static Boolean copyfile(File fromFile, File toFile) {
		if(fromFile==null||toFile==null){
			return false;
		}
		if (!fromFile.exists()) {
			return false;
		}
		if (!fromFile.isFile()) {
			return false;
		}
		if (!fromFile.canRead()) {
			return false;
		}
		if (!toFile.getParentFile().exists()) {
			toFile.getParentFile().mkdirs();
		}
		
		try {
			java.io.FileInputStream fosfrom = new java.io.FileInputStream(
					fromFile);
			java.io.FileOutputStream fosto = new FileOutputStream(toFile);
			byte bt[] = new byte[1024];
			int c;
			while ((c = fosfrom.read(bt)) > 0) {
				fosto.write(bt, 0, c); // 将内容写到新文件当中
			}
			fosfrom.close();
			fosto.close();
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
	
	
	/**
	 * 对spanableString进行正则判断，如果符合要求，则以表情图片代替
	 * @param context
	 * @param spannableString
	 * @param patten
	 * @param start
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws NumberFormatException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
//    public static void dealExpression(Context context,SpannableString spannableString, Pattern patten, int start) throws SecurityException, NoSuchFieldException, NumberFormatException, IllegalArgumentException, IllegalAccessException {
//    	Matcher matcher = patten.matcher(spannableString);
//        while (matcher.find()) {
//            String key = matcher.group();
//            if (matcher.start() < start) {
//                continue;
//            }
//            Field field = R.drawable.class.getDeclaredField(key);
//			int resId = Integer.parseInt(field.get(null).toString());		//通过上面匹配得到的字符串来生成图片资源id
//            if (resId != 0) {
//                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);	
//                ImageSpan imageSpan = new ImageSpan(bitmap);				//通过图片资源id来得到bitmap，用一个ImageSpan来包装
//                int end = matcher.start() + key.length();					//计算该图片名字的长度，也就是要替换的字符串的长度
//                spannableString.setSpan(imageSpan, matcher.start(), end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);	//将该图片替换字符串中规定的位置中
//                if (end < spannableString.length()) {						//如果整个字符串还未验证完，则继续。。
//                    dealExpression(context,spannableString,  patten, end);
//                }
//                break;
//            }
//        }
//    }
    
    /**
     * 得到一个SpanableString对象，通过传入的字符串,并进行正则判断
     * @param context
     * @param str
     * @return
     */
//    public static SpannableString getExpressionString(Context context,String str,String zhengze){
//    	SpannableString spannableString = new SpannableString(str);
//        Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);		//通过传入的正则表达式来生成一个pattern
//        try {
//            dealExpression(context,spannableString, sinaPatten, 0);
//        } catch (Exception e) {
//           DebugLog.loge("dealExpression", e.getMessage());
//        }
//        return spannableString;
//    }
    
	private static long lastClickTime;

	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 500) {
			return true;
		}
		lastClickTime = time;
		return false;
	}
	
	public static boolean isFastDoubleClick(long delta) {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < delta) {
			return true;
		}
		lastClickTime = time;
		return false;
	}
	private static long lastTime;
	public static boolean isFastDuplicate() {
		long time = System.currentTimeMillis();
		long timeD = time - lastTime;
		if (0 < timeD && timeD < 1000) {
			return true;
		}
		lastTime = time;
		return false;
	}
	public static boolean isRightEmailFormat(String email) {
		boolean bRightFormat = false;
		
		Pattern p = Pattern.compile("^([a-z0-9A-Z]+[-_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
		Matcher m = p.matcher(email);
		bRightFormat = m.matches();
		
		return bRightFormat;
	}
	public static String transStr(String str){
//		String a = str.replaceAll("\\\\", "\\\\\\\\");
		String b = str.replaceAll("\"", "\\\\\"");
		String c = b.replaceAll("\\\\n", "\n");
		return c;
	}
	
	/**
	 * 获取某个时刻后面某天的某个时刻的时间毫秒数
	 * @param current
	 * @return
	 */
	public static long getNextTimeMinus(long current, int delayDay,int hour){
		Calendar cd = Calendar.getInstance();
		cd.setTimeInMillis(current);
		cd.add(Calendar.DAY_OF_YEAR, delayDay);
		cd.set(Calendar.HOUR_OF_DAY, hour);
		return cd.getTimeInMillis();
	}
	//为头像背景分配随即的背景图片
//	public static int getCardBackGround(String cardId) {
//		int[] BackGroundDrawables = new int[] { R.drawable.card_avatar_bg,
//				R.drawable.card_avatar_bg1, R.drawable.card_avatar_bg2,
//				R.drawable.card_avatar_bg3,R.drawable.card_avatar_bg4 };
//		try {
//			int intCardId = Integer.parseInt(cardId);
//			int index = intCardId % BackGroundDrawables.length;
//
//			return BackGroundDrawables[index];
//		} catch (Exception e) {
//			return BackGroundDrawables[0];
//		}
//	}
	
	/**
	 * 去掉重复的数组元素
	 * @param array
	 * @return
	 */
	public static String[] deduplicateArray(String[] array){
		Set<String> set = new HashSet<String>();
		for (int i = 0; i < array.length; i++) {
			if(array[i] != null){
				set.add(array[i]);
			}
		}
		String[] result = new String[set.size()];
		set.toArray(result);
		return result;
	}

	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
	
    /**
     * 获取屏幕宽度 px
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }
}
