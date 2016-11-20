package csci201.han.edward.fi.draw;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by tanujamohan on 11/19/16.
 */
public class ServerThread extends Thread{
    private Socket s;
    private Server server;
    private OutputStream os;
    private InputStream is;
    private boolean isReady;

    public ServerThread(Socket s, Server server) {
        this.server = server;
        this.s = s;
        isReady = false;


        try {
            os = s.getOutputStream();
            is = s.getInputStream();
            this.start();
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }

    public void run() {
        while(true) {
            String message = (String) getStringFromInputStream(is);
            server.counter++;
            server.checkToSendMessage();

        }
    }

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

    public void sendMessage(String s) {
        byte[] b = s.getBytes();
        try {
            os.write(b);
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }

}
