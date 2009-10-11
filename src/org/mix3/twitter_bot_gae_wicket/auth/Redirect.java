package org.mix3.twitter_bot_gae_wicket.auth;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.WebPage;
import org.mix3.twitter_bot_gae_wicket.page.Manage;

public class Redirect extends WebPage{
	public Redirect(){
    	if(!continueToOriginalDestination()){
//    		setRedirect(true);
    		throw new RestartResponseException(Manage.class);
    	}
	}
}
