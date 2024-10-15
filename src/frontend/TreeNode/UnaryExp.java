package frontend.TreeNode;

import frontend.Token;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class UnaryExp implements CommonTreeNode{
    public enum UnaryExpType{
        PrimaryExp_TYPE,
        Function_TYPE,
        UnaryEXp_TYPE;
    }
    public UnaryExpType type;
    public PrimaryExp primaryExp;
    public Token ident;
    private Token left;
    private Token right;
    public FuncRParams funcRParams;
    private UnaryOp unaryOp;
    public UnaryExp unaryExp;
    public UnaryExp(PrimaryExp primaryExp){
        type=UnaryExpType.PrimaryExp_TYPE;
        this.primaryExp=primaryExp;
    }
    public UnaryExp(Token ident,Token left,FuncRParams funcRParams,Token right){
        type=UnaryExpType.Function_TYPE;
        this.ident=ident;
        this.left=left;
        this.funcRParams=funcRParams;
        this.right=right;
    }
    public UnaryExp(UnaryOp unaryOp,UnaryExp unaryExp){
        type=UnaryExpType.UnaryEXp_TYPE;
        this.unaryOp=unaryOp;
        this.unaryExp=unaryExp;
    }
    public void walk(){
        Path path = Paths.get("parser.txt");
        try {
            if (type==UnaryExpType.PrimaryExp_TYPE){
                primaryExp.walk();
            } else if (type==UnaryExpType.Function_TYPE) {
                Files.write(path, ident.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
                Files.write(path, left.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                if (funcRParams!=null){
                    funcRParams.walk();
                }
                if (right!=null){
                    Files.write(path, right.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                }
            }else {
                unaryOp.walk();
                unaryExp.walk();
            }
            Files.write(path, "<UnaryExp>\n".getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
        } catch (IOException e) {

        }
    }
}
