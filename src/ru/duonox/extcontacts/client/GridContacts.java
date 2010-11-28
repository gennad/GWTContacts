package ru.duonox.extcontacts.client;


import java.util.ArrayList;
import java.util.List;



import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;

public class GridContacts extends LayoutContainer {

	List<ColumnConfig> configs;
	ColumnConfig column;
	ListStore<ContactsModel> store;
	ColumnModel cm;
	ContentPanel cp;
	Grid<ContactsModel> grid;
	CheckBoxSelectionModel<ContactsModel> sm;
	ToolBar toolBar;
	

	public GridContacts() {
		this("");
	}
	public GridContacts(String gridName) {
		setLayout(new FitLayout());
		setHeight("600");

		this.configs = new ArrayList<ColumnConfig>();
		if(gridName != ""){
			this.sm = new CheckBoxSelectionModel<ContactsModel>();
			this.configs.add(sm.getColumn());
		}

		this.column = new ColumnConfig();
		this.column.setId("name");
		this.column.setHeader((gridName == "") ? "Groop names" : "Contacts");
		this.column.setWidth((gridName == "") ? 170 : 200);
		this.column.setAlignment(HorizontalAlignment.LEFT);
		this.configs.add(this.column);

		if(gridName == ""){
			this.column = new ColumnConfig();
			this.column.setId("number");
			this.column.setWidth(30);
			this.column.setAlignment(HorizontalAlignment.RIGHT);
			this.configs.add(this.column);
		}


		this.store = new ListStore<ContactsModel>();

		this.cm = new ColumnModel(this.configs);

		this.cp = new ContentPanel();
		this.cp.setLayout(new FitLayout());
		this.cp.setBodyBorder(false);
		this.cp.setIcon(Resources.ICONS.table());
		this.cp.setHeading((gridName == "") ? "Группы" : gridName);
		this.cp.setButtonAlign(HorizontalAlignment.CENTER);
		
		if(gridName == ""){
			this.toolBar = new ToolBar();
			this.toolBar.add(new Button("Add", Resources.ICONS.add()));
			this.toolBar.add(new SeparatorToolItem());
			this.toolBar.add(new Button("Remove", Resources.ICONS.delete()));
			this.toolBar.add(new SeparatorToolItem());
			this.cp.setTopComponent(toolBar);
		}

		this.grid = new Grid<ContactsModel>(this.store, this.cm);
		this.grid.setStyleAttribute("borderTop", "none");
		this.grid.setAutoExpandColumn("name");
		this.grid.setBorders(true);
		this.grid.setStripeRows(true);
		if(gridName != ""){
			this.grid.setSelectionModel(this.sm);
			this.grid.addPlugin(this.sm);
		}
		this.cp.add(this.grid);
		
		add(this.cp);
	}

}