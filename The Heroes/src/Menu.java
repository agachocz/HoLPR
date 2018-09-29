import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.*;


public class Menu extends JPanel{

	//MENU G£ÓWNE
	private JPanel mainMenu;
	private JButton newGameB = new JButton("NOWA GRA");
	private JButton saveB = new JButton("ZAPISZ");
	private JButton readB = new JButton("WCZYTAJ");
	private JButton gameB = new JButton("POWRÓT DO GRY");
	private JButton creditsB = new JButton();
	private JButton exitB = new JButton("WYJŒCIE");
	private JButton creatorB = new JButton("KREATOR");
	
	//WCZYTYWANIE ZAPISANEJ GRY
	private JPanel read;
	private JButton readChosenB = new JButton("WCZYTAJ");
	private JComboBox<String> savesList = new JComboBox<String>();
	private JButton backB1 = new JButton("POWRÓT");
	
	File dir = new File("Saves");
	String[] names;
	
	//ZAPISYWANIE GRY
	Save newSave;
	private JPanel save;
	private JTextField name = new JTextField("Wpisz nazwê");
	private JButton saveGameB = new JButton("ZAPISZ");
	private JButton backB2 = new JButton("POWRÓT");
	private JComboBox<String> savesList2 = new JComboBox<String>();
	
	//UTWORZENIE NOWEJ GRY
	private JPanel newGame;
	private JComboBox<Party> partiesList = new JComboBox<Party>();
	ComboBoxRenderer renderer = new ComboBoxRenderer();
	private JButton backB4 = new JButton("POWRÓT");
	private JButton makeNewGameB = new JButton("NOWA GRA");
	
	private JButton backB3 = new JButton("POWRÓT");
	private Save s;
	
	ActionListener lst = new Listener();
	
	Main main;
	
	String imgS = "Graphics//Menu//bground.png";
	Image img;
	
	
	public Menu(Main main){
		
		this.main = main;
		try {
			img = Main.getImage(imgS);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//setSize(300, 600);
		
		newGameB.addActionListener(lst);
		saveB.addActionListener(lst);
		readB.addActionListener(lst);
		gameB.addActionListener(lst);
		creditsB.addActionListener(lst);
		exitB.addActionListener(lst);
		creatorB.addActionListener(lst);
		backB1.addActionListener(lst);
		backB2.addActionListener(lst);
		backB3.addActionListener(lst);
		backB4.addActionListener(lst);
		readChosenB.addActionListener(lst);
		saveGameB.addActionListener(lst);
		savesList2.addActionListener(lst);
		partiesList.addActionListener(lst);
		makeNewGameB.addActionListener(lst);
		
		//Menu g³ówne
		mainMenu = new JPanel();
		mainMenu.setSize(300, 600);
		mainMenu.setLayout(new GridLayout(6, 1));

		mainMenu.add(newGameB);
		mainMenu.add(saveB);
		mainMenu.add(readB);
		mainMenu.add(gameB);
		//mainMenu.add(creditsB);
		mainMenu.add(exitB);
		mainMenu.add(creatorB);
		
		saveB.setEnabled(false);
		gameB.setEnabled(false);
		
		//Wczytywanie
		read = new JPanel();
		read.setPreferredSize(new Dimension(300, 300));
		read.setLayout(new GridLayout(3, 1));
		
		read.add(backB1);
		read.add(savesList);
		read.add(readChosenB);
		
		//Zapisywanie
		save = new JPanel();
		save.setSize(300, 300);
		save.setLayout(new GridLayout(4, 1));
		
		save.add(backB2);
		save.add(name);
		save.add(savesList2);
		save.add(saveGameB);
		
		//Nowa gra
		newGame = new JPanel();
		newGame.setSize(500, 5000);
		newGame.setLayout(new GridLayout(3, 1));
		
		renderer.setPreferredSize(new Dimension(200, 64));
		partiesList.setRenderer(renderer);
		partiesList.setMaximumRowCount(3);
		
		newGame.add(backB4);
		newGame.add(partiesList);
		newGame.add(makeNewGameB);
		
		add(mainMenu);
		add(read);
		add(save);
		add(newGame);
		hide();
		mainMenu.setVisible(true);
		
		repaint();
		
		
	}
	
	
	public void paintComponent(Graphics g)
	{
		g.drawImage(img, 0, 0, this);		
	}
	
	
	
	public void hide(){
		mainMenu.setVisible(false);
		read.setVisible(false);
		save.setVisible(false);
		newGame.setVisible(false);
	}
	public void show(){
		hide();
		mainMenu.setVisible(true);
		if(main.game){
			saveB.setEnabled(true);
			gameB.setEnabled(true);			
		}
		/*else {
			saveB.setEnabled(false);
			gameB.setEnabled(false);	
		}*/
	}
	
	public void makeSavesList(JComboBox<String> list){
		list.removeAllItems();
		
		names = dir.list();
		
		for(int i=0; i<names.length; i++){
			list.addItem(names[i].substring(0, names[i].length()-4)); 
		}
	}
	
	//Zapisywanie gry
	public void save(Save save) throws IOException {
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream ("Saves//"+save.getName()+".dat"));
		out.writeObject(save);
		out.close();
	}
	
