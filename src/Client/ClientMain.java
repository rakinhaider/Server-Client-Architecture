package Client;

import javax.swing.JFrame;

public class ClientMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ClientLoginFrame clientLoginFrame=new ClientLoginFrame();
		clientLoginFrame.setSize(450,300);
		clientLoginFrame.setVisible(true);
		clientLoginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}

}
