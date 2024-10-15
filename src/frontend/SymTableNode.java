package frontend;

import frontend.TreeNode.FuncFParam;

import java.util.ArrayList;

public class SymTableNode {
    public final int tableid;
    public final String name;
    public final String type;//VAR ARRAY FUNC
    public final String btype;//INT CHAR VOID
    public final int isconst;
    public int lineno;
    public enum FPSTYPE{
        INTDIM0,
        INTDIM1,
        CHARDIM0,
        CHARDIM1;
    }
    public ArrayList<FPSTYPE> fpstypes=new ArrayList<>();
    public SymTableNode(int tableid,String name,String type,String btype,int isconst){
        this.tableid=tableid;
        this.name=name;
        this.type=type;
        this.btype=btype;
        this.isconst=isconst;
    }
    public void setLineno(int lineno){
        this.lineno=lineno;
    }
    public void setFPSTYPE(FuncFParam fps){
        if (fps.type== FuncFParam.FuncFParamType.Dim0){
            if (fps.btype.getType()==Type.INT){
                fpstypes.add(FPSTYPE.INTDIM0);
            }else {
                fpstypes.add(FPSTYPE.CHARDIM0);
            }
        }else {
            if (fps.btype.getType()==Type.INT){
                fpstypes.add(FPSTYPE.INTDIM1);
            }else {
                fpstypes.add(FPSTYPE.CHARDIM1);
            }
        }
    }
    @Override
    public String toString(){
        String tmp = null;
        if (type.equals("VAR") && btype.equals("CHAR") && isconst==1){
            tmp="ConstChar";
        }else if (type.equals("VAR") && btype.equals("INT") && isconst==1){
            tmp="ConstInt";
        }else if (type.equals("ARRAY") && btype.equals("CHAR") && isconst==1){
            tmp="ConstCharArray";
        }else if (type.equals("ARRAY") && btype.equals("INT") && isconst==1){
            tmp="ConstIntArray";
        }else if (type.equals("VAR") && btype.equals("CHAR") && isconst==0){
            tmp="Char";
        }else if (type.equals("VAR") && btype.equals("INT") && isconst==0){
            tmp="Int";
        }else if (type.equals("ARRAY") && btype.equals("CHAR") && isconst==0){
            tmp="CharArray";
        }else if (type.equals("ARRAY") && btype.equals("INT") && isconst==0){
            tmp="IntArray";
        }else if (type.equals("FUNC") && btype.equals("VOID")){
            tmp="VoidFunc";
        }else if (type.equals("FUNC") && btype.equals("CHAR")){
            tmp="CharFunc";
        }else if (type.equals("FUNC") && btype.equals("INT")){
            tmp="IntFunc";
        } else {
            System.out.println("程序流豆浆了! SymTableNode-toString");
        }
        return tableid+" "+name+" "+tmp+"\n";
    }
}
