package client;

import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import Util.XMLGen;
import Util.XMLParseTool;
import basic.Protocol;

class Receiving implements Runnable
{
	Socket sockOfReceiver = null;
	BufferedReader recieve = null;
	private String friendName;
	private ChatFrame chatBox;
	private byte[] bufferOfReceiver;
	private InputStream input;
	public Receiving(Socket sockOfReceiver,String friendName,ChatFrame chatBox) {
		this.chatBox = chatBox;
		this.friendName  = friendName;
		this.sockOfReceiver = sockOfReceiver;
	}
	public void run() {
		try{
		recieve = new BufferedReader(new InputStreamReader(this.sockOfReceiver.getInputStream()));
		String msgRecieved = null;
		while((msgRecieved = recieve.readLine())!= null)
		{
			System.out.println("da nhan thong tin phan hoi + \n + " + msgRecieved);
			if (XMLParseTool.isXMLCode(msgRecieved))
            {
            	Document doc = XMLParseTool.genXMLDocument(msgRecieved);
            	try {			
        			NodeList ele = doc.getElementsByTagName(Protocol.PROTOCOL);
        			for (int i = 0; i < ele.getLength(); i++)
        			{
        				
        				NodeList child = ele.item(i).getChildNodes();
        				for(int j = 0; j < child.getLength();j++)
        				{      					
        					Node node = child.item(j);					
        					if(node.getNodeType() ==Node.ELEMENT_NODE)
        					{
        						Element e = (Element) node;
        						String tagName = e.getTagName();
        						//BEGIN PARSE
        						if(tagName.contains(Protocol.CHAT_ACCEPT_TEXT))
        						{
        							
        							chatBox.btnEndChat.setEnabled(true);
        							chatBox.btnSendFile.setEnabled(true);
        							chatBox.btnSendMess.setEnabled(true);
        							chatBox.txtConversation.append("\n Your friend have accepted you request, now you can chat :) ");
        							break;
        						}
        						else if (tagName.contains(Protocol.CHAT_DENY_TEXT))
        						{
        							System.out.println("deny");
        							chatBox.txtConversation.append("\n You friend not accept to chat");
        							sockOfReceiver.close();
        							break;
        							
        						}
        						else if (tagName.contains(Protocol.CHAT_CLOSE_TEXT))
        						{
        							chatBox.btnSendFile.setEnabled(false);
        							chatBox.btnSendMess.setEnabled(false);
        							chatBox.txtConversation.append("\n End conversation");
        							sockOfReceiver.close();
        							break;
        						}
        						else if(tagName.contains(Protocol.CHAT_MESS))
        						{
        							String mess = e.getTextContent();
        							chatBox.txtConversation.append("\n " + friendName + " : "  +  mess);
        						}
        					
        						else if (tagName.contains(Protocol.FILE_REQ_ACK))
        						{
        							/*int portAck = Integer.parseInt(e.getElementsByTagName(Protocol.FILE_REQ_ACK).item(0).getTextContent());
        							System.out.println(portAck);*/
        							chatBox.txtConversation.append("\n you friend want to recieve file");
        							SendFile send = new SendFile(sockOfReceiver.getInetAddress().getHostAddress(), 3500);
        							new Thread(send).start();
        							SendCommand sendCommand = new SendCommand(sockOfReceiver, XMLGen.genFILE_DATA_END());
        							new Thread(sendCommand).start();
        						}
        						else if (tagName.contains(Protocol.FILE_REQ_NOACK_TEXT))
        						{
        							chatBox.txtConversation.append("\n you friend won't to recieve file");
        						}
        						else if (tagName.contains(Protocol.FILE_REQ))
        						{
        							int option = JOptionPane.showConfirmDialog(null, "Do you want get file from friend have " + friendName , "File trasnfer request", JOptionPane.YES_NO_OPTION);
        							if(option == 0) {
        								//int port = getAvailablePort();
        								SendCommand sendCommand = new SendCommand(sockOfReceiver, XMLGen.genFILE_REQ_ACK(3500));
        							 	new Thread(sendCommand).start();
        								System.out.println("Server recieve file is running...");  
        							    ServerSocket server;
        								try {
        									System.out.println("Server recieve file is trying...");
        									server = new ServerSocket(3500);
        									System.out.println("Server recieve file is trying hard...");
        									Socket sk = server.accept();  
        							 		System.out.println("Server accepted client");  
        							 		input = sk.getInputStream();
        							 		bufferOfReceiver = new byte[sk.getReceiveBufferSize()];
        								} catch (IOException e2)
        								{
        									
        								}
        							}else {
	        							SendCommand sendCommand = new SendCommand(sockOfReceiver, XMLGen.genFILE_REQ_NOACK());
	        							new Thread(sendCommand).start();
        							}
        						}
    							else if (tagName.contains(Protocol.FILE_DATA_END))
        						{
    						        FlowLayout layout = new FlowLayout();
    						        JPanel mainPanel = new JPanel();  
    						        mainPanel.setLayout(layout);
    								JFileChooser fileChooser = new JFileChooser();
    								fileChooser.setDialogTitle("Specify a file to save");
    								fileChooser.showOpenDialog(mainPanel);
    								File file = fileChooser.getSelectedFile();
    								FileOutputStream wr = new FileOutputStream(file);
    								int bytesReceived = 0;
    								
    						 		while((bytesReceived = input.read(bufferOfReceiver))>0)  
    						 		{       						       
    						 			wr.write(bufferOfReceiver,0,bytesReceived);  
    						 		}  
    						 		wr.close();
    						 		JOptionPane.showMessageDialog(null, "File received!");
        						}
        						}
        					}
        				}
        			}
            	catch (Exception e)
        		{
        			
        		}
            }
		}
			
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
}
