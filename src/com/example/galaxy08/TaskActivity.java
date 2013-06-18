package com.example.galaxy08;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.galaxy08.task.DayTaskAdapter;
import com.example.galaxy08.task.Task;
import com.example.galaxy08.task.Task.TASK_LEVEL;
import com.example.galaxy08.task.Task.TASK_STATUS;

public class TaskActivity extends Activity implements OnClickListener,
OnCheckedChangeListener,android.widget.RadioGroup.OnCheckedChangeListener{
	
	private LinearLayout taskMain;
	
	private int hasTask[];
	
	private View year_button;
	private View month_button;
	private View dayTask;
	private ListView todayList;
	private ListView tomorrowList;
	private ExpandableListView allList;
	
	private DayTaskAdapter adapter;
	
	private PopupWindow setWindow;
	
	private boolean isDayTask = false;
	
	private TextView setButton;
	
	private LinearLayout searchEditParent;
	
	private RadioGroup taskDayRadioGroup;
	
	private void setupView(){
		dayTask = findViewById(R.id.task_day);
		
		todayList = (ListView)findViewById(R.id.task_main_today_task);
		tomorrowList = (ListView)findViewById(R.id.task_main_tomorrow_task);
		allList = (ExpandableListView)findViewById(R.id.task_main_all_task);
		
		year_button = findViewById(R.id.task_year_button);
		month_button = findViewById(R.id.task_month_button);
		((LinearLayout)findViewById(R.id.task_date_button)).setOnClickListener(this);
		setButton = (TextView)findViewById(R.id.task_day_setting);//.setOnClickListener(this);
		setButton.setOnClickListener(this);
		LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View taskSetView = mLayoutInflater.inflate(R.layout.set_window, null,true);
		setWindow = new PopupWindow(taskSetView, LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT, true);
		setWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.task_popwindow_back));
		setWindow.setTouchInterceptor(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
					setWindow.dismiss();
					return true;
				}
				return false;
			}
		});
		((CheckBox)taskSetView.findViewById(R.id.set_window_common)).setOnCheckedChangeListener(this);
		((CheckBox)taskSetView.findViewById(R.id.set_window_important)).setOnCheckedChangeListener(this);
		((CheckBox)taskSetView.findViewById(R.id.set_window_instancy)).setOnCheckedChangeListener(this);
		
		taskDayRadioGroup = (RadioGroup)findViewById(R.id.task_day);
		taskDayRadioGroup.setOnCheckedChangeListener(this);
		
		adapter = new DayTaskAdapter(this, getData(String.valueOf(System.currentTimeMillis())));
		todayList.setAdapter(adapter);
		searchEditParent = (LinearLayout)findViewById(R.id.search_task_parent);
	}
	
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task);
		taskMain = (LinearLayout)findViewById(R.id.task_main);
		float density = getResources().getDisplayMetrics().density;
		hasTask = new int[31];
		int[] day_of_month = {31,28,31,30,31,30,31,31,30,31,30,31};
		Date d = new Date();
		day_of_month[1]+=isLeap(d.getYear())?1:0;
		
		setupView();
		
		for(int i = 0;i<day_of_month[d.getMonth()];i++){
			// 0000 0000 一个证书的最低八位 
			//如果没有任务 这个整数是0  普通任务 0x01; 重要任务 0x02;紧急任务 0x04
			// 任务全部完成 0x08
			hasTask[i] = 0;
			ArrayList<Task> ts = getData(String.valueOf(System.currentTimeMillis()/(24*3600*1000)));
			if(ts==null)
				continue;
			for(Task t:ts){
				switch (t.getLevel()) {
				case LEVEL_COMMON:
					hasTask[i]|=0x01;
					break;
				case LEVEL_IMPROTANT:
					hasTask[i]|=0x02;
					break;
				case LEVEL_INSTANCY:
					hasTask[i]|=0x04;
					break;
				default:
					break;
				}
				switch (t.getStatus()){
				//如果任务已经完成了
				case STATUS_FINISH:
					hasTask[i]|=0x08;
					break;
				default:
					hasTask[i]&=0x07;
					break;
				}
			}
		}
		
		EggCalendar c = new EggCalendar(this,hasTask);
		taskMain.addView(c, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
		
	};
	
	private boolean isLeap(int year){
		return ((year%4==0&&year%100!=0)||(year%400==0));
	}
	
	/*
	 * 查询 以结束时间为标准的任务(最小单位:天)
	 * 例如： 某一项任务 以 2013/05/14 15:00 结束
	 * 该项任务应该归纳入 2013/05/14 的任务
	 */
	private ArrayList<Task> getData(String endTime){
		ArrayList<Task> tasks = null;
		//查询数据库
		tasks = new ArrayList<Task>();
		Task t1 = new Task();
		t1.setName("睁眼");
		t1.setContent("睡醒了，睁开眼睛");
		t1.setEndTime(String.valueOf(System.currentTimeMillis()-6*3600*1000));
		t1.setLevel(TASK_LEVEL.LEVEL_COMMON);
		t1.setCommitTime(String.valueOf(System.currentTimeMillis()-7*3600*1000));
		t1.setStatus(TASK_STATUS.STATUS_COMMIT);
		tasks.add(t1);
		
		Task t2 = new Task();
		t2.setName("呼吸");
		t2.setContent("每一秒钟最重要的事情");
		t2.setEndTime(String.valueOf(System.currentTimeMillis()-4*3600*1000));
		t2.setLevel(TASK_LEVEL.LEVEL_IMPROTANT);
		t2.setStatus(TASK_STATUS.STATUS_FINISH);
		t2.setCommitTime(String.valueOf(System.currentTimeMillis()-3*3600*1000));
		tasks.add(t2);
		
		Task t3 = new Task();
		t3.setName("去厕所");
		t3.setContent("这个很紧急");
		t3.setEndTime(String.valueOf(System.currentTimeMillis()-2*3600*1000));
		t3.setLevel(TASK_LEVEL.LEVEL_INSTANCY);
		t3.setStatus(TASK_STATUS.STATUS_ON);
		tasks.add(t3);
		
		Task t4 = new Task();
		t4.setName("出门带钥匙");
		t4.setContent("忘记带钥匙很惨的");
		t4.setEndTime(String.valueOf(System.currentTimeMillis()+1*3600*1000));
		t4.setLevel(TASK_LEVEL.LEVEL_INSTANCY);
		t4.setStatus(TASK_STATUS.STATUS_OFF);
		tasks.add(t4);
		
		Task t5 = new Task();
		t5.setName("衣衫整洁");
		t5.setContent("这是个良好的习惯");
		t5.setEndTime(String.valueOf(System.currentTimeMillis()+2*3600*1000));
		t5.setLevel(TASK_LEVEL.LEVEL_COMMON);
		t5.setStatus(TASK_STATUS.STATUS_CANCEL);
		tasks.add(t5);
		
		return tasks;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.task_date_button:
			if(!isDayTask){
				year_button.setVisibility(View.GONE);
				month_button.setVisibility(View.GONE);
				taskMain.setVisibility(View.GONE);
				
				dayTask.setVisibility(View.VISIBLE);
				todayList.setVisibility(View.VISIBLE);
				//taskDayRadioGroup.
				onCheckedChanged(taskDayRadioGroup,taskDayRadioGroup.getCheckedRadioButtonId());
				isDayTask = true;
			}else{
				year_button.setVisibility(View.VISIBLE);
				month_button.setVisibility(View.VISIBLE);
				taskMain.setVisibility(View.VISIBLE);
				
				dayTask.setVisibility(View.GONE);
				todayList.setVisibility(View.GONE);
				searchEditParent.setVisibility(View.GONE);
				tomorrowList.setVisibility(View.GONE);
				allList.setVisibility(View.GONE);
				isDayTask = false;
			}
			break;

		case R.id.task_day_setting:
			if(setWindow.isShowing()){
				setWindow.dismiss();
			}else{
				int[] location = new int[2];
				setButton.getLocationOnScreen(location);
				setWindow.showAtLocation(setButton, Gravity.TOP, location[0], 
						location[1]+setButton.getHeight()-2);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.set_window_common:
			
			break;
		case R.id.set_window_instancy:
			
			break;
		case R.id.set_window_important:
			
			break;

		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.task_today_button:
			Toast.makeText(this, "今天", Toast.LENGTH_SHORT).show();
			tomorrowList.setVisibility(View.GONE);
			allList.setVisibility(View.GONE);
			searchEditParent.setVisibility(View.GONE);
			todayList.setVisibility(View.VISIBLE);
			break;
		case R.id.task_tomorrow_button:
			Toast.makeText(this, "明天", Toast.LENGTH_SHORT).show();
			tomorrowList.setVisibility(View.VISIBLE);
			allList.setVisibility(View.GONE);
			searchEditParent.setVisibility(View.GONE);
			todayList.setVisibility(View.GONE);
			break;
		case R.id.task_all_button:
			Toast.makeText(this, "全部", Toast.LENGTH_SHORT).show();
			tomorrowList.setVisibility(View.GONE);
			allList.setVisibility(View.VISIBLE);
			searchEditParent.setVisibility(View.VISIBLE);
			todayList.setVisibility(View.GONE);
			break;

		default:
			break;
		}
	}

}
