package frontend;


public class Lexer {
    private final String text;

    private int ptr;
    private int lineno;

    private boolean isEnd;
    public Lexer(String s){
        this.text=s;
        ptr=0;
        lineno=1;
        isEnd=false;
    }
    public boolean isEnd(){
        return isEnd;
    }
    public Token gettoken(){
        StringBuilder token=new StringBuilder();
        Type type= Type.IDENT;
        while (true){
            if (ptr==text.length()){
                return null;
            }
            char c=text.charAt(ptr);
            ptr++;
            if (ptr==text.length()){
                isEnd=true;
            }
            if (c==' '||c=='\n'||c=='\r'){
                if (c == '\n') {
                    lineno++;
                }
                if (!token.toString().isEmpty()){
                    return new Token(lineno, type, token.toString());
                }else {
                    continue;
                }
            }
            if (c=='{'){
                token.append(c);
                type=Type.LBRACE;
                return new Token(lineno, type, token.toString());
            }
            if (c=='}'){
                token.append(c);
                type=Type.RBRACE;
                return new Token(lineno, type, token.toString());
            }
            if (c=='['){
                token.append(c);
                type=Type.LBRACK;
                return new Token(lineno, type, token.toString());
            }
            if (c==']'){
                token.append(c);
                type=Type.RBRACK;
                return new Token(lineno, type, token.toString());
            }
            if (c=='('){
                token.append(c);
                type=Type.LPAREN;
                return new Token(lineno, type, token.toString());
            }
            if (c==')'){
                token.append(c);
                type=Type.RPAREN;
                return new Token(lineno, type, token.toString());
            }
            if (c==','){
                token.append(c);
                type=Type.COMMA;
                return new Token(lineno, type, token.toString());
            }
            if (c==';'){
                token.append(c);
                type=Type.SEMICN;
                return new Token(lineno, type, token.toString());
            }
            if (c=='='){
                if (text.charAt(ptr)=='='){
                    token.append(c);
                    token.append(text.charAt(ptr));
                    ptr++;
                    type=Type.EQL;
                    return new Token(lineno, type, token.toString());
                } else {
                    token.append(c);
                    type=Type.ASSIGN;
                    return new Token(lineno, type, token.toString());
                }
            }
            if (c=='>'){
                if (text.charAt(ptr)=='='){
                    token.append(c);
                    token.append(text.charAt(ptr));
                    ptr++;
                    type=Type.GEQ;
                    return new Token(lineno, type, token.toString());
                }else {
                    token.append(c);
                    type=Type.GRE;
                    return new Token(lineno, type, token.toString());
                }
            }
            if (c=='<'){
                if (text.charAt(ptr)=='='){
                    token.append(c);
                    token.append(text.charAt(ptr));
                    ptr++;
                    type=Type.LEQ;
                    return new Token(lineno, type, token.toString());
                }else {
                    token.append(c);
                    type=Type.LSS;
                    return new Token(lineno, type, token.toString());
                }
            }
            if (c=='%'){
                token.append(c);
                type=Type.MOD;
                return new Token(lineno, type, token.toString());
            }
            if (c=='/'){
                if (text.charAt(ptr)=='/'){
                    c=text.charAt(ptr);
                    ptr++;
                    while (c!='\n'){
                        if (ptr==text.length()){
                            return null;
                        }
                        c=text.charAt(ptr);
                        ptr++;
                    }
                    lineno++;
                } else if (text.charAt(ptr)=='*'){
                    ptr++;
                    while (!(text.charAt(ptr)=='*'&&text.charAt(ptr+1)=='/')){
                        c=text.charAt(ptr);
                        ptr++;
                        if (c=='\n'){
                            lineno++;
                        }
                    }
                    ptr=ptr+2;
                    continue;
                }else {
                    token.append(c);
                    type=Type.DIV;
                    return new Token(lineno, type, token.toString());
                }
            }
            if (c=='*'){
                token.append(c);
                type=Type.MULT;
                return new Token(lineno, type, token.toString());
            }
            if (c=='-'){
                token.append(c);
                type=Type.MINU;
                return new Token(lineno, type, token.toString());
            }
            if (c=='+'){
                token.append(c);
                type=Type.PLUS;
                return new Token(lineno, type, token.toString());
            }
            if (c=='!'){
                if (text.charAt(ptr)=='='){
                    token.append(c);
                    token.append(text.charAt(ptr));
                    ptr++;
                    type=Type.NEQ;
                    return new Token(lineno, type, token.toString());
                }else {
                    token.append(c);
                    type=Type.NOT;
                    return new Token(lineno, type, token.toString());
                }

            }
            if (c=='&'){
                if (text.charAt(ptr)=='&'){
                    token.append(c);
                    continue;
                }else if (text.charAt(ptr)!='&' && token.toString().isEmpty()){
                    token.append(c);
                    type=Type.SAND;
                    return new Token(lineno, type, token.toString());
                } else if (token.toString().equals("&")) {
                    token.append(c);
                    type=Type.AND;
                    return new Token(lineno, type, token.toString());
                }
            }
            if (c=='|') {
                if (text.charAt(ptr) == '|') {
                    token.append(c);
                    continue;
                } else if (text.charAt(ptr) != '|' && token.toString().isEmpty()) {
                    token.append(c);
                    type = Type.SOR;
                    return new Token(lineno, type, token.toString());
                } else if (token.toString().equals("|")) {
                    token.append(c);
                    type = Type.OR;
                    return new Token(lineno, type, token.toString());
                }
            }
            if (isLetter(c) || c=='_'){
                token.append(c);
                c=text.charAt(ptr);
                ptr++;
                while (isLetter(c) || isDigit(c) || c=='_'){
                    token.append(c);
                    c=text.charAt(ptr);
                    ptr++;
                    if (ptr==text.length()){
                        if (isLetter(c) || isDigit(c)){
                            token.append(c);
                        }
                        isEnd=true;
                        break;
                    }
                }
                ptr--;
                if (token.toString().equals("main")){
                    type=Type.MAIN;
                    return new Token(lineno, type, token.toString());
                }
                if (token.toString().equals("const")){
                    type=Type.CONST;
                    return new Token(lineno, type, token.toString());
                }
                if (token.toString().equals("int")){
                    type=Type.INT;
                    return new Token(lineno, type, token.toString());
                }
                if (token.toString().equals("char")){
                    type=Type.CHAR;
                    return new Token(lineno, type, token.toString());
                }
                if (token.toString().equals("break")){
                    type=Type.BREAK;
                    return new Token(lineno, type, token.toString());
                }
                if (token.toString().equals("continue")){
                    type=Type.CONTINUE;
                    return new Token(lineno, type, token.toString());
                }
                if (token.toString().equals("if")){
                    type=Type.IF;
                    return new Token(lineno, type, token.toString());
                }
                if (token.toString().equals("else")){
                    type=Type.ELSE;
                    return new Token(lineno, type, token.toString());
                }
                if (token.toString().equals("for")){
                    type=Type.FOR;
                    return new Token(lineno, type, token.toString());
                }
                if (token.toString().equals("getint")){
                    type=Type.GETINT;
                    return new Token(lineno, type, token.toString());
                }
                if (token.toString().equals("getchar")){
                    type=Type.GETCHAR;
                    return new Token(lineno, type, token.toString());
                }
                if (token.toString().equals("printf")){
                    type=Type.PRINTF;
                    return new Token(lineno, type, token.toString());
                }
                if (token.toString().equals("return")){
                    type=Type.RETURN;
                    return new Token(lineno, type, token.toString());
                }
                if (token.toString().equals("void")){
                    type=Type.VOID;
                    return new Token(lineno, type, token.toString());
                }
                type=Type.IDENT;
                return new Token(lineno, type, token.toString());
            }
            if (isDigit(c)){
                token.append(c);
                c=text.charAt(ptr);
                ptr++;
                while (isDigit(c)){
                    token.append(c);
                    c=text.charAt(ptr);
                    ptr++;
                    if (ptr==text.length()){
                        if (isDigit(c)){
                            token.append(c);
                        }
                        isEnd=true;
                        break;
                    }
                }
                ptr--;
                type=Type.IntConst;
                return new Token(lineno, type, token.toString());
            }
            if (c=='"'){
                token.append(c);
                c=text.charAt(ptr);
                ptr++;
                while (c!='"'){
                    token.append(c);
                    c=text.charAt(ptr);
                    ptr++;
                }
                token.append(c);
                type=Type.StringConst;
                return new Token(lineno, type, token.toString());
            }
            if (c=='\'') {
                token.append(c);
                c = text.charAt(ptr);
                ptr++;
                char c2=text.charAt(ptr);
                if (c == '\\') {
                    token.append(c);
                    token.append(c2);
                    ptr++;
                    token.append(text.charAt(ptr));
                    ptr++;
                    type = Type.CharConst;
                    return new Token(lineno, type, token.toString());
                }else if (c2 == '\'') {
                    token.append(c);
                    token.append(c2);
                    ptr++;
                    type = Type.CharConst;
                    return new Token(lineno, type, token.toString());
                }
                 else {
                    type=Type.CharConst;
                    return new Token(lineno, type, token.toString());
                }

            }
        }
    }
    private boolean isDigit(char c){
        return c >= '0' && c <= '9';
    }
    private boolean isLetter(char c){
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }
}
