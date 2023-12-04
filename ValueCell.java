package textExcel;

import textExcel.helpers.Utils;

public class ValueCell extends RealCell{
    public ValueCell(String value) {
        super(String.valueOf(Double.parseDouble(value)));//So it turns into scientific notation
    }

    @Override
    public double getDoubleValue() {
        return Double.parseDouble(value);
    }
}
