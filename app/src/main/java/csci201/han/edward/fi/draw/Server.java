package csci201.han.edward.fi.draw;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server extends Thread {
    private Vector<ServerThread> serverThreads;
    private int port;
    GameCanvas gc;
    int counter;

    public Server(GameCanvas gc) {
        port = 8080;
        this.gc = gc;
        this.start();
        counter = 0;
    }


    public void run() {
        ServerSocket ss = null;
        while(true) {
            try {
                ss = new ServerSocket(port);
                Socket s = ss.accept();
                ServerThread st = new ServerThread(s, this);
                serverThreads.add(st);
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            } finally {
                try {
                    if (ss != null) ss.close();
                } catch (IOException ioe) {}
            }
        }
    }


    public void sendDataToAllClients(String s) {
        for(ServerThread st : serverThreads) {
            st.sendMessage(s);
        }
    }

    public void checkToSendMessage() {
        if(counter == 2) {
            for(ServerThread st : serverThreads) {
                st.sendMessage("done");
            }
        }
    }
}