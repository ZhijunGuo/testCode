package com.example.galaxy08.http.response;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.galaxy08.entity.User;
import com.example.galaxy08.http.ResponseType;
import com.example.galaxy08.http.model.BaseResponse;

@SuppressWarnings("serial")
public class FriendsResponse extends BaseResponse implements ResponseType, Parser<FriendsResponse>{
	
	private ArrayList<Long> deletes;
	
	private ArrayList<User> friends;
	
	@Override
	public FriendsResponse parser(JSONObject jsonObject) {
		JSONObject dataObject = jsonObject.optJSONObject("data");
		if(dataObject!=null){
			JSONArray deleteArray = dataObject.optJSONArray("delete");
			if(deleteArray!=null){
				deletes = new ArrayList<Long>();
				for(int i=0;i<deleteArray.length();i++){
					long temp = deleteArray.optLong(i);
					deletes.add(Long.valueOf(temp));
				}
			}
			JSONArray friendsArray = dataObject.optJSONArray("friends");
			if(friendsArray!=null){
				friends = new ArrayList<User>();
				for(int i = 0;i<friendsArray.length();i++){
					User temp = new User();
					try {
						temp.parser(friendsArray.getJSONObject(i));
					} catch (JSONException e) {
						e.printStackTrace();
					}
					friends.add(temp);
				}
			}
		}
		return this;
	}

	public ArrayList<Long> getDeletes() {
		return deletes;
	}

	public void setDeletes(ArrayList<Long> deletes) {
		this.deletes = deletes;
	}

	public ArrayList<User> getFriends() {
		return friends;
	}

	public void setFriends(ArrayList<User> friends) {
		this.friends = friends;
	}

	
}