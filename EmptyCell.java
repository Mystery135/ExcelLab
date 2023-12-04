package textExcel;

import org.w3c.dom.Text;

public class EmptyCell implements Cell{
    private String value;
    public EmptyCell(){
        StringBuilder value = new StringBuilder();
        for (int i = 0; i<Constants.CELL_SPACE; i++){
            value.append(" ");
        }
        this.value = value.toString();
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
