import java.io.*;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Main main = new Main();
        System.out.println(main.parseFile("file.txt"));

        String[] test = new String[3];
        test[0] = "rrrrrrrr";
        test[1] = "aaaaaaaaaa";
        test[2] = "bbbbbbbbbbbbb";

        main.writeToFile(test);
    }

    public String[] parseFile(String path) throws IOException {
        String[] array = new String[50];
        String line;
        int i = 0;

        BufferedReader bf = new BufferedReader(
                new FileReader(path));

        line = bf.readLine();

        while (line != null){
            array[i] = line;
            line = bf.readLine();
            i++;
        }
        bf.close();

        return array;
    }

    public void writeToFile(String[] array) throws IOException {
        String fileName = "output.txt";
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        for (int i = 0; i < array.length; i++){
            writer.write(array[i] + "\n");
        }
        writer.close();
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
