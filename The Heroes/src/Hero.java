import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.util.ArrayList;


public class Hero extends Thing {
	
	/**
	 * Bohater
	 */
	String menuIMGS;
	transient Image menuIMG;

	public Hero(byte id, String name, String info, String logo, String menuIMGS) throws IOException{
		super(id, name, info, logo);
		this.menuIMGS = menuIMGS;
		
		menuIMG = Main.getImage(menuIMGS);
		this.setRect(0, 0, 93, 100);
}
	
	public void paint (Graphics g, ImageObserver io, int x, int y){
		if(menuIMG == null){
			try {
				menuIMG = Main.getImage(menuIMGS);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		g.drawImage(getLogoIMG(), x, y+2, io);
	}
	
	public Image getMenuIMG(){
		return menuIMG;
	}
	
	//width - maksymalna liczba znaków w linijce
	public String[] getInfoInLines(int width)
	{
		ArrayList<String> lines = new ArrayList<String>();
		String line = "";
		String word = "";
		
		String info = getInfo();
		
		for(int i=0; i<info.length(); i++)
		{
			if(info.charAt(i) != ' '){
				word += info.charAt(i);
			}
			else {
				if(line.length()+word.length()+1 <= width){
					line += " " + word;
					word = "";
				}
				
				else{
					lines.add(line);
					line = word;
					word = "";
				}
			}
		}
		line += " " + word;
		lines.add(line);
		
		String[] linesTab = new String[lines.size()];
		for(int i=0; i<linesTab.length; i++){
			linesTab[i] = lines.get(i);
		}
		return linesTab;
	}

}
