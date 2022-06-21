package info;

public class Message implements java.io.Serializable{
    
    Message(int type){
        this.type=type;
    }
    
    Message(Object info, int type){
        this(type);
        this.info=info;
    }
    
    Message(Object info, String body, int type){
        this(info, type);
        this.body=body;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Object getInfo() {
        return info;
    }

    public void setInfo(Object info) {
        this.info = info;
    }
    
    
    
    static final int JOIN_REQUEST=0, LEAVE=1, MESSAGE=2, JOIN_APROVED=3, JOIN_EVENT=4, JOIN_REJECTED=5;
    //static final String UNAVAILABLE_NAME="Unavailable name", UNAVAILABLE_PORT="Unavailable port";
    private int type;
    private String body;
    private Object info;
    
}
