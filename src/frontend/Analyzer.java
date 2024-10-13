package frontend;

import frontend.TreeNode.*;
import Error.Error;

import java.util.ArrayList;

public class Analyzer {
    private final ArrayList<Error> errors;
    private final CompUnit compUnit;
    private final STManger manger;
    public ArrayList<Error> getErrors(){
        return errors;
    }
    public Analyzer(CompUnit cu){
        manger=new STManger();
        compUnit=cu;
        errors=new ArrayList<>();
    }
    public STManger getManger(){return manger;}
    public void analyzeCompUnit(){
        ArrayList<Decl> decls=compUnit.getDeclList();
        for (Decl decl : decls){
            analyzeDecl(decl);
        }
        ArrayList<FuncDef> funcDefs=compUnit.getFuncList();
        for (FuncDef fdf: funcDefs){
            analyzeFunc(fdf);
        }
        analyzeMainFunc(compUnit.getMainFuncDef());
    }
    public void analyzeMainFunc(MainFuncDef mdf){
        analyzeBlock(null,mdf.getBlk());
    }
    public void analyzeDecl(Decl decl){
        int id=manger.table_now.getTable_id();
        int isconst=decl.getIsconst();
        //获得btype int or char
        //获得nameList
        //获得typeList dim1 or dim0
        String btype;
        ArrayList<String> nameList=new ArrayList<>();
        ArrayList<String> typeList=new ArrayList<>();
        if (isconst==1){
            ConstDecl cd=decl.getConstDecl();
            ArrayList<ConstDef> constDefs=cd.getConstDefs();
            if (cd.getBtype().getType()==Type.INT){
                btype="INT";
            }else {
                btype="CHAR";
            }
            for (ConstDef cdf : constDefs){
                if (cdf.getDim()==0){
                    typeList.add("VAR");
                }else {
                    typeList.add("ARRAY");
                }
                nameList.add(cdf.getName());
            }
            for (int i=0;i<nameList.size();i++){
                SymTableNode stn=new SymTableNode(id,nameList.get(i),typeList.get(i),btype,isconst);
                manger.add_node(stn);
            }
        }else {
            VarDecl vd=decl.getVarDecl();
            ArrayList<VarDef> varDefs=vd.getVarDefs();
            if (vd.getbType().getType()==Type.INT){
                btype="INT";
            }else {
                btype="CHAR";
            }
            for (VarDef vdf : varDefs){
                if (vdf.getDim()==0){
                    typeList.add("VAR");
                }else {
                    typeList.add("ARRAY");
                }
                nameList.add(vdf.getName());
            }
            for (int i=0;i<nameList.size();i++){
                SymTableNode stn=new SymTableNode(id,nameList.get(i),typeList.get(i),btype,isconst);
                manger.add_node(stn);
            }
        }
    }
    public void analyzeFunc(FuncDef fdf){
        int id=manger.table_now.getTable_id();
        String type="FUNC";
        String btype=fdf.getType();
        String name=fdf.getName();
        SymTableNode stn=new SymTableNode(id,name,type,btype,0);
        manger.add_node(stn);
        FuncFParams fps=fdf.getFps();
        int idnew=manger.getTable_num()+1;
        ArrayList<SymTableNode> nodes=new ArrayList<>();
        if (fps!=null){
            ArrayList<FuncFParam> fplist=fps.getList();
            for (FuncFParam fp: fplist){
                SymTableNode stn1=new SymTableNode(idnew,fp.getName(),fp.getDim(),fp.getBtype(),0);
                nodes.add(stn1);
            }
        }
        analyzeBlock(nodes,fdf.getBlk());
    }
    public void analyzeBlock(ArrayList<SymTableNode> nodes,Block blk){
        manger.enter_new_block();
        //处理可能存在的参数表
        if (nodes!=null){
            for (SymTableNode stn : nodes){
                manger.add_node(stn);
            }
        }
        ArrayList<BlockItem> blockItems=blk.getBis();
        for (BlockItem blockItem : blockItems){
            if (blockItem.getType()==1){
                analyzeDecl(blockItem.getDecl());
            }else {
                Stmt stmt=blockItem.getStmt();
                analyzeStmt(stmt);
            }
        }
        manger.get_back();
    }
    public void analyzeStmt(Stmt stmt){
        if (stmt.getType()==Stmt.StmtType.BLOCK){
            analyzeBlock(null,stmt.getBlock());
        }else if (stmt.getType()== Stmt.StmtType.IF){
            analyzeStmt(stmt.getStmt1_if());
            if (stmt.judge_if_type()==0){
                analyzeStmt(stmt.getStmt2_if());
            }
        }else if (stmt.getType()== Stmt.StmtType.FOR){
            analyzeStmt(stmt.getStmt_for());
        }
    }
}
