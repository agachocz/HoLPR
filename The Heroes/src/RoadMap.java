import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;


public class RoadMap implements Serializable {
	
	private int width;
	private int height;
	private int step; //rozmiar jednego odcinka drogi
	private int nrRows, nrCols;
	
	//private ArrayList<Road> roads = new ArrayList<Road>();
	private Road[][] roads; 
	private int selID = -1; //Id zaznaczonego elementu 
	private int selX = -1;
	private int selY = -1;

	private ArrayList<Building> buildings = new ArrayList<Building>(); //budynki, do których dochodz¹ drogi
	
	public RoadMap(int width, int height, int step){
		this.width = width;
		this.height = height;
		this.step = step;
		nrRows = height/step;
		nrCols = width/step;
		
		roads = new Road[nrRows][nrCols];
	}
	
	public void paint(Graphics g, ImageObserver io, Rectangle scene){
		
		for(int i = 0; i<nrRows; i++){
			for(int j = 0; j<nrCols; j++){
				//System.out.println((int)scene.getX()/step);
				//System.out.println((int)scene.getY()/step);
				if(i >= (int)scene.getX()/step && i <= (scene.getX()+scene.getWidth())/step){
					if(j >= (int)scene.getY()/step && j <= (scene.getX()+scene.getHeight())/step){
						if( i>=0 && j >= 0 && roads[i][j] != null){
				roads[i][j].paint(g, io, (int)scene.getX(), (int)scene.getY());
				}
					}
				}
			    //g.drawImage(img[grid[i][j]+1], i*step, j*step, io);
				
			}
		}
		
		/*
		//Maluje ka¿dy kawa³ek drogi, który mieœci siê na scenie
		for(int i=0; i<roads.length; i++){
			if(roads.get(i).getRect().intersects(scene)){
				roads.get(i).paint(g, io, (int)scene.getX(), (int)scene.getY());
			}
		}
		*/
		for(int i=0; i<buildings.size(); i++){
			if(buildings.get(i).getRect().intersects(scene)){
				buildings.get(i).paint(g, io, (int)scene.getX(), (int)scene.getY());
				
			}
		}
	}
	
	//Zaznaczanie dróg w zasiêgu
	//Przy za³o¿eniu sta³ego maksymalnego dystansu 5 dróg
	public void checkAv(int posX, int posY, boolean b){
		//indeksy startu
		int xStart = posX/step;
		int yStart = posY/step;
		
		ArrayList<Integer> passed;
		if(b) passed = new ArrayList<Integer>();
		else passed = null;
		
		roads[xStart][yStart].checkNext(roads, xStart, yStart, 5, b, passed, -1);
		
	}
	
	public void add(Road r){ //dodanie kawa³ka drogi
		//wyrównanie do siatki
		r.addX(r.getX()%step);
		r.addY(r.getY()%step);
		
		int x = r.getX()/step;
		int y = r.getY()/step;
		//próba pod³¹czenia do systemu
		
				r.setType(0);
				roads[x][y] = r;
			
				if(roads[x][y-1] != null){
					join(roads[x][y-1], r, 2);
					removeEnds(x, y-1, 2);
					removeEnds(x, y, 0);
					
					//jeœli jest tam budynek
					if(roads[x][y-1].getType() == -1){
						for(Building b : buildings){
							System.out.println("Building id: " + b.getId());
							System.out.println("Road: "+roads[x][y-1].getBuildingID());
							if(b.getId() == roads[x][y-1].getBuildingID()){
								if(!b.isConnected()){
									connectBuild(b, x, y);
								}
							}
						}
					}
					
				}
				if(roads[x][y+1] != null){ 
					join(roads[x][y+1], r, 0);					
					removeEnds(x, y+1, 0);
					removeEnds(x, y, 2);
					
					if(roads[x][y+1].getType() == -1){
						for(Building b : buildings){
							if(b.getId() == roads[x][y+1].getBuildingID()){
								if(!b.isConnected()){
									connectBuild(b, x, y);
								}
							}
						}
					}

				}
				if(roads[x-1][y] != null){
					join(roads[x-1][y], r, 1);
					removeEnds(x-1, y, 1);
					removeEnds(x, y, 3);
					
					if(roads[x-1][y].getType() == -1){
						for(Building b : buildings){
							if(b.getId() == roads[x-1][y].getBuildingID()){
								if(!b.isConnected()){
									connectBuild(b, x, y);
								}
							}
						}
					}
					
				}
				if(roads[x+1][y] != null){ 
					join(roads[x+1][y], r, 3);
					removeEnds(x+1, y, 3);
					removeEnds(x, y, 1);
					
					if(roads[x+1][y].getType() == -1){
						for(Building b : buildings){
							if(b.getId() == roads[x+1][y].getBuildingID()){
								if(!b.isConnected()){
									connectBuild(b, x, y);
								}
							}
						}
					}
					
				}
				
				

		}
	/*
	public void remove(Road r){
		for(int i=0; i<roads.size(); i++){
			if(r.getId() == roads.get(i).getId()){ //odnajduje fragment na liœcie
				//Zmiana typów pod³¹czonych dróg
				if(r.getNorthID() >= 0){ //je¿eli jest pod³¹czona od pó³nocy
					
				}
				roads.remove(i); //usuwa fragment
			}
		}
	}*/
	
