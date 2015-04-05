import java.io.Serializable;


public class messageOBJ implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	private String username;
	private String typeOfMessage;
	private Card card;
	private Integer data;
	private Integer trick;
	private Integer currentPoints;
	private Integer gamePoints;
	
	public messageOBJ(){
		message = null;
		username = null;
		typeOfMessage = null;
		card = null;
		data = 0;
		currentPoints = 0;
		gamePoints = 0;
		trick = 0;
	}
	
	public void setMessageOBJMessage(String clientMessage){
		message = clientMessage;
	}
	
	public void setUsernameOBJMessage(String clientName){
		username = clientName;
	}
	
	public void setTypeOBJMessage(String messageType){
		typeOfMessage = messageType;
	}
	
	public void setCardOBJMessage(Card inCard){
		card = inCard;
	}
	
	public void setDataOBJMessage(Integer input){
		data = input;
	}
	
	public void setCurrentPointsOBJMessage(Integer input){
		currentPoints = input;
	}
	
	public void setGamePointsOBJMessage(Integer input){
		gamePoints = input;
	}
	
	public String getMessageOBJMessage(){
		return message;
	}
	
	public String getUsernameOBJMessage(){
		return username;
	}
	
	public String getTypeOBJMessage(){
		return typeOfMessage;
	}
	
	public Card getCardOBJMessage(){
		return card;
	}
	
	public Integer getDataOBJMessage(){
		return data;
	}
	
	public void setTrickOBJMessage(Integer trickNumber){
		trick = trickNumber;
	}
	
	public Integer getTrickOBJMessage(){
		return trick;
	}
	
	public Integer getCurrentPointsOBJMessage(){
		return currentPoints;
	}
	
	public Integer getGamePointsOBJMessage(){
		return gamePoints;
	}
	
}
