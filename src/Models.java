import java.awt.event.*;
import java.io.*;

class Models implements ActionListener {
	Mainfile frame;
	int num;
	
	public Models(Mainfile f, int n) {
		frame = f;
		num = n;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		frame.ClearAll();
		if (num == 1) {
			frame.planets.add(new Planet(6e14, 0, 0, 0, 0.09093181534,false,false));
			frame.planets.add(new Planet(6e13, 40000, 0, 0, -0.9093181534,false,false));
			frame.planets.add(new Planet(1, 20000, -34641.01615, -0.8662418831, -0.409193169,false,false));
			frame.planets.add(new Planet(1, 20000, 34641.01615, 0.8662418831, -0.409193169,false,false));
			frame.planets.add(new Planet(1, -37877.06205, 0, 0, 1.038095069,false,false));
		}
		else if (num == 2) {
			frame.planets.add(new Planet(6e14, 0, -40000, 0.7600256208, 0,false,false));
			frame.planets.add(new Planet(6e14, 34641.01615, 20000, -0.3800128104, 0.6582014952,false,false));
			frame.planets.add(new Planet(6e14, -34641.01615, 20000, -0.3800128104, -0.6582014952,false,false));
		}
		else if (num == 3) {
			double px = 0.842;
			double py = px/1.1;
			frame.planets.add(new Planet(6e14, 0, 0, -px, -py,false,false));
			frame.planets.add(new Planet(6e14, 49000, -12000, px/2, py/2,false,false));
			frame.planets.add(new Planet(6e14, -49000, 12000, px/2, py/2,false,false));
		}
		else return;
    }
}
