package frontend.TreeNode;

import frontend.Token;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class ForStmt implements CommonTreeNode{
    public LVal lVal;
    private Token eq;
    public Exp exp;
    public ForStmt(LVal lVal,Token eq,Exp exp){
        this.lVal=lVal;
        this.eq=eq;
        this.exp=exp;
    }
    public void walk(){
        lVal.walk();
        Path path = Paths.get("parser.txt");
        try {
            Files.write(path, eq.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
            exp.walk();
            Files.write(path, "<ForStmt>\n".getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
        } catch (IOException e) {

        }
    }
}
