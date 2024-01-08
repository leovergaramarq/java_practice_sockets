package info;

public class Pack implements java.io.Serializable {
    
    public Pack(int type) {
        this.type=type;
    }
    
    public Pack(Object info, int type) {
        this(type);
        this.info=info;
    }
    
    public Pack(Object info, String body, int type) {
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
    
    public static final int JOIN_REQUEST=0, LEAVE=1, MESSAGE=2, JOIN_APROVED=3, JOIN_EVENT=4, 
            JOIN_REJECTED=5, CHECK_SERVER_REQUEST=6, CHECK_SERVER_APROVED=7, CHECK_SERVER_REJECTED=8;
    //static final String UNAVAILABLE_NAME="Unavailable name", UNAVAILABLE_PORT="Unavailable port";
    private int type;
    private String body;
    private Object info;
    
}
