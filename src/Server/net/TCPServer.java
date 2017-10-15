package Server.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import javax.net.ServerSocketFactory;


import Server.controller.Controller;
import Server.shape.*;
import Server.util.AES;
import Server.util.JsonMessageUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

//client connection handler thread
public class TCPServer implements Runnable {
	private int serverPort;
	
	ServerSocketFactory factory = null;
	private ServerSocket serverSocket = null;
	private volatile boolean running = false;
	private Map<String, ClientSocket> accpetpedClientSocketMap = new HashMap<>();
	private List<ClientSocket> waitingClientSocketlist = new ArrayList<>();

    public Map<String, ClientSocket> getAccpetpedClientSocketMap() {
        return accpetpedClientSocketMap;
    }

    public void setAccpetpedClientSocketMap(Map<String, ClientSocket> accpetpedClientSocketMap) {
        this.accpetpedClientSocketMap = accpetpedClientSocketMap;
    }

    public List<ClientSocket> getWaitingClientSocketlist() {
        return waitingClientSocketlist;
    }

    public void setWaitingClientSocketlist(List<ClientSocket> waitingClientSocketlist) {
        this.waitingClientSocketlist = waitingClientSocketlist;
    }

    private Controller controller;

	private int userNum = 1;

	public synchronized String getNextUsername(){
		return "user_" + String.valueOf(userNum++);
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}


	public TCPServer() {
		factory = ServerSocketFactory.getDefault();
	}
	
	
	public TCPServer(int serverPort) {	
		this();
		this.serverPort = serverPort;
	}

	public void approveClient(ClientSocket clientSocket){
		accpetpedClientSocketMap.put(clientSocket.getUsername(), clientSocket);
		waitingClientSocketlist.remove(clientSocket);
		this.controller.addNewClient(clientSocket.getUsername(), clientSocket.getClientIP(), clientSocket.getClientPort());
        String approveData = "{\"cmd\":\"check\",\"content\":\"approve\"}";
        clientSocket.sendData(approveData);
	}

    public void rejectClient(ClientSocket clientSocket) {
        String approveData = "{\"cmd\":\"check\",\"content\":\"reject\"}";
        clientSocket.sendData(approveData);
        clientSocket.shutdownSocket();
    }


	public int getServerPort() {
		return serverPort;
	}
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	@Override
	public void run() {
		this.connect();
	}
	
	public void connect() {
		if (this.running)
			return;
		
		try(ServerSocket serverSocket = factory.createServerSocket(serverPort)){
			this.serverSocket = serverSocket;
			this.running = true;
			this.controller.showWhiteBoardWindow();
			System.out.println("Waiting for client connection..");
			while (this.running) {
				Socket socket = serverSocket.accept();
				System.out.println("received connection from IP: " + socket.getInetAddress() + "port: " + socket.getPort());
				//start client requests handler thread
				new Thread(new ClientSocket(socket)).start();
			}
		}catch (IOException e){
			stop();
		} 
	}
	
	public boolean isConnected() {
		return this.running;
	}
	
	public void stop() {
		if (running)
			running = false;
		if (serverSocket != null) {
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally{
				serverSocket = null;
				this.controller.serverDisconnect();
				this.waitingClientSocketlist.clear();
				this.accpetpedClientSocketMap.clear();
				System.out.println("server stopped");
			}
		}
	}

    //client requests handler thread
	public class ClientSocket implements Runnable {
		private Socket socket;
		private String clientIP;
		private String clientPort;
		private String username;

		boolean flag = true;
		long lastReceiveTime;
		// Connection will be closed if no message received from client every 11 seconds (plus 1 seconds for delay)
		private long receiveTimeDelay = 11000;
		private long checkDelay = 100;

		private DataInputStream input = null;
		private DataOutputStream output = null;
		
		public ClientSocket(Socket socket) {
			try {
				this.socket = socket;
				lastReceiveTime = System.currentTimeMillis();
				input = new DataInputStream(this.socket.getInputStream());
				output = new DataOutputStream(this.socket.getOutputStream());
				this.clientIP = this.socket.getInetAddress().getHostAddress();
				this.clientPort = String.valueOf(this.socket.getPort());
				this.username = getNextUsername();
				waitingClientSocketlist.add(this);
				controller.newClientConnected(this);
                approveClient(this);
			}catch (IOException e){
				System.out.println("ClientSocket IO Exception:"+e.getMessage());
				shutdownSocket();
			}
		}

		public String getClientIP() {
			return clientIP;
		}

		public void setClientIP(String clientIP) {
			this.clientIP = clientIP;
		}

		public String getClientPort() {
			return clientPort;
		}

		public void setClientPort(String clientPort) {
			this.clientPort = clientPort;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		@Override
		public void run() {
			while (running && flag) {
				if (System.currentTimeMillis() - lastReceiveTime > receiveTimeDelay) {
					shutdownSocket();
				} else {
					try {
						if (input.available() > 0) {
							messageHandler(input.readUTF());
							lastReceiveTime = System.currentTimeMillis();
						} else {
							Thread.sleep(checkDelay);
						}
					} catch (Exception e) {
						System.out.println("Receiving data exception: " +e.getMessage());
						shutdownSocket();
					}
				}
			}
		}

		public void shutdownSocket() {
			System.out.println("The server closes a connection to a client.");
			if (flag)
				flag = false;

			if (socket != null) {
				System.out.println("port " + socket.getPort());
				try {
					socket.close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
				} finally{
					socket = null;
					input = null;
					output = null;
					waitingClientSocketlist.remove(this);
					accpetpedClientSocketMap.remove(this.getUsername());
				}
			}

		}
		
		private void messageHandler(String message) {
			String msg = AES.Decrypt(message);
			java.lang.reflect.Type mapType = new TypeToken<Map<String, Object>>(){}.getType();
			Gson gson = new Gson();
			Map<String,Object> data = gson.fromJson(msg, mapType);
			System.out.println("received msg from client: "+socket.getInetAddress()+" port:"+socket.getPort() +" msg: " + data);
			switch (data.get("cmd").toString()) {
                case "heartbeat":
                    String heartbeatData = "{\"cmd\":\"heartbeat\",\"content\":\"ack\"}";
                    sendData(heartbeatData);
                    break;
                case "checkACK":
                    controller.sendCurrentShapes(this);
                    break;
                case "addShape":
                    String classType = data.get("classType").toString();
                    String objectData = data.get("object").toString();
                    Shape shape = JsonMessageUtil.GenerateShapeFromMessage(classType, objectData);
                    controller.addShape(shape, msg);
			default:
				break;
			}
		}
		
		public void sendData(String msg){
			try {
				output.writeUTF(AES.Encrypt(msg));
				output.flush();
			} catch (IOException e) {
				e.printStackTrace();
				shutdownSocket();
                System.out.println("Sending data exception: " +e.getMessage());
			}
		}
	}

}
