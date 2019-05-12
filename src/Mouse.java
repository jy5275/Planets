
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Mouse implements MouseMotionListener, MouseListener {
    Mainfile frame;
    TextField tField = new TextField(30);
    Planet cache;
    int gotx, goty;
    double begx, begy;
    boolean Clicking = false;

    public Mouse(Mainfile f) {
        frame = f;
        frame.addMouseListener(this);
        frame.addMouseMotionListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        gotx = e.getX();
        goty = e.getY();
        frame.curx = gotx;
        frame.cury = goty;
        begx = Mainfile.recvt(gotx);
        begy = Mainfile.recvt(goty);
        Clicking = true;
        frame.vplanet = new Planet(Mainfile.DEFAULT_M, begx, begy, 0, 0, Mainfile.impath);
    }

    @Override
    /* 创建一个新Planet, 把它加到planets容器里 */
    public void mouseReleased(MouseEvent e) {
        double finx = Mainfile.recvt(e.getX());
        double finy = Mainfile.recvt(e.getY());
        double vx_ = (finx - begx) / 20000;
        double vy_ = (finy - begy) / 20000;
        Planet newp = new Planet(Mainfile.DEFAULT_M, begx, begy, vx_, vy_, Mainfile.impath);
        frame.planets.add(newp);
        frame.vplanet = null;
        Clicking = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (Clicking) {
            frame.curx = e.getX();
            frame.cury = e.getY();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

}