package frontend.TreeNode;

import frontend.Token;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class VarDef implements CommonTreeNode{
    public enum VarDefType{
        initialized_dim0,
        notinitialized_dim0,
        initialized_dim1,
        notinitialized_dim1;
    }
    private Token ident;
    private Token left;
    private Token right;

    private ConstExp constExp;
    private Token eq;
    private InitVal initVal;
    private VarDefType type;
    public VarDef(Token i){
        type=VarDefType.notinitialized_dim0;
        ident=i;
        left=null;
        right=null;
        constExp=null;
        eq=null;
        initVal=null;
    }
    public VarDef(Token i,Token e,InitVal c){
        type=VarDefType.initialized_dim0;
        ident=i;
        left=null;
        right=null;
        constExp=null;
        eq=e;
        initVal=c;
    }
    public VarDef(Token i,Token l,Token r,ConstExp ce){
        type=VarDefType.notinitialized_dim1;
        ident=i;
        left=l;
        right=r;
        constExp=ce;
        eq=null;
        initVal=null;
    }
    public VarDef(Token i,Token l,Token r,ConstExp ce,Token e,InitVal c){
        type=VarDefType.initialized_dim1;
        ident=i;
        left=l;
        right=r;
        constExp=ce;
        eq=e;
        initVal=c;
    }
    public void walk(){
        Path path = Paths.get("parser.txt");
        try {
            if (type==VarDefType.notinitialized_dim0){
                Files.write(path, ident.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
            } else if (type==VarDefType.initialized_dim0) {
                Files.write(path, ident.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                Files.write(path, eq.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                initVal.walk();
            } else if (type==VarDefType.notinitialized_dim1) {
                Files.write(path, ident.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                Files.write(path, left.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                constExp.walk();
                if (right!=null){
                    Files.write(path, right.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                }
            } else if (type==VarDefType.initialized_dim1) {
                Files.write(path, ident.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                Files.write(path, left.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                constExp.walk();
                if (right!=null){
                    Files.write(path, right.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                }
                Files.write(path, eq.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                initVal.walk();
            }
            Files.write(path, "<VarDef>\n".getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
        } catch (IOException e) {

        }
    }
    public String getName(){
        return ident.getValue();
    }
    public int getDim(){
        if (type==VarDefType.initialized_dim0 || type==VarDefType.notinitialized_dim0){
            return 0;
        }else {
            return 1;
        }
    }
}
