package runner;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.annotations.AfterClass;
import org.testng.annotations.DataProvider;


public class C {

	@BeforeClass(alwaysRun=true)
	public void setUp() {
        System.out.println("class c before class");
    }

	@Test(groups = {"create"}, dataProvider="createData")
	public void create(String name) {
        System.out.println("in create c " + name + " Thread - " + Thread.currentThread().getId());
    }

    @DataProvider(name = "createData", parallel=true)
    public Object[][] createData() {
        return new Object[][] {
            {"cc1"},
            {"cc2"},
        };
    }

	@Test(groups={"delete"}, dependsOnGroups={"create"}, priority=-2, dataProvider="deleteData")
	public void delete(String name) {
        System.out.println("in delete c " + name + " Thread - " + Thread.currentThread().getId());
    }

    @DataProvider(name = "deleteData", parallel=true)
    public Object[][] deleteData() {
        return new Object[][] {
            {"dc1"},
            {"dc2"},
        };
    }

	@AfterClass(alwaysRun=true)
	public void teardown() {
        System.out.println("class c after class");
    }
}
