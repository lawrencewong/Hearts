import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;


public class Connection {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Connection window = new Connection();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Connection() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 429, 139);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblHeartsInsertThe = new JLabel("Hearts! Insert the IP of the game server:");
		lblHeartsInsertThe.setBounds(56, 11, 206, 14);
		frame.getContentPane().add(lblHeartsInsertThe);
	}
}
