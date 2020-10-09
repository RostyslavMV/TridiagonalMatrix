package com.rmv.oop.task2;

import com.rmv.oop.task2.model.TridiagonalMatrix;
import com.rmv.oop.task2.service.TridiagonalMatrixSolver;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.net.URL;
import java.util.List;

@SpringBootApplication
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class Main implements CommandLineRunner {

    private TridiagonalMatrixSolver tridiagonalMatrixSolver;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) {
        URL matrixURL = getClass().getClassLoader().getResource("matrix.txt");
        File matrixFile = new File(matrixURL.getFile());
        TridiagonalMatrix tridiagonalMatrix = new TridiagonalMatrix(matrixFile);
        List<Double> res = tridiagonalMatrixSolver.solve(tridiagonalMatrix);
        if (res != null) {
            System.out.println(res);
        }
    }
}
