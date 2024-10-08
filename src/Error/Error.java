package Error;

public class Error {
    private char type;
    private int lineno;
    public Error(char type,int lineno){
        this.type=type;
        this.lineno=lineno;
    }
    public char getType() {
        return this.type;
    }
    public int getLineno(){
        return this.lineno;
    }
}
