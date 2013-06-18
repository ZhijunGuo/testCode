package com.example.galaxy08;

import java.io.InputStream;
import java.util.Calendar;

import java.util.Date;


import android.content.Context;
import android.content.res.Resources;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import android.graphics.Color;

import android.graphics.Paint;

import android.graphics.Path;

import android.graphics.Path.Direction;

import android.graphics.Typeface;

import android.util.AttributeSet;

import android.util.Log;

import android.view.MotionEvent;

import android.view.View;


/**
 */

public class EggCalendar extends View implements View.OnTouchListener{

	private final static String TAG = "anCalendar";

	private Date selectedStartDate;

	private Date selectedEndDate;

	private Date curDate; // 当前日历显示的月

	private Date today; // 今天的日期文字显示红色

	private Date downDate; // 手指按下状态时临时日期

	private Date showFirstDate, showLastDate; // 日历显示的第一个日期和最后一个日期

	private int downIndex; // 按下的格子索引

	private Calendar calendar;

	private Surface surface;

	private int[] date = new int[42]; // 日历显示数字

	private int curStartIndex, curEndIndex; // 当前显示的日历起始的索引

	private boolean completed = false; // 为false表示只选择了开始日期，true表示结束日期也选择了

	public EggCalendar(Context context) {

		super(context);

		init();

	}


	public EggCalendar(Context context, AttributeSet attrs) {

		super(context, attrs);

		init();

	}

	private int[] hasTask;
	
	public void setHasTask(int[] hasTask) {
		if(hasTask!=null)
			this.hasTask = hasTask;
		else 
			this.hasTask = new int[31];
	}
	
	public EggCalendar(Context context, int[] hasTask) {
		super(context);
		this.setHasTask(hasTask);
		init();
	}


	private void init() {

		//获取今天的日期
		curDate = selectedStartDate = selectedEndDate = today = new Date();
		//获取日历的一个实例
		calendar = Calendar.getInstance();
		
		calendar.setTime(curDate);

		surface = new Surface();

		surface.density = getResources().getDisplayMetrics().density;

		setBackgroundColor(surface.bgColor);

		setOnTouchListener(this);

	}

	@Override

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		surface.width = (int) (200 * surface.density);

		surface.height = (int) (215 * surface.density);

		if (View.MeasureSpec.getMode(widthMeasureSpec) == View.MeasureSpec.EXACTLY) {

			surface.width = View.MeasureSpec.getSize(widthMeasureSpec);

		}

		if (View.MeasureSpec.getMode(heightMeasureSpec) == View.MeasureSpec.EXACTLY) {

			surface.height = View.MeasureSpec.getSize(heightMeasureSpec);

		}

		//Log.d(TAG, "w-h:" + surface.width + "-" + surface.height);

		widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(surface.width, View.MeasureSpec.EXACTLY);

		heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(surface.height, View.MeasureSpec.EXACTLY);

		setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}

	@Override

	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

		Log.d(TAG, "[onLayout] changed:" + (changed ? "new size" : "not change") + " left:" + left + " top:" + top + 

				" right:" + right + " bottom:" + bottom);

		if (changed) {

			surface.init();

		}

		super.onLayout(changed, left, top, right, bottom);

	}

	@Override

	protected void onDraw(Canvas canvas) {

		//Log.d(TAG, "onDraw");

		// 画框

		canvas.drawPath(surface.boxPath, surface.borderPaint);

		// 年月

		String monthText = getYearAndmonth();

		float textWidth = surface.monthPaint.measureText(monthText);

		canvas.drawText(monthText, (surface.width - textWidth)/2f, surface.monthHeight*3/4f, surface.monthPaint);

		// 上一月/下一月

		canvas.drawPath(surface.preMonthBtnPath, surface.monthChangeBtnPaint);

		canvas.drawPath(surface.nextMonthBtnPath, surface.monthChangeBtnPaint);

		// 星期

		float weekTextY = surface.monthHeight + surface.weekHeight*3/4f;

		for (int i=0; i<surface.weekText.length; i++) {
			float weekTextX = i * surface.cellWidth + (surface.cellWidth - surface.weekPaint.measureText(surface.weekText[i]))/2f;
			canvas.drawText(surface.weekText[i], weekTextX, weekTextY, surface.weekPaint);
		}

		// 计算日期
		calculateDate();
		// 按下状态，选择状态背景色
		drawDownOrSelectedBg(canvas);
		// write date number
		// today index
		int todayIndex = -1;

		calendar.setTime(curDate);

		String curYearAndMonth = calendar.get(Calendar.YEAR) + "" + calendar.get(Calendar.MONTH);

		calendar.setTime(today);

		String todayYearAndMonth = calendar.get(Calendar.YEAR) + "" + calendar.get(Calendar.MONTH);

		if (curYearAndMonth.equals(todayYearAndMonth)) {
			int todayNumber = calendar.get(Calendar.DAY_OF_MONTH);
			todayIndex = curStartIndex + todayNumber - 1;
		}

		for (int i=0; i<42; i++) {
			int color = surface.textColor;
			/*
			 * 上个月或者下个月的日期 都是用和边框一样的颜色表示
			 */
			if (isLastMonth(i)) {
				color = surface.borderColor;
			} else if (isNextMonth(i)) {
				color = surface.borderColor;
			}
			if (todayIndex != -1 && i == todayIndex) {
				//恰好就是今天
				color = surface.todayNumberColor;
			}
			if(date[i]>i||(i>28&&date[i]<13)){
				drawCellText(canvas, i, date[i]+"", color, 0);
			}else{
				drawCellText(canvas, i, date[i]+"", color, hasTask[date[i]-1]);
			}
		}
		super.onDraw(canvas);
	}
	//计算日期
	private void calculateDate() {
		//当前时间
		calendar.setTime(curDate);
		//1号
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		//周几
		int dayInWeek = calendar.get(Calendar.DAY_OF_WEEK);
		int monthStart = dayInWeek;
		if (monthStart == 1) {
			monthStart = 8;
		}
		
		monthStart -= 1;
		curStartIndex = monthStart;
		date[monthStart] = 1;

		if (monthStart > 0) {

			calendar.set(Calendar.DAY_OF_MONTH, 0);

			int dayInmonth = calendar.get(Calendar.DAY_OF_MONTH);

			for (int i=monthStart-1; i>=0; i--) {
				date[i] = dayInmonth;
				dayInmonth--;
			}
			calendar.set(Calendar.DAY_OF_MONTH, date[0]);
		}

		showFirstDate = calendar.getTime();
		calendar.setTime(curDate);
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 0);

		int monthDay = calendar.get(Calendar.DAY_OF_MONTH);

		for (int i=1; i<monthDay; i++) {
			date[monthStart + i] = i + 1;
		}

		curEndIndex = monthStart + monthDay;

		for (int i=monthStart + monthDay; i<42; i++) {

			date[i] = i - (monthStart + monthDay) + 1;

		}

		if (curEndIndex < 42) {
			// 显示了下一月的
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}

		calendar.set(Calendar.DAY_OF_MONTH, date[41]);
		showLastDate = calendar.getTime();

	}

	/**
	 * 
	 * @param canvas
	 * @param index
	 * @param text
	 */

	private void drawCellText(Canvas canvas, int index, String text, int color,int task) {

		int x = getXByIndex(index);

		int y = getYByIndex(index);

		surface.datePaint.setColor(color);

		float cellY = surface.monthHeight + surface.weekHeight + (y - 1) * surface.cellHeight + surface.cellHeight * 3/4f;

		float cellX = (surface.cellWidth * (x-1)) + (surface.cellWidth - surface.datePaint.measureText(text))/2f;

		canvas.drawText(text, cellX, cellY-3, surface.datePaint);
		
		Paint paint = new Paint();
		Resources rec = getResources();
		
		int temp = 0;
		if(Integer.valueOf(text)>9){
			temp = 5;
		}
		
		if(task==0)
			return;
		//任务是否完成
		if((task&0x08)==0x08){
			InputStream in1 = rec.openRawResource(R.drawable.done);
			canvas.drawBitmap(BitmapFactory.decodeStream(in1), cellX+temp, cellY, paint);
		}else{
			//紧急任务
			if((task&0x04)==0x04){
				InputStream in4 = rec.openRawResource(R.drawable.red_point);
				canvas.drawBitmap(BitmapFactory.decodeStream(in4), cellX-5+temp, cellY+3, paint);
			}
			//重要任务
			if((task&0x02)==0x02){
				InputStream in3 = rec.openRawResource(R.drawable.yellow_point);
				canvas.drawBitmap(BitmapFactory.decodeStream(in3), cellX+5+temp, cellY+3, paint);
			}
			//普通任务
			if((task&0x01)==0x01){
				InputStream in2 = rec.openRawResource(R.drawable.green_point);
				canvas.drawBitmap(BitmapFactory.decodeStream(in2), cellX+15+temp, cellY+3, paint);
			}
		}
	}

	/**
	 * @param canvas
	 * @param index
	 * @param color
	 */

	private void drawCellBg(Canvas canvas, int index, int color) {

		int x = getXByIndex(index);

		int y = getYByIndex(index);

		surface.cellBgPaint.setColor(color);

		float left = surface.cellWidth * (x - 1) + surface.borderWidth;

		float top = surface.monthHeight + surface.weekHeight + (y - 1) * surface.cellHeight + surface.borderWidth;

		canvas.drawRect(left, top, left + surface.cellWidth - surface.borderWidth, 

				top + surface.cellHeight - surface.borderWidth, surface.cellBgPaint);

	}

	private void drawDownOrSelectedBg(Canvas canvas) {

		// down and not up

		if (downDate != null) {

			drawCellBg(canvas, downIndex, surface.cellDownColor);

		}

		// selected bg color

		if (!selectedEndDate.before(showFirstDate) && !selectedStartDate.after(showLastDate)) {

			int[] section = new int[]{-1, -1};

			calendar.setTime(curDate);

			calendar.add(Calendar.MONTH, -1);

			findSelectedIndex(0, curStartIndex, calendar, section);

			if (section[1] == -1) {

				calendar.setTime(curDate);

				findSelectedIndex(curStartIndex, curEndIndex, calendar, section);

			}

			if (section[1] == -1) {

				calendar.setTime(curDate);

				calendar.add(Calendar.MONTH, 1);

				findSelectedIndex(curEndIndex, 42, calendar, section);

			}

			if (section[0] == -1) {section[0] = 0;}

			if (section[1] == -1) {section[1] = 41;}

			for (int i=section[0]; i<=section[1]; i++) {

				drawCellBg(canvas, i, surface.cellSelectedColor);

			}

		}

	}



	private void findSelectedIndex(int startIndex, int endIndex, Calendar calendar, int[] section) {

		for (int i=startIndex; i<endIndex; i++) {

			calendar.set(Calendar.DAY_OF_MONTH, date[i]);

			Date temp = calendar.getTime();

			//Log.d(TAG, "temp:" + temp.toLocaleString());

			if (temp.compareTo(selectedStartDate) == 0) {

				section[0] = i;

			}

			if (temp.compareTo(selectedEndDate) == 0) {

				section[1] = i;

				return;

			}

		}

	}

	public Date getSelectedStartDate() {

		return selectedStartDate;

	}

	public Date getSelectedEndDate() {

		return selectedEndDate;

	}

	private boolean isLastMonth(int i) {

		if (i < curStartIndex) {

			return true;

		}

		return false;

	}

	private boolean isNextMonth(int i) {

		if (i >= curEndIndex) {

			return true;

		}

		return false;

	}

	private int getXByIndex(int i) {

		return i%7 + 1; // 1 2 3 4 5 6 7

	} 

	private int getYByIndex(int i) {

		return i/7 + 1; // 1 2 3 4 5 6

	}

	// 获得当前应该显示的年月

	private String getYearAndmonth() {

		calendar.setTime(curDate);

		int year = calendar.get(Calendar.YEAR);

		int month = calendar.get(Calendar.MONTH) + 1;

		return year + "年" + month + "月";

	}
	//设置选中的日期
	private void setSelectedDateByCoor(float x, float y) {

		// change month

		if (y < surface.monthHeight) {

			// pre month

			if (x < surface.monthChangeWidth) {

				calendar.setTime(curDate);

				calendar.add(Calendar.MONTH, -1);

				curDate = calendar.getTime();

			} 

			// next month

			else if (x > surface.width - surface.monthChangeWidth) {

				calendar.setTime(curDate);

				calendar.add(Calendar.MONTH, 1);

				curDate = calendar.getTime();

			}

		}

		// cell click down

		if (y > surface.monthHeight + surface.weekHeight) {

			int m = (int) (Math.floor(x/surface.cellWidth) + 1);

			int n = (int) (Math.floor((y - (surface.monthHeight + surface.weekHeight))/new Float(surface.cellHeight)) + 1);

			downIndex = (n-1) * 7 + m - 1;

			//Log.d(TAG, "downIndex:" + downIndex);

			calendar.setTime(curDate);

			if (isLastMonth(downIndex)) {

				calendar.add(Calendar.MONTH, -1);

			} else if (isNextMonth(downIndex)) {

				calendar.add(Calendar.MONTH, 1);

			}

			calendar.set(Calendar.DAY_OF_MONTH, date[downIndex]);

			downDate = calendar.getTime();

		}

		invalidate();

	}

	@Override

	public boolean onTouch(View v, MotionEvent event) {

		switch (event.getAction()) {

		case MotionEvent.ACTION_DOWN:

			setSelectedDateByCoor(event.getX(), event.getY());

			break;

		case MotionEvent.ACTION_UP:

			if (downDate != null) {

				if (!completed) {

					if (downDate.before(selectedStartDate)) {

						selectedEndDate = selectedStartDate;

						selectedStartDate = downDate;

					} else {

						selectedEndDate = downDate;

					}

					completed = true;

				} else {

					selectedStartDate = selectedEndDate = downDate;

					completed = false;

				}

				//Log.d(TAG, "downdate:" + downDate.toLocaleString());

				Log.d(TAG, "start:" + selectedStartDate.toLocaleString());

				Log.d(TAG, "end:" + selectedEndDate.toLocaleString());

				downDate = null;

				invalidate();

			}

			break;

		}

		return true;

	}

	/**

	 * 

	 * 1. 布局尺寸

	 * 2. 文字颜色，大小

	 * 3. 当前日期的颜色，选择的日期颜色 

	 */

	private class Surface {

		public float density;

		public int width; // 整个控件的宽度

		public int height; // 整个控件的高度

		public float monthHeight; // 显示月的高度 

		public float monthChangeWidth; // 上一月、下一月按钮宽度

		public float weekHeight; // 显示星期的高度

		public float cellWidth; // 日期方框宽度

		public float cellHeight; // 日期方框高度

		public float borderWidth;

		public int bgColor = Color.parseColor("#FFFFFF");

		private int textColor = Color.BLACK;

		private int textColorUnimportant = Color.parseColor("#666666");

		private int btnColor = Color.parseColor("#666666");

		private int borderColor = Color.parseColor("#CCCCCC");

		public int todayNumberColor = Color.RED;

		public int cellDownColor = Color.parseColor("#CCFFFF");

		public int cellSelectedColor = Color.parseColor("#99CCFF");

		public Paint borderPaint;

		public Paint monthPaint;

		public Paint weekPaint;

		public Paint datePaint;

		public Paint monthChangeBtnPaint;

		public Paint cellBgPaint;

		public Path boxPath; // 边框路径

		public Path preMonthBtnPath; // 上一月按钮三角形

		public Path nextMonthBtnPath; // 下一月按钮三角形

		public String[] weekText = {"周日","周一", "周二", "周三", "周四", "周五", "周六"};

		public void init() {
			//高度
			float temp = height / 7f;
			
			monthHeight = (float) ((temp + temp*0.3f) * 0.6);

			monthChangeWidth = monthHeight * 1.5f;

			weekHeight = (float) ((temp + temp*0.3f) * 0.4);
			//每个日期的高度=总高度-显示月份的高度-显示星期的高度 分为6份
			cellHeight = (height - monthHeight - weekHeight) / 6f;

			cellWidth = width / 7f;
			//边界
			borderPaint = new Paint();

			borderPaint.setColor(borderColor);

			borderPaint.setStyle(Paint.Style.STROKE);

			borderWidth = (float) (0.5 * density);

			//Log.d(TAG, "borderwidth:" + borderWidth);

			borderWidth = borderWidth < 1 ? 1 : borderWidth;

			borderPaint.setStrokeWidth(borderWidth);
			//月份
			monthPaint = new Paint();

			monthPaint.setColor(textColor);

			monthPaint.setAntiAlias(true);

			float textSize = cellHeight * 0.4f;

			//Log.d(TAG, "text size:" + textSize);

			monthPaint.setTextSize(textSize);

			monthPaint.setTypeface(Typeface.DEFAULT_BOLD);
			//星期
			weekPaint = new Paint();

			weekPaint.setColor(textColorUnimportant);

			weekPaint.setAntiAlias(true);

			float weekTextSize = weekHeight * 0.6f;

			weekPaint.setTextSize(weekTextSize);

			weekPaint.setTypeface(Typeface.DEFAULT_BOLD);
			//日期
			datePaint = new Paint();

			datePaint.setColor(textColor);

			datePaint.setAntiAlias(true);

			float cellTextSize = cellHeight * 0.5f;

			datePaint.setTextSize(cellTextSize);

			//datePaint.setTypeface(Typeface.DEFAULT_BOLD);
			//画线
			boxPath = new Path();

			boxPath.addRect(0, 0, width, height, Direction.CW);

			boxPath.moveTo(0, monthHeight);

			boxPath.rLineTo(width, 0);

			boxPath.moveTo(0, monthHeight + weekHeight);

			boxPath.rLineTo(width, 0);

			for (int i=1; i<6; i++) {

				boxPath.moveTo(0, monthHeight + weekHeight + i*cellHeight);

				boxPath.rLineTo(width, 0);

				boxPath.moveTo(i*cellWidth, monthHeight);

				boxPath.rLineTo(0, height - monthHeight);

			}

			boxPath.moveTo(6*cellWidth, monthHeight);

			boxPath.rLineTo(0, height - monthHeight);
			//上一个月
			preMonthBtnPath = new Path();

			int btnHeight = (int) (monthHeight * 0.6f);

			preMonthBtnPath.moveTo(monthChangeWidth/2f, monthHeight/2f);

			preMonthBtnPath.rLineTo(btnHeight/2f, -btnHeight/2f);

			preMonthBtnPath.rLineTo(0, btnHeight);

			preMonthBtnPath.close();
			//下一个月
			nextMonthBtnPath = new Path();

			nextMonthBtnPath.moveTo(width-monthChangeWidth/2f, monthHeight/2f);

			nextMonthBtnPath.rLineTo(-btnHeight/2f, -btnHeight/2f);

			nextMonthBtnPath.rLineTo(0, btnHeight);

			nextMonthBtnPath.close();

			monthChangeBtnPaint = new Paint();

			monthChangeBtnPaint.setAntiAlias(true);

			monthChangeBtnPaint.setStyle(Paint.Style.FILL_AND_STROKE);

			monthChangeBtnPaint.setColor(btnColor);

			cellBgPaint = new Paint();

			cellBgPaint.setAntiAlias(true);

			cellBgPaint.setStyle(Paint.Style.FILL);

			cellBgPaint.setColor(cellSelectedColor);

		}

	}

}
