
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;

public class Mouse implements MouseMotionListener, MouseListener,MouseWheelListener {
    Mainfile frame;
    int gotx, goty; // click闂佸搫鍟冲▔娑㈡儓閸℃稓宓侀柡鍥╁仦缂嶅繘鏌″蹇斿
    double begx, begy; // click闂佸搫鍟冲▔娑㈡偩閵堝浄鎷风憴鍕噧婵炲吋顨婂浠嬫晸閿燂拷
    boolean Clicking = false; // 閻熸粎澧楅幐鍛婃櫠閻樿鍙婃い鏍ㄧ閸庡サlick婵炲瓨绮屽Λ婊呮崲閺囩偐鏌︽い锛勭彚lease?
    double zoommult=1.04;

    public Mouse(Mainfile f) {
        frame = f;
        frame.addMouseListener(this);
        frame.addMouseMotionListener(this);
        frame.addMouseWheelListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    	if(frame.menuLevel!=1|!frame.moveS)
    		return;
    	int tmpx=e.getX(),tmpy=e.getY();
    	for (int i = 0; i < frame.planets.size(); i++) {
			Planet p = frame.planets.get(i);
			if (!p.visible)	
				continue;
			int dx=Mainfile.cvt(p.x, true)-tmpx,dy=Mainfile.cvt(p.y, false)-tmpy;
			if(Math.sqrt(dx*dx+dy*dy)<p.diam) {
				frame.selected=true;
				frame.selectedPlanet=p;
				return;
			}
		}
    	frame.selected=false;
    }

    @Override
    /* 闂佸湱顭堥ˇ顔剧箔閸涱啣妤呮偐瀹曞洤鐏遍梺鍝勫暙婢у骸鈻撻幋鐘冲仒閻忕偠澹堢粈瀣煥濞戞瑦鐨戦柡浣规崌楠炲骞侀々鏉攁net */
    public void mousePressed(MouseEvent e) {
        gotx = e.getX(); // 闂佺绻愰ˇ浼存偉閿濆洣娌煫鍥ㄦ尵椤斿酣鏌涜閸旀牠鎮ラ敓锟�
        goty = e.getY(); // 闂佺绻愰ˇ浼存偉閿濆洣娌煫鍥ㄦ尵椤斿酣鏌涜閸旀牠鎮ラ敓锟�
        frame.curx = gotx; // Mouse闁诲繐绻愬Λ妤冪礊鐎ｎ喖绀堢�广儱鎳庣敮銊╂煛瀹ュ懏鐭楃紓宥呮噽缁辨棃顢欓懡銈囧�炵紓鍌欑劍閳瑰樅infile, 闂佸搫鍊块敓鑺ョ〒閳规帡鏌涘顒佸攭闁跨喕妫勯幊鎰板极閹屽殨闁荤喐澹嗗▓鍝ョ磼閹惧懏瀚�
        frame.cury = goty; // Mouse闁诲繐绻愬Λ妤冪礊鐎ｎ喖绀堢�广儱鎳庣敮銊╂煛瀹ュ懏鐭楃紓宥呮噽缁辨棃顢欓懡銈囧�炵紓鍌欑劍閳瑰樅infile, 闂佸搫鍊块敓鑺ョ〒閳规帡鏌涘顒佸攭闁跨喕妫勯幊鎰板极閹屽殨闁荤喐澹嗗▓鍝ョ磼閹惧懏瀚�
        Clicking = true;
        /* 闂佸憡甯楃粙鎴犵磽閹惧鈻旀い鎾跺У閻ｇ湞planet, 闁荤喐娲戦悞锕�螣婢跺瞼鐭嗛柛婵嗗闊剟鎮橀悙鑼缂佺娀鏀辩粙澶愭晸閿燂拷, 婵炶揪绲藉Λ鏃傜箔婵夊窊ve, 婵炴垶鎸哥粔纾嬨亹閸儱绀夐柨鐕傛嫹, 婵炴垶姊婚崰宥囩箔婢舵劕妫橀柦妯侯槸椤拷 */
        if(!frame.moveS){
            begx = Mainfile.recvt(gotx,true); // 闂佺绻愰ˇ浼存偉閿濆浄鎷烽悷鐗堟悙闁伙絼绮欏畷鎼佸箛椤撶姴鐏�
            begy = Mainfile.recvt(goty,false); // 闂佺绻愰ˇ浼存偉閿濆浄鎷烽悷鐗堟悙闁伙絼绮欏畷鎼佸箛椤撶姴鐏�
            frame.vplanet = new Planet(Mainfile.DEFAULT_M, begx, begy, 0, 0,true,frame.STILL);
        }
    }

    @Override
    /* 闂佸搫顦伴崕宕囨閹寸倣妤呮偐瀹曞洤鐏遍梺鍝勫暙婢у骸鈻撻幋鐘冲仒閻忕偠澹堢粈锟�: 闂佸憡甯楃粙鎴犵磽閹捐妫橀柣褎鐛焌net, 闂佸憡姊绘慨鎾春鐎圭灊anets闁诲骸婀遍幊鎾斥枍閹烘鐓傞柨鐕傛嫹, vplanet闂佺儵鏅涢悺銊ф暜閹绢喖绠ラ柡鍐挎嫹閻㈩垽鎷� */
    public void mouseReleased(MouseEvent e) {
        int finsX = e.getX();
        int finsY = e.getY();
        double finx = Mainfile.recvt(finsX,true);
        double finy = Mainfile.recvt(finsY,false);
        if(frame.moveS){
            frame.saveMov=true;
        }else{
            double vx_ = (finx - begx) / 20000;
            double vy_ = (finy - begy) / 20000;
            if(frame.STILL) {
            	vx_=0;
            	vy_=0;
            }
            frame.tmpplanet = new Planet(Mainfile.DEFAULT_M, begx, begy, vx_, vy_,false,frame.STILL);
            frame.vplanet = null;
            frame.planetadded=true;
        }
        Clicking = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    /* Mouse闁诲繐绻愬Λ妤冪礊鐎ｎ喖绀堢�广儱鎳庣敮銊╂煛瀹ュ懏鐭楃紓宥呮噽缁辨棃顢欓懡銈囧�為梻渚囧亝鐢偟鍒掗惂鏄nfile, 闂佸搫鍊块敓鑺ョ〒閳规帡鏌涘顒佸攭闁跨喕妫勯幊鎰板极閹屽殨闁荤喐澹嗗▓鍝ョ磼閹惧懏瀚� */
    public void mouseDragged(MouseEvent e) {
        if (Clicking) {
            frame.curx = e.getX(); // 婵炵鍋愭慨宄扳枔閹达箑鍙婃い鏍ㄦ皑濞煎本顨ラ悙瀛樼婵炲吋顨婂浠嬫晸閿燂拷, 婵炴垶鎸哥粔闈浳ｉ崫銉嫹閻熺増鎼愰柣锝勭矙瀹曟悂骞囬鐘茬伇
            frame.cury = e.getY();
        }
    }
    
    
    public void mouseWheelMoved(MouseWheelEvent e) {
    	int tmpRo=e.getWheelRotation();
  		Mainfile.zoom*=Math.pow(zoommult, tmpRo);
   		frame.zoomed=true;

    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

}