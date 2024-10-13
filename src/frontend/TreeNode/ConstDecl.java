package frontend.TreeNode;

import frontend.Token;
import frontend.Type;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class ConstDecl implements CommonTreeNode{
    private Token const_string;
    private Token btype;
    private ArrayList<ConstDef> constDefs;
    private Token symbol;
    public ConstDecl(Token t1,Token t2,ConstDef t3){
        constDefs=new ArrayList<>();
        btype=t2;
        constDefs.add(t3);
    }
    public void addConstDef(ConstDef c){
        constDefs.add(c);
    }
    public void end(Token t4){
        symbol=t4;
    }
    public Token getBtype(){return btype;}
    public ArrayList<ConstDef> getConstDefs(){return constDefs;}
    public void walk(){
        Path path = Paths.get("parser.txt");
        try {
            Files.write(path, const_string.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
            Files.write(path, btype.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
            constDefs.get(0).walk();
            int lineno=btype.getLineno();
            for (int i=1;i< constDefs.size();i++){
                Token tmp=new Token(lineno, Type.COMMA,",");
                Files.write(path, tmp.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                constDefs.get(i).walk();
            }
            if (symbol!=null){
                Files.write(path, symbol.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
            }
            Files.write(path, "<ConstDecl>\n".getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
