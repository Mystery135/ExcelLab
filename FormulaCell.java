package textExcel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static textExcel.helpers.Utils.abbreviateText;
import static textExcel.helpers.Utils.formatText;

public class FormulaCell extends RealCell{
    DecimalFormat decimalFormat = new DecimalFormat("###.#");
    HashSet<String> listOfCellsInFormula;
    Cell[][] cells;
    public FormulaCell(String value, HashSet<String> listOfCellsInFormula, Cell[][] cells) {
        super(value);
        this.listOfCellsInFormula = listOfCellsInFormula;
        this.cells = cells;
    }

    @Override
    public double getDoubleValue() {
        String updatedValue = value;
        SpreadsheetLocation valueLocation;
        for (String cell : listOfCellsInFormula){
            valueLocation = new SpreadsheetLocation(cell);
            updatedValue = updatedValue.replace(cell, String.valueOf(((RealCell)cells[valueLocation.getRow()][valueLocation.getCol()]).getDoubleValue()));
        }
        List<String> commandAsString = new ArrayList<>();

        int j = -1;

        for (int i = 0; i < updatedValue.length(); i++) {
            if (Character.isDigit(updatedValue.charAt(i)) || updatedValue.charAt(i) == '.') {
                if (j == -1 || (!Character.isDigit(updatedValue.charAt(i - 1)) && updatedValue.charAt(i-1) != '.')) {
                    commandAsString.add(String.valueOf(updatedValue.charAt(i)));
                    j++;
                } else {
                    commandAsString.set(j, commandAsString.get(j) + updatedValue.charAt(i));
                }
            } else {
                commandAsString.add(String.valueOf(updatedValue.charAt(i)));
                j++;
            }
        }

        double newValue;
        System.out.println(commandAsString);


        if (commandAsString.contains("^")) {
            for (int i = 0; i < commandAsString.size(); i++) {
                if (formatText(commandAsString.get(i)).equals("^")) {
                    newValue = Math.pow(Double.parseDouble(commandAsString.get(i - 1)), Double.parseDouble(commandAsString.get(i + 1)));
                    commandAsString.set(i - 1, String.valueOf(newValue));
                    commandAsString.remove(i);
                    commandAsString.remove(i);
                    i--;
                }
            }
        }

        if (commandAsString.contains("*") || commandAsString.contains("/")) {
            for (int i = 0; i < commandAsString.size(); i++) {
                if (formatText(commandAsString.get(i)).equals("*")) {
                    newValue = Double.parseDouble(commandAsString.get(i - 1)) * Double.parseDouble(commandAsString.get(i + 1));
                    commandAsString.set(i - 1, String.valueOf(newValue));
                    commandAsString.remove(i);
                    commandAsString.remove(i);
                    i--;
                } else if (formatText(commandAsString.get(i)).equals("/")) {
                    newValue = Double.parseDouble(commandAsString.get(i - 1)) / Double.parseDouble(commandAsString.get(i + 1));;
                    commandAsString.set(i - 1, String.valueOf(newValue));
                    commandAsString.remove(i);
                    commandAsString.remove(i);
                    i--;
                }
            }
        }
        if (commandAsString.contains("+") || commandAsString.contains("-")) {
            for (int i = 0; i < commandAsString.size(); i++) {
                if (formatText(commandAsString.get(i)).equals("+")) {
                    newValue = Double.parseDouble(commandAsString.get(i - 1)) + Double.parseDouble(commandAsString.get(i + 1));;
                    commandAsString.set(i - 1, String.valueOf(newValue));
                    commandAsString.remove(i);
                    commandAsString.remove(i);
                    i--;
                } else if (formatText(commandAsString.get(i)).equals("-")) {
                    newValue = Double.parseDouble(commandAsString.get(i - 1)) - Double.parseDouble(commandAsString.get(i + 1));;
                    commandAsString.set(i - 1, String.valueOf(newValue));
                    commandAsString.remove(i);
                    commandAsString.remove(i);
                    i--;
                }
            }
        }
        return Double.parseDouble(commandAsString.get(0));
    }

    @Override
    public String abbreviatedCellText() {
        return (abbreviateText(String.valueOf(decimalFormat.format(getDoubleValue()))));
    }

    @Override
    public String fullCellText() {
        return value;
    }
}
