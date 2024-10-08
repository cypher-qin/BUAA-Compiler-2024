package frontend.TreeNode;

import frontend.Token;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class MainFuncDef implements CommonTreeNode{
    private Token int_token;
    private Token main_token;
    private Token left;
    private Token right;

    private Block blk;
    public MainFuncDef(Token int_token,Token main_token,Token left,Token right,Block blk){
        this.int_token=int_token;
        this.main_token=main_token;
        this.left=left;
        this.right=right;
        this.blk=blk;
    }
    public void walk(){
        Path path = Paths.get("parser.txt");
        try {
            Files.write(path, int_token.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
            Files.write(path, main_token.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
            Files.write(path, left.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
            if (right!=null){
                Files.write(path, right.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
            }
            blk.walk();
            Files.write(path, "<MainFuncDef>\n".getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
        } catch (IOException e) {

        }
    }
}
