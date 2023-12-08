package textExcel;
// Update this file with your own code.

import textExcel.helpers.Utils;

import java.io.*;
import java.lang.reflect.Array;
import java.security.InvalidParameterException;
import java.util.*;
import static textExcel.helpers.Utils.formatText;

public class Spreadsheet implements Grid
{
private Cell[][] cells;
int cellWidth;
int cellHeight;
History history;
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

boolean addedHistory = false;
	@Override
	public String processCommand(String command)  {
		if (!command.contains("history") || command.contains("\"")){
			if (history != null){history.add(command); addedHistory = true;}
		}
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
								history = new History(historyDepth);
							}else{
								System.out.println("Please input a nonzero positive integer for the length of the history.");
								historyDepth = Utils.getInt(new Scanner(System.in));
								history = new History(historyDepth);
							}
						} catch (Exception e) {
							System.out.println("Please input a nonzero positive integer for the length of the history.");
							historyDepth = Utils.getInt(new Scanner(System.in));
							history = new History(historyDepth);
						}
					return "History started of length " + historyDepth + "! Type history display to display history.";
				}else if (formatText(args.get(1)).equalsIgnoreCase("display")){
					if (history == null){
						return "No history started yet! Type \"history start n\" to start history.";
					}
					return history.toString();
				}else if (formatText(args.get(1)).equalsIgnoreCase("clear")){
					if (history == null){
						return "No history started yet! Type \"history start n\" to start history.";
					}
					if (args.size() < 3){
						history.clear();
						return "All history cleared!";
					}
					int clearLength;
					try {
						if (Integer.parseInt(formatText(args.get(2))) < 0){throw new InvalidParameterException("Clear length cannot be negative");}
						if (Integer.parseInt(formatText(args.get(2))) <= history.size()){
							clearLength = Integer.parseInt(formatText(args.get(2)));
						}else{
							clearLength = history.size();
						}
					} catch (Exception e) {
						System.out.print("Please input a nonzero positive integer for how much history to clear: ");
						clearLength = Utils.getInt(new Scanner(System.in));
						if (clearLength <= history.size()){
							clearLength = Integer.parseInt(formatText(args.get(2)));
						}else{
							clearLength = history.size();
						}
					}
					for (int i = 0; i<clearLength; i++){
						history.remove(0);
					}
					return clearLength + " history entr" + (clearLength == 1 ? "y" : "ies") + " cleared! Type history display to display history.";
				}else if (formatText(args.get(1)).equalsIgnoreCase("stop")){
					history = null;
					return "History stopped.";
				}
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
			}else if (command.contains("import")){
				Scanner scanner = new Scanner(System.in);
				System.out.println("Do you want to save this current file first? (Y/N)");
				String save = Utils.getInput(scanner, List.of("Y", "N"), "Type \"Y\" for yes and \"N\" for no!");
				if (save.equalsIgnoreCase("Y")) {
					System.out.println(processCommand("save"));
					System.out.println();
				}
				System.out.println("What is the name of the file you want to import? (ex. data.csv.)");
				System.out.println("If the file is not in this project's folder, you may need to specify the file path (ex. C:\\Users\\user\\Downloads\\data)");
				String filePath = scanner.nextLine();

				try {
					File file = new File(filePath);
					if (!file.exists()){
						return "No file with that name exists!";
					}
					Scanner fileReader = new Scanner(file);
					StringBuilder data = new StringBuilder();
					while (fileReader.hasNextLine()){
						data.append(fileReader.nextLine());
					}

					ArrayList<String> lines = new ArrayList<>(Arrays.asList(data.toString().split("\n")));
					for (int i = 0; i<lines.size(); i++){//Disregard commas between Quotes!
						for (int j = 0; j<lines.get(i).split(",").length; j++){
							List<String> cells = List.of(lines.get(i).split(","));
							if (cells.get(j).contains("%")){
								String s = cells.get(j).replace("%", "");
								try {
									double d = Double.parseDouble(s);
									cells[i][j] = new PercentCell(cells.get(j));
									continue;//CONTINUE IF ASSIGNED!
								}catch (NumberFormatException e){

								}
							}else if (cells.get(j).contains("%")){
								String s = cells.get(j).replace("%", "");
								try {
									double d = Double.parseDouble(s);
									this.cells[i][j] = new PercentCell(cells.get(j));
								}catch (NumberFormatException e){

								}
							}
							try {
								double d = Double.parseDouble(cells.get(j));
							}catch (NumberFormatException e){

							}
						}
					}




					System.out.println(lines);


				}catch (Exception e){
					return "Invalid file.";
				}




			}
			else if(command.contains("=")){
					return processCellAssignation(command);
			}
			else if (command.toCharArray().length > 1){
				if (Character.isDigit(command.toCharArray()[command.toCharArray().length-1]) && Character.isLetter(command.toCharArray()[0])){
					return processCellInspection(formatText(command));
				}
			}
			if (history != null){history.remove(history.size()-1);}
			return Constants.INVALID_COMMAND;
		}catch (Exception e){
			if (addedHistory){
				history.remove(history.size()-1);
				addedHistory = false;
			}
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
				if (history != null){history.remove(history.size()-1);}
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
