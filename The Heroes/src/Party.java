import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;


public class Party extends Thing {

	/**
	 * Partia polityczna
	 * Posiada bohaterów oraz siedziby
	 */
	
	private ArrayList<City> cities;
	private ArrayList<Hero> heroes;
	private ArrayList<Decision> decisions;
	private ArrayList<Decision> usedDecisions = new ArrayList<Decision>();
	private ArrayList<Army> armies;
	
	private int money = 0;
	private int votes = 0;
	private int sausage = 0;
	
	private String bannerS;
	private transient Image banner;
	private boolean isBannerShowed = false;
	private int bannerCounter = 0; //czas wyœwietlania baneru / 100 milisekund
	
	private Bus bus;
	
	public Party (byte id, String name, String info, String logo, ArrayList<Decision> decisions, Bus bus, String bannerS) throws IOException{
		super(id, name, info, logo);
		cities = new ArrayList<City>();
		heroes = new ArrayList<Hero>();
		armies = new ArrayList<Army>();
		this.decisions = decisions; 
		this.bannerS = bannerS;
		this.bus = bus;
		
		//wstêpne iloœci surowców
		addMoney(500);
		addVotes(500);
		addSausage(500);
		
		banner = Main.getImage(bannerS);
	}
	
	public void paint(Graphics g, ImageObserver io, int x0, int y0){
		
		for(int i=0; i<cities.size(); i++){
			g.drawImage(cities.get(i).getMenuIMG(), x0+200, y0+298+i*44, io);
			//System.out.println("Menu city x: "+(x0+200)+" y: "+(y0+298+i*44));
			//System.out.println(cities.get(i).getMenuIMG());
		}
		
		int count = 0;
		
		for(Army army : armies){
			army.paintIcon(g, io, x0+18, y0+298+count*44);
			count++;
		}
		for(City city : cities){
			if(city.getVisitingArmy().hasHero()){
				city.getVisitingArmy().paintIcon(g, io, x0+18, y0+298+count*44);
				count++;
			}
		}
		
		
		//opuszczanie baneru
		if(isBannerShowed){
			bannerCounter++;
			if(bannerCounter == 50) {
				isBannerShowed = false;
				bannerCounter = 0;
			}
		}

	}
	
	public void newDay(){
		showBanner();
		for(Army a : armies){
			a.setRange(5);
		}
		for(City city : cities){

			Army army = city.getVisitingArmy();

			if(army.hasHero()){
				army.setRange(5);
			}
			
		}
	}
	
	public Image getBanner() {
		return banner;
	}
	
	public void showBanner(){
		isBannerShowed = true;
	}
	
	public boolean hasShowedBanner(){
		return isBannerShowed;
	}
	
	public Army getClickedArmy(Rectangle r){
		
		Army a = null;
		
		for(Army army : armies){
			
			if(army.isClicked(r)){
				a = army;
				break;
			}

		}
		for(City city : cities){

			Army army = city.getVisitingArmy();

			if(army.hasHero()){
				if(army.isClicked(r)){
					a = army;
					break;
				}
			}
		}
		
		return a;
	}

	public City getClickedCity(Rectangle r)
	{
		City clicked = null;
		for(City c : cities)
		{
			if(r.intersects(c.getMainBuilding().getRect()))clicked = c;
		}
		return clicked;
	}
	
	public void addCity(City c){cities.add(c);}
	public void addHero(Hero h){heroes.add(h);}
	public void removeCity(City c){cities.remove(c);}
	public void removeHero(Hero h){heroes.remove(h);}
	
	public Decision nextDecision(){
		Decision d = decisions.get(0);
		usedDecisions.add(decisions.get(0));
		//decisions.remove(0);
		return d;
	}
	
	public boolean isDecision()
	{
		if(decisions.size() > 0) return true;
		else return false;
	}
	
	public void addMoney(int i){money += i;}
	public int getMoney(){return money;}
	public void addVotes(int i){votes += i;}
	public int getVotes(){return votes;}
	public void addSausage(int i){sausage += i;}
	public int getSausage(){return sausage;}
	
	public ArrayList<City> getCities(){return cities;}
	public ArrayList<Army> getArmies(){return armies;}

	
	public Bus makeBus(){
		try {
		return (Bus) bus.clone();
	} catch (CloneNotSupportedException e) {
		e.printStackTrace();
		return null;
	}}
	
	private void readObject (ObjectInputStream in) throws ClassNotFoundException, IOException{
		in.defaultReadObject();
		this.setLogo(Main.getImage(getLogo()));
		this.update();
		banner = Main.getImage(bannerS);
	}

}
