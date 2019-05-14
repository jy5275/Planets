import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;

import javax.swing.*;
import java.util.*;

class Point {
    double x, y;

    Point(double x_, double y_) {
        x = x_;
        y = y_;
    }
}

class Force {
    double Fx, Fy;

    void clear() {
        Fx = 0;
        Fy = 0;
    }
}

public class Planet {
    double G = 6.67e-11, dx = 2500;
    /* 瑙ｅ喅闈犺繎椋炲嚭闂锛氬鏋滀袱澶╀綋璺濈<mergezone,鍒欏湪Move鏃跺己琛岃瀹冧滑鏀炬參绉诲姩閫熷害 */
    double maymergezone = 8000;
    double m, x, y, vx, vy;
    boolean hasTrace=false;
    int lastX,lastY;
    Force F;
    LinkedList<Point> log;
    Color drawColor;
    int diam;
    boolean visible, maymerge = false; // this鏄惁涓庢煇涓ぉ浣撹窛绂昏繎浜嶮ergezone
    
    Planet(double m_, double x_, double y_, double vx_, double vy_, boolean ifVirtual) {
        m = m_;
        x = x_;
        y = y_;
        vx = vx_;
        vy = vy_;
        setColorAndDiam(ifVirtual);
        F = new Force();
        log = new LinkedList<Point>();
        visible = true;
    }
    
    void setColorAndDiam(boolean ifVirtual) {
    	int tmpAlpha=255;
    	if(ifVirtual) {
    		tmpAlpha=128;
    	}
        if(m>=6e14) {
        	drawColor = new Color(156,38,50,tmpAlpha);
        	diam=25;
        }else if(m>=3e13) {
        	drawColor = new Color(0,90,171,tmpAlpha);
        	diam=20;
        }else {
        	drawColor = new Color(203,203,203,tmpAlpha);
        	diam=15;
        }
    }
    /* ?????? */
    void Forced(double dt) {
        vx += F.Fx * dt / m;
        vy += F.Fy * dt / m;
        F.clear();
    }

    /* ?????? */
    void Move(double dt) {
        double factor = 1;
        /* 濡傛灉涓lanet闈犲お杩�,灏卞己琛岃浠栦滑浣嶇Щ灏戜竴鐐�(涔樹竴涓皬浜�1鐨刦actor) */
        if (maymerge) {
            if (Math.abs(vx) > 10 || Math.abs(vy) > 10)
                factor = 0.3;
            else if (Math.abs(vx) > 7 || Math.abs(vy) > 7)
                factor = 0.6;
            else if (Math.abs(vx) > 4 || Math.abs(vy) > 4)
                factor = 0.85;
        }
        x += vx * dt * factor;
        y += vy * dt * factor;
        maymerge = false;
    }
    
    void AddTrace() {
    	lastX=Mainfile.cvt(x,true);
    	lastY=Mainfile.cvt(y,false);
        log.push(new Point(x, y));
    }

    void DrawTrace(Graphics g){
        ListIterator<Point>it = log.listIterator();
        Point lastP;
        g.setColor(drawColor);
        if(it.hasNext()) {
            lastP = it.next();
        	while(it.hasNext()){
        		Point tmpP=it.next();
        		g.drawLine(Mainfile.cvt(lastP.x, true), Mainfile.cvt(lastP.y, false), Mainfile.cvt(tmpP.x, true), Mainfile.cvt(tmpP.y, false));
        		lastP=tmpP;
        	}
    	}
    }

    /* ???Planet??, ?????(log), ??!Mainfile.showT, ?log?? */
    void DrawPlanet(Graphics g) {
        if (visible) {
        	g.setColor(drawColor);
        	g.fillOval(Mainfile.cvt(x,true)-diam/2, Mainfile.cvt(y,false)-diam/2, diam, diam);
        }
            //g.drawImage(self, Mainfile.cvt(x), Mainfile.cvt(y), null);
    }

    double GetDistance(Planet p) {
        return Math.sqrt(Math.pow(x - p.x, 2) + Math.pow(y - p.y, 2));
    }

    /* this????p??merge, Merge???true, ????false */
    boolean MergeOK(Planet p) {
        double dist = GetDistance(p);
        if (dist < dx) { // Two planets collides!
            // Dong liang shou heng
            vx = (m * vx + p.m * p.vx) / (m + p.m);
            vy = (m * vy + p.m * p.vy) / (m + p.m);
            if (m < p.m) { // the other is heavier!
                x = p.x;
                y = p.y;
                p.m+=m;
                p.setColorAndDiam(false);
                visible=false;
            }
            else{
            	p.x=x;
            	p.y=y;
            	m += p.m;
            	setColorAndDiam(false);
            	p.visible=false;
            }
            return true;
        }
        if (dist < maymergezone) {
            maymerge = true;
        }
        return false;
    }

    /* p?????this?????? */
    void AddForce(Planet p) {
        double dist = GetDistance(p);
        double dFx = G * m * p.m * (p.x - x) / Math.pow(dist, 3);
        double dFy = G * m * p.m * (p.y - y) / Math.pow(dist, 3);
        F.Fx += dFx;
        F.Fy += dFy;
    }

}
