import java.awt.EventQueue;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import java.awt.Color;

import javax.swing.JLabel;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;


public class Client {
	InThread inThread = new InThread();
	OutThread outThread =  new OutThread();
	BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
	DatagramSocket clientSocket = new DatagramSocket();
	InetAddress IPAddress;
	private JFrame frame;
	JButton btnReady;
	JLabel readylabel;
	String clientName;
	private messageOBJ outMessage = null;
	private JLabel lblRoomFullSorry;
	private JButton btnExit;
	private JPanel bottomHandpanel;
	private JPanel leftHandpanel;
	private JPanel rightHandPanel;
	private JPanel topHandpanel;
	private JPanel leftCardPanel;
	private JPanel rightCardPanel;
	private JPanel bottomCardPanel;
	private JPanel topCardPanel;
	private JLabel lblIn;
	private JLabel leftNameAndScore;
	private JLabel topNameAndScore;
	private JLabel rightNameAndScore;
	private JLabel bottomNameAndScore;

	public Client(String serverIP, String clientUsername) throws IOException {
		IPAddress  = InetAddress.getByName(serverIP);
		clientName = clientUsername;
		initialize();
		messageOBJ outPacket = new messageOBJ();
		outPacket.setTypeOBJMessage("LN");
		outPacket.setMessageOBJMessage("TEST");
		outPacket.setUsernameOBJMessage(clientUsername);
		outMessage = outPacket;
		outThread.start();
		inThread.start();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(0, 100, 0));
		frame.getContentPane().setForeground(Color.BLACK);
		frame.setTitle("Hearts @ " + IPAddress);
		frame.getContentPane().setLayout(null);
		
		lblRoomFullSorry = new JLabel("Room full. Sorry!");
		lblRoomFullSorry.setForeground(Color.WHITE);
		lblRoomFullSorry.setHorizontalAlignment(SwingConstants.CENTER);
		lblRoomFullSorry.setBounds(431, 189, 200, 50);
		frame.getContentPane().add(lblRoomFullSorry);
		lblRoomFullSorry.setLabelFor(btnExit);
		lblRoomFullSorry.setVisible(false);
		
		readylabel = new JLabel("");
		readylabel.setHorizontalAlignment(SwingConstants.CENTER);
		readylabel.setForeground(Color.WHITE);
		readylabel.setVerticalAlignment(SwingConstants.TOP);
		readylabel.setBounds(431, 200, 200, 39);
		frame.getContentPane().add(readylabel);
		readylabel.setLabelFor(btnReady);
		readylabel.setVisible(false);
		
		JLabel playingASlblNewLabel = new JLabel("Playing as " + clientName);
		playingASlblNewLabel.setFont(new Font("HelveticaNeueLT Pro 55 Roman", Font.PLAIN, 12));
		playingASlblNewLabel.setForeground(new Color(255, 255, 255));
		playingASlblNewLabel.setBounds(0, 0, 225, 23);
		frame.getContentPane().add(playingASlblNewLabel);
		
