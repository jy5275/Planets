import java.io.*;
import java.util.*;
import java.net.*;

class Manager extends Thread{
	Socket client;
	Object lck;
	Manager(Socket c_, Object lck_){
		client = c_;
		lck = lck_;
	}
	
	public void run(){
		BufferedReader br = null;
		FileWriter fw = null;
		try{
			synchronized(lck){
				Calendar calendar = Calendar.getInstance();
				Date time = calendar.getTime();
				System.out.println(time);
				br = new BufferedReader(new InputStreamReader(client.getInputStream()));
				fw = new FileWriter("rec.txt", true);
				fw.write(client.getInetAddress() + "\t" + time + "\t");
				String buf;
				while((buf = br.readLine()) != null){
					System.out.println(buf);
					fw.write(buf);
				}
				fw.write("\n");
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
            try {
				client.close();
				if(br != null) br.close();
				if(fw != null) fw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}
}

public class PlanetRec{
	public static void main(String[] args) throws Exception{
		ServerSocket socket1150 = new ServerSocket(1150);
		Object lck = new Object();
		while (true) {
			Socket call = socket1150.accept();
			new Manager(call, lck).start();
		}
	}
}