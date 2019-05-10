
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Mouse implements MouseMotionListener, MouseListener {
    Mainfile frame;
    TextField tField = new TextField(30);
    Planet cache;
    double begx, begy;

    public Mouse(Mainfile f) {
        frame = f;
        frame.add(tField, "South");
        frame.addMouseListener(this);
        frame.addMouseMotionListener(this);
    }

    public void Add2Frame(Mainfile f_) {
        frame = f_;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        begx = Mainfile.recvt(e.getX());
        begy = Mainfile.recvt(e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        double finx = Mainfile.recvt(e.getX());
        double finy = Mainfile.recvt(e.getY());
        double vx_ = (finx - begx) / 20000;
        double vy_ = (finy - begy) / 20000;
        Planet newp = new Planet(Mainfile.DEFAULT_M, begx, begy, vx_, vy_, "images/moon.png");
        frame.planets.add(newp);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        String string = "鼠标拖动到位置：（" + e.getX() + "，" + e.getY() + "）";
        tField.setText(string);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

}