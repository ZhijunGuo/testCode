package com.example.galaxy08.http.response;

import org.json.JSONObject;

import com.example.galaxy08.http.ResponseType;

public interface Parser<T extends ResponseType> {
	public T parser(JSONObject object);
}
