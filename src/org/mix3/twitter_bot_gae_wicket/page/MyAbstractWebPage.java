package org.mix3.twitter_bot_gae_wicket.page;

import org.apache.wicket.PageParameters;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;

@AuthorizeInstantiation({Roles.ADMIN})
public class MyAbstractWebPage extends WebPage{
	@SuppressWarnings("serial")
	public MyAbstractWebPage(PageParameters parameters){
		add(new HeaderContributor(new IHeaderContributor(){
			@Override
			public void renderHead(IHeaderResponse response) {
				response.renderString("<link type=\"image/x-icon\" rel=\"shortcut icon\" href=\"/favicon.ico\" />");
			}
		}));
	}
}
