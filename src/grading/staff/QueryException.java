package grading.staff;

public class QueryException extends Exception {
    private static final long serialVersionUID = 1238993407975235708L;
    private Object response;
    
    public QueryException(String msg, Object response) {
        super(msg);
        this.response = response;
    }
    
    public Object getResponse() {
        return response;
    }
}