	public void connectBuild(Building b, int x, int y){
		b.setEntry(x, y);
		for(int i = b.getX()/step; i<(b.getX() + b.getWidth())/step; i++){
			for(int j = b.getY()/step; j<(b.getY() + b.getHeight())/step; j++){
				roads[i][j] = null;
			}
		}
	}
	
	public void addBuilding(Building b, Road r){
		buildings.add(b);
		buildings.get(buildings.size()-1).setId(Main.ID++);
		
		for(int i = b.getX()/step; i<(b.getX() + b.getWidth())/step; i++){
			for(int j = b.getY()/step; j<(b.getY() + b.getHeight())/step; j++){
				roads[i][j] = r;
				roads[i][j].setType(-1);
				roads[i][j].setBuildingID(b.getId());
			}
		}
	}
	
	public void removeEnds(int x, int y, int dir){
		//x, y - wspó³rzêdne w tablicy
		//dir - kierunek
		
		switch(dir){
		
		case 0: //góra (czyli przy³¹czono drogê z góry)
			
			if(roads[x-1][y] == null){
				switch(roads[x][y].getType()){
				case 2: roads[x][y].setType(1); break;
				case 6: roads[x][y].setType(4); break;
				case 9: roads[x][y].setType(1); break;
				case 10: roads[x][y].setType(7); break;
				}
			}
			if(roads[x+1][y] == null){
				switch(roads[x][y].getType()){
				case 4: roads[x][y].setType(1); break;
				case 6: roads[x][y].setType(2); break;
				case 7: roads[x][y].setType(1); break;
				case 10: roads[x][y].setType(9); break;
				}
			}
			if(roads[x][y+1] == null){
				switch(roads[x][y].getType()){
				case 7: roads[x][y].setType(4); break;
				case 9: roads[x][y].setType(2); break;
				case 10: roads[x][y].setType(6); break;
				}
			}
			
			break;
		
		case 1: //prawo
			
			if(roads[x][y-1] == null){
				switch(roads[x][y].getType()){
				case 4: roads[x][y].setType(0); break;
				case 6: roads[x][y].setType(4); break;
				case 7: roads[x][y].setType(5); break;
				case 10: roads[x][y].setType(8); break;
				}
			}
			if(roads[x][y+1] == null){
				switch(roads[x][y].getType()){
				case 5: roads[x][y].setType(0); break;
				case 7: roads[x][y].setType(6); break;
				case 8: roads[x][y].setType(0); break;
				case 10: roads[x][y].setType(6); break;
				}
			}
			if(roads[x-1][y] == null){
				switch(roads[x][y].getType()){
				case 6: roads[x][y].setType(4); break;
				case 8: roads[x][y].setType(5); break;
				case 10: roads[x][y].setType(7); break;
				}
			}
			
			break;
			
		case 2: //dó³
			
			if(roads[x-1][y] == null){
				switch(roads[x][y].getType()){
				case 3: roads[x][y].setType(1); break;
				case 8: roads[x][y].setType(5); break;
				case 9: roads[x][y].setType(1); break;
				case 10: roads[x][y].setType(7); break;
				}
			}
			if(roads[x+1][y] == null){
				switch(roads[x][y].getType()){
				case 5: roads[x][y].setType(1); break;
				case 7: roads[x][y].setType(1); break;
				case 8: roads[x][y].setType(3); break;
				case 10: roads[x][y].setType(7); break;
				
			}
			}
			if(roads[x][y-1] == null){
				switch(roads[x][y].getType()){
				case 7: roads[x][y].setType(5); break;
				case 9: roads[x][y].setType(3); break;
				case 10: roads[x][y].setType(8); break;
				}
			}
			
			break;
			
		case 3: //lewo
			
			if(roads[x][y-1] == null){
				switch(roads[x][y].getType()){
				case 2: roads[x][y].setType(0); break;
				case 6: roads[x][y].setType(0); break;
				case 9: roads[x][y].setType(3); break;
				case 10: roads[x][y].setType(8); break;
				}
			}
			if(roads[x][y+1] == null){
				switch(roads[x][y].getType()){
				case 3: roads[x][y].setType(0); break;
				case 8: roads[x][y].setType(0); break;
				case 9: roads[x][y].setType(2); break;
				case 10: roads[x][y].setType(6); break;
				
			}
			}
			if(roads[x+1][y] == null){
				switch(roads[x][y].getType()){
				case 6: roads[x][y].setType(2); break;
				case 8: roads[x][y].setType(3); break;
				case 10: roads[x][y].setType(9); break;
				}
			}
			
			break;
			
		}
		
		
	}
	
