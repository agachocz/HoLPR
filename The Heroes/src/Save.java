import java.io.Serializable;
import java.util.ArrayList;


public class Save implements Serializable {
	
	private String name;
	private int day = 0;
	int mapWidth;
	int mapHeight;
	
	private ArrayList<Thing> things;
	private RoadMap roadMap;
	private Party[] parties;
	private int mainParty;
	

	public Save(String name, int day, int mapWidth, int mapHeight, ArrayList<Thing> things, RoadMap roadMap, Party[] parties, int mainParty){
		this.name = name;
		this.day = day;
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
		this.things = things;
		this.roadMap = roadMap;
		this.parties = parties;
		this.mainParty = mainParty;
	}
	
	public String getName(){return name;}
	public void setName(String s){name = s;}
	public int getDay(){return day;}
	public void setMapDim(int w, int h){mapWidth = w; mapHeight = h;}
	public int getMapWidth(){return mapWidth;}
	public int getMapHeight(){return mapHeight;}
	public ArrayList<Thing> getThings(){return things;}
	public RoadMap getRoads(){return roadMap;}
	public Party[] getParties(){return parties;}
	public int getMainParty(){return mainParty;}
	public void setMainParty(int i){mainParty = i;}
	
}
