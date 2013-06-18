package com.example.galaxy08.tool;

import java.util.HashMap;
import java.util.Map;

import android.text.TextUtils;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 
 */
public class Pinyin {

	private static Pinyin instance;

	public static final char PINYIN_SEPERATOR = ' ';

	public synchronized static Pinyin getInstance() {
		if (instance == null)
			instance = new Pinyin();
		return instance;
	}

	private HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();

	private Pinyin() {
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
	}

	/**
	 * 进行拼音拆字，组合成拼音单字和首字母拼接
	 * 
	 * @param gb2312
	 *            待拆中文
	 * @return
	 */
//	 public String getPinyin(String gb2312){
//	 return getPinyin( gb2312, true);
//	 }

	public String getPinyin(char c) {
		String pinyin = null;
		if (c < 0)
			return pinyin;
		if ((c >= 0x4e00) && (c <= 0x9fbb)) {
			try {
				String[] pinyins = PinyinHelper.toHanyuPinyinStringArray(c,
						defaultFormat);
				if (pinyins != null && pinyins.length>0)
					pinyin = pinyins[0];
			} catch (BadHanyuPinyinOutputFormatCombination e) {
				DebugLog.logd("Pinyin", "get pinyin error", e);
			}
		}
		return pinyin;
	}

	public String getPinyin(String str) {
		if (TextUtils.isEmpty(str))
			return "";
		char[] chars = str.toCharArray();
		StringBuilder sb = new StringBuilder();
		for (char ch : chars) {
			String pinyin = getPinyin(ch);
			if (!TextUtils.isEmpty(pinyin)) {
				if (sb.length() > 0)
					sb.append(PINYIN_SEPERATOR);
				sb.append(pinyin);
			}
		}
		return sb.length() > 0 ? sb.toString() : "";
	}
	
	public String getPinyinNotStrick(String str) {
		if (TextUtils.isEmpty(str))
			return "";
		boolean isPinyin = false;
		char[] chars = str.toCharArray();
		StringBuilder sb = new StringBuilder();
		for (char ch : chars) {
			if (ch >= 0 && ch <= 255) {
				if (isPinyin && sb.length() > 0)
					sb.append(PINYIN_SEPERATOR);
				sb.append(ch);
				isPinyin = false;
			} else {
				String pinyin = getPinyin(ch);
				if (!TextUtils.isEmpty(pinyin)) {
					if (sb.length() > 0)
						sb.append(PINYIN_SEPERATOR);
					sb.append(pinyin);
					isPinyin = true;
				}

			}
		}
		return sb.length() > 0 ? sb.toString()
				: "";
	}

	public String getPinyinHead(String str) {
		String pinyin = getPinyin(str);
		return getPinyinHeadFromPinyin(pinyin);
	}

	public String getPinyinHeadFromPinyin(String pinyin) {
		if (TextUtils.isEmpty(pinyin))
			return "";
		StringBuilder sb = new StringBuilder();
		String[] pinyins = pinyin.split(PINYIN_SEPERATOR + "");
		for (String pi : pinyins)
			if (!TextUtils.isEmpty(pi))
				sb.append(pi.charAt(0));
		return sb.toString();
	}

