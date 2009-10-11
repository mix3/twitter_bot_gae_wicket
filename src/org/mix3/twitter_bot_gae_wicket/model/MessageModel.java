package org.mix3.twitter_bot_gae_wicket.model;

import java.io.Serializable;

import com.google.appengine.api.datastore.Key;

@SuppressWarnings("serial")
public class MessageModel implements Serializable{
	private Key key;
	private String message = "";
	
	public MessageModel(){}
	
	public MessageModel(Key key, String message){
		this.key = key;
		this.message = message;
	}
	
	public Key getKey() {
		return key;
	}
	public void setKey(Key key) {
		this.key = key;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
