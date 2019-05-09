import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import java.util.*;
import java.awt.event.*;

public class Mainfile extends Frame {
	Image bg = Toolkit.getDefaultToolkit().getImage("images/backg.png");
	ArrayList<Planet> planets;
	Mouse m;
	JPanel p;
	JButton cltrbt, clbt, Huge, Mid, Tiny;
	static public double DEFAULT_M = 3e13;
	static int time = 0;

	Mainfile(String title, Mouse m_) {
		super(title);
		m = m_;

		ClearTrace ct = new ClearTrace(this);
		ClearAll ca = new ClearAll(this);
		CreateHuge chuge = new CreateHuge(this);
		CreateMid cmid = new CreateMid(this);
		CreateTiny ctiny = new CreateTiny(this);

		cltrbt = new JButton("Clear Traces");
		cltrbt.addActionListener(ct);
		cltrbt.setBounds(1650, 100, 120, 80);
		cltrbt.setVisible(true);

		clbt = new JButton("Clear all");
		clbt.addActionListener(ca);
		clbt.setBounds(1650, 220, 120, 80);
		clbt.setVisible(true);

		Huge = new JButton("Huge");
		Huge.addActionListener(chuge);
		Huge.setBounds(1670, 320, 100, 40);
		Huge.setVisible(true);

		Mid = new JButton("Medium");
		Mid.addActionListener(cmid);
		Mid.setBounds(1670, 370, 100, 40);
		Mid.setVisible(true);

		Tiny = new JButton("Tiny");
		Tiny.addActionListener(ctiny);
		Tiny.setBounds(1670, 420, 100, 40);
		Tiny.setVisible(true);

		planets = new ArrayList<Planet>();
		planets.add(new Planet(5e13, 0, 0, 0, 0, "images/moon.png"));
		planets.add(new Planet(3e13, 42097, 0, 0, 0.1, "images/moon.png"));
		planets.add(new Planet(2e13, 6097, 52097, 0.05, 0.03, "images/moon.png"));

		p = new JPanel(null);
		p.setBackground(Color.DARK_GRAY);
		p.add(cltrbt);
		p.add(clbt);
		p.add(Huge);
		p.add(Mid);
		p.add(Tiny);

		setBackground(Color.white);
		setSize(1846, 1500);
		setLocation(50, 50);

		addMouseListener(m);
		add(p);

		setVisible(true); // setVisible写在最后一句
		p.setVisible(true);
	}

	public void ClearTrace() {
		for (int i = 0; i < planets.size(); i++) {
			Planet p = planets.get(i);
			p.log.clear();
			if (!p.visible) {
				planets.remove(p);
				i--;
			}
		}
	}

	public void ClearAll() {
		planets.clear();
	}

	public static int cvt(double x) {
		double red = x / (100);
		return (int) red + 400;
	}

	public static double recvt(int n) {
		return (double) (n - 400) * 100;
	}

	// 画窗口的方法
	public void paint(Graphics g) {
		g.drawImage(bg, 0, 0, null);
		double dt = 150; // 10 min

		for (Planet p : planets)
			p.DrawPlanet(g);

		for (Planet p : planets) {
			if (!p.visible)
				continue;
			for (Planet q : planets) {
				if (!q.visible)
					continue;
				if (p == q)
					continue;
				if (!p.MergeOK(q))
					p.AddForce(q);
			}
			p.Forced(dt);
		}
		for (Planet p : planets) {
			if (!p.visible)
				continue;
			p.Move(dt);
			p.AddTrace();
		}
	}

	// 窗口加载
	void launchFrame() throws Exception {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		while (true) { // 重画窗口, 25次/s
			repaint();
			Thread.sleep(2); // 40ms
		}
	}

	private Image offScreenImage = null;

	public void update(Graphics g) {
		if (offScreenImage == null)
			offScreenImage = this.createImage(1646, 1263);
		Graphics gOff = offScreenImage.getGraphics();
		paint(gOff);
		g.drawImage(offScreenImage, 0, 0, null);
	}

	public static void main(String[] args) throws Exception {
		System.out.println("Planets test version! ");
		Mouse m = new Mouse();
		Mainfile galaxy = new Mainfile("Planets in galaxy", m);
		m.Add2Frame(galaxy);
		galaxy.launchFrame();
	}
}
