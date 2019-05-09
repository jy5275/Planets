import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import java.math.*;
import java.util.*;
import java.awt.event.*;

class ClearTrace implements ActionListener {
	Mainfile frame;

	public ClearTrace(Mainfile f) {
		frame = f;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		frame.ClearTrace();
	}
}

class ClearAll implements ActionListener {
	Mainfile frame;

	public ClearAll(Mainfile f) {
		frame = f;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		frame.ClearAll();
	}
}

public class Mainfile extends Frame {
	Image bg = Toolkit.getDefaultToolkit().getImage("images/backg.png");
	LinkedList<Planet> planets;
	Mouse m;
	JPanel p;
	JButton cltrbt, clbt;
	static public final double DEFAULT_M = 3e13;

	Mainfile(String title, Mouse m_) {
		super(title);
		m = m_;
		cltrbt = new JButton("Clear Traces");
		ClearTrace ct = new ClearTrace(this);
		ClearAll ca = new ClearAll(this);
		cltrbt.addActionListener(ct);
		cltrbt.setBounds(1690, 100, 130, 80);
		cltrbt.setVisible(true);

		clbt = new JButton("Clear all");
		clbt.addActionListener(ca);
		clbt.setBounds(1690, 250, 130, 80);
		clbt.setVisible(true);

		planets = new LinkedList<Planet>();
		planets.push(new Planet(5e13, 0, 0, 0, 0, "images/moon.png"));
		planets.push(new Planet(3e13, 42097, 0, 0, 0.1, "images/moon.png"));
		planets.push(new Planet(2e13, 6097, 52097, 0.05, 0.03, "images/moon.png"));

		p = new JPanel(null);
		p.setBackground(Color.DARK_GRAY);
		p.add(cltrbt);
		p.add(clbt);

		setBackground(Color.white);
		setSize(1846, 1500);
		setLocation(50, 50);

		addMouseListener(m);
		add(p);

		setVisible(true); // setVisible写在最后一句
		p.setVisible(true);
	}

	public void ClearTrace() {
		for (Planet p : planets)
			p.log.clear();
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
		double dt = 600; // 10 min
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
			Thread.sleep(10); // 40ms
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
