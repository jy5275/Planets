import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.util.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Mainfile extends Frame {
	BufferedImage bgBF, planetBF, traceBF, informBF;
	Graphics2D bgBG, planetBG, traceBG, informBG;
	ArrayList<Planet> planets;
	Planet vplanet;
	Mouse m;
	JPanel menu;
	JTextField enterFileName;
	JButton cltrbt, clbt, Huge, Mid, Tiny, Show, Move, Pause, Start, Save, Load, Delete, Ret, Model, Model1, Model2,
			Model3;
	JCheckBox Still;
	static public int movX = 0, movY = 0, clickX = 0, clickY = 0;
	static public double zoom = 1.0;
	static public double DEFAULT_M = 3e13;
	static int time = 0;
	public Planet selectedPlanet;
	public boolean showT = false, moveS = true, saveMov = false, STILL = false, zoomed = false, selected = false;
	public int menuLevel = 0;
	int curx, cury;
	static int bgwidth, bgheight;
	int partheight = 50;
	boolean repaintP = true, needRedrawTrace = false;
	static boolean started = false;

	void setStill() {
		STILL = !STILL;
	}

	void addMainMenu() {
		menu.removeAll();
		menu.repaint();
		menu.add(Pause);
		menu.add(cltrbt);
		menu.add(clbt);
		menu.add(Still);
		menu.add(Huge);
		menu.add(Mid);
		menu.add(Tiny);
		menu.add(Show);
		menu.add(Move);
		menu.revalidate();
	}

	void addSecMenu() {
		menu.removeAll();
		menu.repaint();
		menu.add(Start);
		menu.add(Still);
		menu.add(Huge);
		menu.add(Mid);
		menu.add(Tiny);
		menu.add(Move);
		menu.add(Load);
		menu.add(Save);
		menu.add(Delete);
		menu.add(Model);
		menu.revalidate();
	}

	void addSaveMenu() {
		menu.removeAll();
		menu.repaint();
		menu.add(Ret);
		menu.add(enterFileName);
		menu.revalidate();
		moveS = true;
	}

	void addLoadMenu() {
		menu.removeAll();
		menu.repaint();
		File dir = new File("data");
		String[] filelist = dir.list();
		JPanel loadList = new JPanel();
		loadList.setLocation(bgwidth, 5 * partheight);
		loadList.setBackground(Color.BLACK);
		loadList.setPreferredSize(new Dimension(6 * partheight, filelist.length * partheight));
		for (int i = 0; i < filelist.length; i++) {
			System.out.println(filelist[i]);
			LoadGalaxy tmplg = new LoadGalaxy(this, filelist[i]);
			JButton tmpButton = new JButton(filelist[i]);
			loadList.add(tmpButton);
			// tmpButton.setBounds(bgwidth,(5+i)*partheight,6*partheight,partheight);
			tmpButton.setLocation(bgwidth, (5 + i) * partheight);
			tmpButton.setPreferredSize(new Dimension(6 * partheight, partheight));
			tmpButton.setBackground(Color.WHITE);
			tmpButton.setBorderPainted(false);
			tmpButton.addActionListener(tmplg);
		}
		JScrollPane rollList = new JScrollPane(loadList, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		rollList.setBounds(bgwidth, 5 * partheight, 6 * partheight, 30 * partheight);
		rollList.setBorder(null);
		menu.add(rollList);
		menu.add(Ret);
		menu.setPreferredSize(new Dimension(100, 1000));
		menu.revalidate();
		moveS = true;
	}

	void addModelMenu() {
		menu.removeAll();
		menu.repaint();
		menu.add(Model1);
		menu.add(Model2);
		menu.add(Model3);
		menu.add(Ret);
		menu.revalidate();
	}

	Mainfile(String title, int menuL) {
		super(title);
		menuLevel = menuL;
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension scrnsize = toolkit.getScreenSize();
		bgwidth = (int) (scrnsize.width * 0.8);
		bgheight = (int) (scrnsize.height * 0.9);
		partheight = (int) (bgheight / 40);
		m = new Mouse(this);
		showT = true;
		ClearTrace ct = new ClearTrace(this);
		ClearAll ca = new ClearAll(this);
		CreateHuge chuge = new CreateHuge(this);
		CreateMid cmid = new CreateMid(this);
		CreateTiny ctiny = new CreateTiny(this);
		ShowTrace show = new ShowTrace(this);
		MoveScreen movsc = new MoveScreen(this);
		changeMenuAction pact = new changeMenuAction(this, 1);
		changeMenuAction strt = new changeMenuAction(this, 0);
		changeMenuAction saveg = new changeMenuAction(this, 2);
		changeMenuAction loadg = new changeMenuAction(this, 3);
		changeMenuAction modelg = new changeMenuAction(this, 4);
		SaveGalaxy saveF = new SaveGalaxy(this);
		deletePlanet dele = new deletePlanet(this);
		Models model1 = new Models(this, 1);
		Models model2 = new Models(this, 2);
		Models model3 = new Models(this, 3);

		Pause = new JButton("");
		Pause.setBackground(Color.BLACK);
		Pause.addActionListener(pact);
		Pause.setBounds(bgwidth + 3 * partheight, 1 * partheight, 3 * partheight, 3 * partheight);
		Pause.setBorderPainted(false);
		ImageIcon pauseimage = new ImageIcon("images/pause.png");
		pauseimage.setImage(
				pauseimage.getImage().getScaledInstance(3 * partheight, 3 * partheight, Image.SCALE_AREA_AVERAGING));
		Pause.setIcon(pauseimage);

		Start = new JButton("");
		Start.setBackground(Color.BLACK);
		Start.addActionListener(strt);
		Start.setBounds(bgwidth + 3 * partheight, 1 * partheight, 3 * partheight, 3 * partheight);
		Start.setBorderPainted(false);
		ImageIcon startimage = new ImageIcon("images/start.png");
		startimage.setImage(
				startimage.getImage().getScaledInstance(3 * partheight, 3 * partheight, Image.SCALE_AREA_AVERAGING));
		Start.setIcon(startimage);

		Still = new JCheckBox("Still");
		Still.setBackground(Color.BLACK);
		Still.setForeground(Color.WHITE);
		Still.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JCheckBox checkBox = (JCheckBox) e.getSource();
				Object object = new Object();
				object = ((checkBox.getParent().getParent()));
				Mainfile m = (Mainfile) object;
				m.setStill();

			}

		});
		Still.setBounds(bgwidth, 4 * partheight, 6 * partheight, 2 * partheight);

		Huge = new JButton("");
		Huge.setBackground(Color.BLACK);
		Huge.setBorderPainted(false);
		Huge.addActionListener(chuge);
		ImageIcon hugeimage = new ImageIcon("images/huge.png");
		hugeimage.setImage(hugeimage.getImage().getScaledInstance(6 * partheight, 3 * partheight, Image.SCALE_DEFAULT));
		Huge.setIcon(hugeimage);
		Huge.setBounds(bgwidth, 6 * partheight, 6 * partheight, 3 * partheight);

		Mid = new JButton("");
		Mid.setBackground(Color.BLACK);
		Mid.setBorderPainted(false);
		Mid.addActionListener(cmid);
		ImageIcon mediumimage = new ImageIcon("images/medium.png");
		mediumimage.setImage(
				mediumimage.getImage().getScaledInstance(6 * partheight, 3 * partheight, Image.SCALE_DEFAULT));
		Mid.setIcon(mediumimage);
		Mid.setBounds(bgwidth, 10 * partheight, 6 * partheight, 3 * partheight);

		Tiny = new JButton("");
		Tiny.setBackground(Color.BLACK);
		Tiny.setBorderPainted(false);
		Tiny.addActionListener(ctiny);
		ImageIcon tinyimage = new ImageIcon("images/tiny.png");
		tinyimage.setImage(tinyimage.getImage().getScaledInstance(6 * partheight, 3 * partheight, Image.SCALE_DEFAULT));
		Tiny.setIcon(tinyimage);
		Tiny.setBounds(bgwidth, 14 * partheight, 6 * partheight, 3 * partheight);

		Move = new JButton("");
		Move.setBackground(Color.BLACK);
		Move.addActionListener(movsc);
		Move.setBounds(bgwidth, 18 * partheight, 6 * partheight, 3 * partheight);
		Move.setBorderPainted(false);
		ImageIcon moveimage = new ImageIcon("images/move.png");
		moveimage.setImage(
				moveimage.getImage().getScaledInstance(6 * partheight - 10, 3 * partheight, Image.SCALE_DEFAULT));
		Move.setIcon(moveimage);

		clbt = new JButton("");
		clbt.setBackground(Color.BLACK);
		clbt.addActionListener(ca);
		clbt.setBounds(bgwidth, 22 * partheight, 6 * partheight, 3 * partheight);
		clbt.setBorderPainted(false);
		ImageIcon clbtimage = new ImageIcon("images/clear.png");
		clbtimage.setImage(clbtimage.getImage().getScaledInstance(6 * partheight, 3 * partheight, Image.SCALE_DEFAULT));
		clbt.setIcon(clbtimage);

		cltrbt = new JButton("");
		cltrbt.setBackground(Color.BLACK);
		cltrbt.addActionListener(ct);
		cltrbt.setBounds(bgwidth, 26 * partheight, 6 * partheight, 3 * partheight);
		cltrbt.setBorderPainted(false);
		ImageIcon cltrbtimage = new ImageIcon("images/hide.png");
		cltrbtimage.setImage(
				cltrbtimage.getImage().getScaledInstance(6 * partheight, 3 * partheight, Image.SCALE_DEFAULT));
		cltrbt.setIcon(cltrbtimage);

		Show = new JButton("");
		Show.setBackground(Color.BLACK);
		Show.addActionListener(show);
		Show.setBounds(bgwidth, 30 * partheight, 6 * partheight, 3 * partheight);
		Show.setBorderPainted(false);
		ImageIcon showimage = new ImageIcon("images/showtrace.png");
		showimage.setImage(showimage.getImage().getScaledInstance(6 * partheight, 3 * partheight, Image.SCALE_DEFAULT));
		Show.setIcon(showimage);

		Delete = new JButton("");
		Delete.setBackground(Color.BLACK);
		Delete.addActionListener(dele);
		Delete.setBounds(bgwidth, 22 * partheight, 6 * partheight, 3 * partheight);
		Delete.setBorderPainted(false);
		ImageIcon deleteimage = new ImageIcon("images/delete.png");
		deleteimage.setImage(
				deleteimage.getImage().getScaledInstance(6 * partheight, 3 * partheight, Image.SCALE_DEFAULT));
		Delete.setIcon(deleteimage);

		Save = new JButton("");
		Save.setBackground(Color.BLACK);
		Save.addActionListener(saveg);
		Save.setBounds(bgwidth, 26 * partheight, 6 * partheight, 3 * partheight);
		Save.setBorderPainted(false);
		ImageIcon saveimage = new ImageIcon("images/save.png");
		saveimage.setImage(saveimage.getImage().getScaledInstance(6 * partheight, 3 * partheight, Image.SCALE_DEFAULT));
		Save.setIcon(saveimage);

		Load = new JButton("");
		Load.setBackground(Color.BLACK);
		Load.addActionListener(loadg);
		Load.setBounds(bgwidth, 30 * partheight, 6 * partheight, 3 * partheight);
		Load.setBorderPainted(false);
		ImageIcon loadimage = new ImageIcon("images/load.png");
		loadimage.setImage(loadimage.getImage().getScaledInstance(6 * partheight, 3 * partheight, Image.SCALE_DEFAULT));
		Load.setIcon(loadimage);

		enterFileName = new JTextField("");
		enterFileName.setBackground(Color.BLACK);
		enterFileName.setForeground(Color.WHITE);
		enterFileName.setBounds(bgwidth, 6 * partheight, 6 * partheight, (int) (1.5 * partheight));
		enterFileName.setFont(new Font("Microsoft YaHei", Font.LAYOUT_NO_LIMIT_CONTEXT, 20));
		saveF.nameJT = enterFileName;
		enterFileName.addActionListener(saveF);

		Ret = new JButton("Return");
		Ret.addActionListener(pact);
		Ret.setBounds(bgwidth, 2 * partheight, 6 * partheight, 3 * partheight);
		Ret.setBorderPainted(false);
		ImageIcon Retimage = new ImageIcon("images/return.png");
		Retimage.setImage(Retimage.getImage().getScaledInstance(6 * partheight, 3 * partheight, Image.SCALE_DEFAULT));
		Ret.setIcon(Retimage);

		Model = new JButton("Models");
		Model.addActionListener(modelg);
		Model.setBounds(bgwidth, 34 * partheight, 6 * partheight, 3 * partheight);
		Model.setBorderPainted(false);
		ImageIcon Modelimage = new ImageIcon("images/model.png");
		Modelimage
				.setImage(Modelimage.getImage().getScaledInstance(6 * partheight, 3 * partheight, Image.SCALE_DEFAULT));
		Model.setIcon(Modelimage);

		Model1 = new JButton("Model1");
		Model1.addActionListener(model1);
		Model1.setBounds(bgwidth, 6 * partheight, 6 * partheight, 3 * partheight);

		Model2 = new JButton("Model2");
		Model2.addActionListener(model2);
		Model2.setBounds(bgwidth, 10 * partheight, 6 * partheight, 3 * partheight);

		Model3 = new JButton("Model3");
		Model3.addActionListener(model3);
		Model3.setBounds(bgwidth, 14 * partheight, 6 * partheight, 3 * partheight);

		planets = new ArrayList<Planet>();
		planets.add(new Planet(DEFAULT_M, 0, 0, 0.6, 0.4, false, false));
		planets.add(new Planet(DEFAULT_M, 42097, 0, -0.2, 0.2, false, false));
		planets.add(new Planet(DEFAULT_M, 6097, 52097, -0.6, -0.3, false, false));

		menu = new JPanel(null);
		menu.setLocation(bgwidth, 0);
		menu.setBackground(Color.BLACK);
		switch (menuLevel) {
		case 0:
			addMainMenu();
			break;
		case 1:
			addSecMenu();
			break;
		case 2:
			addSaveMenu();
			break;
		case 3:
			addLoadMenu();
			break;
		}

		bgBF = new BufferedImage(bgwidth + 7 * partheight, bgheight, BufferedImage.TYPE_INT_RGB);
		traceBF = new BufferedImage(bgwidth, bgheight, BufferedImage.TYPE_INT_ARGB);
		planetBF = new BufferedImage(bgwidth, bgheight, BufferedImage.TYPE_INT_ARGB);
		informBF = new BufferedImage(bgwidth, bgheight, BufferedImage.TYPE_INT_ARGB);
		bgBG = bgBF.createGraphics();
		traceBG = traceBF.createGraphics();
		planetBG = planetBF.createGraphics();
		informBG = informBF.createGraphics();
		traceBG.setBackground(new Color(0, 0, 0, 0));
		planetBG.setBackground(new Color(0, 0, 0, 0));
		informBG.setBackground(new Color(0, 0, 0, 0));

		setSize(bgwidth + 7 * partheight, bgheight);
		setLocation((int) (scrnsize.width * 0.05), (int) (scrnsize.height * 0.05));

		addMouseListener(m);
		add(menu); // setVisible閸愭瑩鏁撻弬銈嗗闁跨喐鏋婚幏鐑芥晸閹活厺绱幏鐑芥晸閿燂拷
		menu.setVisible(true);
		setVisible(true);
	}

	public void ClearTrace() {
		traceBG.clearRect(0, 0, bgwidth, bgheight);
		for (int i = 0; i < planets.size(); i++) {
			Planet p = planets.get(i);
			p.log.clear();
			if (!p.visible) {
				planets.remove(p);
				i--;
			}
		}
	}

	public void ClearAll() { // 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚�
		planets.clear();
		traceBG.clearRect(0, 0, bgwidth, bgheight);
		vplanet = null;
	}

	/* 闁跨喐鏋婚幏宄扮杽闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹风兘鏁撻弬銈嗗 cvt2 闁跨喐鏋婚幏宄扮闁跨喐鏋婚幏椋庛仛闁跨喐鏋婚幏鐑芥晸閺傘倖瀚� */
	public static int cvt(double x, boolean ifX) {
		double red = x / (100);
		if (ifX) {
			return (int) ((red + movX) * zoom) + clickX + bgwidth / 2;
		}
		return (int) ((red + movY) * zoom) + clickY + bgheight / 2;
	}

	/* 闁跨喐鏋婚幏宄扮闁跨喐鏋婚幏椋庛仛闁跨喐鏋婚幏鐑芥晸閺傘倖瀚� cvt2 闁跨喐鏋婚幏宄扮杽闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹风兘鏁撻弬銈嗗 */
	public static double recvt(int n, boolean ifX) {
		if (ifX) {
			return (double) ((n - clickX - bgwidth / 2) / zoom - movX) * 100;
		}
		return (double) ((n - clickY - bgheight / 2) / zoom - movY) * 100;
	}

	/* 闁跨喐鏋婚幏椋庛仛闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔峰建绾板瀚筆lanet */
	public void DrawVplanet(Graphics g) {
		if (vplanet != null) {
			g.setColor(vplanet.drawColor);
			int tmpdiam = vplanet.diam;
			g.fillOval(cvt(vplanet.x, true) - tmpdiam / 2, cvt(vplanet.y, false) - tmpdiam / 2, tmpdiam, tmpdiam);
		}
		// g.drawImage(vplanet.self, cvt(vplanet.x), cvt(vplanet.y), null);
	}

	public void redrawTrace() {
		traceBG.clearRect(0, 0, bgwidth, bgheight);
		for (Planet p : planets)
			p.DrawTrace(traceBG);
	}

	boolean MergeOK(Planet a, Planet b) {
		double dist = a.GetDistance(b);
		int tmpax = cvt(a.x, true), tmpay = cvt(a.y, false), tmpbx = cvt(b.x, true), tmpby = cvt(b.y, false);
		if (dist < Planet.dx) { // Two planets collides!
			// Dong liang shou heng
			double tmpvx = (a.m * a.vx + b.m * b.vx) / (a.m + b.m);
			double tmpvy = (a.m * a.vy + b.m * b.vy) / (a.m + b.m);
			if (a.m < b.m) { // the other is heavier!
				b.vx = tmpvx;
				b.vy = tmpvy;
				a.x = b.x;
				a.y = b.y;
				if (showT) {
					traceBG.setColor(a.drawColor);
					traceBG.drawLine(tmpax, tmpay, tmpbx, tmpby);
					a.AddTrace();
				}
				b.m += a.m;
				b.setColorAndDiam(false);
				a.visible = false;
				return true;
			} else {
				a.vx = tmpvx;
				a.vy = tmpvy;
				b.x = a.x;
				b.y = a.y;
				if (showT) {
					traceBG.setColor(b.drawColor);
					traceBG.drawLine(tmpax, tmpay, tmpbx, tmpby);
					b.AddTrace();
				}
				a.m += b.m;
				a.setColorAndDiam(false);
				b.visible = false;
				return true;
			}
		}
		if (dist < Planet.maymergezone) {
			a.maymerge = true;
		}
		return false;
	}

	/* 闁跨喕濡导娆愬闁跨喐鏋婚幏鐑芥晸鏉堝啩绱幏鐑芥晸閺傘倖瀚� */
	void paintFG() {
		planetBG.clearRect(0, 0, bgwidth, bgheight);
		double dt = 120; // 閺冨爼鏁撻幋鎺擃劄闁跨喐鏋婚幏锟�, 闁跨喐鏋婚幏铚傜秴:s
		if (saveMov) {
			movX += clickX / zoom;
			movY += clickY / zoom;
			clickX = 0;
			clickY = 0;
			saveMov = false;
			for (Planet p : planets) {
				p.setLast();
			}
			redrawTrace();
		}
		if (zoomed) {
			for (Planet p : planets) {
				p.setLast();
			}
			redrawTrace();
			zoomed = false;
		}
		if (m.Clicking) { // 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹风lick闁跨喐鏋婚幏閿嬬梾release, 闁跨喖鍙烘导娆愬闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹凤拷
			if (moveS) { // 正在移动中
				clickX = curx - m.gotx;
				clickY = cury - m.goty;
				for (Planet p : planets) {
					p.setLast();
				}
				redrawTrace();
			} else { // 创建新天体中
				planetBG.setColor(vplanet.drawColor);
				planetBG.drawLine(m.gotx, m.goty, curx, cury);
			}
		}
		DrawVplanet(planetBG); // 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹风兘鏁撻崣顐ゎ暜閹风兘鏁撻弬銈嗗闁跨喐鏋婚幏锟�, 閸欘亪鏁撻弬銈嗗闁跨喐鏋婚幏鐑芥晸閺傘倖瀚�,
								// 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹风兘鏁撻弬銈嗗闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹凤拷
		for (Planet p : planets) // 闁跨喐鏋婚幏閿嬬槨闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹凤拷,
									// !visible闁跨喐鏋婚幏鐑芥晸閼哄倸鍤栭幏鐑芥晸閺傘倖瀚归柨鐔告灮閹风兘鏁撻弬銈嗗闁跨喐鏋婚幏椋庡祩閿斿吋瀚归柨鐕傛嫹
			p.DrawPlanet(planetBG);
		if (needRedrawTrace) {
			redrawTrace();
			needRedrawTrace = false;
		}
		if (menuLevel == 1) {
			informBG.clearRect(0, 0, bgwidth, bgheight);
			if (selected) {
				int tmpx = cvt(selectedPlanet.x, true), tmpy = cvt(selectedPlanet.y, false);
				informBG.setColor(selectedPlanet.drawColor);
				informBG.drawString("Mass:" + String.valueOf(selectedPlanet.m), tmpx + 30, tmpy);
				informBG.drawString(
						"Speed:" + String.valueOf(Math
								.sqrt(selectedPlanet.vx * selectedPlanet.vx + selectedPlanet.vy * selectedPlanet.vy)),
						tmpx + 30, tmpy + 20);
			}
		}
		if (menuLevel != 0)
			return;

		/*
		 * 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚瑰В蹇涙晸閺傘倖瀚归柨鐔告灮閹风兘鏁撻弬銈嗗,
		 * 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹风兘鏁撻弬銈嗗闁跨喐婢冮悮瀛樺闁跨喐鏋婚幏锟�(Fx, Fy)
		 */
		int planetsnum = planets.size();
		for (int i = 0; i < planetsnum; i++) {
			Planet p = planets.get(i);
			if (!(p).visible)
				continue;
			for (int j = i + 1; j < planetsnum; j++) {
				Planet q = planets.get(j);
				if (!(q.visible)) // 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔虹崵閹惧懏瀚归柨鐔告灮閹风〇erge闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹凤拷
					continue;
				if (!MergeOK(p, q)) // 闁跨喐鏋婚幏鐑芥晸缁插垾rge闁跨喎澹欑亸杈嚋閹风兘鏁撻弬銈嗗闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹风兘鏁撻弬銈嗗,
									// 闁跨喐鏋婚幏铚傝礋闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹风兘鏁撻幋顏嗗皑閹风兘鏁撻弬銈嗗
					p.AddForce(q);
			}
			p.Forced(dt); // p闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹风兘鏁撻弶鎵皑閹风兘鏁撻弬銈嗗(Fx,
							// Fy)闁跨喍鑼庨幉瀣p闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔峰Л鐠佽瀚�(vx, vy)
		}

		/*
		 * 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚瑰В蹇涙晸閺傘倖瀚归柨鐔告灮閹风兘鏁撻弬銈嗗,
		 * 闁跨喐鏋婚幏鐑芥晸閸旑偄瀹崇涵閿嬪闁跨喐鏋婚幏鐑芥晸閺傘倖瀚规担宥夋晸閻欌剝鏁奸幉瀣
		 */
		for (Planet p : planets) {
			if (!p.visible) // 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹风兘鏁撻弬銈嗗闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹风兘鏁撻敓锟�
				continue;
			p.Move(dt); // 娴ｅ秹鏁撻悪鈩冩暭閸欐﹫绱�
			if (showT) { // 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚圭粈娲晸鐏炲﹨鎶�,
							// 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归幏銉╂晸鐟欐帗瀚堟导娆愬閻滎偊鏁撻弬銈嗗閺冩帪鎷�(log)闁跨喐鏋婚幏锟�
				if (p.hasTrace) {
					traceBG.setColor(p.drawColor);
					traceBG.drawLine(p.lastX, p.lastY, cvt(p.x, true), cvt(p.y, false));
				} else {
					p.hasTrace = true;
				}
				p.AddTrace();
			}
		}
	}

	/*
	 * 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔诲Ν閻ㄥ嫬鍤栭幏鐑芥晸閺傘倖瀚�,
	 * 濮ｅ繘鏁撻弬銈嗗闁跨喐鍩呮导娆愬闁跨喐鏋婚幏鐑芥晸閼哄倻顣幏鐑芥晸閺傘倖瀚规稉锟介柨鐔告灮閹风aint
	 */
	public void paint(Graphics g) {
		paintFG();
		g.drawImage(bgBF, 0, 0, null); // 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹凤拷
		g.drawImage(traceBF, 0, 0, null); // 闁跨喐鏋婚幏鐑芥晸鐏炲﹨鎶�
		g.drawImage(planetBF, 0, 0, null); // 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹凤拷
		if (menuLevel == 1) {
			g.drawImage(informBF, 0, 0, null);
		}
		if (repaintP) {
			menu.repaint();
			repaintP = false;
		}
	}

	/*
	 * 闁跨喐鏋婚幏鐑芥晸閼哄倻銆嬮幏鐑芥晸閹搭亜鍤栭幏鐑芥晸閺傘倖瀚�,
	 * 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归弮鏈电閻╂挳鏁撻弬銈嗗闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹风兘鏁撻弬銈嗗
	 * 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔虹崵椤撱劍瀚归柨鐕傛嫹
	 */
	void launchFrame() throws Exception {
		addWindowListener(new WindowAdapter() { // 闁跨喐鏋婚幏鐑芥晸鐟欐帞顣幏鐑芥晸閺傘倖瀚规稊鍥╃伄闁跨喕鍓奸妷濠勬閹风兘鏁撻弬銈嗗闁跨喐鏋婚幏鐑芥晸鐞涙鎷�
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}

			public void windowDeiconified(WindowEvent e) {
				repaintP = true;
			}
		});
		while (true) { // 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔稿焻娴兼瑦瀚归柨鐔告灮閹风兘鏁撻弬銈嗗, 闁跨喐鏋婚幏宄版儕闁跨喐鏋婚幏锟�
			repaint();
			Thread.sleep(1);
		}
	}

	/*
	 * 闁跨喐鏋婚幏宄板晸 update 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹风兘鏁撻惃鍡涙交閹风兘鏁撻悪鈥茬串閹风兘鏁撻弬銈嗗,
	 * 閸樼喖鏁撻弬銈嗗闁跨喐鏋婚幏铚傜瘍闁跨喐鏋婚幏鐑芥晸閺傘倖瀚�, copy from CSDN
	 */
	private Image offScreenImage = null;

	public void update(Graphics g) {
		if (offScreenImage == null)
			offScreenImage = this.createImage(bgwidth, bgheight);
		Graphics gOff = offScreenImage.getGraphics();
		paint(gOff);
		g.drawImage(offScreenImage, 0, 0, null);
	}

}
