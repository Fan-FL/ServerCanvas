package Server_chatinfo;

import java.io.Serializable;

@SuppressWarnings("serial")
public class chatinfo implements Serializable{
	private String username;
	private String time;
	private String content;
	private String ip_address;

	public chatinfo(String username, String time, String content,String ip_address) {
		this.username = username;
		this.time = time;
		this.content = content;
		this.ip_address = ip_address;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getIp_address() {
		return ip_address;
	}

	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}
	
	
	
	
}
