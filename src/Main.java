import java.io.*;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Parser p = new Parser("file.txt");
        p.parseFile();
        p.produceEntities();
        p.createNKAR();
        p.writeToFile(p.getProcessedNKARSlist(), p.getTransitionsList(), "out.txt");
        p.writeToFile2(p.getEntitiesList(), p.getTransitionsList(), "out2.txt");
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

}
