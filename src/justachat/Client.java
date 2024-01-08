package justachat;

import info.*;
import gui.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Client {

    public static void main(String args[]) {
        Client c = new Client();
        c.homeView = new HomeView(c);
        c.homeView.init();
    }

    public Client() {
    }

    public synchronized void listen() {
        listen(port);
    }

//    public synchronized void listen(int port) {
//        listening = false;
//        if(t != null) t.interrupt();
//        
//        if(t != null) try {
//            t.join();
//        } catch (InterruptedException ex) {
////            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        listening = true;
//        
//        listenSecure(port);
//    }

    Thread t;
    static int threads = 0;

    public synchronized void listen(int port) {
        if(listening) return;
        
        t = new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                if(listening) return;
                
                listening = true;
                
                System.out.println("client port: " + port);
                try ( ServerSocket server = new ServerSocket(port)) {
                    Socket smbd = null;
                    ObjectInputStream in = null;

                    while (listening) {
                        smbd = server.accept();
                        in = new ObjectInputStream(smbd.getInputStream());

                        Pack pkg = (Pack) in.readObject();
                        System.out.println("Request incoming..." + pkg.getType());
                        switch (pkg.getType()) {
                            case Pack.CHECK_SERVER_APROVED:
                                checkServerAproved(pkg);
                                break;
                            case Pack.JOIN_APROVED:
                                joinAproved();
                                break;
                            case Pack.JOIN_REJECTED:
                                joinRejected(pkg);
                                break;
                            case Pack.JOIN_EVENT:
                                userJoined(pkg);
                                break;
                            case Pack.MESSAGE:
                                receivePack(pkg);
                                break;
                            case Pack.LEAVE:
                                userLeft(pkg);
                                break;

                        }
                    }

                    if (smbd != null) {
                        smbd.close();
                    }
                    if (in != null) {
                        in.close();
                    }
                    System.out.println("listening finished");
                } catch (IOException ex) {
                    System.out.println("ERROR: unable to open port.");
                } catch (Exception ex) {
                    System.out.println("ERROR: unexpected exception");
                }
            }
        }, "Listening " + (++threads));
        t.start();
    }

    public boolean checkServer(String ip, int serverPort) {

        System.out.println("Getting info: Ip=" + ip + ", Port=" + serverPort);
        try (
                 Socket socket = new Socket(ip, serverPort);  ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
            out.writeObject(new Pack(port, Pack.CHECK_SERVER_REQUEST));

        } catch (IOException ex) {
            System.out.println("ERROR: Impossible connection.");
            return false;
        }
        return true;
    }

    public void join(String ip, int serverPort, String serverName, int port, String name) {
        this.ip = ip;
        this.serverPort = serverPort;
        this.serverName = serverName;
        this.port = port;
        this.name = name;

        System.out.println("Trying to join...");
        try (
                 Socket socket = new Socket(ip, serverPort);  ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
            out.writeObject(new Pack(new Info(port, name), Pack.JOIN_REQUEST));

        } catch (IOException ex) {
            System.out.println("ERROR: Impossible connection.");
        }
    }

    public void send(String pkg) {
        System.out.println("Sending Pack: \"" + pkg + "\".");

        try (
                 Socket socket = new Socket(ip, serverPort);  ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
            out.writeObject(new Pack(new Info(ip, port, name), pkg, Pack.MESSAGE));
        } catch (IOException ex) {
            System.out.println("ERROR: Impossible connection.");
        }
    }

    public void leave() {
        System.out.println("Leaving...");

        try {
            try (
                     Socket socket = new Socket(ip, serverPort);  ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
                out.writeObject(new Pack(new Info(ip, port, name), Pack.LEAVE));
            }

        } catch (IOException ex) {
            System.out.println("ERROR: Conexión imposible.");
        }
    }

    void checkServerAproved(Pack pkg) {
        System.out.println("Check server aproved");
        homeView.checkServerAproved(pkg);
    }

    void receivePack(Pack pkg) {
    }

    void userJoined(Pack pkg) {
    }

    void userLeft(Pack pkg) {
    }

    void joinAproved() {
        System.out.println("Join server aproved");
        homeView.joinServerAproved();
    }

    void joinRejected(Pack pkg) {
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public HomeView getHomeView() {
        return homeView;
    }

    public void setHomeView(HomeView homeView) {
        this.homeView = homeView;
    }

    public Chat getChatView() {
        return chatView;
    }

    public void setChatView(Chat chatView) {
        this.chatView = chatView;
    }

    public boolean isListening() {
        return listening;
    }

    public void setListening(boolean listening) {
        this.listening = listening;
    }

    private int serverPort, port;
    private String ip, name, serverName;
    private HomeView homeView;
    private Chat chatView;
    private volatile boolean listening;
}
