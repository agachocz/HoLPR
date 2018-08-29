import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Bus extends Thing{

	String[] imgS;
	transient Image[] img;
	private int dir; //kierunek
	private int count = 0;

	
	//Trasa do przebycia
	private ArrayList<Integer> route;
	
	public Bus(int id, String name, String info, String logo, String[] imgS) throws IOException {
		super(id, name, info, logo);
		this.imgS = imgS;
		img = new Image[imgS.length];
		
		for(int i=0; i<imgS.length; i++){
			img[i] = Main.getImage(imgS[i]);
		}
	}
	
	public void setRoute(ArrayList<Integer> route){
		this.route = route;
		nextMove();
		setChanging(true);
	
		}
	
	//Wyznaczenie kierunku na najbli¿szy punkt	
	public void nextMove(){
		if(route.size()>0){
			changeDir(route.get(0));
			route.remove(0);
	}
		else setChanging(false);
	}
	
	public void changeDir(int i){
		dir = i;
		if(i >= 0) setImage(img[i]);
	}
	
	
	public void change(){
		if(count < 4){
		switch(dir){
		case 0: addY(-8); break;
		case 1: addX(8); break;
		case 2: addY(8); break;
		case 3: addX(-8); break;
		}
		count++;
		}
		else {
			nextMove();
			count = 0;
		}
	}
	
	
	private void writeObject(ObjectOutputStream out) throws IOException{
		out.defaultWriteObject();
	}
	
	private void readObject (ObjectInputStream in) throws ClassNotFoundException, IOException{
		in.defaultReadObject();
		
		img = new Image[imgS.length];		
		for(int i=0; i<imgS.length; i++){
			img[i] = Main.getImage(imgS[i]);
	}
	}
}
