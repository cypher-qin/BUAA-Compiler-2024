package frontend.TreeNode;

import frontend.Token;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class LAndExp implements CommonTreeNode{
    public enum LAndExpType{
        SINGLE,
        DOUBLE;
    }
    private LAndExp lAndExp;
    private Token sym;
    private EqExp eqExp;
    private LAndExpType type;
    public LAndExp(EqExp eqExp){
        type=LAndExpType.SINGLE;
        this.eqExp=eqExp;
    }
    public LAndExp(LAndExp lAndExp,Token sym,EqExp eqExp){
        type=LAndExpType.DOUBLE;
        this.eqExp=eqExp;
        this.sym=sym;
        this.lAndExp=lAndExp;
    }
    public void walk(){
        Path path = Paths.get("parser.txt");
        try {
            if (type==LAndExpType.SINGLE){
                eqExp.walk();
            }else {
                lAndExp.walk();
                Files.write(path, sym.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
                eqExp.walk();
            }
            Files.write(path, "<LAndExp>\n".getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
        } catch (IOException e) {

        }
    }
}
