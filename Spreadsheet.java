package textExcel;
// Update this file with your own code.

import java.util.HashSet;

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
	public String processCommand(String command)
	{
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
	private String processCellAssignation(String command){
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
						break;
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
	private String processFunction(String value, String coordinates, HashSet<String> listOfCellsInFormula){
		SpreadsheetLocation location;
		for (String cell : listOfCellsInFormula){
			location = new SpreadsheetLocation(cell);
			value.replace(cell,cells[location.getRow()][location.getCol()].fullCellText());
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
