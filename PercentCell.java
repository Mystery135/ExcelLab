package textExcel;


public class PercentCell extends RealCell{
    public PercentCell(String value) {
        super(value);
    }

    @Override
    public double getDoubleValue() {
        return Double.parseDouble(value.replace("%", ""))/100;//Returns a double value by dividing the percent value by 100 and removing the %
    }
    @Override
    public String fullCellText() {
        return String.valueOf(getDoubleValue());
    }

}
