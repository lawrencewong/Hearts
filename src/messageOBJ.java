import java.io.Serializable;


public class messageOBJ implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	private String username;
	private String typeOfMessage;
	
	public messageOBJ(){
		message = null;
		username = null;
		typeOfMessage = null;
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
	
	public String getMessageOBJMessage(){
		return message;
	}
	
	public String getUsernameOBJMessage(){
		return username;
	}
	
	public String getTypeOBJMessage(){
		return typeOfMessage;
	}
}
