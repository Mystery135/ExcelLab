package textExcel;

public class SpreadsheetLocation implements Location {
    private final int row;
    private final int col;

    //Converts a character and a number to a coordinate
    public SpreadsheetLocation(String loc){
        col = loc.toUpperCase().toCharArray()[0]-65;
        row = Integer.parseInt(loc.substring(1))-1;
    }

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getCol() {
        return col;
    }
}
