package frontend.TreeNode;

import frontend.Token;
import frontend.Type;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FuncDef implements CommonTreeNode{
    private FuncType type;
    private Token ident;
    private Token l;
    private Token r;
    private FuncFParams fps;
    private Block blk;

    public FuncDef(FuncType type,Token ident,Token left, Token right,FuncFParams FPS,Block BLK){
        this.type=type;
        this.ident=ident;
        this.l=left;
        this.r=right;
        this.fps=FPS;
        this.blk=BLK;
    }
    public void walk(){
        Path path = Paths.get("parser.txt");
        try {
            type.walk();
            Files.write(path, ident.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
            Files.write(path, l.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
            if (fps!=null){
                fps.walk();
            }
            if (r!=null){
                Files.write(path, r.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
            }
            blk.walk();
            Files.write(path, "<FuncDef>\n".getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
        } catch (IOException e) {

        }
    }
    public String getType(){
        if (type.getType().getType()== Type.INT){
            return "INT";
        } else if (type.getType().getType()== Type.CHAR) {
            return "CHAR";
        }else {
            return "VOID";
        }
    }
    public String getName(){
        return ident.getValue();
    }
    public FuncFParams getFps(){
        return fps;
    }
    public Block getBlk(){
        return blk;
    }
}
