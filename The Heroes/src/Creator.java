import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.*;


public class Creator extends JFrame implements KeyListener, Serializable, MouseListener, MouseMotionListener {

	Main main; //Klasa g³ówna, tworz¹ca kreator
	int width; //szerokoœæ panelu
	int height; //wysokoœæ panelu
	Container c = getContentPane();
	
	String bgImageS = "Graphics//Things//trawa.png"; //obrazek t³a
	Image bgImage;
	
	Image bufor; //Bufor grafiki
	Graphics bg; //Obiekt graficzny
	int x, y; //Aktualna pozycja na mapie (wyœwietlana w lewym górnym rogu ekranu)
	int xStep = 0; //Przesuniêcie w osi x
	int yStep = 0; //Przesuniêcie w osi y
	int X = 0;
	int Y = 0;
	boolean isFree = true; //Okreœla, czy u¿ytkownik najecha³ myszk¹ na jakiœ obiekt (false) czy nie (true)
	Rectangle r; //pomocniczy obszar okreœlaj¹cy lokalizacjê kursora
	Rectangle scene; //Obszar wyœwietlany aktualnie
	
	Timer tm; 
	ActionListener lst = new Listener();
	
	Map map; //Mapa
	int mapWidth; //szerokoœæ mapy
	int mapHeight; //wysokoœæ mapy
	int mLeft = 0;
	int mRight = 0;
	int mTop = 0;
	int mDown = 0;
	Rectangle mapRect;
	
	Info info; //Panel informacyjny
	//Tools tools; //Elementy do umieszczenia na mapie
	
	
	//DROGI
	String[] roadsImg = new String[11];
	{
		for (int i=0; i<roadsImg.length; i++){
			roadsImg[i] = "Graphics//Things//Roads//"+i+".png";
		}
	}
	RoadMap roadMap;
	boolean roadmode = false; //True, kiedy s¹ edytowane drogi
	JCheckBox roadsCB; //checkBox do w³¹czania i wy³¹czania trybu edytowania drogi
	
	int[] mainBuildID = new int[3]; //id budynków bêd¹cych siedzibami danej partii
	ArrayList<Building> buildings = new ArrayList<Building>();
	{try {
		buildings.add(new Building(Main.ID++, "Ciemnogród", "Siedziba PIS", "Graphics//Things//Buildings//ciemnogrod.png", 0, 0, 192, 192));
		mainBuildID[0] = 0;
		buildings.add(new Building(Main.ID++, "PGR", "Siedziba PSL", "Graphics//Things//Buildings//PGR.png", 0, 0, 192, 192));
		buildings.add(new Building(Main.ID++, "Korpo", "Siedziba Nowoczesnej", "Graphics//Things//Buildings//korpo.png", 0, 0, 192, 192));
		mainBuildID[2] = 2;
		buildings.add(new Building(Main.ID++, "Gazoci¹g", "Gazoci¹g z Rosji", "Graphics//Things//Buildings//gazociag.png", 0, 0, 96, 128));
		buildings.add(new Building(Main.ID++, "Chata", "Siedlisko tradycyjnej, polskiej rodziny", "Graphics//Things//Buildings//chata.png", 0, 0, 96, 96));
		buildings.add(new Building(Main.ID++, "Pa³ac S³oñca Peru", "Siedziba PO", "Graphics//Things//Buildings//platforma.png", 0, 0, 192, 192));
		mainBuildID[1] = 5;
		buildings.add(new Building(Main.ID++, "Cyrk", "Siedziba Sejmu", "Graphics//Things//Buildings//cyrk.png", 0, 0, 96, 96));
	} catch (IOException e) {
		e.printStackTrace();
	}}
	
	JComboBox<String> buildingsCB = new JComboBox<String>();{
		for(int i=0; i<buildings.size(); i++){
			buildingsCB.addItem(buildings.get(i).getName());
		}
	}
	
