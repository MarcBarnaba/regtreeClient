package it.map1920.regtreeClient;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * Gestisce le impostazioni utente con l'ausilio della classe Preferences.
 * Conserva anche 4 delle tabelle inserite di recente dall'utente.
 * Fornisce metodi per get e set.
 * 
 * @author marcbarnaba
 * @see java.util.prefs.Preferences
 */
public class Settings {
	
	private Preferences prefs;
	
	private String ID1 = "Welcome screen enabled";
    private String ID2 = "Indirizzo del Server";
    private String ID3 = "Port Number";
    private String R1 = "Recent table 1";
    private String R2 = "Recent table 2";
    private String R3 = "Recent table 3";
    private String R4 = "Recent table 4";
    private List<String> recent;
	
	public Settings() {
		recent = new ArrayList<String>();
		prefs = Preferences.userRoot().node(this.getClass().getName());
	}
	
	public void setWelcomeEnabled(boolean isEnabled) {
		prefs.putBoolean(ID1, isEnabled);
	}
	
	public void setServerAddr(String serverAddr) {
		prefs.put(ID2, serverAddr);
	}
	
	public void setPortNumber(int port) {
		prefs.putInt(ID3, port);
	}
	
	public boolean getWelcomeEnabled() {
		return prefs.getBoolean(ID1, true);
	}
	
	public String getServerAddr() {
		return prefs.get(ID2,"localhost");
	}
	
	public int getPortNumber() {
		return prefs.getInt(ID3, 8080);
	}
	
	public void refreshRecent(String tableName) {
		if (!recent.contains(tableName)) {
			prefs.put(R4, prefs.get(R3, ""));
			prefs.put(R3, prefs.get(R2, ""));
			prefs.put(R2, prefs.get(R1, ""));
			prefs.put(R1, tableName);
		}
	}
	
	public List<String> getRecent() {
		
		recent.clear();
		recent.add(prefs.get(R1, ""));
		recent.add(prefs.get(R2, ""));
		recent.add(prefs.get(R3, ""));
		recent.add(prefs.get(R4, ""));
		return recent;
	}
}
