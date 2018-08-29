import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.Serializable;

public class Barracks extends Thing {

	private Soldier soldier; //wzorzec produkowanych jednostek
	private int capacity; //liczba miejsc dodawanych co tydzieñ
	private int space; //liczba dostêpnych miejsc
	private int price;
	private int sausage;
	
	private Button okButton;
	//private transient String buttonIMG;
	//na razie u¿ywam logo
	
	public Barracks(byte id, String name, String info, String logo, Soldier soldier, int capacity, int price, int sausage) throws IOException
	{
		super(id, name, info, logo);

		this.soldier = soldier;
		this.capacity = capacity;
		this.price = price;
		this.sausage = sausage;
		space = capacity;
		okButton = new Button(getX()+400, getY()+30, 50, 30, getLogo());
	}
	
	public void paint (Graphics g, ImageObserver io, int width, int height)
	{
		g.setColor(Color.WHITE);
		g.drawImage(soldier.getLogoIMG(), 10, getY()+20, io);
		g.drawString(soldier.getName()+"     "+soldier.getInfo(), 110, getY()+20);
		g.drawString("¯ycie: "+soldier.getLife(), 110, getY()+50);
		g.drawString("Si³a: "+soldier.getPower(), 110, getY()+70);
		g.drawString("Amunicja: "+soldier.getAmmo(), 110, getY()+90);
		
		g.drawString("Cena: "+price, 170, getY()+50);
		g.drawString("Kie³basa wyborcza: "+sausage, 170, getY()+70);
		
		try {
			okButton.paint(g, io, 0, 0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void nextWeek()
	{
		space += capacity;
	}
	
	public Soldier makeSoldier(Party party)
	{
		Soldier newSoldier = null;
		try {
			if(space >= 1 && party.getMoney()>price && party.getSausage()>sausage)
			{
				newSoldier = (Soldier) soldier.clone();
				space--;
				party.addMoney(-price);
				party.addSausage(-sausage);
			}
			
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		
		return newSoldier;
	}
	
	public void upgrade(int dCapacity)
	{
		capacity += dCapacity;
	}
	
	public void updateButton()
	{
		okButton.setX(getX()+400);
		okButton.setY(getY()+30);
		//System.out.println("Przycisk: "+ okButton.getY());
		//System.out.println("Barak: "+ getY());
	}
	
	public Button getOkButton(){return okButton;}
}
