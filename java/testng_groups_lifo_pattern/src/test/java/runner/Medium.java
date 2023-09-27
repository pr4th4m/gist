package runner;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.annotations.AfterClass;
import org.testng.annotations.DataProvider;

public class Medium {

    @BeforeClass(alwaysRun = true)
    public void setUp() {
    }

    @Test(groups = { "open" }, dataProvider = "openData")
    public void open(String name) {
        System.out.println("Thread " + Thread.currentThread().getId() + " - " + name);
    }

    @DataProvider(name = "openData", parallel = true)
    public Object[][] openData() {
        return new Object[][] {
                { "Russian matryoshka medium open" },
        };
    }

    @Test(groups = { "close" }, dependsOnGroups = { "open" }, priority = -1, dataProvider = "closeData")
    public void close(String name) {
        System.out.println("Thread " + Thread.currentThread().getId() + " - " + name);
    }

    @DataProvider(name = "closeData", parallel = true)
    public Object[][] closeData() {
        return new Object[][] {
                { "Russian matryoshka medium close" },
        };
    }

    @AfterClass(alwaysRun = true)
    public void teardown() {
    }
}
