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
import java.security.AllPermission;
import java.sql.ClientInfoStatus;
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
	static Integer donePass = 0;
	static Integer trick = 1;
	static Integer cardLaid = 0;
	static Integer nextTurn = 0;
	static Integer leader = 0;
	static Integer trickPoints = 0;
	static Integer playAgain = 0;
	static Boolean brokenHeart = false;
	static Boolean gameOver = false;
	// Cards
	static Card[] deck = new Card[52];
	static String[] suit = {"S", "H", "C", "D"};
	static String[] rank = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
	static String[] instructions = {"Choose three cards from your hand they will be passed to the player on your left. Wait for your cards to be passed.",
									"Player with the 2 of clubs will start the round."};
	
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
        	deck[i].setPower(i%13);
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
	        		playAgain= 0;
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
	        		boolean found = false;
	        		for(int i = 0; i < clientInfoList.size(); i++){
						if(clientInfoList.get(i).getUsernameCI().equals(receiveMessageOBJ.getUsernameOBJMessage())){
							clientInfoList.get(i).setReadyCI(true);
							clientInfoList.get(i).setSeatingCI(playersReady + 1);
							clientInfoList.get(i).setIPCI(IPAddress);
							clientInfoList.get(i).setPortCI(port);
							serverLogtextArea.append(receiveMessageOBJ.getUsernameOBJMessage() + " is sitting at seat: " + clientInfoList.get(i).getSeatingCI() + "\n");
							playersReady++;
						}
					}
	        		
	        		for(int i = 0; i < clientInfoList.size(); i++){
						if(clientInfoList.get(i).getUsernameCI().equals(receiveMessageOBJ.getUsernameOBJMessage())){
							found = true;
							break;
						}
					}
	        		
	        		if(found == false){
	        			if(clientInfoList.size() < 4){
	        				inClient.setUsernameCI(receiveMessageOBJ.getUsernameOBJMessage());
	            			inClient.setIPCI(IPAddress);
	            			inClient.setPortCI(port);
	            			serverLogtextArea.append(receiveMessageOBJ.getUsernameOBJMessage() + " @ " + IPAddress + " has just joined the game. \n");
		        			clientInfoList.add(inClient);
	            			for(int i = 0; i < clientInfoList.size(); i++){
	    						if(clientInfoList.get(i).getUsernameCI().equals(inClient.getUsernameCI())){
	    							clientInfoList.get(i).setReadyCI(true);
	    							clientInfoList.get(i).setSeatingCI(playersReady + 1);
	    							clientInfoList.get(i).setIPCI(IPAddress);
	    							clientInfoList.get(i).setPortCI(port);
	    							serverLogtextArea.append(inClient.getUsernameCI() + " is sitting at seat: " + clientInfoList.get(i).getSeatingCI() + "\n");
	    						}
	    					}
		        			
		        			
		        			playersReady++;
		        		}else{
		        			serverLogtextArea.append("No Room.\n");
		        			gameFull(inClient);
		        			continue;
		        		}
	        		}

	        	}else if(receiveMessageOBJ.getTypeOBJMessage().equals("PC")){ // Passing a card
	        		donePass++;
	        		for(int i = 0; i < clientInfoList.size(); i++){
						if(clientInfoList.get(i).getUsernameCI().equals(receiveMessageOBJ.getUsernameOBJMessage())){
							if(clientInfoList.get(i).getSeatingCI() == 1){
								serverLogtextArea.append(clientInfoList.get(i).getUsernameCI() + " passing " + receiveMessageOBJ.getCardOBJMessage().getSuit() +  receiveMessageOBJ.getCardOBJMessage().getRank() + " to " + clientInfoList.get(1).getUsernameCI());
								passCard(clientInfoList.get(1), receiveMessageOBJ.getCardOBJMessage());
								clientInfoList.get(1).dealToHandCI(receiveMessageOBJ.getCardOBJMessage());
								for(int k = 0; k < clientInfoList.get(i).getHand().size(); k++){
									if(clientInfoList.get(i).getHand().get(k).getSpriteURL().equals(receiveMessageOBJ.getCardOBJMessage().getSpriteURL())){
										clientInfoList.get(i).getHand().remove(k);
									}
								}
							}else if(clientInfoList.get(i).getSeatingCI() == 2){
								serverLogtextArea.append(clientInfoList.get(i).getUsernameCI() + " passing " + receiveMessageOBJ.getCardOBJMessage().getSuit() +  receiveMessageOBJ.getCardOBJMessage().getRank() + " to " + clientInfoList.get(2).getUsernameCI());
								passCard(clientInfoList.get(2), receiveMessageOBJ.getCardOBJMessage());
								clientInfoList.get(2).dealToHandCI(receiveMessageOBJ.getCardOBJMessage());
								for(int k = 0; k < clientInfoList.get(i).getHand().size(); k++){
									if(clientInfoList.get(i).getHand().get(k).getSpriteURL().equals(receiveMessageOBJ.getCardOBJMessage().getSpriteURL())){
										clientInfoList.get(i).getHand().remove(k);
									}
								}
							}else if(clientInfoList.get(i).getSeatingCI() == 3){
								serverLogtextArea.append(clientInfoList.get(i).getUsernameCI() + " passing " + receiveMessageOBJ.getCardOBJMessage().getSuit() +  receiveMessageOBJ.getCardOBJMessage().getRank() + " to " + clientInfoList.get(3).getUsernameCI());
								passCard(clientInfoList.get(3), receiveMessageOBJ.getCardOBJMessage());
								clientInfoList.get(3).dealToHandCI(receiveMessageOBJ.getCardOBJMessage());
								for(int k = 0; k < clientInfoList.get(i).getHand().size(); k++){
									if(clientInfoList.get(i).getHand().get(k).getSpriteURL().equals(receiveMessageOBJ.getCardOBJMessage().getSpriteURL())){
										clientInfoList.get(i).getHand().remove(k);
									}
								}
							}else if(clientInfoList.get(i).getSeatingCI() == 4){
								serverLogtextArea.append(clientInfoList.get(i).getUsernameCI() + " passing " + receiveMessageOBJ.getCardOBJMessage().getSuit() +  receiveMessageOBJ.getCardOBJMessage().getRank() + " to " + clientInfoList.get(0).getUsernameCI());
								passCard(clientInfoList.get(0), receiveMessageOBJ.getCardOBJMessage());
								clientInfoList.get(0).dealToHandCI(receiveMessageOBJ.getCardOBJMessage());
								for(int k = 0; k < clientInfoList.get(i).getHand().size(); k++){
									if(clientInfoList.get(i).getHand().get(k).getSpriteURL().equals(receiveMessageOBJ.getCardOBJMessage().getSpriteURL())){
										clientInfoList.get(i).getHand().remove(k);
									}
								}
							}
						}
					}
	        	}else if(receiveMessageOBJ.getTypeOBJMessage().equals("LC")){ // Passing a card
	        		Integer index = 0;
	        		serverLogtextArea.append(receiveMessageOBJ.getUsernameOBJMessage() + " PLAYED: " + receiveMessageOBJ.getCardOBJMessage().getSpriteURL());
	        		for(int i= 0; i < clientInfoList.size(); i++){
	        			if( clientInfoList.get(i).getUsernameCI().equals(receiveMessageOBJ.getUsernameOBJMessage())){
	        				index = i;
	        			}
	        		}
	        		serverLogtextArea.append("GOT " +  receiveMessageOBJ.getCardOBJMessage().getSpriteURL() +  " P: " + receiveMessageOBJ.getCardOBJMessage().getPower() +  "  C: "+receiveMessageOBJ.getCardOBJMessage().getSuit() + receiveMessageOBJ.getCardOBJMessage().getRank() + " FROM " + receiveMessageOBJ.getUsernameOBJMessage() + "\n");
	        		clientInfoList.get(index).setActiveCard(receiveMessageOBJ.getCardOBJMessage());
	        		serverLogtextArea.append("CHECK " +  clientInfoList.get(index).getActiveCard().getSpriteURL() +  " P: " +clientInfoList.get(index).getActiveCard().getPower() +  "  C: "+clientInfoList.get(index).getActiveCard().getSuit() + clientInfoList.get(index).getActiveCard().getRank() + " FROM " + receiveMessageOBJ.getUsernameOBJMessage() + "\n");
	        		for(int i= 0; i < clientInfoList.get(index).getHand().size(); i++){
	        			if(  clientInfoList.get(index).getHand().get(i).getSpriteURL().equals(receiveMessageOBJ.getCardOBJMessage().getSpriteURL())){
	        				 clientInfoList.get(index).getHand().remove(i);
	        			}
	        		}
	        		for(int i= 0; i < clientInfoList.size(); i++){
	        			if( clientInfoList.get(i).getUsernameCI() != receiveMessageOBJ.getUsernameOBJMessage()){
	        				showCard(receiveMessageOBJ, clientInfoList.get(i), receiveMessageOBJ.getCardOBJMessage());
	        			}
	        		}
	        		cardLaid++;
	        		// Next person
	        	}else if(receiveMessageOBJ.getTypeOBJMessage().equals("PN")){ // Passing a card
	        		playAgain++;
	        		if(playAgain == 4){
	        			newHand();
	        			playAgain = 0;
	        		}
	        		
	        	}else if(receiveMessageOBJ.getTypeOBJMessage().equals("PL")){ // Passing a card
	        		serverLogtextArea.append(receiveMessageOBJ.getTypeOBJMessage() + " quit.");
	        		playAgain = 0;
	        		gameOverSetUp();
	        	}
	        	
	        	// Print hands
    			for( int j =0; j <clientInfoList.size(); j++ ){
    				serverLogtextArea.append(clientInfoList.get(j).getUsernameCI() + " HAS: \n");
					for(int k=0; k<clientInfoList.get(j).getHand().size(); k++){
						serverLogtextArea.append(clientInfoList.get(j).getHand().get(k).getSuit() + clientInfoList.get(j).getHand().get(k).getRank() + " ");
					}
					serverLogtextArea.append("\n");
				}
    			
    			
	        	// Game start or continue
	        	if(clientInfoList.size() == 4){
	        		if(gameRunning == true){
	        			serverLogtextArea.append("Game in process. This persons turn: " + nextTurn + "\n");
	        			if(donePass == 12){
	        				serverLogtextArea.append("Trick: " + trick + " Cards Laid: " + cardLaid +  " LEAD:" + clientInfoList.get(leader).getSeatingCI() + " next " +  nextTurn +"\n");
	        				if(trick == 1){
	        					if(cardLaid == 0){
	        						serverLogtextArea.append("Done passing.\n");
			        				for(int i=0; i <clientInfoList.size(); i++){
				        				sendInstructions(clientInfoList.get(i), 1);
				        			}
			        				clientInformation temp = new clientInformation();
			        				temp = firstCard();
			        				serverLogtextArea.append("New LEAD:" + clientInfoList.get(leader).getSeatingCI());
			        				nextTurn = temp.getSeatingCI();
			        				nextTurn = nextTurn + 1;
			        				nextTurn = nextTurn%4;
			        				if(nextTurn == 0){
			        					nextTurn = 4;
			        				}
	        					}
	        				}
	        				if(cardLaid != 0 && cardLaid !=4){
		        				// Play 
	        					serverLogtextArea.append("WAITING ON: " + nextTurn + "\n");
	        				
	        						activeCard(nextTurn);
	        						nextTurn = nextTurn + 1;
			        				nextTurn = nextTurn%4;
			        				if(nextTurn == 0){
			        					nextTurn = 4;
			        				}
		        				
		        				serverLogtextArea.append("NEXT: " + nextTurn + "\n");
		        				
	        				}
	        				
	        				

	        				// PLAY ROUND
	        				
	        				if(cardLaid == 4){
	        					serverLogtextArea.append("All cards laid down for the trick. Now calculating trick winner \n");
	        					Card bestCard = new Card();

//	        					bestCard = clientInfoList.get(leader).getActiveCard();
	        					bestCard.setPower(clientInfoList.get(leader).getActiveCard().getPower());
								bestCard.setRank(clientInfoList.get(leader).getActiveCard().getRank());
								bestCard.setSpriteURL(clientInfoList.get(leader).getActiveCard().getSpriteURL());
								bestCard.setSuit(clientInfoList.get(leader).getActiveCard().getSuit());
	        					
	        					// CHECK WHO WON THE TRICK
	        					for (int i = 0; i < clientInfoList.size(); i++) {
	        						serverLogtextArea.append("Player: " + clientInfoList.get(i).getUsernameCI() + " played: " + clientInfoList.get(i).getActiveCard().getSpriteURL() + " \n");
	        						// Check for the same suit
	        						serverLogtextArea.append("VS: " + clientInfoList.get(leader).getUsernameCI() + " " + clientInfoList.get(i).getUsernameCI()+ " \n");
	        						if(!clientInfoList.get(leader).getUsernameCI().equals(clientInfoList.get(i).getUsernameCI())){
	        							if(bestCard.getSuit().equals(clientInfoList.get(i).getActiveCard().getSuit())){
		        							if(bestCard.getPower() > clientInfoList.get(i).getActiveCard().getPower()){
		        								serverLogtextArea.append(bestCard.getSpriteURL() + ": " + bestCard.getPower() + ": " + bestCard.getSuit() + bestCard.getRank() +" beat out " +  clientInfoList.get(i).getActiveCard().getSpriteURL() + ": " +  clientInfoList.get(i).getActiveCard().getPower() +  ": " + clientInfoList.get(i).getActiveCard().getSuit() + clientInfoList.get(i).getActiveCard().getRank() +" \n");
		        							}else if(bestCard.getPower() < clientInfoList.get(i).getActiveCard().getPower()){
		        								serverLogtextArea.append(clientInfoList.get(i).getActiveCard().getSpriteURL() + ": " +  clientInfoList.get(i).getActiveCard().getPower() + ": " + clientInfoList.get(i).getActiveCard().getSuit() + clientInfoList.get(i).getActiveCard().getRank() + " beat out " +  bestCard.getSpriteURL() + ": " + bestCard.getPower() + ": " + bestCard.getSuit() + bestCard.getRank() +" \n");
		        								bestCard.setPower(clientInfoList.get(i).getActiveCard().getPower());
		        								bestCard.setRank(clientInfoList.get(i).getActiveCard().getRank());
		        								bestCard.setSpriteURL(clientInfoList.get(i).getActiveCard().getSpriteURL());
		        								bestCard.setSuit(clientInfoList.get(i).getActiveCard().getSuit());
		        								leader = i;		
		        								serverLogtextArea.append("assigning new leader: " + clientInfoList.get(leader) + "\n");
		        							}
		        						}else{
		        							serverLogtextArea.append(bestCard.getSpriteURL() + " beat out " +  clientInfoList.get(i).getActiveCard().getSpriteURL());
		        						}
	        						}
	        						

								}
	        					
	        					for (int i = 0; i < clientInfoList.size(); i++) {
	        						// Add hearts or QS
	        						if(clientInfoList.get(i).getActiveCard().getSuit().equals("H")){
	        							trickPoints++;
	        							brokenHeart = true;
	        						}else if(clientInfoList.get(i).getActiveCard().getSuit().equals("S") && clientInfoList.get(i).getActiveCard().getRank().equals("Q")){
	        							trickPoints = trickPoints + 13;
	        						}		
	        					}

	        					
	        					// Assign points
	        					clientInfoList.get(leader).setCurrentPointsCI(clientInfoList.get(leader).getCurrentPointsCI() + trickPoints);
	        					trickPoints = 0;
	        					
	        					for (int i = 0; i < clientInfoList.size(); i++) {
	        						serverLogtextArea.append(clientInfoList.get(i).getUsernameCI() + " GOT THIS MANY POINTS: " + clientInfoList.get(i).getCurrentPointsCI() + "  TOTAL GAME POINTS: " + clientInfoList.get(i).getGamePointsCI()+ "\n");
	        						updateScores(clientInfoList.get(i));
	        					}
        						nextTurn = clientInfoList.get(leader).getSeatingCI();
	        					serverLogtextArea.append("NEW LEAD: " + nextTurn + "\n");
	        					cardLaid  = 0;
	        					
	        					if(trick == 13){
	        						for (int i = 0; i < clientInfoList.size(); i++) {
	        							if(clientInfoList.get(i).getCurrentPointsCI() == 26){
	        								serverLogtextArea.append(clientInfoList.get(i).getUsernameCI() + " shot the moon.\n");
	        								for (int k = 0; k < clientInfoList.size(); k++) {
	        									if(!clientInfoList.get(i).getUsernameCI().equals(clientInfoList.get(k).getUsernameCI())){
	        										clientInfoList.get(k).setCurrentPointsCI(26);
	        									}
	        								}
	        								clientInfoList.get(i).setCurrentPointsCI(0);
	        								break;
	        							}
	        							
	        						}
	        						
	        						for (int i = 0; i < clientInfoList.size(); i++) {
	        							clientInfoList.get(i).setGamePointsCI(clientInfoList.get(i).getGamePointsCI() + clientInfoList.get(i).getCurrentPointsCI());
	        							 clientInfoList.get(i).setCurrentPointsCI(0);
	        						}
	        						
	        						for (int i = 0; i < clientInfoList.size(); i++) {
		        						updateScores(clientInfoList.get(i));
		        					}
	        						// reset round
	        						trick = 0;
	        						try {
	        						    Thread.sleep(1000);                 //1000 milliseconds is one second.
	        						} catch(InterruptedException ex) {
	        						    Thread.currentThread().interrupt();
	        						}
	        						
	        						for (int i = 0; i < clientInfoList.size(); i++) {
	        							if(clientInfoList.get(i).getGamePointsCI() >= 100){
	        								serverLogtextArea.append("Game over");
	        								gameOver = true;
	        								// GAME OVER
	        							}
	        						}
	        						
	        						if(gameOver == true){
	        							gameOverSetUp();
	        							gameOver = false;
	        						}else{
	        							newHandAllowLeave();  							
	        						}
	        						
	        					}else{
	        						trick++;
	        						try {
	        						    Thread.sleep(1000);                 //1000 milliseconds is one second.
	        						} catch(InterruptedException ex) {
	        						    Thread.currentThread().interrupt();
	        						}
	        						newTrick();
	        						trickPoints = 0;
	        						serverLogtextArea.append("Asking new leader. : " + clientInfoList.get(leader).getSeatingCI() + "\n");
	        						leadCard();
	        						nextTurn = nextTurn + 1;
			        				nextTurn = nextTurn%4;
			        				if(nextTurn == 0){
			        					nextTurn = 4;
			        				}
		        					
	        					}
	        					
	        				}
	        			}
	        			    
	        		}else if(gameRunning == false && playersReady == 4){
	        			gameRunning = true;
	        			serverLogtextArea.append("All players ready and seated. Commence game! \n");
	        			shuffleDeck();
	        		        
	        			for(int i = 0; i < deck.length; i++){
	        				for( int j =0; j <clientInfoList.size(); j++ ){
	        					if(clientInfoList.get(j).getSeatingCI() == i%4 + 1){
	        						dealCard(clientInfoList.get(j), deck[i]);
	        						break;
	        					}
	        					clientInfoList.get(j).setCurrentPointsCI(0);
	        					clientInfoList.get(j).setGamePointsCI(0);
	        				}
	        	        }
	        			
	        			// First instruction
	        			for(int i=0; i <clientInfoList.size(); i++){
	        				sendInstructions(clientInfoList.get(i), 0);
	        			}	     
	        			sendSeating();
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
		frmHeartsServer.getContentPane().setBackground(new Color(245, 41, 41));
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
		lblServerLog.setForeground(new Color(255, 251, 0));
		frmHeartsServer.getContentPane().add(lblServerLog);
		frmHeartsServer.setTitle("Hearts Server");
		frmHeartsServer.setBounds(100, 100, 845, 525);
		frmHeartsServer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		

	}
	public static void gameOverSetUp(){
		playersReady = 0;
		trick = 1;
		gameRunning = false;
		cardLaid = 0;
		donePass = 0;
		nextTurn = 0;
		messageOBJ gameOverMessage = new messageOBJ();
		gameOverMessage.setTypeOBJMessage("GO");
		String results = "";
		clientInformation winner;
		System.out.println("TEST " + clientInfoList.get(0).getUsernameCI());
		winner = clientInfoList.get(0);
		boolean tie = false;
		String winnerString = clientInfoList.get(0).getUsernameCI();
		String winnerHeader = "Winner: ";
		for(int i = 0; i < clientInfoList.size(); i++){
			results = results + clientInfoList.get(i).getUsernameCI() + " has a a final score of " + clientInfoList.get(i).getGamePointsCI() + ".\n";
			
			if( winner.getUsernameCI() != clientInfoList.get(i).getUsernameCI()){
					if(winner.getGamePointsCI() > clientInfoList.get(i).getGamePointsCI()){
						winner = clientInfoList.get(i);
						winnerString = clientInfoList.get(i).getUsernameCI();
						tie = false;
					}else if(winner.getGamePointsCI() < clientInfoList.get(i).getGamePointsCI()){
						tie = false;
					}else{
						winnerString = winnerString + ", " + clientInfoList.get(i).getUsernameCI();
						tie = true;
					}		
			}
		}
		if(tie == true){
			winnerHeader = "Winners:  ";
		}
		
		winnerHeader = winnerHeader + winnerString;
		
		serverLogtextArea.append(results + winnerHeader);
		
		for(int i = 0; i < clientInfoList.size(); i++){
			gameOverMessage.setUsernameOBJMessage(clientInfoList.get(i).getUsernameCI());
			gameOverMessage.setMessageOBJMessage(results + winnerHeader);
			outMessage = gameOverMessage;
			sendClientObject(clientInfoList.get(i));
		}
		try {
		    Thread.sleep(1000);                 //1000 milliseconds is one second.
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
		for(int i = 0; i < clientInfoList.size(); i++){
			askReady(clientInfoList.get(i));
		}
		clientInfoList.clear();
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
		serverLogtextArea.append("Dealing "  + card.getSuit() + card.getRank() + " to " + client.getUsernameCI() + " SEAT: " + client.getSeatingCI() + "\n");
		client.dealToHandCI(card);
		messageOBJ dealCardMessage = new messageOBJ();
		dealCardMessage.setTypeOBJMessage("DC");
		dealCardMessage.setMessageOBJMessage("");
		dealCardMessage.setUsernameOBJMessage(client.getUsernameCI());
		dealCardMessage.setCardOBJMessage(card);
		outMessage = dealCardMessage;
		sendClientObject(client);
	}
	
	public static void sendInstructions( clientInformation client,  Integer id){
		serverLogtextArea.append("Sending instruction by id: "  + id + " to " + client.getUsernameCI() + "\n");
		messageOBJ instructionMessage = new messageOBJ();
		instructionMessage.setTypeOBJMessage("IN");
		instructionMessage.setMessageOBJMessage(instructions[id]);
		instructionMessage.setUsernameOBJMessage(client.getUsernameCI());
		instructionMessage.setCardOBJMessage(null);
		outMessage = instructionMessage;
		sendClientObject(client);
	}
	
	public static void passCard( clientInformation client, Card card){
		serverLogtextArea.append("Passing "  + card.getSuit() + card.getRank() + " to " + client.getUsernameCI() + " SEAT: " + client.getSeatingCI() + "\n");
		messageOBJ passCardMessage = new messageOBJ();
		passCardMessage.setTypeOBJMessage("PC");
		passCardMessage.setMessageOBJMessage("");
		passCardMessage.setUsernameOBJMessage(client.getUsernameCI());
		passCardMessage.setCardOBJMessage(card);
		outMessage = passCardMessage;
		sendClientObject(client);
	}
	
	public static clientInformation firstCard(){
		String twoOfClubs = "C2";
		clientInformation temp = new clientInformation();
		for(int i = 0; i < clientInfoList.size(); i++){
			for(int k = 0; k < clientInfoList.get(i).getHand().size(); k++){
				if( twoOfClubs.equals(clientInfoList.get(i).getHand().get(k).getSuit() + clientInfoList.get(i).getHand().get(k).getRank())){
					temp = clientInfoList.get(i);
					leader = i;
					break;
				}
			}
		}
		whoseTurn();
		messageOBJ passCardMessage = new messageOBJ();
		passCardMessage.setTypeOBJMessage("FC");
		passCardMessage.setUsernameOBJMessage(temp.getUsernameCI());
		outMessage = passCardMessage;
		sendClientObject(temp);
		return temp;
	}
	
	public static void leadCard(){
		whoseTurn();
		messageOBJ passCardMessage = new messageOBJ();
		passCardMessage.setTypeOBJMessage("LP");
		passCardMessage.setUsernameOBJMessage(clientInfoList.get(leader).getUsernameCI());
		outMessage = passCardMessage;
		sendClientObject(clientInfoList.get(leader));
	}
	
	public static void activeCard(Integer nextUp){
		clientInformation temp = new clientInformation();
		for(int i = 0; i < clientInfoList.size(); i++){
			if(clientInfoList.get(i).getSeatingCI() == nextUp){
				temp = clientInfoList.get(i);
			}
		}
		whoseTurn();
		messageOBJ passCardMessage = new messageOBJ();
		passCardMessage.setTypeOBJMessage("AC");
		passCardMessage.setMessageOBJMessage(clientInfoList.get(leader).getActiveCard().getSuit());
		passCardMessage.setUsernameOBJMessage(temp.getUsernameCI());
		outMessage = passCardMessage;
		sendClientObject(temp);
	}
	
	public static void newHand(){
		playAgain= 0;
		trick = 1;
		donePass = 0;
		nextTurn = 0;
		brokenHeart = false;
		for(int i=0; i <clientInfoList.size(); i++){
			messageOBJ newHandMessage = new messageOBJ();
			newHandMessage.setTypeOBJMessage("NH");
			newHandMessage.setMessageOBJMessage("");
			newHandMessage.setUsernameOBJMessage(clientInfoList.get(i).getUsernameCI());
			outMessage = newHandMessage;
			sendClientObject(clientInfoList.get(i));
		}
		
		shuffleDeck();
        
		for(int i = 0; i < deck.length; i++){
			for( int j =0; j <clientInfoList.size(); j++ ){
				if(clientInfoList.get(j).getSeatingCI() == i%4 + 1){
					dealCard(clientInfoList.get(j), deck[i]);
					break;
				}
				clientInfoList.get(j).setCurrentPointsCI(0);
			}
        }
		
		// First instruction
		for(int i=0; i <clientInfoList.size(); i++){
			sendInstructions(clientInfoList.get(i), 0);
		}	     
		sendSeating();
	}
	
	public static void newHandAllowLeave(){

		for(int i=0; i <clientInfoList.size(); i++){
			messageOBJ newHandMessage = new messageOBJ();
			newHandMessage.setTypeOBJMessage("AL");
			newHandMessage.setMessageOBJMessage("");
			newHandMessage.setUsernameOBJMessage(clientInfoList.get(i).getUsernameCI());
			outMessage = newHandMessage;
			sendClientObject(clientInfoList.get(i));
		}
	}
	
	public static void whoseTurn(){
		messageOBJ newHandMessage = new messageOBJ();
		newHandMessage.setTypeOBJMessage("WT");
		String activePlayer = "";
		if(nextTurn == 0){
			activePlayer = clientInfoList.get(leader).getUsernameCI();
		}else{
			activePlayer = clientInfoList.get(nextTurn - 1).getUsernameCI();
		}
				
		for(int i = 0; i < clientInfoList.size(); i++){
			newHandMessage.setMessageOBJMessage(activePlayer);
			newHandMessage.setUsernameOBJMessage(clientInfoList.get(i).getUsernameCI());
			outMessage = newHandMessage;
			sendClientObject(clientInfoList.get(i));
		}
	}
	
	
	public static void sendSeating(){
		serverLogtextArea.append("Sending seating plan.");
		clientInformation seat1 = new clientInformation();
		clientInformation seat2 = new clientInformation();
		clientInformation seat3 = new clientInformation();
		clientInformation seat4 = new clientInformation();
		messageOBJ sendSeatingMessage = new messageOBJ();
		sendSeatingMessage.setTypeOBJMessage("SP");
		for(int i = 0; i < clientInfoList.size(); i++){
			if(clientInfoList.get(i).getSeatingCI() == 1){
				seat1 = clientInfoList.get(i);
			}else if(clientInfoList.get(i).getSeatingCI() == 2){
				seat2 = clientInfoList.get(i);
			}else if(clientInfoList.get(i).getSeatingCI() == 3){
				seat3 = clientInfoList.get(i);
			}else if(clientInfoList.get(i).getSeatingCI() == 4){
				seat4 = clientInfoList.get(i);
			}
		}
		
		for(int i = 0; i < clientInfoList.size(); i++){
			if(clientInfoList.get(i).getSeatingCI() == 1){
				sendSeatingMessage.setUsernameOBJMessage(clientInfoList.get(i).getUsernameCI());
				sendSeatingMessage.setMessageOBJMessage(seat1.getUsernameCI());
				sendSeatingMessage.setDataOBJMessage(seat1.getSeatingCI());
				outMessage = sendSeatingMessage;
				sendClientObject(clientInfoList.get(i));
				sendSeatingMessage.setMessageOBJMessage(seat2.getUsernameCI());
				sendSeatingMessage.setDataOBJMessage(seat2.getSeatingCI());
				outMessage = sendSeatingMessage;
				sendClientObject(clientInfoList.get(i));
				sendSeatingMessage.setMessageOBJMessage(seat3.getUsernameCI());
				sendSeatingMessage.setDataOBJMessage(seat3.getSeatingCI());
				outMessage = sendSeatingMessage;
				sendClientObject(clientInfoList.get(i));
				sendSeatingMessage.setMessageOBJMessage(seat4.getUsernameCI());
				sendSeatingMessage.setDataOBJMessage(seat4.getSeatingCI());
				outMessage = sendSeatingMessage;
				sendClientObject(clientInfoList.get(i));
			}else if(clientInfoList.get(i).getSeatingCI() == 2){
				sendSeatingMessage.setUsernameOBJMessage(clientInfoList.get(i).getUsernameCI());
				sendSeatingMessage.setMessageOBJMessage(seat2.getUsernameCI());
				sendSeatingMessage.setDataOBJMessage(seat2.getSeatingCI());
				outMessage = sendSeatingMessage;
				sendClientObject(clientInfoList.get(i));
				sendSeatingMessage.setMessageOBJMessage(seat3.getUsernameCI());
				sendSeatingMessage.setDataOBJMessage(seat3.getSeatingCI());
				outMessage = sendSeatingMessage;
				sendClientObject(clientInfoList.get(i));
				sendSeatingMessage.setMessageOBJMessage(seat4.getUsernameCI());
				sendSeatingMessage.setDataOBJMessage(seat4.getSeatingCI());
				outMessage = sendSeatingMessage;
				sendClientObject(clientInfoList.get(i));
				sendSeatingMessage.setMessageOBJMessage(seat1.getUsernameCI());
				sendSeatingMessage.setDataOBJMessage(seat1.getSeatingCI());
				outMessage = sendSeatingMessage;
				sendClientObject(clientInfoList.get(i));
			}else if(clientInfoList.get(i).getSeatingCI() == 3){
				sendSeatingMessage.setUsernameOBJMessage(clientInfoList.get(i).getUsernameCI());
				sendSeatingMessage.setMessageOBJMessage(seat3.getUsernameCI());
				sendSeatingMessage.setDataOBJMessage(seat3.getSeatingCI());
				outMessage = sendSeatingMessage;
				sendClientObject(clientInfoList.get(i));
				sendSeatingMessage.setMessageOBJMessage(seat4.getUsernameCI());
				sendSeatingMessage.setDataOBJMessage(seat4.getSeatingCI());
				outMessage = sendSeatingMessage;
				sendClientObject(clientInfoList.get(i));
				sendSeatingMessage.setMessageOBJMessage(seat1.getUsernameCI());
				sendSeatingMessage.setDataOBJMessage(seat1.getSeatingCI());
				outMessage = sendSeatingMessage;
				sendClientObject(clientInfoList.get(i));
				sendSeatingMessage.setMessageOBJMessage(seat2.getUsernameCI());
				sendSeatingMessage.setDataOBJMessage(seat2.getSeatingCI());
				outMessage = sendSeatingMessage;
				sendClientObject(clientInfoList.get(i));
			}else if(clientInfoList.get(i).getSeatingCI() == 4){
				sendSeatingMessage.setUsernameOBJMessage(clientInfoList.get(i).getUsernameCI());
				sendSeatingMessage.setMessageOBJMessage(seat4.getUsernameCI());
				sendSeatingMessage.setDataOBJMessage(seat4.getSeatingCI());
				outMessage = sendSeatingMessage;
				sendClientObject(clientInfoList.get(i));
				sendSeatingMessage.setMessageOBJMessage(seat1.getUsernameCI());
				sendSeatingMessage.setDataOBJMessage(seat1.getSeatingCI());
				outMessage = sendSeatingMessage;
				sendClientObject(clientInfoList.get(i));
				sendSeatingMessage.setMessageOBJMessage(seat2.getUsernameCI());
				sendSeatingMessage.setDataOBJMessage(seat2.getSeatingCI());
				outMessage = sendSeatingMessage;
				sendClientObject(clientInfoList.get(i));
				sendSeatingMessage.setMessageOBJMessage(seat3.getUsernameCI());
				sendSeatingMessage.setDataOBJMessage(seat3.getSeatingCI());
				outMessage = sendSeatingMessage;
				sendClientObject(clientInfoList.get(i));
			}
			outMessage = sendSeatingMessage;
			sendClientObject(clientInfoList.get(i));
		}
	}
	
	private static void updateScores(clientInformation clientInformation) {
		messageOBJ updateScoresMessage = new messageOBJ();
		updateScoresMessage.setTypeOBJMessage("US");
		updateScoresMessage.setUsernameOBJMessage(clientInformation.getUsernameCI());
		for(int i = 0; i < clientInfoList.size(); i++){
			updateScoresMessage.setMessageOBJMessage(clientInfoList.get(i).getUsernameCI());
			updateScoresMessage.setCurrentPointsOBJMessage(clientInfoList.get(i).getCurrentPointsCI());
			updateScoresMessage.setGamePointsOBJMessage(clientInfoList.get(i).getGamePointsCI());
			updateScoresMessage.setTrickOBJMessage(trick);
			outMessage = updateScoresMessage;
			sendClientObject(clientInformation);
		}
	}
	
	private static void newTrick() {
		messageOBJ newTrickMessage = new messageOBJ();
		newTrickMessage.setTypeOBJMessage("NT");
		Integer broken = 0;
		if(brokenHeart){
			broken = 1;
		}
		for(int i = 0; i < clientInfoList.size(); i++){
			newTrickMessage.setUsernameOBJMessage(clientInfoList.get(i).getUsernameCI());
			newTrickMessage.setTrickOBJMessage(trick);
			newTrickMessage.setDataOBJMessage(broken);
			outMessage = newTrickMessage;
			sendClientObject(clientInfoList.get(i));
		}
	}

	
	public static void showCard(messageOBJ activeUserInfo, clientInformation reciepent, Card card){
		// Set who is sending it
		messageOBJ passCardMessage = new messageOBJ();
		passCardMessage.setTypeOBJMessage("SC");
		passCardMessage.setMessageOBJMessage(activeUserInfo.getUsernameOBJMessage());
		passCardMessage.setDataOBJMessage(activeUserInfo.getDataOBJMessage());
		passCardMessage.setUsernameOBJMessage(reciepent.getUsernameCI());
		passCardMessage.setCardOBJMessage(card);
		passCardMessage.setTrickOBJMessage(trick);
		outMessage = passCardMessage;
		sendClientObject(reciepent);
	}
	
	public static void shuffleDeck(){
		for (int i = 0; i < deck.length; i++) {
			int index = (int)(Math.random()*deck.length);
			String tempSuit = deck[i].getSuit();
			String tempRank = deck[i].getRank();
			String tempSpriteURL = deck[i].getSpriteURL();
			int tempPower= deck[i].getPower();
			
			deck[i].setSuit(deck[index].getSuit());
			deck[i].setRank(deck[index].getRank());
			deck[i].setSpriteURL(deck[index].getSpriteURL());
			deck[i].setPower(deck[index].getPower());
			
			deck[index].setSuit(tempSuit);
			deck[index].setRank(tempRank);
			deck[index].setSpriteURL(tempSpriteURL);
			deck[index].setPower(tempPower);
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
