package frontend.TreeNode;

import frontend.Token;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class MulExp implements CommonTreeNode{
    public enum MulExpType{
        SINGLE,
        DOUBLE;
    }
    public MulExpType type;
    public UnaryExp unaryExp;
    public MulExp mulExp;
    private Token sym;
    public MulExp(UnaryExp unaryExp){
        type=MulExpType.SINGLE;
        this.unaryExp=unaryExp;
    }
    public MulExp(MulExp mulExp,Token sym,UnaryExp unaryExp){
        type=MulExpType.DOUBLE;
        this.unaryExp=unaryExp;
        this.mulExp=mulExp;
        this.sym=sym;
    }
    public void walk(){
        Path path = Paths.get("parser.txt");
        try {
            if (type==MulExpType.SINGLE){
                unaryExp.walk();
            }else {
                mulExp.walk();
                Files.write(path, sym.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
                unaryExp.walk();
            }
            Files.write(path, "<MulExp>\n".getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
        } catch (IOException e) {

        }
    }
}
