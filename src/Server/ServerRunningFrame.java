package Server;

import javax.jws.Oneway;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import java.awt.Color;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Random;
import java.awt.Font;
import javax.swing.border.EmptyBorder;

public class ServerRunningFrame extends JFrame implements Runnable {
	public static ServerSocket serverSocket;
	private static Long studentId;
	private static HashMap<InetAddress, Integer> ipIdMap;
	private static JTextArea textArea;
	private Configurations configurations;
	Thread thread;
	
	public ServerRunningFrame(Configurations configurations) {
		getContentPane().setLayout(null);
		
		this.configurations=configurations;
		
		setSize(500,500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		thread= new Thread(this);
		
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel.setBackground(Color.WHITE);
		panel.setForeground(Color.BLACK);
		panel.setBounds(10, 11, 414, 239);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		textArea = new JTextArea();
		textArea.setBackground(Color.PINK);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setBounds(10, 11, 394, 217);
		panel.add(textArea);
		
		thread.start();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		
		ipIdMap= new HashMap<InetAddress, Integer>();
		
		Random random =new Random();
		int port= random.nextInt(64000);
		
		
		try {
			System.out.println("Connecting to server socket at port "+port);
			textArea.append("Connecting to server socket at port "+port+"\n");
			//repaint();
			serverSocket= new ServerSocket(port);
			
			System.out.println("Waiting for socket");
			textArea.append("Waiting for socket\n");
			while(true)
			{
				Socket socket = serverSocket.accept();
				
				InputStream is=socket.getInputStream();
				OutputStream os=socket.getOutputStream();
			
				
				byte[] bytes = new byte[100];
				int readBytes=is.read(bytes);
				studentId= new Long(new String(bytes,0,readBytes));
				
				DataOutputStream dos= new DataOutputStream(os);
				System.out.println(configurations.toString());
				if(configurations.isValidStudentId(studentId))
				{
					dos.writeUTF("ValidStudentId");
					File directory= new File(configurations.rootPath+File.separator+studentId.toString());
					directory.mkdir();
					new FileOrFolderRecieverThread(socket,studentId,configurations).start();
				}
				else 
				{
					dos.writeUTF("InvalidStudentId");
					socket.close();
				}
				//userName=new String(bytes,0,readBytes);
				//System.out.println(userName);
				println(studentId.toString());
				
				
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void println(String s)
	{
		textArea.append(s+"\n");
	}
}
