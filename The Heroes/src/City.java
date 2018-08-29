import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.ObjectInputStream;


public class City extends Thing {
	
	/**
	 * Miasto. Siedziba partii.
	 * Posiada budynki i miejsce na armiê.
	 */
	
	//private Building[] buildings;
	private Building mainBuilding;
	private Barracks[] barracks;
	private Restaurant restaurant;
	private Army localArmy;
	private Army visitingArmy;
	
	private Thing selGroup = null; //zaznaczona grupa
	
	//true - jeœli zaznaczona grupa pochodzi z lokalnej armii
	private boolean selArmy = true;
	
	private transient Image menuImage;
	private String menuImageS = "Graphics//Menu//zamek.png";
	String menuIMGS;
	transient Image menuIMG;
	private boolean first = true;
	//private Rectangle button = new Rectangle(1145, 770, 85, 50);
	private Button okButton = new Button(1145, 770, 85, 50);

	public City(int id, String name, String info, String logo, Building mainBuilding, 
			Barracks[] barracks, Restaurant restaurant, Party party, String menuIMGS) throws IOException {
		super(id, name, info, logo);
		
		this.menuIMGS = menuIMGS;
		menuIMG = Main.getImage(menuIMGS);
		this.mainBuilding = mainBuilding;
		this.restaurant = restaurant;
		this.barracks = barracks;
		for(int i=0; i<barracks.length; i++){
			
			barracks[i].setX(0);
			barracks[i].setY(i*140);
			barracks[i].setWidth(300);
			barracks[i].setHeight(140);
			barracks[i].updateButton();
		}
		menuImage = Main.getImage(menuImageS);
		
		localArmy = new Army((byte) Main.ID++, name, info, logo, party.makeBus()); //przepisujê dane miasta
		visitingArmy = new Army((byte) Main.ID++, name, info, logo, party.makeBus());
		visitingArmy.setPlace(false);
		
		System.out.println(mainBuilding.getEntryX());
		visitingArmy.getBus().setRect(mainBuilding.getEntryX()*32, mainBuilding.getEntryY()*32, 32, 32);
		
	}
	
	public void paint (Graphics g, ImageObserver io, int width, int height)
	{
		
		g.fillRect(0, 0, width, height);
		
		//Rysowanie baraków
		for(int i=0; i<barracks.length; i++){
			barracks[i].paint(g, io, width, height);
		}
		
		//Rysowanie restauracji
		restaurant.paint(g, io, 500, 0);
		
		g.drawImage(menuImage, 0, height-330, io);
		g.setColor(Color.WHITE);
		g.drawString(getName(), 125, height-300);
		g.drawImage(mainBuilding.getLogoIMG(), 15, height-320, 95, 100, io);
		
		try {
			okButton.paint(g, io, 0, 0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Rysowanie armii
		localArmy.paint(g, io, 368, height-320);
		visitingArmy.paint(g, io, 368, height-174);

		if(selGroup != null){
			g.setColor(Color.YELLOW);
			g.drawRect(selGroup.getX(), selGroup.getY(), selGroup.getWidth(), selGroup.getHeight());
		}
		//g.drawRect(localArmy.getX(), localArmy.getY(), localArmy.getWidth(), localArmy.getHeight());
		//g.drawRect(visitingArmy.getX(), visitingArmy.getY(), visitingArmy.getWidth(), visitingArmy.getHeight());
		
	}
	
	public void moveGroup(Rectangle r){
		if(selGroup != null){
			
			if(r.intersects(localArmy.getRect())){
	
				if(localArmy.moveGroup(selGroup)){
				
				if(selArmy){
					localArmy.removeGroup(selGroup.getId());
				}
				else visitingArmy.removeGroup(selGroup.getId());
				}
				
			}
			else if(r.intersects(visitingArmy.getRect())){

				if(visitingArmy.moveGroup(selGroup)){
					
					if(selArmy){
					localArmy.removeGroup(selGroup.getId());
					}
					else visitingArmy.removeGroup(selGroup.getId());
				}	
				
			}
			
			selGroup = null;
		}
		else {
			selGroup = localArmy.getSelected(r);
			selArmy = true;
			if(selGroup == null) {
				selGroup = visitingArmy.getSelected(r);
				selArmy = false;
			}
		}
	}
	
	
	
	public Image getMenuIMG(){
		return menuIMG;
	}
	
	public Barracks[] getBarracksTab(){return barracks;}
	public Barracks getBarracks(int x){return barracks[x];}
	public Restaurant getRestaurant(){return restaurant;}
	public Building getMainBuilding(){return mainBuilding;}
	public Button getOkButton(){return okButton;}
	public Army getLocalArmy(){return localArmy;}
	public Army getVisitingArmy(){return visitingArmy;}
	public void joinToArmy(Soldier s){localArmy.joinSoldier(s);}
	public void joinToArmy(Hero h){localArmy.joinHero(h);}

	
	private void readObject (ObjectInputStream in) throws ClassNotFoundException, IOException{
		in.defaultReadObject();
		menuImage = Main.getImage(menuImageS);
		menuIMG = Main.getImage(menuIMGS);
	}
}
