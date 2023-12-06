package textExcel;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class History {
    private int length;
    ArrayList<String> list;
    public History(int length){
         list = new ArrayList<String>();
        this.length = length;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
    public void remove(int index){
        list.remove(index);
    }
    public void add(int index){
        if (list.listIterator)

    }
    public ArrayList<String> getList(){
        return list;
    }
    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i<list.size(); i++){
            builder.append(list.get(i));
        }
        return builder.toString();
    }


}
