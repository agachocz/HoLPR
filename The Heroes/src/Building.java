import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.Serializable;



public class Building extends Thing implements Serializable {

	/**
	 * Budynek
	 * @throws IOException 
	 *
	 */
	
	private int entryX, entryY;
	private boolean connected = false;
	
	public Building(int id, String name, String info, String logo) throws IOException {
		super(id, name, info, logo);
	}

	public Building (int id, String name, String info, String logo, int x, int y, int width, int height) throws IOException{
		super(id, name, info, logo, x, y, width, height);
		this.setImage(Main.getImage(logo));
		this.update();
	} 
	
	public boolean isConnected(){return connected;}
	public int getEntryX(){return entryX;}
	public int getEntryY(){return entryY;}
	
	public void setEntry(int x, int y){entryX = x; entryY = y; connected = true;}

	
}
