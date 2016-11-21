package csci201.han.edward.fi.draw;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server extends Thread {
    private Vector<ServerThread> serverThreads;
    private int port;
    GameCanvas gc;
    int playerCounter;
    public static final String TAG = "xyz";
    int isReadyCount= 0;

    public Server(GameCanvas gc) {
        port = 8080;
        this.gc = gc;
        this.start();
        playerCounter = 0;
        serverThreads = new Vector<>();
    }


    public void run() {
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(port);
        } catch (IOException ioe){
//            try {
//                ss.close();
//            } catch(IOException ie) {
//                ie.printStackTrace();
//            }
        }
        while(playerCounter < 2) {
            try {
                Socket s = ss.accept();
                ServerThread st = new ServerThread(s, this);
                serverThreads.add(st);
                Log.d(TAG, "adding a connection to our Server");
                playerCounter++;
                Log.d(TAG, "counter value: " + playerCounter);
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            } finally {
//                try {
//                    if (ss != null) ss.close();
//                } catch (IOException ioe) {}
            }
        }
    }


    public void sendDataToAllClients(byte b) {
        for(ServerThread st : serverThreads) {
            st.sendMessage(b);
        }
    }

    public void checkToSendMessage() {
        Log.d(TAG, "isReadyCount: " + isReadyCount);
        if(isReadyCount >= 2) {
            sendDataToAllClients((byte)2);
//            for(ServerThread st : serverThreads) {
//                st.sendMessage((byte)2);
//            }
        }
    }
}