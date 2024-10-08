package frontend.TreeNode;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class CompUnit implements CommonTreeNode{
    private ArrayList<Decl> decl;
    private ArrayList<FuncDef> funcDef;
    private MainFuncDef mainFuncDef;
    public CompUnit(MainFuncDef m){
        decl=new ArrayList<>();
        funcDef=new ArrayList<>();
        mainFuncDef=m;
    }
    public void addDecl(Decl d){
        decl.add(d);
    }
    public void addFuncDef(FuncDef f){
        funcDef.add(f);
    }
    public void walk(){
        if (!decl.isEmpty()){
            for (Decl d : decl){
                d.walk();
            }
        }
        if (!funcDef.isEmpty()){
            for (FuncDef f : funcDef){
                f.walk();
            }
        }
        mainFuncDef.walk();
        Path path = Paths.get("parser.txt");
        try {
            Files.write(path, "<CompUnit>\n".getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
