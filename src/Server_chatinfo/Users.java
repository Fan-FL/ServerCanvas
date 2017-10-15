package Server_chatinfo;

import java.util.HashMap;
import java.util.Map;

public class Users {
	private Map<String, String> usernameIPMap = new HashMap();
	public void connected(String name, String ip){
		usernameIPMap.put(name, ip);
	}
	
	public String getIPByName(String name){
		return usernameIPMap.get(name);
	}
	
	public void clearUsers(){
		usernameIPMap.clear();
	}
	
}
