package frontend.TreeNode;

import frontend.Token;
import frontend.Type;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FuncFParam implements CommonTreeNode{
    public enum FuncFParamType{
        Dim0,
        Dim1;
    }
    public Token btype;
    private Token ident;
    private Token l;

    private Token r;
    public FuncFParamType type;

    public FuncFParam(Token btype,Token ident){
        this.type=FuncFParamType.Dim0;
        l=null;
        r=null;
        this.btype=btype;
        this.ident=ident;
    }
    public FuncFParam(Token btype,Token ident,Token left,Token right){
        this.type=FuncFParamType.Dim1;
        l=left;
        r=right;
        this.btype=btype;
        this.ident=ident;
    }
    public void walk(){
        Path path = Paths.get("parser.txt");
        try {
            Files.write(path, btype.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
            Files.write(path, ident.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
            if (type==FuncFParamType.Dim1){
                Files.write(path, l.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                if (r!=null){
                    Files.write(path, r.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                }
            }
            Files.write(path, "<FuncFParam>\n".getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
        } catch (IOException e) {

        }
    }
    public String getBtype(){
        if (btype.getType()== Type.INT){
            return "INT";
        }else {
            return "CHAR";
        }
    }
    public String getName(){
        return ident.getValue();
    }
    public String getDim(){
        if (type==FuncFParamType.Dim0){
            return "VAR";
        }else {
            return "ARRAY";
        }
    }
    public int get_ident_lineno(){
        return ident.getLineno();
    }
}
