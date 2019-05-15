import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.util.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;


/*
 *	锟杰改的地凤拷:
 *	2. 锟斤拷同锟斤拷锟斤拷 Planet 锟矫诧拷同锟斤拷色图片锟斤拷锟斤拷示
 *	4. 锟斤拷庸锟斤拷锟�: 锟斤拷锟斤拷 Planet 锟斤拷示锟斤拷锟斤拷
 *	5. 锟斤拷佣锟斤拷锟皆硷拷锟较的ｏ拷锟�
 *	6. 锟斤拷庸锟斤拷锟�: 强锟叫改憋拷锟斤拷锟斤拷锟斤拷式
 *	7.* 锟斤拷锟斤拷锟斤拷锟揭凤拷锟斤拷锟斤拷
 *  8. 锟斤拷停锟斤拷锟斤拷
 */
public class Mainfile extends Frame {
	BufferedImage bgBF,planetBF,traceBF;
	Graphics2D bgBG,planetBG,traceBG;
	ArrayList<Planet> planets;
	Planet vplanet; // 锟斤拷锟节达拷锟斤拷锟叫碉拷Planet,锟斤拷昊姑籸elease
	Mouse m;
	JPanel p;
	JButton cltrbt, clbt, Huge, Mid, Tiny, Show, Move, Pause, Save, Load;
	JCheckBox Still;
	static public int movX=0,movY=0,clickX=0,clickY=0;
	static public double DEFAULT_M = 3e13;
	static int time = 0;
	public boolean showT = false, moveS = true, saveMov=false,STILL=false;
	int curx, cury;
	int bgwidth=1920,bgheight=1080,partheight=50;
	void setStill() {
		STILL=!STILL;
	}
	Mainfile(String title) {
		super(title);
		Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension scrnsize = toolkit.getScreenSize();
        bgwidth=scrnsize.width;
        bgwidth=scrnsize.height;
        while(scrnsize.width<bgwidth)
        	bgwidth-=128;
        while(scrnsize.height<bgheight+100)
        	bgheight-=50;
        while(bgheight<46*partheight)
        	partheight-=1;
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
		cltrbt.setBounds(bgwidth+30, 2*partheight, 120, 3*partheight);

		clbt = new JButton("Clear all");
		clbt.addActionListener(ca);
		clbt.setBounds(bgwidth+30, 6*partheight, 120, 3*partheight);
		
		Still = new JCheckBox("Still");
		Still.setForeground(Color.WHITE);
		Still.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				JCheckBox checkBox=(JCheckBox) e.getSource();
				Object object=new Object();
				object = ((checkBox.getParent().getParent()));
				Mainfile m=(Mainfile)object;
				m.setStill();
				
			}
			
		});
		Still.setBounds(bgwidth+30, 10*partheight, 120, 2*partheight);
		
		
		Huge = new JButton("Huge");
		Huge.addActionListener(chuge);
		ImageIcon hugeimage=new ImageIcon("images/huge.png");
		hugeimage.setImage(hugeimage.getImage().getScaledInstance(6*partheight, 3*partheight, Image.SCALE_DEFAULT));
		Huge.setIcon(hugeimage);
		Huge.setBounds(bgwidth+30, 14*partheight, 6*partheight-7, 3*partheight);
		

		Mid = new JButton("Medium");
		Mid.addActionListener(cmid);
		ImageIcon mediumimage=new ImageIcon("images/medium.png");
		mediumimage.setImage(mediumimage.getImage().getScaledInstance(6*partheight, 3*partheight, Image.SCALE_DEFAULT));
		Mid.setIcon(mediumimage);
		Mid.setBounds(bgwidth+30, 18*partheight, 6*partheight-7, 3*partheight);
		

		Tiny = new JButton("Tiny");
		Tiny.addActionListener(ctiny);
		ImageIcon tinyimage=new ImageIcon("images/tiny.png");
		tinyimage.setImage(tinyimage.getImage().getScaledInstance(6*partheight, 3*partheight, Image.SCALE_DEFAULT));
		Tiny.setIcon(tinyimage);
		Tiny.setBounds(bgwidth+30, 22*partheight, 6*partheight-10, 3*partheight);
		

		Show = new JButton("Show Trace");
		Show.addActionListener(show);
		Show.setBounds(bgwidth+30,26*partheight, 120, 3*partheight);
		

		Move = new JButton("Move");
		Move.addActionListener(movsc);
		Move.setBounds(bgwidth+30, 30*partheight, 120, 3*partheight);
		
		Pause = new JButton("Pause");
		Pause.addActionListener(movsc);
		Pause.setBounds(bgwidth+30, 34*partheight, 120, 3*partheight);
		
		Save = new JButton("Save");
		Save.addActionListener(movsc);
		Save.setBounds(bgwidth+30, 38*partheight, 120, 3*partheight);
		
		Load = new JButton("Load");
		Load.addActionListener(movsc);
		Load.setBounds(bgwidth+30, 42*partheight, 120, 3*partheight);
		

		planets = new ArrayList<Planet>();
		planets.add(new Planet(DEFAULT_M, 0, 0, 0.6, 0.4,false,false));
		planets.add(new Planet(DEFAULT_M, 42097, 0, -0.2, 0.2,false,false));
		planets.add(new Planet(DEFAULT_M, 6097, 52097, -0.6, -0.3,false,false));
		
		p = new JPanel(null);
		p.setBackground(new Color(0,0,0,0));
		p.add(cltrbt);
		p.add(clbt);
		p.add(Still);
		p.add(Huge);
		p.add(Mid);
		p.add(Tiny);
		p.add(Show);
		p.add(Move);
		p.add(Pause);
		p.add(Load);
		p.add(Save);
		p.setLocation(bgwidth+20, 200);

		bgBF = new BufferedImage(bgwidth,bgheight,BufferedImage.TYPE_INT_RGB);
		traceBF = new BufferedImage(bgwidth, bgheight,BufferedImage.TYPE_INT_ARGB);
		planetBF = new BufferedImage(bgwidth, bgheight,BufferedImage.TYPE_INT_ARGB);
		bgBG = bgBF.createGraphics();
		traceBG = traceBF.createGraphics();
		planetBG = planetBF.createGraphics();
		traceBG.setBackground(new Color(0,0,0,0));
		planetBG.setBackground(new Color(0,0,0,0));
        
		setSize(bgwidth+200, bgheight);
		setLocation(50, 50);

		addMouseListener(m);
		add(p);
		setVisible(true); // setVisible写锟斤拷锟斤拷锟揭伙拷锟�
		p.repaint();
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

	public void ClearAll() { // 锟斤拷锟斤拷
		planets.clear();
		traceBG.clearRect(0, 0, bgwidth, bgheight);
		vplanet = null;
	}

	/* 锟斤拷实锟斤拷锟斤拷锟斤拷锟斤拷 cvt2 锟斤拷幕锟斤拷示锟斤拷锟斤拷 */
	public static int cvt(double x,boolean ifX) {
		double red = x / (100);
		if(ifX){
			return (int) red + 400 + movX + clickX;
		}
		return (int) red + 400 + movY +clickY;
	}

	/* 锟斤拷幕锟斤拷示锟斤拷锟斤拷 cvt2 锟斤拷实锟斤拷锟斤拷锟斤拷锟斤拷 */
	public static double recvt(int n,boolean ifX) {
		if(ifX){
			return (double) (n - 400 - movX - clickX) * 100;
		}
		return (double) (n - 400 - movY - clickY) * 100;
	}

	/* 锟斤拷示锟斤拷锟斤拷锟叫碉拷Planet */
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

	boolean MergeOK(Planet a,Planet b) {
        double dist = a.GetDistance(b);
        int tmpax=cvt(a.x,true),tmpay=cvt(a.y,false),tmpbx=cvt(b.x,true),tmpby=cvt(b.y,false);
        if (dist < Planet.dx) { // Two planets collides!
            // Dong liang shou heng
            double tmpvx = (a.m * a.vx + b.m * b.vx) / (a.m + b.m);
            double tmpvy = (a.m * a.vy + b.m * b.vy) / (a.m + b.m);
            if (a.m < b.m) { // the other is heavier!
                b.vx=tmpvx;
                b.vy=tmpvy;
                a.x = b.x;
                a.y = b.y;
                if(showT) {
                	traceBG.setColor(a.drawColor);
                	traceBG.drawLine(tmpax,tmpay,tmpbx,tmpby);
                	a.AddTrace();
                }
                b.m+=a.m;
                b.setColorAndDiam(false);
                a.visible=false;
                return true;
            }
            else{
            	a.vx=tmpvx;
            	a.vy=tmpvy;
            	b.x=a.x;
            	b.y=a.y;
                if(showT) {
                	traceBG.setColor(b.drawColor);
                	traceBG.drawLine(tmpax,tmpay,tmpbx,tmpby);
                	b.AddTrace();
                }
            	a.m += b.m;
            	a.setColorAndDiam(false);
            	b.visible=false;
            	return true;
            }
        }
        if (dist < Planet.maymergezone) {
            a.maymerge = true;
        }
        return false;
    }
	
	/* 锟节伙拷锟斤拷锟较伙拷锟斤拷 */
	void paintFG() {
		planetBG.clearRect(0, 0, bgwidth, bgheight);
		double dt = 120; // 时锟戒步锟斤拷, 锟斤拷位:s
		if (saveMov) {
			movX+=clickX;
			movY+=clickY;
			clickX=0;
			clickY=0;
			saveMov=false;
		}
		if (m.Clicking) { // 锟斤拷锟斤拷锟斤拷click锟斤拷没release, 锟酵伙拷锟斤拷锟斤拷锟斤拷
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
		DrawVplanet(planetBG); // 锟斤拷锟斤拷锟斤拷锟叫碉拷锟斤拷锟斤拷, 只锟斤拷锟斤拷锟斤拷, 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
		for (Planet p : planets) // 锟斤拷每锟斤拷锟斤拷锟斤拷, !visible锟斤拷锟节凤拷锟斤拷锟斤拷锟斤拷锟斤拷獯︼拷锟�
			p.DrawPlanet(planetBG);
		/* 锟斤拷锟斤拷每锟斤拷锟斤拷锟斤拷, 锟斤拷锟斤拷锟斤拷锟斤拷锟杰猴拷锟斤拷(Fx, Fy) */
		int planetsnum=planets.size();
		for (int i=0;i<planetsnum;i++) {
			Planet p=planets.get(i);
			if (!(p).visible)
				continue;
			for (int j=i+1;j<planetsnum;j++) {
				Planet q=planets.get(j);
				if (!(q.visible)) // 锟斤拷锟斤拷锟窖撅拷锟斤拷merge锟斤拷锟斤拷锟斤拷
					continue;
				if (!MergeOK(p,q)) // 锟斤拷锟組erge锟剿就诧拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷, 锟斤拷为锟斤拷锟斤拷锟斤拷锟截猴拷锟斤拷
					p.AddForce(q);
			}
			p.Forced(dt); // p锟斤拷锟斤拷锟斤拷锟杰猴拷锟斤拷(Fx, Fy)锟侥憋拷p锟斤拷锟斤拷锟劫讹拷(vx, vy)
		}

		/* 锟斤拷锟斤拷每锟斤拷锟斤拷锟斤拷, 锟斤拷锟劫度硷拷锟斤拷锟斤拷位锟狡改憋拷 */
		for (Planet p : planets) {
			if (!p.visible) // 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�
				continue;
			p.Move(dt); // 位锟狡改变！
			if (showT) { // 锟斤拷锟斤拷示锟届迹, 锟斤拷锟斤拷拥锟角拔伙拷玫锟斤拷旒�(log)锟斤拷
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

	/* 锟斤拷锟斤拷锟节的凤拷锟斤拷, 每锟斤拷锟截伙拷锟斤拷锟节碉拷锟斤拷一锟斤拷paint */  
	public void paint(Graphics g) {
		paintFG();
		g.drawImage(bgBF, 0, 0, null); // 锟斤拷锟斤拷锟斤拷
		g.drawImage(traceBF, 0, 0, null); //锟斤拷锟届迹
		g.drawImage(planetBF, 0, 0, null); //锟斤拷锟斤拷锟斤拷
		this.setVisible(true);
	}
	
	/* 锟斤拷锟节硷拷锟截凤拷锟斤拷, 锟斤拷锟斤拷时一直锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟窖拷锟� */
	void launchFrame() throws Exception {
		addWindowListener(new WindowAdapter() { // 锟斤拷锟角碉拷锟斤拷乇瞻锟脚ナ癸拷锟斤拷锟斤拷锟街�
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		while (true) { // 锟斤拷锟斤拷锟截伙拷锟斤拷锟斤拷, 锟斤拷循锟斤拷
			repaint();
			Thread.sleep(1);
		}
	}

	/* 锟斤拷写 update 锟斤拷锟斤拷锟斤拷锟皆革拷锟狡伙拷锟斤拷, 原锟斤拷锟斤拷也锟斤拷锟斤拷, copy from CSDN */
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