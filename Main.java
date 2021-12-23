package search;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    private static Scanner sc = new Scanner(System.in);
    private static final List<List<String>> START_LIST_OF_PEOPLE = new ArrayList<>(100);
    private static final Map<String, Set<Integer>> MAP_INVERTED_INDEX = new HashMap<>();

    public static void main(String[] args) {
        start();
    }


    public static void start() {
        initializationMapInvertedIndex("/Users/asanin/IdeaProjects/Simple Search Engine/Simple Search Engine/task/src/search/names.txt");
        while (true) {
            System.out.println("=== Menu ===\n" +
                    "1. Find a person\n" +
                    "2. Print all people\n" +
                    "0. Exit");
            String s = sc.nextLine();
            if (s.equals("1")) {
                searchAndFoundPeopleByOneInMap();
            } else if (s.equals("2")) {
                printAllPeople();

            } else if (s.equals("0")) {
                System.out.println("Bye!");
                return;
            } else {
                System.out.println("Incorrect option! Try again.");
            }
        }
    }


    private static void printAllPeople() {
        System.out.println("=== List of people ===");
        START_LIST_OF_PEOPLE.stream().map(n -> n.toString().replace(",", "").replace("[", "").replace("]", "").trim()).forEach(System.out::println);
    }


    private static void searchAndFoundPeopleByOneInMap() {
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < 1; i++) {
            System.out.println("Enter a name or email to search all suitable people.");
            String searchWord = scanner.nextLine().toLowerCase();
            if (MAP_INVERTED_INDEX.containsKey(searchWord)) {
                Set<Integer> set = MAP_INVERTED_INDEX.get(searchWord);
                System.out.println(set.size() + " persons found:");
                for (Integer integer : set) {
                    System.out.println(START_LIST_OF_PEOPLE.get(integer).toString().replace("[", "").replace(",", "").replace("]", ""));
                }
            } else {
                System.out.println("No matching people found.");
            }
        }
    }

    private static void initializationMapInvertedIndex(String pathToFile) {
        File file = new File(pathToFile);
        try (Scanner scanner = new Scanner(file)) {
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

}
