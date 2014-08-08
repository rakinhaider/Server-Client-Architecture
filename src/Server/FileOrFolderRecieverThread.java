package Server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

import javax.swing.plaf.metal.MetalIconFactory.FolderIcon16;

public class FileOrFolderRecieverThread extends Thread {

	private String userName;
	private Socket socket;
	private BufferedInputStream bufStream;
	private DataInputStream dis;
	public FileOrFolderRecieverThread(Socket socket,String userName) throws Exception {
		
		this.userName=userName;
		this.socket=socket;
		bufStream= new BufferedInputStream(socket.getInputStream());
		dis=new DataInputStream(bufStream);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		try {
			
			while(true){
				while(bufStream.available()==0){}
				
				File directory= new File(userName);
				directory.mkdir();
				
				recieveFolder(userName);
				
				
				/*String fileNameString= dis.readUTF();
				FileOutputStream fos=new FileOutputStream(fileNameString);
		    	BufferedOutputStream bufoutStream = new BufferedOutputStream(fos);
		    	
		    	int available = dis.readInt();
		    	
		    	byte[] buffer = new byte[available];
		    	dis.readFully(buffer,0,buffer.length);
		    	bufoutStream.write(buffer, 0, buffer.length);
		    	bufoutStream.flush();
		    	System.out.println(new String(buffer,0,buffer.length));
		    	bufoutStream.close();
		    	bufStream.close();*/
		   
			    
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	private void recieveFile(String abstractPath) throws Exception {
		// TODO Auto-generated method stub
		
		String fileNameString= dis.readUTF();
		
		
		FileOutputStream fos=new FileOutputStream(abstractPath+File.separator+fileNameString);
    	BufferedOutputStream bufoutStream = new BufferedOutputStream(fos);
    	
    	int available = dis.readInt();
    	
    	byte[] buffer = new byte[available];
    	dis.readFully(buffer,0,buffer.length);
    	bufoutStream.write(buffer, 0, buffer.length);
    	bufoutStream.flush();
    	//System.out.println(new String(buffer,0,buffer.length));
    	bufoutStream.close();
    	//bufStream.close();
	    fos.close();
	    
	    if(!Protocol.matchAll(fileNameString))
	    {
	    	File file= new File(abstractPath+File.separator+fileNameString);
	    	file.delete();
	    }
	    else
	    {
	    	System.out.println("Recieved A File:"+fileNameString);
	    }
	    
	}

	private void recieveFolder(String abstractPath) throws Exception {
		// TODO Auto-generated method stub
		while(bufStream.available()==0){}
		
		
		String choice= dis.readUTF();
		
		if(choice.equals("Folder"))
		{
			String folderName= dis.readUTF();
			//todo folder create logic
			File directory= new File(abstractPath+File.separator+folderName);
			directory.mkdir();
			int numDir=dis.readInt();
			
			for(int i = 0;i<numDir;i++)
			{
				while(bufStream.available()==0){}
				
				recieveFolder(abstractPath+File.separator+folderName);
			}
			System.out.println("Recieved A Folder:"+folderName);
		}
		else
		{
			recieveFile(abstractPath);
		}
		
		
		
	}
	
}
