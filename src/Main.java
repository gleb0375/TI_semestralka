import java.io.*;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Parser p = new Parser("file.txt");
        p.parseFile(p.getDataList());
        p.produceEntities();
    }

    /*public boolean isG3RP() throws FileNotFoundException {
    char[] ar = parseFile("file.txt");
    //System.out.println(ar);
    for (int i = 0; i< ar.length - 2; i++){
        if (ar[i]=='>' || ar[i] == '|') {
            if (Character.isLowerCase(ar[i+1]) && Character.isUpperCase(ar[i+2]))
                continue;
            else
                return false;
        }
    }
    return true;
    }*/

    /*public char[] createTable() throws FileNotFoundException {
        char[] chars = parseFile("file.txt");
        char[] table;
        System.out.println("    *   |   a   |   b   |");
        for (int i = 0; i < chars.length - 1; i++){
           if (chars[i+1] == '-') {
               System.out.print("     " + chars[i]);}

           if (chars[i] == 'a') {
               System.out.print("      " + chars[i + 1]);

           }
           if (chars[i]== 'b')
               System.out.println("       " +chars[i+1]);
        }

        return chars;
    }*/



}
