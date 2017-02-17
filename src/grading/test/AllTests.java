package grading.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    ServerTests.class,
    QueryTests.class,
    UpdateTests.class,
    RegressionTests.class,
    KMeansTests.class
})

public class AllTests {

}
