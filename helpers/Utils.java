package textExcel.helpers;

import textExcel.Constants;
import textExcel.TextExcel;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Utils {
    public static String abbreviateText(String value) {
        StringBuilder toReturn = new StringBuilder();
        String suffix = "";
        if (value.endsWith("%")) {
            suffix = "%";
            value = value.substring(0, value.length() - 1);
        } else if (value.matches(".*E[-+]?\\d+$")) {
            int sciNotationEnding = value.lastIndexOf('E');
            suffix = value.substring(sciNotationEnding);
            value = value.substring(0, sciNotationEnding);
        }

        if (value.length() < Constants.CELL_SPACE) {
            toReturn.append(value);
            for (int i = 0; i < Constants.CELL_SPACE - value.length() - suffix.length(); i++) {
                toReturn.append(" ");
            }
        } else if (value.length() > Constants.CELL_SPACE - suffix.length()) {
            toReturn.append(value, 0, Constants.CELL_SPACE - suffix.length());
        } else {
            toReturn.append(value);
        }

        toReturn.append(suffix); // Add the suffix at the end

        return toReturn.toString();
    }
    public static String formatText(String s){
        return s.replaceAll("\\s", "").toUpperCase();
    }


    public static int getInt(Scanner scanner){
        while (true){
            if (scanner.hasNextInt()){
                return scanner.nextInt();
            }else{
                System.out.println("Input a valid int!");
                scanner.nextLine();
            }
        }
    }



    public static void doOperation(List<String> commandAsString, char symbol, int i){
        double newValue;
        switch (symbol){
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

    public static List<String> toList(String updatedValue) {
        List<String> commandAsString = new ArrayList<>();

        int j = -1;
        boolean isInScientificNotation = false; // Flag to indicate we are currently reading a scientific notation number

        for (int i = 0; i < updatedValue.length(); i++) {
            char currentChar = updatedValue.charAt(i);

            // Check if the current character is a digit, a decimal point, or a part of the scientific notation
            if (Character.isDigit(currentChar) || currentChar == '.' ||
                    (isInScientificNotation && (currentChar == '-' || currentChar == '+')) ||
                    (!isInScientificNotation && (currentChar == 'E' || currentChar == 'e'))) {

                if (currentChar == 'E' || currentChar == 'e') {
                    isInScientificNotation = true; // We're starting to read a scientific notation number
                }

                if (j == -1 ||
                        (!Character.isDigit(updatedValue.charAt(i - 1)) &&
                                updatedValue.charAt(i - 1) != '.' &&
                                !isInScientificNotation)) {
                    commandAsString.add(String.valueOf(currentChar));
                    j++;
                } else {
                    commandAsString.set(j, commandAsString.get(j) + currentChar);
                }
            } else {
                if (isInScientificNotation) {
                    isInScientificNotation = false; // We've finished reading a scientific notation number
                }
                commandAsString.add(String.valueOf(currentChar));
                j++;
            }
        }

        return commandAsString;
    }
}