		btnReady = new JButton("Ready");
		btnReady.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				readylabel.setVisible(false);
        		btnReady.setVisible(false);
				messageOBJ outPacket = new messageOBJ();
				outPacket.setTypeOBJMessage("R");
				outPacket.setMessageOBJMessage(clientName + " is ready.");
				outPacket.setUsernameOBJMessage(clientName);
				outMessage = outPacket;
				outThread.run();
			}
		});
		btnReady.setBounds(431, 307, 200, 50);
		btnReady.setVisible(false);
		frame.getContentPane().add(btnReady);
		
		btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnExit.setVisible(false);
		btnExit.setBounds(431, 307, 200, 50);
		frame.getContentPane().add(btnExit);
		
		bottomHandpanel = new JPanel();
		bottomHandpanel.setBounds(235, 584, 600, 100);
		frame.getContentPane().add(bottomHandpanel);
		
		leftHandpanel = new JPanel();
		leftHandpanel.setBounds(10, 29, 100, 600);
		frame.getContentPane().add(leftHandpanel);
		
		rightHandPanel = new JPanel();
		rightHandPanel.setBounds(1010, 29, 100, 600);
		frame.getContentPane().add(rightHandPanel);
		
		topHandpanel = new JPanel();
		topHandpanel.setBounds(235, 11, 600, 100);
		frame.getContentPane().add(topHandpanel);
		
		leftCardPanel = new JPanel();
		leftCardPanel.setBounds(336, 246, 85, 135);
		frame.getContentPane().add(leftCardPanel);
		
		rightCardPanel = new JPanel();
		rightCardPanel.setBounds(641, 246, 85, 135);
		frame.getContentPane().add(rightCardPanel);
		
		bottomCardPanel = new JPanel();
		bottomCardPanel.setBounds(491, 368, 85, 135);
		frame.getContentPane().add(bottomCardPanel);
		
		topCardPanel = new JPanel();
		topCardPanel.setBounds(491, 161, 85, 135);
		frame.getContentPane().add(topCardPanel);
		
		lblIn = new JLabel("Instructions");
		lblIn.setForeground(Color.WHITE);
		lblIn.setBounds(138, 368, 76, 39);
		frame.getContentPane().add(lblIn);
		
		leftNameAndScore = new JLabel("");
		leftNameAndScore.setForeground(Color.WHITE);
		leftNameAndScore.setBounds(116, 277, 178, 50);
		frame.getContentPane().add(leftNameAndScore);
		
		topNameAndScore = new JLabel("");
		topNameAndScore.setForeground(Color.WHITE);
		topNameAndScore.setBounds(431, 118, 200, 39);
		frame.getContentPane().add(topNameAndScore);
		
		rightNameAndScore = new JLabel("");
		rightNameAndScore.setForeground(Color.WHITE);
		rightNameAndScore.setBounds(826, 277, 178, 50);
		frame.getContentPane().add(rightNameAndScore);
		
		bottomNameAndScore = new JLabel("");
		bottomNameAndScore.setForeground(Color.WHITE);
		bottomNameAndScore.setBounds(431, 534, 200, 39);
		frame.getContentPane().add(bottomNameAndScore);
		
		JTextArea textArea = new JTextArea();
		textArea.setForeground(Color.WHITE);
		textArea.setBackground(new Color(0, 100, 0));
		textArea.setBounds(138, 407, 275, 166);
		frame.getContentPane().add(textArea);
		frame.setBounds(100, 100, 1136, 734);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		
	}
	
	public class InThread extends Thread{
		public void run(){
			byte[] receiveData = new byte[1024];
			
			while(true){
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                try {
					clientSocket.receive(receivePacket);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
                receiveData = receivePacket.getData();
                ByteArrayInputStream in = new ByteArrayInputStream(receiveData);
                ObjectInputStream is = null;
				try {
					is = new ObjectInputStream(in);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
                
                try{
                	messageOBJ receiveMessage = (messageOBJ) is.readObject();
                	
                	if(receiveMessage.getTypeOBJMessage().equals("AR")){
                		readylabel.setText("Are you ready?");
                		readylabel.setVisible(true);
                		btnReady.setVisible(true);
                	}else if(receiveMessage.getTypeOBJMessage().equals("GF")){
                		lblRoomFullSorry.setVisible(true);
                		btnExit.setVisible(true);
                	}
                } catch (ClassNotFoundException e){
                	e.printStackTrace();
                } catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public class OutThread extends Thread{
		
		public void run(){
			byte[] sendData;
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ObjectOutputStream os = null;
			try {
				os = new ObjectOutputStream(outputStream);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				os.writeObject(outMessage);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			sendData = outputStream.toByteArray();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
			try {
				clientSocket.send(sendPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
			outMessage = null;
		}
	}
}
