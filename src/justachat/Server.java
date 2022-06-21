package justachat;

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
    
    public static void main(String args[]){
        String ip=askIP();
        int port=askPort();
        
        Server server=new Server(port, ip);
        server.start();
    }
    
    public Server(){
        this(3000, LOCALHOST);
    }
    
    public Server(int port, String ip){
        this.port = port;
        this.ip = ip;
        clients=new ArrayList();
    }
    
    static String askIP() {
        Scanner s = new Scanner(System.in);
        String msgPrefix="";
        String ip = "";
        // https://stackoverflow.com/questions/5667371/validate-ipv4-address-in-java
        String ipFormat =  "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
        boolean match = false;
        
        while(!match) {
            System.out.print(msgPrefix+"Type your ip (empty for localhost): ");
            ip = s.nextLine().trim();
            if (ip.equals("")) return LOCALHOST;
            
            match = Pattern.compile(ipFormat).matcher(ip).find();
            if (!match && msgPrefix.equals("")) msgPrefix = "Try again. ";
        }
        return ip;
    }
    
    static int askPort(){
        int port=-1;
        String msgPrefix="";
        Scanner s = new Scanner(System.in);
        
        while(port<=0 || port>9999){
            try{
                System.out.print(msgPrefix+"Type your port: ");
                port = s.nextInt();
            }catch(InputMismatchException e){
                port=-1;
                if(msgPrefix.equals("")) msgPrefix="Try Again. ";
            }
        }
        return port;
    }
    
    void start(){
        Thread listening=new Thread(new Runnable(){
            @Override
            public void run(){
                try (ServerSocket server=new ServerSocket(port)){
                    System.out.println("Server open on port "+port+"!\n");
                    
                    while(true){
                        try (
                                Socket client=server.accept();
                                ObjectInputStream in = new ObjectInputStream(client.getInputStream())
                            ) {
                            Message msg=(Message) in.readObject();
                            switch(msg.getType()){
                                case Message.JOIN_REQUEST:
                                    System.out.println("ajksdn");
                                    joinClient(msg);
                                    break;
                                case Message.MESSAGE:
                                    manageMessage(msg);
                                    break;
                                case Message.LEAVE:
                                    quitUser(msg);
                                    break;
                            }
                            System.out.println("");
                        } catch (Exception ex) {
                            System.out.println("ERROR: Unable to read message.");
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
    
    boolean joinClient(Message msg) throws IOException{
        ClientInfo client=(ClientInfo)msg.getInfo();
        
        try(
                Socket socket=new Socket(client.getIp(), client.getPort());
                ObjectOutputStream out=new ObjectOutputStream(socket.getOutputStream())
            ){
            if(validClient(client)){
                for(ClientInfo c: clients){
                    try(
                            Socket socket2=new Socket(c.getIp(), c.getPort());
                            ObjectOutputStream out2=new ObjectOutputStream(socket2.getOutputStream())
                        ){
                        out2.writeObject(new Message(client, Message.JOIN_EVENT));
                    }
                }
                clients.add(client);
                out.writeObject(new Message(Message.JOIN_APROVED));
                System.out.println("****** "+client.getName()+" joins the party!");
                
                return true;
            }
            else{
                out.writeObject(new Message(infoServer(), Message.JOIN_REJECTED));
            }
        }
        return false;
    }
    
    void manageMessage(Message msg) throws IOException{
        ClientInfo client=(ClientInfo)msg.getInfo();
        
        if(hasClient(client)){
            System.out.println(client.getName()+" ("+client.getIp()+"): "+msg.getBody());
            for(ClientInfo c: clients){
                if(!!client.getIp().equals(c.getIp()) && client.getPort()!=c.getPort())
                    try(
                            Socket socket=new Socket(c.getIp(), c.getPort());
                            ObjectOutputStream out=new ObjectOutputStream(socket.getOutputStream())
                        ){
                        out.writeObject(new Message(client, msg.getBody(), Message.MESSAGE));
                    }
            }
        }else{
            System.out.println("***REJECTED MESSAGE***");
        }
    }
    
    void quitUser(Message msg) throws IOException{
        ClientInfo client=(ClientInfo)msg.getInfo();
        
        if(!hasClient(client)) return;
        
        for(int i=0; i<clients.size(); i++)
            if(clients.get(i).getIp().equals(client.getIp()) && clients.get(i).getPort()==client.getPort())
                clients.remove(i);
        System.out.println("****** "+client.getName()+" leaves the chat.");
        
        for(ClientInfo c: clients){
            if(!!client.getIp().equals(c.getIp()) && client.getPort()!=c.getPort())
                try(
                        Socket socket=new Socket(c.getIp(), c.getPort());
                        ObjectOutputStream out=new ObjectOutputStream(socket.getOutputStream())
                    ){
                    out.writeObject(new Message(client, Message.LEAVE));
                }
        }
    }
    
    boolean validClient(ClientInfo client){
        if(ip.equals(client.getIp()) && port==client.getPort()) return false;
        return !hasClient(client);
    }
    
    boolean hasClient(ClientInfo client){
        for(ClientInfo c: clients)
            if(client.equals(c)) return true;
        
        return false;
    }
    
    ArrayList<ClientInfo> infoServer(){
        ArrayList<ClientInfo> info=new ArrayList();
        
        info.add(new ClientInfo("Server", ip, port));
        for(ClientInfo c: clients) info.add(c);
        
        return info;
    }
    
    static final String LOCALHOST = "localhost";
    final String ip;
    final int port;
    ArrayList<ClientInfo> clients;
    
}