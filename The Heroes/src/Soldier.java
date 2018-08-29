import java.io.IOException;


public class Soldier extends Thing {
	
	/**
	 * Jednostka
	 * OBRAZEK MUSI MIEÆ 95x100!
	 */
	
	private int life;
	private int power;
	private int ammo;

	public Soldier(byte id, String name, String info, String logo, int life, int power, int ammo) throws IOException {
		super(id, name, info, logo);
		this.life = life;
		this.power = power;
		this.ammo = ammo;
	}
	
	public int getLife(){return life;}
	public int getPower(){return power;}
	public int getAmmo(){return ammo;}

}
