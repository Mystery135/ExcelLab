package textExcel;
// Update this file with your own code.

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Spreadsheet implements Grid
{
private Cell[][] cells;
int cellWidth;
int cellHeight;
	public Spreadsheet(){
		cellWidth = 9;
		cellHeight = 9;
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
		}else if(command.contains("=")){
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
				System.out.println("Index: [" + i + "," + j + "] - {" + cells[i][j].abbreviatedCellText() + "}");
			}
		}
		return getCell(location).abbreviatedCellText();
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
		SpreadsheetLocation valueLocation;
		for (String cell : listOfCellsInFormula){
			valueLocation = new SpreadsheetLocation(cell);
			value = value.replace(cell,cells[valueLocation.getRow()][valueLocation.getCol()].fullCellText());
		}
		System.out.println(value);





		List<String> commandAsString = new ArrayList<>();

		try {


//			value = formatText(value);



			int j = -1;

			for (int i = 0; i < value.length(); i++) {
				if (Character.isDigit(value.charAt(i))) {
					if (j == -1 || !Character.isDigit(value.charAt(i - 1))) {
						commandAsString.add(String.valueOf(value.charAt(i)));
						j++;
					} else {
						commandAsString.set(j, commandAsString.get(j) + value.charAt(i));
					}
				} else {
					commandAsString.add(String.valueOf(value.charAt(i)));
					j++;
				}
			}
			for (int i = 0; i<commandAsString.size(); i++){
				if (formatText(commandAsString.get(i)).equals("+")){
					int sum = Integer.parseInt(commandAsString.get(i - 1)) + Integer.parseInt(commandAsString.get(i + 1));;
					commandAsString.set(i-1, String.valueOf(sum));
					commandAsString.remove(i);
					commandAsString.remove(i);
					i--;
				}
			}
			value = commandAsString.get(0);



			System.out.println(commandAsString);

			SpreadsheetLocation location = new SpreadsheetLocation(coordinates);
			cells[location.getRow()][location.getCol()] = new ValueCell(value);


//			int i = Integer.parseInt(value);
			return "";



//			System.out.println(Integer.parseInt(value));
//			cells[location.getRow()][location.getCol()] = new ValueCell(String.valueOf(Integer.parseInt(value)));
//			return String.valueOf(Integer.parseInt(value));
		}catch (Exception e){
			System.out.println("ERROR");
			System.out.println(commandAsString);
			e.printStackTrace();
			return "ERROR";
		}


	}
//	private String processCellInspection(String command){
//		return null;
//	}

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
	private String formatText(String s){
		return s.replaceAll("\\s", "").toUpperCase();
	}

}
