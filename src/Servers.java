import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JLabel;

import java.awt.Font;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
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
	
	// Cards
	static Card[] deck = new Card[52];
	static String[] suit = {"S", "H", "C", "D"};
	static String[] rank = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
	
	/**
	 * Launch the application.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		serverSocket = new DatagramSocket(9876);
		byte[] receiveData = new byte[1024]; 
        messageOBJ receiveMessageOBJ = new messageOBJ();
        
        // Construct Deck
        for(int i = 0; i < deck.length; i++){
        	deck[i] = new Card();
        	deck[i].setSuit(suit[i/13]);
        	deck[i].setRank(rank[i%13]);
        	deck[i].setSpriteURL("sprites\\" + deck[i].getSuit() + deck[i].getRank() + ".png");
        }
        
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
							clientInfoList.get(i).setSeatingCI(playersReady + 1);
							serverLogtextArea.append(receiveMessageOBJ.getUsernameOBJMessage() + " is sitting at seat: " + clientInfoList.get(i).getSeatingCI() + "\n");
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
	        			serverLogtextArea.append("All players ready and seated. Commence game! \n");
	        			shuffleDeck();
	        			for(int i = 0; i < deck.length; i++){
	        				for( int j =0; j <clientInfoList.size(); j++ ){
	        					System.out.println(clientInfoList.get(j).getSeatingCI() + " " + j + " " + i%4 +1);
	        					if(clientInfoList.get(j).getSeatingCI() == i%4 + 1){
	        						System.out.println(deck[i].getSuit() + deck[i].getRank() + " " +deck[i].getSpriteURL() + " DEAL TO: " + j +" "+ clientInfoList.get(j).getUsernameCI() +"\n");
	        						dealCard(clientInfoList.get(j), deck[i]);
	        						break;
	        					}
	        				}
	        				
	        	        }
	        			
	        			// Print hands
	        			for( int j =0; j <clientInfoList.size(); j++ ){
	        				
	        				serverLogtextArea.append(j + " HAS: \n");
        					for(int k=0; k<clientInfoList.get(j).getHand().size(); k++){
        						serverLogtextArea.append(clientInfoList.get(j).getHand().get(k).getSuit() + clientInfoList.get(j).getHand().get(k).getRank() + " ");
        					}
        					serverLogtextArea.append("\n");
        				}
	        			
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
		lblServerLog.setBounds(10, 10, 200, 20);
		lblServerLog.setFont(new Font("HelveticaNeueLT Pro 55 Roman", Font.PLAIN, 13));
		lblServerLog.setForeground(new Color(255, 255, 255));
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
	
	public static void dealCard( clientInformation client, Card card){
		System.out.println("Dealing" + card.getSuit() + card.getRank() + " to " + client.getUsernameCI());
		client.dealToHandCI(card);
//		messageOBJ gameFullMessage = new messageOBJ();
//		gameFullMessage.setTypeOBJMessage("DC");
//		gameFullMessage.setMessageOBJMessage("");
//		gameFullMessage.setUsernameOBJMessage(client.getUsernameCI());
//		outMessage = gameFullMessage;
//		serverLogtextArea.append("Notifying " + client.getUsernameCI() + " game room is full.\n");
//		sendClientObject(client);
	}
	
	public static void shuffleDeck(){
		for (int i = 0; i < deck.length; i++) {
			int index = (int)(Math.random()*deck.length);
			String tempSuit = deck[i].getSuit();
			String tempRank = deck[i].getRank();
			String tempSpriteURL = deck[i].getSpriteURL();
			
			deck[i].setSuit(deck[index].getSuit());
			deck[i].setRank(deck[index].getRank());
			deck[i].setSpriteURL(deck[index].getSpriteURL());
			
			deck[index].setSuit(tempSuit);
			deck[index].setRank(tempRank);
			deck[index].setSpriteURL(tempSpriteURL);
		}
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
