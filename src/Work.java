import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Work {
    private Integer terminalSymbolsCount;
    private Entity beginEntity;
    private List<String> dataList;
    private List<Entity> entitiesList;
    private List<String> connectionsList;
    private Set<Character> transitionsList;
    private Set<Entity> outEntitiesSet;
    private Entity absorptiveEntity;

    public Work(String fileName) throws FileNotFoundException {
        this.dataList = this.readFile(fileName);
        this.entitiesList = new ArrayList();
        this.connectionsList = new ArrayList();
        this.transitionsList = new LinkedHashSet<>();
        this.outEntitiesSet = new LinkedHashSet<>();
    }

    /**
     * metoda přečte vstupní soubor po řádcích.
     *
     * @return list - list stringu
     * @param: fileName - soubor, který se má přečíst
     */
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

    /**
     * Metoda ignoruje první 4 řádky, protože jsou informativní, a přidá stavy do
     * entitiesList pravé části do connectionsList.
     */
    public void parseFile() {
        this.terminalSymbolsCount = Integer.valueOf(dataList.get(2));
        // Игнорим первые четыре строки, ибо это информационные строки, а затем делим на до стрелки и после, убирая пробелы
        for (int i = 4; i < dataList.size(); ++i) {
            String[] s = (dataList.get(i)).replaceAll("\\s+", "").split("->");
            // добавление ентит в лист
            this.entitiesList.add(new Entity(s[0]));
            // добавление связей в лист
            this.connectionsList.add(s[1]);
        }
    }

    /**
     * Metoda vytváří nedeterministický konečný automat, jeho podobnost ve formě listu
     */
    public void produceEntities() {
        //
        if (this.entitiesList.size() != this.connectionsList.size()) {
            System.out.println("Something wrong");
            System.exit(-1);
        } else {
            for (int i = 0; i < this.entitiesList.size(); ++i) {
                this.produceEntity(this.entitiesList.get(i), this.connectionsList.get(i));
            }
            beginEntity = this.entitiesList.stream()
                    .filter(streamEnity -> streamEnity.key.equals(dataList.get(3)))
                    .findAny()
                    .orElse(null);
        }
        //řazení podle abecedy
        List<Character> sortedList = transitionsList.stream()
                .sorted().toList();
        transitionsList = new LinkedHashSet<>(sortedList);

        // vytvoření absorpčního stavu a přidání do entitiesList
        int ascii = (entitiesList.get(entitiesList.size() - 1).key.charAt(0)) + 1;
        Entity absorbeEntity = new Entity("" + ((char) ascii));
        ArrayList<Entity> absorbeList = new ArrayList<>();
        absorbeList.add(absorbeEntity);
        transitionsList.forEach(transition -> {
            absorbeEntity.map.put(transition, absorbeList);
        });
        this.absorptiveEntity = absorbeEntity;
        entitiesList.add(absorbeEntity);
    }


    /**
     * Kontrola gramatiky typu G3PR
     *
     * @param entityConnectionsArray - pole jednotlivých přechodů
     * @return true/false
     */
    public boolean checkGrammar(String[] entityConnectionsArray) {
        for (int i = 0; i < entityConnectionsArray.length; i++) {
            if (Character.isLowerCase(entityConnectionsArray[i].charAt(0))) {
                int finalI = i;
                if (this.entitiesList.stream()
                        .noneMatch(streamEnity -> streamEnity.key.equals(entityConnectionsArray[finalI].substring(1)))) {
                    return false;
                }
            } else if (entityConnectionsArray[i].equals("$")) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }


    /**
     * Pomocná metoda pro produceEntities
     *
     * @param entity      - stavy
     * @param connections - pravá strana gramatiky
     */
    public void produceEntity(Entity entity, String connections) {
        // rozdělíme všechny možné přechody
        String[] entityConnectionsArray = connections.split("\\|");

        if (!checkGrammar(entityConnectionsArray)) {
            System.out.print("[ERROR] Wrong grammar!");
            System.exit(-1);
        }

        Arrays.sort(entityConnectionsArray);

        for (String connection : entityConnectionsArray) {
            char connectionChar = connection.charAt(0);
            if (connectionChar != '$') {
                // pokud je mapa prázdná, vytvoří se klíč + list k tomuto klíči
                if (entity.map.get(connectionChar) == null) {
                    //přidáme všechny možné přechodové symboly
                    transitionsList.add(connectionChar);

                    entity.map.put(connectionChar, new ArrayList());
                }
                (entity.map.get(connectionChar)).add(
                        this.entitiesList.stream()
                                .filter(streamEnity -> streamEnity.key.equals(connection.substring(1)))
                                .findAny()
                                .orElse(null));
            } else {
                outEntitiesSet.add(entity);
            }
        }
    }

    /**
     * Metoda zapíše výstupní soubor ve formátu NKAR.
     *
     * @param listOfEntities   - list, který obsahuje nedeterministický konečný automat.
     * @param setOfTransitions - set přechodových symbolů
     * @param outputFileName   - název výstupního souboru
     * @throws IOException
     */
    public void writeToFile(List<Entity> listOfEntities, Set<Character> setOfTransitions, String outputFileName) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName));
        writer.write("G3PR\n" + listOfEntities.size() + "\n" + getTerminalSymbolsCount() + "\n");
        for (Entity entity : listOfEntities) {
            writer.write(entity.key + ": ");
            for (Character transition : setOfTransitions) {
                if (entity.map.get(transition) == null) {
                    writer.write(absorptiveEntity.key + " ");
                } else {
                    entity.map.get(transition).forEach(mapEntity -> {
                        try {
                            writer.write(String.valueOf(mapEntity));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    writer.write(" ");
                }
            }
            writer.write("\n");
        }
        writer.write("1 " + beginEntity + "\n");
        writer.write(outEntitiesSet.size() + " ");
        outEntitiesSet.forEach(out -> {
            try {
                writer.write(out.key + " ");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        writer.close();
    }


    public List<Entity> getEntitiesList() {
        return entitiesList;
    }

    public Set<Character> getTransitionsList() {
        return transitionsList;
    }

    public Integer getTerminalSymbolsCount() {
        return terminalSymbolsCount;
    }
}