package main.java.model.rating;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;

public class RatingTable {
    private ArrayList<RatingLine> lines = new ArrayList<RatingLine>();

    final private int tableSize = 3;

    public void add(String name, int suns) {
        RatingLine line = new RatingLine();
        line.name = name;
        line.suns = suns;
        lines.add(line);
        lines.sort(comparator);
        if(lines.size() > tableSize) {
            lines.remove(3);
        }
    }

    public ArrayList<RatingLine> getRatingTable() {
        return lines;
    }

    public void saveToFile(String filename) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

        writer.write(lines.size() + "\n");
        for(int i = 0; i < lines.size(); i++) {
            lines.get(i).writeToWriter(writer);
        }

        writer.flush();
        writer.close();
    }

    public static RatingTable loadFromFile(String filename) throws IOException {
        if(!new File(filename).exists()) return new RatingTable();

        BufferedReader reader = new BufferedReader(new FileReader(filename));

        int size = Integer.parseInt(reader.readLine());

        RatingTable table = new RatingTable();
        for(int i = 0; i < size; i++) {
            table.lines.add(RatingLine.readFromReader(reader));
        }

        reader.close();

        return table;
    }

    public int getSize() {
        return lines.size();
    }

    final private Comparator comparator = new Comparator<RatingLine>() {
        @Override
        public int compare(RatingLine o1, RatingLine o2) {
            if (o1.suns < o2.suns) return 1;
            else if (o1.suns > o2.suns) return -1;
            else return 0;
        }
    };
}

