import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;


public class Road extends Thing implements Serializable {
	
	private int northID = -1; //ID do tego, co znajduje siê na pó³noc
	private int southID = -1; //Równe -1 gdy nie wskazuje na nic
	private int westID = -1;
	private int eastID = -1;
	
	private int type; //typ drogi
	/*
	 * -1 - brak orientacji, typ podstawowy
	 * 0 - pozioma (W-E)
	 * 1 - pionowa (N-S)
	 * 2 - skrêt (W-N)
	 * 3 - skrêt (W-S)
	 * 4 - skrêt (E-N)
	 * 5 - skrêt (E-S)
	 * 6 - skrzy¿owanie T (W-N-E)
	 * 7 - skrzy¿owanie T (N-E-S)
	 * 8 - skrzy¿owanie T (E-S-W)
	 * 9 - skrzy¿owanie T (S-W-N)
	 * 10 - skrzy¿owanie (N-S-W-E)
	 */
	
	private String[] imgS;
	private transient Image[] img;
	private transient Image logoImg;
	
	private int buildingID = -1;
	
	//true - jeœli droga jest w zasiêgu
	private boolean available = false;
	//Trasa do tej drogi z obecnego punktu
	//Jeœli ta droga jest w zasiêgu
	private ArrayList<Integer> route;

	public Road(int id, String name, String info, String logo, String imgS[])
			throws IOException {
		super(id, name, info, logo);
		
		this.setWidth(32);
		this.setHeight(32);	
		
		this.imgS = imgS;
		img = new Image[imgS.length];
		logoImg = Main.getImage(logo);
		for(int i=0; i<imgS.length; i++){
			img[i] = Main.getImage(imgS[i]);
		}
		setType(-1);
		
		buildingID = -1;
	}
	
	@Override
	public void paint(Graphics g, ImageObserver io, int posX, int posY){ // posX - pocz¹tek ekranu w osi X
	    Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(curImg, x-posX, y-posY, io);
		
		if(available){
			g2d.setColor(Color.yellow);
			g2d.drawOval(x-posX+10, y-posY+10, 10, 10);
			g2d.setColor(Color.white);
		}
		//g2d.drawRect(x-posX, y-posY, width, height);
		//System.out.println(curImg.getHeight(io));
		if(isChanging) change();
}
	
	public void select(){available = true;}
	public void deselect(){available = false;}
	public boolean isAvailable(){return available;}
	
	//Funkcja zaznaczaj¹ca/odznaczaj¹ca kolejne drogi w odleg³oœci równej steps
	//passed - przebyta dot¹d droga, przy odznaczaniu równa null
	public void checkNext(Road[][] roads, int i, int j, int steps, boolean b, ArrayList<Integer> passed, int dir){
		if(steps > 0){
			steps--;
		if(b) {
			select();
			passed = (ArrayList<Integer>) passed.clone();
			passed.add(dir);
			//System.out.println("i: "+i+" j: "+j+" dir "+dir);
			//for(int r : route){
			//	System.out.println(r);
			//}
			this.route = passed;
		}
		else deselect();
		
		if(northID != -1){
			if(roads[i][j-1] != null){
			//passed.add(2);
			roads[i][j-1].checkNext(roads, i, j-1, steps, b, passed, 0);
			}
		}
		if(eastID != -1){
			if(roads[i+1][j] != null){
			//passed.add(3);
			roads[i+1][j].checkNext(roads, i+1, j, steps, b, passed, 1);
			}
		}
		if(southID != -1){
			if(roads[i][j+1] != null){
			//passed.add(0);
			roads[i][j+1].checkNext(roads, i, j+1, steps, b, passed, 2);
			}
		}
		if(westID != -1){
			if(roads[i-1][j] != null){
			//passed.add(1);
			roads[i-1][j].checkNext(roads, i-1, j, steps, b, passed, 3);
			}
		}
		}
	}
	
	public ArrayList<Integer> getRoute(){return route;}
	
	public void setBuildingID(int i){buildingID = i;}
	public int getBuildingID(){return buildingID;}
	
	public void setNorthID(int x){northID = x;}
	public int getNorthID(){return northID;}
	
	public void setSouthID(int x){southID = x;}
	public int getSouthID(){return southID;}
	
	public void setWestID(int x){westID = x;}
	public int getWestID(){return westID;}
	
	public void setEastID(int x){eastID = x;}
	public int getEastID(){return eastID;}
	
	public int getType(){return type;}
	public void setType(int x){
		type = x;
		if(x!=-1)setImage(img[x]);
		else setImage(logoImg); }
	
	public void update(){
		if(type!=-1)setImage(img[type]);
		else setImage(logoImg); 
	}
	
	private void readObject (ObjectInputStream in) throws ClassNotFoundException, IOException{
		in.defaultReadObject();
		this.resize();
		img = new Image[imgS.length];
		logoImg = Main.getImage(getLogo()); 
		for(int i=0; i<imgS.length; i++){
			img[i] = Main.getImage(imgS[i]);
		}
		if(type!=-1)this.setImage(img[type]);
	}
}