	String[] citiesMenuIMG = new String[4];{
		citiesMenuIMG[0] = "Graphics//Things//Buildings//PiScityMenu.png";
		citiesMenuIMG[1] = "Graphics//Things//Buildings//POcityMenu.png";
		citiesMenuIMG[2] = "Graphics//Things//Buildings//NcityMenu.png";
		citiesMenuIMG[3] = "Graphics//Things//Buildings//PSLcityMenu.png";
	}
	
	Building curBuild; //Aktualnie wybrany budynek
	Building umBuild; //Budynek, na który najechano kursorem
	
	//OBIEKTY
	ArrayList<Thing> things = new ArrayList<Thing>(); //Lista wzorców dla elementów
	{try {
		//Droga jest zawsze pierwszym elementem
		things.add(new Road(Main.ID++, "droga", "fragment kamiennej drogi", "Graphics//Things//Roads//logo.png", roadsImg));
		
		things.add(new Thing(Main.ID++, "wzgórze1", "Zielone wzgórze", "Graphics//Things//Mountains//wzgórzeZielone.png", 0, 0, 64, 32));
		things.add(new Thing(Main.ID++, "wzgórze2", "Br¹zowe wzgórze", "Graphics//Things//Mountains//wzgórzeBr¹zowe.png", 0, 0, 64, 32));
		things.add(new Thing(Main.ID++, "góra1", "Góra", "Graphics//Things//Mountains//góraPrawo.png", 0, 0, 192, 128));
		things.add(new Thing(Main.ID++, "góra2", "Góra", "Graphics//Things//Mountains//góraLewo.png", 0, 0, 192, 160));
		things.add(new Thing(Main.ID++, "jezioro", "jezioro", "Graphics//Things//jezioro.png", 0, 0, 160, 96));
		
	} catch (IOException e) {
		e.printStackTrace();
	}}
	
	ArrayList<Thing> nt = new ArrayList<Thing>(); //Lista dodanych do œwiata gry elementów; nt = new things
	
	
	
	Thing curThing; //Aktualnie "u¿ywana" rzecz
	Thing umThing; //Obiekt, na który najechano kursorem
	
	JButton doneB = new JButton("GOTOWE");
	JComboBox<String> cb = new JComboBox<String>();
	{
	for (int i=1; i<things.size(); i++){cb.addItem(things.get(i).getName());}
	}
	
	//WIADOMOŒCI I DECYZJE
	Authority[] authorities = new Authority[1];{
		authorities[0] = new Authority("Seweryn Blumsztajn", "Dziennikarz", "Graphics//Authorities//S_Blumsztajn.png");
	}
	
	String[] decTexts = new String[1];{
		decTexts[0] = "Celem programu 500+ jest zapewnienie wsparcia rodzinom ubogim i wielodzietnym, zapobie¿enie zapaœci demograficznej "+
				"oraz napêdzenie konsumpcji. Œwiadczenia s¹ udzielane w wysokoœci 500 z³ miesiêcznie na drugie i ka¿de kolejne dziecko "+
				"oraz na ka¿de dziecko w rodzinach najubo¿szych. Program bêdzie kosztowny, ale przysporzy Twojej partii popularnoœci. "+
				"Czy chcesz go przyj¹æ?";
	}
	
	ArrayList<Decision> decisions = new ArrayList<Decision>();{
		decisions.add(new Decision("500+", decTexts[0], authorities[0], "Pierdolê, nie rodzê",
				new int[]{0, 10, 50}, new int[]{100, 3, 0}));
		decisions.add(new Decision("500+", "treœæ", authorities[0], "Pierdolê, nie rodzê",
				new int[]{0, 0, 0}, new int[]{0, 0, 0}));
		decisions.add(new Decision("500+", "treœæ", authorities[0], "Pierdolê, nie rodzê",
				new int[]{0, 0, 0}, new int[]{0, 0, 0}));
		decisions.add(new Decision("500+", "treœæ", authorities[0], "Pierdolê, nie rodzê",
				new int[]{0, 0, 0}, new int[]{0, 0, 0}));
	}
	
	
	
