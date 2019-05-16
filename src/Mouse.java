
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;

public class Mouse implements MouseMotionListener, MouseListener {
    Mainfile frame;
    int gotx, goty; // click閺冭泛鐫嗛獮鏇炴綏閺嶏拷
    double begx, begy; // click閺冭泛鐣ょ�规瑥娼楅弽锟�
    boolean Clicking = false; // 瑜版挸澧犻弰顖氭儊click娴滃棜绻曞▽顡竐lease?
    double zoomadd=0.05;

    public Mouse(Mainfile f) {
        frame = f;
        frame.addMouseListener(this);
        frame.addMouseMotionListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    /* 閹稿绗呮Η鐘崇垼閺冨墎娈戠悰灞艰礋閿涙氨鏁撻幋鎭lanet */
    public void mousePressed(MouseEvent e) {
        gotx = e.getX(); // 閸忓鐖ｇ仦蹇撶閸ф劖鐖�
        goty = e.getY(); // 閸忓鐖ｇ仦蹇撶閸ф劖鐖�
        frame.curx = gotx; // Mouse鐏忓棗缍嬮崜宥呭帨閺嶅洣缍呯純顔荤炊缂佹┇ainfile, 閺傞�涚┒閸氬氦锟藉懐鏁惧鐟扮殸缁撅拷
        frame.cury = goty; // Mouse鐏忓棗缍嬮崜宥呭帨閺嶅洣缍呯純顔荤炊缂佹┇ainfile, 閺傞�涚┒閸氬氦锟藉懐鏁惧鐟扮殸缁撅拷
        Clicking = true;
        /* 閸掓稑缂撴稉顓犳畱vplanet, 鐟曚焦妯夌粈鍝勬躬鐏炲繐绠锋稉锟�, 娴ｅ棔绗塎ove, 娑撳秴褰堥崝锟�, 娑旂喍绗夐弬钘夊 */
        if(!frame.moveS){
            begx = Mainfile.recvt(gotx,true); // 閸忓鐖ｇ�瑰洤鐣介崸鎰垼
            begy = Mainfile.recvt(goty,false); // 閸忓鐖ｇ�瑰洤鐣介崸鎰垼
            frame.vplanet = new Planet(Mainfile.DEFAULT_M, begx, begy, 0, 0,true,frame.STILL);
        }
    }

    @Override
    /* 閺夋儳绱戞Η鐘崇垼閺冨墎娈戠悰灞艰礋: 閸掓稑缂撻弬鐧檒anet, 閸旂姴鍩宲lanets鐎圭懓娅掗柌锟�, vplanet閻╁瓨甯撮幍鏃�甯� */
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
    /* Mouse鐏忓棗缍嬮崜宥呭帨閺嶅洣缍呯純顔荤炊闁帞绮癕ainfile, 閺傞�涚┒閸氬氦锟藉懐鏁惧鐟扮殸缁撅拷 */
    public void mouseDragged(MouseEvent e) {
        if (Clicking) {
            frame.curx = e.getX(); // 娴肩姷娈戦弰顖氱潌楠炴洖娼楅弽锟�, 娑撳秵妲哥�瑰洤鐣介崸鎰垼
            frame.cury = e.getY();
        }
    }
    
    public void mouseWheelMoved(MouseWheelEvent e) {
    	if(e.getWheelRotation()==1) {
    		frame.zoom+=zoomadd;
    		frame.zoomed=true;
    	}
    	if(e.getWheelRotation()==-1) {
    		if(frame.zoom>zoomadd) {
    			frame.zoom-=zoomadd;
    			frame.zoomed=true;
    		}
    	}
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

}