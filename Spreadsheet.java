package textExcel;
// Update this file with your own code.

import java.awt.*;
import java.io.*;
import java.util.*;
import static textExcel.helpers.Utils.formatText;

public class Spreadsheet implements Grid
{
private Cell[][] cells;
int cellWidth;
int cellHeight;
	public Spreadsheet(){
		cellWidth = 10;
		cellHeight = 10;
		cells = new Cell[cellWidth][cellHeight];
		for (int i = 0; i<cellWidth; i++){
			for (int j = 0; j<cellHeight; j++){
				cells[i][j] = new EmptyCell();
			}
		}
	}


	@Override
	public String processCommand(String command)  {
		if (command.equalsIgnoreCase("clear")){
			for (int i = 0; i<cellWidth; i++){
				for (int j = 0; j<cellHeight; j++){
					cells[i][j] = new EmptyCell();
				}
			}
		return getGridText();
		}else if (command.contains("clear") && !command.contains("\"")){
			command = formatText(command);
			String position = formatText(formatText(command.replace("CLEAR","")));
			SpreadsheetLocation location = new SpreadsheetLocation(position);
			cells[location.getRow()][location.getCol()] = new EmptyCell();
			return getGridText();
		}else if (formatText(command).equalsIgnoreCase("save")){
			try {
				Scanner scanner = new Scanner(System.in);;
				System.out.println("What do you want to name your file? (If you name your file the same as an existing .csv file, you will overwrite it.)");
				System.out.println("You can also specify a path (ex. C:\\Users\\websi\\Downloads\\file)");
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
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		else if(command.contains("=")){
			return processCellAssignation(command);
		}
		else if (Character.isDigit(command.toCharArray()[command.toCharArray().length-1]) && Character.isLetter(command.toCharArray()[0])){
			return processCellInspection(formatText(command));
		}
		return "Invalid command! Try again!";//TODO: make into variable
	}
	private String processCellInspection(String command){
		SpreadsheetLocation location = new SpreadsheetLocation(command);
		for (int i = 0; i<cells.length; i++){
			for (int j = 0; j<cells[0].length; j++){
				System.out.println("Index: [" + i + "," + j + "] - {" + cells[i][j].fullCellText() + "}");
			}
		}
		return getCell(location).fullCellText();
	}
	private String processCellAssignation(String command)  {
		String coordinates;
		String value;
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
			boolean flag = false;
			HashSet<String> listOfCellsInFormula = new HashSet<>();
			for (int i = 0; i<cellWidth; i++){
				for (int j = 0; j<cellHeight; j++){
					String s = String.valueOf((char) (i+65)) + j;
					value = formatText(value);
					if (value.contains(s)){
						listOfCellsInFormula.add(s);
						flag = true;
					}
					if (value.contains("+") || value.contains("-") || value.contains("*") || value.contains("/") || value.contains("^")){
						flag = true;
					}
				}
			}
			if (!flag) {
				return "Invalid command! Try again!";
			}else{
				processFunction(value, coordinates, listOfCellsInFormula);
			}
		}
		return getGridText();
	}
	private String processFunction(String value, String coordinates, HashSet<String> listOfCellsInFormula) {

		System.out.println(value);



		try {

			SpreadsheetLocation location = new SpreadsheetLocation(coordinates);
			cells[location.getRow()][location.getCol()] = new FormulaCell(value,listOfCellsInFormula, cells);
			return "";
		}catch (Exception e){
			System.out.println("ERROR");
			e.printStackTrace();
			return "ERROR";
		}


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
			for (int j = 0; j<TextExcel.CELL_SPACE-1; j++){
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
