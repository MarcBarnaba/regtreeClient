package it.map1920.regtreeClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Gestisce attributi relativi alla connessione con il server e fornisce 
 * metodi per stabilire la connessione, inviare il nome del dataset e effettuare 
 * la fase di previsione.
 * E' possibile avere una sola istanza attiva perchè il client può essere connesso ad un 
 * solo server alla volta.
 *
 */
public class Connection {

	private static Connection instance = null;
	
	private String serverName;
	private int port;
	private InetAddress addr;
	private Socket socket = null;
	private ObjectOutputStream out = null;
	private ObjectInputStream in = null;	
	
	private Connection(String serverName, int port) {
		this.serverName = serverName;
		this.port = port;
	}
	
	/**
	 * Se non esiste un'istanza della classe, ne crea una e la restituisce.
	 * @param serverName nome host
	 * @param port numero di porta
	 * @return Istanza della classe Connection
	 */
	public static Connection getInstance(String serverName, int port) {
		
		if(instance == null) {
			instance = new Connection(serverName, port);
		}
		return instance;
	}
	
	/**
	 * Tenta di stabilire una connessione al server specificato 
	 * alla creazione dell'istanza.
	 * @return true connessione stabilita, false altrimenti
	 */
    public boolean tryConnection() {
    	
		try {
			addr = InetAddress.getByName(serverName);
		} catch (UnknownHostException e) {
			return false;
		}

		try {
			socket = new Socket(addr, Integer.valueOf(port).intValue());
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());

		} catch (IOException e) {
			return false;
		}
		
		return true;
    }
    
    /**
     * Invia al server il nome della tabella da cui apprendere specificando il luogo dove cercarla.
     * 
     * @param decision Specifica la posizione della tabella: 1 - Tabella da Database;
     * 2 - Tabella da Archivio
     * @param tableName Nome della tabella da cui apprendere i dati
     * @return true se l'apprendimento va a buon fine, false se la tabella non esiste o qualcosa va storto
     */
    public boolean learnTree(int decision, String tableName) {
    	
    	String answer;
    	
		try {

			if (decision == 1) {
				System.out.println("Starting data acquisition phase!");

				out.writeObject(0);
				out.writeObject(tableName);
				answer = in.readObject().toString();
				
				if (!answer.equals("OK")) {
					Main.getViewLoader().createErrAlert("Tabella inesistente");
					return false;
				}

				System.out.println("Starting learning phase!");
				out.writeObject(1);

			} else {
				out.writeObject(2);
				out.writeObject(tableName);

			}

			answer = in.readObject().toString();
			if (!answer.equals("OK")) {
				System.out.println(answer);
				return false;
			}
			
		} catch (NullPointerException e) {
			Main.getViewLoader().createErrAlert("Tabella inesistente");
			return false;
		}  catch (Exception e) {
			Main.getViewLoader().createErrAlert(e.toString());
			return false;
		}
		
		return true;
    }
    
    /**
     * Invia al server il segnale che indica l'inizio della fase di previsione.
     * 
     * @return true segnale inviato correttamente, false altrimenti
     */
    public boolean startPrediction() {
    	
		try {
			out.writeObject(3);
			System.out.println("Starting prediction phase!");
			return true;
			
		} catch (Exception e) {
			Main.getViewLoader().createErrAlert(e.toString());
			return false;
		}
    }
    
    /**
     * Invia al server il segnale che indica la fine della fase di previsione.
     * 
     * @return true segnale inviato correttamente, false altrimenti
     */
    public boolean endPrediction() {
    	
		try {
			out.writeObject(4);
			System.out.println("Ending prediction phase!");
			return true;
			
		} catch (Exception e) {
			Main.getViewLoader().createErrAlert(e.toString());
			return false;
		}
    }
    
    /**
     * 
     * @return Una Stringa così composta: 
     * <p>QUERY![Lista di nodi percorribili] quando non si è raggiunto un nodo foglia</p>
     * <p>QUERY!OK quando è stato raggiunto un nodo foglia</p>
     * La stringa "Error" se qualcosa va storto.
     */
    public String getAnswer() {
    	
    	try {
    		
			return in.readObject().toString() + "!" + in.readObject().toString();
			
		} catch (Exception e) {
			Main.getViewLoader().createErrAlert(e.toString());
			return "Error";
		}
    }
    
    /**
     * Da usare dopo che getAnswer() ha restituito QUERY!OK.
     * @return il classValue risultato della previsione, sotto forma di oggetto di tipo String. 
     * La stringa "Error" se qualcosa va storto.
     */
    public String getClassValue() {
    	
    	try {
    		
			return in.readObject().toString();
			
		} catch (Exception e) {
			Main.getViewLoader().createErrAlert(e.toString());
			return "Error";
		}
    }
    
    /**
     * Invia al server il nodo scelto per proseguire la fase di previsione.
     * 
     * @param option 0..n_opzioni disponibili
     * @return true segnale inviato correttamente, false altrimenti
     */
    public boolean sendReply(int option) {
    	
		try {
			out.writeObject(option);
		} catch (IOException e) {
			Main.getViewLoader().createErrAlert(e.toString());
			return false;
		}
		return true;

    }
    
}
