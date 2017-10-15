package Server;


import Server.UI.StartServerWindow;

import java.awt.EventQueue;

public class Server {
	// The main class of this program
	public static void main(String[] args){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StartServerWindow start_server = new StartServerWindow();
					start_server.showStartServerWindow();
					//start_server.startFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		}
	}

