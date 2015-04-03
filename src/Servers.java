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
import java.util.ArrayList;
import javax.swing.JScrollPane;


public class Servers {
	
	static ArrayList<clientInformation> clientInfoList = new ArrayList<clientInformation>();
	private JFrame frmHeartsServer;
	static JTextArea serverLogtextArea;
	static DatagramSocket serverSocket;
	static boolean gameRunning;
	static Integer playersReady = 0;
	private static messageOBJ outMessage = null;
	/**
	 * Launch the application.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		serverSocket = new DatagramSocket(9876);
		byte[] receiveData = new byte[1024]; 
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
			clientInformation inClient = new clientInformation();
			outMessage = null;
			
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
	        serverSocket.receive(receivePacket);
	        receiveData = receivePacket.getData();
	        ByteArrayInputStream in = new ByteArrayInputStream(receiveData);
	        ObjectInputStream is = new ObjectInputStream(in);
	        
	        InetAddress IPAddress = receivePacket.getAddress();
	        int port = receivePacket.getPort();
	        
	        try{
	        	receiveMessageOBJ = (messageOBJ) is.readObject();
	        	// Print raw input
	        	serverLogtextArea.append("-----DATA RECIEVED FROM" + IPAddress + "-----\n");
	        	serverLogtextArea.append("MESG: " + receiveMessageOBJ.getMessageOBJMessage() + "\n");
	        	serverLogtextArea.append("TYPE: " + receiveMessageOBJ.getTypeOBJMessage() + "\n");
	        	serverLogtextArea.append("USER:" + receiveMessageOBJ.getUsernameOBJMessage() + "\n");
	        	serverLogtextArea.append("--------------------------------------------------------\n");
	        	
	        	// Check number of players if LN
	        	serverLogtextArea.append("Number of players: " + clientInfoList.size() + " Number of players ready: " + playersReady + "\n");
	        	if(receiveMessageOBJ.getTypeOBJMessage().equals("LN")){
	        		inClient.setUsernameCI(receiveMessageOBJ.getUsernameOBJMessage());
        			inClient.setIPCI(IPAddress);
        			inClient.setPortCI(port);
	        		if(clientInfoList.size() < 4){
	        			serverLogtextArea.append(receiveMessageOBJ.getUsernameOBJMessage() + " @ " + IPAddress + " has just joined the game. \n");
	        			clientInfoList.add(inClient);
	        			askReady(inClient);
	        			inClient = null;
	        		}else{
	        			serverLogtextArea.append("No Room.\n");
	        			gameFull(inClient);
	        			continue;
	        		}
	        	}else if(receiveMessageOBJ.getTypeOBJMessage().equals("LO")){ // Logout
	        		for(int i = 0; i < clientInfoList.size(); i++){
						if(clientInfoList.get(i).getUsernameCI().equals(receiveMessageOBJ.getUsernameOBJMessage())){
							 clientInfoList.remove(i);
							 playersReady--;
						}
					}
	        	}else if(receiveMessageOBJ.getTypeOBJMessage().equals("R")){ // Player is ready
	        		serverLogtextArea.append(receiveMessageOBJ.getUsernameOBJMessage() + " is ready. \n");
	        		for(int i = 0; i < clientInfoList.size(); i++){
						if(clientInfoList.get(i).getUsernameCI().equals(receiveMessageOBJ.getUsernameOBJMessage())){
							clientInfoList.get(i).setReadyCI(true);
							playersReady++;
						}
					}
	        	}
	        	
	        	// Game start or continue
	        	if(clientInfoList.size() == 4){
	        		if(gameRunning == true){
	        			serverLogtextArea.append("Game in process.\n");
	        		}else if(gameRunning == false && playersReady == 4){
	        			gameRunning = true;
	        			serverLogtextArea.append("All players ready. Commence game! \n");
	        		}
	        	}
	        	
	        	
	        	
			} catch (ClassNotFoundException e){
				e.printStackTrace();
			}
	        inClient = null;
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
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 42, 809, 433);
		frmHeartsServer.getContentPane().add(scrollPane);
		
		serverLogtextArea = new JTextArea();
		scrollPane.setViewportView(serverLogtextArea);
		serverLogtextArea.setWrapStyleWord(true);
		
		JLabel lblServerLog = new JLabel("Server Log:");
		lblServerLog.setFont(new Font("HelveticaNeueLT Pro 55 Roman", Font.PLAIN, 13));
		lblServerLog.setForeground(new Color(255, 255, 255));
		lblServerLog.setBounds(10, 10, 200, 20);
		frmHeartsServer.getContentPane().add(lblServerLog);
		frmHeartsServer.setTitle("Hearts Server");
		frmHeartsServer.setBounds(100, 100, 845, 525);
		frmHeartsServer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		

	}
	
	public static void askReady( clientInformation client){
		messageOBJ readyMessage = new messageOBJ();
		readyMessage.setTypeOBJMessage("AR");
		readyMessage.setMessageOBJMessage("");
		readyMessage.setUsernameOBJMessage(client.getUsernameCI());
		outMessage = readyMessage;
		serverLogtextArea.append("Asking if " + client.getUsernameCI() + " is ready.\n");
		sendClientObject(client);
	}
	
	public static void gameFull( clientInformation client){
		messageOBJ gameFullMessage = new messageOBJ();
		gameFullMessage.setTypeOBJMessage("GF");
		gameFullMessage.setMessageOBJMessage("");
		gameFullMessage.setUsernameOBJMessage(client.getUsernameCI());
		outMessage = gameFullMessage;
		serverLogtextArea.append("Notifying " + client.getUsernameCI() + " game room is full.\n");
		sendClientObject(client);
	}
	
	public static void sendClientObject(clientInformation client){
		byte[] sendData = new byte[1024];
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
		
		if(outMessage.getUsernameOBJMessage().equals( client.getUsernameCI()) ){
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, client.getIPCI(), client.getPortCI());
			try {
				serverSocket.send(sendPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		outMessage = null;
	}
}
