import java.net.InetAddress;
import java.util.ArrayList;


public class clientInformation {
	private String username;
	private InetAddress ipAddress;
	private int port;
	private boolean ready;
	private int seating;
	private ArrayList<Card> hand = new ArrayList<Card>();
	
	public clientInformation(){
		username = null;
		ipAddress = null;
		port = 0;
		ready = false;
		seating = 0;
	}
	
	public void setUsernameCI(String clientName){
		username = clientName;
	}
	
	public void setIPCI(InetAddress ip){
		ipAddress = ip;
	}
	
	public void setPortCI(int clientPort){
		port = clientPort;
	}
	
	public void setReadyCI(boolean status){
		ready = status;
	}
	
	public void setSeatingCI(int i){
		seating = i;
	}
	
	public String getUsernameCI(){
		return username;
	}
	
	public InetAddress getIPCI(){
		return ipAddress;
	}
	
	public int getPortCI(){
		return port;
	}
	
	public boolean getReadyCI(){
		return ready;
	}

	public int getSeatingCI(){
		return seating;
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