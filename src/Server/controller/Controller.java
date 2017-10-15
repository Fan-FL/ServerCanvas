package Server.controller;

import Server.UI.StartServerWindow;
import Server.UI.WhiteBoardWindow;
import Server.net.TCPServer;


public class Controller {
    private TCPServer tcpServer;
    private StartServerWindow startserverWindow;
    private WhiteBoardWindow whiteBoardWindow;

    public void setStartserverWindow(StartServerWindow startserverWindow) {
        this.startserverWindow = startserverWindow;
    }

    public TCPServer getTcpServer() {
        return tcpServer;
    }

    public void setTcpServer(TCPServer tcpServer) {
        this.tcpServer = tcpServer;
    }

    public StartServerWindow getStartserverWindow() {
        return startserverWindow;
    }

    public void startServer(int serverPort) {
        this.tcpServer = new TCPServer(serverPort);
        this.tcpServer.setController(this);
        new Thread(this.tcpServer).start();
    }

    public void showWhiteBoardWindow() {
        this.whiteBoardWindow = new WhiteBoardWindow("Mini-Canvas Server", this);
        this.startserverWindow.finish();
    }

    public void serverDisconnect(){

    }

    public void newClientConnected(TCPServer.ClientSocket clientSocket) {
        //approve or not
    }

    public void approveNewClient(TCPServer.ClientSocket clientSocket){
        tcpServer.approveClient(clientSocket);
    }

    public void rejectNewClient(TCPServer.ClientSocket clientSocket){
        tcpServer.rejectClient(clientSocket);
    }

    public void addNewClient(String username, String IP, String port){

    }

}
