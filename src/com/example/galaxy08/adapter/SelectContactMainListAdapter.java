package com.example.galaxy08.adapter;

import java.util.ArrayList;
import java.util.List;

import com.example.galaxy08.R;
import com.example.galaxy08.R.array;
import com.example.galaxy08.R.id;
import com.example.galaxy08.R.layout;
import com.example.galaxy08.entity.Department;
import com.example.galaxy08.entity.Group;
import com.example.galaxy08.entity.SelectUser;
import com.example.galaxy08.entity.User;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.SlidingDrawer;
import android.widget.TextView;
/*
 * 
 */

public class SelectContactMainListAdapter extends BaseExpandableListAdapter{

	private LayoutInflater inflater;
	//private String[] mGroupStrings;
	private List<List<SelectUser>> mData;
	private OnCheckedChangeListener listener;
	
	private OnClickListener clickListener;
	
	private ArrayList<Department> ds;
	
	public SelectContactMainListAdapter(Context context,List<List<SelectUser>> mData,
			OnClickListener clickListener,ArrayList<Department> ds){
		this.inflater = LayoutInflater.from(context);
		setmData(mData);
		setDs(ds);
		this.clickListener = clickListener; 
	}
	
	public void setDs(ArrayList<Department> ds) {
		if(ds!=null)
			this.ds = ds;
		else
			this.ds = new ArrayList<Department>();
	}
	
	public List<List<SelectUser>> getmData() {
		return mData;
	}

	public void setmData(List<List<SelectUser>> mData) {
		if(mData!=null)
			this.mData = mData;
		else
			this.mData = new ArrayList<List<SelectUser>>();
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return mData.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ChildViewHodler h = null;
		if(convertView==null){
			convertView = inflater.inflate(R.layout.select_contact_main_list_item, null);
			h = new ChildViewHodler();
			h.avatar = (ImageView)convertView.findViewById(R.id.select_contact_main_list_item_image);
			h.contactName = (TextView)convertView.findViewById(R.id.select_contact_main_list_item_name);
			h.contactPosition = (TextView)convertView.findViewById(R.id.select_contact_main_list_item_position);
			h.check = (CheckBox)convertView.findViewById(R.id.select_contact_main_list_item_check);
			convertView.setTag(h);
		}else{
			h = (ChildViewHodler)convertView.getTag();
		}
		SelectUser item = (SelectUser)getChild(groupPosition, childPosition);
		User contact = item.getContact();
		h.contactName.setText(contact.getUser_name());
		h.contactPosition.setText(contact.getPosition());
		h.check.setChecked(item.isSelect());
		//h.check.setOnCheckedChangeListener(listener);
		h.check.setOnClickListener(clickListener);
		h.check.setTag(item);
		return convertView;
	}
	
	class ChildViewHodler{
		ImageView avatar;
		TextView contactName;
		TextView contactPosition;
		CheckBox check;
	}
	
	public void removeContact(){
		
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return mData.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mData.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return mData.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		GroupViewHodler h = null;
		if(convertView==null){
			convertView = inflater.inflate(R.layout.message_main_list_item02, null);
			h = new GroupViewHodler();
			h.groupName = (TextView)convertView.findViewById(R.id.message_main_groupname);
			convertView.setTag(h);
		}else{
			h = (GroupViewHodler)convertView.getTag();
		}
		h.groupName.setText(ds.get(groupPosition).getDepartment_name());
		
		return convertView;
	}
	
	class GroupViewHodler{
		TextView groupName;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
