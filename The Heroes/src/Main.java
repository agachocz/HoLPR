import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Main extends JFrame implements KeyListener{

	/**
	 * G³ówna klasa gry
	 * Wyœwietla menu i pole gry
	 */
	
	static Toolkit tk = Toolkit.getDefaultToolkit();
	//int width = (int)tk.getScreenSize().getWidth();
	//int height = (int)tk.getScreenSize().getHeight();
	int width = 1500;
	int height = 850;
	String imgS = "Graphics//Menu//bground.png";
	Image img;
	
	JPanel actPanel; 
	
	Menu menu;
	Scene scene;
	Creator creator; 
	
	boolean game = false; //true, je¿eli zosta³a uruchomiona jakaœ gra
	
	public static int ID = 0; //Ostatnie u¿ywane ID
	
	Container c = getContentPane();
	
	public Main() throws IOException{
		
		img = getImage(imgS);
		
		addKeyListener(this);
		 
		setSize(width, height);
		setLocation(0, 0);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new FlowLayout(FlowLayout.CENTER));
		//setUndecorated(true);
				
		menu = new Menu(this);
		
		//menu.setBackground(Color.BLACK);
		
		c.setLayout(new GridLayout(1, 1));
		c.add(menu);
		repaint();
	}
	
	/*
	public void paint(Graphics g)
	{
		g.drawImage(img, 0, 0, this);
		//actPanel.repaint();
		//repaint();
		menu.repaint();
		//if(c.getComponent(0) == menu){
		//	menu.repaint();
		//}
		//else scene.repaint();
	}
	*/
	
	public Scene getScene(){return scene;}
	public World getWorld(){return scene.getWorld();}
	
	public boolean isFocusTraversable(){
		return true;
	}
	
	public static void main(String[] args) throws IOException {
			JFrame mainFrame = new Main();
			mainFrame.setVisible(true);
			mainFrame.repaint();
	} 
	
	public void showDialog(JDialog dialog)
	{
		//dialog.pack();
		dialog.setVisible(true);
	}
	
	public void showCreator() throws IOException{ 
		creator = new Creator();
		//menu.setVisible(false);
		creator.setVisible(true);
	}
	
	public void read(Save save){ //uruchomienie wczytanego zapisu
		try {
			World world = new World(save.getName(), save.getMapWidth(), save.getMapHeight(), save.getRoads(), save.getThings(),
					save.getParties(), save.getMainParty(), save.getDay());
			scene = new Scene(this, world);
			//actPanel = scene;
			//actPanel.repaint();
			//c.add(scene);
			//menu.setVisible(false);
			//scene.setVisible(true);
			c.remove(menu);
			c.add(scene);
			//JPanel test = new JPanel();
			//test.setBackground(Color.RED);
			//test.setSize(new Dimension(200, 400));
			//c.add(test);
			//test.setVisible(true);
			scene.setSize(width, height);
			
			game = true;
			
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	
	public void exit(){System.exit(0);}
	
	public void menu(){
		//scene.setVisible(false);
		//menu.setVisible(true);
		c.remove(scene);
		c.add(menu);
		repaint();
		menu.show();
	}
	
	public void backToGame(){
		//menu.setVisible(false);
		//scene.setVisible(true);
		c.remove(menu);
		c.add(scene);
		scene.start();
		//System.out.println(actPanel.getName());
	}
	
	public static Image getImage (String s) throws IOException{
		return tk.getImage(s);
	}

	//Obs³uga klawiatury
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_ESCAPE && game){
			scene.stop();
			menu();
			//System.out.println(actPanel.getName());
	}}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {
		}

}
