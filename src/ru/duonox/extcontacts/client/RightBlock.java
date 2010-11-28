package ru.duonox.extcontacts.client;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

public class RightBlock extends LayoutContainer {

	ContentPanel vp;
	TabPanel panel;
	TabItem view;
	TabItem edit;
	
	
	protected RightBlock() {

		setLayout(new FitLayout());
		
		this.vp = new ContentPanel();
		this.vp.setHeaderVisible(false);
		this.vp.setLayout(new FitLayout());

		this.panel = new TabPanel();
		this.panel.setPlain(true);
		this.panel.setResizeTabs(true);
		this.panel.setTabScroll(true);
		this.panel.setAnimScroll(true);


		this.view = new TabItem("View");
		this.view.setIcon(Resources.ICONS.table());
		this.view.addStyleName("pad-text");
		this.view.addText("Just a plain old tab");
		this.panel.add(this.view);

	    this.edit = new TabItem("Edit");
	    this.edit.setIcon(Resources.ICONS.side_list());
	    this.edit.addStyleName("pad-text");
	    this.edit.addText("Just a plain old tab with an icon");
	    this.panel.add(this.edit);

	    this.vp.add(this.panel);
		add(this.vp);

	}

}