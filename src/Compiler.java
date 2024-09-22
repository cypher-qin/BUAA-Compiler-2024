import frontend.Lexer;
import frontend.Token;
import frontend.Type;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Compiler {
    private static ArrayList<Token> tokenList;
    public static void main(String[] args){
        tokenList=new ArrayList<>();
        task1();// 词法分析
    }
    public static void task1(){
        String sourceFilePath = "testfile.txt";
        String content ;
        try {
            content = new String(Files.readAllBytes(Paths.get(sourceFilePath)));
        } catch (IOException e) {
            return;
        }
        int flag=0;
        Token error=null;
        Lexer lexer=new Lexer(content);
        Token t=lexer.gettoken();
        while (t!=null){
            tokenList.add(t);
            if (t.getType().equals(Type.SAND) || t.getType().equals(Type.SOR)){
                flag=1;
                error=t;
            }
            System.out.println(t.getValue());
            if (lexer.isEnd()){
                break;
            }else{
                t=lexer.gettoken();
            }
        }
        String destinationFilePath1 = "lexer.txt";
        String destinationFilePath2 = "error.txt";
        if (flag==0){
            try {
                StringBuilder sb=new StringBuilder();
                for (Token tmp : tokenList){
                    sb.append(tmp.getType().getCategoryCode());
                    sb.append(" ");
                    sb.append(tmp.getValue());
                    sb.append("\n");
                }
                Files.write(Paths.get(destinationFilePath1), sb.toString().getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }else {
            try {
                StringBuilder sb=new StringBuilder();
                sb.append(error.getLineno());
                sb.append(" "+"a");
                Files.write(Paths.get(destinationFilePath2), sb.toString().getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

    }
}
