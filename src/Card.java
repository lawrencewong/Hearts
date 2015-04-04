import java.awt.image.BufferedImage;
import java.io.Serializable;

public class Card implements Serializable{
	private String suit;
	private String rank;
	private String sprite;
	
	public Card(){
		suit = null;
		rank = null;
		sprite = null;
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

	public String getSuit() {
		return suit;
	}

	public String getRank() {
		return rank;
	}
	
	public String getSpriteURL() {
		return sprite;
	}
}
