package main.java.model.rating;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class RatingLine {
    public String name;
    public int suns;

    public void writeToWriter(BufferedWriter writer) throws IOException {
        writer.write(name + " " + suns + "\n");
    }

    public static RatingLine readFromReader(BufferedReader reader) throws IOException {
        String[] lines = reader.readLine().split(" ");

        RatingLine ratingLine = new RatingLine();
        ratingLine.name = lines[0];
        ratingLine.suns = Integer.parseInt(lines[1]);

        return ratingLine;
    }

}
