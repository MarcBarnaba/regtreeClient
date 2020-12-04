package it.map1920.regtreeClient.controller;

import java.util.ArrayList;
import java.util.List;

import it.map1920.regtreeClient.Connection;
import it.map1920.regtreeClient.Main;
import it.map1920.regtreeClient.ui.MenuActions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class PredPhaseController {

	@FXML
	private Text question;
	@FXML
	private VBox optionsContainer;
	@FXML
	private Button nextBtn;
	@FXML
	private Button againBtn;
	@FXML
	private Button homeBtn;
	@FXML
	private MenuBar menuBar;
	@FXML
	private ImageView image;

	private String attrName;
	private List<String> options; // Nodi percorribili
	private String classValue;
	private Connection conn;
	private ToggleGroup group; // Per rendere una sola opzione alla volta selezionabile

	public void initialize() {

		@SuppressWarnings("unused")
		MenuActions ma = new MenuActions(menuBar);

		// Rende impossibile aprire un documento nuovo o recente durante la fase di previsione
		menuBar.getMenus().get(0).getItems().get(0).setDisable(true);
		menuBar.getMenus().get(0).getItems().get(1).setDisable(true);

		againBtn.setVisible(false);

		conn = Main.getConnectionInstance();
		options = new ArrayList<String>();
		group = new ToggleGroup();

		optionsContainer.setOnKeyPressed((event) -> {
			if (event.getCode() == KeyCode.ENTER) {
				onNextBtnPressed(null);
			}
		});

		classValue = null;
		conn.startPrediction();
		refreshInfo();
	}

	/**
	 * Decompone una risposta del server per popolare la lista di nodi percorribili
	 * e per acquisire il classvalue quando il messaggio lo specifica.
	 * 
	 * @return true se va a buon fine, false altrimenti
	 */

	private boolean parseAnswer() {

		String toParse = conn.getAnswer();
		if (toParse.startsWith("QUERY!")) {
			if (toParse.endsWith("OK")) {
				classValue = conn.getClassValue();
			}
			toParse = toParse.substring(6);
			toParse.lines().forEach(s -> options.add(s));

		} else {
			return false;
		}

		return true;
	}

	/**
	 * Aggiorna le informazioni visualizzate utilizzando la lista di String che
	 * rappresenta i nodi percorribili. Estrae il nome dell'attributo in esame e
	 * visualizza il classValue quando la previsione termina.
	 */
	private void refreshInfo() {

		optionsContainer.getChildren().clear();
		options.clear();
		group.getToggles().clear();

		if (parseAnswer()) {

			if (options.size() > 1) {
				attrName = options.get(0).split(":")[1];
				attrName = attrName.split("[><=]")[0];
				question.setText("Quale valore per " + attrName + "?");
				for (String o : options) {
					o = o.split(":")[1];
					RadioButton option = new RadioButton(o);
					option.setToggleGroup(group);
					optionsContainer.getChildren().add(option);
				}

			} else {

				showClass(classValue);
			}

		} else {
			Main.getViewLoader().createErrAlert("Qualcosa è andato storto");
		}
	}

	/**
	 * Visualizza il classValue effettuando modifiche alle proprietà dei nodi della
	 * Scena.
	 * 
	 * @param classValue
	 */
	private void showClass(String classValue) {

		question.setText("Previsione terminata!");

		Text subtitle = new Text("Class = ");
		subtitle.setFont(Font.font("Nunito", FontWeight.MEDIUM, 20));
		subtitle.setFill(question.getFill());
		subtitle.setText(subtitle.getText() + classValue);
		
		image.setImage(new Image("/it/map1920/regtreeClient/images/complete.png"));

		optionsContainer.setAlignment(Pos.BOTTOM_LEFT);
		optionsContainer.setPrefHeight(85);
		optionsContainer.setSpacing(0);
		optionsContainer.setPadding(new Insets(0, 0, 0, 60));
		optionsContainer.getChildren().add(subtitle);

		nextBtn.setDisable(true);
		nextBtn.setVisible(false);

		againBtn.setDisable(false);
		againBtn.setVisible(true);

		homeBtn.setVisible(true);
		homeBtn.setDisable(false);
	}

	/**
	 * Quando il bottone Next viene premuto, invia l'opzione scelta al server.
	 * Visualizza un messaggio di errore se non è selezionata alcuna opzione.
	 * 
	 * @param event
	 */
	@FXML
	protected void onNextBtnPressed(ActionEvent event) {

		RadioButton rb = (RadioButton) group.getSelectedToggle();
		if (rb != null) {
			conn.sendReply(optionsContainer.getChildren().indexOf(rb));
			refreshInfo();
		} else {
			Main.getViewLoader().createErrAlert("Nessuna opzione selezionata!");
		}
		rb = null;

	}

	/**
	 * Quando il pulsante Again viene premuto, inizia una nuova previsione, basata
	 * sullo stesso dataset.
	 * 
	 * @param event
	 */
	@FXML
	protected void onAgainBtnPressed(ActionEvent event) {

		conn.startPrediction();
		classValue = null;
		refreshInfo();

		image.setImage(new Image("/it/map1920/regtreeClient/images/undraw_data_processing_yrrv.png"));
		
		nextBtn.setDisable(false);
		nextBtn.setVisible(true);
		againBtn.setDisable(true);
		againBtn.setVisible(false);
		optionsContainer.setAlignment(Pos.CENTER_LEFT);
		optionsContainer.setSpacing(10);
		optionsContainer.setPadding(new Insets(0, 0, 0, 40));
		optionsContainer.setPrefHeight(224);
		homeBtn.setVisible(false);
		homeBtn.setDisable(true);

	}

	/**
	 * Quando il pulsante Home viene premuto, chiude la connessione e ne crea
	 * un'altra per permettere all'utente di scegliere un nuovo dataset.
	 * Successivamente carica la Scena Home.
	 * 
	 * @param event
	 */
	@FXML
	protected void onHomeBtnPressed(ActionEvent event) {

		conn.endPrediction();
		if (conn.tryConnection()) {
			Main.getViewLoader().loadHome(false);
		}
	}

}
