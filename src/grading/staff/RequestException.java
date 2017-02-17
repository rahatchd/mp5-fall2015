package grading.staff;

public class RequestException extends Exception {
 
    private static final long serialVersionUID = 11314324232L;
 
    Object response;
    public RequestException(String msg, Object response) {
        super(msg);
        this.response = response;
    }
    
    public Object getResponse() {
        return response;
    }
}
