package textExcel;

import textExcel.helpers.Utils;

import java.util.HashSet;
import java.util.List;

import static textExcel.helpers.Utils.abbreviateText;
import static textExcel.helpers.Utils.formatText;

public class FormulaCell extends RealCell {
    HashSet<String> listOfCellsInFormula;
    Cell[][] cells;

    public FormulaCell(String value, HashSet<String> listOfCellsInFormula, Cell[][] cells) throws NumberFormatException {
        super(value);
        this.listOfCellsInFormula = listOfCellsInFormula;
        this.cells = cells;
    }


    //Have to calculate every time because if the cell is a2 + a3 and user changes a2, a3 will need to update accordingly
    @Override
    public double getDoubleValue() {
        String updatedValue = value;
        SpreadsheetLocation valueLocation;

        double decimalValue;
        for (String cell : listOfCellsInFormula) {
            valueLocation = new SpreadsheetLocation(cell);
            if (cells[valueLocation.getRow()][valueLocation.getCol()] instanceof RealCell) {
                    updatedValue = updatedValue.replace(cell, String.valueOf(((RealCell) cells[valueLocation.getRow()][valueLocation.getCol()]).getDoubleValue()));
            } else {
                decimalValue = -1;
                return decimalValue;//If something goes wrong, make the cell -1
            }
        }

        List<String> commandAsString = Utils.toList(updatedValue);


        //Evaluates the expression
        if (commandAsString.contains("^")) {
            for (int i = 0; i < commandAsString.size(); i++) {
                if (formatText(commandAsString.get(i)).equals("^")) {
                    Utils.doOperation(commandAsString, '^', i);
                    i--;
                }
            }
        }
        if (commandAsString.contains("*") || commandAsString.contains("/")) {
            for (int i = 0; i < commandAsString.size(); i++) {
                if (formatText(commandAsString.get(i)).equals("*")) {
                    Utils.doOperation(commandAsString, '*', i);
                    i--;
                } else if (formatText(commandAsString.get(i)).equals("/")) {
                    Utils.doOperation(commandAsString, '/', i);
                    i--;
                }
            }
        }
        if (commandAsString.contains("+") || commandAsString.contains("-")) {
            for (int i = 0; i < commandAsString.size(); i++) {
                if (formatText(commandAsString.get(i)).equals("+")) {
                    Utils.doOperation(commandAsString, '+', i);
                    i--;
                } else if (formatText(commandAsString.get(i)).equals("-")) {
                    Utils.doOperation(commandAsString, '-', i);
                    i--;
                }
            }
        }
        decimalValue = Double.parseDouble(commandAsString.get(0));

        return decimalValue;
    }

    @Override
    public String abbreviatedCellText() {
        return (abbreviateText(String.valueOf(getDoubleValue())));
    }

    @Override
    public String fullCellText() {
        return "=" + value;//Add the '=' so that if this is opened in Excel, the formula works
    }
}
