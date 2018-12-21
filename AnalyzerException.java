public class AnalyzerException extends Exception {
    private String msg;
    public AnalyzerException(String msg){
        this.msg=msg;
    }

    @Override
    public String getMessage() {
        return this.msg;
    }
}
