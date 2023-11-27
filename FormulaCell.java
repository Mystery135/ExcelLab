package textExcel;

public class FormulaCell extends RealCell{
    public FormulaCell(String value) {
        super(value);
    }

    @Override
    public double getDoubleValue() {
        return 0;
    }

    @Override
    public String abbreviatedCellText() {
        return null;
    }

    @Override
    public String fullCellText() {
        return null;
    }
}
