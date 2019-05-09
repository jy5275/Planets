import java.awt.event.*;

class ClearTrace implements ActionListener {
    Mainfile frame;

    public ClearTrace(Mainfile f) {
        frame = f;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        frame.ClearTrace();
    }
}

class ClearAll implements ActionListener {
    Mainfile frame;

    public ClearAll(Mainfile f) {
        frame = f;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        frame.ClearAll();
    }
}