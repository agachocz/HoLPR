import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.util.ArrayList;


public class Army extends Thing {
	
	/**
	 * Zbiór grup przynale¿¹cych do jednego bohatera
	 */

private int capacity = 8; //maksymalna liczba grup w armii
private ArrayList<Group> groups;
private Hero hero;
private Bus bus;

private boolean selected = false;
public int iconX, iconY;

//True, kiedy armia jest wewn¹trz zamku (w górnym rzêdzie)
private boolean onSite = true;
private Rectangle upRect = new Rectangle(368, 530, capacity*97, 100);
private Rectangle downRect = new Rectangle(368, 676, capacity*97, 100);



	public Army(byte id, String name, String info, String logo, Bus bus) throws IOException {
		super(id, name, info, logo);
		this.bus = bus;
		this.setRect(upRect);
		
		groups = new ArrayList<Group>();
	}
	
	public void paint (Graphics g, ImageObserver io, int x, int y){
		int i = 0;

		if(hasHero()){
			hero.paint(g, io, x, y);
			i++;
		}
		for(Group group : groups)
		{
			group.paint(g,  io, x+i*97, y);
			i++;
		}
	}
	
	public void paintIcon (Graphics g, ImageObserver io, int x, int y){
		
		g.drawImage(getHero().getMenuIMG(), x, y, io);
		if(selected){
			g.setColor(Color.yellow);
			g.drawRect(x, y, 62, 40);
		}
		
		iconX = x;
		iconY = y;
	}
	
	public void paintInfo(Graphics g, ImageObserver io, int x, int y){
		hero.paint(g, io, x+16, y);
		g.setColor(Color.white);
		g.drawString(hero.getName(), x+110, y+5);
		
		String[] infoTab = hero.getInfoInLines(20);
		for(int i=0; i<infoTab.length; i++){
			g.drawString(infoTab[i], x+110, y+5+20*i);
		}
		
		for(int i=0; i<groups.size(); i++){
			if(i < 5){
				groups.get(i).paint(g, io, x+60+i*50, y+100); 
			}
			else {
				groups.get(i).paint(g, io, x+35+i*50, y+175);
			}
		}
	}
	
	public boolean isSelected(){return selected;}
	
	public boolean isClicked(Rectangle r){

		if(r.intersects(new Rectangle(iconX, iconY, 62, 40))){
			selected = true;
		}
		else selected = false;
		return selected;
	}
	
	public void joinGroup(Group g)
	{
		if(takenSpace()<capacity) {
			
			//Sprawdziæ rozmiary jednej grupy i rozmieœciæ odpowiednio
			g.setRect(this.x + takenSpace()*97, this.y, g.getWidth(), g.getHeight());
			groups.add(g);
		}
	}
	public void joinSoldier(Soldier s)
	{
		boolean b = true;
		for(Group g : groups)
		{
			if(s.getName().equals(g.getType().getName()))
			{
				g.joinToGroup(s);
				b = false;
				break;
			}
		}
		
		if(b)
		{
			try {
				Group newGroup = new Group((byte)Main.ID++, "Grupa ¿o³nierzy typu "+s.getName(), s.getInfo(), s.getLogo(), (Soldier)s.clone());
				newGroup.joinToGroup(s);
				joinGroup(newGroup);
				
			} catch (IOException | CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public void joinHero(Hero h){
		if(!hasHero() && takenSpace()<capacity){
			hero = h;
			h.setX(this.getX());
			h.setY(this.getY());
			order();
		}
	}
	
	public boolean hasHero(){if(hero == null)return false; else return true;}
	public Hero getHero(){return hero;}
	public int takenSpace(){
		int ts = groups.size();
		if(hasHero()) ts++;
		return ts;
	}
	
	public boolean isOnSite(){return onSite;}
	public void setPlace(boolean b){
		onSite = b;
		if(b){
			setRect(upRect);
		}
		else setRect(downRect);
	}
	
	public void removeGroup(int id){
		
		if(hasHero() && hero.getId() == id){
			hero = null;
		}
		
		else {
					int index = -1;
		for(Group g : groups){
			if(g.getId() == id) index = groups.indexOf(g);
		}
		
		if(index > -1){
			groups.remove(index);
		}
		}
		
		order();
		
	}
	
	//Replace groups after changes
	public void order(){
		
		int h = 0;
		if(hasHero()) h = 1;
		
		for(int i=0; i<groups.size(); i++){
			groups.get(i).setX(this.x + (i+h)*97);
		}
	}
	
	public Thing getSelected(Rectangle r){
		Thing t = null;
		

		if(hasHero()){
			if(r.intersects(hero.getRect())){
				t = hero;
			}
		}
		
		if(t == null){
		for(Group g : groups){
			
			
			if(r.intersects(g.getRect())){
				t = g;
			}
		}
		}
		
		return t;
	}
	
	public boolean moveGroup(Thing t){
		if(t.getClass().getName() == "Hero"){
			if(!hasHero()){
				joinHero((Hero) t);
				return true;
			}
		}
		else if(hasHero()){
			joinGroup((Group) t);
			return true;
		}
		
		return false;
	}

	public Bus getBus(){return bus;}
}
