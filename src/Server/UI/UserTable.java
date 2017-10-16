package Server.UI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
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

        scrollPane.setViewportView(table);
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