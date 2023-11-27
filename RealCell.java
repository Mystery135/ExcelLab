package textExcel;

import textExcel.helpers.Utils;

public abstract class RealCell implements Cell{
    String value;
    public RealCell(String value){
        this.value = value;
    }
    public abstract double getDoubleValue();

    @Override
    public String abbreviatedCellText() {
        return Utils.abbreviateText(value);
    }

    @Override
    public String fullCellText() {
        return value;
    }
}
