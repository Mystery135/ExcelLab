package textExcel;
// Update this file with your own code.

import com.sun.jdi.event.ExceptionEvent;
import textExcel.helpers.Utils;

import java.io.*;
import java.util.*;
import static textExcel.helpers.Utils.formatText;

public class Spreadsheet implements Grid
{
private Cell[][] cells;
int cellWidth;
int cellHeight;
List<String> history = new ArrayList<>();
int historyDepth = 0;
	public Spreadsheet(int height, int width){
		cellWidth = width;
		cellHeight = height;
		cells = new Cell[cellWidth][cellHeight];
		for (int i = 0; i<cellWidth; i++){
			for (int j = 0; j<cellHeight; j++){
				cells[i][j] = new EmptyCell();
			}
		}
	}


	@Override
	public String processCommand(String command)  {
		if (!command.contains("history") || command.contains("\"")){
		history.add(command);}
		//Try/catch so that if code errors, the program won't stop
		try{
			if (command.equalsIgnoreCase("clear")){
				for (int i = 0; i<cellWidth; i++){
					for (int j = 0; j<cellHeight; j++){
						cells[i][j] = new EmptyCell();
					}
				}
				return getGridText();
				//If the user types clear
			}else if (command.contains("clear") && !command.contains("\"") && !command.contains("history")){
				command = formatText(command);
				String position = formatText(formatText(command.replace("CLEAR","")));
				SpreadsheetLocation location = new SpreadsheetLocation(position);
				cells[location.getRow()][location.getCol()] = new EmptyCell();
				return getGridText();
			}
			else if (command.contains("history") && !command.contains("\"")){

				List<String> args;

				args = List.of(command.split(" "));

				if (formatText(args.get(1)).equalsIgnoreCase("start")){
						try {
							if (Integer.parseInt(formatText(args.get(2))) > 0){
								historyDepth = Integer.parseInt(formatText(args.get(2)));
							}else{
								System.out.println("Please input a nonzero positive integer for the length of the history.");
								historyDepth = Utils.getPositiveNonZeroInt(new Scanner(System.in));
							}
						} catch (Exception e) {
							System.out.println("Please input a nonzero positive integer for the length of the history.");
							historyDepth = Utils.getPositiveNonZeroInt(new Scanner(System.in));
						}
					return "History started of length " + historyDepth + "! Type history display to display history.";
				}else if (formatText(args.get(1)).equalsIgnoreCase("display")){
					StringBuilder toReturn = new StringBuilder();
					int loopAmount;
					if (history.size() < historyDepth){
						loopAmount = 0;
					}else{
						loopAmount = history.size()-historyDepth;
					}
					for (int i = history.size(); i>loopAmount; i--){
						toReturn.append(history.get(i - 1)).append("\n");
					}
					return toReturn.toString();
				}else if (formatText(args.get(1)).equalsIgnoreCase("clear")){
					if (args.size() < 3){
						history.clear();
						return "All history cleared!";
					}
					int clearLength = 0;
					try {
						if (Integer.parseInt(formatText(args.get(2))) > 0){
							clearLength = Integer.parseInt(formatText(args.get(2)));
						}else{
							System.out.println("Please input a nonzero positive integer for how much history to clear.");
							clearLength = Utils.getPositiveNonZeroInt(new Scanner(System.in));
						}
					} catch (Exception e) {
						System.out.println("Please input a nonzero positive integer for how much history to clear.");
						clearLength = Utils.getPositiveNonZeroInt(new Scanner(System.in));
					}
					for (int i = 0; i<=clearLength; i++){
						history.remove(i);
					}

					return "History of length " + clearLength + " cleared! Type history display to display history.";

				}
//				String position = formatText(formatText(command.replace("CLEAR","")));
//				SpreadsheetLocation location = new SpreadsheetLocation(position);
//				cells[location.getRow()][location.getCol()] = new EmptyCell();
				return getGridText();
			}
				else if (formatText(command).equalsIgnoreCase("save")){
					Scanner scanner = new Scanner(System.in);;
					System.out.println("What do you want to name your file? (If you name your file the same as an existing .csv file, you will overwrite it.)");
					System.out.println("You can also specify a path (ex. C:\\Users\\user\\Downloads\\data)");
					String fileName = scanner.nextLine();
					File file = new File(fileName + ".csv");
					if (!file.exists()){
						file.createNewFile();
					}
					PrintWriter printWriter = new PrintWriter(file);
					StringBuilder lineBuilder = new StringBuilder();
					for (int i = 0;  i<getCols(); i++){
						for (int j = 0; j<getRows(); j++){
							lineBuilder.append(cells[j][i].fullCellText()).append(",");
						}
						printWriter.println(lineBuilder);

						lineBuilder = new StringBuilder();
					};
					printWriter.close();
					return "Saved! Path: " + file.getAbsolutePath();
			}
			else if(command.contains("=")){
					return processCellAssignation(command);
			}
			else if (command.toCharArray().length > 1){
				if (Character.isDigit(command.toCharArray()[command.toCharArray().length-1]) && Character.isLetter(command.toCharArray()[0])){
					return processCellInspection(formatText(command));
				}
			}
			history.remove(history.size()-1);
			return Constants.INVALID_COMMAND;
		}catch (Exception e){
			history.remove(history.size()-1);
			return Constants.INVALID_COMMAND;
		}
		//If user only types clear, replace all cells with emptycell

	}
	private String processCellInspection(String command){
		SpreadsheetLocation location = new SpreadsheetLocation(command);
		return getCell(location).fullCellText();
	}
	private String processCellAssignation(String command)  {
		String coordinates;
		String value;
		if((command.contains("(") || command.contains(")")) && !command.contains("\"")){
			command = command.replace("(", "").replace(")", "");
		}
		String[] commandParts = command.split("=");
		if (commandParts.length < 2){
			return null;
		}
		coordinates = commandParts[0];
		value = commandParts[1];
		coordinates = formatText(coordinates);
		if (command.contains("\"")){
			String strValue = value.split("\"")[1];
			SpreadsheetLocation location = new SpreadsheetLocation(coordinates);
			cells[location.getRow()][location.getCol()] = new TextCell(strValue);
		}else if (command.contains("%")){
			value = formatText(value);
			SpreadsheetLocation location = new SpreadsheetLocation(coordinates);
			cells[location.getRow()][location.getCol()] = new PercentCell(value);
		}else if (formatText(value).matches("[+-]?\\d+(\\.\\d+)?")){
			value = formatText(value);
			SpreadsheetLocation location = new SpreadsheetLocation(coordinates);
			cells[location.getRow()][location.getCol()] = new ValueCell(value);
		}
		else{
			boolean isFunction = false;
			HashSet<String> listOfCellsInFormula = new HashSet<>();
			for (int i = cellWidth; i>0; i--){
				for (int j = cellWidth; j>0; j--){
					String s = String.valueOf((char) (i+64)) + j;
					value = formatText(value);
					if (value.contains(s)){
						listOfCellsInFormula.add(s);
						isFunction = true;
					}
					else if (value.contains("+") || value.contains("-") || value.contains("*") || value.contains("/") || value.contains("^")){
						isFunction = true;
					}
				}
			}
			if (!isFunction) {
				history.remove(history.size()-1);
				return Constants.INVALID_COMMAND;
			}else{
				processFunction(value, coordinates, listOfCellsInFormula);
			}
		}
		return getGridText();
	}
	private void processFunction(String value, String coordinates, HashSet<String> listOfCellsInFormula) {
		SpreadsheetLocation location = new SpreadsheetLocation(coordinates);
		if (value.contains(coordinates)){
			if (cells[location.getRow()][location.getCol()] instanceof RealCell){
				value = value.replace(coordinates,String.valueOf(((RealCell) cells[location.getRow()][location.getCol()]).getDoubleValue()));
			}else{
				value = value.replace(coordinates, cells[location.getRow()][location.getCol()].fullCellText()); //TODO: Test with Strings!
			}
			listOfCellsInFormula.remove(coordinates);
		}
		FormulaCell formulaCell = new FormulaCell(value,listOfCellsInFormula, cells);
		cells[location.getRow()][location.getCol()] = formulaCell;

	}


	@Override
	public int getRows()
	{
		return cells.length;
	}

	@Override
	public int getCols()
	{
		return cells[0].length;
	}

	@Override
	public Cell getCell(Location loc)
	{
		return cells[loc.getRow()][loc.getCol()];
	}

	@Override
	public String getGridText()
	{
		StringBuilder toReturn = new StringBuilder();
		toReturn.append("   |");
		for (int i = 0; i<getRows(); i++){
			toReturn.append((char)(i+65));
			for (int j = 0; j<Constants.CELL_SPACE-1; j++){
				toReturn.append(" ");
			}
			toReturn.append("|");
		}
		toReturn.append("\n");

		for (int i = 0; i<getCols(); i++){
			int numSpaces = 3 - String.valueOf((i+1)).length();//TODO: fix for 4 digit numbers
			String repeatedSpaces = " ".repeat(numSpaces);
			toReturn.append(i+1).append(repeatedSpaces).append("|");
			for (int j = 0; j<getRows(); j++){
				toReturn.append(cells[j][i].abbreviatedCellText()  + "|");
			}
			toReturn.append("\n");
		}
		return toReturn.toString();
	}
}
