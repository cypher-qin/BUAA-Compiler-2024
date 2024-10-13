package frontend.TreeNode;

import frontend.Token;
import frontend.Type;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class Stmt implements CommonTreeNode{
    public enum StmtType{
        ASSIGN,
        EXP,
        BLOCK,
        IF,
        FOR,
        BREAK_CONTINUE,
        RETURN,
        GET_INT_GET_CHAR,
        PRINT;
    }
    private StmtType type;
    //ASSIGN
    private LVal lVal_assign;
    private Token eq_assign;
    private Exp exp_assign;
    private Token semic_assign;
    public Stmt(LVal lVal,Token eq,Exp exp,Token semic){
        type=StmtType.ASSIGN;
        lVal_assign=lVal;
        eq_assign=eq;
        exp_assign=exp;
        semic_assign=semic;
    }
    //EXP
    private Exp exp_exp;
    private Token semic_exp;
    public Stmt(Token semic,Exp exp){
        type=StmtType.EXP;
        exp_exp=exp;
        semic_exp=semic;
    }
    //BLOCK
    private Block block;
    public Stmt(Block blk){
        type=StmtType.BLOCK;
        block=blk;
    }
    //IF
    private Token if_if;
    private Token left_if;
    private Token right_if;
    private Cond cond_if;
    private Stmt stmt1_if;
    private Token else_if;
    private Stmt stmt2_if;
    public Stmt(Token t1,Token t2,Cond cond,Token t3,Stmt stmt){
        type=StmtType.IF;
        if_if=t1;
        left_if=t2;
        cond_if=cond;
        right_if=t3;
        stmt1_if=stmt;
    }
    public void add_ELSE(Token t1,Stmt stmt){
        else_if=t1;
        stmt2_if=stmt;
    }
    //FOR
    private Token for_for;
    private Token left_for;
    private Token right_for;
    private Token semic1_for;
    private Token semic2_for;
    private ForStmt forStmt1;
    private Cond cond_for;
    private ForStmt forStmt2;
    private Stmt stmt_for;
    public Stmt(Token t1,Token left_for,ForStmt forStmt1,Token semic1_for,Cond cond_for,Token semic2_for,ForStmt forStmt2,Token right_for,Stmt stmt_for){
        type=StmtType.FOR;
        for_for=t1;
        this.left_for=left_for;
        this.forStmt1=forStmt1;
        this.semic1_for=semic1_for;
        this.cond_for=cond_for;
        this.semic2_for=semic2_for;
        this.forStmt2=forStmt2;
        this.right_for=right_for;
        this.stmt_for=stmt_for;
    }
    //BREAK or Continue
    private Token bc_bc;
    private Token semic_bc;
    public Stmt(Token token_bc,Token semic_bc){
        type=StmtType.BREAK_CONTINUE;
        this.bc_bc=token_bc;
        this.semic_bc=semic_bc;
    }
    // return
    private Token re;
    private Exp exp_re;
    private Token semic_re;

    public Stmt(Token re,Exp exp_re,Token semic_re){
        type=StmtType.RETURN;
        this.re=re;
        this.exp_re=exp_re;
        this.semic_re=semic_re;
    }
    //GET_INT GET_CHAR
    private LVal lVal_ic;
    private Token t1_ic;
    private Token get_ic;
    private Token left_ic;
    private Token right_ic;
    private Token semic_ic;
    public Stmt(LVal lVal_ic,Token t1_ic,Token get_ic,Token left_ic,Token right_ic,Token semic_ic){
        type=StmtType.GET_INT_GET_CHAR;
        this.left_ic=left_ic;
        this.t1_ic=t1_ic;
        this.get_ic=get_ic;
        this.lVal_ic=lVal_ic;
        this.right_ic=right_ic;
        this.semic_ic=semic_ic;
    }
    //Print
    private Token print;
    private Token left_p;
    private Token string_p;
    private ArrayList<Exp> expList_p;
    private Token right_p;
    private Token semic_p;
    public Stmt(Token print,Token left_p,Token string_p){
        expList_p=new ArrayList<>();
        type=StmtType.PRINT;
        this.print=print;
        this.left_p=left_p;
        this.string_p=string_p;
    }
    public void addPrintExp(Exp exp){
        expList_p.add(exp);
    }
    public void endEnd_Print(Token right_p,Token semic_p){
        this.right_p=right_p;
        this.semic_p=semic_p;
    }
    public void walk(){
        Path path = Paths.get("parser.txt");
        try {
            if (type==StmtType.ASSIGN){
                lVal_assign.walk();
                Files.write(path, eq_assign.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
                exp_assign.walk();
                if (semic_assign!=null) {
                    Files.write(path, semic_assign.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                }
            } else if (type==StmtType.EXP) {
                if (exp_exp!=null){
                    exp_exp.walk();
                }
                if (semic_exp!=null) {
                    Files.write(path, semic_exp.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                }
            } else if (type==StmtType.BLOCK) {
                block.walk();
            } else if (type==StmtType.IF) {
                Files.write(path, if_if.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                Files.write(path, left_if.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                cond_if.walk();
                if (right_if!=null){
                    Files.write(path, right_if.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                }
                stmt1_if.walk();
                if (else_if!=null){
                    Files.write(path, else_if.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                    stmt2_if.walk();
                }
            } else if (type==StmtType.FOR) {
                Files.write(path, for_for.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                Files.write(path, left_for.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                if (forStmt1!=null){forStmt1.walk();}
                Files.write(path, semic1_for.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                if (cond_for!=null){cond_for.walk();}
                Files.write(path, semic2_for.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                if (forStmt2!=null){forStmt2.walk();}
                if (right_for!=null){
                    Files.write(path, right_for.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                }
                stmt_for.walk();
            } else if (type==StmtType.BREAK_CONTINUE) {
                Files.write(path, bc_bc.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                if (semic_bc!=null){
                    Files.write(path, semic_bc.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                }
            }else if (type==StmtType.RETURN) {
                Files.write(path, re.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                if (exp_re!=null){
                    exp_re.walk();
                }
                if (semic_re!=null){
                    Files.write(path, semic_re.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                }
            }else if (type==StmtType.GET_INT_GET_CHAR) {
                lVal_ic.walk();
                Files.write(path, t1_ic.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                Files.write(path, get_ic.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                Files.write(path, left_ic.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                if (right_ic!=null){
                    Files.write(path, right_ic.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                }
                if (semic_ic!=null){
                    Files.write(path, semic_ic.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                }
            } else if (type==StmtType.PRINT) {
                Files.write(path, print.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                Files.write(path, left_p.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                Files.write(path, string_p.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                if (!expList_p.isEmpty()){
                    for (Exp exp : expList_p) {
                        Token tmp = new Token(1, Type.COMMA, ",");
                        Files.write(path, tmp.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                        exp.walk();
                    }
                }
                if (right_p!=null){
                    Files.write(path, right_p.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                }
                if (semic_p!=null){
                    Files.write(path, semic_p.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
                }
            }
            Files.write(path, "<Stmt>\n".getBytes(StandardCharsets.UTF_8),StandardOpenOption.APPEND);
        } catch (IOException e) {

        }
    }
    public StmtType getType(){
        return type;
    }
    public Block getBlock(){
        return block;
    }
    public Stmt getStmt1_if(){
        return stmt1_if;
    }
    public Stmt getStmt2_if(){
        return stmt2_if;
    }
    public int judge_if_type(){
        if (else_if==null){
            return 1;
        }else {
            return 0;
        }
    }
    public Stmt getStmt_for(){
        return stmt_for;
    }
}
