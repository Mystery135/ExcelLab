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
    public void clear(){
        list.clear();
    }

    public int size(){
        return list.size();
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
    public void add(String command)
        {
            list.add(command);
            if (list.size() > length){
            list.remove(0);
        }
    }
    public ArrayList<String> getList(){
        return list;
    }
    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();

        for (int i = list.size()-1; i>=0; i--){
            builder.append(list.get(i)).append("\n");
        }
        return builder.toString();
    }


}
