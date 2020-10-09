package com.rmv.oop.task2.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@AllArgsConstructor
@Getter
@Setter
@Slf4j
public class TridiagonalMatrix {
    // diagonal below the main diagonal of a matrix
    private List<Double> a;
    //main diagonal of a matrix
    private List<Double> c;
    // diagonal above the main diagonal of a matrix
    private List<Double> b;
    // column vector (values after the = in equations)
    private List<Double> f;

    public TridiagonalMatrix(File fileWithMatrix) {
        a = new ArrayList<>();
        c = new ArrayList<>();
        b = new ArrayList<>();
        f = new ArrayList<>();
        try {
            Scanner myReader = new Scanner(fileWithMatrix);
            int i = 0;
            while (myReader.hasNextLine()) {
                String readedLine = myReader.nextLine();
                String[] splited = readedLine.split("\\s+");
                c.add(Double.parseDouble(splited[i]));
                f.add(Double.parseDouble(splited[splited.length - 1]));
                if (i > 0) {
                    a.add(Double.parseDouble(splited[i - 1]));
                }
                if (i < splited.length - 2) {
                    b.add(Double.parseDouble(splited[i + 1]));
                }
                i++;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            log.error(e.toString());
        }
    }
}
