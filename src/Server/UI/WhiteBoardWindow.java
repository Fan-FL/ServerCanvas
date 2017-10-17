package Server.UI;

import Server.controller.Controller;
import Server.file.FileHandler;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.text.SimpleDateFormat;

// Draw the main frame of the white board
public class WhiteBoardWindow extends JFrame implements ActionListener {

	private static final long serialVersionUID = -2551980583852173918L;
	private Controller controller;
	private JToolBar buttonpanel;
	// define the button panel

	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	private JMenuBar bar;
	private JMenu file, color, stroke;
	// four main menu of the button panel
	private JMenuItem newfile, openfile, savefile, exit;
	private JMenuItem colorchoice, strokeitem;
	private Icon nf, sf, of, ex;
	// The icon objects of the button panel
	private JLabel startbar;
	private DrawArea drawarea;

	public DrawArea getDrawarea() {
		return drawarea;
	}

	public void setDrawarea(DrawArea drawarea) {
		this.drawarea = drawarea;
	}

	private FileHandler fileclass;

	private UserTable userTable;

    public UserTable getUserTable() {
        return userTable;
    }

    public void setUserTable(UserTable userTable) {
        this.userTable = userTable;
    }

    private JPanel chat;

	private JTextArea recvArea, sendArea;
	private JPanel chatRoomPanel, recviveAreaPanel, sendAreaPanel,
			sendButtonPanel, sendButtonAreaPanel;
	private JLabel chatRoom;
	private JButton clear, send;
	private JScrollPane recviveScroll, sendScroll;

	String[] fontName;
	// Define the name of the icons in the button panel
	private String names[] = {"pen","line", "rect", "frect", "oval", "foval", "circle", "fcircle",
			"roundrect", "froundrect", "rubber", "color", "stroke", "word"};
	private Icon icons[];

	// Show instruction when the mouse moves above the button
	private String tiptext[] = {"freely draw", "draw a straight line",
			"draw a hollow rectangle", "draw a solid rectangle",
			"draw a hollow oval", "draw a solid oval", "draw a hollow circle",
			"draw a solid circle", "draw a rounded corner rectangle",
			"draw a solid rounded corner rectangle", "eraser", "color",
			"brush size", "text input"};
	JButton button[]; // define button group in toolbar
	private JCheckBox bold, italic;

	private JComboBox stytles;
	private String my_ip;

	Toolkit tool = getToolkit();
	Dimension dim = tool.getScreenSize();// Get the size of current screen

	public WhiteBoardWindow(String string, final Controller controller) {
		// TODO constructor of main interface
		super(string);
		this.controller = controller;
		// initial menu
		file = new JMenu("file");
		color = new JMenu("color");
		stroke = new JMenu("brush");
		bar = new JMenuBar();// initial menu

		bar.add(file);
		bar.add(color);
		bar.add(stroke);
        bar.setOpaque(true);
        bar.setBackground(new Color(245,245,245));

		setJMenuBar(bar);

		// Define short cut keys for the buttons
		file.setMnemonic('F');
		color.setMnemonic('C');
		stroke.setMnemonic('S');

		// File menu initialization
		try {
			Reader reader = new InputStreamReader(getClass()
					.getResourceAsStream("/icon"));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
					"Can't read image from the disk", "error",
					JOptionPane.ERROR_MESSAGE);
		}
		// Show the icons for the sub-buttons of File menu
		nf = new ImageIcon(getClass().getResource("/icon/newfile.jpg"));
		sf = new ImageIcon(getClass().getResource("/icon/savefile.jpg"));
		of = new ImageIcon(getClass().getResource("/icon/openfile.jpg"));
		ex = new ImageIcon(getClass().getResource("/icon/exit.jpg"));
		newfile = new JMenuItem("new", nf);
		openfile = new JMenuItem("open", of);
		savefile = new JMenuItem("save", sf);
		exit = new JMenuItem("exit", ex);

		file.add(newfile);
		file.add(openfile);
		file.add(savefile);
		file.add(exit);

