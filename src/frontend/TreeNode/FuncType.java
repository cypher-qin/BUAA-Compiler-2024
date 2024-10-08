package frontend.TreeNode;

import frontend.Token;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FuncType implements CommonTreeNode{
    private Token type;
    public FuncType(Token t){
        type=t;
    }
    public void walk(){
        Path path = Paths.get("parser.txt");
        try {
            Files.write(path, type.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
            Files.write(path, "<FuncType>\n".getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
        } catch (IOException e) {

        }
    }
}
