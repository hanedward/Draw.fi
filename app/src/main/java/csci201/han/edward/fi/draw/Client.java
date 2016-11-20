package csci201.han.edward.fi.draw;

/**
 * Created by rager7 on 11/19/2016.
 */
        import java.io.ByteArrayOutputStream;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.OutputStream;
        import java.net.Socket;
        import java.net.UnknownHostException;
        import android.os.AsyncTask;
        import android.widget.TextView;

public class Client extends AsyncTask<Void, Void, Void> {

    String dstAddress;
    int dstPort;
    String response = "";
    TextView textResponse;
    OutputStream os;
    InputStream is;

    Client(String addr, int port) {
        dstAddress = addr;
        dstPort = port;

    }

    public void sendToServer(String b) {
        try {
            os.write(Byte.valueOf(b));
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }

    @Override
    protected Void doInBackground(Void... arg0) {

        Socket socket = null;

        try {
            socket = new Socket(dstAddress, dstPort);
            os = socket.getOutputStream();
            is = socket.getInputStream();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        textResponse.setText(response);
        super.onPostExecute(result);
    }

}

