
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Mouse implements MouseMotionListener, MouseListener {
    Mainfile frame;
    int gotx, goty; // click时屏幕坐标
    double begx, begy; // click时宇宙坐标
    boolean Clicking = false; // 当前是否click了还没release?

    public Mouse(Mainfile f) {
        frame = f;
        frame.addMouseListener(this);
        frame.addMouseMotionListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    /* 按下鼠标时的行为：生成vplanet */
    public void mousePressed(MouseEvent e) {
        gotx = e.getX(); // 光标屏幕坐标
        goty = e.getY(); // 光标屏幕坐标
        frame.curx = gotx; // Mouse将当前光标位置传给Mainfile, 方便后者画弹射线
        frame.cury = goty; // Mouse将当前光标位置传给Mainfile, 方便后者画弹射线
        begx = Mainfile.recvt(gotx); // 光标宇宙坐标
        begy = Mainfile.recvt(goty); // 光标宇宙坐标
        Clicking = true;
        /* 创建中的vplanet, 要显示在屏幕上, 但不Move, 不受力, 也不施力 */
        frame.vplanet = new Planet(Mainfile.DEFAULT_M, begx, begy, 0, 0, Mainfile.impath);
    }

    @Override
    /* 松开鼠标时的行为: 创建新Planet, 加到planets容器里, vplanet直接扔掉 */
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
    /* Mouse将当前光标位置传递给Mainfile, 方便后者画弹射线 */
    public void mouseDragged(MouseEvent e) {
        if (Clicking) {
            frame.curx = e.getX(); // 传的是屏幕坐标, 不是宇宙坐标
            frame.cury = e.getY();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

}