package runner;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.annotations.AfterClass;
import org.testng.annotations.DataProvider;


public class A {

	@BeforeClass(alwaysRun=true)
	public void setUp() {
        System.out.println("class a before class");
    }

	@Test(groups={"create"}, dataProvider="createData")
	public void create(String name) {
        System.out.println("in create a " + name + " Thread - " + Thread.currentThread().getId());
    }

    @DataProvider(name = "createData", parallel=true)
    public Object[][] createData() {
        return new Object[][] {
            {"ca1"},
            {"ca2"},
        };
    }

	@Test(groups={"delete"}, dependsOnGroups={"create"}, dataProvider="deleteData")
	public void delete(String name) {
        System.out.println("in delete a " + name + " Thread - " + Thread.currentThread().getId());
    }

    @DataProvider(name = "deleteData", parallel=true)
    public Object[][] deleteData() {
        return new Object[][] {
            {"da1"},
            {"da2"},
        };
    }

	@AfterClass(alwaysRun=true)
	public void teardown() {
        System.out.println("class a after class");
    }
}
