package com.example.galaxy08.adapter;

import java.util.ArrayList;

import com.example.galaxy08.R;
import com.example.galaxy08.entity.FeedSession;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/*
 * 
 */
public class FeedAdapter extends BaseAdapter {

	ArrayList<FeedSession> feeds = null;
	
	private LayoutInflater inflater = null;
	
	public FeedAdapter(Context context, ArrayList<FeedSession> feeds){
		this.setfeeds(feeds);
		this.inflater = LayoutInflater.from(context);
	}
	
	public void setfeeds(ArrayList<FeedSession> feeds) {
		if(feeds!=null)
			this.feeds = feeds;
		else
			this.feeds = new ArrayList<FeedSession>();
	}
	
	public void setFeedsAndNotify(ArrayList<FeedSession> feeds){
		setfeeds(feeds);
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return feeds.size();
	}

	@Override
	public Object getItem(int position) {
		return feeds.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewItemHodler h = null;
		if(convertView==null){
			convertView = inflater.inflate(R.layout.main_new_item, null);
			h = new ViewItemHodler();
			h.image = (ImageView)convertView.findViewById(R.id.new_item_image);
			h.name = (TextView)convertView.findViewById(R.id.new_item_name);
			h.messageCount = (TextView)convertView.findViewById(R.id.new_item_message_count);
			h.lastMessage = (TextView)convertView.findViewById(R.id.new_item_last_message);
			convertView.setTag(h);
			
		}else{
			h = (ViewItemHodler)convertView.getTag();
		}
		
		FeedSession n = feeds.get(position);
//		if(n.getImgPath()==null&&n.getSystemImage()!=0){
//			h.image.setBackgroundResource(n.getSystemImage());
//		}
		if(n.getFeedsession_name().equals("销售组")){
			h.image.setBackgroundResource(R.drawable.group);
		}
		else if(n.getFeedsession_name().equals("公司动态")){
			h.image.setBackgroundResource(R.drawable.company_feeds);
		}else{
			h.image.setBackgroundResource(R.drawable.attention);
		}
		h.name.setText(n.getFeedsession_name());
		h.messageCount.setText(String.valueOf(n.getMessageCount()));
		if(n.getLastFeed()!=null){
			h.lastMessage.setText(n.getLastFeed().getContent());
		}
		
		return convertView;
	}
	
	class ViewItemHodler{
		ImageView image;
		TextView name;
		TextView messageCount;
		TextView lastMessage;
	}

}
