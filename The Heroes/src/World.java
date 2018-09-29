import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.util.ArrayList;


public class World {
	
	/**
	 * Œwiat gry.
	 * Posiada partie oraz niezale¿ne budynki.
	 */
	private String name;
	int mapWidth;
	int mapHeight;
	private Party[] parties;
	private RoadMap roadMap;
	private ArrayList <Thing> things;
	
	private int days = 0;
	
	int curParty = 0; //numer partii (w tablicy), która w³aœnie wykonuje ruch
	int mainParty; //numer partii, któr¹ gra u¿ytkownik
	
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

	//r - obszar mapy wyœwietlany na ekranie
	public void paint(Graphics g, ImageObserver io, Rectangle r){
		if(mode == 0){ //tryb mapy
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
		
		//Rysowanie zawartoœci armii w dole menu
		//Trzeba zrobiæ elastyczne jednostki, zale¿ne od Main
		if(movingArmy != null){
			movingArmy.paintInfo(g, io, 1230, 555);
		}
		
		//wyœwietlanie baneru po przejœciu do tury kolejnej partii
		if(parties[curParty].hasShowedBanner()){
			Image banner = parties[curParty].getBanner();
			g.drawImage(banner, (int)r.getWidth()/2-banner.getWidth(io)/2, (int)r.getHeight()/2-banner.getHeight(io)/2, io);
		}
		}
		
		else if(mode == 1){ //tryb menu
			g.setColor(Color.BLACK);
			clickedCity.paint(g, io, (int)r.getWidth()-270, (int)r.getHeight());

		}
	}
	
	//Tryb kierowania armi¹ - zaznacza dostêpne pola
	//a = null, jeœli armia zostaje odznaczona
	public void moveArmy(Army a, int prevRange){
		if(a == null){
			if(movingArmy != null){
			int xStart = movingArmy.getBus().getX();
			int yStart = movingArmy.getBus().getY();
			roadMap.checkAv(xStart, yStart, false, prevRange);
			}
		}
		else {
			int xStart = a.getBus().getX();
			int yStart = a.getBus().getY();
			roadMap.checkAv(xStart, yStart, true, prevRange); 
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
				
				//Rozpoczêcie ruchu armii
				ArrayList<Integer> route = roadMap.makeMove((int)r.getX(), (int)r.getY());
				if(route != null){
					movingArmy.getBus().setRoute(route);
					int prevRange = movingArmy.getRange();
					movingArmy.setRange(prevRange - route.size());
					System.out.println("Range:" + movingArmy.getRange());
					//Zaprzestanie wyœwietlania dostêpnej trasy
					moveArmy(null, prevRange);

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
		// pêtla po things i sprawdzanie, co zosta³o klikn	
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
		parties[curParty].newDay();
		}
	
	public int getDaysNum(){return days;}
	public String getDate(){
		String s = "";
		s += "Miesi¹c: " + days/28;
		s += " Tydzieñ: " + (days%28)/7;
		s += " Dzieñ: " + (days%28)%7;
		return s;
	}
}
