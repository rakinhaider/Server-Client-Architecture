package Server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

import javax.swing.JOptionPane;
import javax.swing.plaf.metal.MetalIconFactory.FolderIcon16;

public class FileOrFolderRecieverThread extends Thread {

	private Long studentId;
	private Socket socket;
	private BufferedInputStream bufStream;
	private DataInputStream dis;
	private Configurations configurations;

	public FileOrFolderRecieverThread(Socket socket, Long studentId,
			Configurations configurations) throws Exception {

		this.configurations = configurations;
		this.studentId = studentId;
		this.socket = socket;
		bufStream = new BufferedInputStream(socket.getInputStream());
		dis = new DataInputStream(bufStream);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		try {

			while (true) {
				while (bufStream.available() == 0) {
				}

				String configreq = dis.readUTF();
				ServerRunningFrame.println(configreq);
				if (configreq.equals("ConfigurationRequest")) {

					DataOutputStream dOutputStream = new DataOutputStream(
							socket.getOutputStream());

					String[] extensions = configurations.getExtensions();
					dOutputStream.writeInt(extensions.length);
					for (String s : extensions) {
						dOutputStream.writeUTF(s);
					}

					dOutputStream.writeInt(configurations.numberOfFiles);
					dOutputStream.writeLong(configurations.maximumSize);
					dOutputStream.writeLong(configurations.minId);
					dOutputStream.writeLong(configurations.maxId);
					dOutputStream.writeBoolean(configurations.folderAllowed);
					
					
					while (dis.available() == 0) {
					}
					dis.readUTF();
					dOutputStream.writeInt(ServerRunningFrame.idNumOfFilesMap.get(studentId));
					if (dis.readUTF().equals("Valid"))
						recieveFolder(configurations.rootPath + File.separator
								+ studentId.toString());
				}

				/*
				 * String fileNameString= dis.readUTF(); FileOutputStream
				 * fos=new FileOutputStream(fileNameString);
				 * BufferedOutputStream bufoutStream = new
				 * BufferedOutputStream(fos);
				 * 
				 * int available = dis.readInt();
				 * 
				 * byte[] buffer = new byte[available];
				 * dis.readFully(buffer,0,buffer.length);
				 * bufoutStream.write(buffer, 0, buffer.length);
				 * bufoutStream.flush(); System.out.println(new
				 * String(buffer,0,buffer.length)); bufoutStream.close();
				 * bufStream.close();
				 */

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void recieveFile(String abstractPath) throws Exception {
		// TODO Auto-generated method stub

		DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

		// check overwrite
		String fileNameString = dis.readUTF();
		if (fileExists(abstractPath, fileNameString)) {
			dos.writeUTF("Exists");
			while (dis.available() == 0) {
			}
			String choice = dis.readUTF();
			System.out.println("choice :" + choice);
			if (!choice.equals("Overwrite"))
				return;
		} else
			dos.writeUTF("DoesNotExists");

		while (dis.available() == 0) {
		}
		fileNameString = dis.readUTF();
		System.out.println(fileNameString);
		FileOutputStream fos = new FileOutputStream(abstractPath
				+ File.separator + fileNameString);
		BufferedOutputStream bufoutStream = new BufferedOutputStream(fos);

		while (bufStream.available() == 0) {
		}

		byte[] bytes = new byte[512000];
		int firstByte = dis.readInt();
		int segmentSize = dis.readInt();
		System.out.println(firstByte);
		System.out.println(segmentSize);
		while (true) {
			dis.read(bytes, 0, segmentSize);
			String s = new String(bytes, 0, 512);
			bufoutStream.write(bytes, 0, segmentSize);
			bufoutStream.flush();

			dos.writeUTF("Acknowledgement");
			if (segmentSize < 512) {

				break;
			}
			while (dis.available() == 0) {
			}

			fileNameString = dis.readUTF();
			firstByte = dis.readInt();
			segmentSize = dis.readInt();

			System.out.println(fileNameString);
			System.out.println(firstByte);
			System.out.println(segmentSize);

		}
		System.out.println("loop break");
		bufoutStream.flush();
		bufoutStream.close();

		fos.close();
		dis.readUTF();

		File file = new File(abstractPath + File.separator + fileNameString);
		FileInputStream fis = new FileInputStream(file);

		String checksum = dis.readUTF();
		if (!isEqualChecksum(fis, checksum))
			JOptionPane
					.showMessageDialog(null,
							"The checksums of the recieved and sent file are not equal.");
		else
			JOptionPane
					.showMessageDialog(null,
							"Both the checksum of the recieved and sent files are equal.");

		ServerRunningFrame.idNumOfFilesMap.put(studentId,
				ServerRunningFrame.idNumOfFilesMap.get(studentId) + 1);
		System.out.println("Recieved A File:" + fileNameString);

	}

	private boolean isEqualChecksum(InputStream fos, String checkSum)
			throws Exception {
		// TODO Auto-generated method stub

		String curCheckSum = Checksum.md5(fos);
		System.out.println(checkSum);
		System.out.println(curCheckSum);
		return curCheckSum.equals(checkSum);
	}

	private void recieveFolder(String abstractPath) throws Exception {
		// TODO Auto-generated method stub
		while (bufStream.available() == 0) {
		}

		String choice = dis.readUTF();
		System.out.println(choice);

		if (choice.equals("Folder")) {
			String folderName = dis.readUTF();
			System.out.println(folderName);

			File directory = new File(abstractPath + File.separator
					+ folderName);
			directory.mkdir();

			int numDir = dis.readInt();
			System.out.println(numDir);
			for (int i = 0; i < numDir; i++) {
				while (bufStream.available() == 0) {
				}

				recieveFolder(abstractPath + File.separator + folderName);
			}
			System.out.println("Recieved A Folder:" + folderName);
		} else {
			recieveFile(abstractPath);
		}

	}

	public boolean fileExists(String abstractpath, String fileName) {
		File directory = new File(abstractpath + File.separator + fileName);
		return directory.exists();
	}

}