package Client;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.border.LineBorder;
import java.awt.Panel;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;

import Server.Configurations;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientFrame extends JFrame {
	private Socket socket;
	private JTextField tfFilePath;
	private JTextField tfFolderPath;
	private JFileChooser fileChooser;
	private JFileChooser folderChooser;
	private Configurations configurations;
	private JLabel lblWarning;

	public ClientFrame(final Socket socket) {
		getContentPane().setLayout(null);

		this.socket = socket;

		fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(true);
		folderChooser = new JFileChooser();
		folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		folderChooser.setMultiSelectionEnabled(true);

		JPanel panel = new JPanel();
		panel.setName("Client");
		panel.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "File Sender",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(70,
						130, 180)));

		panel.setForeground(new Color(0, 0, 0));
		panel.setBounds(40, 28, 351, 204);
		getContentPane().add(panel);
		panel.setLayout(null);

		tfFilePath = new JTextField();
		tfFilePath.setBounds(10, 41, 239, 20);
		tfFilePath
				.setText("E:\\Android Workplace\\Server-Client-Architecture\\src\\Client\\ClientFrame.java");
		panel.add(tfFilePath);
		tfFilePath.setColumns(10);

		JLabel lblFilePath = new JLabel("File Path");
		lblFilePath.setBounds(10, 16, 81, 14);
		panel.add(lblFilePath);

		JButton btnSendAFile = new JButton("Send A File");
		btnSendAFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String path = tfFilePath.getText();

				// FileInputStream fis = null;
				try {

					// System.out.println(bufStream.available());

					OutputStream os = socket.getOutputStream();

					DataOutputStream dos = new DataOutputStream(os);
					dos.writeUTF("ConfigurationRequest");

					DataInputStream dis = new DataInputStream(socket
							.getInputStream());

					while (dis.available() == 0) {
					}

					configurations = new Configurations();

					int length = dis.readInt();
					String[] extensions = new String[length];
					for (int i = 0; i < length; i++) {
						extensions[i] = dis.readUTF();
					}
					configurations.setExtensions(extensions);
					configurations.setNumberOfFiles(dis.readInt());
					configurations.setMaximumSize(dis.readLong());
					configurations.setMinId(dis.readLong());
					configurations.setMaxId(dis.readLong());
					configurations.setFolderAllowed(dis.readBoolean());

					System.out.println(configurations.toString());

					File file = new File(path);

					if (configurations.isValidExtension(file.getName())
							&& configurations.isValidSize(file.length())) {
						dos.writeUTF("Valid");

						transmitFile(path);

						JOptionPane.showMessageDialog(null,
								"Successfully Transfered.");
					} else {
						dos.writeUTF("Invalid");
						String msg = "";
						if (configurations.isValidExtension(file.getName())) {
							msg.concat("Invalid Extension!!!!");
						}
						if (configurations.isValidSize(file.length())) {
							msg.concat("Very Large File!!!!");
						}

						JOptionPane.showMessageDialog(null, msg);

					}

				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}

		});
		btnSendAFile.setBounds(89, 72, 152, 23);
		panel.add(btnSendAFile);

		JLabel lblDirectiry = new JLabel("Directory");
		lblDirectiry.setBounds(10, 103, 166, 14);
		panel.add(lblDirectiry);

		tfFolderPath = new JTextField();
		tfFolderPath.setBounds(10, 128, 239, 20);
		tfFolderPath
				.setText("E:\\Android Workplace\\Server-Client-Architecture\\src");
		panel.add(tfFolderPath);
		tfFolderPath.setColumns(10);

		JButton btnSendAFolder = new JButton("Send A Folder");
		btnSendAFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				String path = tfFolderPath.getText();

				// FileInputStream fis = null;
				try {

					OutputStream os = socket.getOutputStream();

					DataOutputStream dos = new DataOutputStream(os);
					dos.writeUTF("ConfigurationRequest");

					DataInputStream dis = new DataInputStream(socket
							.getInputStream());

					while (dis.available() == 0) {
					}

					configurations = new Configurations();

					int length = dis.readInt();
					String[] extensions = new String[length];
					for (int i = 0; i < length; i++) {
						extensions[i] = dis.readUTF();
					}
					configurations.setExtensions(extensions);
					configurations.setNumberOfFiles(dis.readInt());
					configurations.setMaximumSize(dis.readLong());
					configurations.setMinId(dis.readLong());
					configurations.setMaxId(dis.readLong());
					configurations.setFolderAllowed(dis.readBoolean());

					System.out.println(configurations.toString());

					File file = new File(path);

					if (configurations.isValidSize(file.length())
							&& configurations.isFolderAllowed()) {

						dos.writeUTF("Valid");
						// lblWarning.setText("Success");
						transmitFolder(path);

						JOptionPane.showMessageDialog(null,
								"Successfully Transfered.");
					} else {
						String msg = "";
						dos.writeUTF("Invalid");
						if (configurations.isValidExtension(file.getName())) {
							msg += "Invalid Extension!!!!";
						}
						if (configurations.isValidSize(file.length())) {
							msg += "Very Large File!!!!";
						}
						if (configurations.isFolderAllowed()) {
							msg += "Folder Not Allowed!!!!";
						}
						// lblWarning.setText(msg);

						JOptionPane.showMessageDialog(null, msg);

					}

					System.out.println("end");

				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.out.println("end");
			}
		});
		btnSendAFolder.setBounds(89, 159, 152, 23);
		panel.add(btnSendAFolder);

		JButton btnBrowseFile = new JButton("Browse");
		btnBrowseFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (fileChooser.showOpenDialog(getFrames()[0]) == JFileChooser.APPROVE_OPTION) {
					tfFilePath.setText(fileChooser.getSelectedFile()
							.getAbsolutePath());
				}
			}
		});
		btnBrowseFile.setBounds(252, 40, 89, 23);
		panel.add(btnBrowseFile);

		JButton btnBrowseFolder = new JButton("Browse");
		btnBrowseFolder.setBounds(252, 127, 89, 23);
		panel.add(btnBrowseFolder);

		btnBrowseFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (folderChooser.showOpenDialog(getFrames()[0]) == JFileChooser.APPROVE_OPTION) {
					tfFolderPath.setText(folderChooser.getSelectedFile()
							.getAbsolutePath());
				}
			}
		});

	}

	private void transmitFile(String path) throws Exception {
		// TODO Auto-generated method stub

		int read = 0, firstByte = 0;
		File file = new File(path);
		FileInputStream fis = new FileInputStream(file);
		BufferedInputStream bufStream = new BufferedInputStream(fis);

		OutputStream os = socket.getOutputStream();
		DataOutputStream dos = new DataOutputStream(os);

		DataInputStream dis = new DataInputStream(socket.getInputStream());

		dos.writeUTF("File");
		System.out.println("File");
		byte[] bytes = new byte[512];

		while (true) {
			read = bufStream.read(bytes);
			dos.writeUTF(file.getName());
			System.out.println(file.getName());
			dos.writeInt(firstByte);
			System.out.println(firstByte);
			if (read == -1)
				read = 0;
			dos.writeInt(read);
			System.out.println(read);
			dos.write(bytes, 0, read);
			// System.out.println(new String(bytes,0,read));

			while (dis.available() == 0) {
			}

			if (dis.readUTF().equals("Acknowledgement")) {
				System.out.println("Acknowledgement");
				if (read < 512) {
					System.out.println("loop broken");
					break;
				}
				firstByte += read;
			}

		}
		// dos.flush();

		/*
		 * dos.writeInt(bufStream.available()); byte[] bytes = new
		 * byte[bufStream.available()]; bufStream.read(bytes, 0, bytes.length);
		 * //System.out.print(new String(bytes, 0, bytes.length));
		 * dos.write(bytes, 0, bytes.length); dos.flush();
		 */

		/*
		 * byte[] bytes = new byte[1024]; int read; while ((read =
		 * bufStream.read(bytes)) != -1) { //
		 * http://stackoverflow.com/questions/
		 * 6101916/simple-java-file-transfer-program-problem os.write(bytes, 0,
		 * read);
		 * 
		 * }
		 */
	}

	private void transmitFolder(String path) throws Exception {
		File fileOrDir = new File(path);
		// System.out.println(path);
		DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

		String absolutePath = fileOrDir.getAbsolutePath();
		if (fileOrDir.isDirectory()) {
			dos.writeUTF("Folder");
			dos.writeUTF(fileOrDir.getName());
			dos.writeInt(fileOrDir.list().length);
			System.out.println("Folder");
			System.out.println(fileOrDir.getName());
			System.out.println(fileOrDir.list().length);
			for (String s : fileOrDir.list()) {
				// System.out.println(absolutePath + s);
				transmitFolder(absolutePath + File.separator + s);

			}
		} else {
			if (configurations.isValidExtension(fileOrDir.getName())
					&& configurations.isValidSize(fileOrDir.length())) {
				transmitFile(path);
			} else
				JOptionPane.showMessageDialog(null, "This File Named "
						+ fileOrDir.getName()
						+ " Is Not Allowed. Send Another File");
		}
	}

}
