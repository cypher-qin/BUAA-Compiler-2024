package frontend.TreeNode;

import frontend.Token;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class LVal implements CommonTreeNode{
    public enum LValType{
        Dim0,
        Dim1;
    }
    public Token ident;
    private Token left;
    private Token right;
    public Exp exp;
    public LValType type;
    public LVal(Token ident,Token left,Exp exp,Token right){
        type=LValType.Dim1;
        this.ident=ident;
        this.left=left;
        this.exp=exp;
        this.right=right;
    }
    public LVal(Token ident){
        type=LValType.Dim0;
        this.ident=ident;
    }
    public void walk(){
        Path path = Paths.get("parser.txt");
        try {
            Files.write(path, ident.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
            if (type==LValType.Dim1){
                Files.write(path, left.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                exp.walk();
                if (right!=null){
                    Files.write(path, right.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                }
            }
            Files.write(path, "<LVal>\n".getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
        } catch (IOException e) {

        }
    }
}
