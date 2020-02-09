package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

import Util.XMLGen;

public class Listening implements Runnable{
	private ServerSocket ss;
	public Listening(ServerSocket ss2)
	{		
		this.ss = ss2;
	}
	public void run() {
		 Socket s=null;
		 System.out.println("Client Host Listening......");
		 System.out.println("Client host is created with port : " + String.valueOf(ss.getLocalPort()));
		    while(!ss.isClosed()){
		        try{
		           s= ss.accept();//listening in their port, accept socket from another client
		           BufferedReader recieve = new BufferedReader(new InputStreamReader(s.getInputStream()));//take info from client active
		           String friendName = recieve.readLine();
		           HandleAChatRequest hostA = new HandleAChatRequest(s, friendName);//send socket and friend name to handler
		           Thread thread = new Thread(hostA);
		           thread.start();
		        }
		        catch(Exception e){
		        	System.out.println("Client Host connection Error ");
		        	if(ss.isClosed())
		        		break;
		    }
		    }
	}
}
class HandleAChatRequest implements Runnable{
	private Socket s;
	private String friendName;
	public HandleAChatRequest(Socket socket,String friendName)
	{
		this.s = socket;
		this.friendName = friendName;
	}
	@Override
	public void run() {
		
		try{		
         PrintWriter os = new PrintWriter(s.getOutputStream());		
         System.out.println("Connection is established to "+ s.getInetAddress().toString() + " - port : " + String.valueOf(s.getPort()) );
         int option = JOptionPane.showConfirmDialog(null, "Do you want to chat with friend have " + friendName , "Friend chat request", JOptionPane.YES_NO_OPTION);  
         if (option == 0 )
         {       	
         	 ChatFrame chatBox = new ChatFrame(s,friendName);
     		 chatBox.setVisible(true);
     		 chatBox.btnEndChat.setEnabled(true);
     		 chatBox.btnSendFile.setEnabled(true);
     		 chatBox.btnSendMess.setEnabled(true);
     		 SendCommand sendCommand = new SendCommand(s, XMLGen.genChatAccept());//send accept to client active
     		 new Thread(sendCommand).start();       	 
     		 Receiving receiver = new Receiving(s, friendName, chatBox);
     		 Thread recieveThread = new Thread(receiver);
     		 recieveThread.run();				
         }
         else
         {
         	os.println(XMLGen.genChatDeny());
         	os.flush();
         	System.out.println("Client host have decline a chat");        	
         }
		}
		catch (Exception e){}
	
	}
}
