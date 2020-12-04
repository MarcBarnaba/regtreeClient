package it.map1920.regtreeClient;

import it.map1920.regtreeClient.ui.ViewLoader;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * L'applicazione implementa la GUI di un client per la previsione di un classvalue
 * fornendo al server il nome del dataset da cui apprendere.
 * @author marcbarnaba
 *
 */
public class Main extends Application {

	private static Connection conn;
	private static Settings sett;
	private static ViewLoader vl;

	public static void main(String[] args) {

		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {

		sett = new Settings();
		conn = Connection.getInstance(sett.getServerAddr(), sett.getPortNumber());
		vl = new ViewLoader(primaryStage);
		
		primaryStage.setTitle("Regression Tree Learner");
		primaryStage.setResizable(false);
		
		if (sett.getWelcomeEnabled()) {
			vl.loadWelcome();
			primaryStage.show();

		} else {
			if (conn.tryConnection()) {
				vl.loadHome(false);
				primaryStage.show();

			} else {
				vl.loadPreferences();
				vl.createErrAlert("Non sono riuscito a contattare il server.\n"
						+ "Controlla le impostazioni di connessione"
						+ " e verifica che il server sia in ascolto.");

			}
		}

	}

	public static ViewLoader getViewLoader() {
		return vl;
	}

	public static Connection getConnectionInstance() {
		return conn;
	}

	public static Settings getSettings() {
		return sett;
	}

}