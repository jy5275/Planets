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

    /* 閻愮瓖howTrace ==> 閺勶拷/閸氼洐howTrace閻樿埖锟戒焦鏁奸崣锟�,鐠嬪啰鏁ainfile::ClearTrace閺傝纭�. */
    public void actionPerformed(ActionEvent e) {
        if (frame.showT) {
            frame.showT = false;
            frame.ClearTrace();
        } else{
            frame.showT = true;
            for(Planet p:frame.planets){
                p.AddTrace();
            }
        }
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

class changeMenuAction implements ActionListener{
	Mainfile frame;
	int level;
	public changeMenuAction(Mainfile f,int a) {
		frame = f;
		level=a;
	}
	public void actionPerformed(ActionEvent e) {
		frame.menuLevel=level;
		frame.selected=false;
		switch(level) {
		case 0:frame.addMainMenu();break;
		case 1:frame.addSecMenu();break;
		case 2:frame.addSaveMenu();break;
		case 3:frame.addLoadMenu();break;
		case 4:frame.addModelMenu();break;
		}
	}
}

class deletePlanet implements ActionListener{
	Mainfile frame;
	public deletePlanet(Mainfile f) {
		frame=f;
	}
	public void actionPerformed(ActionEvent e) {
		if(frame.selected) {
			frame.planets.remove(frame.selectedPlanet);
			frame.selected=false;
			frame.needRedrawTrace=true;
		}
	}
}
