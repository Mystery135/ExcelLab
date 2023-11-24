package textExcel;

public class TextCell implements Cell{
    private String value;
    public TextCell(String value){
        this.value = value;
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
