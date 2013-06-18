package com.example.galaxy08.chat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.example.galaxy08.R;
import com.example.galaxy08.entity.ChatMessage;
import com.example.galaxy08.entity.ChatMessage.MESSAGE_MEDIA_TYPE;
import com.example.galaxy08.entity.ChatMessage.MESSAGE_TYPE;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChatHistoryListAdapter extends BaseAdapter {

	private ArrayList<ChatMessage> messages;
	
	private LayoutInflater inflater;
	
	public ChatHistoryListAdapter(Context context,ArrayList<ChatMessage> messages){
		this.inflater = LayoutInflater.from(context);
		this.setMessages(messages);
	}
	
	public void setMessages(ArrayList<ChatMessage> messages) {
		if(messages!=null)
			this.messages = messages;
		else{
			this.messages = new ArrayList<ChatMessage>();
		}
	}
	
	@Override
	public int getCount() {
		return messages.size();
	}

	@Override
	public Object getItem(int position) {
		return messages.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		
		ChatHistoryItemHodler h = null;
		
		ChatMessage message = messages.get(position);
		
		if(convertView==null){
			convertView = inflater.inflate(R.layout.chat_history_item, null);
			h = new ChatHistoryItemHodler();
			h.name = (TextView)convertView.findViewById(R.id.chat_history_item_name);
			h.time = (TextView)convertView.findViewById(R.id.chat_history_item_time);
			h.content = (TextView)convertView.findViewById(R.id.chat_history_item_content);
			h.image = (ImageView)convertView.findViewById(R.id.chat_history_item_image);
			convertView.setTag(h);
		}else{
			h = (ChatHistoryItemHodler)convertView.getTag();
		}
		if(message.getType()==MESSAGE_TYPE.MESSAGE_SEND){
			h.name.setText(message.getUserid());
		}else{
			h.name.setText(message.getTargetid());
		}
		h.time.setText(getDate(message.getTimestamp()));
		
		if(message.getMediatype()==MESSAGE_MEDIA_TYPE.MEDIA_AUDIO){
			h.image.setVisibility(View.GONE);
			h.content.setVisibility(View.VISIBLE);
			h.content.setCompoundDrawablesWithIntrinsicBounds(R.drawable.audio_message, 0, 0, 0);
			h.content.setText(message.getAudioTime()+"'");
		}else if(message.getMediatype()==MESSAGE_MEDIA_TYPE.MEDIA_TEXT){
			h.content.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
			h.content.setText(message.getContent());
			h.content.setVisibility(View.VISIBLE);
			h.image.setVisibility(View.GONE);
		}else if(message.getMediatype()==MESSAGE_MEDIA_TYPE.MEDIA_IMAGE){
			//h.content.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
			h.content.setVisibility(View.GONE);
			h.image.setVisibility(View.VISIBLE);
			h.image.setImageBitmap(BitmapFactory.decodeFile(message.getMediaCache()));
		}
		return convertView;
	}
	
	class ChatHistoryItemHodler{
		TextView name;
		TextView time;
		TextView content;
		ImageView image;
	}

	public void dataSetChangeAll(ArrayList<ChatMessage> ms) {
		this.messages.addAll(ms);
		notifyDataSetChanged();
	}
	
	private String getDate(long time){
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return f.format(new Date(time));
	}
	
	public void removeMessage(ChatMessage message){
		messages.remove(message);
		notifyDataSetChanged();
	}

	public void replaceData(ArrayList<ChatMessage> data) {
		messages = data;
		notifyDataSetChanged();
	}

}
