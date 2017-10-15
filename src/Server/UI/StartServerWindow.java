package Server.UI;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import Server.controller.Controller;

//import Proj1.server.ServerThread;

public class StartServerWindow implements ActionListener{
    private JFrame startFrame = new JFrame("Mini-Canvas  Start Server");
    private JTextField portNumberTextField;
    private Controller controller;

    public void showStartServerWindow(){
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        int windowWidth = 512;
        int windowHeight = 375;
        
        JPanel icon = new JPanel();
        JPanel text = new JPanel();
        JLabel background = new JLabel();
        JLabel port = new JLabel("   Your port number:");
        portNumberTextField = new JTextField(15);
        JButton start = new JButton(" Start ");
        JButton exit = new JButton("     Exit    ");
        
        start.addActionListener(this);
        start.setActionCommand("start");
		exit.addActionListener(this);
		exit.setActionCommand("exit");
        
        
        ImageIcon image = new ImageIcon(getClass().getResource("/icon/Loginbackground.jpg"));
        background.setIcon(image);
        icon.add(background);
        
        JPanel inputPort = new JPanel();
        inputPort.add(port);
        inputPort.add(portNumberTextField);
        
        JPanel buttons = new JPanel();
        JPanel bbuttons = new JPanel();
        buttons.add(bbuttons, BorderLayout.CENTER);
        bbuttons.add(start);
        bbuttons.add(exit);
        
        text.setLayout( new BorderLayout() );
        text.add(inputPort,BorderLayout.NORTH);
        text.add(buttons,BorderLayout.CENTER);
        
        startFrame.setBounds((width - windowWidth) / 2,
                (height - windowHeight) / 2, windowWidth, windowHeight);
        startFrame.setVisible(true);
        startFrame.setLayout( new BorderLayout() );
        startFrame.add(icon, BorderLayout.NORTH);
        startFrame.add(text, BorderLayout.CENTER);
        startFrame.setResizable(false);
        startFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        startFrame.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {
                if(controller != null){
                    controller.getTcpServer().stop();
                }
                System.exit(0);
            }

        });
    }
    
    public void actionPerformed(ActionEvent e) {  
        if(e.getActionCommand().equals("start")){
            //test
            portNumberTextField.setText("4444");

            this.controller = new Controller();
            this.controller.setStartserverWindow(this);
            int serverPort;
            try{
                serverPort = Integer.parseInt(portNumberTextField.getText());
            } catch (NumberFormatException exception){
                return;
            }
            controller.startServer(serverPort);

        }  
        if(e.getActionCommand().equals("exit")){
            if(this.controller != null){
                this.controller.getTcpServer().stop();
            }
            System.exit(0);
        }  
    }

    public void finish() {
        startFrame.dispose();
        portNumberTextField = null;
    }

}

