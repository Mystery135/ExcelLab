package textExcel;

public class SpreadsheetLocation implements Location {
    private String loc;
    private int row;
    private int col;
    public SpreadsheetLocation(String loc){
        this.loc = loc;
        row = loc.toUpperCase().toCharArray()[0]-65;
        col = Integer.parseInt(loc.substring(1))-1;

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
