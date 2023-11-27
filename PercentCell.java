package textExcel;

import textExcel.helpers.Utils;

public class PercentCell extends RealCell{
    public PercentCell(String value) {
        super(value);
    }

    @Override
    public double getDoubleValue() {
        return Double.parseDouble(value.replace("%", ""))*100;
    }


}
