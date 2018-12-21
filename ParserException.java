public class ParserException extends Exception{
    private String msg;
    public ParserException(String msg){
        this.msg=msg;
    }

    @Override
    public String getMessage() {
        return this.msg;
    }
}
