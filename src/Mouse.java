
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;

public class Mouse implements MouseMotionListener, MouseListener,MouseWheelListener {
    Mainfile frame;
    int gotx, goty; // click闁哄啳娉涢惈鍡涚嵁閺囩偞缍忛柡宥忔嫹
    double begx, begy; // click闁哄啳娉涢悾銈囷拷瑙勭懃濞兼寮介敓锟�
    boolean Clicking = false; // 鐟滅増鎸告晶鐘诲及椤栨碍鍎奵lick濞存粌妫滅换鏇炩柦椤＄珢lease?
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
    /* 闁圭顦粭鍛楅悩宕囧灱闁哄啫澧庡▓鎴犳偘鐏炶壈绀嬮柨娑欐皑閺佹捇骞嬮幁顩杔anet */
    public void mousePressed(MouseEvent e) {
        gotx = e.getX(); // 闁稿繐顦伴悥锝囦沪韫囨挾顔庨柛褎鍔栭悥锟�
        goty = e.getY(); // 闁稿繐顦伴悥锝囦沪韫囨挾顔庨柛褎鍔栭悥锟�
        frame.curx = gotx; // Mouse閻忓繐妫楃紞瀣礈瀹ュ懎甯ㄩ柡宥呮矗缂嶅懐绱旈鑽ょ倞缂備焦鈹嘺infile, 闁哄倿锟芥稓鈹掗柛姘唉閿熻棄鎳愰弫鎯ь嚕閻熸壆娈哥紒鎾呮嫹
        frame.cury = goty; // Mouse閻忓繐妫楃紞瀣礈瀹ュ懎甯ㄩ柡宥呮矗缂嶅懐绱旈鑽ょ倞缂備焦鈹嘺infile, 闁哄倿锟芥稓鈹掗柛姘唉閿熻棄鎳愰弫鎯ь嚕閻熸壆娈哥紒鎾呮嫹
        Clicking = true;
        /* 闁告帗绋戠紓鎾寸▔椤撶姵鐣眝planet, 閻熸洑鐒﹀Ο澶岀矆閸濆嫭韬悘鐐茬箰缁犻攱绋夐敓锟�, 濞达絽妫旂粭濉巓ve, 濞戞挸绉磋ぐ鍫ュ礉閿燂拷, 濞戞梻鍠嶇粭澶愬棘閽樺顫� */
        if(!frame.moveS){
            begx = Mainfile.recvt(gotx,true); // 闁稿繐顦伴悥锝囷拷鐟版搐閻ｄ粙宕搁幇顓犲灱
            begy = Mainfile.recvt(goty,false); // 闁稿繐顦伴悥锝囷拷鐟版搐閻ｄ粙宕搁幇顓犲灱
            frame.vplanet = new Planet(Mainfile.DEFAULT_M, begx, begy, 0, 0,true,frame.STILL);
        }
    }

    @Override
    /* 闁哄鍎崇槐鎴炍楅悩宕囧灱闁哄啫澧庡▓鎴犳偘鐏炶壈绀�: 闁告帗绋戠紓鎾诲棘閻ф獟anet, 闁告梻濮撮崺瀹瞝anets閻庡湱鎳撳▍鎺楁煂閿燂拷, vplanet闁烩晛鐡ㄧ敮鎾箥閺冿拷鐢拷 */
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
            Planet newp = new Planet(Mainfile.DEFAULT_M, begx, begy, vx_, vy_,false,frame.STILL);
            frame.planets.add(newp);
            frame.vplanet = null;
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
    /* Mouse閻忓繐妫楃紞瀣礈瀹ュ懎甯ㄩ柡宥呮矗缂嶅懐绱旈鑽ょ倞闂侇偅甯炵划鐧昦infile, 闁哄倿锟芥稓鈹掗柛姘唉閿熻棄鎳愰弫鎯ь嚕閻熸壆娈哥紒鎾呮嫹 */
    public void mouseDragged(MouseEvent e) {
        if (Clicking) {
            frame.curx = e.getX(); // 濞磋偐濮峰▓鎴﹀及椤栨氨娼屾鐐存礀濞兼寮介敓锟�, 濞戞挸绉靛Σ鍝ワ拷鐟版搐閻ｄ粙宕搁幇顓犲灱
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