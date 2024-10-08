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

public class ConstInitVal implements CommonTreeNode{
    public enum ConstInitValType{
        Dim0,
        Dim1,
        StringConst;
    }
    private ConstInitValType type;
    private Token stringconst;
    private Token l;
    private Token r;
    private ArrayList<ConstExp> constExps;

    public ConstInitVal(Token t1){
        type=ConstInitValType.StringConst;
        stringconst=t1;
    }
    public ConstInitVal(Token t1,Token t2){
        type=ConstInitValType.Dim1;
        constExps=new ArrayList<>();
        l=t1;
        r=t2;
    }
    public void addConstExp(ConstExp c1){
        constExps.add(c1);
    }
    public void endConstInitVal(Token t){
        r=t;
    }
    public ConstInitVal(ConstExp c2){
        type=ConstInitValType.Dim0;
        constExps=new ArrayList<>();
        constExps.add(c2);
    }
    public void walk(){
        Path path = Paths.get("parser.txt");
        try {
            if (type==ConstInitValType.StringConst){
                Files.write(path, stringconst.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
            } else if (type==ConstInitValType.Dim0) {
                constExps.get(0).walk();
            }else if (type==ConstInitValType.Dim1) {
                Files.write(path, l.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                if (!constExps.isEmpty()){
                    constExps.get(0).walk();
                }
                int lineno=l.getLineno();
                for (int i=1;i<constExps.size();i++){
                    Token tmp=new Token(lineno, Type.COMMA,",");
                    Files.write(path, tmp.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                    constExps.get(i).walk();
                }
                Files.write(path, r.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
            }
            Files.write(path, "<ConstInitVal>\n".getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
        } catch (IOException e) {

        }
    }
}