		// Add short cut keys for the buttons
		newfile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				InputEvent.CTRL_MASK));
		openfile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				InputEvent.CTRL_MASK));
		savefile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				InputEvent.CTRL_MASK));
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
				InputEvent.CTRL_MASK));

		newfile.addActionListener(this);
		openfile.addActionListener(this);
		savefile.addActionListener(this);
		exit.addActionListener(this);

		// Initialization of the color palate
		colorchoice = new JMenuItem("color palete");
		colorchoice.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
				InputEvent.CTRL_MASK));
		colorchoice.addActionListener(this);
		color.add(colorchoice);

		// Initialization for stroke menu
		strokeitem = new JMenuItem("set brush");
		strokeitem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
				InputEvent.CTRL_MASK));
		stroke.add(strokeitem);
		strokeitem.addActionListener(this);

		// Initialization for the toolbar
		buttonpanel = new JToolBar(JToolBar.HORIZONTAL);
		icons = new ImageIcon[names.length];
		button = new JButton[names.length];
		for (int i = 0; i < names.length; i++) {
			// read icons for the button to the buffer
			icons[i] = new ImageIcon(getClass().getResource(
					"/icon/" + names[i] + ".jpg"));
			// Create the buttons
			button[i] = new JButton("", icons[i]);
			// Show instructions when the mouse moves over the icon
			button[i].setToolTipText(tiptext[i]);
			buttonpanel.add(button[i]);
			button[i].setBackground(Color.white);
			if (i < 3) {
				button[i].addActionListener(this);
			} else if (i <= 17) {
				button[i].addActionListener(this);
			}

		}
		// Handles the styles of the characters
		CheckBoxHandler CHandler = new CheckBoxHandler();
		bold = new JCheckBox("bold");
		bold.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
		// Set font for the character "bold"
		bold.addItemListener(CHandler);
		italic = new JCheckBox("italic");
		italic.addItemListener(CHandler);
		// Set font for the character "italic"
		italic.setFont(new Font(Font.DIALOG, Font.ITALIC, 30));
		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		// Get available fonts from the computer
		fontName = ge.getAvailableFontFamilyNames();
		stytles = new JComboBox(fontName); // Initialization for the font lists
		stytles.addItemListener(CHandler);
		stytles.setMaximumSize(new Dimension(400, 50));
		stytles.setMinimumSize(new Dimension(250, 40));
		stytles.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
		// Set font for the lists

		buttonpanel.add(bold);
		buttonpanel.add(italic);
		buttonpanel.add(stytles);

		// Initialization for the start bar
		startbar = new JLabel("White Board");
		startbar.setBackground(new Color(245,245,245));
		try {
			my_ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

        userTable = new UserTable(this);
        JPanel kickButton = userTable.kick();
        JLabel my_info = new JLabel("      You are: Admin" +"(" + my_ip + ")"); 
        my_info.setForeground(Color.black);
        JPanel userinfo = new JPanel();
        userinfo.setLayout(new BorderLayout());

        JPanel user_table = new JPanel();
        user_table.setLayout(new BorderLayout());
        user_table.setBackground(Color.white);
        user_table.setBorder(BorderFactory.createLineBorder(Color.black));
        user_table.add(userTable, BorderLayout.CENTER);
        user_table.setPreferredSize(new Dimension(250, dim.height - 260));
        userinfo.add(my_info, BorderLayout.NORTH);
        userinfo.add(user_table, BorderLayout.CENTER);
        userinfo.add(kickButton, BorderLayout.SOUTH);

		chat = new JPanel();
		chat.setLayout(new BorderLayout(0, 10));
		chat.setPreferredSize(new Dimension(400, dim.height - 130));
		chat.add(recvWindow(), BorderLayout.NORTH);
		chat.add(sendWindow(), BorderLayout.CENTER);
		chat.add(buttons(), BorderLayout.SOUTH);

		// Initialization for the canvas
		drawarea = new DrawArea(this);
		fileclass = new FileHandler(this, drawarea);

		Container con = getContentPane(); // Get the canvas implemented
		con.setLayout(new BorderLayout());
		con.add(buttonpanel, BorderLayout.NORTH);
		con.add(drawarea, BorderLayout.CENTER);
		con.add(startbar, BorderLayout.SOUTH);
		con.add(userinfo, BorderLayout.WEST);
		con.add(chat, BorderLayout.EAST);

		setBounds(0, 0, dim.width, dim.height - 40);
		setVisible(true);
		validate();
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				if(controller != null){
                    controller.sendToClients("{\"cmd\":\"serverClosed\"}");
					controller.getTcpServer().stop();
				}
				System.exit(0);
			}

		});
	}

	// The characters shown in the start bar
	public void setStratBar(String s) {
		startbar.setText(s);
	}

	public JPanel userInfo(String userName, String ipAddress){
		JPanel userinfo = new JPanel();
		userinfo.setLayout(new BorderLayout());
		JLabel username = new JLabel(" " + userName);
		username.setBackground(Color.white);
		JButton operation = new JButton("DELETE");
		operation.addActionListener(this);
		operation.setActionCommand("delete");
		username.setToolTipText(ipAddress);
		userinfo.add(username, BorderLayout.WEST);
		userinfo.add(operation, BorderLayout.EAST);
		userinfo.setBorder(BorderFactory.createLineBorder(Color.black));
		//userinfo.setPreferredSize(new Dimension(200,10));
		return userinfo;
		
	}
	public JPanel recvWindow() {
		recvArea = new JTextArea(400, (dim.height - 130) * 2 / 3 - 50);

		chatRoom = new JLabel("The Chat Room");
		chatRoom.setOpaque(true);
		chatRoom.setBackground(new Color(245,245,245));
		chatRoomPanel = new JPanel();
		chatRoomPanel.setLayout(new GridLayout());
		chatRoomPanel.add(chatRoom);
		recviveScroll = new JScrollPane(recvArea);

		recviveScroll
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		recvArea.setEditable(false);
		recvArea.setLineWrap(true);

		recviveAreaPanel = new JPanel();
		recviveAreaPanel.setLayout(new BorderLayout());
		recviveAreaPanel.add(chatRoomPanel, BorderLayout.NORTH);
		recviveAreaPanel.add(recviveScroll, BorderLayout.CENTER);
		// recviveAreaPanel.setSize(400,(dim.height-130)*2/3 - 50);
		recviveAreaPanel.setPreferredSize(new Dimension(400,
				(dim.height - 130) * 2 / 3 - 50));
		recviveAreaPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		return recviveAreaPanel;
	}

	public JPanel sendWindow() {
		sendArea = new JTextArea(400, (dim.height - 130) / 3 - 50);
		sendAreaPanel = new JPanel();
		sendScroll = new JScrollPane(sendArea);

		sendScroll
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		sendArea.setLineWrap(true);
		sendAreaPanel.setLayout(new BorderLayout());

		sendAreaPanel.add(sendScroll , BorderLayout.CENTER);
		sendAreaPanel.setSize(400, (dim.height - 130) / 3 - 50);
		sendAreaPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		return sendAreaPanel;
	}

	public JPanel buttons() {
		clear = new JButton("Clear");
		send = new JButton("Send");
		//send.setMnemonic(/n);

		clear.addActionListener(this);
		clear.setActionCommand("clear");
		send.addActionListener(this);
		send.setActionCommand("send");

		sendButtonPanel = new JPanel();
		sendButtonPanel.add(clear);
		sendButtonPanel.add(send);
		sendButtonAreaPanel = new JPanel();
		sendButtonAreaPanel.add(sendButtonPanel, BorderLayout.EAST);
		return sendButtonAreaPanel;
	}

	public void addChatMessage(String content) {
		recvArea.append(content);
	}

	public String generateChatMessage() {
		String userName = this.userTable.getMyUsername();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = df.format(new Date());
		String message = userName + "   (" + time + ("):") + System.getProperty("line.separator")
				+ sendArea.getText() + System.getProperty("line.separator") + System.getProperty("line.separator");
		return message;
	}

	/*
	 * The functions of the button
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		for (int i = 0; i <= 10; i++) {
			if (e.getSource() == button[i]) {
				drawarea.setCurrentShapeType(DrawArea.ShapeType.values()[i]);
				drawarea.repaint();
			}

		}
		if (e.getSource() == newfile) {
			// new file
			fileclass.newFile();
		} else if (e.getSource() == openfile) {
			// open file
			fileclass.openFile();
		} else if (e.getSource() == savefile) {
			// save file
			fileclass.saveFile();
		} else if (e.getSource() == exit) {
			// exit
            controller.sendToClients("{\"cmd\":\"serverClosed\"}");
			this.controller.getTcpServer().stop();
			System.exit(0);
		} else if (e.getSource() == button[11]) {
			// color plate
			drawarea.chooseColor();// Choose your color
		} else if (e.getSource() == button[12] || e.getSource() == strokeitem) {
			// Brush size
			drawarea.setStroke();
		} else if (e.getSource() == button[13]) {
			// add text
			JOptionPane.showMessageDialog(null,
					"please click on canvs to confirm position of textinput",
					"hints", JOptionPane.INFORMATION_MESSAGE);
			drawarea.setCurrentShapeType(DrawArea.ShapeType.WORD);
			drawarea.repaint();
		} else if (e.getActionCommand().equals("clear")) {
			sendArea.setText("");
		} else if (e.getActionCommand().equals("send")) {
			this.controller.addChatMessage(generateChatMessage());
			sendArea.setText("");
		}

	}


    // About the font of the character
	public class CheckBoxHandler implements ItemListener {

		public void itemStateChanged(ItemEvent ie) {
			// TODO the font of the character
			if (ie.getSource() == bold) // Bold font
			{
				if (ie.getStateChange() == ItemEvent.SELECTED)
					drawarea.setFont(1, Font.BOLD);
				else
					drawarea.setFont(1, Font.PLAIN);
			} else if (ie.getSource() == italic) // Italic font
			{
				if (ie.getStateChange() == ItemEvent.SELECTED)
					drawarea.setFont(2, Font.ITALIC);
				else
					drawarea.setFont(2, Font.PLAIN);

			} else if (ie.getSource() == stytles) // System font
			{
				drawarea.setStytle(fontName[stytles.getSelectedIndex()]);
			}
		}

	}
}
