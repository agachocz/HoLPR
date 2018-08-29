import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.io.Serializable;

import javax.swing.*;


public class Scene extends JPanel implements Serializable, MouseListener, MouseMotionListener{

	Main main;
	private World world;
	private Micromap micromap;
	GameMenu gameMenu;
	
	int width; //szeroko�� panelu
	int height; //wysoko�� panelu
	Image bufor; //Bufor grafiki
	Graphics bg; //Obiekt graficzny
	int x, y; //Aktualna pozycja na mapie (wy�wietlana w lewym g�rnym rogu ekranu)
	int xStep = 0; //Przesuni�cie w osi x
	int yStep = 0; //Przesuni�cie w osi y
	int X = 0;
	int Y = 0;
	int mapWidth; //szeroko�� mapy
	int mapHeight; //wysoko�� mapy
	int mLeft = 0;
	int mRight = 0;
	int mTop = 0;
	int mDown = 0;
	boolean isFree = true; //Okre�la, czy u�ytkownik najecha� myszk� na jaki� obiekt (false) czy nie (true)
	Rectangle r; //pomocniczy obszar okre�laj�cy lokalizacj� kursora
	Rectangle scene; //Obszar wy�wietlany aktualnie
	String bgImageS = "Graphics//Things//trawa.png"; //obrazek t�a
	String menuImageS = "Graphics//Menu//menu.png"; //obrazek menu
	Image bgImage;
	
	Timer tm; 
	ActionListener lst = new Listener();
	
	//Zmienne okre�laj�ce, co jest aktualnie wy�wietlane
	boolean isMap = true; //mapa
	boolean isCity = false; //miasto/budynek
	
	
	public Scene(Main main, World world){
		
		addMouseListener(this);
		addMouseMotionListener(this);
		
		
		tm = new Timer(50, lst); //Timer. Od�wie�a gr� co 100 milisekund
		
		this.main = main;
		this.world = world;
		//width = (int)Main.tk.getScreenSize().getWidth();
		//height = (int)Main.tk.getScreenSize().getHeight();
		//Ustalam sta�� wysoko�� i szeroko��
		this.width = main.width;
		this.height = main.height;
		
		setSize(width, height);
		mapWidth = world.getMapWidth();
		mapHeight = world.getMapHeight();
		x = 0;
		y = 0;
		scene = new Rectangle(x, y, width, height);
		try {
			bgImage = Main.getImage(bgImageS);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Zak�adam, �e mapa jest kwadratowa
		micromap = new Micromap(world, mapWidth/200);
		gameMenu = new GameMenu(width-270, 0, 270, height, micromap, menuImageS);
		
		tm.start();
	}
	
	public void paint(Graphics g){
		
		if(bufor==null){
			bufor=createImage(width, height);
		    bg=bufor.getGraphics();
		}
		
		if(isMap){//Gdy w��czona jest mapa
			//Malowanie mapy
			
			scene.setBounds(x, y, width, height);
			//Prostok�t okre�laj�cy obszar, kt�ry wy�wietla si� na ekranie
			
			//PRZESUWANIE SCENY (POD�O�A)
			if(x<0) mLeft = -x;
			else mLeft = 0;
			
			if(y<0) mTop = -y;
			else mTop = 0;
			
			if(x+width > mapWidth) {mRight = x+width - mapWidth;}
			else mRight = 0;
			
			if(y+height > mapHeight) mDown = y+height - mapHeight;
			else mDown = 0;
			
			for(int i=-1; i<(width/256)+1; i++){
				for (int j=-1; j<(height/256)+1; j++){
					g.drawImage(bgImage, i*256 - X, j*256 - Y, this);
				}		
			}

			//marginesy
			g.fillRect(0, 0, mLeft, height);
			g.fillRect(width-mRight, 0, mRight, height);
			g.fillRect(0, 0, width, mTop );
			g.fillRect(0, height-mDown, width, mDown);			
			
			world.paint(g, this, scene);

			
			//Malowanie menu
			gameMenu.paint(g, this, scene, world.getCurParty());
			g.drawString(world.getDate(), width-230, 815);				
		} 
		
	}
	
public void update(){ //Przesuwa scen� i odmalowuje
	
		if( !(x < -100 && xStep < 0) && !(x > (mapWidth-width) + 400 && xStep > 0)) {
			x+=xStep;
			X+=xStep;
			if(X>=256 || X<=-256){X=0;}
		}		
		
		if( !(y < -100 && yStep < 0) && !(y > (mapHeight-height) +100 && yStep >0)) {
			y+=yStep;
			Y+=yStep;
			if(Y>=256 || Y<=-256){Y=0;}
		}
		repaint();
	}

public void stop(){
	tm.stop();
}

public void start(){
	update();
	tm.start();
}

public void nextWeek()
{
	if(world.getCurParty().isDecision()){
	JDialog dialog = new DecisionDialog(main, world.getCurParty().nextDecision());
	main.showDialog(dialog); 
	}
	//dialog.pack();
//	dialog.setVisible(true);
}

public World getWorld(){return world;}

private class Listener implements ActionListener{ //S�uchacz do listy Combo i od�wie�ania mapy

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==tm){
			update();
		}
	}
	}

	@Override
	public void mouseDragged(MouseEvent e) {}

	@Override
	public void mouseMoved(MouseEvent e) {
		
		
		//Je�li kursor zjedzie do brzegu ekranu, scena si� przesuwa
		//tylko wtedy, je�li widok nie jest oddalony o wi�cej ni� wielko�� panelu od brzegu mapy
		//if(x > -width && x < mapWidth+width && y > -height && y < mapHeight+height){
				if(e.getX()<30 && x > -width/5){xStep=-10;}
				else if(e.getX()>width-300 && e.getX()<width-270 && x < mapWidth+width/5){xStep=10;}
				else {xStep=0;}
				
				if(e.getY()<50 && y > -height/5){yStep=-10;}
				else if(e.getY()>height-50 && y < mapHeight+height/5){yStep=10;}
				else {yStep=0;}
	//}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		r = new Rectangle(e.getX(), e.getY(), 3, 3);
		//Sprawdzam, czy kursor znajdowa� si� w menu, czy na mapie
		if(e.getX()>width-270){
			if(gameMenu.isMainMenuClicked(r)){
				this.stop();
				main.menu();
			}
			else if(gameMenu.isNewDayClicked(r)){
				
				world.newDay();
				if(world.getDaysNum()%7 == 0)
				{
					nextWeek();
				}
			}
			//Wybieranie ikony armii w menu
			Army a = world.getCurParty().getClickedArmy(r);
			world.moveArmy(a);
			
		}
		else {
			r.setBounds((int)r.getX()+x, (int)r.getY()+y, 3, 3);
			world.mouseClicked(e, r);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	public void keyPressed(KeyEvent e) {}

	public void keyReleased(KeyEvent e) {}

	public void keyTyped(KeyEvent e) {
		
		if(e.getKeyCode()==KeyEvent.VK_ESCAPE){
			
			tm.stop();
			main.menu();
			
		}
	}
}
