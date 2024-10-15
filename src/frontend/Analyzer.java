package frontend;

import frontend.TreeNode.*;
import Error.Error;

import java.util.ArrayList;

public class Analyzer {
    private final ArrayList<Error> errors;
    private final CompUnit compUnit;
    private final STManger manger;
    private int flag_for;
    public ArrayList<Error> getErrors(){
        return errors;
    }
    public Analyzer(CompUnit cu){
        manger=new STManger();
        compUnit=cu;
        errors=new ArrayList<>();
        flag_for=0;
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
        checkreturn(1,mdf.getBlk(),mdf.blk.right.getLineno());
        analyzeBlock(null,mdf.getBlk());
    }
    public void analyzeDecl(Decl decl){
        int isconst=decl.getIsconst();
        if (isconst==1){
            analyzeConstDecl(decl.getConstDecl());
        }else {
            analyzeVarDecl(decl.getVarDecl());
        }
    }
    public void analyzeConstDecl(ConstDecl cd){
        int id=manger.table_now.getTable_id();
        String btype;
        ArrayList<String> nameList=new ArrayList<>();
        ArrayList<String> typeList=new ArrayList<>();
        ArrayList<Integer> linenolist=new ArrayList<>();
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
            linenolist.add(cdf.get_ident_lineno());
            nameList.add(cdf.getName());
        }
        for (int i=0;i<nameList.size();i++){
            SymTableNode stn=new SymTableNode(id,nameList.get(i),typeList.get(i),btype,1);
            if (manger.checksame(stn.name)){
                errors.add(new Error('b',linenolist.get(i)));
            }else {
                manger.add_node(stn);
            }
        }
    }
    public void analyzeVarDecl(VarDecl vd){
        int id=manger.table_now.getTable_id();
        String btype;
        ArrayList<String> nameList=new ArrayList<>();
        ArrayList<String> typeList=new ArrayList<>();
        ArrayList<Integer> linenolist=new ArrayList<>();
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
            if (vdf.initVal!=null){
                analyzeInitial(vdf.initVal);
            }
            linenolist.add(vdf.get_ident_lineno());
            nameList.add(vdf.getName());
        }
        for (int i=0;i<nameList.size();i++){
            SymTableNode stn=new SymTableNode(id,nameList.get(i),typeList.get(i),btype,0);
            if (manger.checksame(stn.name)){
                errors.add(new Error('b',linenolist.get(i)));
            }else {
                manger.add_node(stn);
            }
        }
    }
    public void analyzeInitial(InitVal initVal){
        for (Exp exp : initVal.expList){
            analyzeExp(exp);
        }
    }
    public void analyzeFunc(FuncDef fdf){
        int id=manger.table_now.getTable_id();
        String type="FUNC";
        String btype=fdf.getType();
        String name=fdf.getName();
        SymTableNode stn=new SymTableNode(id,name,type,btype,0);
        if (manger.checksame(name)){
            errors.add(new Error('b',fdf.get_ident_lineno()));
        }else {
            manger.add_node(stn);
        }
        FuncFParams fps=fdf.getFps();
        int idnew=manger.getTable_num()+1;
        ArrayList<SymTableNode> nodes=new ArrayList<>();
        if (fps!=null){
            ArrayList<FuncFParam> fplist=fps.getList();
            for (FuncFParam fp: fplist){
                stn.setFPSTYPE(fp);
                SymTableNode stn1=new SymTableNode(idnew,fp.getName(),fp.getDim(),fp.getBtype(),0);
                nodes.add(stn1);
                stn1.setLineno(fp.get_ident_lineno());
            }
        }
        if (btype.equals("VOID")){
            checkreturn(0,fdf.getBlk(),fdf.getBlk().right.getLineno());
        } else {
            checkreturn(1,fdf.getBlk(),fdf.getBlk().right.getLineno());
        }
        analyzeBlock(nodes,fdf.getBlk());
    }
    public void analyzeBlock(ArrayList<SymTableNode> nodes,Block blk){
        manger.enter_new_block();
        //处理可能存在的参数表
        if (nodes!=null){
            for (SymTableNode stn : nodes){
                if (manger.checksame(stn.name)){
                    errors.add(new Error('b',stn.lineno));
                }else {
                    manger.add_node(stn);
                }
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
        if (stmt.getType()== Stmt.StmtType.ASSIGN){
            String name=stmt.lVal_assign.ident.getValue();
            if (manger.getSTN(name)!=null && manger.getSTN(name).isconst==1){
                errors.add(new Error('h',stmt.lVal_assign.ident.getLineno()));
            }
            analyzeLval(stmt.lVal_assign);
            analyzeExp(stmt.exp_assign);
        } else if (stmt.getType()== Stmt.StmtType.EXP) {
            if (stmt.exp_exp!=null){
                analyzeExp(stmt.exp_exp);
            }
        }else if (stmt.getType()==Stmt.StmtType.BLOCK){
            analyzeBlock(null,stmt.getBlock());
        }else if (stmt.getType()== Stmt.StmtType.IF){
            analyzeCond(stmt.cond_if);
            analyzeStmt(stmt.getStmt1_if());
            if (stmt.judge_if_type()==0){
                analyzeStmt(stmt.getStmt2_if());
            }
        }else if (stmt.getType()== Stmt.StmtType.FOR){
            analyzeForStmt(stmt.forStmt1);
            analyzeCond(stmt.cond_for);
            analyzeForStmt(stmt.forStmt2);
            flag_for++;
            analyzeStmt(stmt.getStmt_for());
            flag_for--;
        } else if (stmt.getType()== Stmt.StmtType.RETURN) {
            if (stmt.exp_re!=null){
                analyzeExp(stmt.exp_re);
            }
        } else if (stmt.getType()== Stmt.StmtType.GET_INT_GET_CHAR) {
            String name=stmt.lVal_ic.ident.getValue();
            if (manger.getSTN(name).isconst==1){
                errors.add(new Error('h',stmt.lVal_ic.ident.getLineno()));
            }
            analyzeLval(stmt.lVal_ic);
        } else if (stmt.getType()== Stmt.StmtType.PRINT) {
            int num=stmt.expList_p.size();
            String printstring=stmt.string_p.getValue();
            int expect=readstring(printstring);
            if (expect!=num){
                errors.add(new Error('l',stmt.print.getLineno()));
            }
            for (Exp exp: stmt.expList_p){
                analyzeExp(exp);
            }
        }else if (stmt.getType()== Stmt.StmtType.BREAK_CONTINUE){
            if (flag_for==0){
                errors.add(new Error('m',stmt.bc_bc.getLineno()));
            }
        }
    }
    public void analyzeForStmt(ForStmt forStmt){
        if (forStmt==null){
            return;
        }
        String name=forStmt.lVal.ident.getValue();
        if (manger.getSTN(name)!=null && manger.getSTN(name).isconst==1){
            errors.add(new Error('h',forStmt.lVal.ident.getLineno()));
        }
        analyzeLval(forStmt.lVal);
        analyzeExp(forStmt.exp);
    }
    //Exp
    public void analyzeExp(Exp exp){
        analyzeAddExp(exp.addExp);
    }
    public void analyzeAddExp(AddExp addExp){
        if (addExp.type== AddExp.AddExpType.SINGLE){
            analyzeMulExp(addExp.mulExp);
        }else {
            analyzeAddExp(addExp.addExp);
            analyzeMulExp(addExp.mulExp);
        }
    }
    public void analyzeMulExp(MulExp mulExp){
        if (mulExp.type== MulExp.MulExpType.SINGLE){
            analyzeUnaryExp(mulExp.unaryExp);
        }else {
            analyzeMulExp(mulExp.mulExp);
            analyzeUnaryExp(mulExp.unaryExp);
        }
    }
    public void analyzeUnaryExp(UnaryExp unaryExp){
        if (unaryExp.type== UnaryExp.UnaryExpType.PrimaryExp_TYPE){
            analyzePrimaryExp(unaryExp.primaryExp);
        } else if (unaryExp.type== UnaryExp.UnaryExpType.Function_TYPE) {
            if (!manger.find(unaryExp.ident.getValue())){
                errors.add(new Error('c',unaryExp.ident.getLineno()));
            }else {
                SymTableNode stn=manger.getSTN(unaryExp.ident.getValue());
                int size1;
                if (unaryExp.funcRParams==null){
                    size1=0;
                }else {size1=unaryExp.funcRParams.exps.size();}
                if (size1!=stn.fpstypes.size()){
                    errors.add(new Error('d',unaryExp.ident.getLineno()));
                }else if (size1!=0){
                    ArrayList<SymTableNode.FPSTYPE> typeList=stn.fpstypes;
                    ArrayList<Exp> list=unaryExp.funcRParams.exps;
                    for (int i=0;i<list.size();i++){
                        int result=judgeExpType(list.get(i));
                        if (result==0){
                            if (typeList.get(i)== SymTableNode.FPSTYPE.INTDIM1 || typeList.get(i)== SymTableNode.FPSTYPE.CHARDIM1){
                                errors.add(new Error('e',unaryExp.ident.getLineno()));
                                break;
                            }
                        } else if (result==1) {
                            if (typeList.get(i)!= SymTableNode.FPSTYPE.INTDIM1){
                                errors.add(new Error('e',unaryExp.ident.getLineno()));
                                break;
                            }
                        }else {
                            if (typeList.get(i)!= SymTableNode.FPSTYPE.CHARDIM1){
                                errors.add(new Error('e',unaryExp.ident.getLineno()));
                                break;
                            }
                        }
                    }
                }
            }
            analyzeFuncRParams(unaryExp.funcRParams);
        }else {
            analyzeUnaryExp(unaryExp.unaryExp);
        }
    }
    public int judgeExpType(Exp exp){
        //0 normal
        //1 intDim1
        //2 charDim1
        AddExp a=exp.addExp;
        if (a.type== AddExp.AddExpType.DOUBLE){
            return 0;
        }else if (a.mulExp.type== MulExp.MulExpType.DOUBLE){
            return 0;
        } else if (a.mulExp.unaryExp.type!= UnaryExp.UnaryExpType.PrimaryExp_TYPE) {
            return 0;
        }else if (a.mulExp.unaryExp.primaryExp.type== PrimaryExp.PrimaryExpType.EXP_TYPE){
            return 0;
        }else if (a.mulExp.unaryExp.primaryExp.type== PrimaryExp.PrimaryExpType.LVal_TYPE){
            LVal l=a.mulExp.unaryExp.primaryExp.lVal;
            if (l.type== LVal.LValType.Dim1){
                return 0;
            }else {
                SymTableNode stn1=manger.getSTN(l.ident.getValue());
                if (stn1.type.equals("ARRAY") && stn1.btype.equals("INT")){
                    return 1;
                } else if (stn1.type.equals("ARRAY") && stn1.btype.equals("CHAR")) {
                    return 2;
                }
            }
        }
        return 0;
    }
    public void analyzeLval(LVal lVal){
        if (!manger.find(lVal.ident.getValue())){
            errors.add(new Error('c',lVal.ident.getLineno()));
        }
        if (lVal.type== LVal.LValType.Dim1){
            analyzeExp(lVal.exp);
        }
    }
    public void analyzePrimaryExp(PrimaryExp primaryExp){
        if (primaryExp.type== PrimaryExp.PrimaryExpType.EXP_TYPE){
            analyzeExp(primaryExp.exp);
        } else if (primaryExp.type== PrimaryExp.PrimaryExpType.LVal_TYPE) {
            analyzeLval(primaryExp.lVal);
        }
    }
    public void analyzeFuncRParams(FuncRParams frs){
        if (frs==null){
            return;
        }
        for (Exp exp:frs.exps){
            analyzeExp(exp);
        }
    }
    //Cond
    public void analyzeCond(Cond cond){
        if (cond==null){
            return;
        }
        analyzeLOrExp(cond.lOrExp);
    }
    public void analyzeLOrExp(LOrExp lOrExp){
        if (lOrExp.type== LOrExp.LOrExpType.DOUBLE){
            analyzeLOrExp(lOrExp.lOrExp);
        }
        analyzeLAndExp(lOrExp.lAndExp);
    }
    public void analyzeLAndExp(LAndExp lAndExp){
        if (lAndExp.type== LAndExp.LAndExpType.DOUBLE){
            analyzeLAndExp(lAndExp.lAndExp);
        }
        analyzeEqExp(lAndExp.eqExp);
    }
    public void analyzeEqExp(EqExp eqExp){
        if (eqExp.type== EqExp.EqExpType.DOUBLE){
            analyzeEqExp(eqExp.eqExp);
        }
        analyzeRelExp(eqExp.relExp);
    }
    public void analyzeRelExp(RelExp relExp){
        if (relExp.type== RelExp.RelExpType.DOUBLE){
            analyzeRelExp(relExp.relExp);
        }
        analyzeAddExp(relExp.addExp);
    }
    public void checkreturn(int flag,Block block,int lineno){
        ArrayList<BlockItem> blockItems=block.getBis();
        if (blockItems==null){
            return;
        }
        if (flag==1){
            if (blockItems.isEmpty()){
                errors.add(new Error('g',lineno));
                return;
            }
            BlockItem bi=blockItems.get(blockItems.size()-1);
            if (bi.getType()==0 && bi.getStmt().getType()== Stmt.StmtType.RETURN){
            }else {
                errors.add(new Error('g',lineno));
            }
        }else {
            for (BlockItem bit: blockItems){
                if (bit.getType()==0){
                    Stmt s=bit.getStmt();
                    if (s.getType()== Stmt.StmtType.RETURN && s.exp_re!=null){
                        errors.add(new Error('f',s.re.getLineno()));
                    } else if (s.getType()==Stmt.StmtType.BLOCK) {
                        checkreturn(flag,s.getBlock(),lineno);
                    } else if (s.getType()== Stmt.StmtType.IF) {
                        Stmt s1=s.getStmt1_if();
                        if (s1.getType()== Stmt.StmtType.RETURN && s1.exp_re!=null){
                            errors.add(new Error('f',s1.re.getLineno()));
                        }else if (s1.getType()==Stmt.StmtType.BLOCK){
                            checkreturn(flag,s1.getBlock(),lineno);
                        }
                        Stmt s2=s.getStmt2_if();
                        if (s2!=null){
                            if (s2.getType()== Stmt.StmtType.RETURN && s2.exp_re!=null){
                                errors.add(new Error('f',s2.re.getLineno()));
                            }else if (s2.getType()==Stmt.StmtType.BLOCK){
                                checkreturn(flag,s2.getBlock(),lineno);
                            }
                        }
                    } else if (s.getType()==Stmt.StmtType.FOR) {
                        Stmt s1=s.getStmt_for();
                        if (s1.getType()== Stmt.StmtType.RETURN && s1.exp_re!=null){
                            errors.add(new Error('f',s1.re.getLineno()));
                        }else if (s1.getType()==Stmt.StmtType.BLOCK){
                            checkreturn(flag,s1.getBlock(),lineno);
                        }
                    }
                }
            }
        }
    }
    public int readstring(String s){
        int ptr=0;
        int num=0;
        while (ptr<s.length()){
            if (s.charAt(ptr)=='%'&&(s.charAt(ptr+1)=='d'||s.charAt(ptr+1)=='c')){
                num++;
                ptr+=2;
            }else {
                ptr++;
            }
        }
        return num;
    }
}
