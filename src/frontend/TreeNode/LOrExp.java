package frontend.TreeNode;

import frontend.Token;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class LOrExp implements CommonTreeNode{
    public enum LOrExpType{
        SINGLE,
        DOUBLE;
    }
    private LOrExpType type;
    private LAndExp lAndExp;
    private Token sym;
    private LOrExp lOrExp;
    public LOrExp(LAndExp lAndExp){
        type=LOrExpType.SINGLE;
        this.lAndExp=lAndExp;
    }
    public LOrExp(LOrExp lOrExp,Token sym,LAndExp lAndExp){
        type=LOrExpType.DOUBLE;
        this.lOrExp=lOrExp;
        this.sym=sym;
        this.lAndExp=lAndExp;
    }
    public void walk(){
        Path path = Paths.get("parser.txt");
        try {
            if (type== LOrExpType.SINGLE){
                lAndExp.walk();
            }else {
                lOrExp.walk();
                Files.write(path, sym.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
                lAndExp.walk();
            }
            Files.write(path, "<LOrExp>\n".getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
        } catch (IOException e) {

        }
    }
}
