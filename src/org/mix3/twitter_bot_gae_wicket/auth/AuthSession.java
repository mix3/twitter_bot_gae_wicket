package org.mix3.twitter_bot_gae_wicket.auth;

import org.apache.wicket.Request;
import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.authorization.strategies.role.Roles;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class AuthSession extends AuthenticatedWebSession{
	public AuthSession(Request request) {
		super(request);
	}

	@Override
	public boolean authenticate(String userid, String password) {
		return UserServiceFactory.getUserService().isUserAdmin();
	}

	@Override
	public Roles getRoles() {
		User user = UserServiceFactory.getUserService().getCurrentUser();
		if(user == null){
			return null;
		}else if(UserServiceFactory.getUserService().isUserAdmin()){
			return new Roles(Roles.ADMIN);
		}else{
			return new Roles(Roles.USER);
		}
	}

}
