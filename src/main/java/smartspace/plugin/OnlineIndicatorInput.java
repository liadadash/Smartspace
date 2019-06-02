package smartspace.plugin;

import java.util.ArrayList;
import java.util.Arrays;

public class OnlineIndicatorInput {
	private String[] onlineMembers;
	
	public OnlineIndicatorInput() {
	}

	public String[] getOnlineMembers() {
		return onlineMembers;
	}

	public void setOnlineMembers(String[] onlineMembers) {
		this.onlineMembers = onlineMembers;
	}
	
	public void addPlayer(String smartspace, String email) {
		ArrayList<String> players = new ArrayList<>(Arrays.asList(this.onlineMembers));
		String key = smartspace + "#" + email;
		
		if (!players.contains(key)) {
			players.add(key);
		}
		
		this.onlineMembers = players.toArray(new String[0]);
	}
	
	public void removePlayer(String smartspace, String email) {
		ArrayList<String> players = new ArrayList<>(Arrays.asList(this.onlineMembers));
		String key = smartspace + "#" + email;
		
		while (players.contains(key)) {
			players.remove(key);
		}
		
		this.onlineMembers = players.toArray(new String[0]);
	}
}
