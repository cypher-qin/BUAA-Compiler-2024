package frontend.TreeNode;

import frontend.Token;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Character implements CommonTreeNode{
    private Token charconst;
    public Character(Token charconst){
        this.charconst=charconst;
    }
    public void walk(){
        Path path = Paths.get("parser.txt");
        try {
            Files.write(path, charconst.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
            Files.write(path, "<Character>\n".getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
