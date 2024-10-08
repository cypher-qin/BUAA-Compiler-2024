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

public class VarDecl implements CommonTreeNode{
    private Token btype;
    private ArrayList<VarDef> varDefs;
    private Token symbol;
    public VarDecl(Token t2,VarDef t3){
        varDefs=new ArrayList<>();
        btype=t2;
        varDefs.add(t3);
    }
    public void addVarDef(VarDef c){
        varDefs.add(c);
    }
    public void end(Token t4){
        symbol=t4;
    }
    public void walk(){
        Path path = Paths.get("parser.txt");
        try {
            Files.write(path, btype.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
            varDefs.get(0).walk();
            for (int i=1;i<varDefs.size();i++){
                Token tmp=new Token(btype.getLineno(), Type.COMMA,",");
                Files.write(path, tmp.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                varDefs.get(i).walk();
            }
            if (symbol!=null){
                Files.write(path, symbol.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
            }
            Files.write(path, "<VarDecl>\n".getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
        } catch (IOException e) {

        }
    }
}
