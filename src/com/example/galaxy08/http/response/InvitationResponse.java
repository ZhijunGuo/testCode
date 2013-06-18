package com.example.galaxy08.http.response;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.galaxy08.entity.Invite;
import com.example.galaxy08.http.ResponseType;
import com.example.galaxy08.http.model.BaseResponse;

@SuppressWarnings("serial")
public class InvitationResponse extends BaseResponse implements ResponseType, Parser<InvitationResponse>{
	
	private ArrayList<Invite> invites;
	
	@Override
	public InvitationResponse parser(JSONObject jsonObject) {
		JSONArray dataArray = jsonObject.optJSONArray("data");
		if(dataArray!=null){
			invites = new ArrayList<Invite>();
			for(int i = 0;i<dataArray.length();i++){
				Invite in = new Invite();
				try {
					in.parser(dataArray.getJSONObject(i));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				invites.add(in);
			}
		}
		return this;
	}

	public ArrayList<Invite> getInvites() {
		return invites;
	}

	public void setInvites(ArrayList<Invite> invites) {
		this.invites = invites;
	}
	
}