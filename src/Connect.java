import java.io.DataOutputStream;
import java.net.Socket;

public class Connect implements Runnable{

    Socket client;
    String vString;
    DataOutputStream out;
    Connect(String v_){
        vString = v_;
    }
    public void run(){
        try{
            client = new Socket("49.234.7.244", 1150);
            out = new DataOutputStream(client.getOutputStream());
            out.writeUTF(vString);
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}