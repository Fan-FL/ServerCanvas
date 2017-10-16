package Server.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
public class UserTable extends JPanel{

    private DefaultTableModel tableModel;
    private JTable table;
    private WhiteBoardWindow whiteboard = null;
    private String myUsername = "admin";
    
	Toolkit tool = getToolkit();
	Dimension dim = tool.getScreenSize();// Get the size of current screen

    public String getMyUsername() {
        return myUsername;
    }

    public void setMyUsername(String myUsername) {
        this.myUsername = myUsername;
    }

    public UserTable(WhiteBoardWindow whiteboard){
        super();
        this.whiteboard = whiteboard;
        String[] columnNames = {"Username"};
        tableModel = new DefaultTableModel(columnNames,0);
        table = new JTable(tableModel){
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane,BorderLayout.CENTER);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        table.setPreferredSize(new Dimension(250,dim.height - 270));
//        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        //table.setPreferredSize(new Dimension(250,dim.height - 270));
        table.setPreferredScrollableViewportSize(new Dimension(250,dim.height - 280));
        table.setRowHeight(30);

        scrollPane.setViewportView(table);
        add(scrollPane,BorderLayout.CENTER);

    }
/*    
    public JPanel userinfo(){
        String[] columnNames = {"Username"};
        
        tableModel = new DefaultTableModel(columnNames,0);
        //tableModel.setBackground();
        table = new JTable(tableModel){
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setPreferredScrollableViewportSize(new Dimension(400,dim.height - 300));
        table.setBackground(Color.green);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane
		.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setViewportView(table);
        JPanel userinfo = new JPanel();
        userinfo.setLayout(new BorderLayout());
        userinfo.add(table, BorderLayout.CENTER);
        userinfo.add(scrollPane, BorderLayout.EAST);
//        userinfo.setPreferredSize(new Dimension(250,dim.height - 270));
        return userinfo;
    }
 */   
    public JPanel kick(){
        final JPanel panel = new JPanel();
        add(panel,BorderLayout.SOUTH);
        panel.add(new JLabel("Please select user before kick"));

        final JButton kickButton = new JButton("kick");
        kickButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                kickUser();
            }
        });
        panel.add(kickButton);
        return panel;
    }

    public synchronized void addUser(String username){
        String []rowValues = {username};
        tableModel.addRow(rowValues);
    }

    private synchronized void kickUser(){
        int selectedRow = table.getSelectedRow();
        if(selectedRow!=-1){
            this.whiteboard.getController().kickUser((String)table.getValueAt(selectedRow,0));
        }
    }

    public synchronized boolean deleteUser(String username){
        int rowCount = tableModel.getRowCount();
        if(rowCount != -1){
            for (int i=0; i<rowCount; i++){
                if(table.getValueAt(i,0).equals(username)){
                    tableModel.removeRow(i);
                    return true;
                }
            }
        }
        return false;
    }

    public synchronized List<String> getAllUsers(){
        int rowCount = tableModel.getRowCount();
        if(rowCount != -1){
            List<String> usernamesList = new ArrayList<>();
            for (int i=0; i<rowCount; i++){
                usernamesList.add((String)table.getValueAt(i,0));
            }
            return usernamesList;
        }else{
            return null;
        }
    }

}
