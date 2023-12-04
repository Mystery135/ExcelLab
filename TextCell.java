package textExcel;

import textExcel.helpers.Utils;

public class TextCell implements Cell{
    private String value;
    public TextCell(String value){
        this.value = value;
    }
    @Override
    public String abbreviatedCellText() {
        return Utils.abbreviateText(value);
    }

    @Override
    public String fullCellText() {
        return "\"" + value + "\"";
    }
}
