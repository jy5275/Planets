import java.awt.*;
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
    final double G = 6.67e-11, dx = 1700;
    double m, x, y, vx, vy;
    Force F;
    LinkedList<Point> log;
    Image self, tail;
    boolean visible;

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

    void Forced(double dt) {
        vx += F.Fx * dt / m;
        vy += F.Fy * dt / m;
        F.clear();
    }

    void Move(double dt) {
        x += vx * dt;
        y += vy * dt;
    }

    void AddTrace() {
        log.push(new Point(x, y));
    }

    void DrawPlanet(Graphics g) {
        if (visible)
            g.drawImage(self, Mainfile.cvt(x), Mainfile.cvt(y), null);
        for (Point p : log)
            g.drawImage(tail, Mainfile.cvt(p.x), Mainfile.cvt(p.y), null);
    }

    double GetDistance(Planet p) {
        return Math.sqrt(Math.pow(x - p.x, 2) + Math.pow(y - p.y, 2));
    }

    boolean MergeOK(Planet p) {
        if (GetDistance(p) < dx) {
            // x y 动量守恒
            vx = (m * vx + p.m * p.vx) / (m + p.m);
            vy = (m * vy + p.m * p.vy) / (m + p.m);
            m += p.m;
            p.visible = false;
            return true;
        }
        return false;
    }

    void AddForce(Planet p) {
        double dist = GetDistance(p);
        double dFx = G * m * p.m * (p.x - x) / Math.pow(dist, 3);
        double dFy = G * m * p.m * (p.y - y) / Math.pow(dist, 3);
        F.Fx += dFx;
        F.Fy += dFy;
    }
}
