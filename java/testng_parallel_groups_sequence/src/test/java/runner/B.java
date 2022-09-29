package runner;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.annotations.AfterClass;
import org.testng.annotations.DataProvider;


public class B {

	@BeforeClass(alwaysRun=true)
	public void setUp() {
        System.out.println("class b before class");
    }

	@Test(groups = {"create"}, dataProvider="createData")
	public void create(String name) {
        System.out.println("in create b " + name + " Thread - " + Thread.currentThread().getId());
    }

    @DataProvider(name = "createData", parallel=true)
    public Object[][] createData() {
        return new Object[][] {
            {"cb1"},
            {"cb2"},
        };
    }

	@Test(groups={"delete"}, dependsOnGroups={"create"}, priority=-1, dataProvider="deleteData")
	public void delete(String name) {
        System.out.println("in delete b " + name + " Thread - " + Thread.currentThread().getId());
    }

    @DataProvider(name = "deleteData", parallel=true)
    public Object[][] deleteData() {
        return new Object[][] {
            {"db1"},
            {"db2"},
        };
    }

	@AfterClass(alwaysRun=true)
	public void teardown() {
        System.out.println("class b after class");
    }
}
