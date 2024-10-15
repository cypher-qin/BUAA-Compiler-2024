package frontend.TreeNode;

import frontend.Token;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class AddExp implements CommonTreeNode{
    public enum AddExpType{
        SINGLE,
        DOUBLE;
    }
    public AddExpType type;
    public MulExp mulExp;
    public Token sym;
    public AddExp addExp;
    public AddExp(MulExp mulExp){
        type=AddExpType.SINGLE;
        this.mulExp=mulExp;
    }
    public AddExp(AddExp addExp,Token sym,MulExp mulExp){
        type=AddExpType.DOUBLE;
        this.addExp=addExp;
        this.sym=sym;
        this.mulExp=mulExp;
    }
    public void walk(){
        Path path = Paths.get("parser.txt");
        if (type==AddExpType.SINGLE){
            mulExp.walk();
            try {
                Files.write(path, "<AddExp>\n".getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
            } catch (IOException e) {

            }
        }else {
            addExp.walk();
            try {
                Files.write(path, sym.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            mulExp.walk();
            try {
                Files.write(path, "<AddExp>\n".getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
