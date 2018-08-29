import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.util.ArrayList;


public class World {
	
	/**
	 * �wiat gry.
	 * Posiada partie oraz niezale�ne budynki.
	 */
	private String name;
	int mapWidth;
	int mapHeight;
	private Party[] parties;
	private RoadMap roadMap;
	private ArrayList <Thing> things;
	
	private int days = 0;
	
	int curParty = 0; //numer partii (w tablicy), kt�ra w�a�nie wykonuje ruch
	int mainParty; //numer partii, kt�r� gra u�ytkownik
	
	City clickedCity;
	Army movingArmy;
	
	//tryb
	//0 - mapa, 1 - zamek, 2 - bitwa
	private byte mode = 0;

	public World(String name, int mapWidth, int mapHeight, RoadMap roadMap, ArrayList<Thing> things, Party[] parties, int mainParty, int days) throws IOException {
		this.name = name;
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
		this.roadMap = roadMap;
		this.things = things;
		this.parties = parties;
		this.mainParty = mainParty;
		
		curParty = mainParty;
		this.days = days;
	}

	public void paint(Graphics g, ImageObserver io, Rectangle r){
		if(mode == 0){
		roadMap.paint(g, io, r);
		for(int i=0; i<things.size(); i++){
			if(things.get(i).getRect().intersects(r)){
			things.get(i).paint(g, io, (int)r.getX(), (int)r.getY());
		}}
		
		for(int i=0; i<parties.length; i++){
			for(City city : parties[i].getCities()){
				if(city.getVisitingArmy().hasHero()){
					city.getVisitingArmy().getBus().paint(g, io, (int)r.getX(), (int)r.getY());
					//g.drawRect(city.getMainBuilding().getEntryX(), city.getMainBuilding().getEntryX(), 32, 32);
				}
			}
		}
		
		//Rysowanie zawarto�ci armii w dole menu
		//Trzeba zrobi� elastyczne jednostki, zale�ne od Main
		if(movingArmy != null){
			movingArmy.paintInfo(g, io, 1230, 555);
		}
		}
		
		else if(mode == 1){
			g.setColor(Color.BLACK);
			clickedCity.paint(g, io, (int)r.getWidth(), (int)r.getHeight());

		}
	}
	
	//Tryb kierowania armi�
	//a = null, je�li armia zostaje odznaczona
	public void moveArmy(Army a){
		if(a == null){
			if(movingArmy != null){
			int xStart = movingArmy.getBus().getX();
			int yStart = movingArmy.getBus().getY();
			roadMap.checkAv(xStart, yStart, false);
			}
		}
		else {
			int xStart = a.getBus().getX();
			int yStart = a.getBus().getY();
			roadMap.checkAv(xStart, yStart, true);
		}
		movingArmy = a;
	}
	
	
	public void mouseClicked(MouseEvent e, Rectangle r){
		
		if(mode == 0){ //MAPA
		if(e.getClickCount()==1){

		}
		else {
			if(getCurParty().getClickedCity(r) != null){
				mode = 1;
				//System.out.println(getCurParty().getClickedCity(r));
				clickedCity = getCurParty().getClickedCity(r);
			}
			else {
				
				//Rozpocz�cie ruchu armii
				ArrayList<Integer> route = roadMap.makeMove((int)r.getX(), (int)r.getY());
				if(route != null){
					movingArmy.getBus().setRoute(route);
					moveArmy(null);
				}
			}
		}
		}
		
		else if(mode == 1){ //MIASTO
			if(clickedCity.getOkButton().isClicked(new Rectangle(e.getX(), e.getY(), 3, 3))){
				//System.out.println("zmiana");
				mode = 0;
			}
			
			for(Barracks b : clickedCity.getBarracksTab()){
				if(b.getOkButton().isClicked(new Rectangle(e.getX(), e.getY(), 3, 3)))
				{
					Soldier newSoldier = b.makeSoldier(getCurParty());
					clickedCity.joinToArmy(newSoldier);
				}
			}
			
			if(clickedCity.getRestaurant().isClicked(new Rectangle(e.getX(), e.getY(), 3, 3))){
				Hero newHero = clickedCity.getRestaurant().buyHero(getCurParty());
				if(newHero != null){
					clickedCity.joinToArmy(newHero);
				}
			}
			
			else {
				clickedCity.moveGroup(new Rectangle(e.getX(), e.getY(), 3, 3));
			}
		}
		//if(!roadMap.makeMove())
		//{
		// p�tla po things i sprawdzanie, co zosta�o klikn	
		//}
	}
	
	public Save createSave(){
		Save s = new Save(name, days, mapWidth, mapHeight, things, roadMap, parties, mainParty);
		return s;
	}
	
	public void setMapDim(int w, int h){mapWidth = w; mapHeight = h;}
	public int getMapWidth(){return mapWidth;}
	public int getMapHeight(){return mapHeight;}
	
	public ArrayList<Thing> getThings(){return things;}
	public RoadMap getRoadMap(){return roadMap;}
	public Party[] getParties(){return parties;}
	public Party getParty(int i){return parties[i];}
	public Party getMainParty(){
		return parties[mainParty];}
	public Party getCurParty(){return parties[curParty];}
	
	public byte getMode(){return mode;}
	
	
	public void newDay(){
		days++;
		curParty++;
		if(curParty == parties.length) curParty = 0;
		}
	
	public int getDaysNum(){return days;}
	public String getDate(){
		String s = "";
		s += "Miesi�c: " + days/28;
		s += " Tydzie�: " + (days%28)/7;
		s += " Dzie�: " + (days%28)%7;
		return s;
	}
}