package frontend.TreeNode;


public class Decl implements CommonTreeNode{
    public enum DeclType{
        Const_TYPE,
        Var_TYPE;
    }
    private DeclType type;
    private ConstDecl constDecl;
    private VarDecl varDecl;
    public Decl(ConstDecl c){
        type=DeclType.Const_TYPE;
        constDecl=c;
    }
    public Decl(VarDecl v){
        type=DeclType.Var_TYPE;
        varDecl=v;
    }
    public void walk(){
        if (type==DeclType.Const_TYPE){
            constDecl.walk();
        }else {
            varDecl.walk();
        }
    }
}
