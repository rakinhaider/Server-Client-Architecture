package Server;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Server {

	/**
	 * @param args
	 */
	
	public static ServerSocket serverSocket;
	private static String userName;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Random random =new Random();
		int port= random.nextInt(64000);
		
		try {
			System.out.println("Connecting to server socket at port "+port);
			serverSocket= new ServerSocket(port);
			
			System.out.println("Waiting for socket");
			while(true)
			{
				Socket socket = serverSocket.accept();
				
				InputStream is=socket.getInputStream();
				OutputStream os=socket.getOutputStream();
				

				
				byte[] bytes = new byte[100];
				int readBytes=is.read(bytes);
				userName=new String(bytes,0,readBytes);
				System.out.println(userName);
				
				System.out.println(is.available());
				
				new FileOrFolderRecieverThread(socket,userName).start();
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}

}
