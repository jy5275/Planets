
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Mouse implements MouseMotionListener, MouseListener {
    Mainfile frame;
    int gotx, goty; // click鏃跺睆骞曞潗鏍�
    double begx, begy; // click鏃跺畤瀹欏潗鏍�
    boolean Clicking = false; // 褰撳墠鏄惁click浜嗚繕娌elease?

    public Mouse(Mainfile f) {
        frame = f;
        frame.addMouseListener(this);
        frame.addMouseMotionListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    /* 鎸変笅榧犳爣鏃剁殑琛屼负锛氱敓鎴恦planet */
    public void mousePressed(MouseEvent e) {
        gotx = e.getX(); // 鍏夋爣灞忓箷鍧愭爣
        goty = e.getY(); // 鍏夋爣灞忓箷鍧愭爣
        frame.curx = gotx; // Mouse灏嗗綋鍓嶅厜鏍囦綅缃紶缁橫ainfile, 鏂逛究鍚庤�呯敾寮瑰皠绾�
        frame.cury = goty; // Mouse灏嗗綋鍓嶅厜鏍囦綅缃紶缁橫ainfile, 鏂逛究鍚庤�呯敾寮瑰皠绾�
        begx = Mainfile.recvt(gotx); // 鍏夋爣瀹囧畽鍧愭爣
        begy = Mainfile.recvt(goty); // 鍏夋爣瀹囧畽鍧愭爣
        Clicking = true;
        /* 鍒涘缓涓殑vplanet, 瑕佹樉绀哄湪灞忓箷涓�, 浣嗕笉Move, 涓嶅彈鍔�, 涔熶笉鏂藉姏 */
        frame.vplanet = new Planet(Mainfile.DEFAULT_M, begx, begy, 0, 0,true);
    }

    @Override
    /* 鏉惧紑榧犳爣鏃剁殑琛屼负: 鍒涘缓鏂癙lanet, 鍔犲埌planets瀹瑰櫒閲�, vplanet鐩存帴鎵旀帀 */
    public void mouseReleased(MouseEvent e) {
        double finx = Mainfile.recvt(e.getX());
        double finy = Mainfile.recvt(e.getY());
        double vx_ = (finx - begx) / 20000;
        double vy_ = (finy - begy) / 20000;
        Planet newp = new Planet(Mainfile.DEFAULT_M, begx, begy, vx_, vy_,false);
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
    /* Mouse灏嗗綋鍓嶅厜鏍囦綅缃紶閫掔粰Mainfile, 鏂逛究鍚庤�呯敾寮瑰皠绾� */
    public void mouseDragged(MouseEvent e) {
        if (Clicking) {
            frame.curx = e.getX(); // 浼犵殑鏄睆骞曞潗鏍�, 涓嶆槸瀹囧畽鍧愭爣
            frame.cury = e.getY();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

}