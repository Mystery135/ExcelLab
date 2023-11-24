package textExcel;
// Update this file with your own code.

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
		if (command.trim().length() == 2 && Character.isDigit(command.toCharArray()[1]) && Character.isLetter(command.toCharArray()[0])){
			return processCellInspection(command.trim().toUpperCase());
		}
		return null;
	}
	private String processCellInspection(String command){
		int xIndex = command.toCharArray()[0]-65;
		int yIndex = Integer.parseInt(command.substring(1,2));
		return cells[xIndex][yIndex].abbreviatedCellText() + "fgjidos;g";

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
			toReturn.append("      " + (char)(i+65) + "       |");

		}
		toReturn.append("\n");

		for (int i = 0; i<getRows(); i++){
			toReturn.append(i+1 + "  |");
			for (int j = 0; j<getCols(); j++){
				toReturn.append(cells[i][j].abbreviatedCellText()  + "|");
			}
			toReturn.append("\n");
		}


		// TODO Auto-generated method stub
		return toReturn.toString();
	}

}
