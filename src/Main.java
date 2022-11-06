import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
    Main main = new Main();
    System.out.println(main.isG3RP());

    }

    public char[] parseFile(String path) throws FileNotFoundException {
        String theString = "";

        File file = new File(path);
        Scanner scanner = new Scanner(file);

        theString = scanner.nextLine();
        while (scanner.hasNextLine()) {
            theString = theString + "\n" + scanner.nextLine();
        }

        char[] charArray = theString.toCharArray();
        return charArray;
    }

    public boolean isG3RP() throws FileNotFoundException {
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
    }



}
