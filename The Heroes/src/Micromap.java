import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

public class Micromap {

	private int mapWidth;
	private int mapHeight;
	private int scale;
	private int width;
	private int height;
	
	private ArrayList <Building> buildings;
	private RoadMap roadMap;
	private ArrayList <Thing> things;
	
	private byte[][] matrix;
	/* UK£AD KOLORYSTYCZNY
	 * 
	 * 0 - zielony (trawa)
	 * 1 - niebieski (przyroda)
	 * 2 - czerwony (budynek)
	 * 3 - czarny (droga)
	 * 
	 */
	
	public Micromap(World world, int scale)
	{
		this.mapWidth = world.getMapWidth();
		this.mapHeight = world.getMapHeight();
		this.scale = scale;
		roadMap = world.getRoadMap();
		buildings = roadMap.getBuildings();
		things = world.getThings();
		
		
		width = mapWidth/scale;
		height = mapHeight/scale;
		
		matrix = new byte[width][height];
		for(int i=0; i<width; i++){
			for(int j=0; j<height; j++){
				matrix[i][j] = 0;
			}
		}
		
		Thing t;
		for(int i=0; i<things.size(); i++){
			t = things.get(i);
			for(int j=0; j<=(t.getWidth()/scale); j++){
				for(int k=0; k<=(t.getHeight()/scale); k++){
					matrix[t.getX()/scale+j][t.getY()/scale+k] = 1;
				}
			}	
		}
		
		Building b;
		for(int i=0; i<buildings.size(); i++){
			b = buildings.get(i);
			for(int j=0; j<=(b.getWidth()/scale); j++){
				for(int k=0; k<=(b.getHeight()/scale); k++){
					matrix[b.getX()/scale+j][b.getY()/scale+k] = 2;
				}
			}
		}
		
		Road[][] roads = roadMap.getRoads();
		int step = roadMap.getStep();
		
		for(int i=0; i<roads.length; i++){
			for(int j=0; j<roads[i].length; j++){
				if(roads[i][j] != null){
					matrix[i*step/scale][j*step/scale] = 3;
				}
			}
		}
		
	}
		
		public void paint(Graphics g, ImageObserver io, int posX, int posY, Rectangle scene){
			Graphics2D g2d = (Graphics2D) g;
			
			//TRAWA
			g2d.setColor(Color.GREEN);
			for(int i=0; i<width; i++){
				for(int j=0; j<height; j++){
					if(matrix[i][j] == 0)g2d.fillRect(posX+i, posY+j, 1, 1);
				}
			}
			
			//NIEBIESKI
			g2d.setColor(Color.BLUE);
			for(int i=0; i<width; i++){
				for(int j=0; j<height; j++){
					if(matrix[i][j] == 1)g2d.fillRect(posX+i, posY+j, 1, 1);
				}
			}
			
			//TRAWA
			g2d.setColor(Color.RED);
			for(int i=0; i<width; i++){
				for(int j=0; j<height; j++){
					if(matrix[i][j] == 2)g2d.fillRect(posX+i, posY+j, 1, 1);
				}
			}
			
			//TRAWA
			g2d.setColor(Color.BLACK);
			for(int i=0; i<width; i++){
				for(int j=0; j<height; j++){
					if(matrix[i][j] == 3)g2d.fillRect(posX+i, posY+j, 1, 1);
				}
			}
			
			g2d.setColor(Color.WHITE);
			g2d.drawRect(posX+((int)scene.getX()/scale), posY+((int)scene.getY()/scale), 
					(int)scene.getWidth()/scale, (int)scene.getHeight()/scale);
		}
		
		public int getScale(){return scale;}
		public int getWidth(){return mapWidth/scale;}
		public int getHeight(){return mapHeight/scale;}
		
}
