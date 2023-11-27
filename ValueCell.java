package textExcel;

import textExcel.helpers.Utils;

public class ValueCell extends RealCell{
    public ValueCell(String value) {
        super(value);
    }

    @Override
    public double getDoubleValue() {
        return Double.parseDouble(value);
    }
}
