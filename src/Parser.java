import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser {
    private List<String> dataList;
    private List<Entity> entitiesList;
    private List<String> connectionsList;

    public Parser(String fileName) throws FileNotFoundException {
        this.dataList = this.readFile(fileName);
        this.entitiesList = new ArrayList();
        this.connectionsList = new ArrayList();
    }

    public List<String> readFile(String fileName) throws FileNotFoundException {
        System.out.println("[DEBUG] Start file reading.");

        List<String> list;
            FileInputStream fis = new FileInputStream(fileName);
                InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
                    BufferedReader reader = new BufferedReader(isr);
                        list = reader.lines().toList();

        System.out.println("[DEBUG] End file reading.");
        return list;
    }

    public void writeToFile(List<String> listOfStrings, String outputFileName) throws IOException {
        System.out.println("[DEBUG] Start file writing.");
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName));
        listOfStrings.forEach((element) -> {
            try {
                writer.write(element + "\n");
            } catch (IOException var3) {
                var3.printStackTrace();
            }

        });
        writer.close();
        System.out.println("[DEBUG] End file writing.");
    }

    public void parseFile(List<String> listOfElements) {
        for(int i = 4; i < listOfElements.size(); ++i) {
            String[] s = (listOfElements.get(i)).replaceAll("\\s+", "").split("->");
            this.entitiesList.add(new Entity(s[0]));
            this.connectionsList.add(s[1]);
        }

    }

    public void produceEntities() {
        if (this.entitiesList.size() != this.connectionsList.size()) {
            System.out.println("Something wrong");
        } else {
            for(int i = 0; i < this.entitiesList.size(); ++i) {
                this.produceEntity(this.entitiesList.get(i), this.connectionsList.get(i));
            }

        }
    }

    public void produceEntity(Entity entity, String connections) {
        String[] entityConnectionsArray = connections.split("\\|");
        Arrays.sort(entityConnectionsArray);

        for(int i = 0; i < entityConnectionsArray.length; ++i) {
            String connection = entityConnectionsArray[i];
            char connectionChar = connection.charAt(0);
            if (connectionChar != '$') {
                if (entity.map.get(connectionChar) == null) {
                    entity.map.put(connectionChar, new ArrayList());
                }
                (entity.map.get(connectionChar)).add(
                        this.entitiesList.stream()
                                .filter(streamEnity -> streamEnity.key.equals(connection.substring(1)))
                                .findAny()
                                .orElse(null));
            }
        }

    }

    public List<String> getDataList() {
        return this.dataList;
    }
}
