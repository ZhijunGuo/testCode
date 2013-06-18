package com.example.galaxy08.chat;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.galaxy08.R;
import com.example.galaxy08.chat.AsyncMultiMediaDownloader.Callback;
import com.example.galaxy08.entity.ChatMessage;

public class ChatListAdapter extends BaseAdapter {
	
	private ArrayList<ChatMessage> cs = null;
	
	private LayoutInflater inflater;
	
	private AsyncMultiMediaDownloader loader;
	
	public void setCs(ArrayList<ChatMessage> cs) {
		if(cs!=null)
			this.cs = cs;
		else
			this.cs = new ArrayList<ChatMessage>();
	}
	
	public ChatListAdapter(Context c,ArrayList<ChatMessage> cs){
		inflater = LayoutInflater.from(c);
		this.setCs(cs);
		loader = new AsyncMultiMediaDownloader();
	}

	@Override
	public int getCount() {
		return cs.size();
	}

	public ArrayList<ChatMessage> getCs(){
		return this.cs;
	}
	
	@Override
	public Object getItem(int position) {
		return cs.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void dataSetChange(ChatMessage c){
		this.cs.add(c);
		this.notifyDataSetChanged();
	}
	
	public void dataSetChangeAll(ArrayList<ChatMessage> cs){
		this.setCs(cs);
		this.notifyDataSetChanged();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ChatMessage c = cs.get(position);
		Handler h = null;
		if(convertView==null){
			convertView = inflater.inflate(R.layout.chat_item, null);
			
			h = new Handler();

			convertView.setTag(h);
		}else{
			h = (Handler)convertView.getTag();
		}
		if(c.getType().equals(ChatMessage.MESSAGE_TYPE.MESSAGE_SEND)){
			h.icon = (ImageView)convertView.findViewById(R.id.chat_item_right_icon);
			h.content = (TextView)convertView.findViewById(R.id.chat_item_right_text);
			h.image = (ImageView)convertView.findViewById(R.id.chat_item_image_my);
			
			h.bar = (ProgressBar)convertView.findViewById(R.id.chat_item_progress_my);
			h.bar.setVisibility(View.VISIBLE);
			h.sendStatus = (ImageView)convertView.findViewById(R.id.chat_item_send_fail);
			h.sendStatus.setVisibility(View.GONE);
			
			h.waitFileLoad = (ProgressBar)convertView.findViewById(R.id.chat_item_wait_file_load);
			h.waitFileLoad.setVisibility(View.GONE);
			
			convertView.findViewById(R.id.chat_item_left_icon).setVisibility(View.GONE);
			convertView.findViewById(R.id.chat_item_left_text).setVisibility(View.GONE);
			convertView.findViewById(R.id.chat_item_image_target).setVisibility(View.GONE);
		}else{
			h.icon = (ImageView)convertView.findViewById(R.id.chat_item_left_icon);
			h.content = (TextView)convertView.findViewById(R.id.chat_item_left_text);
			h.image = (ImageView)convertView.findViewById(R.id.chat_item_image_target);
			h.sendStatus = (ImageView)convertView.findViewById(R.id.chat_item_send_fail);
			h.sendStatus.setVisibility(View.GONE);
			
			h.waitFileLoad = (ProgressBar)convertView.findViewById(R.id.chat_item_wait_file_load);
			h.waitFileLoad.setVisibility(View.GONE);
			
			convertView.findViewById(R.id.chat_item_right_icon).setVisibility(View.GONE);
			convertView.findViewById(R.id.chat_item_right_text).setVisibility(View.GONE);
			convertView.findViewById(R.id.chat_item_image_my).setVisibility(View.GONE);
		}
		h.icon.setVisibility(View.VISIBLE);
		h.content.setVisibility(View.VISIBLE);
		h.image.setVisibility(View.GONE);
		
		switch (c.getStatus()) {
		case STATUS_SEND_SUCCESS:
			if(h.bar!=null){
				h.bar.setVisibility(View.GONE);
			}
			break;
		case STATUS_SEND_FAIL:
			h.bar.setVisibility(View.GONE);
			h.sendStatus.setVisibility(View.VISIBLE);
			break;
			
		default:
			break;
		}
		
		if(c.getMediatype()==ChatMessage.MESSAGE_MEDIA_TYPE.MEDIA_AUDIO){
			h.content.setCompoundDrawablesWithIntrinsicBounds(R.drawable.audio_message, 0, 0, 0);
			h.content.setText(c.getAudioTime()+"'");
			
			if(c.getMediaCache()==null){
				//语音文件还没有下载完毕
				h.waitFileLoad.setVisibility(View.VISIBLE);
				h.content.setVisibility(View.GONE);
				
				loader.loadFile(c, new Callback() {
					
					@Override
					public void fileLoaded(ChatMessage message) {
						notifyDataSetChanged();
					}
				});
			}else{
				h.content.setVisibility(View.VISIBLE);
				h.waitFileLoad.setVisibility(View.GONE);
			}
			
		}else if(c.getMediatype()==ChatMessage.MESSAGE_MEDIA_TYPE.MEDIA_TEXT){
			h.content.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
			h.content.setText(c.getContent());
		}else if(c.getMediatype()==ChatMessage.MESSAGE_MEDIA_TYPE.MEDIA_IMAGE){
			//h.image.setVisibility(View.VISIBLE);
			h.content.setVisibility(View.GONE);
			if(c.getMediaCache()==null){
				//对方发来图片，但是还没有下载完成，显示 progressbar
				h.image.setVisibility(View.GONE);
				h.waitFileLoad.setVisibility(View.VISIBLE);
				//添加到 下载任务队列
				loader.loadFile(c,new Callback() {
					@Override
					public void fileLoaded(ChatMessage message) {
						notifyDataSetChanged();
					}
				});
			}else{
				//下载完成 正常显示
				h.image.setVisibility(View.VISIBLE);
				h.waitFileLoad.setVisibility(View.GONE);
				Bitmap bm = BitmapFactory.decodeFile(c.getMediaCache());
				h.image.setImageBitmap(bm);
			}
		}
		
		return convertView;
	}
	
	class Handler{
		ImageView icon;
		TextView content;
		ImageView image;
		ProgressBar bar;
		ProgressBar waitFileLoad;
		ImageView sendStatus;
	}

	//在 listview 的顶部增加新数据
	public void addArrayListAtFrist(ArrayList<ChatMessage> list){
		this.cs.addAll(0, list);
		this.notifyDataSetChanged();
	}
	
	public void addArrayListMessage(ArrayList<ChatMessage> list) {
		this.cs.addAll(list);
		this.notifyDataSetChanged();
	}

	public void addMessage(ChatMessage message) {
		this.cs.add(message);
		this.notifyDataSetChanged();
	}

}