	//ARMIA
	//BUSY
	String[] PiSBus = new String[4];{
		PiSBus[0] =	"Graphics//Things//Buses//PiS//up.png";
		PiSBus[1] =	"Graphics//Things//Buses//PiS//right.png";
		PiSBus[2] =	"Graphics//Things//Buses//PiS//down.png";
		PiSBus[3] =	"Graphics//Things//Buses//PiS//left.png";
	}
	String[] POBus = new String[4];{
		POBus[0] =	"Graphics//Things//Buses//PO//up.png";
		POBus[1] =	"Graphics//Things//Buses//PO//right.png";
		POBus[2] =	"Graphics//Things//Buses//PO//down.png";
		POBus[3] =	"Graphics//Things//Buses//PO//left.png";
	}
	String[] NowBus = new String[4];{
		NowBus[0] =	"Graphics//Things//Buses//Nowoczesna//up.png";
		NowBus[1] =	"Graphics//Things//Buses//Nowoczesna//right.png";
		NowBus[2] =	"Graphics//Things//Buses//Nowoczesna//down.png";
		NowBus[3] =	"Graphics//Things//Buses//Nowoczesna//left.png";
	}
	Bus[] buses = new Bus[3];{
		buses[0] = new Bus( (byte)Main.ID++, "Pisobus", "Bus kadry pisowskiej", PiSBus[2], PiSBus);
		buses[1] = new Bus( (byte)Main.ID++, "Peobus", "Bus kadry platformy", POBus[2], POBus);
		buses[2] = new Bus( (byte)Main.ID++, "Nowoczesny Bus", "Bus kadry nowoczesnej", NowBus[2], NowBus);
		
	}
	//JEDNOSTKI
	//PiS
	Soldier biskup = new Soldier((byte) Main.ID++, "BISKUP", "Gotowy do poœwiêceñ", "Graphics//Things//Parties//Soldiers//biskup.png", 100, 50, 10);
	Soldier moher = new Soldier((byte) Main.ID++, "MOHER", "Moherowe berety pasuj¹ na czerepy", "Graphics//Things//Parties//Soldiers//moher.png", 20, 30, 5);
	Soldier janusz = new Soldier((byte) Main.ID++, "JANUSZ", "Mistrz w rzucaniu miêsem", "Graphics//Things//Parties//Soldiers//janusz.png", 70, 70, 0);
	//PO
	Soldier leming = new Soldier((byte) Main.ID++, "LEMING", "M³ody, wykszta³cony, z wielkiego oœrodka", "Graphics//Things//Parties//Soldiers//leming.png", 100, 50, 10);
	Soldier resDziecko = new Soldier((byte) Main.ID++, "RESORTOWE DZIECKO", "Wszystko pozostaje w rodzinie", "Graphics//Things//Parties//Soldiers//resDziecko.png", 20, 30, 5);
	Soldier autorytet = new Soldier((byte) Main.ID++, "DY¯URNY AUTORYTET", "...i ju¿ wiesz, co myœleæ", "Graphics//Things//Parties//Soldiers//autorytet.png", 70, 70, 0);
	
	//Nowoczesna
	
