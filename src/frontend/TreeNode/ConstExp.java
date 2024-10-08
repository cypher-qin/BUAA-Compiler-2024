package frontend.TreeNode;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class ConstExp implements CommonTreeNode{
    private AddExp addExp;
    public ConstExp(AddExp addExp){
        this.addExp=addExp;
    }
    public void walk(){
        addExp.walk();
        Path path = Paths.get("parser.txt");
        try {
            Files.write(path, "<ConstExp>\n".getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
        } catch (IOException e) {

        }
    }
}
