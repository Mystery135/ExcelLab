package textExcel;


public class EmptyCell implements Cell{
    private final String value;
    public EmptyCell(){
        //Adds CELL_SPACE spaces to the value
        this.value = " ".repeat(Math.max(0, Constants.CELL_SPACE));
    }
    @Override
    public String abbreviatedCellText() {
        return value;
    }

    @Override
    public String fullCellText() {
        return "";
    }
}
