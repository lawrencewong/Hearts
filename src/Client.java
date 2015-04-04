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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class Client {
	private static ArrayList<Card> hand = new ArrayList<Card>();
	private static ArrayList<Card> passedToMe = new ArrayList<Card>();
	private static ArrayList<Integer> passedCards = new ArrayList<Integer>();
	InThread inThread = new InThread();
	private static OutThread outThread;
	BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
	DatagramSocket clientSocket = new DatagramSocket();
	InetAddress IPAddress;
	private JFrame frame;
	JButton btnReady;
	JLabel readylabel;
	String twoOfClubs = "C2";
	static String clientName;
	private static messageOBJ outMessage = null;
	private JLabel lblRoomFullSorry;
	private JButton btnExit;
	private static JPanel bottomHandpanel;
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
	private JLabel lhc0;
	private JLabel lhc1;
	private JLabel lhc2;
	private JLabel lhc3;
	private JLabel lhc4;
	private JLabel lhc5;
	private JLabel lhc6;
	private JLabel lhc7;
	private JLabel lhc8;
	private JLabel lhc9;
	private JLabel lhc10;
	private JLabel lhc11;
	private JLabel lhc12;
	private JLabel rhc12;
	private JLabel rhc11;
	private JLabel rhc10;
	private JLabel rhc9;
	private JLabel rhc8;
	private JLabel rhc7;
	private JLabel rhc6;
	private JLabel rhc5;
	private JLabel rhc4;
	private JLabel rhc3;
	private JLabel rhc2;
	private JLabel rhc1;
	private JLabel rhc0;
	private JLabel thc12;
	private JLabel thc11;
	private JLabel thc10;
	private JLabel thc9;
	private JLabel thc8;
	private JLabel thc7;
	private JLabel thc6;
	private JLabel thc5;
	private JLabel thc4;
	private JLabel thc3;
	private JLabel thc2;
	private JLabel thc1;
	private JLabel thc0;
	private JLabel lc;
	private JLabel rc;
	private JLabel bc;
	private JLabel tc;
	private JTextArea instructionArea;
	private static HashMap<String, JLabel> cardLabels = new HashMap<String, JLabel>();
	private static HashMap<String, JLabel> cardLeftLabels = new HashMap<String, JLabel>();
	private static HashMap<String, JLabel> cardRightLabels = new HashMap<String, JLabel>();
	private static HashMap<String, JLabel> cardTopLabels = new HashMap<String, JLabel>();
	private static Boolean myTurn = false;
	private static Boolean firstTurn = false;
	
	private int toPass = 3;



	public Client(String serverIP, String clientUsername) throws IOException {
		IPAddress  = InetAddress.getByName(serverIP);
		clientName = clientUsername;
		initialize();
		outThread =  new OutThread();
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
		bhc0.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(toPass > 0){
					bhc0.setIcon(null);
					bhc0.setVisible(false);
					passCard(hand.get(0));
					passedCards.add(0);
					toPass--;
					checkUpdate();
				}else if(myTurn){
					if(firstTurn){
						if(twoOfClubs.equals(hand.get(0).getSuit() + hand.get(0).getRank())){
							bhc0.setIcon(null);
							bhc0.setVisible(false);
							playCard(hand.get(0));
							ImageIcon imageTopIcon = new ImageIcon(hand.get(0).getSpriteURL());
							bc.setIcon(imageTopIcon);
							bottomCardPanel.add( bc, BorderLayout.CENTER );
							bottomCardPanel.setVisible(true);
						}
					}else{
						bhc0.setIcon(null);
						bhc0.setVisible(false);
						playCard(hand.get(0));
						ImageIcon imageTopIcon = new ImageIcon(hand.get(0).getSpriteURL());
						bc.setIcon(imageTopIcon);
						bottomCardPanel.add( bc, BorderLayout.CENTER );
						bottomCardPanel.setVisible(true);
					}
				}
			}
		});
		bottomHandpanel.add(bhc0);
		
		bhc1 = new JLabel("");
		bhc1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(toPass > 0){
					bhc1.setIcon(null);
					bhc1.setVisible(false);
					passCard(hand.get(1));
					passedCards.add(1);
					toPass--;
					checkUpdate();
				}else if(myTurn){
					if(firstTurn){
						if(twoOfClubs.equals(hand.get(1).getSuit() + hand.get(1).getRank())){
							bhc1.setIcon(null);
							bhc1.setVisible(false);
							playCard(hand.get(1));
							ImageIcon imageTopIcon = new ImageIcon(hand.get(1).getSpriteURL());
							bc.setIcon(imageTopIcon);
							bottomCardPanel.add( bc, BorderLayout.CENTER );
							bottomCardPanel.setVisible(true);
						}
					}else{
						bhc1.setIcon(null);
						bhc1.setVisible(false);
						playCard(hand.get(1));
						ImageIcon imageTopIcon = new ImageIcon(hand.get(1).getSpriteURL());
						bc.setIcon(imageTopIcon);
						bottomCardPanel.add( bc, BorderLayout.CENTER );
						bottomCardPanel.setVisible(true);
					}
				}
			}
		});
		bottomHandpanel.add(bhc1);
		
		bhc2 = new JLabel("");
		bhc2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(toPass > 0){
					bhc2.setIcon(null);
					bhc2.setVisible(false);
					System.out.println(hand.get(2).getSuit() + hand.get(2).getRank());
					passCard(hand.get(2));
					passedCards.add(2);
					toPass--;
					checkUpdate();
				}else if(myTurn){
					if(firstTurn){
						if(twoOfClubs.equals(hand.get(2).getSuit() + hand.get(2).getRank())){
							bhc2.setIcon(null);
							bhc2.setVisible(false);
							playCard(hand.get(2));
							ImageIcon imageTopIcon = new ImageIcon(hand.get(2).getSpriteURL());
							bc.setIcon(imageTopIcon);
							bottomCardPanel.add( bc, BorderLayout.CENTER );
							bottomCardPanel.setVisible(true);
						}
					}else{
						bhc2.setIcon(null);
						bhc2.setVisible(false);
						playCard(hand.get(2));
						ImageIcon imageTopIcon = new ImageIcon(hand.get(2).getSpriteURL());
						bc.setIcon(imageTopIcon);
						bottomCardPanel.add( bc, BorderLayout.CENTER );
						bottomCardPanel.setVisible(true);
					}
				}
			}
		});
		bottomHandpanel.add(bhc2);
		
		bhc3 = new JLabel("");
		bhc3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(toPass > 0){
					bhc3.setIcon(null);
					bhc3.setVisible(false);
					System.out.println(hand.get(3).getSuit() + hand.get(3).getRank());
					passCard(hand.get(3));
					passedCards.add(3);
					toPass--;
					checkUpdate();
				}else if(myTurn){
					if(firstTurn){
						if(twoOfClubs.equals(hand.get(3).getSuit() + hand.get(3).getRank())){
							bhc3.setIcon(null);
							bhc3.setVisible(false);
							playCard(hand.get(3));
							ImageIcon imageTopIcon = new ImageIcon(hand.get(3).getSpriteURL());
							bc.setIcon(imageTopIcon);
							bottomCardPanel.add( bc, BorderLayout.CENTER );
							bottomCardPanel.setVisible(true);
						}
					}else{
						bhc3.setIcon(null);
						bhc3.setVisible(false);
						playCard(hand.get(3));
						ImageIcon imageTopIcon = new ImageIcon(hand.get(3).getSpriteURL());
						bc.setIcon(imageTopIcon);
						bottomCardPanel.add( bc, BorderLayout.CENTER );
						bottomCardPanel.setVisible(true);
					}
				}
			}
		});
		bottomHandpanel.add(bhc3);
		
		bhc4 = new JLabel("");
		bhc4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(toPass > 0){
					bhc4.setIcon(null);
					bhc4.setVisible(false);
					System.out.println(hand.get(4).getSuit() + hand.get(4).getRank());
					passCard(hand.get(4));
					passedCards.add(4);
					toPass--;
					checkUpdate();
				}else if(myTurn){
					if(firstTurn){
						if(twoOfClubs.equals(hand.get(4).getSuit() + hand.get(4).getRank())){
							bhc4.setIcon(null);
							bhc4.setVisible(false);
							playCard(hand.get(4));
							ImageIcon imageTopIcon = new ImageIcon(hand.get(4).getSpriteURL());
							bc.setIcon(imageTopIcon);
							bottomCardPanel.add( bc, BorderLayout.CENTER );
							bottomCardPanel.setVisible(true);
						}
					}else{
						bhc4.setIcon(null);
						bhc4.setVisible(false);
						playCard(hand.get(4));
						ImageIcon imageTopIcon = new ImageIcon(hand.get(4).getSpriteURL());
						bc.setIcon(imageTopIcon);
						bottomCardPanel.add( bc, BorderLayout.CENTER );
						bottomCardPanel.setVisible(true);
					}
				}
			}
		});
		bottomHandpanel.add(bhc4);
		
		bhc5 = new JLabel("");
		bhc5.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(toPass > 0){
					bhc5.setIcon(null);
					bhc5.setVisible(false);
					System.out.println(hand.get(5).getSuit() + hand.get(5).getRank());
					passCard(hand.get(5));
					passedCards.add(5);
					toPass--;
					checkUpdate();
				}else if(myTurn){
					if(firstTurn){
						if(twoOfClubs.equals(hand.get(5).getSuit() + hand.get(5).getRank())){
							bhc5.setIcon(null);
							bhc5.setVisible(false);
							playCard(hand.get(5));
							ImageIcon imageTopIcon = new ImageIcon(hand.get(5).getSpriteURL());
							bc.setIcon(imageTopIcon);
							bottomCardPanel.add( bc, BorderLayout.CENTER );
							bottomCardPanel.setVisible(true);
						}
					}else{
						bhc5.setIcon(null);
						bhc5.setVisible(false);
						playCard(hand.get(5));
						ImageIcon imageTopIcon = new ImageIcon(hand.get(5).getSpriteURL());
						bc.setIcon(imageTopIcon);
						bottomCardPanel.add( bc, BorderLayout.CENTER );
						bottomCardPanel.setVisible(true);
					}
				}
			}
		});
		bottomHandpanel.add(bhc5);
		
		bhc6 = new JLabel("");
		bhc6.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(toPass > 0){
					bhc6.setIcon(null);
					bhc6.setVisible(false);
					System.out.println(hand.get(6).getSuit() + hand.get(6).getRank());
					passCard(hand.get(6));
					passedCards.add(6);
					toPass--;
					checkUpdate();
				}else if(myTurn){
					if(firstTurn){
						if(twoOfClubs.equals(hand.get(6).getSuit() + hand.get(6).getRank())){
							bhc6.setIcon(null);
							bhc6.setVisible(false);
							playCard(hand.get(6));
							ImageIcon imageTopIcon = new ImageIcon(hand.get(6).getSpriteURL());
							bc.setIcon(imageTopIcon);
							bottomCardPanel.add( bc, BorderLayout.CENTER );
							bottomCardPanel.setVisible(true);
						}
					}else{
						bhc6.setIcon(null);
						bhc6.setVisible(false);
						playCard(hand.get(6));
						ImageIcon imageTopIcon = new ImageIcon(hand.get(6).getSpriteURL());
						bc.setIcon(imageTopIcon);
						bottomCardPanel.add( bc, BorderLayout.CENTER );
						bottomCardPanel.setVisible(true);
					}
				}
			}
		});
		bottomHandpanel.add(bhc6);
		
		bhc7 = new JLabel("");
		bhc7.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(toPass > 0){
					bhc7.setIcon(null);
					bhc7.setVisible(false);
					System.out.println(hand.get(7).getSuit() + hand.get(7).getRank());
					passCard(hand.get(7));
					passedCards.add(7);
					toPass--;
					checkUpdate();
				}else if(myTurn){
					if(firstTurn){
						if(twoOfClubs.equals(hand.get(7).getSuit() + hand.get(7).getRank())){
							bhc7.setIcon(null);
							bhc7.setVisible(false);
							playCard(hand.get(7));
							ImageIcon imageTopIcon = new ImageIcon(hand.get(7).getSpriteURL());
							bc.setIcon(imageTopIcon);
							bottomCardPanel.add( bc, BorderLayout.CENTER );
							bottomCardPanel.setVisible(true);
						}
					}else{
						bhc7.setIcon(null);
						bhc7.setVisible(false);
						playCard(hand.get(7));
						ImageIcon imageTopIcon = new ImageIcon(hand.get(7).getSpriteURL());
						bc.setIcon(imageTopIcon);
						bottomCardPanel.add( bc, BorderLayout.CENTER );
						bottomCardPanel.setVisible(true);
					}
				}
			}
		});
		bottomHandpanel.add(bhc7);
		
		bhc8 = new JLabel("");
		bhc8.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(toPass > 0){
					bhc8.setIcon(null);
					bhc8.setVisible(false);
					System.out.println(hand.get(8).getSuit() + hand.get(8).getRank());
					passCard(hand.get(8));
					passedCards.add(8);
					toPass--;
					checkUpdate();
				}else if(myTurn){
					if(firstTurn){
						if(twoOfClubs.equals(hand.get(8).getSuit() + hand.get(8).getRank())){
							bhc8.setIcon(null);
							bhc8.setVisible(false);
							playCard(hand.get(8));
							ImageIcon imageTopIcon = new ImageIcon(hand.get(8).getSpriteURL());
							bc.setIcon(imageTopIcon);
							bottomCardPanel.add( bc, BorderLayout.CENTER );
							bottomCardPanel.setVisible(true);
						}
					}else{
						bhc8.setIcon(null);
						bhc8.setVisible(false);
						playCard(hand.get(8));
						ImageIcon imageTopIcon = new ImageIcon(hand.get(8).getSpriteURL());
						bc.setIcon(imageTopIcon);
						bottomCardPanel.add( bc, BorderLayout.CENTER );
						bottomCardPanel.setVisible(true);
					}
				}
			}
		});
		bottomHandpanel.add(bhc8);
		
		bhc9 = new JLabel("");
		bhc9.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(toPass > 0){
					bhc9.setIcon(null);
					bhc9.setVisible(false);
					System.out.println(hand.get(9).getSuit() + hand.get(9).getRank());
					passCard(hand.get(9));
					passedCards.add(9);
					toPass--;
					checkUpdate();
				}else if(myTurn){
					if(firstTurn){
						if(twoOfClubs.equals(hand.get(9).getSuit() + hand.get(9).getRank())){
							bhc9.setIcon(null);
							bhc9.setVisible(false);
							playCard(hand.get(9));
							ImageIcon imageTopIcon = new ImageIcon(hand.get(9).getSpriteURL());
							bc.setIcon(imageTopIcon);
							bottomCardPanel.add( bc, BorderLayout.CENTER );
							bottomCardPanel.setVisible(true);
						}
					}else{
						bhc9.setIcon(null);
						bhc9.setVisible(false);
						playCard(hand.get(9));
						ImageIcon imageTopIcon = new ImageIcon(hand.get(9).getSpriteURL());
						bc.setIcon(imageTopIcon);
						bottomCardPanel.add( bc, BorderLayout.CENTER );
						bottomCardPanel.setVisible(true);
					}
				}
			}
		});
		bottomHandpanel.add(bhc9);
		
		bhc10 = new JLabel("");
		bhc10.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(toPass > 0){
					bhc10.setIcon(null);
					bhc10.setVisible(false);
					System.out.println(hand.get(10).getSuit() + hand.get(10).getRank());
					passCard(hand.get(10));
					passedCards.add(10);
					toPass--;
					checkUpdate();
				}else if(myTurn){
					if(firstTurn){
						if(twoOfClubs.equals(hand.get(10).getSuit() + hand.get(10).getRank())){
							bhc10.setIcon(null);
							bhc10.setVisible(false);
							playCard(hand.get(10));
							ImageIcon imageTopIcon = new ImageIcon(hand.get(10).getSpriteURL());
							bc.setIcon(imageTopIcon);
							bottomCardPanel.add( bc, BorderLayout.CENTER );
							bottomCardPanel.setVisible(true);
						}
					}else{
						bhc10.setIcon(null);
						bhc10.setVisible(false);
						playCard(hand.get(10));
						ImageIcon imageTopIcon = new ImageIcon(hand.get(10).getSpriteURL());
						bc.setIcon(imageTopIcon);
						bottomCardPanel.add( bc, BorderLayout.CENTER );
						bottomCardPanel.setVisible(true);
					}
				}
			}
		});
		bottomHandpanel.add(bhc10);
		
		bhc11 = new JLabel("");
		bhc11.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(toPass > 0){
					bhc11.setIcon(null);
					bhc11.setVisible(false);
					System.out.println(hand.get(11).getSuit() + hand.get(11).getRank());
					passCard(hand.get(11));
					passedCards.add(11);	
					toPass--;
					checkUpdate();
				}else if(myTurn){
					if(firstTurn){
						if(twoOfClubs.equals(hand.get(11).getSuit() + hand.get(11).getRank())){
							bhc11.setIcon(null);
							bhc11.setVisible(false);
							playCard(hand.get(11));
							ImageIcon imageTopIcon = new ImageIcon(hand.get(11).getSpriteURL());
							bc.setIcon(imageTopIcon);
							bottomCardPanel.add( bc, BorderLayout.CENTER );
							bottomCardPanel.setVisible(true);
						}
					}else{
						bhc11.setIcon(null);
						bhc11.setVisible(false);
						playCard(hand.get(11));
						ImageIcon imageTopIcon = new ImageIcon(hand.get(11).getSpriteURL());
						bc.setIcon(imageTopIcon);
						bottomCardPanel.add( bc, BorderLayout.CENTER );
						bottomCardPanel.setVisible(true);
					}
				}
			}
		});
		bottomHandpanel.add(bhc11);
		
		bhc12 = new JLabel("");
		bhc12.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(toPass > 0){
					bhc12.setIcon(null);
					bhc12.setVisible(false);
					System.out.println(hand.get(12).getSuit() + hand.get(12).getRank());
					passCard(hand.get(12));
					passedCards.add(12);
					toPass--;
					checkUpdate();
				}else if(myTurn){
					if(firstTurn){
						if(twoOfClubs.equals(hand.get(12).getSuit() + hand.get(12).getRank())){
							bhc12.setIcon(null);
							bhc12.setVisible(false);
							playCard(hand.get(12));
							ImageIcon imageTopIcon = new ImageIcon(hand.get(12).getSpriteURL());
							bc.setIcon(imageTopIcon);
							bottomCardPanel.add( bc, BorderLayout.CENTER );
							bottomCardPanel.setVisible(true);
						}
					}else{
						bhc12.setIcon(null);
						bhc12.setVisible(false);
						playCard(hand.get(12));
						ImageIcon imageTopIcon = new ImageIcon(hand.get(12).getSpriteURL());
						bc.setIcon(imageTopIcon);
						bottomCardPanel.add( bc, BorderLayout.CENTER );
						bottomCardPanel.setVisible(true);
					}
				}
			}
		});
		bottomHandpanel.add(bhc12);
		
		leftHandpanel = new JPanel();
		leftHandpanel.setBackground(new Color(0, 100, 0));
		leftHandpanel.setBounds(10, 29, 100, 600);
		frame.getContentPane().add(leftHandpanel);
		leftHandpanel.setLayout(new GridLayout(13, 1, 0, 0));
		
		lhc0 = new JLabel("");
		leftHandpanel.add(lhc0);
		
		lhc1 = new JLabel("");
		leftHandpanel.add(lhc1);
		
		lhc2 = new JLabel("");
		leftHandpanel.add(lhc2);
		
		lhc3 = new JLabel("");
		leftHandpanel.add(lhc3);
		
		lhc4 = new JLabel("");
		leftHandpanel.add(lhc4);
		
		lhc5 = new JLabel("");
		leftHandpanel.add(lhc5);
		
		lhc6 = new JLabel("");
		leftHandpanel.add(lhc6);
		
		lhc7 = new JLabel("");
		leftHandpanel.add(lhc7);
		
		lhc8 = new JLabel("");
		leftHandpanel.add(lhc8);
		
		lhc9 = new JLabel("");
		leftHandpanel.add(lhc9);
		
		lhc10 = new JLabel("");
		leftHandpanel.add(lhc10);
		
		lhc11 = new JLabel("");
		leftHandpanel.add(lhc11);
		
		lhc12 = new JLabel("");
		leftHandpanel.add(lhc12);
		
		rightHandPanel = new JPanel();
		rightHandPanel.setBackground(new Color(0, 100, 0));
		rightHandPanel.setBounds(1010, 29, 100, 600);
		frame.getContentPane().add(rightHandPanel);
		rightHandPanel.setLayout(new GridLayout(13, 1, 0, 0));
		
		rhc12 = new JLabel("");
		rightHandPanel.add(rhc12);
		
		rhc11 = new JLabel("");
		rightHandPanel.add(rhc11);
		
		rhc10 = new JLabel("");
		rightHandPanel.add(rhc10);
		
		rhc9 = new JLabel("");
		rightHandPanel.add(rhc9);
		
		rhc8 = new JLabel("");
		rightHandPanel.add(rhc8);
		
		rhc7 = new JLabel("");
		rightHandPanel.add(rhc7);
		
		rhc6 = new JLabel("");
		rightHandPanel.add(rhc6);
		
		rhc5 = new JLabel("");
		rightHandPanel.add(rhc5);
		
		rhc4 = new JLabel("");
		rightHandPanel.add(rhc4);
		
		rhc3 = new JLabel("");
		rightHandPanel.add(rhc3);
		
		rhc2 = new JLabel("");
		rightHandPanel.add(rhc2);
		
		rhc1 = new JLabel("");
		rightHandPanel.add(rhc1);
		
		rhc0 = new JLabel("");
		rightHandPanel.add(rhc0);
		
		topHandpanel = new JPanel();
		topHandpanel.setBackground(new Color(0, 100, 0));
		topHandpanel.setBounds(235, 11, 600, 100);
		frame.getContentPane().add(topHandpanel);
		topHandpanel.setLayout(new GridLayout(0, 13, 0, 0));
		
		thc12 = new JLabel("");
		topHandpanel.add(thc12);
		
		thc11 = new JLabel("");
		topHandpanel.add(thc11);
		
		thc10 = new JLabel("");
		topHandpanel.add(thc10);
		
		thc9 = new JLabel("");
		topHandpanel.add(thc9);
		
		thc8 = new JLabel("");
		topHandpanel.add(thc8);
		
		thc7 = new JLabel("");
		topHandpanel.add(thc7);
		
		thc6 = new JLabel("");
		topHandpanel.add(thc6);
		
		thc5 = new JLabel("");
		topHandpanel.add(thc5);
		
		thc4 = new JLabel("");
		topHandpanel.add(thc4);
		
		thc3 = new JLabel("");
		topHandpanel.add(thc3);
		
		thc2 = new JLabel("");
		topHandpanel.add(thc2);
		
		thc1 = new JLabel("");
		topHandpanel.add(thc1);
		
		thc0 = new JLabel("");
		topHandpanel.add(thc0);
		
		leftCardPanel = new JPanel();
		leftCardPanel.setBackground(new Color(0, 100, 0));
		leftCardPanel.setBounds(336, 246, 85, 135);
		frame.getContentPane().add(leftCardPanel);
		leftCardPanel.setLayout(new GridLayout(1, 0, 0, 0));
		
		lc = new JLabel("");
		leftCardPanel.add(lc);
		
		rightCardPanel = new JPanel();
		rightCardPanel.setBackground(new Color(0, 100, 0));
		rightCardPanel.setBounds(641, 246, 85, 135);
		frame.getContentPane().add(rightCardPanel);
		rightCardPanel.setLayout(new GridLayout(1, 0, 0, 0));
		
		rc = new JLabel("");
		rc.setForeground(new Color(0, 100, 0));
		rc.setBackground(new Color(0, 100, 0));
		rightCardPanel.add(rc);
		
		bottomCardPanel = new JPanel();
		bottomCardPanel.setBackground(new Color(0, 100, 0));
		bottomCardPanel.setBounds(491, 368, 85, 135);
		frame.getContentPane().add(bottomCardPanel);
		bottomCardPanel.setLayout(new GridLayout(1, 0, 0, 0));
		
		bc = new JLabel("");
		bottomCardPanel.add(bc);
		bottomHandpanel.setVisible(false);
		
		topCardPanel = new JPanel();
		topCardPanel.setBackground(new Color(0, 100, 0));
		topCardPanel.setBounds(491, 161, 85, 135);
		frame.getContentPane().add(topCardPanel);
		topCardPanel.setLayout(new GridLayout(1, 0, 0, 0));
		
		tc = new JLabel("");
		tc.setForeground(new Color(0, 100, 0));
		tc.setBackground(new Color(0, 100, 0));
		topCardPanel.add(tc);
		
		lblIn = new JLabel("Instructions");
		lblIn.setForeground(Color.WHITE);
		lblIn.setBounds(138, 368, 76, 39);
		frame.getContentPane().add(lblIn);
		lblIn.setVisible(false);
		
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
		
		instructionArea = new JTextArea();
		instructionArea.setWrapStyleWord(true);
		instructionArea.setLineWrap(true);
		instructionArea.setForeground(Color.WHITE);
		instructionArea.setBackground(new Color(0, 100, 0));
		instructionArea.setBounds(138, 407, 275, 166);
		instructionArea.setVisible(false);
		frame.getContentPane().add(instructionArea);
		frame.setBounds(100, 100, 1136, 734);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
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
		
		cardTopLabels.put("1", thc0);
		cardTopLabels.put("2", thc1);
		cardTopLabels.put("3", thc2);
		cardTopLabels.put("4", thc3);
		cardTopLabels.put("5", thc4);
		cardTopLabels.put("6", thc5);
		cardTopLabels.put("7", thc6);
		cardTopLabels.put("8", thc7);
		cardTopLabels.put("9", thc8);
		cardTopLabels.put("10", thc9);
		cardTopLabels.put("11", thc10);
		cardTopLabels.put("12", thc11);
		cardTopLabels.put("13", thc12);
		
		cardRightLabels.put("1", rhc0);
		cardRightLabels.put("2", rhc1);
		cardRightLabels.put("3", rhc2);
		cardRightLabels.put("4", rhc3);
		cardRightLabels.put("5", rhc4);
		cardRightLabels.put("6", rhc5);
		cardRightLabels.put("7", rhc6);
		cardRightLabels.put("8", rhc7);
		cardRightLabels.put("9", rhc8);
		cardRightLabels.put("10", rhc9);
		cardRightLabels.put("11", rhc10);
		cardRightLabels.put("12", rhc11);
		cardRightLabels.put("13", rhc12);
		
		cardLeftLabels.put("1", lhc0);
		cardLeftLabels.put("2", lhc1);
		cardLeftLabels.put("3", lhc2);
		cardLeftLabels.put("4", lhc3);
		cardLeftLabels.put("5", lhc4);
		cardLeftLabels.put("6", lhc5);
		cardLeftLabels.put("7", lhc6);
		cardLeftLabels.put("8", lhc7);
		cardLeftLabels.put("9", lhc8);
		cardLeftLabels.put("10", lhc9);
		cardLeftLabels.put("11", lhc10);
		cardLeftLabels.put("12", lhc11);
		cardLeftLabels.put("13", lhc12);

		
	}
	
	public class InThread extends Thread{
		public void run(){
			byte[] receiveData = new byte[1024];
			
			while(true){
				checkUpdate();
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
                		bottomHandpanel.setVisible(true);
                		Card tempCard = new Card();
                		tempCard = receiveMessage.getCardOBJMessage();
                		hand.add(tempCard);
                		Integer index = hand.size();
                		ImageIcon imageIcon = new ImageIcon(receiveMessage.getCardOBJMessage().getSpriteURL());
                		cardLabels.get(index.toString()).setIcon(imageIcon);
        		        bottomHandpanel.add( cardLabels.get(index.toString()), BorderLayout.CENTER );
        		        
        		        ImageIcon imageRightIcon = new ImageIcon("sprites\\bh.png");
                		cardRightLabels.get(index.toString()).setIcon(imageRightIcon);
        		        rightHandPanel.add( cardRightLabels.get(index.toString()), BorderLayout.CENTER );
        		        
        		        ImageIcon imageLeftIcon = new ImageIcon("sprites\\bh.png");
                		cardLeftLabels.get(index.toString()).setIcon(imageLeftIcon);
        		        leftHandpanel.add( cardLeftLabels.get(index.toString()), BorderLayout.CENTER );
        		        
        		        ImageIcon imageTopIcon = new ImageIcon("sprites\\bv.png");
                		cardTopLabels.get(index.toString()).setIcon(imageTopIcon);
        		        topHandpanel.add( cardTopLabels.get(index.toString()), BorderLayout.CENTER );
        		        
                	}else if(receiveMessage.getTypeOBJMessage().equals("IN")){
                		instructionArea.setText(receiveMessage.getMessageOBJMessage());
                		lblIn.setVisible(true);
                		instructionArea.setVisible(true);
                	}else if(receiveMessage.getTypeOBJMessage().equals("PC")){
                		passedToMe.add(receiveMessage.getCardOBJMessage());
                		checkUpdate();
                	}else if(receiveMessage.getTypeOBJMessage().equals("FC")){
                		firstTurn = true;
                		myTurn = true;
                	}
                } catch (ClassNotFoundException e){
                	e.printStackTrace();
                } catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void playCard(Card card){
		messageOBJ passCardMessage = new messageOBJ();
		passCardMessage.setTypeOBJMessage("LC");
		passCardMessage.setMessageOBJMessage("");
		passCardMessage.setUsernameOBJMessage(clientName);
		passCardMessage.setCardOBJMessage(card);
		outMessage = passCardMessage;
		outThread.run();
	}
	
	public static void passCard(Card card){
		messageOBJ passCardMessage = new messageOBJ();
		passCardMessage.setTypeOBJMessage("PC");
		passCardMessage.setMessageOBJMessage("");
		passCardMessage.setUsernameOBJMessage(clientName);
		passCardMessage.setCardOBJMessage(card);
		outMessage = passCardMessage;
		outThread.run();
	}
	public static void checkUpdate(){
		if(passedToMe.size() == 3 && passedCards.size() == 3){
			for( int i = 0; i < hand.size(); i++){
				for( int k = 0; k < passedCards.size(); k++){
					if(passedCards.get(k) == i){
						hand.get(passedCards.get(k)).setRank(passedToMe.get(k).getRank());
						hand.get(passedCards.get(k)).setSuit(passedToMe.get(k).getSuit());
						hand.get(passedCards.get(k)).setRank(passedToMe.get(k).getSpriteURL());
						ImageIcon imageIcon = new ImageIcon(passedToMe.get(k).getSpriteURL());
                		cardLabels.get(Integer.toString(passedCards.get(k)+1)).setIcon(imageIcon);
        		        bottomHandpanel.add( cardLabels.get(Integer.toString(passedCards.get(k)+1)), BorderLayout.CENTER );
        		        cardLabels.get(Integer.toString(passedCards.get(k)+1)).setVisible(true);
        		        passedToMe.remove(k);
        		        passedCards.remove(k);
						break;
					}
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
			checkUpdate();
		}
	}
}
