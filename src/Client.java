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

import org.omg.PortableInterceptor.ClientRequestInterceptor;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


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
	static Integer mySeat = 0;
	static Integer trickNumber = 0;
	Integer seated = 0;
	Boolean brokenHearts = false;
	Boolean myLead = false;
	static Boolean allHearts = false;
	static String clientName;
	private static JTextArea notices;
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
	private JLabel leftName;
	private JLabel topName;
	private JLabel rightName;
	private JLabel bottomName;
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
	boolean allowLeave = false;
	private int toPass = 3;
	private JLabel bottomScore;
	private JLabel leftScore;
	private JLabel topScore;
	private JLabel rightScore;
	private static JButton playAgain;
	static String leadSuit = ""; 


	public Client(String serverIP, String clientUsername) throws IOException {
		IPAddress  = InetAddress.getByName(serverIP);
		clientName = clientUsername;
		initialize();
		outThread =  new OutThread();
		messageOBJ outPacket = new messageOBJ();
		outPacket.setTypeOBJMessage("LN");
		outPacket.setMessageOBJMessage("");
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
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				if(allowLeave){
					playerLeft();
					System.exit(0);
				}
			}
		});
		frame.getContentPane().setForeground(Color.BLACK);
		frame.setTitle("Hearts @ " + IPAddress);
		frame.getContentPane().setLayout(null);
		
		playAgain = new JButton("Play Next Hand");
		playAgain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				playAgain.setVisible(false);
				playNextHand();
			}
		});
		playAgain.setBounds(431, 307, 200, 50);
		playAgain.setVisible(false);
		frame.getContentPane().add(playAgain);
		
		notices = new JTextArea();
		notices.setOpaque(false);
		notices.setForeground(new Color(255, 251, 0));
		notices.setEditable(false);
		notices.setLineWrap(true);
		notices.setBounds(586, 407, 394, 96);
		frame.getContentPane().add(notices);
		
		lblRoomFullSorry = new JLabel("Room full. Sorry!");
		lblRoomFullSorry.setForeground(new Color(255, 251, 0));
		lblRoomFullSorry.setHorizontalAlignment(SwingConstants.CENTER);
		lblRoomFullSorry.setBounds(431, 189, 200, 50);
		frame.getContentPane().add(lblRoomFullSorry);
		lblRoomFullSorry.setLabelFor(btnExit);
		lblRoomFullSorry.setVisible(false);
		
		readylabel = new JLabel("");
		readylabel.setHorizontalAlignment(SwingConstants.CENTER);
		readylabel.setForeground(new Color(255, 251, 0));
		readylabel.setVerticalAlignment(SwingConstants.TOP);
		readylabel.setBounds(431, 200, 200, 39);
		frame.getContentPane().add(readylabel);
		readylabel.setLabelFor(btnReady);
		readylabel.setVisible(false);
		
		JLabel playingASlblNewLabel = new JLabel("Playing as " + clientName);
		playingASlblNewLabel.setFont(new Font("HelveticaNeueLT Pro 55 Roman", Font.PLAIN, 12));
		playingASlblNewLabel.setForeground(new Color(255, 251, 0));
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
		//bottomHandpanel.setBackground(new Color(0, 100, 0));
		bottomHandpanel.setOpaque(false);
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
					}else if(myLead){
						if(hand.get(0).getSuit().equals("H")){
							if(brokenHearts){
								bhc0.setIcon(null);
								bhc0.setVisible(false);
								playCard(hand.get(0));
								ImageIcon imageTopIcon = new ImageIcon(hand.get(0).getSpriteURL());
								bc.setIcon(imageTopIcon);
								bottomCardPanel.add( bc, BorderLayout.CENTER );
								bottomCardPanel.setVisible(true);
							}else{
								checkAllHearts();
								if(allHearts){
									bhc0.setIcon(null);
									bhc0.setVisible(false);
									playCard(hand.get(0));
									ImageIcon imageTopIcon = new ImageIcon(hand.get(0).getSpriteURL());
									bc.setIcon(imageTopIcon);
									bottomCardPanel.add( bc, BorderLayout.CENTER );
									bottomCardPanel.setVisible(true);
								}
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
					}else{
						if(doIHaveLeadSuit() && hand.get(0).getSuit().equals(leadSuit)){
							bhc0.setIcon(null);
							bhc0.setVisible(false);
							playCard(hand.get(0));
							ImageIcon imageTopIcon = new ImageIcon(hand.get(0).getSpriteURL());
							bc.setIcon(imageTopIcon);
							bottomCardPanel.add( bc, BorderLayout.CENTER );
							bottomCardPanel.setVisible(true);
						}else if(doIHaveLeadSuit() == false){
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
					}else if(myLead){
						if(hand.get(1).getSuit().equals("H")){
							if(brokenHearts){
								bhc1.setIcon(null);
								bhc1.setVisible(false);
								playCard(hand.get(1));
								ImageIcon imageTopIcon = new ImageIcon(hand.get(1).getSpriteURL());
								bc.setIcon(imageTopIcon);
								bottomCardPanel.add( bc, BorderLayout.CENTER );
								bottomCardPanel.setVisible(true);
							}else{
								checkAllHearts();
								if(allHearts){
									bhc1.setIcon(null);
									bhc1.setVisible(false);
									playCard(hand.get(1));
									ImageIcon imageTopIcon = new ImageIcon(hand.get(1).getSpriteURL());
									bc.setIcon(imageTopIcon);
									bottomCardPanel.add( bc, BorderLayout.CENTER );
									bottomCardPanel.setVisible(true);
								}
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
					}else{
						if(doIHaveLeadSuit() && hand.get(1).getSuit().equals(leadSuit)){
							bhc1.setIcon(null);
							bhc1.setVisible(false);
							playCard(hand.get(1));
							ImageIcon imageTopIcon = new ImageIcon(hand.get(1).getSpriteURL());
							bc.setIcon(imageTopIcon);
							bottomCardPanel.add( bc, BorderLayout.CENTER );
							bottomCardPanel.setVisible(true);
						}else if(doIHaveLeadSuit() == false){
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
					}else if(myLead){
						if(hand.get(2).getSuit().equals("H")){
							if(brokenHearts){
								bhc2.setIcon(null);
								bhc2.setVisible(false);
								playCard(hand.get(2));
								ImageIcon imageTopIcon = new ImageIcon(hand.get(2).getSpriteURL());
								bc.setIcon(imageTopIcon);
								bottomCardPanel.add( bc, BorderLayout.CENTER );
								bottomCardPanel.setVisible(true);
							}else{
								checkAllHearts();
								if(allHearts){
									bhc2.setIcon(null);
									bhc2.setVisible(false);
									playCard(hand.get(2));
									ImageIcon imageTopIcon = new ImageIcon(hand.get(2).getSpriteURL());
									bc.setIcon(imageTopIcon);
									bottomCardPanel.add( bc, BorderLayout.CENTER );
									bottomCardPanel.setVisible(true);
								}
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
					}else{
						if(doIHaveLeadSuit() && hand.get(2).getSuit().equals(leadSuit)){
							bhc2.setIcon(null);
							bhc2.setVisible(false);
							playCard(hand.get(2));
							ImageIcon imageTopIcon = new ImageIcon(hand.get(2).getSpriteURL());
							bc.setIcon(imageTopIcon);
							bottomCardPanel.add( bc, BorderLayout.CENTER );
							bottomCardPanel.setVisible(true);
						}else if(doIHaveLeadSuit() == false){
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
					}else if(myLead){
						if(hand.get(3).getSuit().equals("H")){
							if(brokenHearts){
								bhc3.setIcon(null);
								bhc3.setVisible(false);
								playCard(hand.get(3));
								ImageIcon imageTopIcon = new ImageIcon(hand.get(3).getSpriteURL());
								bc.setIcon(imageTopIcon);
								bottomCardPanel.add( bc, BorderLayout.CENTER );
								bottomCardPanel.setVisible(true);
							}else{
								checkAllHearts();
								if(allHearts){
									bhc3.setIcon(null);
									bhc3.setVisible(false);
									playCard(hand.get(3));
									ImageIcon imageTopIcon = new ImageIcon(hand.get(3).getSpriteURL());
									bc.setIcon(imageTopIcon);
									bottomCardPanel.add( bc, BorderLayout.CENTER );
									bottomCardPanel.setVisible(true);
								}
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
					}else{
						if(doIHaveLeadSuit() && hand.get(3).getSuit().equals(leadSuit)){
							bhc3.setIcon(null);
							bhc3.setVisible(false);
							playCard(hand.get(3));
							ImageIcon imageTopIcon = new ImageIcon(hand.get(3).getSpriteURL());
							bc.setIcon(imageTopIcon);
							bottomCardPanel.add( bc, BorderLayout.CENTER );
							bottomCardPanel.setVisible(true);
						}else if(doIHaveLeadSuit() == false){
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
					}else if(myLead){
						if(hand.get(4).getSuit().equals("H")){
							if(brokenHearts){
								bhc4.setIcon(null);
								bhc4.setVisible(false);
								playCard(hand.get(4));
								ImageIcon imageTopIcon = new ImageIcon(hand.get(4).getSpriteURL());
								bc.setIcon(imageTopIcon);
								bottomCardPanel.add( bc, BorderLayout.CENTER );
								bottomCardPanel.setVisible(true);
							}else{
								checkAllHearts();
								if(allHearts){
									bhc4.setIcon(null);
									bhc4.setVisible(false);
									playCard(hand.get(4));
									ImageIcon imageTopIcon = new ImageIcon(hand.get(4).getSpriteURL());
									bc.setIcon(imageTopIcon);
									bottomCardPanel.add( bc, BorderLayout.CENTER );
									bottomCardPanel.setVisible(true);
								}
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
					}else{
						if(doIHaveLeadSuit() && hand.get(4).getSuit().equals(leadSuit)){
							bhc4.setIcon(null);
							bhc4.setVisible(false);
							playCard(hand.get(4));
							ImageIcon imageTopIcon = new ImageIcon(hand.get(4).getSpriteURL());
							bc.setIcon(imageTopIcon);
							bottomCardPanel.add( bc, BorderLayout.CENTER );
							bottomCardPanel.setVisible(true);
						}else if(doIHaveLeadSuit() == false){
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
					}else if(myLead){
						if(hand.get(5).getSuit().equals("H")){
							if(brokenHearts){
								bhc5.setIcon(null);
								bhc5.setVisible(false);
								playCard(hand.get(5));
								ImageIcon imageTopIcon = new ImageIcon(hand.get(5).getSpriteURL());
								bc.setIcon(imageTopIcon);
								bottomCardPanel.add( bc, BorderLayout.CENTER );
								bottomCardPanel.setVisible(true);
							}else{
								checkAllHearts();
								if(allHearts){
									bhc5.setIcon(null);
									bhc5.setVisible(false);
									playCard(hand.get(5));
									ImageIcon imageTopIcon = new ImageIcon(hand.get(5).getSpriteURL());
									bc.setIcon(imageTopIcon);
									bottomCardPanel.add( bc, BorderLayout.CENTER );
									bottomCardPanel.setVisible(true);
								}
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
					}else{
						if(doIHaveLeadSuit() && hand.get(5).getSuit().equals(leadSuit)){
							bhc5.setIcon(null);
							bhc5.setVisible(false);
							playCard(hand.get(5));
							ImageIcon imageTopIcon = new ImageIcon(hand.get(5).getSpriteURL());
							bc.setIcon(imageTopIcon);
							bottomCardPanel.add( bc, BorderLayout.CENTER );
							bottomCardPanel.setVisible(true);
						}else if(doIHaveLeadSuit() == false){
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
					}else if(myLead){
						if(hand.get(6).getSuit().equals("H")){
							if(brokenHearts){
								bhc6.setIcon(null);
								bhc6.setVisible(false);
								playCard(hand.get(6));
								ImageIcon imageTopIcon = new ImageIcon(hand.get(6).getSpriteURL());
								bc.setIcon(imageTopIcon);
								bottomCardPanel.add( bc, BorderLayout.CENTER );
								bottomCardPanel.setVisible(true);
							}else{
								checkAllHearts();
								if(allHearts){
									bhc6.setIcon(null);
									bhc6.setVisible(false);
									playCard(hand.get(6));
									ImageIcon imageTopIcon = new ImageIcon(hand.get(6).getSpriteURL());
									bc.setIcon(imageTopIcon);
									bottomCardPanel.add( bc, BorderLayout.CENTER );
									bottomCardPanel.setVisible(true);
								}
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
					}else{
						if(doIHaveLeadSuit() && hand.get(6).getSuit().equals(leadSuit)){
							bhc6.setIcon(null);
							bhc6.setVisible(false);
							playCard(hand.get(6));
							ImageIcon imageTopIcon = new ImageIcon(hand.get(6).getSpriteURL());
							bc.setIcon(imageTopIcon);
							bottomCardPanel.add( bc, BorderLayout.CENTER );
							bottomCardPanel.setVisible(true);
						}else if(doIHaveLeadSuit() == false){
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
					}else if(myLead){
						if(hand.get(7).getSuit().equals("H")){
							if(brokenHearts){
								bhc7.setIcon(null);
								bhc7.setVisible(false);
								playCard(hand.get(7));
								ImageIcon imageTopIcon = new ImageIcon(hand.get(7).getSpriteURL());
								bc.setIcon(imageTopIcon);
								bottomCardPanel.add( bc, BorderLayout.CENTER );
								bottomCardPanel.setVisible(true);
							}else{
								checkAllHearts();
								if(allHearts){
									bhc7.setIcon(null);
									bhc7.setVisible(false);
									playCard(hand.get(7));
									ImageIcon imageTopIcon = new ImageIcon(hand.get(7).getSpriteURL());
									bc.setIcon(imageTopIcon);
									bottomCardPanel.add( bc, BorderLayout.CENTER );
									bottomCardPanel.setVisible(true);
								}
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
					}else{
						if(doIHaveLeadSuit() && hand.get(7).getSuit().equals(leadSuit)){
							bhc7.setIcon(null);
							bhc7.setVisible(false);
							playCard(hand.get(7));
							ImageIcon imageTopIcon = new ImageIcon(hand.get(7).getSpriteURL());
							bc.setIcon(imageTopIcon);
							bottomCardPanel.add( bc, BorderLayout.CENTER );
							bottomCardPanel.setVisible(true);
						}else if(doIHaveLeadSuit() == false){
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
					}else if(myLead){
						if(hand.get(8).getSuit().equals("H")){
							if(brokenHearts){
								bhc8.setIcon(null);
								bhc8.setVisible(false);
								playCard(hand.get(8));
								ImageIcon imageTopIcon = new ImageIcon(hand.get(8).getSpriteURL());
								bc.setIcon(imageTopIcon);
								bottomCardPanel.add( bc, BorderLayout.CENTER );
								bottomCardPanel.setVisible(true);
							}else{
								checkAllHearts();
								if(allHearts){
									bhc8.setIcon(null);
									bhc8.setVisible(false);
									playCard(hand.get(8));
									ImageIcon imageTopIcon = new ImageIcon(hand.get(8).getSpriteURL());
									bc.setIcon(imageTopIcon);
									bottomCardPanel.add( bc, BorderLayout.CENTER );
									bottomCardPanel.setVisible(true);
								}
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
					}else{
						if(doIHaveLeadSuit() && hand.get(8).getSuit().equals(leadSuit)){
							bhc8.setIcon(null);
							bhc8.setVisible(false);
							playCard(hand.get(8));
							ImageIcon imageTopIcon = new ImageIcon(hand.get(8).getSpriteURL());
							bc.setIcon(imageTopIcon);
							bottomCardPanel.add( bc, BorderLayout.CENTER );
							bottomCardPanel.setVisible(true);
						}else if(doIHaveLeadSuit() == false){
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
					}else if(myLead){
						if(hand.get(9).getSuit().equals("H")){
							if(brokenHearts){
								bhc9.setIcon(null);
								bhc9.setVisible(false);
								playCard(hand.get(9));
								ImageIcon imageTopIcon = new ImageIcon(hand.get(9).getSpriteURL());
								bc.setIcon(imageTopIcon);
								bottomCardPanel.add( bc, BorderLayout.CENTER );
								bottomCardPanel.setVisible(true);
							}else{
								checkAllHearts();
								if(allHearts){
									bhc9.setIcon(null);
									bhc9.setVisible(false);
									playCard(hand.get(9));
									ImageIcon imageTopIcon = new ImageIcon(hand.get(9).getSpriteURL());
									bc.setIcon(imageTopIcon);
									bottomCardPanel.add( bc, BorderLayout.CENTER );
									bottomCardPanel.setVisible(true);
								}
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
					}else{
						if(doIHaveLeadSuit() && hand.get(9).getSuit().equals(leadSuit)){
							bhc9.setIcon(null);
							bhc9.setVisible(false);
							playCard(hand.get(9));
							ImageIcon imageTopIcon = new ImageIcon(hand.get(9).getSpriteURL());
							bc.setIcon(imageTopIcon);
							bottomCardPanel.add( bc, BorderLayout.CENTER );
							bottomCardPanel.setVisible(true);
						}else if(doIHaveLeadSuit() == false){
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
					}else if(myLead){
						if(hand.get(10).getSuit().equals("H")){
							if(brokenHearts){
								bhc10.setIcon(null);
								bhc10.setVisible(false);
								playCard(hand.get(10));
								ImageIcon imageTopIcon = new ImageIcon(hand.get(10).getSpriteURL());
								bc.setIcon(imageTopIcon);
								bottomCardPanel.add( bc, BorderLayout.CENTER );
								bottomCardPanel.setVisible(true);
							}else{
								checkAllHearts();
								if(allHearts){
									bhc10.setIcon(null);
									bhc10.setVisible(false);
									playCard(hand.get(10));
									ImageIcon imageTopIcon = new ImageIcon(hand.get(10).getSpriteURL());
									bc.setIcon(imageTopIcon);
									bottomCardPanel.add( bc, BorderLayout.CENTER );
									bottomCardPanel.setVisible(true);
								}
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
					}else{
						if(doIHaveLeadSuit() && hand.get(10).getSuit().equals(leadSuit)){
							bhc10.setIcon(null);
							bhc10.setVisible(false);
							playCard(hand.get(10));
							ImageIcon imageTopIcon = new ImageIcon(hand.get(10).getSpriteURL());
							bc.setIcon(imageTopIcon);
							bottomCardPanel.add( bc, BorderLayout.CENTER );
							bottomCardPanel.setVisible(true);
						}else if(doIHaveLeadSuit() == false){
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
					}else if(myLead){
						if(hand.get(11).getSuit().equals("H")){
							if(brokenHearts){
								bhc11.setIcon(null);
								bhc11.setVisible(false);
								playCard(hand.get(11));
								ImageIcon imageTopIcon = new ImageIcon(hand.get(11).getSpriteURL());
								bc.setIcon(imageTopIcon);
								bottomCardPanel.add( bc, BorderLayout.CENTER );
								bottomCardPanel.setVisible(true);
							}else{
								checkAllHearts();
								if(allHearts){
									bhc11.setIcon(null);
									bhc11.setVisible(false);
									playCard(hand.get(11));
									ImageIcon imageTopIcon = new ImageIcon(hand.get(11).getSpriteURL());
									bc.setIcon(imageTopIcon);
									bottomCardPanel.add( bc, BorderLayout.CENTER );
									bottomCardPanel.setVisible(true);
								}
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
					}else{
						if(doIHaveLeadSuit() && hand.get(11).getSuit().equals(leadSuit)){
							bhc11.setIcon(null);
							bhc11.setVisible(false);
							playCard(hand.get(11));
							ImageIcon imageTopIcon = new ImageIcon(hand.get(11).getSpriteURL());
							bc.setIcon(imageTopIcon);
							bottomCardPanel.add( bc, BorderLayout.CENTER );
							bottomCardPanel.setVisible(true);
						}else if(doIHaveLeadSuit() == false){
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
					}else if(myLead){
						if(hand.get(12).getSuit().equals("H")){
							if(brokenHearts){
								bhc12.setIcon(null);
								bhc12.setVisible(false);
								playCard(hand.get(12));
								ImageIcon imageTopIcon = new ImageIcon(hand.get(12).getSpriteURL());
								bc.setIcon(imageTopIcon);
								bottomCardPanel.add( bc, BorderLayout.CENTER );
								bottomCardPanel.setVisible(true);
							}else{
								checkAllHearts();
								if(allHearts){
									bhc12.setIcon(null);
									bhc12.setVisible(false);
									playCard(hand.get(12));
									ImageIcon imageTopIcon = new ImageIcon(hand.get(12).getSpriteURL());
									bc.setIcon(imageTopIcon);
									bottomCardPanel.add( bc, BorderLayout.CENTER );
									bottomCardPanel.setVisible(true);
								}
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
					}else{
						if(doIHaveLeadSuit() && hand.get(12).getSuit().equals(leadSuit)){
							bhc12.setIcon(null);
							bhc12.setVisible(false);
							playCard(hand.get(12));
							ImageIcon imageTopIcon = new ImageIcon(hand.get(12).getSpriteURL());
							bc.setIcon(imageTopIcon);
							bottomCardPanel.add( bc, BorderLayout.CENTER );
							bottomCardPanel.setVisible(true);
						}else if(doIHaveLeadSuit() == false){
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
			}
		});
		bottomHandpanel.add(bhc12);
		
		leftHandpanel = new JPanel();
		//leftHandpanel.setBackground(new Color(0, 100, 0));
		leftHandpanel.setOpaque(false);
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
		//rightHandPanel.setBackground(new Color(0, 100, 0));
		rightHandPanel.setOpaque(false);
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
		//topHandpanel.setBackground(new Color(0, 100, 0));
		topHandpanel.setOpaque(false);
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
		//leftCardPanel.setBackground(new Color(0, 100, 0));
		leftCardPanel.setOpaque(false);
		leftCardPanel.setBounds(336, 246, 85, 135);
		frame.getContentPane().add(leftCardPanel);
		leftCardPanel.setLayout(new GridLayout(1, 0, 0, 0));
		
		lc = new JLabel("");
		leftCardPanel.add(lc);
		
		rightCardPanel = new JPanel();
		//rightCardPanel.setBackground(new Color(0, 100, 0));
		rightCardPanel.setBounds(641, 246, 85, 135);
		rightCardPanel.setOpaque(false);
		frame.getContentPane().add(rightCardPanel);
		rightCardPanel.setLayout(new GridLayout(1, 0, 0, 0));
		
		rc = new JLabel("");
		//rc.setForeground(new Color(0, 100, 0));
		//rc.setBackground(new Color(0, 100, 0));
		rightCardPanel.add(rc);
		
		bottomCardPanel = new JPanel();
		//bottomCardPanel.setBackground(new Color(0, 100, 0));
		bottomCardPanel.setOpaque(false);
		bottomCardPanel.setBounds(491, 368, 85, 135);
		frame.getContentPane().add(bottomCardPanel);
		bottomCardPanel.setLayout(new GridLayout(1, 0, 0, 0));
		
		bc = new JLabel("");
		bottomCardPanel.add(bc);
		bottomHandpanel.setVisible(false);
		
		topCardPanel = new JPanel();
		//topCardPanel.setBackground(new Color(0, 100, 0));
		topCardPanel.setOpaque(false);
		topCardPanel.setBounds(491, 161, 85, 135);
		frame.getContentPane().add(topCardPanel);
		topCardPanel.setLayout(new GridLayout(1, 0, 0, 0));
		
		tc = new JLabel("");
		//tc.setForeground(new Color(0, 100, 0));
		//tc.setBackground(new Color(0, 100, 0));
		topCardPanel.add(tc);
		
		lblIn = new JLabel("Instructions");
		lblIn.setForeground(new Color(255, 251, 0));
		lblIn.setBounds(138, 368, 76, 39);
		frame.getContentPane().add(lblIn);
		lblIn.setVisible(false);
		
		leftName = new JLabel("");
		leftName.setForeground(new Color(255, 251, 0));
		leftName.setBounds(117, 189, 178, 50);
		frame.getContentPane().add(leftName);
		
		topName = new JLabel("");
		topName.setForeground(new Color(255, 251, 0));
		topName.setBounds(336, 122, 200, 31);
		frame.getContentPane().add(topName);
		
		rightName = new JLabel("");
		rightName.setForeground(new Color(255, 251, 0));
		rightName.setBounds(822, 189, 178, 50);
		frame.getContentPane().add(rightName);
		
		bottomName = new JLabel("");
		bottomName.setForeground(new Color(255, 251, 0));
		bottomName.setBounds(501, 534, 200, 39);
		frame.getContentPane().add(bottomName);
		
		instructionArea = new JTextArea();
		instructionArea.setEditable(false);
		instructionArea.setWrapStyleWord(true);
		instructionArea.setLineWrap(true);
		instructionArea.setForeground(new Color(255, 251, 0));
		instructionArea.setOpaque(false);
		//instructionArea.setBackground(new Color(0, 100, 0));
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
		
		bottomScore = new JLabel("");
		bottomScore.setForeground(new Color(255, 251, 0));
		bottomScore.setBounds(726, 534, 200, 39);
		frame.getContentPane().add(bottomScore);
		
		leftScore = new JLabel("");
		leftScore.setForeground(new Color(255, 251, 0));
		leftScore.setBounds(120, 246, 178, 50);
		frame.getContentPane().add(leftScore);
		
		topScore = new JLabel("");
		topScore.setForeground(new Color(255, 251, 0));
		topScore.setBounds(571, 119, 200, 31);
		frame.getContentPane().add(topScore);
		
		rightScore = new JLabel("");
		rightScore.setForeground(new Color(255, 251, 0));
		rightScore.setBounds(822, 264, 178, 50);
		frame.getContentPane().add(rightScore);
		topCardPanel.setOpaque(false);
		topHandpanel.setOpaque(false);
		leftCardPanel.setOpaque(false);
		leftHandpanel.setOpaque(false);
		rightCardPanel.setOpaque(false);
		rightHandPanel.setOpaque(false);
		JLabel background = new JLabel();
		background.setIcon(new ImageIcon("sprites\\background.png"));
		background.setBounds(0, 0, 1134, 705);
		frame.getContentPane().add(background);

		
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
                		
                		instructionArea.setVisible(false);
                	}else if(receiveMessage.getTypeOBJMessage().equals("GF")){
                		lblRoomFullSorry.setVisible(true);
                		btnExit.setVisible(true);
                	}else if(receiveMessage.getTypeOBJMessage().equals("DC")){
                		bottomHandpanel.setVisible(true);
                		notices.setVisible(false);
                		Card tempCard = new Card();
                		tempCard = receiveMessage.getCardOBJMessage();
                		hand.add(tempCard);
                		Integer index = hand.size();
                		ImageIcon imageIcon = new ImageIcon(receiveMessage.getCardOBJMessage().getSpriteURL());
                		cardLabels.get(index.toString()).setIcon(imageIcon);
                		cardLabels.get(index.toString()).setVisible(true);
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
        		        for(Integer i = 1; i < 14; i++){
                			cardLabels.get(i.toString()).setVisible(true);
                			cardLeftLabels.get(i.toString()).setVisible(true);
                			cardTopLabels.get(i.toString()).setVisible(true);
                			cardRightLabels.get(i.toString()).setVisible(true);
                		}
        		        
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
                		trickNumber = 1;
                	}else if(receiveMessage.getTypeOBJMessage().equals("SP")){
                		if( mySeat == 0){
                			mySeat = receiveMessage.getDataOBJMessage();
                			bottomName.setText(clientName);
                		}
                		if( seated == 1){
                			leftName.setText(receiveMessage.getMessageOBJMessage());
                		}else if ( seated == 2){
                			topName.setText(receiveMessage.getMessageOBJMessage());
                		}else if ( seated == 3){
                			rightName.setText(receiveMessage.getMessageOBJMessage());
                		}
                		seated++;
                	}else if(receiveMessage.getTypeOBJMessage().equals("SC")){
                		trickNumber = receiveMessage.getTrickOBJMessage();
                		Integer labelToEdit = trickNumber;
                		if( receiveMessage.getMessageOBJMessage().equals(leftName.getText())){
                			ImageIcon imageTopIcon = new ImageIcon(receiveMessage.getCardOBJMessage().getSpriteURL());
                     		lc.setIcon(imageTopIcon);
                     		cardLeftLabels.get(labelToEdit.toString()).setIcon(null);
             		        leftCardPanel.add(lc, BorderLayout.CENTER );
                		}else if ( receiveMessage.getMessageOBJMessage().equals(topName.getText())){
                			ImageIcon imageTopIcon = new ImageIcon(receiveMessage.getCardOBJMessage().getSpriteURL());
                     		tc.setIcon(imageTopIcon);
             		        topCardPanel.add(tc, BorderLayout.CENTER );
             		        cardTopLabels.get(labelToEdit.toString()).setIcon(null);
                		}else if ( receiveMessage.getMessageOBJMessage().equals(rightName.getText())){
                			ImageIcon imageTopIcon = new ImageIcon(receiveMessage.getCardOBJMessage().getSpriteURL());
                     		rc.setIcon(imageTopIcon);
             		        rightCardPanel.add(rc, BorderLayout.CENTER );
             		        cardRightLabels.get(labelToEdit.toString()).setIcon(null);
                		}
                		
                	}else if(receiveMessage.getTypeOBJMessage().equals("AC")){
                		firstTurn = false;
                		myTurn = true;
                		leadSuit =  receiveMessage.getMessageOBJMessage();
                	}else if(receiveMessage.getTypeOBJMessage().equals("US")){
                		trickNumber = receiveMessage.getTrickOBJMessage();
	               		if(receiveMessage.getMessageOBJMessage().equals(bottomName.getText())){
	                			bottomScore.setText(receiveMessage.getCurrentPointsOBJMessage()+ "(" + receiveMessage.getGamePointsOBJMessage() +  ")");
	                	}else if(receiveMessage.getMessageOBJMessage().equals(topName.getText())){
	                			topScore.setText(receiveMessage.getCurrentPointsOBJMessage() + "(" + receiveMessage.getGamePointsOBJMessage() +  ")");
	              		}else if(receiveMessage.getMessageOBJMessage().equals(leftName.getText())){
	               			leftScore.setText(receiveMessage.getCurrentPointsOBJMessage() + "(" + receiveMessage.getGamePointsOBJMessage() +  ")");
	               		}else if(receiveMessage.getMessageOBJMessage().equals(rightName.getText())){
	              			rightScore.setText(receiveMessage.getCurrentPointsOBJMessage() + "(" + receiveMessage.getGamePointsOBJMessage() +  ")");
	                		}
                	}else if(receiveMessage.getTypeOBJMessage().equals("NT")){
                		trickNumber = receiveMessage.getTrickOBJMessage();
                		bc.setIcon(null);
                		lc.setIcon(null);
                		tc.setIcon(null);
                		rc.setIcon(null);
                		if(receiveMessage.getDataOBJMessage() == 1){
                			brokenHearts = true;
                		}
                	}else if(receiveMessage.getTypeOBJMessage().equals("LP")){
                		firstTurn = false;
                		myTurn = true;
                		myLead = true;
                	}else if(receiveMessage.getTypeOBJMessage().equals("NH")){
                		hand.clear();
                		bc.setIcon(null);
                		lc.setIcon(null);
                		tc.setIcon(null);
                		rc.setIcon(null);
                		brokenHearts = false;
                		allHearts = false;
                		toPass = 3;
                		allowLeave = false;
                	}else if(receiveMessage.getTypeOBJMessage().equals("AL")){
                		allowLeave = true;
                		playAgain.setVisible(true);
                	}else if(receiveMessage.getTypeOBJMessage().equals("GO")){
                		hand.clear();
                		bc.setIcon(null);
                		lc.setIcon(null);
                		tc.setIcon(null);
                		rc.setIcon(null);
                		brokenHearts = false;
                		allHearts = false;
                		toPass = 3;
                		instructionArea.setVisible(false);
                		lblIn.setVisible(false);
                		notices.setText(receiveMessage.getMessageOBJMessage());
                		notices.setVisible(true);
                		mySeat = 0;
                		firstTurn = false;
                		myTurn = false;
                		trickNumber = 0;
                		seated = 0;
                		brokenHearts = false;
                		myLead = false;
                		allHearts = false;
                		for(Integer i = 1; i < 14; i++){
                			cardLabels.get(i.toString()).setIcon(null);
                			cardLabels.get(i.toString()).setVisible(false);
                			cardLeftLabels.get(i.toString()).setIcon(null);
                			cardLeftLabels.get(i.toString()).setVisible(false);
                			cardTopLabels.get(i.toString()).setIcon(null);
                			cardTopLabels.get(i.toString()).setVisible(false);
                			cardRightLabels.get(i.toString()).setIcon(null);
                			cardRightLabels.get(i.toString()).setVisible(false);
                		}
                		
                		leftName.setText(null);
                		leftScore.setText(null);
                		topName.setText(null);
                		topScore.setText(null);
                		bottomName.setText(null);
                		bottomScore.setText(null);
                		rightScore.setText(null);
                		rightName.setText(null);
                	}else if(receiveMessage.getTypeOBJMessage().equals("WT")){
                		if(clientName.equals(receiveMessage.getMessageOBJMessage())){
                			notices.setText("My Turn");
                		}else{
                			notices.setText(receiveMessage.getMessageOBJMessage() + "'s Turn");
                		}
                		notices.setVisible(true);
                	}
                	
                } catch (ClassNotFoundException e){
                	e.printStackTrace();
                } catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	public static void playNextHand(){
		playAgain.setVisible(false);
		messageOBJ playerLeftMessage = new messageOBJ();
		playerLeftMessage.setTypeOBJMessage("PN");
		playerLeftMessage.setMessageOBJMessage("");
		playerLeftMessage.setUsernameOBJMessage(clientName);
		outMessage = playerLeftMessage;
		outThread.run();
	}
	
	
	public static void playerLeft(){
		messageOBJ playerLeftMessage = new messageOBJ();
		playerLeftMessage.setTypeOBJMessage("PL");
		playerLeftMessage.setMessageOBJMessage("");
		playerLeftMessage.setUsernameOBJMessage(clientName);
		outMessage = playerLeftMessage;
		outThread.run();
	}
	
	public static void playCard(Card card){
		messageOBJ passCardMessage = new messageOBJ();
		passCardMessage.setTypeOBJMessage("LC");
		passCardMessage.setMessageOBJMessage("");
		passCardMessage.setUsernameOBJMessage(clientName);
		passCardMessage.setCardOBJMessage(card);
		passCardMessage.setDataOBJMessage(mySeat);
		myTurn = false;
		firstTurn = false;
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
			for( int k = 0; k < passedCards.size(); k++){
				hand.get(passedCards.get(k)).setRank(passedToMe.get(k).getRank());
				hand.get(passedCards.get(k)).setSuit(passedToMe.get(k).getSuit());
				hand.get(passedCards.get(k)).setSpriteURL(passedToMe.get(k).getSpriteURL());
				hand.get(passedCards.get(k)).setPower(passedToMe.get(k).getPower());
				ImageIcon imageIcon = new ImageIcon(passedToMe.get(k).getSpriteURL());
        		cardLabels.get(Integer.toString(passedCards.get(k)+1)).setIcon(imageIcon);
		        bottomHandpanel.add( cardLabels.get(Integer.toString(passedCards.get(k)+1)), BorderLayout.CENTER );
		        cardLabels.get(Integer.toString(passedCards.get(k)+1)).setVisible(true);
			}
			passedToMe.clear();
			passedCards.clear();
		}
	}
	
	public static Boolean doIHaveLeadSuit(){
		boolean haveSuit = false;
		Integer index = 0;
		for( int i = 0; i < hand.size(); i++){
			index = i + 1;
			if( hand.get(i).getSuit().equals(leadSuit) && cardLabels.get(index.toString()).isVisible()){
				haveSuit = true;
			}
		}
		return haveSuit;
	}
	
	public static void checkAllHearts(){
		Integer check = 0;
		for(int i = 0; i < hand.size(); i ++){
			if(hand.get(i).getSuit().equals("H")){
				check++;
			}
		}
		if(check == hand.size()){
			allHearts = true;
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
			myLead = false;
			leadSuit = "";
		}
	}
}
