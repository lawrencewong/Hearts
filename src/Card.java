import java.awt.image.BufferedImage;
import java.io.Serializable;

public class Card implements Serializable{
	private String suit;
	private String rank;
	private String sprite;
	private Integer internalPower;
	
	public Card(){
		suit = null;
		rank = null;
		sprite = null;
		internalPower = 0;
	}

	public void setSuit(String inSuit) {
		suit = inSuit;
	}

	public void setRank(String inRank) {
		rank = inRank;
	}
	
	public void setSpriteURL(String inSprite) {
		sprite = inSprite;
	}
	
	public void setPower(Integer power) {
		internalPower = power;
	}

	public String getSuit() {
		return suit;
	}

	public String getRank() {
		return rank;
	}
	
	public String getSpriteURL() {
		return sprite;
	}
	
	public Integer getPower() {
		return internalPower;
	}
}
