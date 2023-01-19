package pl.coderslab;

import org.apache.commons.validator.GenericValidator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class TaskManager {

    public static void main(String[] args) {

        run();

    }

    public static void run() {

        boolean quit = false;
        int choice;
        printMenu();

        while (!quit) {
            System.out.println("What would you like to do? (press 4 to print options)");
            choice = validateChoice();

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

    public static int validateChoice() {
        boolean flag = true;
        int choice = -1;
        while (flag) {
            try {
                Scanner scanner = new Scanner(System.in);
                choice = scanner.nextInt();
                if (choice < 1 || choice > 5) {
                    System.out.println("Please, pick the value from 1 to 5");
                } else flag = false;
            } catch (InputMismatchException e) {
                System.out.println("Input must be numeric value, try picking again");
            }
        }
        return choice;
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
        String result = "";
        String isFalse = "false";
        String isTrue = "true";
        while (flag) {
            Scanner scanner = new Scanner(System.in);
            importance = scanner.nextLine();
            if (importance.trim().equalsIgnoreCase(isTrue) || importance.trim().equalsIgnoreCase(isFalse)) {
                result = importance;
                flag = false;
            } else {
                flag = true;
                System.out.println("type \"true\" or \"false\"");
            }
        }

        return result;
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

        int number = -1;
        boolean flag = true;

        while (flag) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Please, type the position to be removed");
            try {
                number = scanner.nextInt();
                flag = false;
            } catch (InputMismatchException e) {
                System.out.println("Expected value is a number");
            }
        }

        //Read from the original file and write to the new
        //unless content matches data to be removed.
        File file = new File("tasks.txt");
        StringBuilder sb = new StringBuilder();

        int count = 0;
        try {
            Scanner scanner1 = new Scanner(file);

            while ((scanner1.hasNextLine())) {
                count++;
                String line = scanner1.nextLine();
                if (number != count && line != null) {
                    try (PrintWriter printWriter = new PrintWriter("taskCopy.txt")) {
                        String line1 = sb.append(line + "\n").toString().trim();
                        printWriter.println(line1);
                        if (number < 1 || number > count) {
                            System.out.println("Your list does not contain position " + number);
                            //for some reason prints itself twice?? To manage.
                        }
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

        } catch (
                FileNotFoundException e) {
            System.out.println("No such file found");
        } catch (
                IOException e) {
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
