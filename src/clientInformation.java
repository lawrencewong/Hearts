import java.net.InetAddress;


public class clientInformation {
	private String username;
	private InetAddress ipAddress;
	private int port;
	
	public clientInformation(){
		username = null;
		ipAddress = null;
		port = 0;
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
	
	public String getUsernameCI(){
		return username;
	}
	
	public InetAddress getIPCI(){
		return ipAddress;
	}
	
	public int getPortCI(){
		return port;
	}

}