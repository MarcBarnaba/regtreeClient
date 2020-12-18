package it.map1920.regtreeClient.ui;

import java.io.IOException;
import java.util.Optional;

import it.map1920.regtreeClient.Main;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;




/**
 * Fornisce metodi per inserire le Scene nello Stage dell'applicazione.
 * Fornisce metodi per creare Alert di Errore, Informazione, Conferma.
 * 
 *
 */
public class ViewLoader {

	private Stage stage = null;
	
	public ViewLoader(Stage stage) {
		this.stage = stage;
		
		//stage.getIcons().add(new Image("resources/images/AppIcon.png"));

		this.stage.setOnCloseRequest(confirmCloseEH);
	}
	
	/**
	 * Gestisce la richiesta di chiusura dell'app creando un Alert di conferma.
	 */
	private EventHandler<WindowEvent> confirmCloseEH = event -> {
	        Alert closeConfirmation = new Alert(
	                Alert.AlertType.CONFIRMATION,
	                "Are you sure you want to exit?"
	        );
	        Button exitButton = (Button) closeConfirmation.getDialogPane().lookupButton(
	                ButtonType.OK
	        );
	        exitButton.setText("Exit");
	        closeConfirmation.setHeaderText("Confirm Exit");
	        Optional<ButtonType> closeResponse = closeConfirmation.showAndWait();
	        if (!ButtonType.OK.equals(closeResponse.get())) {
	            event.consume();
	        }
	    };
	
	/**
	 * Invia una richiesta di chiusura dell'applicazione.
	 */
	public void askClose() {
		
		stage.fireEvent(new WindowEvent( stage, WindowEvent.WINDOW_CLOSE_REQUEST) );
	}
	
	/**
	 * Crea un Alert per un errore.
	 * 
	 * @param content messaggio di errore
	 */
	public void createErrAlert(String content) {
		
		Alert a = new Alert(AlertType.ERROR);
		
		a.setHeaderText("Errore");
		a.setContentText(content);

		a.show();
	}
	
	/**
	 * Crea un alert per comunicare un'informazione all'utente.
	 * 
	 * @param header titolo alert
	 * @param content messaggio
	 */
	public void createInfoAlert(String header, String content) {
		
		Alert a = new Alert(AlertType.INFORMATION);
		
		a.setHeaderText(header);
		a.setContentText(content);
		
		a.show();

	}
	
	/**
	 * Crea un alert per chiedere conferma all'utente.
	 * @param header titolo alert
	 * @param content messaggio
	 */
	public void createConfirmAlert(String header, String content) {
		
		Alert a = new Alert(AlertType.CONFIRMATION);
		
		a.setHeaderText(header);
		a.setContentText(content);
		
		a.show();

	}
	
	/**
	 * Carica la Scena Welcome nello Stage dell'app.
	 */
    public void loadWelcome() {
    	
    	BorderPane root = new BorderPane();
        FXMLLoader loader = null;
		try {
			loader = new FXMLLoader(Main.class.getResource("/it/map1920/regtreeClient/view/Welcome.fxml"));
			root = (BorderPane) loader.load();
    		stage.setScene(new Scene(root));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
    }
    
    /**
     * Carica la Scena Home nello Stage dell'app.
     * 
     * @param newWindow true se la Scena va caricata in una nuova finestra, false altrimenti.
     */
    public void loadHome(boolean newWindow) {
		
		try {
		    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/it/map1920/regtreeClient/view/Home.fxml"));
		            Parent root = (Parent) fxmlLoader.load();
		            if(newWindow) {
		            	Stage newStage = new Stage();
			            newStage.setTitle("Regression Tree Learner");
			    		newStage.setScene(new Scene(root));
			    		newStage.show();
			    		stage = newStage;
		            } else {
		            	stage.setScene(new Scene(root));
		            }

		    } catch(Exception e) {
		       e.printStackTrace();
		      }
		
	}
    
    /**
     * Carica la Scena PredPhase nello Stage dell'app.
     */
	public void loadPredView() {
		
		try {
		    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/it/map1920/regtreeClient/view/PredPhase.fxml"));
		            Parent root = (Parent) fxmlLoader.load();
		    		stage.setScene(new Scene(root));


		    } catch(Exception e) {
		       e.printStackTrace();
		      }
		
	}
	
	/**
	 * Carica la Scena Preferences in una nuova finestra.
	 */
	public void loadPreferences() {
		try {
		    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/it/map1920/regtreeClient/view/Preferences.fxml"));
		            Parent root = (Parent) fxmlLoader.load();
		            Stage newStage = new Stage();
		            newStage.setTitle("Impostazioni");
		    		newStage.setScene(new Scene(root));
		    		newStage.show();


		    } catch(Exception e) {
		       e.printStackTrace();
		      }
		
	}
	
	/**
	 * Carica la guida utente
	 */
	public void loadUserGuide() {
		WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.load( getClass().getResource("/it/map1920/regtreeClient/usermanual.html").toString() );

        Stage newStage = new Stage();
        newStage.setTitle("Manuale");
		newStage.setScene(new Scene(webView));
		newStage.show();
	}
    
}
