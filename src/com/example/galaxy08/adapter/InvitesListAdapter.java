package com.example.galaxy08.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.example.galaxy08.R;
import com.example.galaxy08.entity.Invite;

public class InvitesListAdapter extends BaseAdapter {

	private ArrayList<Invite> invites;
	
	private LayoutInflater inflater;
	
	public InvitesListAdapter(Context context,ArrayList<Invite> arrayList){
		this.inflater = LayoutInflater.from(context);
		this.setInvites(arrayList);
	}

	public void setInvites(ArrayList<Invite> invites) {
		if(invites!=null)
			this.invites = invites;
		else
			this.invites = new ArrayList<Invite>();
	}
	
	public ArrayList<Invite> getInvites() {
		return invites;
	}
	
	@Override
	public int getCount() {
		return invites.size();
	}

	@Override
	public Object getItem(int position) {
		return invites.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		
		final Invite invite = invites.get(position);
		
		if(convertView==null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.info_window_item, null);
			holder.checkbox = (CheckBox)convertView.findViewById(R.id.info_window_item_checkbox);
			holder.company = (TextView)convertView.findViewById(R.id.info_window_item_company);
			holder.title = (TextView)convertView.findViewById(R.id.info_window_item_position);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		holder.checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				invite.setChecked(isChecked);
			}
		});
		holder.company.setText("公司:"+invite.getCompany());
		holder.title.setText("职位:"+invite.getTitle());
		
		return convertView;
	}

	class ViewHolder{
		CheckBox checkbox;
		TextView company;
		TextView title;
	}
	
}
