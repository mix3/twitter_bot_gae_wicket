package org.mix3.twitter_bot_gae_wicket.service;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withOffset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.mix3.twitter_bot_gae_wicket.model.MessageModel;

import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;
import com.google.inject.Singleton;

@Singleton
public class ServiceImpl implements Service{
	private DatastoreService datastoreService;
	private Twitter twitter;
	
	public ServiceImpl(){
		datastoreService = DatastoreServiceFactory.getDatastoreService();
		twitter = new Twitter("Twitter ID", "Twitter PASS");
	}

	@Override
	public List<MessageModel> getAll() {
		List<MessageModel> list = new ArrayList<MessageModel>();
		List<Entity> entitys = datastoreService.prepare(new Query("message")).asList(withOffset(0));
		for(Entity e : entitys){
			list.add(new MessageModel(e.getKey(), (String)e.getProperty("message")));
		}
		return list;
	}

	@Override
	public void put(MessageModel messageModel) {
		Transaction transaction = datastoreService.beginTransaction();
		try{
			Entity entity = new Entity("message");
			entity.setProperty("message", messageModel.getMessage());
			datastoreService.put(transaction, entity);
			transaction.commit();
		}finally{
			if(transaction.isActive()){
				transaction.rollback();
			}
		}
	}

	@Override
	public void update(MessageModel messageModel) throws EntityNotFoundException {
		Transaction transaction = datastoreService.beginTransaction();
		try{
			if(messageModel.getMessage() == null){
				datastoreService.delete(messageModel.getKey());
			}else{
				Entity entity = datastoreService.get(messageModel.getKey());
				entity.setProperty("message", messageModel.getMessage());
				datastoreService.put(transaction, entity);
			}
			transaction.commit();
		}finally{
			if(transaction.isActive()){
				transaction.rollback();
			}
		}
	}

	@Override
	public void post() throws TwitterException {
		List<Entity> entities = datastoreService.prepare(new Query("message")).asList(withOffset(0));
		if(!entities.isEmpty()){
			Collections.shuffle(entities);
			Entity entity = entities.get(0);
			twitter.updateStatus((String)entity.getProperty("message"));
		}
	}
}