	public void join(Road r1, Road r2, int dir){ //³¹czy dwie drogi, dir = direction
		
		switch(dir){
		
		case 0: //góra (czyli r2 jest powy¿ej r1)
			
			if(r1.getType()==0){r1.setType(6);}
			else if(r1.getType()==3){r1.setType(9);}
			else if(r1.getType()==5){r1.setType(7);}
			else if(r1.getType()==8){r1.setType(10);}
			
			r1.setNorthID(r2.getId());
			
			if(r2.getType()==0){r2.setType(8);}
			else if(r2.getType()==2){r2.setType(9);}
			else if(r2.getType()==4){r2.setType(7);}
			else if(r2.getType()==6){r2.setType(10);}
			else if(r2.getType()==-1){r2.setType(1);}
			r2.setSouthID(r1.getId());
			
			break;
		
		case 1: //prawo
			
			if(r1.getType()==1){r1.setType(7);}
			else if(r1.getType()==2){r1.setType(6);}
			else if(r1.getType()==3){r1.setType(8);}
			else if(r1.getType()==9){r1.setType(10);}
			r1.setEastID(r2.getId());
			
			if(r2.getType()==1){r2.setType(9);}
			else if(r2.getType()==4){r2.setType(6);}
			else if(r2.getType()==5){r2.setType(8);}
			else if(r2.getType()==7){r2.setType(10);}
			else if(r2.getType()==-1){r2.setType(0);}
			r2.setWestID(r1.getId());
			
			break;
			
		case 2: //dó³
			
			if(r2.getType()==0){r2.setType(6);}
			else if(r2.getType()==3){r2.setType(9);}
			else if(r2.getType()==5){r2.setType(7);}
			else if(r2.getType()==8){r2.setType(10);}
			else if(r2.getType()==-1){r2.setType(1);}
			r2.setNorthID(r1.getId());
			
			if(r1.getType()==0){r1.setType(8);}
			else if(r1.getType()==2){r1.setType(9);}
			else if(r1.getType()==4){r1.setType(7);}
			else if(r1.getType()==6){r1.setType(10);}
			r1.setSouthID(r2.getId());
			
			break;
			
		case 3: //lewo
			
			if(r2.getType()==1){r2.setType(7);}
			else if(r2.getType()==2){r2.setType(6);}
			else if(r2.getType()==3){r2.setType(8);}
			else if(r2.getType()==9){r2.setType(10);}
			else if(r2.getType()==-1){r2.setType(0);}
			r2.setEastID(r1.getId());
			
			if(r1.getType()==1){r1.setType(9);}
			else if(r1.getType()==4){r1.setType(6);}
			else if(r1.getType()==5){r1.setType(8);}
			else if(r1.getType()==7){r1.setType(10);}
			r1.setWestID(r2.getId());
			
			break;
			
		}
		
	}
	
	public boolean select(Rectangle r){ //zwraca true, je¿eli jest jakiœ fragment (selID) na tej pozycji
										//lub jeœli jest tam budynek
		boolean b = false;
		selID = -1;
		
		int x = (int) (r.getX()/step);
		int y = (int) (r.getY()/step);
		
		if(roads[x][y] != null){
		selID = roads[x][y].getId();
		selX = x;
		selY = y;
		}
		
		if(!b){ //jeœli nie ma pod kursorem ¿adnej drogi, sprawdza, czy s¹ budynki
		for(int i=0; i<buildings.size(); i++){
			if(buildings.get(i).getRect().intersects(r)){
				b = true;
				break;
			}
		}}
		return b;
	}
	
	public void removeSelected(){ //usuwa klikniêty fragment
		if(selID!=-1){
			roads[selX][selY] = null;
			selID = -1;
			selX = -1;
			selY = -1;
			
		}
		}
	
	public ArrayList<Integer> makeMove(int x, int y){
		//punkt koñcowy
		int desX = x/32;
		int desY = y/32;

		//Jeœli miejsce jest w zasiêgu		
		if(desX < nrCols && desY < nrRows && roads[desX][desY].isAvailable()){
			return roads[desX][desY].getRoute();
		}
		else return null;
	}
	
	public ArrayList<Building> getBuildings(){return buildings;}
	//public ArrayList<Road> getRoads(){return roads;}
	public Road[][] getRoads(){return roads;}
	public int getStep(){return step;}
	
	//ZAPIS I ODCZYT DANYCH
	private void writeObject(ObjectOutputStream out) throws IOException{
		out.defaultWriteObject();
	}

	private void readObject (ObjectInputStream in) throws ClassNotFoundException, IOException{
		in.defaultReadObject();
		//for(int i=0; i<roads.size(); i++){
		//	roads.get(i).update();
		//}
	}
		
		
	}

