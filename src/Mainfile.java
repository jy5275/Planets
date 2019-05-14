import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;


/*
 *	�ܸĵĵط�:
 *	2. ��ͬ���� Planet �ò�ͬ��ɫͼƬ����ʾ
 *	4. ��ӹ���: ���� Planet ��ʾ����
 *	5. ��Ӷ���ԭʼ��ϵģ��
 *	6. ��ӹ���: ǿ�иı�������ʽ
 *	7.* �������ҷ�����
 *  8. ��ͣ����
 */
public class Mainfile extends Frame {
	Image bg = Toolkit.getDefaultToolkit().getImage("images/backg.png");
	BufferedImage planetBF,traceBF;
	Graphics2D planetBG,traceBG;
	ArrayList<Planet> planets;
	Planet vplanet; // ���ڴ����е�Planet,��껹ûrelease
	Mouse m;
	JPanel p;
	JButton cltrbt, clbt, Huge, Mid, Tiny, Show, Move;
	static public int movX=0,movY=0,clickX=0,clickY=0;
	static public double DEFAULT_M = 3e13;
	static int time = 0;
	boolean showT = false, moveS = true, saveMov=false;
	int curx, cury;
	int bgwidth=1646,bgheight=1263;

	Mainfile(String title) {
		super(title);
		Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension scrnsize = toolkit.getScreenSize();
        while(scrnsize.width<bgwidth+200)
        	bgwidth-=256;
        while(scrnsize.height<bgheight-200)
        	bgheight-=256;
		m = new Mouse(this);
		showT = true;
		ClearTrace ct = new ClearTrace(this);
		ClearAll ca = new ClearAll(this);
		CreateHuge chuge = new CreateHuge(this);
		CreateMid cmid = new CreateMid(this);
		CreateTiny ctiny = new CreateTiny(this);
		ShowTrace show = new ShowTrace(this);
		MoveScreen movsc = new MoveScreen(this);

		cltrbt = new JButton("Clear Traces");
		cltrbt.addActionListener(ct);
		cltrbt.setBounds(bgwidth, 100, 120, 80);
		cltrbt.setVisible(true);

		clbt = new JButton("Clear all");
		clbt.addActionListener(ca);
		clbt.setBounds(bgwidth, 220, 120, 80);
		clbt.setVisible(true);

		Huge = new JButton("Huge");
		Huge.addActionListener(chuge);
		Huge.setBounds(bgwidth, 320, 100, 40);
		Huge.setVisible(true);

		Mid = new JButton("Medium");
		Mid.addActionListener(cmid);
		Mid.setBounds(bgwidth, 370, 100, 40);
		Mid.setVisible(true);

		Tiny = new JButton("Tiny");
		Tiny.addActionListener(ctiny);
		Tiny.setBounds(bgwidth, 420, 100, 40);
		Tiny.setVisible(true);

		Show = new JButton("Show Trace");
		Show.addActionListener(show);
		Show.setBounds(bgwidth, 480, 120, 80);
		Show.setVisible(true);

		Move = new JButton("Move");
		Move.addActionListener(movsc);
		Move.setBounds(bgwidth, 600, 120, 80);
		Move.setVisible(true);

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
		p.add(Move);
		
		traceBF = new BufferedImage(bgwidth, bgheight,BufferedImage.TYPE_INT_ARGB);
		planetBF = new BufferedImage(bgwidth, bgheight,BufferedImage.TYPE_INT_ARGB);
		traceBG = traceBF.createGraphics();
		planetBG = planetBF.createGraphics();
		traceBG.setBackground(new Color(0,0,0,0));
		planetBG.setBackground(new Color(0,0,0,0));
        
		setSize(bgwidth+200, bgheight);
		setLocation(50, 50);

		addMouseListener(m);
		add(p);

		setVisible(true); // setVisibleд�����һ��
		p.setVisible(true);
	}

	public void ClearTrace() {
		traceBG.clearRect(0, 0, bgwidth, bgheight);
		for(Planet p:planets) {
			p.log.clear();
		}
	}

	public void ClearAll() { // ����
		planets.clear();
		traceBG.clearRect(0, 0, bgwidth, bgheight);
		vplanet = null;
	}

	/* ��ʵ�������� cvt2 ��Ļ��ʾ���� */
	public static int cvt(double x,boolean ifX) {
		double red = x / (100);
		if(ifX){
			return (int) red + 400 + movX + clickX;
		}
		return (int) red + 400 + movY +clickY;
	}

