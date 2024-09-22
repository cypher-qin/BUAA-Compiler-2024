package frontend;

public enum Type {
    // 标识符
    IDENT("IDENFR"),
    // 整型常量
    IntConst("INTCON"),
    // 字符串常量
    StringConst("STRCON"),
    // 字符常量
    CharConst("CHRCON"),
    // main关键字
    MAIN("MAINTK"),
    // const关键字
    CONST("CONSTTK"),
    // int关键字
    INT("INTTK"),
    // char关键字
    CHAR("CHARTK"),
    // break关键字
    BREAK("BREAKTK"),
    // continue关键字
    CONTINUE("CONTINUETK"),
    // if关键字
    IF("IFTK"),
    // else关键字
    ELSE("ELSETK"),
    // void关键字
    VOID("VOIDTK"),
    // 逻辑非
    NOT("NOT"),
    // 乘法
    MULT("MULT"),
    // 逗号
    COMMA("COMMA"),
    // 逻辑与
    AND("AND"),
    SAND("SINGLEAND"),

    // 逻辑或
    OR("OR"),
    SOR("SOR"),
    // 除法
    DIV("DIV"),
    // 求余
    MOD("MOD"),
    // 左括号
    LPAREN("LPARENT"),
    // 右括号
    RPAREN("RPARENT"),
    // for关键字
    FOR("FORTK"),
    // 小于
    LSS("LSS"),
    // 小于等于
    LEQ("LEQ"),
    // 左方括号
    LBRACK("LBRACK"),
    // 右方括号
    RBRACK("RBRACK"),
    // getint函数
    GETINT("GETINTTK"),
    // getchar函数
    GETCHAR("GETCHARTK"),
    // printf函数
    PRINTF("PRINTFTK"),
    // 大于
    GRE("GRE"),
    // 大于等于
    GEQ("GEQ"),
    // 左大括号
    LBRACE("LBRACE"),
    // 右大括号
    RBRACE("RBRACE"),
    // return关键字
    RETURN("RETURNTK"),
    // 赋值
    ASSIGN("ASSIGN"),
    // 加法
    PLUS("PLUS"),
    // 减法
    MINU("MINU"),
    // 等于
    EQL("EQL"),
    // 不等于
    NEQ("NEQ"),
    // 分号
    SEMICN("SEMICN");

    private final String categoryCode;

    // 枚举构造函数
    Type(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    // Getter方法获取类别码
    public String getCategoryCode() {
        return categoryCode;
    }
}
