import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.util.ArrayList;


public class Group extends Thing {
	
	/**
	 * Zbiór identycznych jednostek
	 */
	
private int quantity;
private int life;
private int ammo;
private int power;
private Soldier type; //"wzór" jednostki przechowywanej w grupie
private ArrayList<Soldier> soldiers;



	public Group(byte id, String name, String info, String logo, Soldier type) throws IOException {
		super(id, name, info, logo);
		this.type = type;
		soldiers = new ArrayList<Soldier>();
		
		this.setRect(0, 0, 93, 100);
	}
	
	public void paint (Graphics g, ImageObserver io, int x, int y){
		g.drawImage(type.getLogoIMG(), x, y+2, io);
		g.setColor(Color.BLACK);
		g.drawString(""+soldiers.size(), x+2, y+15);
	}

	public int getQuantity(){return quantity;}
	public void setQuantity(int x){quantity = x;}
	public void addQuantity(int x){quantity+=x;}
	
	public int getLife(){return life;}
	public void setLife(int x){life = x;}
	public void addLife(int x){life+=x;}
	
	public int getAmmo(){return ammo;}
	public void setAmmo(int x){ammo = x;}
	public void addAmmo(int x){ammo+=x;}
	
	public int getPower(){return power;}
	public void setPower(int x){power = x;}
	public void addPower(int x){power+=x;}
	
	public Soldier getType(){return type;}
	public void joinToGroup(Soldier s)
	{
		if(s.getName().equals(type.getName()))
		{
			soldiers.add(s);
		}
	}
}
