package textExcel;

import textExcel.helpers.Utils;

import java.util.HashSet;
import java.util.List;

import static textExcel.helpers.Utils.abbreviateText;
import static textExcel.helpers.Utils.formatText;

public class FormulaCell extends RealCell {
    HashSet<String> listOfCellsInFormula;
    Cell[][] cells;
    private double decimalValue;

    public FormulaCell(String value, HashSet<String> listOfCellsInFormula, Cell[][] cells) throws NumberFormatException {
        super(value);

        this.listOfCellsInFormula = listOfCellsInFormula;
        this.cells = cells;



    }

    @Override
    public double getDoubleValue() {
        //Have to calculate every time because if the cell is a2 + a3 and user changes a2, this cell will need to update accordingly.
        String updatedValue = value;
        SpreadsheetLocation valueLocation;
        for (String cell : listOfCellsInFormula) {
            valueLocation = new SpreadsheetLocation(cell);
            if (cells[valueLocation.getRow()][valueLocation.getCol()] instanceof RealCell) {
                if (cells[valueLocation.getRow()][valueLocation.getCol()] == this){
                    updatedValue = updatedValue.replace(cell, String.valueOf(((RealCell) cells[valueLocation.getRow()][valueLocation.getCol()]).getDoubleValue()));
                }else{
                    updatedValue = updatedValue.replace(cell, String.valueOf(((RealCell) cells[valueLocation.getRow()][valueLocation.getCol()]).getDoubleValue()));
                }
            } else {
                decimalValue = -1;
            }
        }

        List<String> commandAsString = Utils.toList(updatedValue);


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
        return "=" + value;
    }
}
