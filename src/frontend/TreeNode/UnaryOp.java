package frontend.TreeNode;

import frontend.Token;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class UnaryOp implements CommonTreeNode{
    private Token sym;
    public UnaryOp(Token t){
        sym=t;
    }
    public void walk(){
        Path path = Paths.get("parser.txt");
        try {
            Files.write(path, sym.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
            Files.write(path, "<UnaryOp>\n".getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
        } catch (IOException e) {

        }
    }
}
