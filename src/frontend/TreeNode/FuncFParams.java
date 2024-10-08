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

public class FuncFParams implements CommonTreeNode{
    private ArrayList<FuncFParam> fps;
    public FuncFParams(FuncFParam fp){
        fps=new ArrayList<>();
        fps.add(fp);
    }
    public void addFP(FuncFParam fp){
        fps.add(fp);
    }
    public void walk(){
        Path path = Paths.get("parser.txt");
        try {
            fps.get(0).walk();
            for (int i=1;i<fps.size();i++){
                Token tmp=new Token(1, Type.COMMA,",");
                Files.write(path, tmp.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
                fps.get(i).walk();
            }
            Files.write(path, "<FuncFParams>\n".getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
        } catch (IOException e) {

        }


    }
}
