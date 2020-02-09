package client;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.*;

import javax.swing.JPasswordField;

import Util.XMLParseTool;
import Util.XMLGen;
import basic.Friend;
import basic.Protocol;
import basic.UserAccount;

import java.util.*;

import javax.swing.JOptionPane;

import org.w3c.dom.*;
public class LoginUI {
	JFrame frame;
	private JTextField txtName;
	private JTextField txtPort;
	private JPasswordField txtPass;
	private String userName;
	private String passWord;
	private int userConnectionPort;
	private String svIP;
	private int svPort;
	private Boolean isConnected = false;
	private Socket sockToConnectSV;
    private BufferedReader reader;
    private ArrayList<Friend> frList = new ArrayList<Friend>();
    private PrintWriter writer;
    private JTextField txtsvIP;
    private ManageUI manageFrame;
	/**
	 * Launch the application.
	 */
    public static void main(String[] args) {
    	EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginUI window = new LoginUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});		
	}	
	public LoginUI() {
		initialize();		
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		//create frame
		frame = new JFrame("Chat application");
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);
		
		//create label Username
		JLabel lblUsername = new JLabel("Username");
		springLayout.putConstraint(SpringLayout.NORTH, lblUsername, 20, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, lblUsername, 28, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(lblUsername);
		
		//create label Password
		JLabel lblPassword = new JLabel("Password");
		springLayout.putConstraint(SpringLayout.NORTH, lblPassword, 22, SpringLayout.SOUTH, lblUsername);
		springLayout.putConstraint(SpringLayout.EAST, lblPassword, 0, SpringLayout.EAST, lblUsername);
		frame.getContentPane().add(lblPassword);
		
		//create label svPort
		JLabel lblPort = new JLabel("svPort");
		springLayout.putConstraint(SpringLayout.NORTH, lblPort, 110, SpringLayout.SOUTH, lblUsername);
		springLayout.putConstraint(SpringLayout.EAST, lblPort, 0, SpringLayout.EAST, lblUsername);
		frame.getContentPane().add(lblPort);
		
		//create textbox Username
		txtName = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, txtName, -3, SpringLayout.NORTH, lblUsername);
		springLayout.putConstraint(SpringLayout.WEST, txtName, 40, SpringLayout.EAST, lblUsername);
		frame.getContentPane().add(txtName);
		txtName.setColumns(10);
		
		//create textbox Password
		txtPass = new JPasswordField();
		springLayout.putConstraint(SpringLayout.NORTH, txtPass, 22, SpringLayout.SOUTH, txtName);
		springLayout.putConstraint(SpringLayout.WEST, txtPass, 0, SpringLayout.WEST, txtName);
		springLayout.putConstraint(SpringLayout.EAST, txtPass, 0, SpringLayout.EAST, txtName);
		frame.getContentPane().add(txtPass);
		
		//create textbox Svport
		txtPort = new JTextField();
		txtPort.setText("6000");
		txtPort.setEditable(false);
		springLayout.putConstraint(SpringLayout.NORTH, txtPort, 66, SpringLayout.SOUTH, txtPass);
		springLayout.putConstraint(SpringLayout.WEST, txtPort, 0, SpringLayout.WEST, txtPass);
		springLayout.putConstraint(SpringLayout.EAST, txtPort, 0, SpringLayout.EAST, txtPass);
		frame.getContentPane().add(txtPort);
		txtPort.setColumns(10);
		
		JButton btReg = new JButton("Sign up");
		btReg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SignUp signupFrame = new SignUp();
				signupFrame.setDefaultCloseOperation(1);
				signupFrame.setVisible(true);
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, btReg, 183, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, btReg, -41, SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, btReg, 29, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, btReg, -300, SpringLayout.EAST, frame.getContentPane());
		frame.getContentPane().add(btReg);
		
		JButton btLogIn = new JButton("Sign in");
		frame.getRootPane().setDefaultButton(btLogIn);
		springLayout.putConstraint(SpringLayout.NORTH, btLogIn, 0, SpringLayout.NORTH, btReg);
		springLayout.putConstraint(SpringLayout.WEST, btLogIn, 22, SpringLayout.EAST, btReg);
		springLayout.putConstraint(SpringLayout.SOUTH, btLogIn, 0, SpringLayout.SOUTH, btReg);
		springLayout.putConstraint(SpringLayout.EAST, btLogIn, -173, SpringLayout.EAST, frame.getContentPane());
		frame.getContentPane().add(btLogIn);
		
		JButton btExit = new JButton("Exit");
		btExit.addActionListener(new ActionListener() {
			 public void actionPerformed (ActionEvent e) {
			 System.exit(0);
			 }
			});
		springLayout.putConstraint(SpringLayout.NORTH, btExit, 0, SpringLayout.NORTH, btReg);
		springLayout.putConstraint(SpringLayout.WEST, btExit, 19, SpringLayout.EAST, btLogIn);
		springLayout.putConstraint(SpringLayout.SOUTH, btExit, 0, SpringLayout.SOUTH, btReg);
		springLayout.putConstraint(SpringLayout.EAST, btExit, -49, SpringLayout.EAST, frame.getContentPane());
		frame.getContentPane().add(btExit);
		
		JLabel lblServerIp = new JLabel("Server IP");
		springLayout.putConstraint(SpringLayout.NORTH, lblServerIp, 22, SpringLayout.SOUTH, lblPassword);
		springLayout.putConstraint(SpringLayout.EAST, lblServerIp, 0, SpringLayout.EAST, lblPassword);
		frame.getContentPane().add(lblServerIp);
		
		txtsvIP = new JTextField();
		txtsvIP.setText("localhost");
		springLayout.putConstraint(SpringLayout.NORTH, txtsvIP, 22, SpringLayout.SOUTH, txtPass);
		springLayout.putConstraint(SpringLayout.WEST, txtsvIP, 0, SpringLayout.WEST, txtName);
		springLayout.putConstraint(SpringLayout.EAST, txtsvIP, 0, SpringLayout.EAST, txtName);
		frame.getContentPane().add(txtsvIP);
		txtsvIP.setColumns(10);
				
		btLogIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isConnected == true){
					JOptionPane.showMessageDialog(null, "You are connected");
				}
				else {
				userName = txtName.getText();
				passWord = txtPass.getText();
				svIP = txtsvIP.getText();
				userConnectionPort = getAvailablePort();
				svPort = Integer.parseInt(txtPort.getText());
				UserAccount usr = new UserAccount(userName,passWord);
				String clientCmd = XMLGen.genLOG_IN(usr, userConnectionPort); //genLogin request string
					try {
						sockToConnectSV = new Socket(svIP,svPort);//connect to server with server IP address and server svPort number
						writer = new PrintWriter(new DataOutputStream(sockToConnectSV.getOutputStream()));//writer to write protocol and push to server 
			            writer.println(clientCmd); //send request login to server### No.1
			            writer.flush();
			            reader = new BufferedReader(new InputStreamReader(sockToConnectSV.getInputStream()));//reader to get response from server
			 			String line = reader.readLine();//
			 			int result = 1;
			            	Document doc = XMLParseTool.genXMLDocument(line);
			            	try {			
			        			NodeList ele = doc.getElementsByTagName(Protocol.SESSION_BEGIN);
			        			Node child = ele.item(0);
			        			result =  Integer.parseInt(child.getTextContent());			   
			        			}
			            	catch(Exception ab){
			            		ab.printStackTrace();
			            		System.out.print("SESSION_BEGIN error!!!");
			            	}				
					if(result ==0){		
						UpdateStatusThread update = new UpdateStatusThread(sockToConnectSV, userName);//sleep 30000 ms
						new Thread(update).start();//start thread to update after 30000 ms
						writer.println(XMLGen.genGET_FRIEND_LIST());//send request get friend list### No.2
						writer.flush();
			 			line = reader.readLine();	//read friend list from server
			 			doc = XMLParseTool.genXMLDocument(line);
			 			try {			
		        			System.out.println("session friend list");
							ArrayList<Friend> list = new ArrayList<Friend>();
							NodeList nList = doc.getElementsByTagName(Protocol.FRIEND);//get 
							for (int k = 0; k < nList.getLength(); k++)
							{
								Node nNode = nList.item(k);													
								Element eElement = (Element) nNode;
								String username = eElement.getElementsByTagName(Protocol.USERNAME).item(0).getTextContent();
								String ip = eElement.getElementsByTagName(Protocol.IP).item(0).getTextContent();
								int svPort = Integer.parseInt(eElement.getElementsByTagName(Protocol.PORT).item(0).getTextContent());
								Friend newFriend = new Friend(username,ip,svPort);
								list.add(newFriend);												
								//}
		        			}
							System.out.print(String.valueOf(list.size())+ " size");
							frList = list;
							}
		            	catch(Exception ab){
		            		ab.printStackTrace();}
			    	System.out.println("FriendList hien co : "+ frList.size());	
					manageFrame = new ManageUI(sockToConnectSV,frList,userName,userConnectionPort);// tao ra giao dien cua ManageUI
					manageFrame.setVisible(true);				
					frame.setDefaultCloseOperation(1);	//HIDE ON CLOSE				
					frame.setVisible(false);			//Hide when open ManageUI
					}
					else{
						JOptionPane.showMessageDialog(null, "Check your username and password");
					}
					}
					catch (IOException e2) {
							System.out.print("Server is offline");
							JOptionPane.showMessageDialog(null, "Server is offline");
					}				
				}			
			}	
		});		
	}
	private int getAvailablePort() {
        int userConnectionPort = -1;
        try {
        	
            ServerSocket t = new ServerSocket(0); //port number of 0 means that the port number is automatically allocated
            userConnectionPort = t.getLocalPort();
            t.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return userConnectionPort;
    }
}
