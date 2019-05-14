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
    Image self, tail;
    boolean visible, maymerge = false; // this鏄惁涓庢煇涓ぉ浣撹窛绂昏繎浜嶮ergezone

    Planet(double m_, double x_, double y_, double vx_, double vy_, String path) {
        m = m_;
        x = x_;
        y = y_;
        vx = vx_;
        vy = vy_;
        F = new Force();
        log = new LinkedList<Point>();
        tail = Toolkit.getDefaultToolkit().getImage("images/tail.png");
        self = Toolkit.getDefaultToolkit().getImage(path);
        visible = true;
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
    	lastX=Mainfile.cvt(x);
    	lastY=Mainfile.cvt(y);
        log.push(new Point(x, y));
    }

    /* ???Planet??, ?????(log), ??!Mainfile.showT, ?log?? */
    void DrawPlanet(Graphics g) {
        if (visible)
            g.drawImage(self, Mainfile.cvt(x), Mainfile.cvt(y), null);
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
                self = p.self;
                x = p.x;
                y = p.y;
            }
            m += p.m;
            p.visible = false;
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
