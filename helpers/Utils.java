package textExcel.helpers;

import textExcel.TextExcel;

public class Utils {
    public static String abbreviateText(String value){
        StringBuilder toReturn = new StringBuilder();
        if (value.length() < TextExcel.CELL_SPACE){
            toReturn.append(value);
            for (int i = 0; i<TextExcel.CELL_SPACE-value.length(); i++){
                toReturn.append(" ");
            }
        }else if (value.length() > TextExcel.CELL_SPACE){
            toReturn.append(value, 0, TextExcel.CELL_SPACE);
        }else{
            toReturn.append(value);
        }
        return toReturn.toString();
    }
    public static String formatText(String s){
        return s.replaceAll("\\s", "").toUpperCase();
    }
}
