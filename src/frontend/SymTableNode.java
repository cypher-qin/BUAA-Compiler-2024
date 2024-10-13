package frontend;

public class SymTableNode {
    private final int tableid;
    private final String name;
    private final String type;//VAR ARRAY FUNC
    private final String btype;//INT CHAR VOID
    private final int isconst;
    public SymTableNode(int tableid,String name,String type,String btype,int isconst){
        this.tableid=tableid;
        this.name=name;
        this.type=type;
        this.btype=btype;
        this.isconst=isconst;
    }
    @Override
    public String toString(){
        String tmp = null;
        if (type.equals("VAR") && btype.equals("CHAR") && isconst==1){
            tmp="ConstChar";
        }else if (type.equals("VAR") && btype.equals("INT") && isconst==1){
            tmp="ConstInt";
        }else if (type.equals("ARRAY") && btype.equals("CHAR") && isconst==1){
            tmp="ConstCharArray";
        }else if (type.equals("ARRAY") && btype.equals("INT") && isconst==1){
            tmp="ConstIntArray";
        }else if (type.equals("VAR") && btype.equals("CHAR") && isconst==0){
            tmp="Char";
        }else if (type.equals("VAR") && btype.equals("INT") && isconst==0){
            tmp="Int";
        }else if (type.equals("ARRAY") && btype.equals("CHAR") && isconst==0){
            tmp="CharArray";
        }else if (type.equals("ARRAY") && btype.equals("INT") && isconst==0){
            tmp="IntArray";
        }else if (type.equals("FUNC") && btype.equals("VOID")){
            tmp="VoidFunc";
        }else if (type.equals("FUNC") && btype.equals("CHAR")){
            tmp="CharFunc";
        }else if (type.equals("FUNC") && btype.equals("INT")){
            tmp="IntFunc";
        } else {
            System.out.println("程序流豆浆了! SymTableNode-toString");
        }
        return tableid+" "+name+" "+tmp+"\n";
    }
}
