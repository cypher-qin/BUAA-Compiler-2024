package frontend;

import java.util.HashMap;

public class STManger {
    private final HashMap<Integer,SymbolTable> symbolTables;
    private int table_num;
    public SymbolTable table_now;
    public STManger(){
        symbolTables=new HashMap<>();
        SymbolTable tmp=new SymbolTable(0,1);
        symbolTables.put(1,tmp);
        table_now=tmp;
        table_num=1;
    }
    public void enter_new_block(){
        table_num++;
        SymbolTable nt=new SymbolTable(table_now.getTable_id(),table_num);
        table_now.addChild(nt);
        symbolTables.put(nt.getTable_id(),nt);
        table_now=nt;
    }
    public void get_back(){
        table_now=symbolTables.get(table_now.getParent_id());
    }
    public void add_node(SymTableNode stn){
        table_now.addNode(stn);
    }
    public int getTable_num(){return table_num;}
    public SymbolTable getTable(){return symbolTables.get(1);}

    public boolean checksame(String name){
        for(SymTableNode symTableNode : table_now.nodes){
            if (symTableNode.name.equals(name)){
                return true;
            }
        }
        return false;
    }
    public boolean find(String name){
        int id=table_now.getTable_id();
        while (id!=0){
            for (SymTableNode stn:symbolTables.get(id).nodes){
                if (stn.name.equals(name)){
                    return true;
                }
            }
            id=symbolTables.get(id).getParent_id();
        }
        return false;
    }
    public SymTableNode getSTN(String name){
        int id=table_now.getTable_id();
        while (id!=0){
            for (SymTableNode stn:symbolTables.get(id).nodes){
                if (stn.name.equals(name)){
                    return stn;
                }
            }
            id=symbolTables.get(id).getParent_id();
        }
        return null;
    }
}
