package ru.duonox.extcontacts.client;


import java.util.ArrayList;

import javax.persistence.criteria.CaseExpression;


import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class Body extends Viewport {

	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	private final LoaderAsync loader = GWT.create(Loader.class);

    ContentPanel cp;
	ContentPanel panelGroups;
	ContentPanel panelContacts;
	ContentPanel panelMain;
	TableData tdGroups;
	TableData tdContacts;
	TableData tdMain;
	GridContacts gridGroups;
	GridContacts gridContacts;
	private String email = "ayk@lemon-computing.com";
	private String pass = "qaz123wsx123";

	ToolBar toolBar;
	//Main main;
	Text footer;
	HorizontalPanel hp;

	private final Listener<MessageBoxEvent> listener = new Listener<MessageBoxEvent>() {

		public void handleEvent(MessageBoxEvent ce) {
			Button btn = ce.getButtonClicked();

			Info.display("MessageBox", "The '{0}' button was pressed", btn.getText());
        }
    };


	public Body() {

		setLayout(new FitLayout());
		setStyleAttribute("padding", "5px");

		this.cp = new ContentPanel();
		this.cp.setHeading("Extended Contacts v0.2");
		this.cp.setFrame(true);

		this.panelGroups = new ContentPanel();
		this.panelGroups.setLayout(new FitLayout());
		this.panelGroups.setHeaderVisible(false);
		this.panelGroups.setIntStyleAttribute("minWidth", 200);
		this.panelGroups.setBorders(true);

		this.panelContacts = new ContentPanel();
		this.panelContacts.setLayout(new FitLayout());
		this.panelContacts.setHeaderVisible(false);
		this.panelContacts.setIntStyleAttribute("minWidth", 200);
		this.panelContacts.setBorders(true);
//		this.panelContacts.setHeight("700");

		this.panelMain = new ContentPanel();
		this.panelMain.setLayout(new FitLayout());
		this.panelMain.setBorders(true);
		this.panelMain.setHeight("700");

		this.toolBar = new ToolBar();
		this.toolBar.add(new Button("Add", Resources.ICONS.add()));
		this.toolBar.add(new SeparatorToolItem());
		this.toolBar.add(new Button("Remove", Resources.ICONS.delete()));
		this.toolBar.add(new SeparatorToolItem());

		this.footer = new Text();
		this.footer.setText("<div style='width: 100%; text-align: center; color: gray;'>2010 © Lemon Computing Ltd.</div>");

		this.cp.setTopComponent(toolBar);
		this.cp.setBottomComponent(footer);

		//this.main = new Main();

		//this.cp.add(this.main);

		this.hp = new HorizontalPanel();
		this.hp.setLayout(new FitLayout());
		this.hp.setTableWidth("100%");
		this.hp.setTableHeight("99%");

		this.tdGroups = new TableData();
		this.tdGroups.setWidth("20%");
		this.tdContacts = new TableData();
		this.tdContacts.setWidth("20%");
		this.tdMain = new TableData();

		this.hp.add(this.panelGroups, this.tdGroups);
		this.hp.add(this.panelContacts, this.tdContacts);
		this.hp.add(this.panelMain, this.tdMain);
		this.cp.add(this.hp);
		add(this.cp);


		this.gridGroups = new GridContacts();
		this.gridContacts = new GridContacts("All");
		this.panelGroups.add(this.gridGroups);
		this.panelContacts.add(this.gridContacts);
		RequestMan("groups", "");
		RequestMan("contacts", "All");
		RightBlock rightBlock = new RightBlock();
		this.panelMain.add(rightBlock);

	}

	public void RequestMan(String requestType, String textToServer){
		
	    loader.requestServer(requestType, this.email, this.pass, textToServer, new AsyncCallback<ArrayList<String[]>>() {

			public void onFailure(Throwable caught) {
	    		// Show the RPC error message to the user
	    		MessageBox.alert("Error", SERVER_ERROR, listener);
	    	}

	    	public void onSuccess(ArrayList<String[]> result) {
	    		AppMan(result);
	    	}
	    });

	}

	private void AppMan(ArrayList<String[]> result){

		if (result.get(result.size() - 1)[0].equals("groups")) {
			this.gridGroups.store.removeAll();
			this.gridGroups.store.add(GridData.getContacts("", result));
		} else if (result.get(result.size() - 1)[0].equals("contacts")) {
			this.gridContacts.store.removeAll();
			this.gridContacts.store.add(GridData.getContacts("All", result));
		}
	}
}