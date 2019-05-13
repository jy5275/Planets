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
    /* 解决靠近飞出问题：如果两天体距离<mergezone,则在Move时强行让它们放慢移动速度 */
    double maymergezone = 8000;
    double m, x, y, vx, vy;
    Force F;
    LinkedList<Point> log;
    Image self, tail;
    boolean visible, maymerge = false; // this是否与某个天体距离近于Mergezone

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
        /* 如果两Planet靠太近,就强行让他们位移少一点(乘一个小于1的factor) */
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

    /* Mainfile.showT == true ????AddTrace */
    void AddTrace() {
        log.push(new Point(x, y));
    }

    /* ???Planet??, ?????(log), ??!Mainfile.showT, ?log?? */
    void DrawPlanet(Graphics g) {
        for (Point p : log)
            g.drawImage(tail, Mainfile.cvt(p.x), Mainfile.cvt(p.y), null);
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
