package frontend;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class SymbolTable {
    private int parent_id;
    private ArrayList<SymbolTable> childs;
    private ArrayList<SymTableNode> nodes;
    private int table_id;

    public SymbolTable(int parent,int this_id){
        this.parent_id=parent;
        this.table_id=this_id;
        nodes=new ArrayList<>();
        childs=new ArrayList<>();
    }
    public void addNode(SymTableNode sn){
        nodes.add(sn);
    }
    public void addChild(SymbolTable st){
        childs.add(st);
    }
    public int getTable_id(){return table_id;}
    public int getParent_id(){return parent_id;}
    public void print(){
        Path path = Paths.get("symbol.txt");
        for (SymTableNode stn : nodes){
            try {
                Files.write(path, stn.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
            } catch (IOException e) {

            }
        }
        for (SymbolTable st : childs){
            st.print();
        }
    }
}
