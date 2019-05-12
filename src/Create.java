
import java.awt.event.*;

class CreateHuge implements ActionListener {
    Mainfile frame;
    static final double hugem = 6e14;
    // String impath = "images/sun.png";

    public CreateHuge(Mainfile f) {
        frame = f;
    }

    @Override
    /* 界面上按下[Huge]时调用此方法,修改Mainfile中参数,以便创建下一个天体 */
    public void actionPerformed(ActionEvent e) {
        frame.DEFAULT_M = hugem;
        frame.impath = "images/sun.png";
    }
}

class CreateMid implements ActionListener {
    Mainfile frame;
    static final double midm = 3e13;
    // String impath = "images/earth.png";

    public CreateMid(Mainfile f) {
        frame = f;
    }

    @Override
    /* 界面上按下[Meddium]时调用此方法,修改Mainfile中参数,以便创建下一个天体 */
    public void actionPerformed(ActionEvent e) {
        frame.DEFAULT_M = midm;
        frame.impath = "images/earth.png";
    }
}

class CreateTiny implements ActionListener {
    Mainfile frame;
    static final double tinym = 1e12;
    // String impath = "images/moon.png";

    public CreateTiny(Mainfile f) {
        frame = f;
    }

    @Override
    /* 界面上按下[Tiny]时调用此方法,修改Mainfile中参数,以便创建下一个天体 */
    public void actionPerformed(ActionEvent e) {
        frame.DEFAULT_M = tinym;
        frame.impath = "images/moon.png";
    }
}