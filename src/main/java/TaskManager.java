import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class TaskManager {

    public static void main(String[] args) {

        boolean quit = false;
        int choice = 0;
        printMenu();

        while (!quit) {
            System.out.println("What would you like to do? (press 4 to print options)");
            Scanner scanner = new Scanner(System.in);
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addTask();
                    break;
                case 2:
                    removeTask();
                    break;
                case 3:
                    listTasks();
                    break;
                case 4:
                    printMenu();
                    break;
                case 5:
                    System.out.println("Bye, bye!");
                    quit = true;
                    break;
            }
        }
    }

    //public static String[][] tasks = new String[count][4];

    public static void addTask() {
        StringBuilder sb = new StringBuilder();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type tasks description");
        String description = scanner.nextLine();
        // tasks[count][0] = description;
        System.out.println("Type tasks date");
        String date = scanner.nextLine();
        //tasks[count][1] = date;
        System.out.println("Is this task of great importance? True/False");
        String importance = scanner.nextLine();
        //tasks[count][2] = importance;
        sb.append(description).append(", ").append(date).append(", ").append(importance).append("\n");
        String result = sb.toString();


        //String[][] newArray = Arrays.copyOf(tasks, count);


        try (FileWriter fileWriter = new FileWriter("tasks.txt", true)) {
            fileWriter.append(result);
        } catch (IOException ex) {
            System.out.println("Problem occurred while writing to file");
        }

    }

    public static String[] listTasks() {
        int count = 0;
        ArrayList<String> listOfStrings = new ArrayList<>();
        String[][] tasks;
        File file = new File("tasks.txt");
        StringBuilder reading = new StringBuilder();
        try {
            Scanner scan = new Scanner(file);
            while (scan.hasNextLine()) {
                count++;
                String line = count + ", " + scan.nextLine();
                listOfStrings.add(line);
                //reading.append(scan.nextLine() + "\n");
            }
        } catch (FileNotFoundException e) {
            System.out.println("No such file found");
        }
        //System.out.println(reading.toString());

        String[] result = listOfStrings.toArray(new String[0]);

        for (String line : result
        ) {
            System.out.println(line);
        }
        return result;


    }

    public static void removeTask() {
        int count = 0;
        Scanner scanner = new Scanner(System.in);
        System.out.println("please, type the number position to be removed");
        int number = scanner.nextInt();
        //Read from the original file and write to the new
        //unless content matches data to be removed.
        File file = new File("tasks.txt");
        File fileCopy = new File("tasksCopy.txt");

        StringBuilder reading = new StringBuilder();
        try {
            Scanner scan = new Scanner(file);
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                if (!line.startsWith(Integer.toString(number))) {
                    try (PrintWriter printWriter = new PrintWriter("taskCopy.txt")) {
                        printWriter.println(line);
                    } catch (FileNotFoundException ex) {
                        System.out.println("Exception while writing");
                    }

                }

            }

            if (!file.delete()) {
                System.out.println("Could not delete file");
                return;
            }

            Path from = Paths.get("taskCopy.txt");
            Path to = Paths.get("tasks.txt");

            Files.move(from, to);

        } catch (FileNotFoundException e) {
            System.out.println("No such file found");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void printMenu() {
        System.out.println("Options to choose : ");
        System.out.println("1 - add new task");
        System.out.println("2 - remove task ");
        System.out.println("3 - list my tasks ");
        System.out.println("4 - print menu ");
        System.out.println("5 - quit ");

    }

}
