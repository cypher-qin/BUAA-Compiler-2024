package frontend;

public class Token {
    private final int lineno;
    private final Type type;

    private final String value;
    public Token(int lineno,Type type,String value){
        this.lineno=lineno;
        this.type=type;
        this.value=value;
    }
    public int getLineno(){
        return lineno;
    }
    public Type getType(){
        return type;
    }
    public String getValue(){
        return value;
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append(this.type.getCategoryCode());
        sb.append(' ');
        sb.append(this.value);
        sb.append('\n');
        return sb.toString();
    }
}
