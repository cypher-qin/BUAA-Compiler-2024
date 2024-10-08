package frontend.TreeNode;

import frontend.Token;
import frontend.Type;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class InitVal implements CommonTreeNode{
    public enum InitValType{
        Dim0,
        Dim1,
        StringConst;
    }
    private ArrayList<Exp> expList;
    private InitValType type;

    private Token stringconst;

    private Token l;
    private Token r;

    public InitVal(Token t1){
        type=InitValType.StringConst;
        expList=new ArrayList<>();
        stringconst=t1;
        l=null;
        r=null;
    }
    public InitVal(Exp exp){
        type=InitValType.Dim0;
        expList=new ArrayList<>();
        expList.add(exp);
        l=null;
        r=null;
    }
    public InitVal(Token t1,Token t2){
        type=InitValType.Dim1;
        expList=new ArrayList<>();
        l=t1;
        r=t2;
    }
    public void addExp(Exp exp){
        expList.add(exp);
    }
    public void endAdd(Token t1){
        r=t1;
    }
    public void walk(){
        Path path = Paths.get("parser.txt");
        try {
            if (type== InitValType.StringConst){
                Files.write(path, stringconst.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
            } else if (type== InitValType.Dim0) {
                expList.get(0).walk();
            }else if (type== InitValType.Dim1) {
                Files.write(path, l.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                if (!expList.isEmpty()){
                    expList.get(0).walk();
                }
                int lineno=l.getLineno();
                for (int i=1;i<expList.size();i++){
                    Token tmp=new Token(lineno, Type.COMMA,",");
                    Files.write(path, tmp.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                    expList.get(i).walk();
                }
                Files.write(path, r.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
            }
            Files.write(path, "<InitVal>\n".getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
        } catch (IOException e) {

        }
    }
}