	/**
	 * 进行拼音拆字，组合成拼音单字和首字母拼接
	 * 
	 * @param gb2312
	 *            待拆中文
	 * @param useSingleWord
	 *            是否包含单字拼音
	 * @return
	 */
	public String getPinyin(String gb2312, boolean useSingleWord) {
		if (TextUtils.isEmpty(gb2312)) {
			return gb2312;
		}
		char[] chars = gb2312.toCharArray();

		StringBuffer retuBuf = new StringBuffer();
		StringBuffer breBuf = new StringBuffer();
		StringBuffer totleBuf = new StringBuffer();
		for (int i = 0, Len = chars.length; i < Len; i++) {
			String pinyin = null;
			char c = chars[i];
			if ((c >= 0x4e00) && (c <= 0x9fbb)) {
				try {
					String[] pinyins = PinyinHelper.toHanyuPinyinStringArray(
							chars[i], defaultFormat);
					if (pinyins != null)
						pinyin = pinyins[0];
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
				totleBuf.append(c);
				continue;
			}
			if (pinyin != null) {
				if (useSingleWord) {
					retuBuf.append(",");
					retuBuf.append(pinyin);
				}
				totleBuf.append(pinyin);
				breBuf.append(pinyin.substring(0, 1));
			}
		}
		if (breBuf.length() > 0)
			retuBuf.append(",").append(breBuf);
		retuBuf.append(",").append(totleBuf);
		return retuBuf.toString();
	}

	public String getBjxPinyin(String lastName) {
		if(TextUtils.isEmpty(lastName)) return "";
		String result = null;
		if (bjx == null)
			initBjx();
		if (bjx.containsKey(lastName))
			result = bjx.get(lastName);
		return result;
	}

	private static Map<String, String> bjx;

	private void initBjx() {
		bjx = new HashMap<String, String>(636);
		bjx.put("艾", "ai");
		bjx.put("爱新觉罗", "ai"+PINYIN_SEPERATOR+"xin"+PINYIN_SEPERATOR+"jue"+PINYIN_SEPERATOR+"luo");
		bjx.put("安", "an");
		bjx.put("敖", "ao");
		bjx.put("巴", "ba");
		bjx.put("白", "bai");
		bjx.put("百里", "bai"+PINYIN_SEPERATOR+"li");
		bjx.put("柏", "bo");
		bjx.put("班", "ban");
		bjx.put("包", "bao");
		bjx.put("鲍", "bao");
		bjx.put("暴", "bao");
		bjx.put("贝", "bei");
		bjx.put("贲", "ben");
		bjx.put("毕", "bi");
		bjx.put("碧鲁", "bi"+PINYIN_SEPERATOR+"lu");
		bjx.put("边", "bian");
		bjx.put("卞", "bian");
		bjx.put("别", "bie");
		bjx.put("邴", "bing");
		bjx.put("伯赏", "bo"+PINYIN_SEPERATOR+"shang");
		bjx.put("博尔济锦", "bo"+PINYIN_SEPERATOR+"er"+PINYIN_SEPERATOR+"ji"+PINYIN_SEPERATOR+"jin");
		bjx.put("薄", "bo");
		bjx.put("卜", "bu");
		bjx.put("步", "bu");
		bjx.put("蔡", "cai");
		bjx.put("苍", "cang");
		bjx.put("藏", "zang");
		bjx.put("操", "cao");
		bjx.put("曹", "cao");
		bjx.put("岑", "cen");
		bjx.put("查", "zha");
		bjx.put("柴", "chai");
		bjx.put("昌", "chang");
		bjx.put("长孙", "zhang"+PINYIN_SEPERATOR+"sun");
		bjx.put("常", "chang");
		bjx.put("畅", "chang");
		bjx.put("晁", "chao");
		bjx.put("巢", "chao");
		bjx.put("车", "che");
		bjx.put("陈", "chen");
		bjx.put("谌", "chen");
		bjx.put("成", "cheng");
		bjx.put("承", "cheng");
		bjx.put("程", "cheng");
		bjx.put("池", "chi");
		bjx.put("充", "chong");
		bjx.put("仇", "qiu");
		bjx.put("初", "chu");
		bjx.put("储", "chu");
		bjx.put("楚", "chu");
		bjx.put("褚", "chu");
		bjx.put("淳于", "chun"+PINYIN_SEPERATOR+"yu");
		bjx.put("从", "cong");
		bjx.put("崔", "cui");
		bjx.put("崔", "cui");
		bjx.put("戴", "dai");
		bjx.put("单", "shan");
		bjx.put("单于", "chan"+PINYIN_SEPERATOR+"yu");
		bjx.put("澹台", "tantai");
		bjx.put("党", "dang");
		bjx.put("刀", "dao");
		bjx.put("道", "dao");
		bjx.put("邓", "deng");
		bjx.put("狄", "di");
		bjx.put("第五", "di"+PINYIN_SEPERATOR+"wu");
		bjx.put("刁", "diao");
		bjx.put("丁", "ding");
		bjx.put("东", "dong");
		bjx.put("东方", "dong"+PINYIN_SEPERATOR+"fang");
		bjx.put("东郭", "dong"+PINYIN_SEPERATOR+"guo");
		bjx.put("东门", "dong"+PINYIN_SEPERATOR+"men");
		bjx.put("董", "dong");
		bjx.put("都", "du");
		bjx.put("钭", "tou");
		bjx.put("窦", "dou");
		bjx.put("独孤", "du"+PINYIN_SEPERATOR+"gu");
		bjx.put("堵", "du");
		bjx.put("杜", "du");
		bjx.put("端木", "duan"+PINYIN_SEPERATOR+"mu");
		bjx.put("段", "duan");
		bjx.put("段干", "duan"+PINYIN_SEPERATOR+"gan");
		bjx.put("多", "duo");
		bjx.put("额尔德雨", "e"+PINYIN_SEPERATOR+"er"+PINYIN_SEPERATOR+"de"+PINYIN_SEPERATOR+"yu");
		bjx.put("鄂", "e");
		bjx.put("法", "fa");
		bjx.put("藩", "fan");
		bjx.put("樊", "fan");
		bjx.put("范", "fan");
		bjx.put("方", "fang");
		bjx.put("房", "fang");
		bjx.put("费", "fei");
		bjx.put("丰", "feng");
		bjx.put("封", "feng");
		bjx.put("酆", "feng");
		bjx.put("冯", "feng");
		bjx.put("逢", "feng");
		bjx.put("凤", "feng");
		bjx.put("伏", "fu");
		bjx.put("扶", "fu");
		bjx.put("洑", "fu");
		bjx.put("浮", "fu");
		bjx.put("符", "fu");
		bjx.put("傅", "fu");
		bjx.put("富", "fu");
		bjx.put("富察", "fu"+PINYIN_SEPERATOR+"cha");
		bjx.put("盖", "gai");
		bjx.put("干", "gan");
		bjx.put("甘", "gan");
		bjx.put("刚", "gang");
		bjx.put("高", "gao");
		bjx.put("郜", "gao");
		bjx.put("戈", "ge");
		bjx.put("葛", "ge");
		bjx.put("庚", "geng");
		bjx.put("耿", "geng");
		bjx.put("弓", "gong");
		bjx.put("公", "gong");
		bjx.put("公良", "gong"+PINYIN_SEPERATOR+"liang");
		bjx.put("公孙", "gong"+PINYIN_SEPERATOR+"sun");
		bjx.put("公西", "gong"+PINYIN_SEPERATOR+"xi");
		bjx.put("公羊", "gong"+PINYIN_SEPERATOR+"yang");
		bjx.put("公冶", "gong"+PINYIN_SEPERATOR+"ye");
		bjx.put("宫", "gong");
		bjx.put("龚", "gong");
		bjx.put("巩", "gong");
		bjx.put("贡", "gong");
		bjx.put("勾", "gou");
		bjx.put("缑亢", "gou"+PINYIN_SEPERATOR+"kang");
		bjx.put("苟", "gou");
		bjx.put("辜", "gu");
		bjx.put("古", "gu");
		bjx.put("谷", "gu");
		bjx.put("谷粱", "gu"+PINYIN_SEPERATOR+"liang");
		bjx.put("顾", "gu");
		bjx.put("关", "guan");
		bjx.put("官", "guan");
		bjx.put("冠", "guan");
		bjx.put("管", "guan");
		bjx.put("光", "guang");
		bjx.put("广", "guang");
		bjx.put("归", "gui");
		bjx.put("归海", "gui"+PINYIN_SEPERATOR+"hai");
		bjx.put("妫", "gui");
		bjx.put("桂", "gui");
		bjx.put("郭", "guo");
		bjx.put("国", "guo");
		bjx.put("过", "guo");
		bjx.put("海", "hai");
		bjx.put("韩", "han");
		bjx.put("杭", "hang");
		bjx.put("郝", "hao");
		bjx.put("何", "he");
		bjx.put("和", "he");
		bjx.put("贺", "he");
		bjx.put("赫", "he");
		bjx.put("赫连", "he"+PINYIN_SEPERATOR+"lian");
		bjx.put("赫舍里", "he"+PINYIN_SEPERATOR+"she"+PINYIN_SEPERATOR+"li");
		bjx.put("衡", "heng");
		bjx.put("弘", "hong");
		bjx.put("红", "hong");
		bjx.put("洪", "hong");
		bjx.put("侯", "hou");
		bjx.put("后", "hou");
		bjx.put("后", "hou");
		bjx.put("呼延", "hu"+PINYIN_SEPERATOR+"yan");
		bjx.put("胡", "hu");
		bjx.put("户", "hu");
		bjx.put("扈", "hu");
		bjx.put("花", "hua");
		bjx.put("华", "hua");
		bjx.put("滑", "hua");
		bjx.put("怀", "huai");
		bjx.put("桓", "huan");
		bjx.put("宦", "huan");
		bjx.put("皇甫", "huang"+PINYIN_SEPERATOR+"fu");
		bjx.put("黄", "huang");
		bjx.put("惠", "hui");
		bjx.put("荤", "hun");
		bjx.put("霍", "huo");
		bjx.put("姬", "ji");
		bjx.put("基", "ji");
		bjx.put("嵇", "ji");
		bjx.put("吉", "ji");
		bjx.put("汲", "ji");
		bjx.put("籍", "ji");
		bjx.put("计", "ji");
		bjx.put("纪", "ji");
		bjx.put("季", "ji");
		bjx.put("蓟", "ji");
		bjx.put("暨", "ji");
		bjx.put("冀", "ji");
		bjx.put("夹谷", "jia"+PINYIN_SEPERATOR+"gu");
		bjx.put("家", "jia");
		bjx.put("郏", "jia");
		bjx.put("贾", "jia");
		bjx.put("剪", "jian");
		bjx.put("简", "jian");
		bjx.put("蹇", "jian");
		bjx.put("江", "jiang");
		bjx.put("姜", "jiang");
		bjx.put("蒋", "jiang");
		bjx.put("焦", "jiao");
		bjx.put("矫", "jiao");
		bjx.put("揭", "jie");
		bjx.put("解", "xie");
		bjx.put("金", "jin");
		bjx.put("晋", "jin");
		bjx.put("晋楚", "jin"+PINYIN_SEPERATOR+"chu");
		bjx.put("靳", "jin");
		bjx.put("经", "jing");
		bjx.put("荆", "jing");
		bjx.put("井", "jing");
		bjx.put("景", "jing");
		bjx.put("咎", "jiu");
		bjx.put("居", "ju");
		bjx.put("鞠", "ju");
		bjx.put("瞿", "qu");
		bjx.put("觉尔察", "jue"+PINYIN_SEPERATOR+"er"+PINYIN_SEPERATOR+"cha");
		bjx.put("卡", "ka");
		bjx.put("堪", "kan");
		bjx.put("阚", "kan");
		bjx.put("康", "kang");
		bjx.put("柯", "ke");
		bjx.put("空", "kong");
		bjx.put("孔", "kong");
		bjx.put("寇", "kou");
		bjx.put("库雅喇", "ku"+PINYIN_SEPERATOR+"ya"+PINYIN_SEPERATOR+"la");
		bjx.put("蒯", "kuai");
		bjx.put("匡", "kuang");
		bjx.put("邝", "kuang");
		bjx.put("况", "kuang");
		bjx.put("况后", "kuang"+PINYIN_SEPERATOR+"hou");
		bjx.put("旷", "kuang");
		bjx.put("夔", "kui");
		bjx.put("来", "lai");
		bjx.put("赖", "lai");
		bjx.put("蓝", "lan");
		bjx.put("郎", "lang");
		bjx.put("劳", "lao");
		bjx.put("乐", "yue");
		bjx.put("乐正", "yue"+PINYIN_SEPERATOR+"zheng");
		bjx.put("雷", "lei");
		bjx.put("冷", "leng");
		bjx.put("黎", "li");
		bjx.put("李", "li");
		bjx.put("厉", "li");
		bjx.put("利", "li");
		bjx.put("励", "li");
		bjx.put("郦", "li");
		bjx.put("连", "lian");
		bjx.put("廉", "lian");
		bjx.put("梁", "liang");
		bjx.put("梁丘", "liang"+PINYIN_SEPERATOR+"qiu");
		bjx.put("粱", "liang");
		bjx.put("廖", "liao");
		bjx.put("林", "lin");
		bjx.put("蔺", "lin");
		bjx.put("凌", "ling");
		bjx.put("令狐", "ling"+PINYIN_SEPERATOR+"hu");
		bjx.put("刘", "liu");
		bjx.put("留", "liu");
		bjx.put("柳", "liu");
		bjx.put("龙", "long");
		bjx.put("隆", "long");
		bjx.put("娄", "lou");
		bjx.put("楼", "lou");
		bjx.put("卢", "lu");
		bjx.put("鲁", "lu");
		bjx.put("陆", "lu");
		bjx.put("逯", "lu");
		bjx.put("鹿", "lu");
		bjx.put("禄", "lu");
		bjx.put("路", "lu");
		bjx.put("闾丘", "lv"+PINYIN_SEPERATOR+"qiu");
		bjx.put("吕", "lv");
		bjx.put("栾", "luan");
		bjx.put("罗", "luo");
		bjx.put("骆", "luo");
		bjx.put("麻", "ma");
		bjx.put("马", "ma");
		bjx.put("麦", "mai");
		bjx.put("满", "man");
		bjx.put("毛", "mao");
		bjx.put("茅", "mao");
		bjx.put("冒", "mao");
		bjx.put("梅", "mei");
		bjx.put("门", "men");
		bjx.put("蒙", "meng");
		bjx.put("孟", "meng");
		bjx.put("糜", "mi");
		bjx.put("米", "mi");
		bjx.put("芈", "mi");
		bjx.put("宓", "mi");
		bjx.put("苗", "miao");
		bjx.put("乜", "nie");
		bjx.put("闵", "min");
		bjx.put("明", "ming");
		bjx.put("谬", "miu");
		bjx.put("缪", "miu");
		bjx.put("莫", "mo");
		bjx.put("墨哈", "mo"+PINYIN_SEPERATOR+"ha");
		bjx.put("牧", "mu");
		bjx.put("睦", "mu");
		bjx.put("慕", "mu");
		bjx.put("慕容", "mu"+PINYIN_SEPERATOR+"rong");
		bjx.put("穆", "mu");
		bjx.put("那", "na");
		bjx.put("那拉", "na"+PINYIN_SEPERATOR+"la");
		bjx.put("纳喇", "na"+PINYIN_SEPERATOR+"la");
		bjx.put("南", "nan");
		bjx.put("南宫", "nan"+PINYIN_SEPERATOR+"gong");
		bjx.put("南门", "nan"+PINYIN_SEPERATOR+"men");
		bjx.put("讷殷富察", "ne"+PINYIN_SEPERATOR+"yin"+PINYIN_SEPERATOR+"fu"+PINYIN_SEPERATOR+"cha");
		bjx.put("能", "neng");
		bjx.put("倪", "ni");
		bjx.put("年爱", "nian"+PINYIN_SEPERATOR+"ai");
		bjx.put("聂", "nie");
		bjx.put("宁", "ning");
		bjx.put("牛", "niu");
		bjx.put("钮", "niu");
		bjx.put("钮祜禄", "niu"+PINYIN_SEPERATOR+"hu"+PINYIN_SEPERATOR+"lu");
		bjx.put("农", "nong");
		bjx.put("欧", "ou");
		bjx.put("欧阳", "ou"+PINYIN_SEPERATOR+"yang");
		bjx.put("殴", "ou");
		bjx.put("潘", "pan");
		bjx.put("庞", "pang");
		bjx.put("逄", "pang");
		bjx.put("裴", "pei");
		bjx.put("朋", "peng");
		bjx.put("彭", "peng");
		bjx.put("蓬", "peng");
		bjx.put("皮", "pi");
		bjx.put("平", "ping");
		bjx.put("蒲", "pu");
		bjx.put("濮", "pu");
		bjx.put("濮阳", "pu"+PINYIN_SEPERATOR+"yang");
		bjx.put("朴", "piao");
		bjx.put("浦", "pu");
		bjx.put("戚", "qi");
		bjx.put("漆", "qi");
		bjx.put("漆雕", "qi"+PINYIN_SEPERATOR+"diao");
		bjx.put("亓官", "qi"+PINYIN_SEPERATOR+"guan");
		bjx.put("祁", "qi");
		bjx.put("齐", "qi");
		bjx.put("钱", "qian");
		bjx.put("强", "qiang");
		bjx.put("乔", "qiao");
		bjx.put("谯笪", "qiao"+PINYIN_SEPERATOR+"da");
		bjx.put("钦", "qin");
		bjx.put("秦", "qin");
		bjx.put("青", "qing");
		bjx.put("卿", "qing");
		bjx.put("清", "qing");
		bjx.put("庆", "qing");
		bjx.put("邛", "qiong");
		bjx.put("丘", "qiu");
		bjx.put("邱", "qiu");
		bjx.put("秋", "qiu");
		bjx.put("求", "qiu");
		bjx.put("裘", "qiu");
		bjx.put("区", "ou");
		bjx.put("曲", "qu");
		bjx.put("屈", "qu");
		bjx.put("麴", "qu");
		bjx.put("璩", "qu");
		bjx.put("全", "quan");
		bjx.put("权", "quan");
		bjx.put("却", "que");
		bjx.put("阙", "que");
		bjx.put("冉", "ran");
		bjx.put("壤驷", "rang"+PINYIN_SEPERATOR+"si");
		bjx.put("让", "rang");
		bjx.put("饶", "rao");
		bjx.put("任", "ren");
		bjx.put("戎", "rong");
		bjx.put("荣", "rong");
		bjx.put("容", "rong");
		bjx.put("融", "rong");
		bjx.put("茹", "ru");
		bjx.put("汝鄢", "ru"+PINYIN_SEPERATOR+"yan");
		bjx.put("阮", "ruan");
		bjx.put("芮", "rui");
		bjx.put("撒哈拉", "sa"+PINYIN_SEPERATOR+"ha"+PINYIN_SEPERATOR+"la");
		bjx.put("萨", "sa");
		bjx.put("萨克达", "sa"+PINYIN_SEPERATOR+"ke"+PINYIN_SEPERATOR+"da");
		bjx.put("萨嘛喇", "sa"+PINYIN_SEPERATOR+"ma"+PINYIN_SEPERATOR+"la");
		bjx.put("赛", "sai");
		bjx.put("桑", "sang");
		bjx.put("沙", "sha");
		bjx.put("山", "shan");
		bjx.put("商", "shang");
		bjx.put("商牟", "shang"+PINYIN_SEPERATOR+"mou");
		bjx.put("上官", "shang"+PINYIN_SEPERATOR+"guan");
		bjx.put("尚", "shang");
		bjx.put("韶", "shao");
		bjx.put("邵", "shao");
		bjx.put("佘", "she");
		bjx.put("佘佴", "she"+PINYIN_SEPERATOR+"er");
		bjx.put("厍", "she");
		bjx.put("申", "shen");
		bjx.put("申屠", "shen"+PINYIN_SEPERATOR+"tu");
		bjx.put("沈", "shen");
		bjx.put("慎", "shen");
		bjx.put("生", "sheng");
		bjx.put("盛", "sheng");
		bjx.put("师", "shi");
		bjx.put("施", "shi");
		bjx.put("石", "shi");
		bjx.put("时", "shi");
		bjx.put("史", "shi");
		bjx.put("是", "shi");
		bjx.put("释", "shi");
		bjx.put("首", "shou");
		bjx.put("寿", "shou");
		bjx.put("殳", "shu");
		bjx.put("疏束", "shu"+PINYIN_SEPERATOR+"shu");
		bjx.put("舒", "shu");
		bjx.put("束", "shu");
		bjx.put("帅", "shuai");
		bjx.put("双", "shuang");
		bjx.put("水", "shui");
		bjx.put("司", "si");
		bjx.put("司空", "si"+PINYIN_SEPERATOR+"kong");
		bjx.put("司寇", "si"+PINYIN_SEPERATOR+"kou");
		bjx.put("司马", "si"+PINYIN_SEPERATOR+"ma");
		bjx.put("司徒", "si"+PINYIN_SEPERATOR+"tu");
		bjx.put("斯", "si");
		bjx.put("松", "song");
		bjx.put("宋", "song");
		bjx.put("苏", "su");
		bjx.put("宿", "su");
		bjx.put("粟", "su");
		bjx.put("隋", "sui");
		bjx.put("孙", "sun");
		bjx.put("索", "suo");
		bjx.put("他塔喇", "ta"+PINYIN_SEPERATOR+"ta"+PINYIN_SEPERATOR+"la");
		bjx.put("台", "tai");
		bjx.put("邰", "tai");
		bjx.put("太叔", "tai"+PINYIN_SEPERATOR+"shu");
		bjx.put("泰", "tai");
		bjx.put("谈", "tan");
		bjx.put("覃", "qin");
		bjx.put("谭", "tan");
		bjx.put("汤", "tang");
		bjx.put("唐", "tang");
		bjx.put("陶", "tao");
		bjx.put("腾", "teng");
		bjx.put("滕", "teng");
		bjx.put("田", "tian");
		bjx.put("通", "tong");
		bjx.put("同", "tong");
		bjx.put("佟", "tong");
		bjx.put("童", "tong");
		bjx.put("图门", "tu"+PINYIN_SEPERATOR+"men");
		bjx.put("涂", "tu");
		bjx.put("涂钦", "tu"+PINYIN_SEPERATOR+"qin");
		bjx.put("屠", "tu");
		bjx.put("拓拔", "tuo"+PINYIN_SEPERATOR+"ba");
		bjx.put("万", "wan");
		bjx.put("万俟", "mo"+PINYIN_SEPERATOR+"qi");
		bjx.put("汪", "wang");
		bjx.put("王", "wang");
		bjx.put("危", "wei");
		bjx.put("微生", "wei"+PINYIN_SEPERATOR+"sheng");
		bjx.put("韦", "wei");
		bjx.put("隗", "wei");
		bjx.put("卫", "wei");
		bjx.put("尉", "wei");
		bjx.put("尉迟", "yu"+PINYIN_SEPERATOR+"chi");
		bjx.put("蔚", "wei");
		bjx.put("魏", "wei");
		bjx.put("温", "wen");
		bjx.put("文", "wen");
		bjx.put("闻", "wen");
		bjx.put("闻人", "wen"+PINYIN_SEPERATOR+"ren");
		bjx.put("问", "wen");
		bjx.put("翁", "weng");
		bjx.put("沃", "wo");
		bjx.put("乌", "wu");
		bjx.put("乌雅", "wu"+PINYIN_SEPERATOR+"ya");
		bjx.put("邬", "wu");
		bjx.put("巫", "wu");
		bjx.put("巫马", "wu"+PINYIN_SEPERATOR+"ma");
		bjx.put("毋", "wu");
		bjx.put("吴", "wu");
		bjx.put("吾", "wu");
		bjx.put("伍", "wu");
		bjx.put("武", "wu");
		bjx.put("西门", "xi"+PINYIN_SEPERATOR+"men");
		bjx.put("郗", "xi");
		bjx.put("奚", "xi");
		bjx.put("习", "xi");
		bjx.put("席", "xi");
		bjx.put("喜塔腊", "xi"+PINYIN_SEPERATOR+"ta"+PINYIN_SEPERATOR+"la");
		bjx.put("郤", "xi");
		bjx.put("夏", "xia");
		bjx.put("夏侯", "xia"+PINYIN_SEPERATOR+"hou");
		bjx.put("鲜", "xian");
		bjx.put("鲜于", "xian"+PINYIN_SEPERATOR+"yu");
		bjx.put("咸", "xian");
		bjx.put("冼", "xian");
		bjx.put("相", "xiang");
		bjx.put("相里", "xiang"+PINYIN_SEPERATOR+"li");
		bjx.put("向", "xiang");
		bjx.put("项", "xiang");
		bjx.put("萧", "xiao");
		bjx.put("肖", "xiao");
		bjx.put("谢", "xie");
		bjx.put("忻", "xin");
		bjx.put("辛", "xin");
		bjx.put("莘", "shen");
		bjx.put("邢", "xing");
		bjx.put("幸", "xing");
		bjx.put("熊", "xiong");
		bjx.put("胥", "xu");
		bjx.put("须", "xu");
		bjx.put("徐", "xu");
		bjx.put("许", "xu");
		bjx.put("轩辕", "xuan"+PINYIN_SEPERATOR+"yuan");
		bjx.put("宣", "xuan");
		bjx.put("薛", "xue");
		bjx.put("寻", "xun");
		bjx.put("荀", "xun");
		bjx.put("鄢", "yan");
		bjx.put("闫法", "yan"+PINYIN_SEPERATOR+"fa");
		bjx.put("严", "yan");
		bjx.put("言", "yan");
		bjx.put("言福", "yan"+PINYIN_SEPERATOR+"fu");
		bjx.put("阎", "yan");
		bjx.put("颜", "yan");
		bjx.put("晏", "yan");
		bjx.put("燕", "yan");
		bjx.put("羊", "yang");
		bjx.put("羊舌", "yang"+PINYIN_SEPERATOR+"she");
		bjx.put("阳佟", "yang"+PINYIN_SEPERATOR+"tong");
		bjx.put("杨", "yang");
		bjx.put("仰", "yang");
		bjx.put("养", "yang");
		bjx.put("姚", "yao");
		bjx.put("业", "ye");
		bjx.put("叶", "ye");
		bjx.put("叶赫那拉", "ye"+PINYIN_SEPERATOR+"he"+PINYIN_SEPERATOR+"na"+PINYIN_SEPERATOR+"la");
		bjx.put("叶赫那兰", "ye"+PINYIN_SEPERATOR+"he"+PINYIN_SEPERATOR+"na"+PINYIN_SEPERATOR+"lan");
		bjx.put("伊", "yi");
		bjx.put("伊雨根觉罗", "yi"+PINYIN_SEPERATOR+"yu"+PINYIN_SEPERATOR+"gen"+PINYIN_SEPERATOR+"jue"+PINYIN_SEPERATOR+"luo");
		bjx.put("衣", "yi");
		bjx.put("依尔觉罗", "yi"+PINYIN_SEPERATOR+"er"+PINYIN_SEPERATOR+"jue"+PINYIN_SEPERATOR+"luo");
		bjx.put("义", "yi");
		bjx.put("易", "yi");
		bjx.put("奕", "yi");
		bjx.put("羿", "yi");
		bjx.put("益", "yi");
		bjx.put("阴", "yin");
		bjx.put("殷", "yin");
		bjx.put("银", "yin");
		bjx.put("尹", "yin");
		bjx.put("印", "yin");
		bjx.put("应", "ying");
		bjx.put("英", "ying");
		bjx.put("雍", "yong");
		bjx.put("尤", "you");
		bjx.put("游", "you");
		bjx.put("有", "you");
		bjx.put("有琴", "you"+PINYIN_SEPERATOR+"qin");
		bjx.put("酉", "you");
		bjx.put("于", "yu");
		bjx.put("余", "yu");
		bjx.put("於", "yu");
		bjx.put("鱼", "yu");
		bjx.put("俞", "yu");
		bjx.put("虞", "yu");
		bjx.put("宇文", "yu"+PINYIN_SEPERATOR+"wen");
		bjx.put("羽", "yu");
		bjx.put("禹", "yu");
		bjx.put("庾", "yu");
		bjx.put("郁", "yu");
		bjx.put("喻", "yu");
		bjx.put("愈", "yu");
		bjx.put("元", "yuan");
		bjx.put("袁", "yuan");
		bjx.put("岳", "yue");
		bjx.put("岳帅", "yue"+PINYIN_SEPERATOR+"shuai");
		bjx.put("越", "yue");
		bjx.put("云", "yun");
		bjx.put("恽", "yun");
		bjx.put("宰", "zai");
		bjx.put("宰父", "zai"+PINYIN_SEPERATOR+"fu");
		bjx.put("昝", "zan");
		bjx.put("臧", "zang");
		bjx.put("迮", "ze");
		bjx.put("曾", "zeng");
		bjx.put("翟", "zhai");
		bjx.put("詹", "zhan");
		bjx.put("展", "zhan");
		bjx.put("占", "zhan");
		bjx.put("湛", "zhan");
		bjx.put("张", "zhang");
		bjx.put("章", "zhang");
		bjx.put("章佳", "zhang"+PINYIN_SEPERATOR+"jia");
		bjx.put("仉", "zhang");
		bjx.put("仉督", "zhang"+PINYIN_SEPERATOR+"du");
		bjx.put("招", "zhao");
		bjx.put("赵", "zhao");
		bjx.put("真", "zhen");
		bjx.put("甄", "zhen");
		bjx.put("郑", "zheng");
		bjx.put("支", "zhi");
		bjx.put("植", "zhi");
		bjx.put("终", "zhong");
		bjx.put("钟", "zhong");
		bjx.put("钟离", "zhong"+PINYIN_SEPERATOR+"li");
		bjx.put("仲", "zhong");
		bjx.put("仲长", "zhong"+PINYIN_SEPERATOR+"chang");
		bjx.put("仲孙", "zhong"+PINYIN_SEPERATOR+"sun");
		bjx.put("周", "zhou");
		bjx.put("朱", "zhu");
		bjx.put("诸", "zhu");
		bjx.put("诸葛", "zhu"+PINYIN_SEPERATOR+"ge");
		bjx.put("竹", "zhu");
		bjx.put("竺", "zhu");
		bjx.put("祝", "zhu");
		bjx.put("爪雨佳", "zhua"+PINYIN_SEPERATOR+"yu"+PINYIN_SEPERATOR+"jia");
		bjx.put("颛孙", "zhuan"+PINYIN_SEPERATOR+"sun");
		bjx.put("庄", "zhuang");
		bjx.put("卓", "zhuo");
		bjx.put("禚", "zhuo");
		bjx.put("子车", "zi"+PINYIN_SEPERATOR+"che");
		bjx.put("訾", "zi");
		bjx.put("宗", "zong");
		bjx.put("宗政", "zong"+PINYIN_SEPERATOR+"zheng");
		bjx.put("邹", "zou");
		bjx.put("祖", "zu");
		bjx.put("左", "zuo");
		bjx.put("左丘", "zuo"+PINYIN_SEPERATOR+"qiu");

	}
}
