package textExcel;

public class TextCell implements Cell{
    private String value;
    public TextCell(String value){
        this.value = value;
    }
    @Override
    public String abbreviatedCellText() {
        StringBuilder toReturn = new StringBuilder();
        if (value.length() < 10){
            toReturn.append(value);
            for (int i = 0; i<10-value.length(); i++){
                toReturn.append(" ");
            }
        }else if (value.length() > 10){
            toReturn.append(value, 0, 10);
        }
        return toReturn.toString();
    }

    @Override
    public String fullCellText() {
        return value;
    }
}
