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
 *	閿熸澃鏀圭殑鍦板嚖鎷�:
 *	2. 閿熸枻鎷峰悓閿熸枻鎷烽敓鏂ゆ嫹 Planet 閿熺煫璇ф嫹鍚岄敓鏂ゆ嫹鑹插浘鐗囬敓鏂ゆ嫹閿熸枻鎷风ず
 *	4. 閿熸枻鎷峰焊閿熸枻鎷烽敓锟�: 閿熸枻鎷烽敓鏂ゆ嫹 Planet 閿熸枻鎷风ず閿熸枻鎷烽敓鏂ゆ嫹
 *	5. 閿熸枻鎷蜂剑閿熸枻鎷烽敓鐨嗩厓纭锋嫹閿熻緝鐨勶綇鎷烽敓锟�
 *	6. 閿熸枻鎷峰焊閿熸枻鎷烽敓锟�: 寮洪敓鍙敼鎲嬫嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷峰紡
 *	7.* 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鎻嚖鎷烽敓鏂ゆ嫹閿熸枻鎷�
 *  8. 閿熸枻鎷峰仠閿熸枻鎷烽敓鏂ゆ嫹
 */
public class Mainfile extends Frame {
	BufferedImage bgBF,planetBF,traceBF;
	Graphics2D bgBG,planetBG,traceBG;
	ArrayList<Planet> planets;
	Planet vplanet; // 閿熸枻鎷烽敓鑺傝揪鎷烽敓鏂ゆ嫹閿熷彨纰夋嫹Planet,閿熸枻鎷锋槉濮戠备elease
	Mouse m;
	JPanel p;
	JButton cltrbt, clbt, Huge, Mid, Tiny, Show, Move, Pause, Save, Load;
	JCheckBox Still;
	static public int movX=0,movY=0,clickX=0,clickY=0;
	static public double zoom=1.0;
	static public double DEFAULT_M = 3e13;
	static int time = 0;
	public boolean showT = false, moveS = true, saveMov=false,STILL=false,zoomed=false;
	int curx, cury;
	static int bgwidth,bgheight;
	int partheight=50;
	boolean repaintP=true;
	void setStill() {
		STILL=!STILL;
	}
	Mainfile(String title) {
		super(title);
		Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension scrnsize = toolkit.getScreenSize();
        bgwidth=(int)(scrnsize.width*0.8);
        bgheight=(int)(scrnsize.height*0.9);
        partheight=(int)(bgheight/50);
		m = new Mouse(this);
		showT = true;
		ClearTrace ct = new ClearTrace(this);
		ClearAll ca = new ClearAll(this);
		CreateHuge chuge = new CreateHuge(this);
		CreateMid cmid = new CreateMid(this);
		CreateTiny ctiny = new CreateTiny(this);
		ShowTrace show = new ShowTrace(this);
		MoveScreen movsc = new MoveScreen(this);
		SaveGalaxy saveg = new SaveGalaxy(this);
		LoadGalaxy loadg = new LoadGalaxy(this);

		
		cltrbt = new JButton("Clear Traces");
		cltrbt.addActionListener(ct);
		cltrbt.setBounds(bgwidth+30, 2*partheight, 120, 3*partheight);

		clbt = new JButton("Clear all");
		clbt.addActionListener(ca);
		clbt.setBounds(bgwidth+30, 6*partheight, 120, 3*partheight);
		
		Still = new JCheckBox("Still");
		Still.setBackground(Color.BLACK);
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
		Huge.setBackground(Color.BLACK);
		Huge.setBorderPainted(false);
		Huge.addActionListener(chuge);
		ImageIcon hugeimage=new ImageIcon("images/huge.png");
		hugeimage.setImage(hugeimage.getImage().getScaledInstance(6*partheight, 3*partheight, Image.SCALE_DEFAULT));
		Huge.setIcon(hugeimage);
		Huge.setBounds(bgwidth+30, 14*partheight, 6*partheight, 3*partheight);
		

		Mid = new JButton("Medium");
		Mid.setBackground(Color.BLACK);
		Mid.setBorderPainted(false);
		Mid.addActionListener(cmid);
		ImageIcon mediumimage=new ImageIcon("images/medium.png");
		mediumimage.setImage(mediumimage.getImage().getScaledInstance(6*partheight, 3*partheight, Image.SCALE_DEFAULT));
		Mid.setIcon(mediumimage);
		Mid.setBounds(bgwidth+30, 18*partheight, 6*partheight, 3*partheight);
		

		Tiny = new JButton("Tiny");
		Tiny.setBackground(Color.BLACK);
		Tiny.setBorderPainted(false);
		Tiny.addActionListener(ctiny);
		ImageIcon tinyimage=new ImageIcon("images/tiny.png");
		tinyimage.setImage(tinyimage.getImage().getScaledInstance(6*partheight, 3*partheight, Image.SCALE_DEFAULT));
		Tiny.setIcon(tinyimage);
		Tiny.setBounds(bgwidth+30, 22*partheight, 6*partheight, 3*partheight);
		

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
		Save.addActionListener(saveg);
		Save.setBounds(bgwidth+30, 38*partheight, 120, 3*partheight);
		
		Load = new JButton("Load");
		Load.addActionListener(loadg);
		Load.setBounds(bgwidth+30, 42*partheight, 120, 3*partheight);
		

		planets = new ArrayList<Planet>();
		planets.add(new Planet(DEFAULT_M, 0, 0, 0.6, 0.4,false,false));
		planets.add(new Planet(DEFAULT_M, 42097, 0, -0.2, 0.2,false,false));
		planets.add(new Planet(DEFAULT_M, 6097, 52097, -0.6, -0.3,false,false));
		
		p = new JPanel(null);
		p.setBackground(Color.BLACK);
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

		bgBF = new BufferedImage(bgwidth+200,bgheight,BufferedImage.TYPE_INT_RGB);
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
		add(p); // setVisible鍐欓敓鏂ゆ嫹閿熸枻鎷烽敓鎻紮鎷烽敓锟�

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

	public void ClearAll() { // 閿熸枻鎷烽敓鏂ゆ嫹
		planets.clear();
		traceBG.clearRect(0, 0, bgwidth, bgheight);
		vplanet = null;
	}

	/* 閿熸枻鎷峰疄閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹 cvt2 閿熸枻鎷峰箷閿熸枻鎷风ず閿熸枻鎷烽敓鏂ゆ嫹 */
	public static int cvt(double x,boolean ifX) {
		double red = x / (100);
		if(ifX){
			return (int) ((red + movX) * zoom) + clickX + bgwidth/2;
		}
		return (int) ((red + movY) * zoom) +clickY + bgheight/2;
	}

	/* 閿熸枻鎷峰箷閿熸枻鎷风ず閿熸枻鎷烽敓鏂ゆ嫹 cvt2 閿熸枻鎷峰疄閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹 */
	public static double recvt(int n,boolean ifX) {
		if(ifX){
			return (double) ((n - clickX - bgwidth/2) / zoom - movX) * 100;
		}
		return (double) ((n - clickY - bgheight/2) / zoom - movY) * 100;
	}

	/* 閿熸枻鎷风ず閿熸枻鎷烽敓鏂ゆ嫹閿熷彨纰夋嫹Planet */
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
	
	/* 閿熻妭浼欐嫹閿熸枻鎷烽敓杈冧紮鎷烽敓鏂ゆ嫹 */
	void paintFG() {
		planetBG.clearRect(0, 0, bgwidth, bgheight);
		double dt = 120; // 鏃堕敓鎴掓閿熸枻鎷�, 閿熸枻鎷蜂綅:s
		if (saveMov) {
			traceBG.clearRect(0, 0, bgwidth, bgheight);
			movX+=clickX / zoom;
			movY+=clickY / zoom;
			clickX=0;
			clickY=0;
			saveMov=false;
			for(Planet p:planets) {
				p.setLast();
			}
			redrawTrace();
		}
		if(zoomed) {
			traceBG.clearRect(0, 0, bgwidth, bgheight);
			for(Planet p:planets) {
				p.setLast();
			}
			redrawTrace();
			zoomed=false;
		}
		if (m.Clicking) { // 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷穋lick閿熸枻鎷锋病release, 閿熼叺浼欐嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷�
			if(moveS){
				traceBG.clearRect(0, 0, bgwidth, bgheight);
				clickX=curx-m.gotx;
				clickY=cury-m.goty;
				for(Planet p:planets) {
					p.setLast();
				}
				redrawTrace();
			}
			else{
				planetBG.setColor(vplanet.drawColor);
				planetBG.drawLine(m.gotx, m.goty, curx, cury);
			}
		}
		DrawVplanet(planetBG); // 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鍙鎷烽敓鏂ゆ嫹閿熸枻鎷�, 鍙敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹, 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷�
		for (Planet p : planets) // 閿熸枻鎷锋瘡閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷�, !visible閿熸枻鎷烽敓鑺傚嚖鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷风嵂锔兼嫹閿燂拷
			p.DrawPlanet(planetBG);
		/* 閿熸枻鎷烽敓鏂ゆ嫹姣忛敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹, 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸澃鐚存嫹閿熸枻鎷�(Fx, Fy) */
		int planetsnum=planets.size();
		for (int i=0;i<planetsnum;i++) {
			Planet p=planets.get(i);
			if (!(p).visible)
				continue;
			for (int j=i+1;j<planetsnum;j++) {
				Planet q=planets.get(j);
				if (!(q.visible)) // 閿熸枻鎷烽敓鏂ゆ嫹閿熺獤鎾呮嫹閿熸枻鎷穖erge閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷�
					continue;
				if (!MergeOK(p,q)) // 閿熸枻鎷烽敓绲別rge閿熷壙灏辫鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹, 閿熸枻鎷蜂负閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鎴尨鎷烽敓鏂ゆ嫹
					p.AddForce(q);
			}
			p.Forced(dt); // p閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏉扮尨鎷烽敓鏂ゆ嫹(Fx, Fy)閿熶茎鎲嬫嫹p閿熸枻鎷烽敓鏂ゆ嫹閿熷姭璁规嫹(vx, vy)
		}

		/* 閿熸枻鎷烽敓鏂ゆ嫹姣忛敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹, 閿熸枻鎷烽敓鍔害纭锋嫹閿熸枻鎷烽敓鏂ゆ嫹浣嶉敓鐙℃敼鎲嬫嫹 */
		for (Planet p : planets) {
			if (!p.visible) // 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓锟�
				continue;
			p.Move(dt); // 浣嶉敓鐙℃敼鍙橈紒
			if (showT) { // 閿熸枻鎷烽敓鏂ゆ嫹绀洪敓灞婅抗, 閿熸枻鎷烽敓鏂ゆ嫹鎷ラ敓瑙掓嫈浼欐嫹鐜敓鏂ゆ嫹鏃掞拷(log)閿熸枻鎷�
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

	/* 閿熸枻鎷烽敓鏂ゆ嫹閿熻妭鐨勫嚖鎷烽敓鏂ゆ嫹, 姣忛敓鏂ゆ嫹閿熸埅浼欐嫹閿熸枻鎷烽敓鑺傜鎷烽敓鏂ゆ嫹涓�閿熸枻鎷穚aint */  
	public void paint(Graphics g) {
		paintFG();
		g.drawImage(bgBF, 0, 0, null); // 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷�
		g.drawImage(traceBF, 0, 0, null); //閿熸枻鎷烽敓灞婅抗
		g.drawImage(planetBF, 0, 0, null); //閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷�
		if(repaintP) {
			p.repaint();
			repaintP=false;
		}
	}
	
	/* 閿熸枻鎷烽敓鑺傜》鎷烽敓鎴嚖鎷烽敓鏂ゆ嫹, 閿熸枻鎷烽敓鏂ゆ嫹鏃朵竴鐩撮敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熺獤顓ㄦ嫹閿燂拷 */
	void launchFrame() throws Exception {
		addWindowListener(new WindowAdapter() { // 閿熸枻鎷烽敓瑙掔鎷烽敓鏂ゆ嫹涔囩灮閿熻剼銉婄櫢鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓琛楋拷
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			public void windowDeiconified(WindowEvent e) {
				repaintP=true;
			}
		});
		while (true) { // 閿熸枻鎷烽敓鏂ゆ嫹閿熸埅浼欐嫹閿熸枻鎷烽敓鏂ゆ嫹, 閿熸枻鎷峰惊閿熸枻鎷�
			repaint();
			Thread.sleep(1);
		}
	}

	/* 閿熸枻鎷峰啓 update 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鐨嗛潻鎷烽敓鐙′紮鎷烽敓鏂ゆ嫹, 鍘熼敓鏂ゆ嫹閿熸枻鎷蜂篃閿熸枻鎷烽敓鏂ゆ嫹, copy from CSDN */
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
