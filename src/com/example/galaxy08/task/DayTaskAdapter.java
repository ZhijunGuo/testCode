package com.example.galaxy08.task;

import java.util.ArrayList;

import com.example.galaxy08.R;
import com.example.galaxy08.SysApplication;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class DayTaskAdapter extends BaseAdapter {
	
	private ArrayList<Task> tasks;
	
	private LayoutInflater inflater;
	
	public DayTaskAdapter(Context context,ArrayList<Task> tasks){
		inflater = LayoutInflater.from(context);
		this.setTasks(tasks);
	}

	public void setTasks(ArrayList<Task> tasks) {
		if(tasks!=null)
			this.tasks = tasks;
		else
			this.tasks = new ArrayList<Task>();
	}
	
	@Override
	public int getCount() {
		return tasks.size();
	}

	@Override
	public Object getItem(int position) {
		return tasks.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		DayTaskItemHodler h = null;
		Task t = tasks.get(position);
		if(convertView==null){
			convertView = inflater.inflate(R.layout.day_task_item, null);
			h = new DayTaskItemHodler();
			convertView.setTag(h);
			h.name = (TextView)convertView.findViewById(R.id.day_task_item_check);
			h.status = (ImageView)convertView.findViewById(R.id.day_task_item_status);
			h.delayOrAdvance = (ImageView)convertView.findViewById(R.id.day_task_item_delay);
			h.clock = (ImageView)convertView.findViewById(R.id.day_task_item_clock);
			h.level = (ImageView)convertView.findViewById(R.id.day_task_item_level);
			h.time = (TextView)convertView.findViewById(R.id.day_task_item_time);
		}else{
			h = (DayTaskItemHodler)convertView.getTag();
		}
		h.name.setText(t.getName());
		switch (t.getStatus()) {
		case STATUS_CANCEL:
			h.status.setBackgroundResource(R.drawable.task_cancel);
			h.clock.setBackgroundResource(R.drawable.clock_no_color);
			break;
		case STATUS_FINISH:
			h.status.setBackgroundResource(R.drawable.checked);
			h.clock.setBackgroundResource(R.drawable.clock_no_color);
			break;
		case STATUS_COMMIT:
			h.status.setBackgroundResource(R.drawable.gray_checked);
			h.clock.setBackgroundResource(R.drawable.clock_no_color);
			break;
		case STATUS_OFF:
			h.status.setBackgroundResource(R.drawable.unchecked);
			h.clock.setBackgroundResource(R.drawable.clock_color);
			break;
		case STATUS_ON:
			h.status.setBackgroundResource(R.drawable.unchecked);
			h.clock.setBackgroundResource(R.drawable.clock_color);
			break;
		default:
			break;
		}
		if(t.getCommitTime()!=null&&Long.valueOf(t.getCommitTime())>Long.valueOf(t.getEndTime())){
			//任务delay
			h.delayOrAdvance.setBackgroundResource(R.drawable.red_down);
		}else if(t.getCommitTime()!=null&&Long.valueOf(t.getCommitTime())<Long.valueOf(t.getEndTime())){
			h.delayOrAdvance.setBackgroundResource(R.drawable.green_up);
		}else{
			h.delayOrAdvance.setBackgroundDrawable(null);
		}

		h.time.setText(transferTime(t.getEndTime()));
		switch (t.getLevel()) {
		case LEVEL_COMMON:
			h.level.setBackgroundResource(R.drawable.task_green_right);
			break;
		case LEVEL_IMPROTANT:
			h.level.setBackgroundResource(R.drawable.task_yellow_right);
			break;
		case LEVEL_INSTANCY:
			h.level.setBackgroundResource(R.drawable.task_red_right);
			break;
		default:
			break;
		}
		return convertView;
	}
	
	class DayTaskItemHodler{
		ImageView status;
		TextView name;
		ImageView delayOrAdvance;
		ImageView clock;
		TextView time;
		ImageView level;
	}
	
	private String transferTime(String time){
		long t = Long.valueOf(time);
		int hour = (int)(t%(24*3600*1000))/(3600*1000);
		hour = hour+8>=24?hour+8-24:hour+8;
		int min = (int)(t%(3600*1000))/(60*1000);
		if(min<10)
			return String.valueOf(hour)+":0"+String.valueOf(min);
		else
			return String.valueOf(hour)+":"+String.valueOf(min);
	}

}
