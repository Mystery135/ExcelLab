package textExcel;

import textExcel.helpers.Utils;

public abstract class RealCell implements Cell{
    String value;
    public RealCell(String value){
        this.value = value;
    }
    public abstract double getDoubleValue();//All RealCells need to override getDoubleValue()

    @Override
    public String abbreviatedCellText() {
        return Utils.abbreviateText(value);//By default, all RealCells return their value abbreviated
    }

    @Override
    public String fullCellText() {
        return value;//By default all RealCells return their raw value
    }
}
