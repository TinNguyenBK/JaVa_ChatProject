package client;

import java.io.PrintWriter;
import java.net.Socket;
import Util.XMLGen;

class SendChat implements Runnable
{
	Socket sock=null;
	PrintWriter print=null;
	private String mess;
	private ChatFrame chatBox;
	public SendChat(Socket sock, String mess,ChatFrame chatBox)
	{
		this.mess = mess;
		this.sock = sock;
		this.chatBox = chatBox;
	}
	public void run(){
		try{
			if(sock.isClosed()==false)
			{
				this.print = new PrintWriter(sock.getOutputStream(), true);	
				this.print.println(XMLGen.genChatMess(mess));
				this.print.flush();
				chatBox.txtConversation.append("\n Me : " + mess);
			}
			else
			{
				chatBox.txtConversation.append("\n Error : Can't send this mess");
			}		
		}
	catch(Exception e){System.out.println(e.getMessage());}
	}
}
