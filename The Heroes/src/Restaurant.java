import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Restaurant extends Thing {

	private ArrayList<Hero> heroes;
	private Hero[] forSale = new Hero[2];
	private int price;
	
	Random r = new Random();
	
	private Button button1;
	private Button button2;
	private int clickedButton;
	
	
	public Restaurant(int id, String name, String info, String logo) throws IOException {
		super(id, name, info, logo);
		price = 100;
		heroes = new ArrayList<Hero>();
		setX(500);
		setY(0);
		button1 = new Button(getX()+450, getY()+70, 50, 30, getLogo());
		button2 = new Button(getX()+450, getY()+270, 50, 30, getLogo()); 
	}
	
	public void paint(Graphics g, ImageObserver io, int posX, int posY){
		
		g.drawImage(forSale[0].getLogoIMG(), posX+10, posY+10, io);
		g.drawString(forSale[0].getName(), posX+120, posY+35);
		//g.drawString(forSale[0].getInfo(), posX+120, posY+50);
		
		String[] infoTab = forSale[0].getInfoInLines(50);
		for(int i=0; i<infoTab.length; i++){
			g.drawString(infoTab[i], posX+120, posY+70+20*i);
		}
		
		try {
			button1.paint(g, io, 0, 0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		g.drawImage(forSale[1].getLogoIMG(), posX+10, posY+210, io);
		g.drawString(forSale[1].getName(), posX+120, posY+235);
		//g.drawString(forSale[1].getInfo(), posX+120, posY+250);
		infoTab = forSale[1].getInfoInLines(50);
		for(int i=0; i<infoTab.length; i++){
			g.drawString(infoTab[i], posX+120, posY+270+20*i);
		}
		
		
		try {
			button2.paint(g, io, 0, 0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void addHero(Hero hero)
	{
		heroes.add(hero);
	}
	
	public Hero buyHero(Party party) //0 - pierwszy, 1 - drugi
	{
		Hero newHero = null;
		
		if(party.getMoney() >= price)
		{
			newHero = forSale[clickedButton];
			random(clickedButton);
		}
		return newHero;
	}
	
	public void random(int i)
	{
		int rand = r.nextInt(heroes.size());
		forSale[i] = heroes.get(rand);
		
		//WRZUCIÆ Z POWROTEM JAK BÊDZIE DU¯O BOHATERÓW!!!
		//heroes.remove(rand);
	}
	
	public boolean isClicked(Rectangle r)
	{
		boolean b = false;
		if(button1.isClicked(r)) {b = true; clickedButton = 0;}
		else if(button2.isClicked(r)){b = true; clickedButton = 1;}
		else {b = false;}
		
		return b;
	}

}
