import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;


/*
 *	能改的地方:
 *	2. 不同质量 Planet 用不同颜色图片来表示
 *	4. 添加功能: 单击 Planet 显示属性
 *	5. 添加多种原始星系模型
 *	6. 添加功能: 强行改变引力公式
 *	7.* 近距离乱飞问题
 *  8. 暂停功能
 */
public class Mainfile extends Frame {
	Image bg = Toolkit.getDefaultToolkit().getImage("images/backg.png");
	BufferedImage planetBF,traceBF;
	Graphics2D planetBG,traceBG;
	ArrayList<Planet> planets;
	Planet vplanet; // 正在创建中的Planet,鼠标还没release
	Mouse m;
	JPanel p;
	JButton cltrbt, clbt, Huge, Mid, Tiny, Show;
	static public double DEFAULT_M = 3e13;
	static int time = 0;
	boolean showT = false;
	int curx, cury;

	Mainfile(String title) {
		super(title);
		m = new Mouse(this);
		showT = true;
		ClearTrace ct = new ClearTrace(this);
		ClearAll ca = new ClearAll(this);
		CreateHuge chuge = new CreateHuge(this);
		CreateMid cmid = new CreateMid(this);
		CreateTiny ctiny = new CreateTiny(this);
		ShowTrace show = new ShowTrace(this);

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

		Show = new JButton("Show Trace");
		Show.addActionListener(show);
		Show.setBounds(1650, 480, 120, 80);
		Show.setVisible(true);

		planets = new ArrayList<Planet>();
		planets.add(new Planet(DEFAULT_M, 0, 0, 0.6, 0.4,false));
		planets.add(new Planet(DEFAULT_M, 42097, 0, -0.2, 0.2,false));
		planets.add(new Planet(DEFAULT_M, 6097, 52097, -0.6, -0.3,false));
		
		p = new JPanel(null);
		p.setBackground(Color.DARK_GRAY);
		p.add(cltrbt);
		p.add(clbt);
		p.add(Huge);
		p.add(Mid);
		p.add(Tiny);
		p.add(Show);
		
		traceBF = new BufferedImage(1646, 1263,BufferedImage.TYPE_INT_ARGB);
		planetBF = new BufferedImage(1646, 1263,BufferedImage.TYPE_INT_ARGB);
		traceBG = traceBF.createGraphics();
		planetBG = planetBF.createGraphics();
		traceBG.setBackground(new Color(0,0,0,0));
		planetBG.setBackground(new Color(0,0,0,0));
        
		setSize(1846, 1500);
		setLocation(50, 50);

		addMouseListener(m);
		add(p);

		setVisible(true); // setVisible写在最后一句
		p.setVisible(true);
	}

	public void ClearTrace() {
		traceBG.clearRect(0, 0, 1646, 1263);
	}

	public void ClearAll() { // 清屏
		planets.clear();
		traceBG.clearRect(0, 0, 1646, 1263);
		vplanet = null;
	}

	/* 真实宇宙坐标 cvt2 屏幕显示坐标 */
	public static int cvt(double x) {
		double red = x / (100);
		return (int) red + 400;
	}

	/* 屏幕显示坐标 cvt2 真实宇宙坐标 */
	public static double recvt(int n) {
		return (double) (n - 400) * 100;
	}

	/* 显示创建中的Planet */
	public void DrawVplanet(Graphics g) {
		if (vplanet != null) {
			g.setColor(vplanet.drawColor);
			int tmpdiam=vplanet.diam;
			g.fillOval(cvt(vplanet.x)-tmpdiam/2, cvt(vplanet.y)-tmpdiam/2, tmpdiam, tmpdiam);
		}
			//g.drawImage(vplanet.self, cvt(vplanet.x), cvt(vplanet.y), null);
	}
	
	/* 在缓存上绘制 */
	void paintFG() {
		planetBG.clearRect(0, 0, 1646, 1263);
		double dt = 120; // 时间步进, 单位:s
		for (Planet p : planets) // 画每个天体, !visible的在方法里边特殊处理
			p.DrawPlanet(planetBG);
		if (m.Clicking) { // 如果鼠标click还没release, 就画弹射线
			planetBG.setColor(vplanet.drawColor);
			planetBG.drawLine(m.gotx, m.goty, curx, cury);
		}
		DrawVplanet(planetBG); // 画创建中的天体, 只画出来, 不参与引力计算
		/* 遍历每个天体, 计算其所受合力(Fx, Fy) */
		for (Planet p : planets) {
			if (!p.visible)
				continue;
			for (Planet q : planets) {
				if (!q.visible) // 跳过已经被merge的天体
					continue;
				if (p == q) // 自己不对自己施力
					continue;
				if (!p.MergeOK(q)) // 如果Merge了就不算引力增量, 因为动量已守恒了
					p.AddForce(q);
			}
			p.Forced(dt); // p天体所受合力(Fx, Fy)改变p天体速度(vx, vy)
		}

		/* 遍历每个天体, 由速度计算其位移改变 */
		for (Planet p : planets) {
			if (!p.visible) // 死掉的天体就跳过
				continue;
			p.Move(dt); // 位移改变！
			if (showT) { // 若显示轨迹, 则添加当前位置到轨迹(log)中
				if(p.hasTrace) {
					traceBG.setColor(p.drawColor);
					traceBG.drawLine(p.lastX, p.lastY, cvt(p.x), cvt(p.y));
				}
				else {
					p.hasTrace=true;
				}
				p.AddTrace();
			}
		}
	}

	/* 画窗口的方法, 每次重画窗口调用一次paint */  
	public void paint(Graphics g) {
		paintFG();
		g.drawImage(bg, 0, 0, null); // 画背景
		g.drawImage(traceBF, 0, 0, null); //画轨迹
		g.drawImage(planetBF, 0, 0, null); //画行星
	}
	
	/* 窗口加载方法, 运行时一直陷在这个方法里死循环 */
	void launchFrame() throws Exception {
		addWindowListener(new WindowAdapter() { // 除非点击关闭按钮使进程终止
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		while (true) { // 反复重画窗口, 死循环
			repaint();
			Thread.sleep(1);
		}
	}

	/* 重写 update 方法可以改善画质, 原理我也不懂, copy from CSDN */
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
		Mainfile galaxy = new Mainfile("Planets in galaxy");
		galaxy.launchFrame();

	}
}