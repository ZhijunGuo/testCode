package com.example.galaxy08.adapter;

import java.util.ArrayList;

import com.example.galaxy08.R;
import com.example.galaxy08.entity.Feed;
import com.example.galaxy08.tool.Tool;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FeedDetailAdapter extends BaseAdapter {

	private ArrayList<Feed> news;
	
	private LayoutInflater inflater;
	
	private OnClickListener listener;
	
	public void setNews(ArrayList<Feed> news) {
		if(news!=null)
			this.news = news;
		else
			this.news = new ArrayList<Feed>();
	}
	
	public FeedDetailAdapter(Context context,ArrayList<Feed> news,OnClickListener l){
		inflater = LayoutInflater.from(context);
		this.setNews(news);
		listener = l;
	}
	
	@Override
	public int getCount() {
		return news.size();
	}

	@Override
	public Object getItem(int position) {
		return news.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		NewContentHodler h = null;
		if(convertView==null){
			convertView = inflater.inflate(R.layout.feed_item, null);
			h = new NewContentHodler();
			convertView.setTag(h);
			h.avatar = (ImageView)convertView.findViewById(R.id.feed_item_avatar);
			h.name = (TextView)convertView.findViewById(R.id.news_content_item_name);
			h.transmitCount = (TextView)convertView.findViewById(R.id.news_content_item_transmit_count);
			h.reviewCount = (TextView)convertView.findViewById(R.id.news_content_item_review_count);
			h.content = (TextView)convertView.findViewById(R.id.news_content_item_content);
			h.time = (TextView)convertView.findViewById(R.id.news_content_item_time);
		}else{
			h = (NewContentHodler)convertView.getTag();
		}
		Feed item = news.get(position);
		h.avatar.setOnClickListener(listener);
		h.avatar.setTag(item);
		h.name.setText(item.getPublisher_name());
		h.content.setText(item.getContent());
		//h.transmitCount.setText(String.valueOf(item.get));
		//h.reviewCount.setText(String.valueOf(item.getReviewCount()));
		h.time.setText(Tool.parseDateTime(Long.valueOf(item.getTime())));
		return convertView;
	}
	
	public void setDataAndNotify(ArrayList<Feed> fs){
		setNews(fs);
		this.notifyDataSetChanged();
	}
	
	class NewContentHodler{
		ImageView avatar;
		TextView name;
		TextView transmitCount;
		TextView reviewCount;
		TextView content;
		TextView time;
	}

}