	//KOSZARY
	Barracks[][] barrTab = new Barracks[3][3];
	{
		barrTab[0][0] = new Barracks((byte) Main.ID++, "Nazwa", "Opis", "Graphics//Menu//OK.png", moher, 10, 15, 20);
		barrTab[0][1] = new Barracks((byte) Main.ID++, "Nazwa", "Opis", "Graphics//Menu//OK.png", janusz, 7, 50, 30);
		barrTab[0][2] = new Barracks((byte) Main.ID++, "Nazwa", "Opis", "Graphics//Menu//OK.png", biskup, 3, 200, 50);
		
		barrTab[1][0] = new Barracks((byte) Main.ID++, "Nazwa", "Opis", "Graphics//Menu//OK.png", leming, 10, 15, 20);
		barrTab[1][1] = new Barracks((byte) Main.ID++, "Nazwa", "Opis", "Graphics//Menu//OK.png", resDziecko, 7, 50, 30);
		barrTab[1][2] = new Barracks((byte) Main.ID++, "Nazwa", "Opis", "Graphics//Menu//OK.png", autorytet, 3, 200, 50);
		
		//Trzecia partia do uzupe³nienia
		barrTab[2][0] = new Barracks((byte) Main.ID++, "Nazwa", "Opis", "Graphics//Menu//OK.png", leming, 10, 15, 20);
		barrTab[2][1] = new Barracks((byte) Main.ID++, "Nazwa", "Opis", "Graphics//Menu//OK.png", resDziecko, 7, 50, 30);
		barrTab[2][2] = new Barracks((byte) Main.ID++, "Nazwa", "Opis", "Graphics//Menu//OK.png", autorytet, 3, 200, 50);
	}
	
	//PARTIE POLITYCZNE
	Party[] parties = new Party[3];{
		parties[0] = new Party( (byte)Main.ID++, "PIS", "Prawo i Sprawiedliwoœæ", "Graphics//Things//Parties//logo//PiS.png",
				decisions, buses[0], "Graphics//Things//Parties//logo//przejœciePiS.jpg");
		parties[1] = new Party( (byte)Main.ID++, "PO", "Platforma Obywatelska", "Graphics//Things//Parties//logo//PO.png",
				decisions, buses[1], "Graphics//Things//Parties//logo//przejœciePO.jpg");
		parties[2] = new Party( (byte)Main.ID++, "Nowoczesna", "Partia Ryszarda Petru", "Graphics//Things//Parties//logo//N.png",
				decisions, buses[2], "Graphics//Things//Parties//logo//przejœciePiS.jpg");
	}
	int mainParty = 0;
	