	/* ��Ļ��ʾ���� cvt2 ��ʵ�������� */
	public static double recvt(int n,boolean ifX) {
		if(ifX){
			return (double) (n - 400 - movX - clickX) * 100;
		}
		return (double) (n - 400 - movY - clickY) * 100;
	}

	/* ��ʾ�����е�Planet */
	public void DrawVplanet(Graphics g) {
		if (vplanet != null) {
			g.setColor(vplanet.drawColor);
			int tmpdiam=vplanet.diam;
			g.fillOval(cvt(vplanet.x,true)-tmpdiam/2, cvt(vplanet.y,false)-tmpdiam/2, tmpdiam, tmpdiam);
		}
			//g.drawImage(vplanet.self, cvt(vplanet.x), cvt(vplanet.y), null);
	}

	public void redrawTrace(){
		for(Planet p: planets)
			p.DrawTrace(traceBG);
	}
	
	/* �ڻ����ϻ��� */
	void paintFG() {
		planetBG.clearRect(0, 0, bgwidth, bgheight);
		double dt = 120; // ʱ�䲽��, ��λ:s
		if (saveMov) {
			movX+=clickX;
			movY+=clickY;
			clickX=0;
			clickY=0;
			saveMov=false;
		}
		if (m.Clicking) { // ������click��ûrelease, �ͻ�������
			if(moveS){
				traceBG.clearRect(0, 0, bgwidth, bgheight);
				clickX=curx-m.gotx;
				clickY=cury-m.goty;
				redrawTrace();
			}
			else{
				planetBG.setColor(vplanet.drawColor);
				planetBG.drawLine(m.gotx, m.goty, curx, cury);
			}
		}
		DrawVplanet(planetBG); // �������е�����, ֻ������, ��������������
		for (Planet p : planets) // ��ÿ������, !visible���ڷ���������⴦��
			p.DrawPlanet(planetBG);
		/* ����ÿ������, ���������ܺ���(Fx, Fy) */
		int planetsnum=planets.size();
		for (int i=0;i<planetsnum;i++) {
			Planet p=planets.get(i);
			if (!(p).visible)
				continue;
			for (int j=i+1;j<planetsnum;j++) {
				Planet q=planets.get(j);
				if (!(q.visible)) // �����Ѿ���merge������
					continue;
				int result=p.MergeOK(q);
				if (result==0) // ���Merge�˾Ͳ�����������, ��Ϊ�������غ���
					p.AddForce(q);
			}
			p.Forced(dt); // p�������ܺ���(Fx, Fy)�ı�p�����ٶ�(vx, vy)
		}

		/* ����ÿ������, ���ٶȼ�����λ�Ƹı� */
		for (Planet p : planets) {
			if (!p.visible) // ���������������
				continue;
			p.Move(dt); // λ�Ƹı䣡
			if (showT) { // ����ʾ�켣, ����ӵ�ǰλ�õ��켣(log)��
				if(p.hasTrace) {
					traceBG.setColor(p.drawColor);
					traceBG.drawLine(p.lastX, p.lastY, cvt(p.x,true), cvt(p.y,false));
				}
				else {
					p.hasTrace=true;
				}
				p.AddTrace();
			}
		}
	}

	private ListIterator<Planet> itp(ArrayList<Planet> planets2) {
		// TODO Auto-generated method stub
		return null;
	}

	/* �����ڵķ���, ÿ���ػ����ڵ���һ��paint */  
	public void paint(Graphics g) {
		paintFG();
		g.drawImage(bg, 0, 0, null); // ������
		g.drawImage(traceBF, 0, 0, null); //���켣
		g.drawImage(planetBF, 0, 0, null); //������
	}
	
	/* ���ڼ��ط���, ����ʱһֱ���������������ѭ�� */
	void launchFrame() throws Exception {
		addWindowListener(new WindowAdapter() { // ���ǵ���رհ�ťʹ������ֹ
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		while (true) { // �����ػ�����, ��ѭ��
			repaint();
			Thread.sleep(1);
		}
	}

	/* ��д update �������Ը��ƻ���, ԭ����Ҳ����, copy from CSDN */
	private Image offScreenImage = null;

	public void update(Graphics g) {
		if (offScreenImage == null)
			offScreenImage = this.createImage(bgwidth, bgheight);
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