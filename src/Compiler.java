import frontend.Lexer;
import frontend.Parser;
import frontend.Token;
import frontend.TreeNode.CompUnit;
import frontend.Type;
import Error.Error;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;

public class Compiler {
    private static ArrayList<Token> tokenList;
    private static ArrayList<Error> errorList;
    public static void main(String[] args){
        errorList=new ArrayList<>();
        tokenList=new ArrayList<>();
        try {
            FileWriter writer0 = new FileWriter("parser.txt", false);
            FileWriter writer1 = new FileWriter("error.txt", false);
        } catch (IOException e) {

        }//清空文件
        task1();// 词法分析
        task2();
        errorList.sort(Comparator.comparingInt((Error o) -> o.getLineno()));
        outputErrorInfo();//输出错误信息
    }
    public static void task1(){
        String sourceFilePath = "testfile.txt";
        String content ;
        try {
            content = new String(Files.readAllBytes(Paths.get(sourceFilePath)));
        } catch (IOException e) {
            return;
        }
        Lexer lexer=new Lexer(content);
        Token t=lexer.gettoken();
        while (t!=null){
            tokenList.add(t);
            if (lexer.isEnd()){
                break;
            }else{
                t=lexer.gettoken();
            }
        }
    }
    public static void task2(){
        Parser parser=new Parser(tokenList);
        if (tokenList.isEmpty()){
            return;
        }
        CompUnit cu=parser.parseCompUnit();
        ArrayList<Error> tmp_errors=parser.getErrors();
        errorList.addAll(tmp_errors);
        if (errorList.isEmpty()){
            cu.walk();
        }
    }
    public static void outputErrorInfo(){
        String destinationFilePath2 = "error.txt";
        if (errorList.isEmpty()){
            return;
        }else {
            try {
                for (Error e: errorList){
                    String sb = e.getLineno() +
                            (" " + e.getType()+"\n");
                    Files.write(Paths.get(destinationFilePath2), sb.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
                }
            }catch (IOException e) {
                System.out.println("Error write failed");
            }
        }
    }
}
