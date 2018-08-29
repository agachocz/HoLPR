import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.Border;

public class DecisionDialog extends JDialog {

	protected JFrame frame;
	private Decision decision;
	protected JTextPane label;
	private JButton option1B;
	private JButton option2B;
	//private JPanel quotePanel = new JPanel();
	//private JLabel titleL;
	private JTextPane quoteL;
	private JLabel autImage;
	private JLabel autL;
	//private JPanel autPanel = new JPanel();
	
	private Listener listener;
	
	private JDialog md;
	
	public DecisionDialog(JFrame frame, Decision decision) {
		super(frame, "Title", true);
		this.frame = frame;
		this.decision = decision;
		
		setSize(new Dimension(400, 500));
		setLocation(1500/2 - this.getWidth()/2, 100);
		setUndecorated(true);
		setResizable(true);
		setBackground(Color.BLACK);
		setForeground(Color.WHITE);

		Container c = getContentPane();
		
		option1B = new JButton(decision.getOptionText1());
		option2B = new JButton(decision.getOptionText2());
		listener = new Listener();
		option1B.addActionListener(listener);
		option2B.addActionListener(listener);
		
		//titleL = new JLabel(decision.getTitle());
		quoteL = new JTextPane();
		quoteL.setText(decision.getQuote());
		//quoteL.setLineWrap(true);
		//quoteL.setOpaque(true); ???
		quoteL.setEditable(false);
		autImage = new JLabel(new ImageIcon(decision.getAuthority().getImgS()));
		autL = new JLabel("<HTML>"+decision.getAuthority().getName() + "<br>" + decision.getAuthority().getTitle()+"</HTML>");
		label = new JTextPane();
		label.setText(decision.getText());
		label.setEditable(false);
		label.setBackground(autL.getBackground());
		quoteL.setBackground(autL.getBackground());
		//label.setLineWrap(true);
		
		Border border = BorderFactory.createLineBorder(Color.BLACK);
		Border messBorder = BorderFactory.createTitledBorder(border, decision.getTitle());
		Border quoteBorder = BorderFactory.createTitledBorder(border, "Dy¿urny autorytet");
		
		
		Box b = Box.createVerticalBox();
		
		//b.add(titleL);
		b.add(Box.createVerticalStrut(10));
		//b.setBorder(border);
		
		Box labelBox = Box.createHorizontalBox();
		labelBox.add(label);
		labelBox.setBorder(messBorder);
		b.add(labelBox);
		b.add(Box.createVerticalStrut(5));
		
		Box quoteBox = Box.createHorizontalBox();
		quoteBox.setBorder(quoteBorder);
		

		Box qb = Box.createVerticalBox();
		qb.add(quoteL);
		//qb.add(Box.createRigidArea(new Dimension(15, 100)));
		quoteBox.add(Box.createHorizontalStrut(20));
		quoteBox.add(qb);
		quoteBox.add(Box.createHorizontalGlue());
		quoteBox.add(Box.createHorizontalStrut(100));
		Box autBox = Box.createVerticalBox();
		autBox.add(autImage);
		autBox.add(autL);
		quoteBox.add(autBox, BorderLayout.EAST);
		
		//quotePanel.setLayout(new BorderLayout());
		//quotePanel.add(quoteL, BorderLayout.CENTER);
		//quotePanel.add(autBox, BorderLayout.EAST);
		//quotePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 10));
		
		//b.add(quotePanel);
		b.add(quoteBox);
		b.add(Box.createVerticalStrut(5));
		
		Box buttonBox = Box.createHorizontalBox();
		buttonBox.add(option1B);
		buttonBox.add(Box.createHorizontalStrut(20));
		buttonBox.add(option2B);
		
		b.add(buttonBox);
		
		//autPanel.setLayout(new BorderLayout());
		//autPanel.add(autImage, BorderLayout.CENTER);
		//autPanel.add(autL, BorderLayout.SOUTH);
		//quotePanel.setLayout(new BorderLayout());
		//quotePanel.add(quoteL, BorderLayout.CENTER);
		//quotePanel.add(autPanel, BorderLayout.EAST);
		
		//c.setLayout(new FlowLayout());
		//c.add(titleL);
		//c.add(label);
		//c.add(quotePanel);

		//JPanel butPanel = new JPanel();
		//butPanel.add(option1B);
		//butPanel.add(option2B);
		
		//c.add(butPanel);
		Box mainBox = Box.createHorizontalBox();
		mainBox.add(Box.createHorizontalStrut(15));
		mainBox.add(b);
		mainBox.add(Box.createHorizontalStrut(15));
		mainBox.setBorder(border);
		
		c.add(mainBox);	
		
	}
	
	private class Listener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(e.getSource() == option1B)
			{
				decision.action(((Main) frame).getWorld(), false);
				setVisible(false);
				String mdText = "<HTML>Dziêki podjêtej decyzji zyskujesz: <br><br> " + decision.getMoney(false) + " pieniêdzy<br>"
						+ decision.getVotes(false) + " nowego elektoratu<br>" + decision.getSausage(false) + " kie³basy wyborczej.<br>"; 
				md = new MessageDialog(frame, new Message(mdText));
				((Main) frame).showDialog(md);
			}
			else if(e.getSource() == option2B)
			{
				decision.action(((Main) frame).getWorld(), true);
				setVisible(false);
				String mdText = "<HTML>Dziêki podjêtej decyzji zyskujesz: <br><br> " + decision.getMoney(true) + " pieniêdzy<br>"
						+ decision.getVotes(true) + " nowego elektoratu<br>" + decision.getSausage(true) + " kie³basy wyborczej.<br>"; 
				md = new MessageDialog(frame, new Message(mdText));
				((Main) frame).showDialog(md);
			}
		}
		
	}

}
