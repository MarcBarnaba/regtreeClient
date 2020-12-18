package it.map1920.regtreeClient.ui;

import it.map1920.regtreeClient.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.stage.Stage;

/**
 * Definisce le azioni disponibili all'interno della barra dei menu presente in ogni Scena.
 *
 */
public class MenuActions {
	
	private MenuBar menuBar;
	private ViewLoader vl;
	

	public MenuActions(MenuBar menuBar) {
		this.menuBar = menuBar;
		vl = Main.getViewLoader();
		initialize();
	}
	
	private void initialize() {
		
		menuBar.getMenus().clear();
		
        // create a menu 
        Menu menuFile = new Menu("File");
        Menu menuRecent = new Menu("Open recent..."); 
        Menu menuHelp = new Menu("Help");
  
        // create menuitems 
        SeparatorMenuItem sep = new SeparatorMenuItem();
        MenuItem mf1 = new MenuItem("New"); 
        MenuItem mf2 = new MenuItem("Quit");
        MenuItem mh1 = new MenuItem("Preferences");
        MenuItem mh2 = new MenuItem("User Guide");

  
        // add menu items to menu 
        menuFile.getItems().add(mf1);
        menuFile.getItems().add(menuRecent);
        Main.getSettings().getRecent().forEach(s -> {
        	if(!s.isEmpty()) {
        		MenuItem item = new MenuItem(s);
            	menuRecent.getItems().add(item);

        	}
        });
        menuFile.getItems().add(sep);
        menuFile.getItems().add(mf2);
        
        menuHelp.getItems().add(mh1);
        menuHelp.getItems().add(mh2);


        
        // create events for menu items 
        
        //New
        EventHandler<ActionEvent> eventmf1 = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) 
            { 
            	vl.loadHome(true);
            	Stage stage = (Stage) menuBar.getScene().getWindow();
            	stage.close();
            } 
        }; 
        
        //Quit
        EventHandler<ActionEvent> eventmf2 = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) 
            { 
            	vl.askClose();
            } 
        };
        
        //Preferences
        EventHandler<ActionEvent> eventmh1 = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) 
            { 
            	vl.loadPreferences();
            } 
        }; 

		// User Guide
		EventHandler<ActionEvent> eventmh2 = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				vl.loadUserGuide();
			}
		};

		// add event to recent tables
		mf1.setOnAction(eventmf1);
		menuRecent.getItems().forEach(item -> {
			item.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					if (Main.getConnectionInstance().learnTree(1, item.getText())) {
						Main.getViewLoader().loadPredView();
					}
				}
			});
		});
		mf2.setOnAction(eventmf2);

		mh1.setOnAction(eventmh1);
		mh2.setOnAction(eventmh2);

		// add menu to menubar
		menuBar.getMenus().add(menuFile);
		menuBar.getMenus().add(menuHelp);

	}
}
