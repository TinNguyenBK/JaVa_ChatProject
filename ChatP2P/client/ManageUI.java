package client;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.JOptionPane;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.JButton;


import Util.XMLGen;
import Util.XMLParseTool;
import basic.Friend;
import basic.Protocol;

import java.net.*;
import java.util.ArrayList;
import java.io.*;
import java.awt.List;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.ActionEvent;
import client.LoginUI;

public class ManageUI extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String socketIP;
	public int port;
	public  Socket sockToConnectSV;
	private JPanel contentPane;
	private JTextField txtName;
	private JTextField txtPort;
	private JTextField textIP;
    private JLabel lblOnlineFriend;
    private JButton btnSignOut;
    public String userName;
	public int userConnectionPort;
	private ChatFrame chatbox;
	private List rowList;
	private ArrayList<Friend> friendList;
	private ServerSocket ss2 = null;
	
	public ManageUI(Socket sockToConnectSV,ArrayList<Friend> frList,String userName,int userConnectionPort) {
		this.userName = userName;
		this.socketIP = sockToConnectSV.getInetAddress().toString(); //server IP address to which this socket is connected;
		this.port = sockToConnectSV.getPort();//server port to which this socket is connected;
		this.userConnectionPort = userConnectionPort;	//User Connection Port (server port for other user)
		this.friendList = frList;
		
		System.out.println(userConnectionPort);
		try {
			ss2 = new ServerSocket(userConnectionPort);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Listening Listener = new Listening(ss2); //listening after start MUI
		new Thread(Listener).start();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		this.sockToConnectSV = sockToConnectSV;
		setTitle(userName);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		SpringLayout sl_contentPane = new SpringLayout();
		contentPane.setLayout(sl_contentPane);
		
		JLabel lblUsername = new JLabel("Username");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblUsername, 31, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblUsername, 10, SpringLayout.WEST, contentPane);
		contentPane.add(lblUsername);
		
		JLabel lblPort = new JLabel("Port");
		sl_contentPane.putConstraint(SpringLayout.WEST, lblPort, 0, SpringLayout.WEST, lblUsername);
		contentPane.add(lblPort);
		
		JLabel lblIpAddress = new JLabel("IP Address");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblIpAddress, 98, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, lblPort, -18, SpringLayout.NORTH, lblIpAddress);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblIpAddress, 0, SpringLayout.WEST, lblUsername);
		contentPane.add(lblIpAddress);
		
		txtName = new JTextField();
		txtName.setEditable(false);
		sl_contentPane.putConstraint(SpringLayout.NORTH, txtName, 0, SpringLayout.NORTH, lblUsername);
		sl_contentPane.putConstraint(SpringLayout.WEST, txtName, 31, SpringLayout.EAST, lblUsername);
		contentPane.add(txtName);
		txtName.setColumns(10);
		
		txtPort = new JTextField();
		txtPort.setEditable(false);
		sl_contentPane.putConstraint(SpringLayout.NORTH, txtPort, 0, SpringLayout.NORTH, lblPort);
		sl_contentPane.putConstraint(SpringLayout.EAST, txtPort, 0, SpringLayout.EAST, txtName);
		contentPane.add(txtPort);
		txtPort.setColumns(10);
		
		textIP = new JTextField();
		textIP.setEditable(false);
		sl_contentPane.putConstraint(SpringLayout.NORTH, textIP, 0, SpringLayout.NORTH, lblIpAddress);
		sl_contentPane.putConstraint(SpringLayout.EAST, textIP, 0, SpringLayout.EAST, txtName);
		contentPane.add(textIP);
		textIP.setColumns(10);
		
		JButton btnChat = new JButton("Chat");
		contentPane.getRootPane().setDefaultButton(btnChat);
		//Click CHAT
		btnChat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String friendName = txtName.getText();
				int port = Integer.parseInt(txtPort .getText());
				String IPaddress = textIP.getText();
				new Thread(new Runnable() {
				    public void run()
				    {
				    	try {
							Socket sock = new Socket(IPaddress,port);		//Create connection to Other client (IP,port)
							SendCommand send = new SendCommand(sock,userName);//send IP, port and username to another client still Listening.java in the top 
							new Thread(send).run();
							chatbox = new ChatFrame(sock,friendName);//create frame chat box and waiting response of client negative
							chatbox.setVisible(true);
							chatbox.btnSendFile.setEnabled(false);
							chatbox.btnSendMess.setEnabled(false);
							chatbox.btnEndChat.setEnabled(false);
							chatbox.txtConversation.setText("Sent chat request to your friend");
							Receiving recieveThread = new Receiving(sock,friendName,chatbox);//get response from negative client
							Thread threadRecieve = new Thread(recieveThread);
							threadRecieve.start();					
						} catch (UnknownHostException e) {
							JOptionPane.showMessageDialog(null, "Can't connect to this friend");
							e.printStackTrace();
						} catch (IOException e) {
							JOptionPane.showMessageDialog(null, "Something happen, can't not create connection");
							e.printStackTrace();
						}
				    }}).start();			
			}
		});
		sl_contentPane.putConstraint(SpringLayout.NORTH, btnChat, 49, SpringLayout.SOUTH, textIP);
		sl_contentPane.putConstraint(SpringLayout.WEST, btnChat, 37, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, btnChat, -307, SpringLayout.EAST, contentPane);
		contentPane.add(btnChat);
		
		lblOnlineFriend = new JLabel("Online Friend");
		sl_contentPane.putConstraint(SpringLayout.EAST, lblOnlineFriend, -66, SpringLayout.EAST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblOnlineFriend, 10, SpringLayout.NORTH, contentPane);
		contentPane.add(lblOnlineFriend);
		
		btnSignOut = new JButton("Sign out");
		btnSignOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SendCommand sendCommand = new SendCommand(sockToConnectSV, XMLGen.genKEEP_ALIVE(userName, 0));
				new Thread(sendCommand).run();
				if (ss2 != null && !ss2.isClosed()) {
					try {
						ss2.close();	//close client's server
						sockToConnectSV.close();	//close socket
					} catch (IOException e) {
						System.out.println("Socket is closed");
					}
				}
				setVisible(false);
				LoginUI winUi = new LoginUI();
				winUi.frame.setVisible(true);
			}
		});
		sl_contentPane.putConstraint(SpringLayout.NORTH, btnSignOut, 0, SpringLayout.NORTH, btnChat);
		sl_contentPane.putConstraint(SpringLayout.WEST, btnSignOut, 44, SpringLayout.EAST, btnChat);
		sl_contentPane.putConstraint(SpringLayout.EAST, btnSignOut, -183, SpringLayout.EAST, contentPane);
		contentPane.add(btnSignOut);
		
		//Friend List
		rowList = new List();
		rowList.addItemListener(new ItemListener() {			
			@Override
			//Click on friend name
			public void itemStateChanged(ItemEvent e) {
				if (rowList.getSelectedIndex() < frList.size()){
					txtName.setText(frList.get(rowList.getSelectedIndex()).getAccountName());
					textIP.setText(frList.get(rowList.getSelectedIndex()).getIPAdress());
					txtPort.setText(String.valueOf(frList.get(rowList.getSelectedIndex()).getPort()));
					}				
			}
		});
		sl_contentPane.putConstraint(SpringLayout.NORTH, rowList, 14, SpringLayout.SOUTH, lblOnlineFriend);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, rowList, 140, SpringLayout.SOUTH, lblOnlineFriend);
		sl_contentPane.putConstraint(SpringLayout.EAST, rowList, -23, SpringLayout.EAST, contentPane);
		for(int i=0;i< frList.size();i++){
			if(userName.equals(frList.get(i).getAccountName()))									
				continue;
    		rowList.add(frList.get(i).getAccountName());
    	}
		contentPane.add(rowList);
		
		//refresh friend list
		JButton btnRefreshlist = new JButton("RefreshList");
		btnRefreshlist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				refreshList();
			}
		});
		sl_contentPane.putConstraint(SpringLayout.SOUTH, btnRefreshlist, -31, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, btnRefreshlist, -43, SpringLayout.EAST, contentPane);
		contentPane.add(btnRefreshlist);		
		contentPane.setVisible(true);		
	}
	public void setList(ArrayList<Friend> list)
	{
		rowList.removeAll();
		friendList.clear();
		for(int i=0;i< list.size();i++){
    		rowList.add(list.get(i).getAccountName());
    		friendList.add(list.get(i));
	}
	}
	public void refreshList()
	{
		try{
		PrintWriter writer = new PrintWriter(new DataOutputStream(sockToConnectSV.getOutputStream()));
        writer.println(XMLGen.genGET_FRIEND_LIST());//send get friend list
        writer.flush(); 	
		BufferedReader reader;		
		reader = new BufferedReader(new InputStreamReader(sockToConnectSV.getInputStream()));
		String resOfServer = reader.readLine();
		if (XMLParseTool.isXMLCode(resOfServer))
		{
			Document doc = XMLParseTool.genXMLDocument(resOfServer);		
			ArrayList<Friend> list = new ArrayList<Friend>();
			NodeList nList = doc.getElementsByTagName(Protocol.FRIEND);
			for (int k = 0; k < nList.getLength(); k++)
			{
				Node nNode = nList.item(k);
				if (nNode.getNodeType() == Node.ELEMENT_NODE)
				{										
					Element eElement = (Element) nNode;
					String username = eElement.getElementsByTagName(Protocol.USERNAME).item(0).getTextContent();
					if(userName.equals(username))									
						continue;
					String ip = eElement.getElementsByTagName(Protocol.IP).item(0).getTextContent();
					int port = Integer.parseInt(eElement.getElementsByTagName(Protocol.PORT).item(0).getTextContent());
					Friend newFriend = new Friend(username,ip,port);
					list.add(newFriend);
				}
			}
			//write friend list into rowList
			setList(list);	
		}	
		}
		catch (Exception ex)
		{
			ex.getStackTrace();
		}
	
		}
}
