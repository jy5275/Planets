
import java.awt.event.*;

class CreateHuge implements ActionListener {
    Mainfile frame;
    static final double hugem = 6e14;

    public CreateHuge(Mainfile f) {
        frame = f;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        frame.DEFAULT_M = hugem;
        frame.moveS = false;
    }
}

class CreateMid implements ActionListener {
    Mainfile frame;
    static final double midm = 3e13;

    public CreateMid(Mainfile f) {
        frame = f;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        frame.DEFAULT_M = midm;
        frame.moveS = false;
    }
}

class CreateTiny implements ActionListener {
    Mainfile frame;
    static final double tinym = 1e12;

    public CreateTiny(Mainfile f) {
        frame = f;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        frame.DEFAULT_M = tinym;
        frame.moveS = false;
    }
}
