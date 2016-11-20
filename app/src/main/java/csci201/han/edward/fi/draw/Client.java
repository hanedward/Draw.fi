package csci201.han.edward.fi.draw;

/**
 * Created by rager7 on 11/19/2016.
 */
        import java.io.BufferedReader;
        import java.io.ByteArrayOutputStream;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.io.OutputStream;
        import java.net.Socket;
        import java.net.UnknownHostException;

        import android.content.Context;
        import android.os.AsyncTask;
        import android.provider.Telephony;
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

    public Client(String addr, int port) {
        dstAddress = addr;
        dstPort = port;
        readyToSwitch = false;


        try {
            socket = new Socket(dstAddress, dstPort);
            os = socket.getOutputStream();
            is = socket.getInputStream();

            this.start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendToServer(String b) {
        try {
            os.write(Byte.valueOf(b));
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }

    public void run() {

        while(true) {
            String message  = (String) getStringFromInputStream(is); //online code -- careful
            readyToSwitch = true;
        }

    }

    public boolean getReady() {
        return readyToSwitch;
    }

//    @Override
//    protected Void doInBackground(Void... arg0) {
//
//        Socket socket = null;
//
//        try {
//            socket = new Socket(dstAddress, dstPort);
//            os = socket.getOutputStream();
//            is = socket.getInputStream();
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (socket != null) {
//                try {
//                    socket.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return null;
//    }

//    @Override
//    protected void onPostExecute(Void result) {
//        textResponse.setText(response);
//        super.onPostExecute(result);
//    }

    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }

}

