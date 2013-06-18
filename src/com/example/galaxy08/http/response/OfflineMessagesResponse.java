package com.example.galaxy08.http.response;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.galaxy08.entity.ChatMessage;
import com.example.galaxy08.entity.Invite;
import com.example.galaxy08.http.ResponseType;
import com.example.galaxy08.http.model.BaseResponse;

@SuppressWarnings("serial")
public class OfflineMessagesResponse extends BaseResponse implements ResponseType, Parser<OfflineMessagesResponse>{
	
	private ArrayList<ChatMessage> messages;
	
	private ArrayList<ChatMessage> groupMessages;
	
	@Override
	public OfflineMessagesResponse parser(JSONObject jsonObject) {
		JSONObject dataObject = jsonObject.optJSONObject("data");
		if(dataObject!=null){
			JSONArray messageArray = dataObject.optJSONArray("letters");
			if(messageArray!=null){
				messages = new ArrayList<ChatMessage>();
				for(int i = 0;i<messageArray.length();i++){
					ChatMessage message = new ChatMessage();
					try {
						message.parser(messageArray.getJSONObject(i));
					} catch (JSONException e) {
						e.printStackTrace();
					}
					messages.add(message);
				}
			}
			JSONArray groupMessageArray = dataObject.optJSONArray("groupLetters");
			if(groupMessageArray!=null){
				groupMessages = new ArrayList<ChatMessage>();
				for(int i = 0;i<groupMessageArray.length();i++){
					ChatMessage message = new ChatMessage();
					try {
						message.parser(groupMessageArray.getJSONObject(i));
					} catch (JSONException e) {
						e.printStackTrace();
					}
					groupMessages.add(message);
				}
			}
		}
		return this;
	}

	public ArrayList<ChatMessage> getMessages() {
		return messages;
	}

	public void setMessages(ArrayList<ChatMessage> messages) {
		this.messages = messages;
	}

	public ArrayList<ChatMessage> getGroupMessages() {
		return groupMessages;
	}

	public void setGroupMessages(ArrayList<ChatMessage> groupMessages) {
		this.groupMessages = groupMessages;
	}
	
}