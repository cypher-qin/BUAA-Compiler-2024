package frontend.TreeNode;

public class BlockItem implements CommonTreeNode{
    public enum BlockItemType{
        DECL,
        STMT;
    }
    private Decl decl;
    private Stmt stmt;
    private BlockItemType type;

    public BlockItem(Decl decl){
        type=BlockItemType.DECL;
        this.decl=decl;
        this.stmt=null;
    }
    public BlockItem(Stmt stmt){
        type=BlockItemType.STMT;
        this.decl=null;
        this.stmt=stmt;
    }
    public void walk(){
        if (type==BlockItemType.DECL){
            decl.walk();
        }else {
            stmt.walk();
        }
    }
    public int getType(){
        if (type==BlockItemType.STMT){
            return 0;
        }else {
            return 1;
        }
    }
    public Decl getDecl(){
        return decl;
    }
    public Stmt getStmt(){
        return stmt;
    }
}