	//Wczytywanie gry
	public Save read(String n) throws IOException, ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream("Saves//"+n+".dat"));
		Save s = (Save) in.readObject();
		in.close();
		return s;
		}
	
	private class Listener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(e.getSource()==newGameB){
				
				try {
					s = read("default");
					partiesList.removeAllItems();
					
					for(int i=0; i<s.getParties().length; i++){
						partiesList.addItem(s.getParties()[i]);
					}
					
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				hide();
				newGame.setVisible(true);
			}
	        if(e.getSource()==saveB){
	        	newSave = main.getScene().getWorld().createSave();
	        	name.setText(newSave.getName());
	        	makeSavesList(savesList2);
	        	hide();
	        	save.setVisible(true);
	        }
	        if(e.getSource()==readB){
	        	hide();
	        	read.setVisible(true);
	        	makeSavesList(savesList);
	        }       
	        if(e.getSource()==gameB){
	        	main.backToGame();
	        }
	        if(e.getSource()==creditsB){}       
	        if(e.getSource()==exitB){
	        	main.exit();
	        } 
	        if(e.getSource()==creatorB){try {
				main.showCreator();
			} catch (IOException e1) {
				e1.printStackTrace();
			}}
	        if(e.getSource()==backB1 || e.getSource()==backB2 || e.getSource()==backB4){
	        	hide();
	        	mainMenu.setVisible(true);
	        }
	        if(e.getSource()==readChosenB){
	        	try {
					main.read(read((String) savesList.getSelectedItem()));
				} catch (ClassNotFoundException | IOException e1) {
					e1.printStackTrace();
				}
	        	hide();
	        }
	        if(e.getSource()==saveGameB){
	        	newSave.setName(name.getText());
	        	if(newSave.getName()!=""){
	        	try {
					save(newSave);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
	        	hide();
	        	mainMenu.setVisible(true);
	        	}
	        }
	        if(e.getSource()==savesList2){
	        	repaint();
	        	name.setText((String) savesList.getSelectedItem());
	        	repaint();
	        }
	        if(e.getSource()==makeNewGameB){
	        	s.setMainParty(partiesList.getSelectedIndex());
	        	partiesList.removeAllItems();
	        	main.read(s);
	        	hide();
	        }
			
		}
		
	}

	
	//KLASA DO POPRAWNEGO WYŒWIETLANIA COMBO BOX
	
	private class ComboBoxRenderer extends JLabel implements ListCellRenderer {

		public ComboBoxRenderer() {
			setOpaque(true);
			setHorizontalAlignment(LEFT);
			setVerticalAlignment(CENTER);
		}
/*
* This method finds the image and text corresponding
* to the selected value and returns the label, set up
* to display the text and image.
*/
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
//Get the selected index. (The index param isn't
//always valid, so just use the value.)

			Party p = (Party)value;
			
			
			if (isSelected) {
					setBackground(list.getSelectionBackground());
					setForeground(list.getSelectionForeground());
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}
			

//Set the icon and text.  If icon was null, say so.
			ImageIcon icon = new ImageIcon(p.getLogo());
			String name = p.getName();
			setIcon(icon);
			setText(name);

			return this;
		}

}
}
