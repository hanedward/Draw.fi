package csci201.han.edward.fi.draw;

/**
 * Created by rager7 on 11/19/2016.
 */
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.OutputStream;
        import java.net.Socket;
        import java.net.UnknownHostException;

        import android.util.Log;
        import android.widget.TextView;

public class Client extends Thread {

    String dstAddress;
    int dstPort;
    String response = "";
    TextView textResponse;
    OutputStream os;
    InputStream is;
    private Socket socket;
    boolean readyToSwitch;
    public static final String TAG = "xyz";

    public Client(String addr, int port) {
        dstAddress = addr;
        dstPort = port;
        readyToSwitch = false;


    }

    public void sendToServer(byte b) {
        try {
            Log.d(TAG, "I am writing this to the server: " + b);
            os.write(b);
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }

    public void run() {

        try {
            socket = new Socket(dstAddress, dstPort);
            os = socket.getOutputStream();
            is = socket.getInputStream();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(true) {
            try {
                byte b = (byte)is.read();
                if(b == 2) {
                    Log.d(TAG, "Server said I am ready to switch");
                    readyToSwitch = true;
                }
            } catch (IOException ioe) { }

        }

    }

    public boolean getReady() {
        return readyToSwitch;
    }


//    private static String getStringFromInputStream(InputStream is) {
//
//        BufferedReader br = null;
//        StringBuilder sb = new StringBuilder();
//
//        String line;
//        try {
//
//            br = new BufferedReader(new InputStreamReader(is));
//            while ((line = br.readLine()) != null) {
//                sb.append(line);
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (br != null) {
//                try {
//                    br.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        return sb.toString();
//
//    }

}

