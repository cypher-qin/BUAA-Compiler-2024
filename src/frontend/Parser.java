package frontend;

import frontend.TreeNode.*;
import frontend.TreeNode.Character;
import frontend.TreeNode.Number;
import Error.Error;
import java.util.ArrayList;
public class Parser {
    private final ArrayList<Token> tokens;
    private final ArrayList<Error> errors;
    private int ptr;
    private int function_type;
    public Parser(ArrayList<Token> tokeList){
        errors=new ArrayList<>();
        tokens=tokeList;
        function_type=0;
        ptr=0;
    }
    public ArrayList<Error> getErrors(){
        return errors;
    }
    public boolean isDefineHead(Type type){
        return type == Type.INT || type == Type.CHAR || type == Type.CONST;
    }
    public boolean isBType(Type type){
        return type == Type.INT || type == Type.CHAR ;
    }
    public boolean isFuncHead(Type type){
        return type == Type.INT || type == Type.CHAR || type==Type.VOID;
    }

    public boolean isExpHead(Type type){
        boolean isUnaryOp=type==Type.PLUS||type==Type.MINU||type==Type.NOT;
        return type==Type.IntConst || type==Type.CharConst || type==Type.IDENT || isUnaryOp || type==Type.LPAREN;
    }
    public CompUnit parseCompUnit(){
        //CompUnit → {Decl} {FuncDef} MainFuncDef
        ArrayList<Decl> declList=new ArrayList<>();
        ArrayList<FuncDef> funcList=new ArrayList<>();
        //可能会有一堆Decl,要保证解析完读入下一个
        while (isDefineHead(tokens.get(ptr).getType()) && tokens.get(ptr+1).getType()!=Type.MAIN && tokens.get(ptr+2).getType()!=Type.LPAREN){
            declList.add(parseDecl());
        }
        //可能会有一堆Fun,要保证解析完读入下一个
        while(isFuncHead(tokens.get(ptr).getType()) && tokens.get(ptr+1).getType()!=Type.MAIN){
            funcList.add(parseFuncDef());
        }
        MainFuncDef main=parseMainFuncDef();
        CompUnit compUnit=new CompUnit(main);
        //将之前解析到的东西加到compUnit实例中
        for (Decl d : declList){
            compUnit.addDecl(d);
        }
        for (FuncDef f : funcList){
            compUnit.addFuncDef(f);
        }
        return compUnit;
    }
    public Decl parseDecl(){
        //Decl → ConstDecl | VarDecl
        if (tokens.get(ptr).getType()==Type.CONST){
            ConstDecl cd=parseConstDecl();
            return new Decl(cd);
        }else {
            VarDecl vd=parseVarDecl();
            return new Decl(vd);
        }
    }
    public ConstDecl parseConstDecl(){
        //ConstDecl → 'const' BType ConstDef { ',' ConstDef } ';'//i
        Token consttoken=tokens.get(ptr);
        ptr++;
        Token btype=tokens.get(ptr);
        ptr++;
        ConstDef c0=parseConstDef();
        Token sym=tokens.get(ptr);
        ConstDecl cdl=new ConstDecl(consttoken,btype,c0);
        if (sym.getType()==Type.SEMICN){
            ptr++;
            cdl.end(sym);
            return cdl;
        }else if (sym.getType()==Type.COMMA){
            while (tokens.get(ptr).getType()==Type.COMMA){
                ptr++;
                ConstDef cdf=parseConstDef();
                cdl.addConstDef(cdf);
            }
            if (tokens.get(ptr).getType()==Type.SEMICN){
                cdl.end(tokens.get(ptr));
                ptr++;
                return cdl;
            }else {
                Error e=new Error('i',tokens.get(ptr-1).getLineno());
                errors.add(e);
            }
        } else {
            Error e=new Error('i',tokens.get(ptr-1).getLineno());
            errors.add(e);
        }
        return cdl;
    }
    public ConstDef parseConstDef(){
        //ConstDef → Ident [ '[' ConstExp ']' ] '=' ConstInitVal // k
        Token ident=tokens.get(ptr++);
        if (tokens.get(ptr).getType()==Type.LBRACK){
            Token left=tokens.get(ptr++);
            ConstExp ce=parseConstExp();
            Token right=tokens.get(ptr++);
            if (right.getType()!=Type.RBRACK){
                errors.add(new Error('k',tokens.get(ptr-1).getLineno()));
                Token eq=tokens.get(ptr-1);
                ConstInitVal ci=parseConstInitVal();
                return new ConstDef(ident,left,ce,null,eq,ci);
            }else {
                Token eq=tokens.get(ptr++);
                ConstInitVal ci=parseConstInitVal();
                return new ConstDef(ident,left,ce,right,eq,ci);
            }
        }else {
            Token eq=tokens.get(ptr++);
            ConstInitVal ci=parseConstInitVal();
            return new ConstDef(ident,eq,ci);
        }
    }
    public ConstInitVal parseConstInitVal(){
        //ConstInitVal → ConstExp | '{' [ ConstExp { ',' ConstExp } ] '}' | StringConst
        Token head=tokens.get(ptr++);
        Type head_token_type=head.getType();
        switch (head_token_type){
            case LBRACE:
                Token tmp=tokens.get(ptr);
                ConstInitVal ci0=new ConstInitVal(head,null);
                if (tmp.getType()!=Type.RBRACE){
                    ConstExp ce0=parseConstExp();
                    ci0.addConstExp(ce0);
                    while (tokens.get(ptr).getType()!=Type.RBRACE){
                        ptr++;
                        ci0.addConstExp(parseConstExp());
                    }
                    ci0.endConstInitVal(tokens.get(ptr++));
                }else {
                    ci0.endConstInitVal(tmp);
                    ptr++;
                }
                return ci0;
            case StringConst:
                return new ConstInitVal(head);
            default:
                ptr--;
                return new ConstInitVal(parseConstExp());
        }
    }
    public VarDecl parseVarDecl(){
        // VarDecl → BType VarDef { ',' VarDef } ';' // i
        Token head=tokens.get(ptr++);
        VarDef v0=parseVarDef();
        VarDecl vdl=new VarDecl(head,v0);
        while (tokens.get(ptr).getType()==Type.COMMA){
            ptr++;
            vdl.addVarDef(parseVarDef());
        }
        Token end=tokens.get(ptr);
        if (end.getType()==Type.SEMICN){
            ptr++;
            vdl.end(end);
        }else {
            errors.add(new Error('i',tokens.get(ptr-1).getLineno()));
        }
        return vdl;
    }
    public VarDef parseVarDef(){
        // VarDef → Ident [ '[' ConstExp ']' ] | Ident [ '[' ConstExp ']' ] '=' InitVal // k
        Token ident=tokens.get(ptr++);
        Type next_token_type=tokens.get(ptr).getType();
        switch (next_token_type){
            case LBRACK:
                Token left=tokens.get(ptr);
                ptr++;
                ConstExp ce0=parseConstExp();
                if (tokens.get(ptr).getType()==Type.RBRACK){
                    Token right=tokens.get(ptr);
                    ptr++;
                    if (tokens.get(ptr).getType()==Type.ASSIGN){
                        Token assign=tokens.get(ptr);
                        ptr++;
                        InitVal iv=parseInitVal();
                        return new VarDef(ident,left,right,ce0,assign,iv);
                    }else {
                        return new VarDef(ident,left,right,ce0);
                    }
                }else {
                    errors.add(new Error('k',tokens.get(ptr-1).getLineno()));
                    if (tokens.get(ptr).getType()==Type.ASSIGN){
                        Token assign=tokens.get(ptr++);
                        InitVal iv=parseInitVal();
                        return new VarDef(ident,left,null,ce0,assign,iv);
                    }else {
                        return new VarDef(ident,left,null,ce0);
                    }
                }
            case ASSIGN:
                Token assign=tokens.get(ptr++);
                InitVal iv=parseInitVal();
                return new VarDef(ident,assign,iv);
            default:
                return new VarDef(ident);
        }
    }
    public InitVal parseInitVal(){
        // InitVal → Exp | '{' [ Exp { ',' Exp } ] '}' | StringConst
        Token head=tokens.get(ptr++);
        Type head_token_type=head.getType();
        switch (head_token_type){
            case LBRACE:
                Token tmp=tokens.get(ptr);
                InitVal ci0=new InitVal(head,null);
                if (tmp.getType()!=Type.RBRACE){
                    Exp ce0=parseExp();
                    ci0.addExp(ce0);
                    while (tokens.get(ptr).getType()!=Type.RBRACE){
                        ptr++;
                        ci0.addExp(parseExp());
                    }
                    ci0.endAdd(tokens.get(ptr++));
                }else {
                    ci0.endAdd(tmp);
                    ptr++;
                }
                return ci0;
            case StringConst:
                return new InitVal(head);
            default:
                ptr--;
                return new InitVal(parseExp());
        }
    }
    public FuncDef parseFuncDef(){
        // FuncDef → FuncType Ident '(' [FuncFParams] ')' Block // j
        FuncType ft=parseFuncType();
        Token ident=tokens.get(ptr++);
        Token left=tokens.get(ptr++);
        if (tokens.get(ptr).getType()==Type.RPAREN){
            Token right=tokens.get(ptr);
            ptr++;
            Block block=parseBlock();

            return new FuncDef(ft,ident,left,right,null,block);
        }else if (isBType(tokens.get(ptr).getType())){
            FuncFParams fps=parseFuncFParams();
            Token right=tokens.get(ptr);
            if (right.getType()==Type.RPAREN){
                ptr++;
                Block block=parseBlock();
                return new FuncDef(ft,ident,left,right,fps,block);
            }else {
                errors.add(new Error('j',tokens.get(ptr-1).getLineno()));
                Block block=parseBlock();
                return new FuncDef(ft,ident,left,null,fps,block);
            }
        }else {
            errors.add(new Error('j',tokens.get(ptr-1).getLineno()));
            Block block=parseBlock();
            return new FuncDef(ft,ident,left,null,null,block);
        }
    }
    public MainFuncDef parseMainFuncDef(){
        // MainFuncDef → 'int' 'main' '(' ')' Block // j
        Token int_token=tokens.get(ptr++);
        Token main_token=tokens.get(ptr++);
        Token left=tokens.get(ptr++);
        function_type=1;
        if (tokens.get(ptr).getType()==Type.RPAREN){
            Token right=tokens.get(ptr++);
            Block block=parseBlock();
            return new MainFuncDef(int_token,main_token,left,right,block);
        }else {
            errors.add(new Error('j',tokens.get(ptr-1).getLineno()));
            Block block=parseBlock();
            return new MainFuncDef(int_token,main_token,left,null,block);
        }
    }
    public FuncType parseFuncType(){
        //FuncType → 'void' | 'int' | 'char'
        Token type=tokens.get(ptr++);
        if (type.getType()==Type.INT || type.getType()==Type.CHAR){
            function_type=1;
        }else {
            function_type=0;
        }
        return new FuncType(type);
    }
    public FuncFParams parseFuncFParams(){
        //FuncFParams → FuncFParam { ',' FuncFParam }
        FuncFParam fp=parseFuncFParam();
        FuncFParams fps=new FuncFParams(fp);
        while (tokens.get(ptr).getType()==Type.COMMA){
            ptr++;
            fps.addFP(parseFuncFParam());
        }
        return fps;
    }
    public FuncFParam parseFuncFParam(){
        // FuncFParam → BType Ident ['[' ']'] // k
        Token BType=tokens.get(ptr++);
        Token ident=tokens.get(ptr++);
        if (tokens.get(ptr).getType()==Type.LBRACK){
            Token left=tokens.get(ptr++);
            if (tokens.get(ptr).getType()==Type.RBRACK){
                Token right=tokens.get(ptr++);
                return new FuncFParam(BType,ident,left,right);
            }else {
                errors.add(new Error('k',tokens.get(ptr-1).getLineno()));
                return new FuncFParam(BType,ident,left,null);
            }
        }else {
            return new FuncFParam(BType,ident);
        }
    }
    public Block parseBlock(){
        //Block → '{' { BlockItem } '}'
        Token left=tokens.get(ptr++);
        Block blk=new Block(left);
        if (tokens.get(ptr).getType()==Type.RBRACE){
            Token right=tokens.get(ptr++);
            blk.endAdd(right);
            return blk;
        }else {
            while (tokens.get(ptr).getType()!=Type.RBRACE){
                blk.addBlockItem(parseBlockItem());
            }
            blk.endAdd(tokens.get(ptr++));
            return blk;
        }
    }
    public BlockItem parseBlockItem(){
        // BlockItem → Decl | Stmt
        Token head=tokens.get(ptr);
        if (isDefineHead(head.getType())){
            return new BlockItem(parseDecl());
        }else {
            return new BlockItem(parseStmt());
        }
    }
    public int judgeing=0;
    public boolean judgeLval(){
        judgeing=1;
        int tmp_ptr=ptr;
        parseLVal();
        if (tokens.get(ptr).getType()==Type.ASSIGN){
            ptr=tmp_ptr;
            judgeing=0;
            return true;
        }else {
            ptr=tmp_ptr;
            judgeing=0;
            return false;
        }
    }
    public Stmt parseStmt(){
        /* Stmt → LVal '=' Exp ';' // i
           | [Exp] ';' // i
           | Block
           | 'if' '(' Cond ')' Stmt [ 'else' Stmt ] // j
           | 'for' '(' [ForStmt] ';' [Cond] ';' [ForStmt] ')' Stmt
           | 'break' ';' | 'continue' ';' // i
           | 'return' [Exp] ';' // i
           | LVal '=' 'getint''('')'';' // i j
           | LVal '=' 'getchar''('')'';' // i j
           | 'printf''('StringConst {','Exp}')'';' // i j
         */
        Token head=tokens.get(ptr);
        switch (head.getType()){
            case IDENT:
                if (judgeLval()){
                    LVal lVal=parseLVal();
                    Token assign=tokens.get(ptr++);
                    if (tokens.get(ptr).getType()==Type.GETCHAR || tokens.get(ptr).getType()==Type.GETINT){
                        Token getci=tokens.get(ptr++);
                        Token left_ci=tokens.get(ptr++);
                        if (tokens.get(ptr).getType()==Type.RPAREN){
                            Token right_ci=tokens.get(ptr++);
                            if (tokens.get(ptr).getType()==Type.SEMICN){
                                Token semicn_ci=tokens.get(ptr);
                                ptr++;
                                return new Stmt(lVal,assign,getci,left_ci,right_ci,semicn_ci);
                            }else {
                                errors.add(new Error('i',tokens.get(ptr-1).getLineno()));
                                return new Stmt(lVal,assign,getci,left_ci,right_ci,null);
                            }
                        } else if (tokens.get(ptr).getType()==Type.SEMICN) {
                            ptr++;
                            errors.add(new Error('j',tokens.get(ptr-1).getLineno()));
                            return new Stmt(lVal,assign,getci,left_ci,null,tokens.get(ptr-1));
                        }else {
                            errors.add(new Error('j',tokens.get(ptr-1).getLineno()));
                            errors.add(new Error('i',tokens.get(ptr-1).getLineno()));
                            return new Stmt(lVal,assign,getci,left_ci,null,null);
                        }
                    }else {
                        Exp exp=parseExp();
                        if (tokens.get(ptr).getType()!=Type.SEMICN){
                            errors.add(new Error('i',tokens.get(ptr-1).getLineno()));
                            return new Stmt(lVal,assign,exp,null);
                        }else{
                            return new Stmt(lVal,assign,exp,tokens.get(ptr++));
                        }
                    }

                }else {
                    Exp exp=parseExp();
                    if (tokens.get(ptr).getType()==Type.SEMICN){
                        return new Stmt(tokens.get(ptr++),exp);
                    }else {
                        errors.add(new Error('i',tokens.get(ptr-1).getLineno()));
                        return new Stmt(null,exp);
                    }
                }
            case PRINTF:
                Token print_token=tokens.get(ptr++);
                Token left=tokens.get(ptr++);
                Token string_const=tokens.get(ptr++);
                Stmt stmt_print=new Stmt(print_token,left,string_const);
                Type type1=tokens.get(ptr).getType();
                switch (type1){
                    case COMMA:
                        while (tokens.get(ptr).getType()==Type.COMMA){
                            ptr++;
                            stmt_print.addPrintExp(parseExp());
                        }
                        Type endType=tokens.get(ptr).getType();
                        switch (endType){
                            case RPAREN:
                                Token right=tokens.get(ptr++);
                                if (tokens.get(ptr).getType()==Type.SEMICN){
                                    ptr++;
                                    stmt_print.endEnd_Print(right,tokens.get(ptr-1));
                                    return stmt_print;
                                }else {
                                    errors.add(new Error('i',tokens.get(ptr-1).getLineno()));
                                    stmt_print.endEnd_Print(right,null);
                                    return stmt_print;
                                }
                            case SEMICN:
                                Token semicn=tokens.get(ptr++);
                                errors.add(new Error('j',tokens.get(ptr-1).getLineno()));
                                stmt_print.endEnd_Print(null,semicn);
                                return stmt_print;
                            default:
                                errors.add(new Error('j',tokens.get(ptr-1).getLineno()));
                                errors.add(new Error('i',tokens.get(ptr-1).getLineno()));
                                stmt_print.endEnd_Print(null,null);
                                return stmt_print;
                        }
                    case RPAREN:
                        Token right=tokens.get(ptr++);
                        if (tokens.get(ptr).getType()==Type.SEMICN){
                            ptr++;
                            stmt_print.endEnd_Print(right,tokens.get(ptr-1));
                            return stmt_print;
                        }else {
                            errors.add(new Error('i',tokens.get(ptr-1).getLineno()));
                            stmt_print.endEnd_Print(right,null);
                            return stmt_print;
                        }
                    case SEMICN:
                        Token semicn=tokens.get(ptr++);
                        errors.add(new Error('j',tokens.get(ptr-1).getLineno()));
                        stmt_print.endEnd_Print(null,semicn);
                        return stmt_print;
                    default:
                        errors.add(new Error('j',tokens.get(ptr-1).getLineno()));
                        errors.add(new Error('i',tokens.get(ptr-1).getLineno()));
                        stmt_print.endEnd_Print(null,null);
                        return stmt_print;
                }
            case RETURN:
                ptr++;
                if (tokens.get(ptr).getType()==Type.SEMICN){
                    return new Stmt(head,(Exp) null,tokens.get(ptr++));
                }else if (isExpHead(tokens.get(ptr).getType())){
                    Exp exp=parseExp();
                    if (tokens.get(ptr).getType()==Type.SEMICN){
                        return new Stmt(head,exp,tokens.get(ptr++));
                    }else {
                        errors.add(new Error('i',tokens.get(ptr-1).getLineno()));
                        return new Stmt(head,exp,null);
                    }
                }else {
                    errors.add(new Error('i',tokens.get(ptr-1).getLineno()));
                    return new Stmt(head,(Exp) null,null);
                }
            case CONTINUE:
            case BREAK:
                ptr++;
                if (tokens.get(ptr).getType()==Type.SEMICN){
                    return new Stmt(head,tokens.get(ptr++));
                }else {
                    errors.add(new Error('i',tokens.get(ptr-1).getLineno()));
                    return new Stmt(head,(Token) null);
                }
            case IF:
                ptr++;
                Token token_if=head;
                Token left1=tokens.get(ptr++);
                Cond cond=parseCond();
                Token right=tokens.get(ptr);
                if (right.getType()==Type.RPAREN){
                    ptr++;
                    Stmt stmt=parseStmt();
                    Stmt result=new Stmt(token_if,left1,cond,right,stmt);
                    if (tokens.get(ptr).getType()==Type.ELSE){
                        Token els_token=tokens.get(ptr++);
                        Stmt stmt2=parseStmt();
                        result.add_ELSE(els_token,stmt2);
                        return result;
                    }else {
                        return result;
                    }
                } else{
                    errors.add(new Error('j',tokens.get(ptr-1).getLineno()));
                    Stmt stmt=parseStmt();
                    Stmt result=new Stmt(token_if,left1,cond,null,stmt);
                    if (tokens.get(ptr).getType()==Type.ELSE){
                        Token els_token=tokens.get(ptr++);
                        Stmt stmt2=parseStmt();
                        result.add_ELSE(els_token,stmt2);
                        return result;
                    }else {
                        return result;
                    }
                }
            case FOR:
                Token token_for=tokens.get(ptr++);
                Token left_for=tokens.get(ptr++);
                Token s1,s2;
                ForStmt f1,f2;
                Cond cd;
                if (tokens.get(ptr).getType()==Type.SEMICN){
                    s1=tokens.get(ptr++);
                    f1=null;
                }else {
                    f1=parseForStmt();
                    s1=tokens.get(ptr++);
                }
                if (tokens.get(ptr).getType()==Type.SEMICN){
                    s2=tokens.get(ptr++);
                    cd=null;
                }else {
                    cd=parseCond();
                    s2=tokens.get(ptr++);
                }
                if (tokens.get(ptr).getType()==Type.RPAREN){
                    Token right_for=tokens.get(ptr++);
                    Stmt stmt=parseStmt();
                    return new Stmt(token_for,left_for,f1,s1,cd,s2,null,right_for,stmt);
                } else if (tokens.get(ptr).getType()==Type.IDENT) {
                    f2=parseForStmt();
                    if (tokens.get(ptr).getType()==Type.RPAREN){
                        Token right_for=tokens.get(ptr++);
                        Stmt stmt=parseStmt();
                        return new Stmt(token_for,left_for,f1,s1,cd,s2,f2,right_for,stmt);
                    }else {
                        errors.add(new Error('j',tokens.get(ptr-1).getLineno()));
                        Stmt stmt=parseStmt();
                        return new Stmt(token_for,left_for,f1,s1,cd,s2,f2,null,stmt);
                    }
                }else {
                    errors.add(new Error('j',tokens.get(ptr-1).getLineno()));
                    Stmt stmt=parseStmt();
                    return new Stmt(token_for,left_for,f1,s1,cd,s2,null,null,stmt);
                }
            case LBRACE:
                Block blk=parseBlock();
                return new Stmt(blk);
            default:
                if (head.getType()==Type.SEMICN){
                    ptr++;
                    return new Stmt(head,(Exp) null);
                }else {
                    Exp exp1=parseExp();
                    if (tokens.get(ptr).getType()==Type.SEMICN){
                        return new Stmt(tokens.get(ptr++),exp1);
                    }else {
                        errors.add(new Error('i',tokens.get(ptr-1).getLineno()));
                        return new Stmt(null,exp1);
                    }
                }
        }
    }
    public ForStmt parseForStmt(){
        LVal lval=parseLVal();
        Token assign=tokens.get(ptr++);
        Exp exp=parseExp();
        return new ForStmt(lval,assign,exp);
    }
    public Exp parseExp(){
        return new Exp(parseAddExp());
    }
    public Cond parseCond(){
        return new Cond(parseLOrExp());
    }
    public LVal parseLVal(){
        Token ident=tokens.get(ptr++);
        if (tokens.get(ptr).getType()==Type.LBRACK){
            Token left=tokens.get(ptr++);
            Exp exp=parseExp();
            if (tokens.get(ptr).getType()==Type.RBRACK){
                return new LVal(ident,left,exp,tokens.get(ptr++));
            }else {
                if (judgeing == 0) {
                    errors.add(new Error('k',tokens.get(ptr-1).getLineno()));
                }
                return new LVal(ident,left,exp,null);
            }
        }else {
            return new LVal(ident);
        }
    }
    public PrimaryExp parsePrimaryExp(){
        // PrimaryExp → '(' Exp ')' | LVal | Number | Character// j
        Token head=tokens.get(ptr);
        switch (head.getType()){
            case LPAREN:
                Token left=tokens.get(ptr++);
                Exp exp=parseExp();
                if (tokens.get(ptr).getType()==Type.RPAREN){
                    Token right=tokens.get(ptr++);
                    return new PrimaryExp(left,exp,right);
                }else {
                    errors.add(new Error('j',tokens.get(ptr-1).getLineno()));
                    return new PrimaryExp(left,exp,null);
                }
            case IDENT:
                LVal lVal=parseLVal();
                return new PrimaryExp(lVal);
            case IntConst:
                return new PrimaryExp(parseNumber());
            case CharConst:
                return new PrimaryExp(parseCharacter());
            default:
                //System.out.println("解析 primaryExp 失败");
                return null;
        }
    }
    public Number parseNumber(){
        Token number=tokens.get(ptr++);
        return new Number(number);
    }
    public Character parseCharacter(){
        Token character=tokens.get(ptr++);
        return new Character(character);
    }
    public UnaryExp parseUnaryExp(){
        //UnaryExp → PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp // j
        Token head=tokens.get(ptr);
        switch (head.getType()){
            case LPAREN:
            case IntConst:
            case CharConst:
                return new UnaryExp(parsePrimaryExp());
            case IDENT:
                if (tokens.get(ptr+1).getType()==Type.LPAREN){
                    Token ident=tokens.get(ptr++);
                    Token left=tokens.get(ptr++);
                    if (isExpHead(tokens.get(ptr).getType())){
                        FuncRParams fps=parseFuncRParams();
                        if (tokens.get(ptr).getType()==Type.RPAREN){
                            Token right=tokens.get(ptr++);
                            return new UnaryExp(ident,left,fps,right);
                        }else {
                            errors.add(new Error('j',tokens.get(ptr-1).getLineno()));
                            return new UnaryExp(ident,left,fps,null);
                        }
                    } else if (tokens.get(ptr).getType()==Type.RPAREN) {
                        Token right=tokens.get(ptr++);
                        return new UnaryExp(ident,left,null,right);
                    }else {
                        errors.add(new Error('j',tokens.get(ptr-1).getLineno()));
                        return new UnaryExp(ident,left,null,null);
                    }
                }else {
                    return new UnaryExp(parsePrimaryExp());
                }
            case PLUS:
            case MINU:
            case NOT:
                UnaryOp uo=parseUnaryOp();
                UnaryExp ue=parseUnaryExp();
                return new UnaryExp(uo,ue);
            default:
                //System.out.println("解析 UnaryExp 失败！请检查代码");
                return null;
        }
    }
    public UnaryOp parseUnaryOp(){
        Token tk=tokens.get(ptr++);
        return new UnaryOp(tk);
    }
    public FuncRParams parseFuncRParams(){
        Exp exp0=parseExp();
        FuncRParams fps=new FuncRParams(exp0);
        while (tokens.get(ptr).getType()==Type.COMMA){
            ptr++;
            fps.addExp(parseExp());
        }
        return fps;
    }
    public MulExp parseMulExp(){
        UnaryExp ue=parseUnaryExp();
        ArrayList<UnaryExp> ues=new ArrayList<>();
        ArrayList<Token> syms=new ArrayList<>();
        ues.add(ue);
        while (tokens.get(ptr).getType()==Type.DIV||
                tokens.get(ptr).getType()==Type.MULT||
                tokens.get(ptr).getType()==Type.MOD
                )  {
            syms.add(tokens.get(ptr));
            ptr++;
            ues.add(parseUnaryExp());
        }
        MulExp mul=new MulExp(ue);
        int tmp_ptr=1;
        while (tmp_ptr<ues.size()){
            mul= new MulExp(mul,syms.get(tmp_ptr-1),ues.get(tmp_ptr));
            tmp_ptr++;
        }
        return mul;
    }
    public AddExp parseAddExp(){
        MulExp me=parseMulExp();
        ArrayList<MulExp> mes=new ArrayList<>();
        ArrayList<Token> syms=new ArrayList<>();
        mes.add(me);
        while (tokens.get(ptr).getType()==Type.PLUS||
                tokens.get(ptr).getType()==Type.MINU
        )  {
            syms.add(tokens.get(ptr));
            ptr++;
            mes.add(parseMulExp());
        }
        AddExp add=new AddExp(me);
        int tmp_ptr=1;
        while (tmp_ptr<mes.size()){
            add= new AddExp(add,syms.get(tmp_ptr-1),mes.get(tmp_ptr));
            tmp_ptr++;
        }
        return add;
    }
    public RelExp parseRelExp(){
        AddExp ae=parseAddExp();
        ArrayList<AddExp> aes=new ArrayList<>();
        ArrayList<Token> syms=new ArrayList<>();
        aes.add(ae);
        while (tokens.get(ptr).getType()==Type.LSS||
                tokens.get(ptr).getType()==Type.LEQ||
                tokens.get(ptr).getType()==Type.GRE||
                tokens.get(ptr).getType()==Type.GEQ
        )  {
            syms.add(tokens.get(ptr));
            ptr++;
            aes.add(parseAddExp());
        }
        RelExp rel=new RelExp(ae);
        int tmp_ptr=1;
        while (tmp_ptr<aes.size()){
            rel= new RelExp(rel,syms.get(tmp_ptr-1),aes.get(tmp_ptr));
            tmp_ptr++;
        }
        return rel;
    }
    public EqExp parseEqExp(){
        RelExp re=parseRelExp();
        ArrayList<RelExp> res=new ArrayList<>();
        ArrayList<Token> syms=new ArrayList<>();
        res.add(re);
        while (tokens.get(ptr).getType()==Type.EQL||
                tokens.get(ptr).getType()==Type.NEQ
        )  {
            syms.add(tokens.get(ptr));
            ptr++;
            res.add(parseRelExp());
        }
        EqExp eqe=new EqExp(re);
        int tmp_ptr=1;
        while (tmp_ptr<res.size()){
            eqe= new EqExp(eqe,syms.get(tmp_ptr-1),res.get(tmp_ptr));
            tmp_ptr++;
        }
        return eqe;
    }
    public LAndExp parseLAndExp(){
        EqExp eqe=parseEqExp();
        ArrayList<EqExp> eqs=new ArrayList<>();
        ArrayList<Token> syms=new ArrayList<>();
        eqs.add(eqe);
        while (tokens.get(ptr).getType()==Type.AND ||
                tokens.get(ptr).getType()==Type.SAND)  {
            if (tokens.get(ptr).getType()==Type.SAND){
                errors.add(new Error('a',tokens.get(ptr-1).getLineno()));
            }
            syms.add(tokens.get(ptr));
            ptr++;
            eqs.add(parseEqExp());
        }
        LAndExp lae=new LAndExp(eqe);
        int tmp_ptr=1;
        while (tmp_ptr<eqs.size()){
            lae= new LAndExp(lae,syms.get(tmp_ptr-1),eqs.get(tmp_ptr));
            tmp_ptr++;
        }
        return lae;
    }
    public LOrExp parseLOrExp(){
        LAndExp lAndExp=parseLAndExp();
        ArrayList<LAndExp> lAndExps=new ArrayList<>();
        ArrayList<Token> syms=new ArrayList<>();
        lAndExps.add(lAndExp);
        while (tokens.get(ptr).getType()==Type.OR ||
                tokens.get(ptr).getType()==Type.SOR)  {
            if (tokens.get(ptr).getType()==Type.SOR){
                errors.add(new Error('a',tokens.get(ptr-1).getLineno()));
            }
            syms.add(tokens.get(ptr));
            ptr++;
            lAndExps.add(parseLAndExp());
        }
        LOrExp lOrExp=new LOrExp(lAndExp);
        int tmp_ptr=1;
        while (tmp_ptr<lAndExps.size()){
            lOrExp= new LOrExp(lOrExp,syms.get(tmp_ptr-1),lAndExps.get(tmp_ptr));
            tmp_ptr++;
        }
        return lOrExp;
    }
    public ConstExp parseConstExp(){
        return new ConstExp(parseAddExp());
    }
}
