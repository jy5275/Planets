
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;

public class Mouse implements MouseMotionListener, MouseListener,MouseWheelListener {
    Mainfile frame;
    int gotx, goty;
    double begx, begy;
    boolean Clicking = false;
    double zoommult = 1.04;

    public Mouse(Mainfile f) {
        frame = f;
        frame.addMouseListener(this);
        frame.addMouseMotionListener(this);
        frame.addMouseWheelListener(this);
    }


    @Override
    public void mouseClicked(MouseEvent e) {
		// Valid only under pause status and moving mode (moveS is true)
    	if (frame.menuLevel !=1 || !frame.moveS)
    		return;
		int tmpx = e.getX(), tmpy = e.getY();

		// Iterate all planets to find which one is selected...
    	for (int i = 0; i < frame.planets.size(); i++) {
			Planet p = frame.planets.get(i);
			if (!p.visible)	
				continue;
			int dx = Mainfile.cvt(p.x, true) - tmpx;
			int dy = Mainfile.cvt(p.y, false) - tmpy;
			if(Math.sqrt(dx*dx+dy*dy) < p.diam) {
				frame.selected = true;
				frame.selectedPlanet = p;
				return;
			}
		}
    	frame.selected = false;
	}
	

    @Override
    /*  */
    public void mousePressed(MouseEvent e) {
        gotx = e.getX(); // 
        goty = e.getY(); // 
        frame.curx = gotx; // , 
        frame.cury = goty; // , 
        Clicking = true;
		// Create new planet only not moving mode
		if (!frame.moveS){
            begx = Mainfile.recvt(gotx,true); // 
            begy = Mainfile.recvt(goty,false); // 
            frame.vplanet = new Planet(Mainfile.DEFAULT_M, begx, begy, 0, 0, true, frame.STILL);
        }
    }

    @Override
    /* : , ,  */
    public void mouseReleased(MouseEvent e) {
        int finsX = e.getX();
        int finsY = e.getY();
        double finx = Mainfile.recvt(finsX, true);
        double finy = Mainfile.recvt(finsY, false);
        if (frame.moveS){
            frame.saveMov = true;
        }else{
            double vx_ = (finx - begx) / 20000;
            double vy_ = (finy - begy) / 20000;
            if (frame.STILL) {
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
    /* ,  */
    public void mouseDragged(MouseEvent e) {
		// Fresh current cursor position if mouse do not release
		// Draw drag line for each frame
        if (Clicking) {
            frame.curx = e.getX();
            frame.cury = e.getY();
        }
    }
    
    
    public void mouseWheelMoved(MouseWheelEvent e) {
    	int tmpRo = e.getWheelRotation();
  		Mainfile.zoom *= Math.pow(zoommult, tmpRo);
   		frame.zoomed = true;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

}