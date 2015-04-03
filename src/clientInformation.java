import java.net.InetAddress;


public class clientInformation {
	private String username;
	private InetAddress ipAddress;
	private int port;
	private boolean ready;
	
	public clientInformation(){
		username = null;
		ipAddress = null;
		port = 0;
		ready = false;
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


}