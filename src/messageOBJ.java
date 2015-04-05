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
	
	public messageOBJ(){
		message = null;
		username = null;
		typeOfMessage = null;
		card = null;
		data = 0;
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
}
