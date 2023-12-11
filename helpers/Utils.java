package textExcel.helpers;
import textExcel.Constants;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Utils {
    //Method to check if a number is a double
    public static boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    //Method to check if a number is a double after removing a string
    public static boolean isDouble(String s, String toRemove) {
        try {
            s = s.replace(toRemove, "");
            Double.parseDouble(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    //Abbreviates text to fit within the number of cell spaces chosen by the user. Works with scientific notation and % and adds "..." if a value is cut off.
    public static String abbreviateText(String value) {
        StringBuilder toReturn = new StringBuilder();
        String suffix = "";
        boolean isScientificNotation = value.matches(".*E[-+]?\\d+$");//Checks if value has a character, then  E and a number. If so, it is in scientific notation

        if (value.endsWith("%")) {//If value ends with %, make it the suffix
            suffix = "%";
            value = value.substring(0, value.length() - 1);//Then, remove the E + number from the value
        } else if (isScientificNotation) {//If value is in scientific notation, make E + the number at the end the suffix
            int sciNotationEnding = value.lastIndexOf('E');
            suffix = value.substring(sciNotationEnding);
            value = value.substring(0, sciNotationEnding);//Then, remove the E + number from the value
        }

        int effectiveLength = Constants.CELL_SPACE - suffix.length();

        //append the value if it fits inside the cell space, otherwise truncate it.
        if (value.length() + suffix.length() <= Constants.CELL_SPACE) {
            toReturn.append(value);
        } else if (value.length() > effectiveLength) {
            toReturn.append(value, 0, effectiveLength - 3);
            toReturn.append("...");
        } else {
            toReturn.append(value);
        }
        toReturn.append(suffix);

        //Adds more spaces if needed
        toReturn.append(" ".repeat(Math.max(0, Constants.CELL_SPACE - toReturn.length())));

        return toReturn.toString();
    }

    public static String formatText(String s) {//Removes all spaces and makes all letters uppercase
        return s.replaceAll("\\s", "").toUpperCase();
    }

    public static String getInput(Scanner scanner, List<String> inputOptions, String errorMessage) {//Gets an inputOption from the user
        while (true) {
            if (scanner.hasNextLine()) {
                String input = scanner.nextLine();
                if (inputOptions.contains(formatText(input))) {
                    return input;
                }
            }
            System.out.println(errorMessage);
        }
    }


    public static int getInt(Scanner scanner, int lowerBound) {//Gets an int higher than the lowerbound from the user
        while (true) {
            if (scanner.hasNextInt()) {
                int i = scanner.nextInt();
                if (i > lowerBound) {
                    return i;
                }
            }
            System.out.println("Input an int above " + lowerBound + "!");
            scanner.nextLine();
        }
    }

    public static int getInt(Scanner scanner) {//Gets a positive nonzero int from the user
        while (true) {
            if (scanner.hasNextInt()) {
                int i = scanner.nextInt();
                if (i > 0) {
                    return i;
                } else {
                    System.out.println("Input a valid positive nonzero int!");
                    scanner.nextLine();
                }
            } else {
                System.out.println("Input a valid positive nonzero int!");
                scanner.nextLine();
            }
        }
    }


    public static void doOperation(List<String> commandAsString, char symbol, int i) {//Does a certain operation on a list of strings.
        double newValue;
        switch (symbol) {
            case '^':
                newValue = Math.pow(Double.parseDouble(commandAsString.get(i - 1)), Double.parseDouble(commandAsString.get(i + 1)));
                break;
            case '*':
                newValue = Double.parseDouble(commandAsString.get(i - 1)) * Double.parseDouble(commandAsString.get(i + 1));
                break;
            case '/':
                newValue = Double.parseDouble(commandAsString.get(i - 1)) / Double.parseDouble(commandAsString.get(i + 1));
                break;
            case '+':
                newValue = Double.parseDouble(commandAsString.get(i - 1)) + Double.parseDouble(commandAsString.get(i + 1));
                break;
            case '-':
                newValue = Double.parseDouble(commandAsString.get(i - 1)) - Double.parseDouble(commandAsString.get(i + 1));
                break;
            default:
                return;
        }
        commandAsString.set(i - 1, String.valueOf(newValue));
        commandAsString.remove(i);
        commandAsString.remove(i);

    }


    //Turns a string value into a list of strings in preparation to perform operations (addition, subtraction, etc) on it.
    public static List<String> toList(String updatedValue) {
        List<String> commandAsString = new ArrayList<>();

        int j = -1;
        boolean isInScientificNotation = false;

        for (int i = 0; i < updatedValue.length(); i++) {
            char currentChar = updatedValue.charAt(i);

            if (Character.isDigit(currentChar) || currentChar == '.' ||
                    (isInScientificNotation && (currentChar == '-' || currentChar == '+')) ||
                    (!isInScientificNotation && (currentChar == 'E' || currentChar == 'e'))) {

                if (currentChar == 'E' || currentChar == 'e') {
                    isInScientificNotation = true;
                }

                if (j == -1 || (!Character.isDigit(updatedValue.charAt(i - 1)) && updatedValue.charAt(i - 1) != '.' && !isInScientificNotation)) {
                    commandAsString.add(String.valueOf(currentChar));
                    j++;
                } else {
                    commandAsString.set(j, commandAsString.get(j) + currentChar);
                }
            } else {
                if (isInScientificNotation) {
                    isInScientificNotation = false;
                }
                commandAsString.add(String.valueOf(currentChar));
                j++;
            }
        }

        return commandAsString;
    }
}