	//BOHATEROWIE
	Hero[][] heroes = new Hero[3][3];
	{
		heroes[0][0] = new Hero((byte) Main.ID++, "ANTONIO", "Byæ mo¿e czasem b³¹dzi jak dziecko we mgle, ale i tak was wszystkich zwyciê¿y. I wywiezie na teczkach.",
				"Graphics//Things//Parties//Heroes//antonio.png", "Graphics//Things//Parties//Heroes//antonioMenu.png");
		heroes[0][1] = new Hero((byte) Main.ID++, "ANTONIO", "Byæ mo¿e czasem b³¹dzi jak dziecko we mgle, ale i tak was wszystkich zwyciê¿y. I wywiezie na teczkach.",
				"Graphics//Things//Parties//Heroes//antonio.png", "Graphics//Things//Parties//Heroes//antonioMenu.png");
		heroes[0][2] = new Hero((byte) Main.ID++, "ANTONIO", "Byæ mo¿e czasem b³¹dzi jak dziecko we mgle, ale i tak was wszystkich zwyciê¿y. I wywiezie na teczkach.",
				"Graphics//Things//Parties//Heroes//antonio.png", "Graphics//Things//Parties//Heroes//antonioMenu.png");
		
		/*
		heroes[1][0] = new Hero((byte) Main.ID++, "KRUL", "Niech strze¿e siê lewactwo! Niewidzialna rêka rynku wznios³a swoj¹ piêœæ.",
				"Graphics//Things//Parties//Heroes//krul.png");
		heroes[1][1] = new Hero((byte) Main.ID++, "KRUL", "Niech strze¿e siê lewactwo! Niewidzialna rêka rynku wznios³a swoj¹ piêœæ.",
				"Graphics//Things//Parties//Heroes//krul.png");
		heroes[1][2] = new Hero((byte) Main.ID++, "KRUL", "Niech strze¿e siê lewactwo! Niewidzialna rêka rynku wznios³a swoj¹ piêœæ.",
				"Graphics//Things//Parties//Heroes//krul.png");
		*/
		
		heroes[1][0] = new Hero((byte) Main.ID++, "GAJOWY", "Zawsze staje na wysokoœci… krzes³a. Podró¿uje z Szogunem i czasem wpada w kimono, ale jednym s³owem potrafi wci¹gn¹æ was do krainy bulu i nadzieji na szybki koniec.",
				"Graphics//Things//Parties//Heroes//gajowy.png", "Graphics//Things//Parties//Heroes//gajowyMenu.png");
		heroes[1][1] = new Hero((byte) Main.ID++, "GAJOWY", "Zawsze staje na wysokoœci… krzes³a. Podró¿uje z Szogunem i czasem wpada w kimono, ale jednym s³owem potrafi wci¹gn¹æ was do krainy bulu i nadzieji na szybki koniec.",
				"Graphics//Things//Parties//Heroes//gajowy.png", "Graphics//Things//Parties//Heroes//gajowyMenu.png");
		heroes[1][2] = new Hero((byte) Main.ID++, "GAJOWY", "Zawsze staje na wysokoœci… krzes³a. Podró¿uje z Szogunem i czasem wpada w kimono, ale jednym s³owem potrafi wci¹gn¹æ was do krainy bulu i nadzieji na szybki koniec.",
				"Graphics//Things//Parties//Heroes//gajowy.png", "Graphics//Things//Parties//Heroes//gajowyMenu.png");
		
		heroes[2][0] = new Hero((byte) Main.ID++, "KOBIETA W CZERWIENI", "Niez³omna bojowniczka z dyktatur¹ kobiet, która wyleje na was kube³ zimnej g³owy",
				"Graphics//Things//Parties//Heroes//melisandre.png", "Graphics//Things//Parties//Heroes//melisandreMenu.png");
		heroes[2][1] = new Hero((byte) Main.ID++, "KOBIETA W CZERWIENI", "Niez³omna bojowniczka z dyktatur¹ kobiet, która wyleje na was kube³ zimnej g³owy",
				"Graphics//Things//Parties//Heroes//melisandre.png", "Graphics//Things//Parties//Heroes//melisandreMenu.png");
		heroes[2][2] = new Hero((byte) Main.ID++, "KOBIETA W CZERWIENI", "Niez³omna bojowniczka z dyktatur¹ kobiet, która wyleje na was kube³ zimnej g³owy",
				"Graphics//Things//Parties//Heroes//melisandre.png", "Graphics//Things//Parties//Heroes//melisandreMenu.png");
	}
	
	Restaurant[] restaurants = new Restaurant[3];
	{
		for(int i=0; i<restaurants.length; i++)
		{
			restaurants[i] = new Restaurant((byte)Main.ID++, "Restauracja sejmowa", "Opis", "Graphics//Menu//OK.png");
			for(int j=0; j<heroes[i].length; j++){
				restaurants[i].addHero(heroes[i][j]);
			}
			restaurants[i].random(0);
			restaurants[i].random(1);
		}
	}
	
	
	
	//Konstruktor kreatora
	public Creator() throws IOException{ 
		
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		
		width = (int)Main.tk.getScreenSize().getWidth();
		height = (int)Main.tk.getScreenSize().getHeight();
		mapHeight = mapWidth = (int)Main.tk.getScreenSize().getWidth();
		//mapHeight = (int)Main.tk.getScreenSize().getHeight();
		//mapRect = new Rectangle(0, 0, WIDTH, int mapHeight;);
		x = 0;
		y = 0;
		
		tm = new Timer(50, lst); //Timer. Odœwie¿a grê co 100 milisekund
		cb.addActionListener(lst);
		buildingsCB.addActionListener(lst);
		doneB.addActionListener(lst); //Dodaje s³uchacz do przycisku
		
		setLocation(0, 0);
		setSize(width, height);
		setName("Kreator mapy");
		GridBagLayout gbl = new GridBagLayout();
		c.setLayout(gbl);
		
		bgImage = Main.getImage(bgImageS);
		
		map = new Map();
		info = new Info();
		
		GridBagConstraints cons = new GridBagConstraints();
		
		cons.fill = GridBagConstraints.BOTH;
		cons.weightx = 90;
		cons.weighty = 80;
		cons.gridx = 0;
		cons.gridy = 0;
		cons.gridwidth = 3;
		cons.gridheight = 1;
		c.add(map, cons);
		
		cons.weightx = 10;
		cons.weighty = 20;
		cons.gridx = 3;
		cons.gridy = 0;
		cons.gridwidth = 1;
		cons.gridheight = 1;
		c.add(info, cons);
		
		roadMap = new RoadMap(mapWidth, mapHeight, 32);
		scene = new Rectangle(x, y, width, height);
		r = new Rectangle(x, y, 1, 1);
		
		tm.start();	
	}
	
