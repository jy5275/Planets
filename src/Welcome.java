import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Welcome extends Frame{
	JButton Start,Load,About,Exit;
	JLabel logo;
	JPanel p;
	Dialog d;
	static volatile boolean started=false;
	int welcomeheight=400,welcomewidth=899;
	int aboutheight=400,aboutwidth=400;
	public void init() {
		// configure frame
		started=false;
		p = new JPanel(null);
		Toolkit kit = Toolkit.getDefaultToolkit();              
        Dimension screenSize = kit.getScreenSize();
        this.setSize(welcomewidth,welcomeheight);
        this.setBackground(Color.black);
		this.setLocation((screenSize.height-welcomeheight)/2,(screenSize.width-welcomewidth)/2);
		this.setUndecorated(true);
		// configure logo
		logo=new JLabel();
		ImageIcon logoimage=new ImageIcon("images/logo.png");
		logoimage.setImage(logoimage.getImage());
		logo.setIcon(logoimage);
		logo.setBounds(0, 0, 899, 202);
		logo.setLocation(0, 0);
		// configure dialog
		d= new Dialog(this,"about this",true);
		d.setBounds((screenSize.width-aboutwidth)/2, (screenSize.height-aboutheight)/2,400, 400);
		d.setLayout(new FlowLayout());
		d.setUndecorated(true);
		d.setBackground(Color.black);
		JLabel lab=new JLabel();
		ImageIcon introimage=new ImageIcon("images/intro.png");
		introimage.setImage(introimage.getImage().getScaledInstance(400, 200, Image.SCALE_SMOOTH));
		lab.setIcon(introimage);
		lab.setForeground(Color.white);
		lab.setBounds((screenSize.width-aboutwidth)/2, (screenSize.height-aboutheight)/2,400, 200);		
		JButton okbut=new JButton();
		okbut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				d.setVisible(false);
			}
		});
		okbut.setBorderPainted(false);
		okbut.setBounds((screenSize.width-aboutwidth)/2, (screenSize.height-aboutheight)/2,50, 50);
		ImageIcon okimage=new ImageIcon("images/ok.png");
		okimage.setImage(okimage.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
		okbut.setIcon(okimage);
		okbut.setBackground(Color.black);
		d.add(lab);
		d.add(okbut);
		
		// configure buttons
		Start = new JButton("");
		Start.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				Welcome.started=true;
			}
		});
		Start.setBounds(250, 203,  200, 80);
		Start.setBorderPainted(false);
		Start.setBackground(Color.black);
		ImageIcon startimage=new ImageIcon("images/start_w.png");
		startimage.setImage(startimage.getImage().getScaledInstance(138, 100, Image.SCALE_SMOOTH));
		Start.setIcon(startimage);
		
		Load = new JButton("");
		//Load.addActionListener(Loadg);
		Load.setBounds(450, 203,  200, 80);
		Load.setBorderPainted(false);
		Load.setBackground(Color.black);
		ImageIcon loadimage=new ImageIcon("images/load_w.png");
		loadimage.setImage(loadimage.getImage().getScaledInstance(133, 100, Image.SCALE_SMOOTH));
		Load.setIcon(loadimage);
		
		About = new JButton("");
		About.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				d.setVisible(true);
			}
		});
		About.setBounds(250, 300,  200, 80);
		About.setBorderPainted(false);
		About.setBackground(Color.black);
		ImageIcon aboutimage=new ImageIcon("images/about_w.png");
		aboutimage.setImage(aboutimage.getImage().getScaledInstance(154, 100, Image.SCALE_SMOOTH));
		About.setIcon(aboutimage);
		
		Exit = new JButton("");
		Exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				System.exit(0);
			}
		});
		Exit.setBounds(450, 300,  200, 80);
		Exit.setBorderPainted(false);
		Exit.setBackground(Color.black);
		ImageIcon exitimage=new ImageIcon("images/exit_w.png");
		exitimage.setImage(exitimage.getImage().getScaledInstance(113, 100, Image.SCALE_SMOOTH));
		Exit.setIcon(exitimage);
		
		p.setBackground(Color.black);
		p.add(Start);
		p.add(Load);
		p.add(About);
		p.add(Exit);
		p.add(logo);
		this.add(p);
		
	}
	Welcome(){
		this.init();
		this.setVisible(true);
	}	
	public static void main(String[] args) throws Exception {
		System.out.println("Planets test version! ");
		Welcome welcome = new Welcome();
		while(true) {
			if(started) {
				break;
			}
		}
		Mainfile galaxy = new Mainfile("Planets in galaxy");
		galaxy.launchFrame();
	}
}

