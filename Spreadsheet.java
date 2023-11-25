package textExcel;
// Update this file with your own code.

import javax.lang.model.util.AbstractElementVisitor6;
import java.text.Format;
import java.util.ArrayList;
import java.util.Arrays;

public class Spreadsheet implements Grid
{
private Cell[][] cells;
int cellWidth;
int cellHeight;
	public Spreadsheet(){
		cellWidth = 4;
		cellHeight = 4;
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
		if (command.length() == 2 && Character.isDigit(command.toCharArray()[1]) && Character.isLetter(command.toCharArray()[0])){
			return processCellInspection(command.replaceAll("\\s", "").toUpperCase());
		}else if(command.contains("=")){
			return processCellAssignation(command);
		}
		return null;
	}
	private String processCellInspection(String command){
		SpreadsheetLocation location = new SpreadsheetLocation(command);
		int xIndex = location.getRow();
		int yIndex = location.getCol();
		for (int i = 0; i<cells.length; i++){
			for (int j = 0; j<cells.length; j++){
				System.out.println("Index: [" + i + "," + j + "] - {" + cells[i][j].fullCellText() + "}");
			}
		}
		return cells[xIndex][yIndex].abbreviatedCellText();
	}
	private String processCellAssignation(String command){
		if (command.contains("\"")){
			StringBuilder assignedValue = new StringBuilder();
			boolean between = false;
			for (int i = 0; i<command.length(); i++){
				if(!command.substring(i, i+1).equalsIgnoreCase("\"") && !between){
					continue;
				}
				if(!between){
					between = true;
				}else{
					break;
				}
				if (!command.substring(i, i+1).equalsIgnoreCase("\"")){
					assignedValue.append(command.charAt(i));
				}
			}

			command = command.replaceAll("\\s", "");
			SpreadsheetLocation location = new SpreadsheetLocation(command.substring(0, 2));



			int row = location.getRow();
			int col = location.getCol();
			cells[row][col] = new TextCell(assignedValue.toString());
		}
		return getGridText();
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getGridText()
	{
		StringBuilder toReturn = new StringBuilder();
		toReturn.append("   |");
		for (int i = 0; i<getCols(); i++){
			toReturn.append((char)(i+65));
			for (int j = 0; j<TextExcel.CELL_SPACE-1; j++){
				toReturn.append(" ");
			}
			toReturn.append("|");
		}
		toReturn.append("\n");

		for (int i = 0; i<getRows(); i++){
			toReturn.append(i+1 + "  |");
			for (int j = 0; j<getCols(); j++){
				toReturn.append(cells[j][i].abbreviatedCellText()  + "|");
			}
			toReturn.append("\n");
		}

		// TODO Auto-generated method stub
		return toReturn.toString();
	}

}
