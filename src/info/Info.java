package info;

public class Info implements java.io.Serializable{
    
    public Info(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }
    
    public Info(int port, String name) {
        this.port = port;
        this.name = name;
    }
    
    public Info(String ip, int port, String name) {
        this.ip = ip;
        this.port = port;
        this.name = name;
    }
        
    @Override
    public boolean equals(Object o) {
        final Info client=(Info)o;
        return (name.equals(client.name) || port == client.port);
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
