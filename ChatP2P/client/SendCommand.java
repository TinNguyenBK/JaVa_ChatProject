package client;

import java.io.PrintWriter;
import java.net.Socket;

public class SendCommand implements Runnable
{
	Socket sockOfReceiver;
	String command;
	public SendCommand(Socket sockOfReceiver, String command)
	{
		this.sockOfReceiver = sockOfReceiver;
		this.command = command;
	}
	@Override
	public void run() {
		try{		
			if (sockOfReceiver.isClosed() ==false)
			{				
				PrintWriter pwPrintWriter  = new PrintWriter(sockOfReceiver.getOutputStream(), true);//Send request to receiver 	
				pwPrintWriter.println(command);
				pwPrintWriter.flush();	
			}
			else	
				System.out.println("Command couldn't send");		
		}
		catch(Exception ex){
			System.out.println(ex.getMessage());
		}	
	}	
}
