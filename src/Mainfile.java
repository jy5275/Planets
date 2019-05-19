import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.util.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Mainfile extends Frame {
	BufferedImage bgBF, planetBF, traceBF, informBF;
	Graphics2D bgBG, planetBG, traceBG, informBG;
	Planet tmpplanet;
	ArrayList<Planet> planets;
	Planet vplanet;
	Mouse m;
	JPanel menu;
	JTextField enterFileName;
	JButton cltrbt, clbt, Huge, Mid, Tiny, Show, Move, Pause, Start, SaveLoad, Delete, Ret, Model, Model1, Model2,
			Model3;
	JCheckBox Still;
	static public int movX = 0, movY = 0, clickX = 0, clickY = 0;
	static public double zoom = 1.0;
	static public double DEFAULT_M = 3e13;
	static int time = 0;
	public Planet selectedPlanet;
	public boolean showT = false, moveS = true, saveMov = false, STILL = false, zoomed = false, selected = false,planetadded=false;
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
		menu.add(SaveLoad);
		menu.add(Delete);
		menu.add(Model);
		menu.revalidate();
	}

	void addSaveMenu() {
		menu.removeAll();
		menu.repaint();
		menu.add(Ret);
		menu.revalidate();
		moveS = true;
	}

	void addLoadMenu() {
		menu.removeAll();
		menu.repaint();
		File dir = new File("data");
		String[] filelist = dir.list();
		JPanel loadList = new JPanel();
		loadList.setLocation(bgwidth, 8 * partheight);
		loadList.setBackground(Color.BLACK);
		loadList.setPreferredSize(new Dimension(6 * partheight, (int)(filelist.length * 4.3 * partheight)));
		for (int i = 0; i < filelist.length; i++) {
			LoadGalaxy tmplg = new LoadGalaxy(this, filelist[i]);
			JButton tmpButton = new JButton("");
			loadList.add(tmpButton);
			tmpButton.setLocation(bgwidth, (8 + i) * partheight);
			tmpButton.setPreferredSize(new Dimension(6 * partheight, 4*partheight));
			ImageIcon tmpimage = new ImageIcon("screenshot/"+filelist[i]+".png");
			File test =new File("screenshot/"+filelist[i]+".png");
			tmpButton.setBackground(Color.BLACK);
			tmpButton.setForeground(Color.WHITE);
			if(!test.exists()) {
				tmpButton.setText(filelist[i]);
			}
			else {
				tmpimage.setImage(
					tmpimage.getImage().getScaledInstance(6 * partheight, 4 * partheight, Image.SCALE_SMOOTH));
			}
			tmpButton.setIcon(tmpimage);
			tmpButton.setBorderPainted(false);
			tmpButton.addActionListener(tmplg);
		}
		JScrollPane rollList = new JScrollPane(loadList, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		if(filelist.length*4.3>26*partheight)
			rollList.setBounds(bgwidth, 8 * partheight, 6*partheight+15, 26 * partheight);
		else
			rollList.setBounds(bgwidth, 8 * partheight, 6*partheight, 26 * partheight);
		rollList.setBorder(null);
		rollList.getVerticalScrollBar().setUI(new DemoScrollBarUI());
		menu.add(Ret);
		menu.add(enterFileName);
		menu.add(rollList);
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
		partheight = (int) (bgheight / 36);
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
		changeMenuAction loadg = new changeMenuAction(this, 2);
		changeMenuAction modelg = new changeMenuAction(this, 3);
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
		hugeimage.setImage(hugeimage.getImage().getScaledInstance(6 * partheight, 3 * partheight, Image.SCALE_SMOOTH));
		Huge.setIcon(hugeimage);
		Huge.setBounds(bgwidth, 6 * partheight, 6 * partheight, 3 * partheight);

		Mid = new JButton("");
		Mid.setBackground(Color.BLACK);
		Mid.setBorderPainted(false);
		Mid.addActionListener(cmid);
		ImageIcon mediumimage = new ImageIcon("images/medium.png");
		mediumimage.setImage(
				mediumimage.getImage().getScaledInstance(6 * partheight, 3 * partheight, Image.SCALE_SMOOTH));
		Mid.setIcon(mediumimage);
		Mid.setBounds(bgwidth, 10 * partheight, 6 * partheight, 3 * partheight);

		Tiny = new JButton("");
		Tiny.setBackground(Color.BLACK);
		Tiny.setBorderPainted(false);
		Tiny.addActionListener(ctiny);
		ImageIcon tinyimage = new ImageIcon("images/tiny.png");
		tinyimage.setImage(tinyimage.getImage().getScaledInstance(6 * partheight, 3 * partheight, Image.SCALE_SMOOTH));
		Tiny.setIcon(tinyimage);
		Tiny.setBounds(bgwidth, 14 * partheight, 6 * partheight, 3 * partheight);

		Move = new JButton("");
		Move.setBackground(Color.BLACK);
		Move.addActionListener(movsc);
		Move.setBounds(bgwidth, 18 * partheight, 6 * partheight, 3 * partheight);
		Move.setBorderPainted(false);
		ImageIcon moveimage = new ImageIcon("images/move.png");
		moveimage.setImage(
				moveimage.getImage().getScaledInstance(6 * partheight - 10, 3 * partheight, Image.SCALE_SMOOTH));
		Move.setIcon(moveimage);

		clbt = new JButton("");
		clbt.setBackground(Color.BLACK);
		clbt.addActionListener(ca);
		clbt.setBounds(bgwidth, 22 * partheight, 6 * partheight, 3 * partheight);
		clbt.setBorderPainted(false);
		ImageIcon clbtimage = new ImageIcon("images/clear.png");
		clbtimage.setImage(clbtimage.getImage().getScaledInstance(6 * partheight, 3 * partheight, Image.SCALE_SMOOTH));
		clbt.setIcon(clbtimage);

		cltrbt = new JButton("");
		cltrbt.setBackground(Color.BLACK);
		cltrbt.addActionListener(ct);
		cltrbt.setBounds(bgwidth, 26 * partheight, 6 * partheight, 3 * partheight);
		cltrbt.setBorderPainted(false);
		ImageIcon cltrbtimage = new ImageIcon("images/hide.png");
		cltrbtimage.setImage(
				cltrbtimage.getImage().getScaledInstance(6 * partheight, 3 * partheight, Image.SCALE_SMOOTH));
		cltrbt.setIcon(cltrbtimage);

		Show = new JButton("");
		Show.setBackground(Color.BLACK);
		Show.addActionListener(show);
		Show.setBounds(bgwidth, 30 * partheight, 6 * partheight, 3 * partheight);
		Show.setBorderPainted(false);
		ImageIcon showimage = new ImageIcon("images/showtrace.png");
		showimage.setImage(showimage.getImage().getScaledInstance(6 * partheight, 3 * partheight, Image.SCALE_SMOOTH));
		Show.setIcon(showimage);

		Delete = new JButton("");
		Delete.setBackground(Color.BLACK);
		Delete.addActionListener(dele);
		Delete.setBounds(bgwidth, 22 * partheight, 6 * partheight, 3 * partheight);
		Delete.setBorderPainted(false);
		ImageIcon deleteimage = new ImageIcon("images/delete.png");
		deleteimage.setImage(
				deleteimage.getImage().getScaledInstance(6 * partheight, 3 * partheight, Image.SCALE_SMOOTH));
		Delete.setIcon(deleteimage);

		SaveLoad = new JButton("");
		SaveLoad.setBackground(Color.BLACK);
		SaveLoad.addActionListener(loadg);
		SaveLoad.setBounds(bgwidth, 26 * partheight, 6 * partheight, 3 * partheight);
		SaveLoad.setBorderPainted(false);
		ImageIcon loadimage = new ImageIcon("images/load.png");
		loadimage.setImage(loadimage.getImage().getScaledInstance(6 * partheight, 3 * partheight, Image.SCALE_SMOOTH));
		SaveLoad.setIcon(loadimage);

		enterFileName = new JTextField("");
		enterFileName.setBackground(Color.BLACK);
		enterFileName.setForeground(Color.WHITE);
		enterFileName.setBounds(bgwidth, 6 * partheight, 6 * partheight,  partheight);
		enterFileName.setFont(new Font("Microsoft YaHei", Font.LAYOUT_NO_LIMIT_CONTEXT, 20));
		saveF.nameJT = enterFileName;
		enterFileName.addActionListener(saveF);

		Ret = new JButton("");
		Ret.addActionListener(pact);
		Ret.setBounds(bgwidth, 2 * partheight, 6 * partheight, 3 * partheight);
		Ret.setBorderPainted(false);
		ImageIcon Retimage = new ImageIcon("images/return.png");
		Retimage.setImage(Retimage.getImage().getScaledInstance(6 * partheight, 3 * partheight, Image.SCALE_SMOOTH));
		Ret.setIcon(Retimage);

		Model = new JButton("");
		Model.addActionListener(modelg);
		Model.setBounds(bgwidth, 30 * partheight, 6 * partheight, 3 * partheight);
		Model.setBorderPainted(false);
		ImageIcon Modelimage = new ImageIcon("images/model.png");
		Modelimage
				.setImage(Modelimage.getImage().getScaledInstance(6 * partheight, 3 * partheight, Image.SCALE_SMOOTH));
		Model.setIcon(Modelimage);

		Model1 = new JButton("");
		Model1.addActionListener(model1);
		Model1.setBounds(bgwidth, 6 * partheight, 6 * partheight, 4 * partheight);
		Model1.setBorderPainted(false);
		ImageIcon Model1image = new ImageIcon("images/model1.png");
		Model1image
				.setImage(Model1image.getImage().getScaledInstance(6 * partheight, 4 * partheight, Image.SCALE_SMOOTH));
		Model1.setIcon(Model1image);
		
		Model2 = new JButton("");
		Model2.addActionListener(model2);
		Model2.setBounds(bgwidth, 10 * partheight, 6 * partheight, 4 * partheight);
		Model2.setBorderPainted(false);
		ImageIcon Model2image = new ImageIcon("images/model2.png");
		Model2image
				.setImage(Model2image.getImage().getScaledInstance(6 * partheight, 4 * partheight, Image.SCALE_SMOOTH));
		Model2.setIcon(Model2image);

		Model3 = new JButton("");
		Model3.addActionListener(model3);
		Model3.setBounds(bgwidth, 14 * partheight, 6 * partheight, 4 * partheight);
		Model3.setBorderPainted(false);
		ImageIcon Model3image = new ImageIcon("images/model3.png");
		Model3image
				.setImage(Model3image.getImage().getScaledInstance(6 * partheight, 4 * partheight, Image.SCALE_SMOOTH));
		Model3.setIcon(Model3image);

		planets = new ArrayList<Planet>();
		planets.add(new Planet(DEFAULT_M, 0, 0, 0.6, 0.4, false, false));
		planets.add(new Planet(DEFAULT_M, 42097, 0, -0.2, 0.2, false, false));
		planets.add(new Planet(DEFAULT_M, 6097, 52097, -0.6, -0.3, false, false));

		menu = new JPanel(null);
		menu.setLocation(bgwidth, 0);
		menu.setBackground(Color.BLACK);
		switch (menuLevel) {
			case 0:addMainMenu();break;
			case 1:addSecMenu();break;
			case 2:addLoadMenu();break;
			case 3:addModelMenu();break;
		}

		bgBF = new BufferedImage(bgwidth + 8 * partheight, bgheight, BufferedImage.TYPE_INT_RGB);
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

		setSize(bgwidth + 8 * partheight, bgheight);
		setLocation((int) (scrnsize.width * 0.05), (int) (scrnsize.height * 0.05));

		addMouseListener(m);
		add(menu); // setVisible闂佸憡鍔栭悷鈺呭极閹捐妫橀柕鍫濇椤忓爼姊虹捄銊ユ瀾闁哄顭烽獮蹇涙倻閼恒儲娅㈤梺瑙勬た閸樿櫣妲愰锟介獮蹇涙倻閼恒儲娅㈤梺璺ㄥ櫐閹凤拷
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

	public void ClearAll() { // 闂備浇娉曢崰鎰板几婵犳艾绠柣鎴ｅГ閺呮悂鏌￠崒妯猴拷鏍拷姘炬嫹
		planets.clear();
		traceBG.clearRect(0, 0, bgwidth, bgheight);
		vplanet = null;
	}

	/* 闂備浇娉曢崰鎰板几婵犳艾绠�瑰嫭澹嗛弶浠嬫⒑鐠恒劌鏋戦柡瀣煼楠炲繘鎮滈懞銉︽闂佸搫鍊堕崐鏍拷姘秺閺屻劑鎮㈤崨濠勪紕闂佸綊顥撻崗姗�寮幘璇叉闁靛牆妫楅锟� cvt2 闂備浇娉曢崰鎰板几婵犳艾绠�瑰嫭澹嗛搴ㄦ⒑鐠恒劌鏋戦柡瀣煼楠炲繑顦版惔娑楃驳闂備浇娉曢崰鎰板几婵犳艾绠柣鎴ｅГ閺呮悂鏌￠崒妯猴拷鏍拷姘炬嫹 */
	public static int cvt(double x, boolean ifX) {
		double red = x / (100);
		if (ifX) {
			return (int) ((red + movX) * zoom) + clickX + bgwidth / 2;
		}
		return (int) ((red + movY) * zoom) + clickY + bgheight / 2;
	}

	/* 闂備浇娉曢崰鎰板几婵犳艾绠�瑰嫭澹嗛搴ㄦ⒑鐠恒劌鏋戦柡瀣煼楠炲繑顦版惔娑楃驳闂備浇娉曢崰鎰板几婵犳艾绠柣鎴ｅГ閺呮悂鏌￠崒妯猴拷鏍拷姘炬嫹 cvt2 闂備浇娉曢崰鎰板几婵犳艾绠�瑰嫭澹嗛弶浠嬫⒑鐠恒劌鏋戦柡瀣煼楠炲繘鎮滈懞銉︽闂佸搫鍊堕崐鏍拷姘秺閺屻劑鎮㈤崨濠勪紕闂佸綊顥撻崗姗�寮幘璇叉闁靛牆妫楅锟� */
	public static double recvt(int n, boolean ifX) {
		if (ifX) {
			return (double) ((n - clickX - bgwidth / 2) / zoom - movX) * 100;
		}
		return (double) ((n - clickY - bgheight / 2) / zoom - movY) * 100;
	}

	/* 闂備浇娉曢崰鎰板几婵犳艾绠瀣昂娴犳盯姊虹捄銊ユ瀾闁哄顭烽獮蹇涙倻閼恒儲娅㈤梺鍝勫�堕崐鏍拷姘秺閺屻劑鎮㈠畡鏉跨紦缂佺偓婢橀ˇ鎵拷姘辩搸lanet */
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

	/* 闂備浇娉曢崰鏇熶繆椤撶喎顕辨繛鍡樺姇椤忓爼姊虹捄銊ユ瀾闁哄顭烽獮蹇涙倻閼恒儲娅㈤柡澶婄墕閸熲晝妲愰锟介獮蹇涙倻閼恒儲娅㈤梺鍝勫�堕崐鏍拷姘炬嫹 */
	void paintFG() {
		planetBG.clearRect(0, 0, bgwidth, bgheight);
		double dt = 120;
		if(planetadded) {
			planets.add(tmpplanet);
			planetadded=false;
		}
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
		if (m.Clicking) {
			if (moveS) {
				clickX = curx - m.gotx;
				clickY = cury - m.goty;
				for (Planet p : planets) {
					p.setLast();
				}
				redrawTrace();
			} else { // 閸掓稑缂撻弬鏉裤亯娴ｆ挷鑵�
				planetBG.setColor(vplanet.drawColor);
				planetBG.drawLine(m.gotx, m.goty, curx, cury);
			}
		}
		DrawVplanet(planetBG); 
		for (Planet p : planets)
									
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
		 * 闂備浇娉曢崰鎰板几婵犳艾绠柣鎴ｅГ閺呮悂鏌￠崒妯猴拷鏍拷姘辨嚀琚欓煫鍥ㄧ♁閺呮悂鏌￠崒妯猴拷鏍拷姘秺閺屻劑鎮㈤崨濠勪紕闂佸綊顥撻崗姗�寮幘璇叉闁靛牆妫楅锟�,
		 * 闂備浇娉曢崰鎰板几婵犳艾绠柣鎴ｅГ閺呮悂鏌￠崒妯猴拷鏍拷姘秺閺屻劑鎮㈤崨濠勪紕闂佸綊顥撻崗姗�寮幘璇叉闁靛牆妫楅鍫曟⒑鐠恒劌鏋戞繝銏犲暣閹喚锟芥稒锚椤忓爼姊虹捄銊ユ瀾闁哄顭烽獮蹇涙晸閿燂拷(Fx, Fy)
		 */
		int planetsnum = planets.size();
		for (int i = 0; i < planetsnum; i++) {
			Planet p = planets.get(i);
			if (!(p).visible)
				continue;
			for (int j = i + 1; j < planetsnum; j++) {
				Planet q = planets.get(j);
				if (!(q.visible)) // 闂備浇娉曢崰鎰板几婵犳艾绠柣鎴ｅГ閺呮悂鏌￠崒妯猴拷鏍拷姘秺閺屻劑鎮㈤搹鐟板惞闂佽鍎抽幊蹇曪拷姘秺閺屻劑鎮㈤崨濠勪紕闂佸綊顥撻妴鍣�rge闂備浇娉曢崰鎰板几婵犳艾绠柣鎴ｅГ閺呮悂鏌￠崒妯猴拷鏍拷姘秺閺屻劑鎮㈤崨濠勪紕闂佺懓鍤栭幏锟�
					continue;
				if (!MergeOK(p, q)) // 闂備浇娉曢崰鎰板几婵犳艾绠柣鎴ｅГ閺呭摜绱掗幓鎺戠伋rge闂備浇娉曢崰搴㈢珶濞嗘垳鐒婇弶鍫亽閸ゅ鏌熸搴″幋闁轰焦鎹囧顒勫Χ閸℃浼撻梻浣芥硶閸犳劙寮告繝姘闁绘垼濮ら弲鎼佹煛閸屾ê锟芥牜锟芥艾缍婇弻銊╂偄閸涘﹦浼勯梺褰掝棑閸忔﹢寮幘璇叉闁靛牆妫楅锟�,
									// 闂備浇娉曢崰鎰板几婵犳艾绠柧姘�界粈瀣⒑鐠恒劌鏋戦柡瀣煼楠炲繘鎮滈懞銉︽闂佸搫鍊堕崐鏍拷姘秺閺屻劑鎮㈤崨濠勪紕闂佸綊顥撻崗姗�寮幘璇茬妞ゅ繐妫楅惃鎴︽煙妞嬪骸鍘撮柡浣规崌瀵剟濡堕崱妤婁紦
					p.AddForce(q);
			}
			p.Forced(dt); // p闂備浇娉曢崰鎰板几婵犳艾绠柣鎴ｅГ閺呮悂鏌￠崒妯猴拷鏍拷姘秺閺屻劑鎮㈤崨濠勪紕闂佸綊顥撻崗姗�寮幘璇茬骇闁规鍠栭惃鎴︽煙妞嬪骸鍘撮柡浣规崌瀵剟濡堕崱妤婁紦(Fx,
							// Fy)闂備浇娉曢崰宥夋嚑鎼淬劌绠熼悗锝庡亜椤忕闂備浇娉曢崰鎰板几婵犳艾绠柣鎴ｅГ閺呮悂鏌￠崒妯猴拷鏍拷姘秺閺屻劑鎮㈠畡闈舵盯鎮规担绛嬫綈閻庢熬鎷�(vx, vy)
		}

		/*
		 * 闂備浇娉曢崰鎰板几婵犳艾绠柣鎴ｅГ閺呮悂鏌￠崒妯猴拷鏍拷姘辨嚀琚欓煫鍥ㄧ♁閺呮悂鏌￠崒妯猴拷鏍拷姘秺閺屻劑鎮㈤崨濠勪紕闂佸綊顥撻崗姗�寮幘璇叉闁靛牆妫楅锟�,
		 * 闂備浇娉曢崰鎰板几婵犳艾绠柣鎴ｅГ閺呮悂鏌涢弮鎴濅簻閻庣懓纾☉鐢告煥鐎ｎ亶浼撻梻浣芥硶閸犳劙寮告繝姘闁绘垼濮ら弲鎼佹煛閸屾ê锟芥牜锟芥俺顫夐幏鍛吋婢跺娅㈤梺缁樼憿閸撴繈寮總绋跨疅閻庯綆鍋勯锟�
		 */
		for (Planet p : planets) {
			if (!p.visible) // 闂備浇娉曢崰鎰板几婵犳艾绠柣鎴ｅГ閺呮悂鏌￠崒妯猴拷鏍拷姘秺閺屻劑鎮㈤崨濠勪紕闂佸綊顥撻崗姗�寮幘璇叉闁靛牆妫楅鍫曟⒑鐠恒劌鏋戦柡瀣煼楠炲繘鎮滈懞銉︽闂佸搫鍊堕崐鏍拷姘秺閺屻劑鎮㈤崨濠勪紕闂佸綊顥撻崗姗�寮幘缁樻櫢闁跨噦鎷�
				continue;
			p.Move(dt); // 婵炶揪绲界粔褰掑极閹剧粯鍋戦柍鈺佸暞閺嗩參鏌涘▎鎰跺叕缂佹唻鎷�
			if (showT) { // 闂備浇娉曢崰鎰板几婵犳艾绠柣鎴ｅГ閺呮悂鏌￠崒妯猴拷鏍拷姘贡缁牆煤椤忓懏娅㈤柣蹇曞仜閿曘劑骞庨敓锟�,
							// 闂備浇娉曢崰鎰板几婵犳艾绠柣鎴ｅГ閺呮悂鏌￠崒妯猴拷鏍拷姘秺楠炲繘濡烽埡鍌涙闁荤喐鐟︾敮妤冿拷姘墛鐎电厧鈻庨幇顒変紦闂佺粯绮犻崑濠囧极閹捐妫橀柕鍫濇椤忓爼鏌￠崘鈺侀嚋闁瑰嚖鎷�(log)闂備浇娉曢崰鎰板几婵犳艾绠柨鐕傛嫹
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
	 * 闂備浇娉曢崰鎰板几婵犳艾绠柣鎴ｅГ閺呮悂鏌￠崒妯猴拷鏍拷姘秺閺屻劑鎮㈢拠鍙傛繈鏌ｉ妸銉ヮ仼闁搞倖鐗犻獮蹇涙倻閼恒儲娅㈤梺鍝勫�堕崐鏍拷姘炬嫹,
	 * 濠殿噯绲界换姗�寮幘璇叉闁靛牆妫楅鍫曟⒑鐠恒劌鏋戦柛鈺佹噺鐎电厧鈻庨幇顒変紦闂備浇娉曢崰鎰板几婵犳艾绠柣鎴ｅГ閺呮悂鏌ら崫鍕拷濠氥�傞锟介獮蹇涙倻閼恒儲娅㈤梺鍝勫�堕崐鏍拷姘愁潐缁嬪鏁撴禒瀣叆闁绘柨鎲￠悘顕�鏌熸搴吋aint
	 */
	public void paint(Graphics g) {
		paintFG();
		g.drawImage(bgBF, 0, 0, null); // 闂備浇娉曢崰鎰板几婵犳艾绠柣鎴ｅГ閺呮悂鏌￠崒妯猴拷鏍拷姘秺閺屻劑鎮㈤崨濠勪紕闂佺懓鍤栭幏锟�
		g.drawImage(traceBF, 0, 0, null); // 闂備浇娉曢崰鎰板几婵犳艾绠柣鎴ｅГ閺呮悂鎮橀悙璇ч練闁硅鎷�
		g.drawImage(planetBF, 0, 0, null); // 闂備浇娉曢崰鎰板几婵犳艾绠柣鎴ｅГ閺呮悂鏌￠崒妯猴拷鏍拷姘秺閺屻劑鎮㈤崨濠勪紕闂佺懓鍤栭幏锟�
		if (menuLevel == 1) {
			g.drawImage(informBF, 0, 0, null);
		}
		if (repaintP) {
			menu.repaint();
			repaintP = false;
		}
	}
	
	public void generateScreenShot(String name) throws IOException {
		BufferedImage screenshot;	
		int fontsize=300;
		screenshot = new BufferedImage(bgwidth, bgheight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D screenshotg=screenshot.createGraphics();
       		screenshotg.setBackground(new Color(0,0,0,0));
		screenshotg.drawImage(bgBF, 0, 0, null);
		
		screenshotg.drawImage(planetBF, 0, 0, null);
		screenshotg.drawImage(traceBF, 0, 0, null);
		screenshotg.setColor(Color.white);
		Font font=new Font("Microsoft YaHei",Font.PLAIN,300);
       		screenshotg.setFont(font); 
        	FontMetrics fm = screenshotg.getFontMetrics(font);
        	int textWidth = fm.stringWidth(name);
        	while(textWidth+300>bgwidth) {
        		fontsize*=0.9;
        		font=new Font("Microsoft YaHei",Font.PLAIN,fontsize);
        		fm = screenshotg.getFontMetrics(font);
        		textWidth=fm.stringWidth(name);
        		screenshotg.setFont(font); 
       		}
        	int textHeight = 100;
        	int widthX = (bgwidth - textWidth) / 2;
        	int heightY = (bgheight - textHeight)/2;
        	screenshotg.drawString(name,widthX,heightY);
		File outputfile = new File("screenshot/"+name+".png");
		ImageIO.write(screenshot,"png",outputfile);
	}

	/*
	 * 闂備浇娉曢崰鎰板几婵犳艾绠柣鎴ｅГ閺呮悂鏌ら崫鍕拷濠氬Υ鐎ｎ喖绠柣鎴ｅГ閺呮悂鏌熼幖顓濈盎闁搞倖鐗犻獮蹇涙倻閼恒儲娅㈤梺鍝勫�堕崐鏍拷姘炬嫹,
	 * 闂備浇娉曢崰鎰板几婵犳艾绠柣鎴ｅГ閺呮悂鏌￠崒妯猴拷鏍拷姘秺瀵噣寮甸悽娈夸紘闂佺儵鏅滈幐鎶藉极閹捐妫橀柕鍫濇椤忓爼姊虹捄銊ユ瀾闁哄顭烽獮蹇涙倻閼恒儲娅㈤梺鍝勫�堕崐鏍拷姘秺閺屻劑鎮㈤崨濠勪紕闂佸綊顥撻崗姗�寮幘璇叉闁靛牆妫楅锟�
	 * 闂備浇娉曢崰鎰板几婵犳艾绠柣鎴ｅГ閺呮悂鏌￠崒妯猴拷鏍拷姘秺閺屻劑鎮㈤搹鐟板惞濡炪倖鎸堕崝宥囷拷姘秺閺屻劑鎮ら崒娑橆伓
	 */
	void launchFrame() throws Exception {
		addWindowListener(new WindowAdapter() { // 闂備浇娉曢崰鎰板几婵犳艾绠柣鎴ｅГ閺呮悂鎮峰▎鎰妞わ綆鍣ｉ獮蹇涙倻閼恒儲娅㈤梺鍝勫�堕崐鏍拷姘愁潐缁嬪﹪宕堕埡鍐х磿闂備浇娉曢崰鏇㈠礈婵傜唯濠电姴瀚鐔兼煙妞嬪骸鍘撮柡浣规崌瀵剟濡堕崱妤婁紦闂備浇娉曢崰鎰板几婵犳艾绠柣鎴ｅГ閺呮悂鎮跺☉娆樻闁瑰嚖鎷�
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}

			public void windowDeiconified(WindowEvent e) {
				repaintP = true;
			}
		});
		while (true) { // 闂備浇娉曢崰鎰板几婵犳艾绠柣鎴ｅГ閺呮悂鏌￠崒妯猴拷鏍拷姘秺閺屻劑鎮㈢粙璺ㄥ姷婵炴潙鍚嬮悷锔撅拷姘秺閺屻劑鎮㈤崨濠勪紕闂佸綊顥撻崗姗�寮幘璇叉闁靛牆妫楅锟�, 闂備浇娉曢崰鎰板几婵犳艾绠�瑰嫮澧楅崕鏇㈡⒑鐠恒劌鏋戦柡瀣煼楠炲繘鏁撻敓锟�
			repaint();
			Thread.sleep(1);
		}
	}

	/*
	 * 闂備浇娉曢崰鎰板几婵犳艾绠�瑰嫭婢橀弲锟� update 闂備浇娉曢崰鎰板几婵犳艾绠柣鎴ｅГ閺呮悂鏌￠崒妯猴拷鏍拷姘秺閺屻劑鎮㈤崨濠勪紕闂佸綊顥撻崗姗�寮幘缁樺剭闁糕剝绋掓禍銈夋煙妞嬪骸鍘撮柡浣规崌閹亪鍨鹃懠顑胯闂佸綊顥撻崗姗�寮幘璇叉闁靛牆妫楅锟�,
	 * 闂佸憡顭囬崰鏍极閹捐妫橀柕鍫濇椤忓爼姊虹捄銊ユ瀾闁哄顭烽獮蹇涙憥閸屾粎妲忛梻浣芥硶閸犳劙寮告繝姘闁绘垼濮ら弲鎼佹煛閸屾ê锟芥牜锟芥熬鎷�, copy from CSDN
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
