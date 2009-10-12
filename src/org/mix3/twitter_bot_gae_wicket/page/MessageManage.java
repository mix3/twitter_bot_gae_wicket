package org.mix3.twitter_bot_gae_wicket.page;

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.mix3.twitter_bot_gae_wicket.model.MessageModel;
import org.mix3.twitter_bot_gae_wicket.service.Service;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.inject.Inject;

public class MessageManage extends MyAbstractWebPage{
	@Inject
	private Service service;
	private List<MessageModel> list = service.getAll();
	private MessageModel input = new MessageModel();
	
    @SuppressWarnings("serial")
	public MessageManage(PageParameters parameters) {
    	super(parameters);
    	
		UserService userService = UserServiceFactory.getUserService();
		add(new ExternalLink("logout", userService.createLogoutURL("/manage")));
		
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
        add(feedback.setOutputMarkupId(true));
        
		final WebMarkupContainer container = new WebMarkupContainer("container");
		add(container.setOutputMarkupId(true));
		
		IDataProvider<MessageModel> provider = new IDataProvider<MessageModel>(){
			@Override
			public Iterator<? extends MessageModel> iterator(int first, int count) {
				return service.get(first, count).iterator();
			}
			@Override
			public IModel<MessageModel> model(MessageModel messageModel) {
				return new Model<MessageModel>(messageModel);
			}
			@Override
			public int size() {
				return service.count();
			}
			@Override
			public void detach() {
			}
		};
		
		DataView<MessageModel> view = new DataView<MessageModel>("list", provider, 10){
			@Override
			protected void populateItem(final Item<MessageModel> item) {
				item.setDefaultModel(new CompoundPropertyModel<MessageModel>(item.getModelObject()));
				item.add(new Label("key"));
				item.add(new AjaxEditableLabel<String>("message"){
					@Override
					protected void onSubmit(AjaxRequestTarget target) {
						super.onSubmit(target);
						try {
							service.update(item.getModelObject());
							list.clear();
							list.addAll(service.getAll());
							target.addComponent(container);
							target.addComponent(feedback);
						} catch (EntityNotFoundException e) {
							error("DB Error!");
							target.addComponent(feedback);
						}
					}
				});
			}
		};
		container.add(view);
		container.add(new PagingNavigator("paging", view));
//		container.add(new ListView<MessageModel>("list", list){
//			@Override
//			protected void populateItem(final ListItem<MessageModel> item) {
//				item.setDefaultModel(new CompoundPropertyModel<MessageModel>(item.getModelObject()));
//				item.add(new Label("key"));
//				item.add(new AjaxEditableLabel<String>("message"){
//					@Override
//					protected void onSubmit(AjaxRequestTarget target) {
//						super.onSubmit(target);
//						try {
//							service.update(item.getModelObject());
//							list.clear();
//							list.addAll(service.getAll());
//							target.addComponent(container);
//							target.addComponent(feedback);
//						} catch (EntityNotFoundException e) {
//							error("DB Error!");
//							target.addComponent(feedback);
//						}
//					}
//				});
//			}
//		});
//		
		Form<MessageModel> form = new Form<MessageModel>("message");
		add(form.setOutputMarkupId(true));
		form.add(new RequiredTextField<String>("message", new PropertyModel<String>(input, "message")));
		form.add(new AjaxButton("submit", form){
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.addComponent(feedback);
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				service.put(input);
				list.clear();
				list.addAll(service.getAll());
				input.setMessage("");
				target.addComponent(container);
				target.addComponent(form);
				target.addComponent(feedback);
			}
		});
	}
}