package frontend.TreeNode;

import frontend.Token;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class ConstDef implements CommonTreeNode{
    public enum ConstDefType{
        Dim0,
        Dim1;
    }
    private ConstDefType type;
    private Token ident;
    private Token left;
    private Token right;
    private ConstExp exp;
    private Token eq;
    private ConstInitVal constInitVal;
    public ConstDef(Token i,Token l,ConstExp exp,Token r,Token e,ConstInitVal c){
        type=ConstDefType.Dim1;
        ident=i;
        left=l;
        right=r;
        this.exp=exp;
        eq=e;
        constInitVal=c;
    }
    public ConstDef(Token i,Token e,ConstInitVal c){
        type=ConstDefType.Dim0;
        ident=i;
        left=null;
        right=null;
        eq=e;
        constInitVal=c;
    }
    public void walk(){
        Path path = Paths.get("parser.txt");
        try {
            Files.write(path, ident.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
            if (type==ConstDefType.Dim1){
                Files.write(path, left.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                exp.walk();
                if (right!=null){
                    Files.write(path, right.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                }
            }
            Files.write(path, eq.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
            constInitVal.walk();
            Files.write(path, "<ConstDef>\n".getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
        } catch (IOException e) {

        }
    }
}
