package Client;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.Color;
import java.awt.Font;

public class ClientLoginFrame extends JFrame {
	private JTextField textField;
	private JTextField txtRakin;
	private JPanel panel;
	private JTextField tfPort;
	private JLabel lblwarning;

	public ClientLoginFrame() {
		getContentPane().setLayout(null);
		System.out.println("Want to connect to Server?");
		panel = new JPanel();
		panel.setBounds(40, 41, 349, 186);
		getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblIpAddress = new JLabel("Ip Address");
		lblIpAddress.setBounds(31, 36, 81, 14);
		panel.add(lblIpAddress);

		JLabel lblPort = new JLabel("Port");
		lblPort.setBounds(31, 67, 46, 14);
		panel.add(lblPort);

		tfPort = new JTextField();
		tfPort.setBounds(139, 64, 169, 20);
		panel.add(tfPort);
		tfPort.setColumns(10);

		textField = new JTextField();
		textField.setText("127.0.0.1");
		textField.setBounds(139, 33, 169, 20);
		panel.add(textField);
		textField.setColumns(10);

		JLabel lblYourName = new JLabel("Your Roll");
		lblYourName.setBounds(31, 101, 81, 14);
		panel.add(lblYourName);

		txtRakin = new JTextField();
		txtRakin.setText("201005009");
		txtRakin.setColumns(10);
		txtRakin.setBounds(139, 95, 169, 20);
		panel.add(txtRakin);

		JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				String ipAddress = textField.getText();
				String name = txtRakin.getText();
				int port = Integer.parseInt(tfPort.getText());

				try {
					System.out.println("Trying to connect to Server.");
					Socket socket = new Socket(ipAddress, port);
					System.out.println("Connected to Server.");

					InputStream is = socket.getInputStream();
					OutputStream os = socket.getOutputStream();

					byte[] bytes = name.getBytes();
					os.write(bytes);

					DataInputStream dis = new DataInputStream(is);

					while (dis.available() == 0) {
					}

					String response = dis.readUTF();
					if (response.equals("ValidStudentId")) {
						ClientFrame clientFrame = new ClientFrame(socket);
						clientFrame.setSize(450, 300);
						clientFrame
								.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						clientFrame.setVisible(true);
						dispose();
					} else {

						String errorMessage="";
						if (response.equals("InvalidIpAddress"))
						{
							errorMessage="The ip address for you device is not allowed to connect with the given Student Id.";
						}
						else if(response.equals("InvalidStudentId"))
						{
							errorMessage="The student id you provided is invalid. Please re-enter a valid student id.";
						}
						
						JOptionPane.showMessageDialog(null, errorMessage);
						socket.close();
					}

				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// new ClientThread().start();

			}
		});
		btnConnect.setBounds(113, 152, 89, 23);
		panel.add(btnConnect);

		lblwarning = new JLabel("");
		lblwarning.setFont(new Font("Arial", Font.BOLD, 14));
		lblwarning.setForeground(Color.RED);
		lblwarning.setBounds(31, 11, 308, 14);
		panel.add(lblwarning);

	}
}
