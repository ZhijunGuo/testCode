package com.example.galaxy08.adapter;

import java.util.List;

import com.example.galaxy08.R;
import com.example.galaxy08.R.array;
import com.example.galaxy08.R.id;
import com.example.galaxy08.R.layout;
import com.example.galaxy08.entity.Group;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/*
 * 
 */

public class MessageMainListAdapter extends BaseExpandableListAdapter{

	private LayoutInflater inflater;
	private String[] mGroupStrings;
	private List<List<Group>> mData;
	
	public MessageMainListAdapter(Context context,List<List<Group>> mData){
		this.inflater = LayoutInflater.from(context);
		this.mData = mData;
		mGroupStrings = context.getResources().getStringArray(R.array.groupname);
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
			convertView = inflater.inflate(R.layout.message_main_list_item, null);
			h = new ChildViewHodler();
			h.icon = (ImageView)convertView.findViewById(R.id.message_main_list_item_icon);
			h.groupName = (TextView)convertView.findViewById(R.id.message_main_list_item_groupname);
			h.groupMessageCount = (TextView)convertView.findViewById(R.id.message_main_list_item_groupmessagecount);
			h.groupLastMessageTime = (TextView)convertView.findViewById(R.id.message_main_list_item_grouptime);
			h.groupLastMessage = (TextView)convertView.findViewById(R.id.message_main_list_item_grouplastmessage);
			convertView.setTag(h);
		}else{
			h = (ChildViewHodler)convertView.getTag();
		}
		Group item = (Group)getChild(groupPosition, childPosition);
		h.groupName.setText(item.getGroupName());
		h.groupMessageCount.setText("20");
		h.groupLastMessageTime.setText(item.getGroupLastMessageTime());
		h.groupLastMessage.setText(item.getGroupLastMessage());
		return convertView;
	}
	
	class ChildViewHodler{
		ImageView icon;
		TextView groupName;
		TextView groupMessageCount;
		TextView groupLastMessageTime;
		TextView groupLastMessage;
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
		h.groupName.setText(mGroupStrings[groupPosition]);
		
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
