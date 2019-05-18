import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;

import javax.swing.*;
import java.util.*;
import java.math.*;

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
    static double G = 6.67e-11, dx = 2000;
    /* 鐟欙絽鍠呴棃鐘虹箮妞嬬偛鍤梻顕�顣介敍姘洤閺嬫粈琚辨径鈺�缍嬬捄婵堫瀲<mergezone,閸掓瑥婀狹ove閺冭泛宸辩悰宀冾唨鐎瑰啩婊戦弨鐐弮缁夎濮╅柅鐔峰 */
    static double maymergezone = 4000;
    double m, x, y, vx, vy;
    boolean hasTrace = false;
    boolean keepstill = false;
    int lastX, lastY;
    Force F;
    LinkedList<Point> log;
    Color drawColor;
    int diam;
    boolean visible, maymerge = false; // this閺勵垰鎯佹稉搴㈢厙娑擃亜銇夋担鎾圭獩缁傛槒绻庢禍宥甧rgezone

    Planet(double m_, double x_, double y_, double vx_, double vy_, boolean ifVirtual, boolean keepstill_) {
        keepstill = keepstill_;
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
        int tmpAlpha = 255;
        if (ifVirtual)
            tmpAlpha = 128;
        double diamindex = Math.log(m) / Math.log(3e13);
        diamindex = Math.pow(diamindex, 3); // power 3 make diam differ more
        diam = Math.max((int) (diamindex * 15),10);

        double colorindex = Math.pow(diamindex, 3); // heavy-red, light-green
        double red = 255 / (1 + Math.exp(-colorindex + 1));
        double green = 255 - 255 / (1 + Math.exp(-colorindex + 1));
        drawColor = new Color((int) red, (int) green, 20, tmpAlpha);
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
        if (keepstill == true)
            return;
        /* 婵″倹鐏夋稉顥秎anet闂堢姴銇婃潻锟�,鐏忓崬宸辩悰宀冾唨娴犳牔婊戞担宥囆╃亸鎴滅閻愶拷(娑旀ü绔存稉顏勭毈娴滐拷1閻ㄥ垿actor) */
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
    
    void setLast() {
        lastX = Mainfile.cvt(x, true);
        lastY = Mainfile.cvt(y, false);
    }

    void AddTrace() {
    	setLast();
        log.push(new Point(x, y));
    }

    void DrawTrace(Graphics g) {
        ListIterator<Point> it = log.listIterator();
        Point lastP;
        g.setColor(drawColor);
        if (it.hasNext()) {
            lastP = it.next();
            while (it.hasNext()) {
                Point tmpP = it.next();
                g.drawLine(Mainfile.cvt(lastP.x, true), Mainfile.cvt(lastP.y, false), Mainfile.cvt(tmpP.x, true),
                        Mainfile.cvt(tmpP.y, false));
                lastP = tmpP;
            }
        }
    }

    /* ???Planet??, ?????(log), ??!Mainfile.showT, ?log?? */
    void DrawPlanet(Graphics g) {
        if (visible) {
            g.setColor(drawColor);
            g.fillOval(Mainfile.cvt(x, true) - diam / 2, Mainfile.cvt(y, false) - diam / 2, diam, diam);
        }
        // g.drawImage(self, Mainfile.cvt(x), Mainfile.cvt(y), null);
    }

    double GetDistance(Planet p) {
        return Math.sqrt(Math.pow(x - p.x, 2) + Math.pow(y - p.y, 2));
    }

    /* this????p??merge, Merge???true, ????false */
    /*
     * implement mergeok func if merge into p return 1, p merges into itself, return
     * 2, else return 0;
     */
    /* p?????this?????? */
    void AddForce(Planet p) {
        double dist = GetDistance(p);
        double dFx = G * m * p.m * (p.x - x) / Math.pow(dist, 3);
        double dFy = G * m * p.m * (p.y - y) / Math.pow(dist, 3);
        F.Fx += dFx;
        F.Fy += dFy;
        p.F.Fx-=dFx;
        p.F.Fy-=dFy;
    }

}
