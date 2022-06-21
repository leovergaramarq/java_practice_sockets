package info;

public class ClientInfo implements java.io.Serializable{
    
    ClientInfo(String name){
        this.name=name;
    }
    
    ClientInfo(String name, String ip, int port){
        this(name);
        this.ip=ip;
        this.port=port;
    }
    
    @Override
    public boolean equals(Object o){
        final ClientInfo client=(ClientInfo)o;
        return (name.equals(client.name) || ip.equals(client.ip) && port==client.port);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
    
    
    
    private String name, ip;
    private int port;
}
