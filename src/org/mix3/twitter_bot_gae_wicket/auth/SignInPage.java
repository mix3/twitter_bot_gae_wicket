package org.mix3.twitter_bot_gae_wicket.auth;

import org.apache.wicket.markup.html.pages.RedirectPage;

import com.google.appengine.api.users.UserServiceFactory;

public class SignInPage extends RedirectPage {
	public SignInPage(){
		super(UserServiceFactory.getUserService().createLoginURL("/redirect"));
	}
}
