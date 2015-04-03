import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.UnknownHostException;


public class Connection {

	private JFrame frmHearts;
	private final JTextField serverIPtextField = new JTextField();
	private JTextField clientUsernametextField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Connection window = new Connection();
					window.frmHearts.setVisible(true);
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
		frmHearts = new JFrame();
		frmHearts.setTitle("Hearts");
		frmHearts.setResizable(false);
		frmHearts.setBounds(100, 100, 429, 267);
		frmHearts.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmHearts.getContentPane().setLayout(null);
		
		JLabel lblHeartsInsertThe = new JLabel("Hearts! Insert the IP of the game server:");
		lblHeartsInsertThe.setBounds(56, 44, 296, 14);
		frmHearts.getContentPane().add(lblHeartsInsertThe);
		serverIPtextField.setBounds(56, 69, 296, 20);
		frmHearts.getContentPane().add(serverIPtextField);
		serverIPtextField.setColumns(10);
		
		clientUsernametextField = new JTextField();
		clientUsernametextField.setBounds(56, 138, 296, 20);
		frmHearts.getContentPane().add(clientUsernametextField);
		clientUsernametextField.setColumns(10);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(56, 100, 296, 50);
		frmHearts.getContentPane().add(lblUsername);
		
		JButton connectbtnNewButton = new JButton("Connect");
		connectbtnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frmHearts.setVisible(false);
				try {
					new Client(serverIPtextField.getText(), clientUsernametextField.getText());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		connectbtnNewButton.setBounds(56, 184, 296, 35);
		frmHearts.getContentPane().add(connectbtnNewButton);
	}
}
