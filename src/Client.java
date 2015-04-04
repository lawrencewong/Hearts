import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import java.awt.Color;

import javax.swing.JLabel;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.SwingConstants;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JPanel;

import java.awt.GridLayout;


public class Client {
	private ArrayList<Card> hand = new ArrayList<Card>();
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
	private JLabel bhc0;
	private JLabel bhc1;
	private JLabel bhc2;
	private JLabel bhc4;
	private JLabel bhc5;
	private JLabel bhc6;
	private JLabel bhc7;
	private JLabel bhc8;
	private JLabel bhc9;
	private JLabel bhc10;
	private JLabel bhc11;
	private JLabel bhc12;
	private JLabel bhc3;
	private HashMap<String, JLabel> cardLabels = new HashMap<String, JLabel>();
	
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
		bottomHandpanel.setBackground(new Color(0, 100, 0));
		bottomHandpanel.setBounds(120, 584, 880, 100);
		frame.getContentPane().add(bottomHandpanel);
		bottomHandpanel.setLayout(new GridLayout(1, 13, 0, 0));
		
		bhc0 = new JLabel("");
		bhc0.setBackground(Color.GRAY);
		bottomHandpanel.add(bhc0);
		
		bhc1 = new JLabel("");
		bhc1.setBackground(Color.RED);
		bottomHandpanel.add(bhc1);
		
		bhc2 = new JLabel("");
		bottomHandpanel.add(bhc2);
		
		bhc3 = new JLabel("");
		bottomHandpanel.add(bhc3);
		
		bhc4 = new JLabel("");
		bottomHandpanel.add(bhc4);
		
		bhc5 = new JLabel("");
		bottomHandpanel.add(bhc5);
		
		bhc6 = new JLabel("");
		bottomHandpanel.add(bhc6);
		
		bhc7 = new JLabel("");
		bottomHandpanel.add(bhc7);
		
		bhc8 = new JLabel("");
		bottomHandpanel.add(bhc8);
		
		bhc9 = new JLabel("");
		bottomHandpanel.add(bhc9);
		
		bhc10 = new JLabel("");
		bottomHandpanel.add(bhc10);
		
		bhc11 = new JLabel("");
		bottomHandpanel.add(bhc11);
		
		bhc12 = new JLabel("");
		bottomHandpanel.add(bhc12);
		
		cardLabels.put("1", bhc0);
		cardLabels.put("2", bhc1);
		cardLabels.put("3", bhc2);
		cardLabels.put("4", bhc3);
		cardLabels.put("5", bhc4);
		cardLabels.put("6", bhc5);
		cardLabels.put("7", bhc6);
		cardLabels.put("8", bhc7);
		cardLabels.put("9", bhc8);
		cardLabels.put("10", bhc9);
		cardLabels.put("11", bhc10);
		cardLabels.put("12", bhc11);
		cardLabels.put("13", bhc12);
		
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
                	}else if(receiveMessage.getTypeOBJMessage().equals("DC")){
                		System.out.println("GOT: " + receiveMessage.getCardOBJMessage().getSpriteURL());
                		Card tempCard = new Card();
                		tempCard = receiveMessage.getCardOBJMessage();
                		hand.add(tempCard);
                		System.out.println(Integer.toString(hand.size()));
                		ImageIcon imageIcon = new ImageIcon(receiveMessage.getCardOBJMessage().getSpriteURL());
                		//JLabel tempLabel = new JLabel("", imageIcon, JLabel.CENTER);
                		cardLabels.get(Integer.toString(hand.size())).setIcon(imageIcon);
                		
//                		bhc0.setIcon(imageIcon);
        		        bottomHandpanel.add( cardLabels.get(Integer.toString(hand.size())), BorderLayout.CENTER );
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
	
	public void dealToHandCI(Card card){
		hand.add(card);
	}
	
	public ArrayList<Card> getHand(){
		return hand;
	}
	
	public void playCardCI(Card card){
		String toPlay = card.getSuit()+card.getRank();
		for(int i = 0; i < hand.size(); i++){
			String currentCard = hand.get(i).getSuit()+hand.get(i).getRank();
			if(currentCard.equals(toPlay)){
				 hand.remove(i);
			}
		}
	}
}
