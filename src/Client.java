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
		
		JLabel playingASlblNewLabel = new JLabel("Playing as " + clientName);
		playingASlblNewLabel.setFont(new Font("HelveticaNeueLT Pro 55 Roman", Font.PLAIN, 12));
		playingASlblNewLabel.setForeground(new Color(255, 255, 255));
		playingASlblNewLabel.setBounds(10, 11, 292, 23);
		frame.getContentPane().add(playingASlblNewLabel);
		
		readylabel = new JLabel("");
		readylabel.setHorizontalAlignment(SwingConstants.CENTER);
		readylabel.setForeground(Color.WHITE);
		readylabel.setVerticalAlignment(SwingConstants.TOP);
		readylabel.setBounds(337, 252, 200, 39);
		frame.getContentPane().add(readylabel);
		
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
		readylabel.setLabelFor(btnReady);
		btnReady.setBounds(337, 307, 200, 50);
		btnReady.setVisible(false);
		readylabel.setVisible(false);
		frame.getContentPane().add(btnReady);
		
		lblRoomFullSorry = new JLabel("Room full. Sorry!");
		lblRoomFullSorry.setForeground(Color.WHITE);
		lblRoomFullSorry.setHorizontalAlignment(SwingConstants.CENTER);
		lblRoomFullSorry.setBounds(337, 191, 200, 50);
		frame.getContentPane().add(lblRoomFullSorry);
		
		btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		lblRoomFullSorry.setLabelFor(btnExit);
		lblRoomFullSorry.setVisible(false);
		btnExit.setVisible(false);
		btnExit.setBounds(337, 307, 200, 50);
		frame.getContentPane().add(btnExit);
		frame.setBounds(100, 100, 949, 618);
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
			System.out.print("OUT\n");
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
