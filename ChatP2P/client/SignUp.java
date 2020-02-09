package client;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JOptionPane;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPasswordField;

import Util.XMLGen;
import basic.UserAccount;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class SignUp extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtName;
	private JPasswordField txtPass;
	private Socket sock = null;
	BufferedReader reader ;
	private JTextField txtIP;
	private JTextField txtServerPort;
	public SignUp() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		SpringLayout sl_contentPane = new SpringLayout();
		contentPane.setLayout(sl_contentPane);
		setVisible(true);
		JLabel label = new JLabel("Username");
		sl_contentPane.putConstraint(SpringLayout.NORTH, label, 30, SpringLayout.NORTH, contentPane);
		contentPane.add(label);
		
		JLabel label_1 = new JLabel("Password");
		sl_contentPane.putConstraint(SpringLayout.NORTH, label_1, 34, SpringLayout.SOUTH, label);
		sl_contentPane.putConstraint(SpringLayout.EAST, label_1, 0, SpringLayout.EAST, label);
		contentPane.add(label_1);
		
		txtName = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.EAST, txtName, -160, SpringLayout.EAST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, label, -59, SpringLayout.WEST, txtName);
		sl_contentPane.putConstraint(SpringLayout.NORTH, txtName, -3, SpringLayout.NORTH, label);
		txtName.setColumns(10);
		contentPane.add(txtName);
		
		JButton btCreate = new JButton("Create");
		contentPane.getRootPane().setDefaultButton(btCreate);
		sl_contentPane.putConstraint(SpringLayout.NORTH, btCreate, 181, SpringLayout.NORTH, contentPane);
		btCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = txtName.getText();
				String pass = txtPass.getText();
				String IPServer = txtIP.getText();
				int serverPort = Integer.parseInt(txtServerPort.getText());
				UserAccount usr = new UserAccount(name,pass);
				String regRequest = XMLGen.genREG(usr);
				try {
					sock = new Socket(IPServer,serverPort);
					PrintWriter writer = new PrintWriter(new DataOutputStream(sock.getOutputStream()));
					BufferedReader reader= new BufferedReader(new InputStreamReader(sock.getInputStream()));
					writer.println(regRequest);//send sign up request
					writer.flush();
					String result = reader.readLine();
					System.out.println(result);
					if (result.contains(XMLGen.genREG_RESULT(1))==true)
					{
						JOptionPane.showMessageDialog(null, "Sign up fail");
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Sign up sucess");
					}
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		sl_contentPane.putConstraint(SpringLayout.WEST, btCreate, 73, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, btCreate, -34, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, btCreate, -256, SpringLayout.EAST, contentPane);
		contentPane.add(btCreate);
		
		JButton btExit = new JButton("Exit");
		btExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(sock != null && !sock.isClosed()) {
						sock.close();
						System.out.println("Socket closed = ");
					}
					setVisible(false);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		sl_contentPane.putConstraint(SpringLayout.NORTH, btExit, -2, SpringLayout.NORTH, btCreate);
		sl_contentPane.putConstraint(SpringLayout.WEST, btExit, 63, SpringLayout.EAST, btCreate);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, btExit, -31, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, btExit, -107, SpringLayout.EAST, contentPane);
		contentPane.add(btExit);
		
		txtPass = new JPasswordField();
		sl_contentPane.putConstraint(SpringLayout.NORTH, txtPass, -3, SpringLayout.NORTH, label_1);
		sl_contentPane.putConstraint(SpringLayout.WEST, txtPass, 0, SpringLayout.WEST, txtName);
		sl_contentPane.putConstraint(SpringLayout.EAST, txtPass, -160, SpringLayout.EAST, contentPane);
		contentPane.add(txtPass);
		
		txtIP = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.SOUTH, txtIP, 0, SpringLayout.SOUTH, txtName);
		sl_contentPane.putConstraint(SpringLayout.EAST, txtIP, -21, SpringLayout.EAST, contentPane);
		txtIP.setColumns(10);
		contentPane.add(txtIP);
		
		txtServerPort = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.NORTH, txtServerPort, 48, SpringLayout.SOUTH, txtIP);
		sl_contentPane.putConstraint(SpringLayout.WEST, txtServerPort, 0, SpringLayout.WEST, txtIP);
		txtServerPort.setColumns(10);
		contentPane.add(txtServerPort);
		
		JLabel lblIpServer = new JLabel("IP server");
		sl_contentPane.putConstraint(SpringLayout.SOUTH, lblIpServer, -6, SpringLayout.NORTH, txtIP);
		sl_contentPane.putConstraint(SpringLayout.EAST, lblIpServer, -60, SpringLayout.EAST, contentPane);
		contentPane.add(lblIpServer);
		
		JLabel lblPort = new JLabel("Port");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblPort, 17, SpringLayout.SOUTH, txtIP);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblPort, 0, SpringLayout.WEST, txtIP);
		contentPane.add(lblPort);
	}

}
