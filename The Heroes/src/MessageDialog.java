import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

public class MessageDialog extends JDialog {

	protected Message message;
	protected JFrame frame;
	protected JButton okButton;
	protected JLabel label;
	
	public MessageDialog(JFrame frame, Message message)
	{
		super(SwingUtilities.windowForComponent(frame));
		this.message = message;
		this.frame = frame;
		
		setSize(new Dimension(200, 200));
		setLocation(1500/2 - this.getWidth()/2, 100);
		setUndecorated(true);
		setResizable(true);
		setBackground(Color.BLACK);
		setForeground(Color.WHITE);
		
		Container c = getContentPane();
		okButton = new JButton("OK");
		label = new JLabel(message.getText());
		
		okButton.addActionListener( new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
				
			}
			} );
		
		Border border = BorderFactory.createLineBorder(Color.BLACK);
		//c.setLayout(new FlowLayout());
		
		Box b = Box.createVerticalBox();
		
		b.add(Box.createVerticalGlue());
		Box textBox = Box.createHorizontalBox();
		textBox.setBorder(border);
		label.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		textBox.add(label);
		b.add(textBox);
		b.add(Box.createVerticalGlue());
		Box butBox = Box.createHorizontalBox();		
		butBox.add(okButton);
		b.add(butBox);
		b.add(Box.createVerticalGlue());
		//b.setBorder(BorderFactory.createEmptyBorder(50, 10, 10, 20));
		
		Box mainBox = Box.createHorizontalBox();
		mainBox.setBorder(border);
		mainBox.add(Box.createHorizontalStrut(10));
		mainBox.add(b);
		mainBox.add(Box.createHorizontalStrut(10));
		
		c.add(mainBox);

		
	}
}
