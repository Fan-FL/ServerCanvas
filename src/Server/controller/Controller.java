package Server.controller;

import Server.UI.StartServerWindow;
import Server.UI.WhiteBoardWindow;
import Server.net.TCPServer;
import Server.shape.Shape;
import Server.util.JsonMessageUtil;

import java.util.List;

import javax.swing.JOptionPane;

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
        // server socket closed
    }

    public void newClientConnected(TCPServer.ClientSocket clientSocket) {
        int res= JOptionPane.showConfirmDialog(null, "A new client wants to conect, agree or not?", "New user connected.", JOptionPane.YES_NO_OPTION);
        if(res==JOptionPane.YES_OPTION){
            this.approveNewClient(clientSocket);
        }else{
            this.rejectNewClient(clientSocket);
        }
    }

    public void approveNewClient(TCPServer.ClientSocket clientSocket){
        tcpServer.approveClient(clientSocket);
    }

    public void getApprovedClientACKFromClient(TCPServer.ClientSocket clientSocket){
        this.addUserToUserTable(clientSocket.getUsername());
        sendToClients("{\"cmd\":\"addUser\",\"content\":\"" + clientSocket.getUsername() + "\"}");
    }

    public void addUserToUserTable(String username){
        this.whiteBoardWindow.getUserTable().addUser(username);
    }

    public void kickUser(String username) {
        TCPServer.ClientSocket clientSocket = tcpServer.getAccpetpedClientSocketMap().get(username);
        if(clientSocket!= null){
            clientSocket.sendData("{\"cmd\":\"kick\"}");
            clientSocket.shutdownSocket();
        }
    }

    public void rejectNewClient(TCPServer.ClientSocket clientSocket){
        tcpServer.rejectClient(clientSocket);
    }

    public void addShape(Shape shape) {
        this.whiteBoardWindow.getDrawarea().addShape(shape, "external");
    }

    public void sendToClients(String msg) {
        for (TCPServer.ClientSocket socket : this.tcpServer.getAccpetpedClientSocketMap().values()) {
            socket.sendData(msg);
        }
    }

    public void sendCurrentShapes(TCPServer.ClientSocket clientSocket) {
        this.whiteBoardWindow.getDrawarea().reentrantReadWriteLock.readLock().lock();
        for (Shape shape : this.whiteBoardWindow.getDrawarea().shapeList) {
            String addShapeMsg = JsonMessageUtil.assembleAddShapeobjectData(shape);
            clientSocket.sendData(addShapeMsg);
        }
        this.whiteBoardWindow.getDrawarea().reentrantReadWriteLock.readLock().unlock();
    }

    public void sendCurrentUsers(TCPServer.ClientSocket clientSocket) {
        List<String> usernamesList = this.whiteBoardWindow.getUserTable().getAllUsers();
        for (String username:usernamesList) {
            clientSocket.sendData("{\"cmd\":\"addUser\",\"content\":\""+username+"\"}");
        }
    }

    public void deleteUser(String username) {
        if(this.whiteBoardWindow.getUserTable().deleteUser(username)){
            sendToClients("{\"cmd\":\"deleteUser\",\"content\":\"" + username + "\"}");
        }
    }

    public void addChatMessage(String text) {
        this.whiteBoardWindow.addChatMessage(text);
        sendToClients("{\"cmd\":\"newChat\",\"content\":\"" + text + "\"}");
    }
}
