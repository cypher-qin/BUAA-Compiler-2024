package frontend.TreeNode;

import frontend.Token;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class Block implements CommonTreeNode{
    private Token left;
    public Token right;
    private ArrayList<BlockItem> bis;
    public Block(Token left){
        bis=new ArrayList<>();
        this.left=left;
    }
    public void addBlockItem(BlockItem bi){
        bis.add(bi);
    }
    public void endAdd(Token right){
        this.right=right;
    }
    public void walk(){
        Path path = Paths.get("parser.txt");
        try {
            Files.write(path, left.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (BlockItem bit:bis){
            bit.walk();
        }
        try {
            Files.write(path, right.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            Files.write(path, "<Block>\n".getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public ArrayList<BlockItem> getBis(){
        return bis;
    }
}
