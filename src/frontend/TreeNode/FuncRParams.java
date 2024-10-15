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

public class FuncRParams implements CommonTreeNode{
    public ArrayList<Exp> exps;
    public FuncRParams(Exp exp){
        exps=new ArrayList<>();
        exps.add(exp);
    }
    public void addExp(Exp exp){
        exps.add(exp);
    }
    public void walk(){
        Path path = Paths.get("parser.txt");
        try {
            exps.get(0).walk();
            for (int i=1;i<exps.size();i++){
                Token tmp=new Token(1, Type.COMMA,",");
                Files.write(path, tmp.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
                exps.get(i).walk();
            }
            Files.write(path, "<FuncRParams>\n".getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
        } catch (IOException e) {

        }
    }
}
