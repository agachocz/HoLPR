import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Button implements Cloneable, Serializable{

	private int x;
	private int y;
	private int width;
	private int height;
	private transient Rectangle rect;
	private transient Image img;
	private String imgS;
	
	private boolean first = true;
	
	public Button(int x, int y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		rect = new Rectangle(x, y, width, height);
	}
	
	public Button(Rectangle rect){
		this.rect = rect;
	}
	
	public Button(int x, int y, int width, int height, String imgS)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.imgS = imgS;
		try {
			img = Main.getImage(imgS);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		rect = new Rectangle(x, y, width, height);
	}
	
	public Button(Rectangle rect, String imgS){
		this.rect = rect;
		this.imgS = imgS;
		try {
			img = Main.getImage(imgS);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void paint(Graphics g, ImageObserver io, int posX, int posY) throws IOException{ // posX - pocz¹tek ekranu w osi X
		/*
    	//if(first){
    		try {
    			readUpdate();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		//first = false;
    		//}
    	*/
		if(img != null){
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(img, x-posX, y-posY, io);

		
	    }
	    else {
	    	g.setColor(Color.BLUE);
	    	g.fillRect(x, y, 50, 30);
	    }
}
	
	public void setX(int i){x = i;}
	public int getX(){return x;}
	public void setY(int i){y = i;}
	public int getY(){return y;}
	public void setWidth(int i){width = i;}
	public int getWidth(){return width;}
	public void setHeight(int i){height = i;}
	public int getHeight(){return height;}
	
	public boolean isClicked(Rectangle r){
		if(r.intersects(rect)) return true;
		else return false;
	}
	
	public Button clone() throws CloneNotSupportedException {
		return (Button) super.clone();} 

	//ZAPIS I ODCZYT DANYCH
	private void writeObject(ObjectOutputStream out) throws IOException{
		out.defaultWriteObject();
	}

	private void readObject (ObjectInputStream in) throws ClassNotFoundException, IOException{
		in.defaultReadObject();
		rect = new Rectangle(x, y, width, height);
		img = Main.getImage(imgS);
	}
	
	public void readUpdate() throws IOException{
		img = Main.getImage(imgS);
	}
}
