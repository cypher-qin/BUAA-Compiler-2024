package frontend.TreeNode;

import frontend.Token;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class EqExp {
    public enum EqExpType{
        SINGLE,
        DOUBLE;
    }
    private EqExpType type;
    private RelExp relExp;
    private Token sym;
    private EqExp eqExp;
    public EqExp(RelExp relExp){
        type= EqExpType.SINGLE;
        this.relExp=relExp;
    }
    public EqExp(EqExp eqExp,Token sym,RelExp relExp){
        type= EqExpType.DOUBLE;
        this.eqExp=eqExp;
        this.sym=sym;
        this.relExp=relExp;
    }
    public void walk(){
        try {
            Path path = Paths.get("parser.txt");
            if (type==EqExpType.SINGLE){
                relExp.walk();
            }else {
                eqExp.walk();
                Files.write(path, sym.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                relExp.walk();
            }
            Files.write(path, "<EqExp>\n".getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
        } catch (IOException e) {

        }
    }
}
