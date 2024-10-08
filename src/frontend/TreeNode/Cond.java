package frontend.TreeNode;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Cond implements CommonTreeNode{
    private LOrExp lOrExp;
    public Cond(LOrExp lOrExp){
        this.lOrExp=lOrExp;
    }
    public void walk(){
        lOrExp.walk();
        Path path = Paths.get("parser.txt");
        try {
            Files.write(path, "<Cond>\n".getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
