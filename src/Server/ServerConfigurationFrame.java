package Server;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JList;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ServerConfigurationFrame extends JFrame {
	private JTextField tfRootDirectory;
	private JTextField tfFileTypes;
	private JTextField tfNumbeOfFiles;
	private JTextField tfFileSize;
	private JTextField tfMinId;
	private JTextField tfMaxId;
	private JTextField tfNewType;
	
	private JFileChooser jFileChooser;
	
	public ServerConfigurationFrame() {
		getContentPane().setLayout(null);
		
		jFileChooser= new JFileChooser();
		jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 464, 313);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Root Directory :");
		lblNewLabel.setBounds(10, 11, 97, 14);
		panel.add(lblNewLabel);
		
		tfRootDirectory = new JTextField();
		tfRootDirectory.setBounds(117, 8, 241, 20);
		panel.add(tfRootDirectory);
		tfRootDirectory.setText("C:\\Users\\Rakin Haider\\Desktop\\New folder");
		tfRootDirectory.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("File Types :");
		lblNewLabel_1.setBounds(9, 36, 78, 14);
		panel.add(lblNewLabel_1);
		
		tfFileTypes = new JTextField();
		tfFileTypes.setBounds(117, 33, 241, 20);
		tfFileTypes.setEditable(false);
		panel.add(tfFileTypes);
		tfFileTypes.setColumns(10);
		
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (jFileChooser.showOpenDialog(getFrames()[0]) == JFileChooser.APPROVE_OPTION) {
					tfRootDirectory.setText(jFileChooser.getSelectedFile()
							.getAbsolutePath());
				}
			}
		});
		btnBrowse.setBounds(368, 7, 86, 23);
		panel.add(btnBrowse);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tfFileTypes.getText().equals(""))
					tfFileTypes.setText(tfNewType.getText());
				else tfFileTypes.setText(tfFileTypes.getText()+";"+tfNewType.getText());
				tfNewType.setText("");
			}
		});
		btnAdd.setBounds(368, 66, 86, 23);
		panel.add(btnAdd);
		
		JLabel lblNumberOfFiles = new JLabel("Number Of FIles");
		lblNumberOfFiles.setBounds(10, 103, 78, 14);
		panel.add(lblNumberOfFiles);
		
		tfNumbeOfFiles = new JTextField();
		tfNumbeOfFiles.setText("10");
		tfNumbeOfFiles.setBounds(117, 100, 241, 20);
		panel.add(tfNumbeOfFiles);
		tfNumbeOfFiles.setColumns(10);
		
		JLabel lblFolderAllowed = new JLabel("Folder Allowed ?");
		lblFolderAllowed.setBounds(10, 148, 78, 14);
		panel.add(lblFolderAllowed);
		
		final JCheckBox chckbxYes = new JCheckBox("Yes");
		chckbxYes.setSelected(true);
		chckbxYes.setBounds(117, 144, 97, 23);
		panel.add(chckbxYes);
		
		JLabel lblMaximumSize = new JLabel("Maximum Size :");
		lblMaximumSize.setBounds(10, 189, 78, 14);
		panel.add(lblMaximumSize);
		
		tfFileSize = new JTextField();
		tfFileSize.setText("2097152");
		tfFileSize.setBounds(128, 186, 86, 20);
		panel.add(tfFileSize);
		tfFileSize.setColumns(10);

		
		JLabel lblIdRange = new JLabel("Id Range :");
		lblIdRange.setBounds(10, 233, 78, 14);
		panel.add(lblIdRange);
		
		tfMinId = new JTextField();
		tfMinId.setText("201005001");
		tfMinId.setBounds(128, 230, 86, 20);
		panel.add(tfMinId);
		tfMinId.setColumns(10);
		
		tfMaxId = new JTextField();
		tfMaxId.setText("201005120");
		tfMaxId.setBounds(249, 230, 86, 20);
		panel.add(tfMaxId);
		tfMaxId.setColumns(10);
		
		JLabel label = new JLabel("-");
		label.setFont(new Font("Tahoma", Font.BOLD, 13));
		label.setBounds(227, 232, 46, 14);
		panel.add(label);
		
		JButton btnStartServer = new JButton("Start Server");
		btnStartServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String rootPath=tfRootDirectory.getText();
				String fileType=tfFileTypes.getText();
				String[] extensions=fileType.split(";");
				int numberOfFiles=Integer.parseInt(tfNumbeOfFiles.getText());
				long maxFileSize= Long.parseLong(tfFileSize.getText());
				long maxId =Long.parseLong(tfMaxId.getText());
				long minId= Long.parseLong(tfMinId.getText());
				Configurations configurations= 
						new Configurations(
								rootPath,
								extensions,
								numberOfFiles,
								maxFileSize,
								minId,
								maxId,
								chckbxYes.isSelected()
								);
				
				dispose();
				ServerRunningFrame serverRunningFrame=new ServerRunningFrame(configurations);
				
			}
		});
		btnStartServer.setBounds(182, 279, 106, 23);
		panel.add(btnStartServer);
		
		JLabel lblNewType = new JLabel("New Type :");
		lblNewType.setBounds(10, 70, 68, 14);
		panel.add(lblNewType);
		
		tfNewType = new JTextField();
		tfNewType.setBounds(117, 67, 241, 20);
		panel.add(tfNewType);
		tfNewType.setColumns(10);
	}
}
