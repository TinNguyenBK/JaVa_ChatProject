package server;

import javax.swing.JFrame;
import javax.swing.SpringLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;


public class ServerFrame {

	private JFrame FrameServer;
	private JTextField txtPort;
	private JButton btnStartServer;
	private JButton btnChoosefile;
	public JFileChooser fileChooser;
	public JTextArea txtCommand;
	private JLabel txtURL;
	public String filePath;
	private int port;
	private Thread thread;
	private SpringLayout springLayout;
	private JLabel lblDatabaseFile;
	private JLabel lblServerPort;
	private JTextArea textArea;
	private JButton btnStartServer_1;
	private JButton btnChooseDatabase;
	private boolean sv_isRun = false;
	private ServerSocket svsock = null;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) throws UnknownHostException,
	IOException {
		new ServerFrame().FrameServer.setVisible(true);
	}
	/**
	 * Create the application.
	 */
	public ServerFrame() {		
		initialize();
		fileChooser = new JFileChooser();
		textArea = new JTextArea();
		
		springLayout.putConstraint(SpringLayout.NORTH, textArea, 32, SpringLayout.SOUTH, lblServerPort);
		springLayout.putConstraint(SpringLayout.WEST, textArea, 24, SpringLayout.WEST, FrameServer.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, textArea, -100, SpringLayout.SOUTH, FrameServer.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, textArea, 296, SpringLayout.WEST, FrameServer.getContentPane());
		textArea.setLineWrap(true);
		this.txtCommand = textArea;
		JScrollPane scrollPane = new JScrollPane();
		springLayout.putConstraint(SpringLayout.EAST, scrollPane, -60, SpringLayout.EAST, FrameServer.getContentPane());
		scrollPane.setViewportView(textArea);
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 26, SpringLayout.SOUTH, lblServerPort);
		springLayout.putConstraint(SpringLayout.WEST, scrollPane, 29, SpringLayout.WEST, FrameServer.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, -51, SpringLayout.SOUTH, FrameServer.getContentPane());
		FrameServer.getContentPane().add(scrollPane);
		txtCommand.setEditable(false);
		
		JButton btnClearLog = new JButton("Clear LOG");
		springLayout.putConstraint(SpringLayout.NORTH, btnClearLog, -4, SpringLayout.NORTH, lblServerPort);
		springLayout.putConstraint(SpringLayout.WEST, btnClearLog, 83, SpringLayout.EAST, btnStartServer_1);
		btnClearLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textArea.setText("");
			}
		});
		FrameServer.getContentPane().add(btnClearLog);		
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		FrameServer = new JFrame();
		FrameServer.setTitle("P2P CHATTING SERVER");
		FrameServer.setBounds(100, 100, 727, 475);
		FrameServer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		springLayout = new SpringLayout();
		FrameServer.getContentPane().setLayout(springLayout);
		
		btnChooseDatabase = new JButton("Choose database");
		springLayout.putConstraint(SpringLayout.EAST, btnChooseDatabase, -257, SpringLayout.EAST, FrameServer.getContentPane());
		this.btnChoosefile = btnChooseDatabase;
		btnChooseDatabase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc= new JFileChooser();			
				if (e.getSource() == btnChooseDatabase) {
					int returnVal = fc.showOpenDialog(fileChooser);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
			            File file = fc.getSelectedFile();
			            filePath = file.getPath();
			            txtURL.setText(filePath);
			            if(file.getName().contains(".xml"))
			            	btnStartServer.setEnabled(true);
			            else
			            	JOptionPane.showMessageDialog(null, "Wrong file type");
					}
				}				
			}
		});
		FrameServer.getContentPane().add(btnChooseDatabase);
		
		lblDatabaseFile = new JLabel("Database file");
		springLayout.putConstraint(SpringLayout.NORTH, btnChooseDatabase, -4, SpringLayout.NORTH, lblDatabaseFile);
		springLayout.putConstraint(SpringLayout.WEST, lblDatabaseFile, 10, SpringLayout.WEST, FrameServer.getContentPane());
		FrameServer.getContentPane().add(lblDatabaseFile);
		
		JLabel txtURL = new JLabel("No file chosen");
		springLayout.putConstraint(SpringLayout.NORTH, txtURL, 0, SpringLayout.NORTH, lblDatabaseFile);
		springLayout.putConstraint(SpringLayout.WEST, txtURL, 18, SpringLayout.EAST, lblDatabaseFile);
		springLayout.putConstraint(SpringLayout.EAST, txtURL, 372, SpringLayout.EAST, lblDatabaseFile);
		FrameServer.getContentPane().add(txtURL);
		this.txtURL = txtURL;
		lblServerPort = new JLabel("Server port");
		springLayout.putConstraint(SpringLayout.NORTH, lblServerPort, 50, SpringLayout.NORTH, FrameServer.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, lblDatabaseFile, -17, SpringLayout.NORTH, lblServerPort);
		springLayout.putConstraint(SpringLayout.WEST, lblServerPort, 0, SpringLayout.WEST, lblDatabaseFile);
		FrameServer.getContentPane().add(lblServerPort);
		
		txtPort = new JTextField();
		txtPort.setText("6000");
		txtPort.setEditable(false);
		springLayout.putConstraint(SpringLayout.WEST, txtPort, 25, SpringLayout.EAST, lblServerPort);
		springLayout.putConstraint(SpringLayout.SOUTH, txtPort, 0, SpringLayout.SOUTH, lblServerPort);
		FrameServer.getContentPane().add(txtPort);
		txtPort.setColumns(10);
		
		btnStartServer_1 = new JButton("Start server");
		FrameServer.getRootPane().setDefaultButton(btnStartServer_1);
		springLayout.putConstraint(SpringLayout.NORTH, btnStartServer_1, -4, SpringLayout.NORTH, lblServerPort);
		springLayout.putConstraint(SpringLayout.WEST, btnStartServer_1, 0, SpringLayout.WEST, btnChooseDatabase);
		btnStartServer_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startServer(e);
			}
		});
		this.btnStartServer = btnStartServer_1;
		btnStartServer_1.setEnabled(false);
		FrameServer.getContentPane().add(btnStartServer_1);				
	}
	private void startServer(java.awt.event.ActionEvent evt)
	{	
		try
		{
			port = Integer.parseInt(txtPort.getText());
			btnChoosefile.setEnabled(false);
			if(!sv_isRun) {
				try {
					svsock = new ServerSocket(port);
				} catch (IOException e) {
					System.out.println("Port invalid");
					txtCommand.append( "\n Port invalid, Please choose another port!!!");
					return;
				}
				thread = new Thread(new Server(this,svsock));
				thread.start();
				sv_isRun = !sv_isRun;
				btnStartServer_1.setText("Stop server");
			} else
				try {
					svsock.close();
					sv_isRun = !sv_isRun;
					btnStartServer_1.setText("Start server");
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		catch( NumberFormatException e)
		{
			txtPort.setText("");
			JOptionPane.showMessageDialog(null, "Please re enter the port number");
		}	
	}
}