import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.io.IOException;

public class Battle {
	
	Army attacker, defender;
	
	//tablica ze œcie¿kami do grafik t³a
	String[] backgroundS = new String[3];
	{
		
	}
	
	Image[] background = new Image[3];

	public Battle(Army attacker, Army defender){
		this.attacker = attacker;
		this.defender = defender;
		
		for(int i = 0; i < backgroundS.length; i++){
			try {
				background[i] = Main.getImage(backgroundS[i]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void paint(Graphics g, ImageObserver io, int width, int height){
		g.fillRect(0, 0, width, height);
		
		
	}
}
