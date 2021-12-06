package search;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final List<List<String>> START_LIST_OF_PEOPLE = new ArrayList<>(100);
    private static final Map<String, Set<Integer>> MAP_INVERTED_INDEX = new HashMap<>();
    public static void main(String[] args) {
        startDotExe(args[1]);
    }

    private static void startDotExe(String pathToTxtFile){
        initializationMapInvertedIndex(pathToTxtFile);
        while (true) {
            System.out.println("=== Menu ===\n" +
                    "1. Find a person\n" +
                    "2. Print all people\n" +
                    "0. Exit");
            String s = sc.nextLine();
            switch (s) {
                case "1":
                    choiceStrategy();
                    break;
                case "2":
                    printAllPeople();
                    break;
                case "0":
                    System.out.println("Bye!");
                    sc.close();
                    return;
                default:
                    System.out.println("Incorrect option! Try again.");
                    break;
            }
        }
    }

    private static void printAllPeople() {
        System.out.println("=== List of people ===");
        START_LIST_OF_PEOPLE.stream().map(n -> n.toString().replace(",", "").replace("[", "").replace("]", "").trim()).forEach(System.out::println);
    }

    public static void initializationMapInvertedIndex(String pathToFile) {
        try (Scanner scanner = new Scanner(new File(pathToFile))) {
            while (scanner.hasNext()) {
                START_LIST_OF_PEOPLE.add(List.of(scanner.nextLine().split(" ")));
                for (int i = 0; i < START_LIST_OF_PEOPLE.size(); i++) {
                    for (int j = 0; j < START_LIST_OF_PEOPLE.get(i).size(); j++) {
                        if (MAP_INVERTED_INDEX.containsKey(START_LIST_OF_PEOPLE.get(i).get(j).toLowerCase())) {
                            MAP_INVERTED_INDEX.get(START_LIST_OF_PEOPLE.get(i).get(j).toLowerCase()).add(i);
                            continue;
                        }
                        int finalI = i;
                        MAP_INVERTED_INDEX.put(START_LIST_OF_PEOPLE.get(i).get(j).toLowerCase(), new HashSet<>() {{
                            add(finalI);
                        }});
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No file found: " + pathToFile);
        }
    }

    private static void searchAndFoundPeopleByOneInMapStrategyAny() {
        int countFoundPeople = 0;
        List<String> listResultOfFoundPeople = new ArrayList<>();
        System.out.println("Enter a name or email to search all suitable people.");
        List<String> words = List.of(sc.nextLine().toLowerCase().split(" "));
        for (int i = 0; i < words.size(); i++) {
            String searchWord = words.get(i).toLowerCase();
            if (MAP_INVERTED_INDEX.containsKey(searchWord)) {
                Set<Integer> set = MAP_INVERTED_INDEX.get(searchWord);
                countFoundPeople += set.size();
                for (Integer integer : set) {
                    listResultOfFoundPeople.add(START_LIST_OF_PEOPLE.get(integer).toString());
                }
            }
            if (listResultOfFoundPeople.isEmpty()) {
                System.out.println("No matching people found.");
                return;
            }
        }
        System.out.println(countFoundPeople + " persons found:");
        listResultOfFoundPeople.forEach(n -> System.out.println(n.replace("[", "").replace(",", "").replace("]", "")));
    }

    private static void searchAndFoundPeopleByOneInMapStrategyNone() {
        List<String> listResultOfFoundPeople = new ArrayList<>();
        Set<String> resultSet = new HashSet<>();
        System.out.println("Enter a name or email to search all suitable people.");
        List<String> words = List.of(sc.nextLine().toLowerCase().split(" "));
        for (Map.Entry<String, Set<Integer>> entry : MAP_INVERTED_INDEX.entrySet()) {
            Set<Integer> temporarySet = entry.getValue();
            for (Integer s : temporarySet) {
                resultSet.add(START_LIST_OF_PEOPLE.get(s).toString());
            }
        }
        for (String word : words) {
            if (MAP_INVERTED_INDEX.containsKey(word.toLowerCase())) {
                Set<Integer> set = MAP_INVERTED_INDEX.get(word.toLowerCase());
                for (Integer integer : set) {
                    listResultOfFoundPeople.add(START_LIST_OF_PEOPLE.get(integer).toString());
                }
            }
            if (listResultOfFoundPeople.isEmpty()) {
                System.out.println("No matching people found.");
                return;
            }
        }
        resultSet.removeAll(listResultOfFoundPeople);
        System.out.println(resultSet.size() + " persons found:");
        resultSet.forEach(n -> System.out.println(n.replace("[", "").replace(",", "").replace("]", "")));
    }

    private static void searchAndFoundPeopleByOneInMapStrategyAll() {
        int countFoundPeople = 0;
        Set<String> listResultOfFoundPeople = new HashSet<>();
        System.out.println("Enter a name or email to search all suitable people.");
        List<String> words = List.of(sc.nextLine().toLowerCase().split(" "));
        for (int i = 0; i < words.size(); i++) {
            if (MAP_INVERTED_INDEX.containsKey(words.get(i).toLowerCase())) {
                Set<Integer> set = MAP_INVERTED_INDEX.get(words.get(i).toLowerCase());
                countFoundPeople += set.size();
                for (Integer integer : set) {
                    listResultOfFoundPeople.add(START_LIST_OF_PEOPLE.get(integer).toString());
                }
            }
            if (listResultOfFoundPeople.isEmpty()) {
                System.out.println("No matching people found.");
                return;
            }
        }
        System.out.println(countFoundPeople + " persons found:");
        listResultOfFoundPeople.forEach(n -> System.out.println(n.replace("[", "").replace(",", "").replace("]", "")));
    }

    private static void choiceStrategy() {
        System.out.println("Select a matching strategy: ALL, ANY, NONE");
        String strategy = sc.nextLine();
        switch (strategy) {
            case "ALL":
                searchAndFoundPeopleByOneInMapStrategyAll();
                break;
            case "ANY":
                searchAndFoundPeopleByOneInMapStrategyAny();
                break;
            case "NONE":
                searchAndFoundPeopleByOneInMapStrategyNone();
                break;
            default:
                System.out.println("Wrong input in regards to strategy");
        }
    }
}
