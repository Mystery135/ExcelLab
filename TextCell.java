package textExcel;

import textExcel.helpers.Utils;

public class TextCell implements Cell{
    private final String value;
    public TextCell(String value){
        this.value = value;
    }
    @Override
    public String abbreviatedCellText() {//Returns the abbreviated text
        return Utils.abbreviateText(value);
    }

    @Override
    public String fullCellText() {//Returns the full text, with the quotation marks
        return "\"" + value + "\"";
    }
}
