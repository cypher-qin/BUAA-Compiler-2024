package frontend.TreeNode;

import frontend.Token;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class PrimaryExp implements CommonTreeNode{
    public enum PrimaryExpType{
        EXP_TYPE,
        LVal_TYPE,
        Number_TYPE,
        Character_TYPE;
    }
    public PrimaryExpType type;
    private Token left;
    private Token right;
    public Exp exp;
    public LVal lVal;
    public Number number;
    public Character character;
    public PrimaryExp(Token left,Exp exp,Token right){
        type=PrimaryExpType.EXP_TYPE;
        this.left=left;
        this.exp=exp;
        this.right=right;
    }
    public PrimaryExp(LVal lVal){
        type=PrimaryExpType.LVal_TYPE;
        this.lVal=lVal;
    }
    public PrimaryExp(Number number){
        type=PrimaryExpType.Number_TYPE;
        this.number=number;
    }
    public PrimaryExp(Character character){
        type=PrimaryExpType.Character_TYPE;
        this.character=character;
    }
    public void walk(){
        Path path = Paths.get("parser.txt");
        try {
            if (type==PrimaryExpType.Number_TYPE){
                number.walk();
            } else if (type==PrimaryExpType.Character_TYPE) {
                character.walk();
            } else if (type==PrimaryExpType.LVal_TYPE) {
                lVal.walk();
            }else {
                Files.write(path, left.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
                exp.walk();
                if (right!=null){
                    Files.write(path, right.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                }
            }
            Files.write(path, "<PrimaryExp>\n".getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
