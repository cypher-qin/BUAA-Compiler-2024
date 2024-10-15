package frontend.TreeNode;

import frontend.Token;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class RelExp implements CommonTreeNode{
    public enum RelExpType{
        SINGLE,
        DOUBLE;
    }
    public RelExpType type;
    public RelExp relExp;
    private Token sym;
    public AddExp addExp;
    public RelExp(AddExp addExp){
        type=RelExpType.SINGLE;
        this.addExp=addExp;
    }
    public RelExp(RelExp relExp,Token sym,AddExp addExp){
        type=RelExpType.DOUBLE;
        this.relExp=relExp;
        this.sym=sym;
        this.addExp=addExp;
    }
    public void walk(){
        Path path = Paths.get("parser.txt");
        try {
            if (type== RelExpType.SINGLE){
                addExp.walk();
            }else {
                relExp.walk();
                Files.write(path, sym.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
                addExp.walk();
            }
            Files.write(path, "<RelExp>\n".getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
        } catch (IOException e) {

        }
    }
}
