import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.Color;

import javax.swing.JTextArea;
import javax.swing.JLabel;

import java.awt.Font;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;


public class Servers {
	
	private JFrame frmHeartsServer;
	static JTextArea serverLogtextArea;
	static DatagramSocket serverSocket;
	/**
	 * Launch the application.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		serverSocket = new DatagramSocket(9876);
		byte[] receiveData = new byte[1024];
        byte[] sendData = new byte[1024];
        messageOBJ receiveMessageOBJ = new messageOBJ();
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Servers window = new Servers();
					window.frmHeartsServer.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		while(true){
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
	        serverSocket.receive(receivePacket);
	        receiveData = receivePacket.getData();
	        ByteArrayInputStream in = new ByteArrayInputStream(receiveData);
	        ObjectInputStream is = new ObjectInputStream(in);
	        
	        InetAddress IPAddress = receivePacket.getAddress();
	        int port = receivePacket.getPort();
	        
	        try{
	        	receiveMessageOBJ = (messageOBJ) is.readObject();
	        	serverLogtextArea.append("-----DATA RECIEVED FROM" + IPAddress + "-----\n");
	        	serverLogtextArea.append("MESG: " + receiveMessageOBJ.getMessageOBJMessage() + "\n");
	        	serverLogtextArea.append("TYPE: " + receiveMessageOBJ.getTypeOBJMessage() + "\n");
	        	serverLogtextArea.append("USER:" + receiveMessageOBJ.getUsernameOBJMessage() + "\n");
	        	serverLogtextArea.append("--------------------------------------------------------\n");
	        	if(receiveMessageOBJ.getTypeOBJMessage().equals("LN")){
	        		serverLogtextArea.append(receiveMessageOBJ.getUsernameOBJMessage() + " @ " + IPAddress + " has just joined the game. \n");
	        	}
	        	
			} catch (ClassNotFoundException e){
				e.printStackTrace();
			}
		}
	}

	public Servers() throws IOException {
		initialize();
		
        
		
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws IOException 
	 */
	private void initialize() throws IOException {
		frmHeartsServer = new JFrame();
		frmHeartsServer.getContentPane().setBackground(new Color(0, 100, 0));
		frmHeartsServer.getContentPane().setLayout(null);
		
		serverLogtextArea = new JTextArea();
		serverLogtextArea.setWrapStyleWord(true);
		serverLogtextArea.setBounds(10, 42, 809, 433);
		frmHeartsServer.getContentPane().add(serverLogtextArea);
		
		JLabel lblServerLog = new JLabel("Server Log:");
		lblServerLog.setFont(new Font("HelveticaNeueLT Pro 55 Roman", Font.PLAIN, 13));
		lblServerLog.setForeground(new Color(255, 255, 255));
		lblServerLog.setBounds(10, 10, 200, 20);
		frmHeartsServer.getContentPane().add(lblServerLog);
		frmHeartsServer.setTitle("Hearts Server");
		frmHeartsServer.setBounds(100, 100, 845, 525);
		frmHeartsServer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		

	}
}
