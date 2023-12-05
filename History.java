package textExcel;

import java.util.ArrayList;

public class History extends ArrayList<Void> {
    private int length;
    public History(int length){
        this.length = length;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
    @Override
    public Void remove(int index){
        

        return null;
    }


}
