package com.rmv.oop.task2.service;

import com.rmv.oop.task2.model.ModifiedCoefficients;
import com.rmv.oop.task2.model.TridiagonalMatrix;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class TridiagonalMatrixSolver {

    private TridiagonalMatrixValidator validator;

    @Setter
    private TridiagonalMatrix matrix;

    private List<Double> a;
    private List<Double> c;
    private List<Double> b;
    private List<Double> f;

    private Double[] leftAlpha;
    private Double[] leftBeta;
    private Double[] rightAlpha;
    private Double[] rightBeta;

    private ModifiedCoefficients leftCoefficients;
    private ModifiedCoefficients rightCoefficients;

    private List<Double> results = new ArrayList<>();

    @Autowired
    public TridiagonalMatrixSolver(TridiagonalMatrixValidator validator) {
        this.validator = validator;
    }

    public List<Double> solve(TridiagonalMatrix matrix) {
        this.matrix = matrix;
        return solve();
    }

    private List<Double> solve() {
        if (matrix == null) {
            log.info("Matrix is null, please set a matrix");
            return null;
        }
        if (!validator.isMatrixValid(matrix)) {
            log.info("Matrix is not a diagonally dominant matrix");
            return null;
        }
        findCoefficients();
        for (int i = 0; i < c.size(); i++) {
            results.add(0.0);
        }
        findMiddleResult();
        findAllResults();
        return results;
    }

    private void findCoefficients() {
        a = matrix.getA();
        c = matrix.getC();
        b = matrix.getB();
        f = matrix.getF();

        leftAlpha = new Double[c.size()];
        leftBeta = new Double[c.size()];
        rightAlpha = new Double[c.size()];
        rightBeta = new Double[c.size()];
        Arrays.fill(leftAlpha, 0.0);
        Arrays.fill(leftBeta, 0.0);
        Arrays.fill(rightAlpha, 0.0);
        Arrays.fill(rightBeta, 0.0);

        Thread left = new Thread(() -> {
            leftAlpha[0] = c.get(0);
            leftBeta[0] = b.get(0);
            leftAlpha[1] = -b.get(0) / c.get(0);
            leftBeta[1] = f.get(0) / c.get(0);
            for (int i = 2; i <= Math.floor(c.size() / 2); i++) {
                leftAlpha[i] = calculateLeftAlpha(i);
                leftBeta[i] = calculateLeftBeta(i);
            }
        });

        Thread right = new Thread(() -> {
            int n = c.size() - 1;
            rightAlpha[n] = -a.get(a.size() - 1) / c.get(c.size() - 1);
            rightBeta[n] = f.get(f.size() - 1) / c.get(c.size() - 1);
            for (int i = c.size() - 2; i >= Math.floor(c.size() / 2) - 1; i--) {
                rightAlpha[i] = calculateRightAlpha(i);
                rightBeta[i] = calculateRightBeta(i);
            }
        });

        left.start();
        right.start();
        try {
            left.join();
            right.join();
        } catch (InterruptedException e) {
            log.error(e.toString());
        }

        leftCoefficients = new ModifiedCoefficients(leftAlpha, leftBeta);
        rightCoefficients = new ModifiedCoefficients(rightAlpha, rightBeta);
    }

    private Double calculateLeftAlpha(int i) {
        return -b.get(i - 1) / (c.get(i - 1) + a.get(i - 2) * leftAlpha[i - 1]);
    }

    private Double calculateLeftBeta(int i) {
        return (f.get(i - 1) - a.get(i - 2) * leftBeta[i - 1])
                / (c.get(i - 1) + a.get(i - 2) * leftAlpha[i - 1]);
    }

    private Double calculateRightAlpha(int i) {
        return -a.get(i - 1) / (c.get(i) + b.get(i) * rightAlpha[i + 1]);
    }

    private Double calculateRightBeta(int i) {
        return (f.get(i) - b.get(i) * rightBeta[i + 1])
                / (c.get(i) + b.get(i) * rightAlpha[i + 1]);
    }

    private void findMiddleResult() {
        int p = (int) Math.floor((c.size()) / 2) - 1;
        Double middleX = (leftCoefficients.getAlpha()[p + 1] * rightCoefficients.getBeta()[p + 1]
                + leftCoefficients.getBeta()[p + 1]) / (1 - leftCoefficients.getAlpha()[p + 1]
                * rightCoefficients.getAlpha()[p + 1]);
        results.set((int) (Math.floor((c.size()) / 2) - 1), middleX);
    }


    public void findAllResults() {
        Thread left = new Thread(() -> {
            for (int i = (int) Math.floor((c.size()) / 2) - 2; i >= 0; i--) {
                results.set(i, results.get(i + 1) * leftCoefficients.getAlpha()[i + 1]
                        + leftCoefficients.getBeta()[i + 1]);
            }
        });
        Thread right = new Thread(() -> {
            for (int i = (int) Math.floor((c.size()) / 2); i <= c.size() - 1; i++) {
                results.set(i, results.get(i - 1) * rightCoefficients.getAlpha()[i]
                        + rightCoefficients.getBeta()[i]);
            }
        });
        left.start();
        right.start();
        try {
            left.join();
            right.join();
        } catch (InterruptedException e) {
            log.error(e.toString());
        }
    }
}