	public void update(){ //Przesuwa scenê i odmalowuje
		
		if(this.isActive()){
		x+=xStep;
		y+=yStep;
		X+=xStep;
		if(X>=256 || X<=-256){X=0;}
		Y+=yStep;
		if(Y>=256 || Y<=-256){Y=0;}
		if(curThing != null){
			curThing.addX(xStep);
			curThing.addY(yStep);
		}
		info.update();
		repaint();
		map.repaint();
	}}
	
	public void save(Save save) throws FileNotFoundException, IOException{
		
	    setVisible(false);
	    ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream ("Saves//"+save.getName()+".dat"));
  		out.writeObject(save);
  		out.close();
  		System.exit(0);
}
	
	
	private class Map extends JPanel { //Mapa gry
		
		public Map(){
	
		}

		
		public void paint(Graphics g){ //Funkcja maluj¹ca wszystkie elementy na mapie
		
		if(bufor==null){
			bufor=createImage(width, height);
		    bg=bufor.getGraphics();
		}
		
		scene.setBounds(x, y, width, height);
		//Prostok¹t okreœlaj¹cy obszar, który wyœwietla siê na ekranie
		
		//PRZESUWANIE SCENY (POD£O¯A)
		if(x<0) mLeft = -x;
		else mLeft = 0;
		
		if(y<0) mTop = -y;
		else mTop = 0;
		
		if(x+width > mapWidth) {mRight = x+width - mapWidth;}
		else mRight = 0;
		
		if(y+height > mapHeight) {mDown = y+height - mapHeight;}
		else mDown = 0;
		
		for(int i=-1; i<(width/256)+1; i++){
			for (int j=-1; j<(height/256)+1; j++){
				g.drawImage(bgImage, i*256 - X, j*256 - Y, this);
			}		
		}
		
		for(int i=-1; i<(width/32)+1; i++){
			for (int j=-1; j<(height/32)+1; j++){
				g.drawRect(i*32 - X, j*32 - Y, 32, 32);
			}		
		}
		
		g.fillRect(0, 0, mLeft, height);
		g.fillRect(width-mRight, 0, mRight, height);
		g.fillRect(0, 0, width, mTop );
		g.fillRect(0, height-mDown, width, mDown);
		
		for(int i=0; i<nt.size(); i++){

			if(nt.get(i).getRect().intersects(scene)){//je¿eli ten obiekt mieœci siê w wyœwietlanym obszarze
				nt.get(i).paint(g, this, x, y); //maluje ten obiekt
			}
		}
		
		roadMap.paint(g, this, scene);
		
		if(!roadmode && curThing != null){curThing.paint(g, this, x, y);}
		if(roadmode && curBuild != null){
			curBuild.paint(g, this, x, y);}
		
		//g.setColor(Color.RED);
		//g.fillRect((int)r.getX()-x, (int)r.getY()-y, (int)r.getWidth(), (int)r.getHeight());
		
		g.setColor(Color.RED);
		g.drawRect((int)r.getX()+x, (int)r.getY()+y, (int)r.getWidth(), (int)r.getHeight());
		
		}
		
	}
	
	private class Info extends JPanel{
		
		JLabel name = new JLabel("Nazwa");
		JLabel info = new JLabel("Opis");
		
		public Info(){
					
			setLayout(new GridBagLayout());
			GridBagConstraints cons = new GridBagConstraints();
			
			roadsCB = new JCheckBox("Tryb edycji dróg");
			roadsCB.addActionListener(lst);
			//buildingsCB.addActionListener(lst);
			buildingsCB.setEnabled(false);
			
			cons.fill = GridBagConstraints.NONE;
			cons.weightx = 100;
			cons.weighty = 100;
			
			cons.gridx = 0; cons.gridwidth = 1; cons.gridy = 0; cons.gridheight = 1; add(roadsCB, cons);
			cons.gridx = 0; cons.gridwidth = 1; cons.gridy = 1; cons.gridheight = 1; add(buildingsCB, cons);
			cons.gridx = 0; cons.gridwidth = 1; cons.gridy = 2; cons.gridheight = 1; add(cb, cons);
			cons.gridx = 0; cons.gridwidth = 1; cons.gridy = 3; cons.gridheight = 1; add(name, cons);
			cons.gridx = 0; cons.gridwidth = 1; cons.gridy = 4; cons.gridheight = 3; add(info, cons);
			cons.gridx = 0; cons.gridwidth = 1; cons.gridy = 7; cons.gridheight = 1; add(doneB, cons);
		}
		
		public void update(){
			
			if(curThing != null){
				name.setText(curThing.getName());
				name.repaint();
				info.setText(curThing.getInfo());
				info.repaint();
			}
			
			else {
				name.setText("Nazwa");
				name.repaint();
				info.setText("Opis");
				info.repaint();
			}
			
			this.repaint();
		}
	}
	

	private class Listener implements ActionListener{ //S³uchacz do listy Combo i odœwie¿ania mapy

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(e.getSource() == tm){update();}
			
			if(e.getSource()==cb){
				for(int i=1; i<things.size(); i++){
					if(cb.getSelectedIndex() == i-1){
						curThing = things.get(i);
					}
				}
			}
			
			if(e.getSource() == roadsCB){
				if(roadsCB.isSelected()){
					roadmode = true;
					curThing = things.get(0); 
					cb.setEnabled(false);
					buildingsCB.setEnabled(true);
				}
				else {
					roadmode = false;
					cb.setEnabled(true);
					buildingsCB.setEnabled(false);
					curThing = null;
				}
			}
			
			if(e.getSource() == buildingsCB){System.out.println("wybrano " + buildingsCB.getSelectedIndex());
				for(int i=0; i<buildings.size(); i++){
					if(buildingsCB.getSelectedIndex() == i){
						curBuild = buildings.get(i);
						
					}
				}
			}
			
			if(e.getSource() == doneB){
				for(int i=0; i<parties.length; i++){
					Building b = buildings.get(mainBuildID[i]);

					for(Building bd : roadMap.getBuildings()){
						if(bd.getName() == b.getName())
						{
							b = bd;
						}
						//System.out.println(b.getName() + "  " + i);
						//System.out.println(bd.getName());
						//System.out.println(b.getX());
					}

					try {
						City c = new City(Main.ID++, b.getName(), b.getInfo(), b.getLogo(), b, barrTab[i], restaurants[i], parties[i], citiesMenuIMG[i]);
						parties[i].addCity(c);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
				}
				Save save = new Save("default", 0, mapWidth, mapHeight, nt, roadMap, parties, mainParty);
				try {
					save(save);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
		}	
	}


	@Override
	public void mouseDragged(MouseEvent e) {}

	@Override
	public void mouseMoved(MouseEvent e) {
		
		
		//Jeœli kursor zjedzie do brzegu ekranu, scena siê przesuwa
		if(e.getX()<30){xStep=-10;}
		else if(e.getX()>width-30){xStep=10;}
		else {xStep=0;}
		
		if(e.getY()<50){yStep=-10;}
		else if(e.getY()>height-50){yStep=10;}
		else {yStep=0;}
		
		//Jeœli jest wybrana jakaœ rzecz, przesuwa siê wraz z kursorem
		if(curThing != null){
			curThing.setX((Math.round ((e.getX()+x-10)/32)) *32);
			curThing.setY((Math.round ((e.getY()+y-25)/32)) *32);
		}
		
		if(roadmode && curBuild != null){
			curBuild.setX((Math.round ((e.getX()+x-10)/32)) *32);
			curBuild.setY((Math.round ((e.getY()+y-25)/32)) *32);
		}
		
		update();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		//Sprawdzam, czy kursor najecha³ na obiekt
		isFree = true;
		umThing = null;
		r.setBounds(e.getX()+x-10, e.getY()+y-25, 10, 10);
		//r.setBounds(e.getX(), e.getY(), 10, 10);
		
		if(roadmode){
				if(roadMap.select(r))isFree = false;
				}
		else{
		for(int i=0; i<nt.size(); i++){
			
			if(nt.get(i).getRect().intersects(r)){
				isFree = false;
				umThing = nt.get(i);
				break;
			}
		}}
		
		if(isFree){ //Jeœli kursor znajduje siê nad pustym ekranem
		  if(curThing != null){ //Umieszczenie obiektu na mapie
			
			  if(e.getButton()==MouseEvent.BUTTON1){ //Lewy przycisk myszy
				  
				  if(roadmode){ //jeœli w³¹czony jest tryb edycji dróg
					  
					  if(curBuild != null){ // ...i jest wybrany budynek
						  try {
						  		roadMap.addBuilding((Building) curBuild.clone(), (Road)things.get(0).clone());
						  		//System.out.println("rozmiar listy: "+nt.size());
						  		//System.out.println("Id elementu: "+nt.get(nt.size()-1).getId());
						  		//System.out.println("Nazwa elementu: "+curThing.getName());
					
						  	} catch (CloneNotSupportedException e1) {
						  		e1.printStackTrace();
						  	}
						  	curBuild = null;
					  }
					  else { //...i nie ma wybranego budynku
					  try {
						  Road road = (Road) curThing.clone();
						  road.setId((byte) Main.ID++);
						  roadMap.add(road);
					  } catch (CloneNotSupportedException e1) {
						e1.printStackTrace();
					}
					}
				  }
				  else{
				  //jeœli w³¹czony jest tryb normalny
					  	try {
					  		nt.add(curThing.clone());
					  		nt.get(nt.size()-1).setId((byte) Main.ID++);
					  		//System.out.println("rozmiar listy: "+nt.size());
					  		//System.out.println("Id elementu: "+nt.get(nt.size()-1).getId());
					  		//System.out.println("Nazwa elementu: "+curThing.getName());
				
					  	} catch (CloneNotSupportedException e1) {
					  		e1.printStackTrace();
					  	}
					  	curThing = null;
				  }
			  }
			  else if(e.getButton()==MouseEvent.BUTTON3){
				if(!roadmode)curThing = null;
				else if(curBuild != null) curBuild = null;
					
			  }		  
			  
		}
		  	
		
		}
		else { //Jeœli kursor najecha³ na jakiœ obiekt
			if(e.getButton()==MouseEvent.BUTTON1 && !roadmode){
				
				
				//Usuwam obiekt z listy nt i przek³adam go do zmiennej curThing
				curThing = umThing;
				for(int i=0; i<nt.size(); i++){
					if(nt.get(i).getId()==curThing.getId()){
						nt.remove(i);
						break;
					}
				}
			}
			else if(e.getButton()==MouseEvent.BUTTON3){
				if(roadmode){
					if(curBuild != null) curBuild = null;
					else roadMap.removeSelected();
				}
				else {
					curThing = null;
				}
			  }
		}
		//System.out.println(isFree);
		//System.out.println(e.getButton());
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}
}