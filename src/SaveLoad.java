import java.awt.event.*;
import java.io.*;
import javax.swing.JTextField;

class SaveGalaxy implements ActionListener {
	Mainfile frame;
	JTextField nameJT;
	File file;
	
	public SaveGalaxy(Mainfile f) {
		frame = f;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			frame.generateScreenShot(nameJT.getText());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int tmp = 0;
		try {
			file= new File("data/"+nameJT.getText());
			FileWriter fw = new FileWriter(file, false);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(frame.planets.size() + "\r\n");
			for (int i = 0; i < frame.planets.size(); i++) {
				tmp = frame.planets.get(i).visible? 1:0;
				bw.write(tmp + "\r\n");
				if (tmp == 0) continue;
				bw.write(frame.planets.get(i).m + "\r\n");
				bw.write(frame.planets.get(i).x + "\r\n");
				bw.write(frame.planets.get(i).y + "\r\n");
				bw.write(frame.planets.get(i).vx + "\r\n");
				bw.write(frame.planets.get(i).vy + "\r\n");
				tmp = frame.planets.get(i).keepstill? 1:0;
				bw.write(tmp + "\r\n");
			}
			bw.close();
			fw.close();
			nameJT.setText("");
			frame.addLoadMenu();
		} catch (IOException ioe) {}
		System.out.println("Save in " + file);
    }
}

class LoadGalaxy implements ActionListener {
	Mainfile frame;
	String fileName;
	File file;
	
	public LoadGalaxy(Mainfile f, String name) {
		frame = f;
		fileName=name;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		file =new File("data/"+fileName);
		frame.planets.clear();
		if (!file.exists()) {
			System.err.println("File not found");
			return;
		}
		try {
			String str = null;
			InputStreamReader isr = new InputStreamReader(new FileInputStream(file));
			BufferedReader br = new BufferedReader(isr);
			str = br.readLine();
			int planetnum = Integer.valueOf(str).intValue();
			double m, x, y, vx, vy;
			boolean keepstill;
			for (int i = 0; i < planetnum; i++) {
				str = br.readLine();
				if (Integer.valueOf(str).intValue() == 0) {
					frame.planets.add(new Planet(0,0,0,0,0,false,false));
					frame.planets.get(i).visible = false;
					continue;
				}
				str = br.readLine();
				m = Double.valueOf(str);
				str = br.readLine();
				x = Double.valueOf(str);
				str = br.readLine();
				y = Double.valueOf(str);
				str = br.readLine();
				vx = Double.valueOf(str);
				str = br.readLine();
				vy = Double.valueOf(str);
				str = br.readLine();
				if (Integer.valueOf(str).intValue() == 1) keepstill = true;
				else keepstill = false;
				frame.planets.add(new Planet(m, x, y, vx, vy, false, keepstill));
			}
			br.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		System.out.println("Load from " + file);
		frame.needRedrawTrace=true;
    }
}
