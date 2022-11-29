import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {
        Work w = new Work("file.txt");
        try {
            w.parseFile();
            w.produceEntities();
            w.writeToFile(w.getEntitiesList(), w.getTransitionsList(), "out.txt");
        }
        catch (Exception e){

        }
    }



}