package client;

import java.net.Socket;
import Util.XMLGen;

public class UpdateStatusThread implements Runnable
{
	private Socket socket;
	private String username;
	public UpdateStatusThread(Socket socket,String username) {
		this.socket= socket;
		this.username = username;
	}
	@Override
	public void run() {
		while (!socket.isClosed())
		{
			System.out.println(" Update status to server");
			try {
				Thread.sleep(30000);//check every 30s
			} catch (InterruptedException e) {
			
				e.printStackTrace();
			}
			SendCommand send= new SendCommand(socket,XMLGen.genKEEP_ALIVE(username, 0));//send request to check alive
			new Thread(send).start();
		}
		
	}
	
}