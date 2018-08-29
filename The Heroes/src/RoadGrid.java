import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.Serializable;

public class RoadGrid implements Serializable {

	int width, height, step, nrRows, nrCols;
	int[][] grid; 
	private String[] imgS;
	private transient Image[] img;
	
	public RoadGrid(int width, int height, int step, String[] imgS){
		this.width = width;
		this.height = height;
		this.step = step;
		this.imgS = imgS;
		
		nrRows = height/step;
		nrCols = width/step;
		
		grid = new int[nrRows][nrCols];
		
		for(int i=0; i<nrRows; i++){
			for(int j=0; j<nrCols; j++){
				grid[i][j] = -1;
			}
		}
		
		img = new Image[imgS.length];
		for(int i=0; i<imgS.length; i++){
			try {
				img[i] = Main.getImage(imgS[i]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void paint(Graphics g, ImageObserver io, Rectangle scene){
		for(int i = (int)scene.getX()/step; i<scene.getWidth()/step; i++){
			for(int j = (int)scene.getY()/step; j<scene.getHeight()/step; j++){
			g.drawImage(img[grid[i][j]+1], i*step, j*step, io);
			}
		}
	}
	
	public void add(Rectangle r){
		int x = (int) (r.getX()/step);
		int y = (int) (r.getY()/step);
		
		if(grid[x][y] != 0){
			grid[x][y] = 4;
			
			if(grid[x-1][y] != -1){ grid[x][y]-=2;}

		}
	}
	
	
}
