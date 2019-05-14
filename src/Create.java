
import java.awt.event.*;

class CreateHuge implements ActionListener {
    Mainfile frame;
    static final double hugem = 6e14;

    public CreateHuge(Mainfile f) {
        frame = f;
    }

    @Override
    /* 鐣岄潰涓婃寜涓媅Huge]鏃惰皟鐢ㄦ鏂规硶,淇敼Mainfile涓弬鏁�,浠ヤ究鍒涘缓涓嬩竴涓ぉ浣� */
    public void actionPerformed(ActionEvent e) {
        frame.DEFAULT_M = hugem;
    }
}

class CreateMid implements ActionListener {
    Mainfile frame;
    static final double midm = 3e13;

    public CreateMid(Mainfile f) {
        frame = f;
    }

    @Override
    /* 鐣岄潰涓婃寜涓媅Meddium]鏃惰皟鐢ㄦ鏂规硶,淇敼Mainfile涓弬鏁�,浠ヤ究鍒涘缓涓嬩竴涓ぉ浣� */
    public void actionPerformed(ActionEvent e) {
        frame.DEFAULT_M = midm;
    }
}

class CreateTiny implements ActionListener {
    Mainfile frame;
    static final double tinym = 1e12;

    public CreateTiny(Mainfile f) {
        frame = f;
    }

    @Override
    /* 鐣岄潰涓婃寜涓媅Tiny]鏃惰皟鐢ㄦ鏂规硶,淇敼Mainfile涓弬鏁�,浠ヤ究鍒涘缓涓嬩竴涓ぉ浣� */
    public void actionPerformed(ActionEvent e) {
        frame.DEFAULT_M = tinym;
    }
}