import org.apache.commons.validator.GenericValidator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.*;

public class TaskManager {

    public static void main(String[] args) {

        run();
        // TODO: 16.01.2023 walidacja importance, walidacja numeru w menu, remove!!!

    }

    public static void run() {

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

    public static String validateDescription() {
        boolean flagDescription = true;
        String description = "";
        while (flagDescription) {
            Scanner scanner = new Scanner(System.in);
            description = scanner.nextLine();
            if (description.isEmpty() || description == null || description.length() > 200) {
                System.out.println("Description cannot be longer than 200 signs also cannot be null nor empty. Try again.");
            } else flagDescription = false;
        }
        return description;
    }

    public static String validateDate() {

        boolean flagDate = true;
        String date = "";

        while (flagDate) {
            Scanner scanner = new Scanner(System.in);
            date = scanner.nextLine();
            if (!GenericValidator.isDate(date, "yyyy-MM-dd", true)) {
                System.out.println("Typed date is not valid, try again");
            } else flagDate = false;
        }

        boolean isLeapYear;
        int year = Integer.parseInt(date.substring(0, 4));
        if ((year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0)) {
            isLeapYear = true;
        } else {
            isLeapYear = false;
        }

        int month = Integer.parseInt(date.substring(5, 7));
        boolean flagMonth = true;
        while (flagMonth) {
            if (month < 1 || month > 12) {
                System.out.println("Month value is incorrect, try again");
            } else flagMonth = false;

        }


        int day = Integer.parseInt(date.substring(8));
        boolean flagDay = true;

        while (flagDay) {
            if (day < 1 || (month == 1 && day > 31) || (month == 2 && isLeapYear && day > 29) ||
                    (month == 2 && !isLeapYear && day > 28) || (month == 3 && day > 31) ||
                    (month == 4 && day > 30) || (month == 5 && day > 31) || (month == 6 && day > 30) ||
                    (month == 7 && day > 31) || (month == 8 && day > 31) || (month == 9 && day > 30) ||
                    (month == 10 && day > 31) || (month == 11 && day > 30) || (month == 12 && day > 31)) {
                System.out.println("Day value is incorrect, try again");
            } else flagDay = false;
        }

        return date;
    }

    //nie dziala w ogole
    public static String validateImportance() {

        boolean flag = true;
        String importance = "";
        while (flag) {
            Scanner scanner = new Scanner(System.in);
            importance = scanner.nextLine();
            if ("true".trim().equalsIgnoreCase(importance) || "false".trim().equalsIgnoreCase(importance)) {
                System.out.println("type \"true\" or \"false\"");

            }
            flag = false;
        }

        return importance;
    }


    public static void addTask() {
        StringBuilder sb = new StringBuilder();
        System.out.println("Type tasks description");
        String description = validateDescription();
        System.out.println("Type tasks date in format yyyy-MM-dd");
        String date = validateDate();
        System.out.println("Is this task of great importance? True/False");
        String importance = validateImportance();
        sb.append(description).append(", ").append(date).append(", ").append(importance).append("\n");
        String result = sb.toString();


        try (FileWriter fileWriter = new FileWriter("tasks.txt", true)) {
            fileWriter.append(result);
        } catch (IOException ex) {
            System.out.println("Problem occurred while writing to file");
        }

    }

    public static String[] listTasks() {
        int count = 0;
        ArrayList<String> listOfStrings = new ArrayList<>();
        File file = new File("tasks.txt");

        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                count++;
                String line = count + ", " + scanner.nextLine();
                listOfStrings.add(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("No such file found");
        }

        String[] result = listOfStrings.toArray(new String[0]);

        for (String line : result
        ) {
            System.out.println(line);
        }
        return result;


    }

    public static void removeTask() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("please, type position to be removed");
        //int number = -1;
       // try {
           int number = scanner.nextInt();
//        } catch (IndexOutOfBoundsException e) {
//            System.out.println("Your task-list does not contain such position");
//        } catch (InputMismatchException e) {
//            System.out.println("Invalid input, try again");
//        }

        //Read from the original file and write to the new
        //unless content matches data to be removed.
        File file = new File("tasks.txt");
        File fileCopy = new File("taskCopy.txt");


        StringBuilder sb = new StringBuilder();
        try {
            Scanner scanner1 = new Scanner(file);
            while (scanner1.hasNextLine()) {
                String line = scanner1.nextLine();
                if (!line.startsWith(Integer.toString(number))) {
                    try (PrintWriter printWriter = new PrintWriter("taskCopy.txt")) {
                        printWriter.println(line);
                    } catch (FileNotFoundException ex) {
                        System.out.println("Exception while writing");
                    }
                }
            }

//            if (!file.delete()) {
//                System.out.println("Could not delete file");
//                return;
//            }
//
//            Path from = Paths.get("taskCopy.txt");
//            Path to = Paths.get("tasks.txt");
//
//            Files.move(from, to);

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
