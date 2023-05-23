package site.facade;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import site.app.Application;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class NumberGeneratorServiceTest {

    private static final long START_NUMBER = 8000000001L;

    private static final int RUNS = 100;

    private static ForkJoinPool EXECUTION_POOL;

    @Autowired
    private NumberGeneratorService numberGeneratorService;

    @BeforeClass
    public static void setupPool() {
        EXECUTION_POOL = new ForkJoinPool(10);
    }

    @AfterClass
    public static void shutdownPool() {
        if (EXECUTION_POOL != null) {
            EXECUTION_POOL.shutdown();
        }
    }

    @Test
    public void generateInvoiceNumbersInParallel() throws ExecutionException, InterruptedException {
        List<Integer> list = IntStream.range(0, RUNS).boxed().collect(Collectors.toList());

        Set<Long> invoiceNumbers = EXECUTION_POOL.submit(() -> list.parallelStream()
            .map(i -> numberGeneratorService.getRealInvoiceNumber())
            .collect(Collectors.toSet())).get();

        assertEquals(RUNS, invoiceNumbers.size());
        assertEquals(RUNS,
            LongStream.range(START_NUMBER, START_NUMBER + RUNS).filter(invoiceNumbers::contains).count());
    }
}