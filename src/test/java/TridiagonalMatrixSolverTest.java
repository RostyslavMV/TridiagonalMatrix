import com.rmv.oop.task2.model.TridiagonalMatrix;
import com.rmv.oop.task2.service.TridiagonalMatrixSolver;
import com.rmv.oop.task2.service.TridiagonalMatrixValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class TridiagonalMatrixSolverTest {

    @Mock
    private TridiagonalMatrixValidator validator;

    @InjectMocks
    TridiagonalMatrixSolver tridiagonalMatrixSolver;

    @Test
    void solve() {
        List<Double> a = new ArrayList<>(Arrays.asList(1.0, 1.0, 1.0, 1.0));
        List<Double> c = new ArrayList<>(Arrays.asList(2.0, 2.0, 2.0, 2.0, 2.0));
        List<Double> b = new ArrayList<>(Arrays.asList(1.0, 1.0, 1.0, 1.0));
        List<Double> f = new ArrayList<>(Arrays.asList(1.0, 1.0, 1.0, 1.0, 1.0));
        TridiagonalMatrix matrix = new TridiagonalMatrix(a, c, b, f);
        when(validator.isMatrixValid(matrix)).thenReturn(true);
        List<Double> result = tridiagonalMatrixSolver.solve(matrix);
        List<Double> expectedResult = new ArrayList<>(Arrays.asList(0.5, 0.0, 0.5, 0.0, 0.5));
        assertEquals(expectedResult, result);
    }
}
