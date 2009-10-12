package org.mix3.twitter_bot_gae_wicket.service;

import java.util.List;

import org.mix3.twitter_bot_gae_wicket.model.MessageModel;

import twitter4j.TwitterException;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.inject.ImplementedBy;

@ImplementedBy(ServiceImpl.class)
public interface Service{
	public List<MessageModel> getAll();
	public List<MessageModel> get(int offset, int limit);
	public int count();
	public void put(MessageModel messageModel);
	public void update(MessageModel messageModel) throws EntityNotFoundException;
	public void post() throws TwitterException;
}
