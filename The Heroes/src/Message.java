import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Message implements Cloneable, Serializable{

	private String text;
	
	public Message(String text)
	{
		this.text = text;
		
	}
	
	//Skutki wiadomoœci
	public void action(World world)
	{
		
	}
	
	public String getText(){return text;}
	
	
	public Message clone() throws CloneNotSupportedException {
		return (Message) super.clone();} 

	//ZAPIS I ODCZYT DANYCH
	private void writeObject(ObjectOutputStream out) throws IOException{
		out.defaultWriteObject();
	}

	private void readObject (ObjectInputStream in) throws ClassNotFoundException, IOException{
		in.defaultReadObject();
	}
}
