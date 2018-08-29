import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public class Thing implements Cloneable, Serializable {
	
	/**
	 * Podstawowa klasa obiektów
	 * Posiada nazwê, numer id, info i logo (ikonê)
	 */

private int id;
private String name;
private String info;
private String logo; //œcie¿ka dostêpu do obrazka

protected int x=0, y=0;
protected int width=0, height=0;
protected transient Image curImg; // Aktualnie wyœwietlane zdjêcie
protected transient Image logoIMG; //Obrazek logo
protected transient Rectangle rect;

protected boolean isChanging = false;

public Thing (int id, String name, String info, String logo) throws IOException{
	this.id = id;
	this.name = name;
	this.info = info;
	this.logo = logo;
	
	curImg = Main.getImage(logo);
	rect = new Rectangle(x, y, width, height);
} 

public Thing (int id, String name, String info, String logo, int x, int y, int width, int height) throws IOException{
	this.id = id;
	this.name = name;
	this.info = info;
	this.logo = logo;
	
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;
	
	logoIMG = curImg = Main.getImage(logo);
	rect = new Rectangle(x, y, width, height);
} 

public void paint(Graphics g, ImageObserver io, int posX, int posY){ // posX - pocz¹tek ekranu w osi X
	    Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(curImg, x-posX, y-posY, io);
		//g2d.drawRect(x-posX, y-posY, width, height);
		//System.out.println(curImg.getHeight(io));
		if(isChanging) change();
}

public int getId(){return id;}
public void setId(int x){id = x;}
public String getName(){return name;}
public String getInfo(){return info;}
public String getLogo(){return logo;}
public Image getLogoIMG(){return logoIMG;}

public int getX(){return x;}
public void setX(int i){x=i; resize();}
public void addX(int i){x+=i; resize();}

public int getY(){return y;}
public void setY(int i){y=i; resize();}
public void addY(int i){y+=i; resize();}

public int getWidth(){return width;}
public void setWidth(int x){width = x; resize();}

public int getHeight(){return height;}
public void setHeight(int x){height = x; resize();}

public Rectangle getRect(){return rect;}
public void setRect(int x, int y, int width, int height){
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;
	this.resize();
	} 

public void setRect(Rectangle r){
	this.x = (int) r.getX();
	this.y = (int) r.getY();
	this.width = (int) r.getWidth();
	this.height = (int) r.getHeight();
	this.resize();
	}

public void resize(){
	rect = new Rectangle(x, y, width, height);
}

public void setChanging(boolean b){isChanging = b;}
public boolean isChanging(){return isChanging;}

public void change(){
	
}

public void setImage(Image img){curImg = img;}
public void setLogo(Image img){logoIMG = img;}

public void update() throws IOException{
	logoIMG = curImg = Main.getImage(logo);
	rect = new Rectangle(x, y, width, height); 
}

public Thing clone() throws CloneNotSupportedException {
	return (Thing) super.clone();} 

//ZAPIS I ODCZYT DANYCH
private void writeObject(ObjectOutputStream out) throws IOException{
	out.defaultWriteObject();
}

private void readObject (ObjectInputStream in) throws ClassNotFoundException, IOException{
	in.defaultReadObject();
	rect = new Rectangle(x, y, width, height);
	logoIMG = curImg = Main.getImage(logo);

}


}
