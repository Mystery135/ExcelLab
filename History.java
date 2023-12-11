package textExcel;

import java.util.ArrayList;

public class History {
    ArrayList<String> list;
    //History class which uses an arraylist
    private final int length;

    public History(int length) {
        list = new ArrayList<>();
        this.length = length;
    }

    public void clear() {
        list.clear();
    }

    public int size() {
        return list.size();
    }

    public void remove(int index) {
        list.remove(index);
    }

    public void add(String command) {
        list.add(command);
        if (list.size() > length) {
            list.remove(0);
        }
    }

    //Returns history in a list format, from most recent to lest recent
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (int i = list.size() - 1; i >= 0; i--) {
            builder.append(list.get(i)).append("\n");
        }
        return builder.toString();
    }


}
