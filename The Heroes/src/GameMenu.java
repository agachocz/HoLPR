import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;
import java.io.IOException;

public class GameMenu {

	Micromap micromap;
	int x;
	int y;
	int width;
	int height;
	Image menuImage;
	
	Rectangle newDay;
	Rectangle mainMenu;
	
	
	public GameMenu(int x, int y, int width, int height, Micromap micromap, String menuImageS)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.micromap = micromap;
		try {
			menuImage = Main.getImage(menuImageS);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mainMenu = new Rectangle(x+100, y+450, 90, 45);
		newDay = new Rectangle(x+100, y+495, 90, 45);
	}
	
	public void paint(Graphics g, ImageObserver io, Rectangle scene, Party curParty)
	{
		//Malowanie menu
		g.setColor(Color.BLACK);
		g.fillRect(x, y, width, height);
		micromap.paint(g, io, x+35, y+37, scene); 
		g.drawImage(curParty.getLogoIMG(), x+115, 270, io);
		g.drawImage(menuImage, x, y, io);
		g.setColor(Color.WHITE);
		g.drawString(curParty.getName(), x+123, 170+micromap.getHeight());
		g.drawString("Bud¿et: "+curParty.getMoney(), x+105, 170+micromap.getHeight()+20); 
		g.drawString("Kie³basa", x+105, 170+micromap.getHeight()+35); 
		g.drawString("wyborcza: "+curParty.getSausage(), x+105, 170+micromap.getHeight()+48); 
		g.drawString("Elektorat: "+curParty.getVotes(), x+105, 170+micromap.getHeight()+63); 
		
		curParty.paint(g, io, x, y);
	}
	
	public boolean isMainMenuClicked(Rectangle r){
		if(r.intersects(mainMenu)) return true;
		else return false;
	}
	
	public boolean isNewDayClicked(Rectangle r){
		if(r.intersects(newDay)) return true;
		else return false;
	}
}
