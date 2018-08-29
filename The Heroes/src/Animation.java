import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.Timer;

public class Animation {

	transient Image[] img;
	Image curImg;
	String[] imgS;
	int pauseLng;
	
	int count = 0;
	
	Timer tm; 
	ActionListener lst = new Listener();
	
	public Animation(String[] imgS, int pauseLng){
		this.pauseLng = pauseLng;
		this.imgS = imgS;
		
		for(int i=0; i<imgS.length; i++){
			try {
				img[i] = Main.getImage(imgS[i]);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		curImg = img[count];
		
		tm = new Timer(pauseLng, lst);
		tm.start();
	}
	
	private class Listener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==tm){
				count++;
				if(count == img.length) count = 0;
				curImg = img[count];
			}
		}
		}
}
