package justachat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

public class Client{
    
    public static void main(String args[]){
        String name=askName(), ip=askIP();
        int port=askPort();
        
        Client client=new Client(name);
        client.join(ip, port);
        client.listen();
    }
    
    public Client(String name){
        info=new ClientInfo(name);
    }
    
    static String askName(){
        String[] noNames={"", "you", "server"};
        String name;
        do{
            name=JOptionPane.showInputDialog("How do you wanna be called?").trim();
            if(!in(name, noNames)) break;
        }while(true);
        
        return name;
    }
    
    static String askIP() {
        String msgPrefix="";
        String ip = "";
        // https://stackoverflow.com/questions/5667371/validate-ipv4-address-in-java
        String ipFormat =  "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
        boolean match = false;
        
        while(!match) {
            ip = JOptionPane.showInputDialog(msgPrefix+"Type your ip (empty for localhost):");
            if (ip.equals("")) return LOCALHOST;
            
            match = Pattern.compile(ipFormat).matcher(ip).find();
            if (!match && msgPrefix.equals("")) msgPrefix = "Try again. ";
        }
        return ip;
    }
    
    static int askPort(){
        int port=-1;
        String msgPrefix="";
        
        while(port<=0 || port>9999){
            try{
                port=Integer.parseInt(JOptionPane.showInputDialog(msgPrefix+"Type your port:"));
            }catch(NumberFormatException e){
                port=-1;
                if(msgPrefix.equals("")) msgPrefix="Try Again\n";
            }
        }
        return port;
    }
    
    void join(String ip, int port){
        info.setIp(ip);
        info.setPort(port);
        
        System.out.println("Trying to join...");
        try(
                Socket socket=new Socket(info.ip, SERVER_PORT);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())
            ) {
            out.writeObject(new Message(info, Message.JOIN_REQUEST));
            
        } catch (IOException ex) {
            System.out.println("ERROR: Impossible connection.");
        }
    }
    
    static boolean in(String name, String[] noNames){
        for(String n: noNames)
            if(name.toLowerCase().equals(n))
                return true;
        
        return false;
    }
    
    void listen(){
        Thread listening=new Thread(new Runnable(){
            @Override
            public void run(){
                try {
                    ServerSocket server=new ServerSocket(info.getPort());
                    while(true){
                        try (
                                Socket smbd=server.accept();
                                ObjectInputStream in=new ObjectInputStream(smbd.getInputStream())
                            ) {
                            Message msg=(Message) in.readObject();
                            switch(msg.getType()){
                                case Message.JOIN_APROVED:
                                    joinAproved();
                                    break;
                                case Message.JOIN_REJECTED:
                                    joinRejected(msg);
                                    break;
                                case Message.JOIN_EVENT:
                                    userJoined(msg);
                                    break;
                                case Message.MESSAGE:
                                    receiveMessage(msg);
                                    break;
                                case Message.LEAVE:
                                    userLeft(msg);
                                    break;
                                
                            }
                        } catch (ClassNotFoundException ex) {
                            System.out.println("ERROR: unable to read message.");
                        }
                    }
                } catch (IOException ex) {
                    System.out.println("ERROR: unable to open port.");
                }
            }
        });
        listening.start();
    }
    
    public void send(String msg){
        System.out.println("Sending message: \""+msg+"\".");
        
        try (
                Socket socket=new Socket(SERVER_IP, SERVER_PORT);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())
            ) {
            out.writeObject(new Message(info, msg, Message.MESSAGE));
        }catch (IOException ex) {
            System.out.println("ERROR: Impossible connection.");
        }
    }
    
    public void leave(){
        System.out.println("Leaving...");
        
        try {
            try (
                    Socket socket=new Socket(SERVER_IP, SERVER_PORT);
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())
                ) {
                out.writeObject(new Message(info, Message.LEAVE));
            }
            
        } catch (IOException ex) {
            System.out.println("ERROR: Conexión imposible.");
        }
    }
    
    void receiveMessage(Message msg){
        ClientInfo c=(ClientInfo)msg.getInfo();
        user.receiveMessage(c.getName(), c.getIp(), msg.getBody());
    }
    
    void userJoined(Message msg){
        ClientInfo c=(ClientInfo)msg.getInfo();
        user.userJoined(c.getName(), c.getIp(), msg.getBody());
    }
    
    void userLeft(Message msg){
        ClientInfo c=(ClientInfo)msg.getInfo();
        user.userLeft(c.getName(), c.getIp(), msg.getBody());
    }
    
    void joinAproved(){
        System.out.println("Succesfull. You're ready to message people!\n");
        //user.joinAproved();
    }
    
    void joinRejected(Message msg){
        System.out.println("Joining failed!");
        user.joinRejected(msg);
    }

    public ClientInfo getInfo() {
        return info;
    }

    public void setInfo(ClientInfo info) {
        this.info = info;
    }

    public UserInterface getUser() {
        return user;
    }

    public void setUser(UserInterface user) {
        this.user = user;
    }
    
    
    private ClientInfo info;
    private UserInterface user;
    private final static String LOCALHOST = "localhost";
}
