import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Parser {
    private String grammar;
    private Integer nonTerminalSymbolsCount;
    private Integer terminalSymbolsCount;
    private Entity beginEntity;
    private List<String> dataList;
    private List<Entity> entitiesList;
    private List<String> connectionsList;
    private Set<Character> transitionsList;
    private Set<Entity> outEnitiesList;
    private Queue<List<Entity>> queueForNKAR;
    private List<NKAR> createdNKARSList;
    private List<NKAR> processedNKARSlist;

    public Parser(String fileName) throws FileNotFoundException {
        this.dataList = this.readFile(fileName);
        this.entitiesList = new ArrayList();
        this.connectionsList = new ArrayList();
        this.transitionsList = new LinkedHashSet<>();
        this.outEnitiesList = new LinkedHashSet<>();
        this.queueForNKAR = new LinkedList<>();
        this.createdNKARSList = new ArrayList<>();
        this.processedNKARSlist = new ArrayList<>();
    }

    public List<String> readFile(String fileName) throws FileNotFoundException {
        System.out.println("[DEBUG] Start file reading.");
        // Прочтёт файл и запишет его в лист
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

    public void parseFile() {
        this.grammar = dataList.get(0);
        this.nonTerminalSymbolsCount = Integer.valueOf(dataList.get(1));
        this.terminalSymbolsCount = Integer.valueOf(dataList.get(2));
        // Игнорим первые четыре строки, ибо это информационные строки, а затем делим на до стрелки и после, убирая пробелы
        for(int i = 4; i < dataList.size(); ++i) {
            String[] s = (dataList.get(i)).replaceAll("\\s+", "").split("->");
            // добавление ентит в лист
            this.entitiesList.add(new Entity(s[0]));
            // добавление связей в лист
            this.connectionsList.add(s[1]);
        }
    }

    public void produceEntities() {
        // листы будут одинаковыми по сути, но проверка нужна, а то мало ли
        if (this.entitiesList.size() != this.connectionsList.size()) {
            System.out.println("Something wrong");
        } else {
            for(int i = 0; i < this.entitiesList.size(); ++i) {
                // обработка конкретной энтиты
                this.produceEntity(this.entitiesList.get(i), this.connectionsList.get(i));
            }
            // добавление начальной эниты, нужно для дальнейшей обработки
            // я всё делаю через стримы, мог бы объяснить, но почитай лучше про это, выгод как таковых нет, по сути это синтактический сахар
            beginEntity = this.entitiesList.stream()
                .filter(streamEnity -> streamEnity.key.equals(dataList.get(3)))
                .findAny()
                .orElse(null);
        }

    }

    public void produceEntity(Entity entity, String connections) {
        // разделит всевозможные конекшены
        String[] entityConnectionsArray = connections.split("\\|");
        //отсортирует конекшены
        Arrays.sort(entityConnectionsArray);

        for (String connection : entityConnectionsArray) {
            char connectionChar = connection.charAt(0);
            if (connectionChar != '$') {
                // если мапа пустая, то создаться ключ + лист к этому ключи, в который в дальнейшем будет что-то попощеться
                if (entity.map.get(connectionChar) == null) {

                    //добавляем все возможны маленькие буквы, т.е. переходные символы, поможет симулировать последнюю таблицу
                    transitionsList.add(connectionChar);

                    entity.map.put(connectionChar, new ArrayList());
                }
                (entity.map.get(connectionChar)).add(
                        this.entitiesList.stream()
                                .filter(streamEnity -> streamEnity.key.equals(connection.substring(1)))
                                .findAny()
                                .orElse(null));
            } else {
                outEnitiesList.add(entity);
            }
        }

    }

    public List<String> getDataList() {
        return this.dataList;
    }

    public void createNKAR() {
        List<Entity> list = new ArrayList<>();
        list.add(beginEntity);
        queueForNKAR.add(list);
        while (!queueForNKAR.isEmpty()) {
            produceStackEntity(queueForNKAR.poll());
        }

    }

    public void produceStackEntity(List<Entity> stackElement ) {
        NKAR nkar = null;
        if (createdNKARSList.isEmpty() ||
                createdNKARSList.stream()
                        .noneMatch(nkarStreamElement -> nkarStreamElement.value.equals(stackElement))) {
        nkar = new NKAR(stackElement);
        } else if (processedNKARSlist.stream()
                .noneMatch(nkarStreamElement2 -> nkarStreamElement2.value.equals(stackElement))) {
            for (NKAR nkarElem : createdNKARSList) {
                if (nkarElem.value.equals(stackElement)){
                    nkar = nkarElem;
                    break;
                }
            }
        } else {
            return;
        }
        if(createdNKARSList.stream()
                .noneMatch(nkarStreamElement -> nkarStreamElement.value.equals(stackElement))) createdNKARSList.add(nkar);
            NKAR finalNkar = nkar;
            transitionsList.forEach(transition ->
            {
                if (finalNkar.map.get(transition) == null) {
                    Set<Entity> set = new HashSet<>();
                    stackElement.forEach(element -> {
                        if (element.map.get(transition) != null)
                        element.map.get(transition).forEach(mapValue -> set.add(mapValue));
                    });
                    if (!set.isEmpty()) {
                        NKAR temp = new NKAR(set.stream().toList());
                        finalNkar.map.put(transition, temp);
                        if (createdNKARSList.stream()
                                .noneMatch(listElement -> listElement.value.equals(set))) {
                            createdNKARSList.add(temp);
                        }
                        queueForNKAR.add(set.stream().toList());
                    }
                }
            });
            processedNKARSlist.add(nkar);
    }

}
