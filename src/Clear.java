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

class ShowTrace implements ActionListener {
    Mainfile frame;

    public ShowTrace(Mainfile f) {
        frame = f;
    }

    /* 点ShowTrace ==> 是/否showTrace状态改变,调用Mainfile::ClearTrace方法. */
    public void actionPerformed(ActionEvent e) {
        if (frame.showT) {
            frame.showT = false;
            frame.ClearTrace();
        } else
            frame.showT = true;
    }
}

class MoveScreen implements ActionListener{
    Mainfile frame;

    public MoveScreen(Mainfile f){
        frame = f;
    }

    public void actionPerformed(ActionEvent e){
        frame.moveS = true;
    }
}