package justachat;

import info.Info;
import info.Pack;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Server {
    
    public static void main(String args[]) {
        String ip=askIP();
        int port=askPort();
        
        Server server=new Server(ip, port, "server");
        server.start();
    }
    
    public Server() {
        this(LOCALHOST, 3000, "server");
    }
    
    public Server(String ip, int port, String name) {
        this.ip = ip;
        this.port = port;
        this.name = name;
        clients = new ArrayList();
    }
    
    static String askIP() {
        Scanner s = new Scanner(System.in);
        String pkgPrefix="";
        String ip = "";
        // https://stackoverflow.com/questions/5667371/validate-ipv4-address-in-java
        String ipFormat =  "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.) {3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
        boolean match = false;
        
        while(!match) {
            System.out.print(pkgPrefix+"Type your ip (empty for localhost): ");
            ip = s.nextLine().trim();
            if (ip.equals("")) return LOCALHOST;
            
            match = Pattern.compile(ipFormat).matcher(ip).find();
            if (!match && pkgPrefix.equals("")) pkgPrefix = "Try again. ";
        }
        return ip;
    }
    
    static int askPort() {
        int port=-1;
        String pkgPrefix="";
        Scanner s = new Scanner(System.in);
        
        while(port<=0 || port>9999) {
            try{
                System.out.print(pkgPrefix+"Type your port: ");
                port = s.nextInt();
            }catch(InputMismatchException e) {
                port=-1;
                if(pkgPrefix.equals("")) pkgPrefix="Try Again. ";
            }
        }
        return port;
    }
    
    void start() {
        Thread listening=new Thread(new Runnable() {
            @Override
            public void run() {
                try (ServerSocket server=new ServerSocket(port)) {
                    System.out.println("Server open on port "+port+"!\n");
                    
                    while(true) {
                        try (
                                Socket client=server.accept();
                                ObjectInputStream in = new ObjectInputStream(client.getInputStream())
                            ) {
                            Pack pkg=(Pack) in.readObject();
                            System.out.println("Request incoming..." + pkg.getType());
                            switch(pkg.getType()) {
                                case Pack.CHECK_SERVER_REQUEST:
                                    checkRequest(pkg);
                                    break;
                                case Pack.JOIN_REQUEST:
                                    joinClient(pkg);
                                    break;
                                case Pack.MESSAGE:
                                    managePack(pkg);
                                    break;
                                case Pack.LEAVE:
                                    quitUser(pkg);
                                    break;
                            }
                            System.out.println("");
                        } catch (Exception ex) {
                            System.out.println("ERROR: Unable to read Pack.");
                            //ex.printStackTrace();
                        }
                    }
                    
                } catch (IOException ex) {
                    System.out.println("ERROR: unable to open port.");
                }
            }
        });
        listening.start();
    }
    
    boolean checkRequest(Pack pkg) throws IOException{
        int clientPort = (int) pkg.getInfo();
        System.out.println("Getting info: Ip="+ ip + ", Port="+clientPort);

        Socket socket = new Socket(ip, clientPort);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

        out.writeObject(new Pack(infoServer(socket), Pack.CHECK_SERVER_APROVED));
        return false;
    }
    
    boolean joinClient(Pack pkg) throws IOException{
        Info client = (Info)pkg.getInfo();
        
        try(
                Socket socket = new Socket(client.getIp(), client.getPort());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())
            ) {
            if(validClient(client)) {
                for(Info c: clients) {
                    try(
                            Socket socket2 = new Socket(c.getIp(), c.getPort());
                            ObjectOutputStream out2 = new ObjectOutputStream(socket2.getOutputStream())
                        ) {
                        out2.writeObject(new Pack(client, Pack.JOIN_EVENT));
                    }
                }
                clients.add(client);
                out.writeObject(new Pack(Pack.JOIN_APROVED));
                System.out.println("****** "+client.getName()+" joins the party!");
                
                return true;
            }
            else{
                out.writeObject(new Pack(infoServer(socket), Pack.JOIN_REJECTED));
            }
        }
        return false;
    }
    
    void managePack(Pack pkg) throws IOException{
        Info client=(Info)pkg.getInfo();
        
        if(hasClient(client)) {
            System.out.println(client.getName()+" ("+client.getIp()+"): "+pkg.getBody());
            for(Info c: clients) {
                if(!!client.getIp().equals(c.getIp()) && client.getPort()!=c.getPort())
                    try(
                            Socket socket=new Socket(c.getIp(), c.getPort());
                            ObjectOutputStream out=new ObjectOutputStream(socket.getOutputStream())
                        ) {
                        out.writeObject(new Pack(client, pkg.getBody(), Pack.MESSAGE));
                    }
            }
        }else{
            System.out.println("***REJECTED Pack***");
        }
    }
    
    void quitUser(Pack pkg) throws IOException{
        Info client=(Info)pkg.getInfo();
        
        if(!hasClient(client)) return;
        
        for(int i=0; i<clients.size(); i++)
            if(clients.get(i).getIp().equals(client.getIp()) && clients.get(i).getPort()==client.getPort())
                clients.remove(i);
        System.out.println("****** "+client.getName()+" leaves the chat.");
        
        for(Info c: clients) {
            if(!!client.getIp().equals(c.getIp()) && client.getPort()!=c.getPort())
                try(
                        Socket socket=new Socket(c.getIp(), c.getPort());
                        ObjectOutputStream out=new ObjectOutputStream(socket.getOutputStream())
                    ) {
                    out.writeObject(new Pack(client, Pack.LEAVE));
                }
        }
    }
    
    boolean validClient(Info client) {
        if(ip.equals(client.getIp()) && port == client.getPort()) return false;
        return !hasClient(client);
    }
    
    boolean hasClient(Info client) {
        for(Info c: clients)
            if(client.equals(c)) return true;
        
        return false;
    }
    
    ArrayList<Info> infoServer(Socket socket) {
        ArrayList<Info> infoUsers=new ArrayList();
        
        infoUsers.add(new Info(socket.getInetAddress().getHostAddress(), port, name));
        for(Info c: clients) infoUsers.add(c);
        
        return infoUsers;
    }
    
    static final String LOCALHOST = "localhost";
//    final Info info;
    final String ip, name;
    final int port;
    ArrayList<Info> clients;
    
}