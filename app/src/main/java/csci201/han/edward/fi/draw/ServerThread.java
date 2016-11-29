package csci201.han.edward.fi.draw;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
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
            try {
                int b = is.read();
                if(b != -1) {
                    server.isReadyCount++;
                    server.checkToSendMessage();
                }
            } catch (IOException ioe) {}
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

    public void sendMessage(byte b) {
//        byte[] b = s.getBytes();
        try {
//            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this.s.getOutputStream())), true);
//            out.print(s);
            os.write(b);
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }

}
