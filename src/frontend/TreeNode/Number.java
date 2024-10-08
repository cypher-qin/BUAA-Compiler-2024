package frontend.TreeNode;

import frontend.Token;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Number implements CommonTreeNode{
    private Token intconst;
    public Number(Token intconst){
        this.intconst=intconst;
    }
    public void walk(){
        Path path = Paths.get("parser.txt");
        try {
            Files.write(path, intconst.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
            Files.write(path, "<Number>\n".getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
