
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.*;

/**
 * The test class TestRouter.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class RouterTest
{

    //private TrieST<Integer> router;
    private IPRouter router;
    /**
     * Default constructor for test class TestRouter
     */
    public RouterTest() {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
       this.router = new IPRouter(8,3);
        try {
            router.loadRoutes("src/main/java/routes1.txt");
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException("Bad routes file name. Tests aborted");
        }
    }

    /**
     * Handle an unroutable address
     */
    @Test
    public void testBadRoute()
    {
        IPAddress address = new IPAddress("73.73.0.1");
        assertEquals(-1, this.router.getRoute(address));
    }

    /**
     * Handle an address that only matches one prefix
     */
    @Test
    public void port2Test()
    {
        IPAddress address = new IPAddress("85.2.0.1");
        int res = this.router.getRoute(address);
        assertEquals(2, res);
    }

    /**
     * Handle an address that only matches multiple prefixes. Only the longest one counts
     */
    @Test
    public void port1Test() {
        IPAddress address = new IPAddress("85.85.85.85");
        int res = this.router.getRoute(address);
        assertEquals(1, res);
    }

    //Test to see if putting same prefix in twice with different port values yields an exception
    @Test (expected =  IllegalArgumentException.class)
    public void repeatPuts()  {
        router.addRule("85.0.0.0/8", 3);
    }

    //testing 3 overlapping rules for one address
    @Test
    public void threeOverlappingRules(){
        router.addRule("24.98.0.0/15", 6);
        router.addRule("24.91.0.0/16", 7);
        router.addRule("24.91.0.0/14", 4);
        IPAddress address = new IPAddress("24.91.45.234");
        int res = this.router.getRoute(address);
        assertEquals(7, res);
    }

    //Doesn't match where length indicator is 1 less than smallest length
    @Test
    public void shortLength(){
        //Shortest is 8
        IPAddress address = new IPAddress("85.0.0.0/7");
        int res = this.router.getRoute(address);
        assertEquals(-1, res);
    }

    @Test
    public void exactlyTheSame(){
        router.addRule("24.98.23.25/32", 2);
        IPAddress address = new IPAddress("24.98.23.25/32");
        int res = this.router.getRoute(address);
        assertEquals(2, res);
    }

    @Test (expected =  IllegalArgumentException.class)
    public void portOutOfRange (){
        router.addRule("24.98.23.25/32", 10);
    }

    @Test
    public void delete(){
        IPAddress address = new IPAddress("85.85.85.85");
        int res = this.router.getRoute(address);
        assertEquals(1, res);
        this.router.deleteRule("85.85.0.0/15");
        //Should still be routed to other route
        res = this.router.getRoute(address);
        assertEquals(2, res);
        //delete second route
        this.router.deleteRule("85.0.0.0/8");
        res = this.router.getRoute(address);
        assertEquals(-1, res);
    }


    @Test
    public void cacheSimpleTest(){
        try {
            router.loadRoutes("src/main/java/routes2.txt");
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException("Bad routes file name. Tests aborted");
        }

        IPAddress address1 = new IPAddress("24.98.23.25/29");
        IPAddress address2 = new IPAddress("24.0.78.25/30");
        IPAddress address3 = new IPAddress("85.43.23.25/31");
        String[] test = {address3.toCIDR(), address2.toCIDR(), address1.toCIDR()};
        this.router.getRoute(address1);
        this.router.getRoute(address2);
        this.router.getRoute(address3);
        String[] array = this.router.dumpCache();
        for (int i = 0; i < array.length; i++){
            assertEquals(test[i], array[i]);
        }
    }

    @Test
    public void bumpedFromCache(){
        try {
            router.loadRoutes("src/main/java/routes2.txt");
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException("Bad routes file name. Tests aborted");
        }

        IPAddress address1 = new IPAddress("24.98.23.25/29");
        IPAddress address2 = new IPAddress("24.0.78.25/30");
        IPAddress address3 = new IPAddress("85.43.23.25/31");
        IPAddress address4 = new IPAddress("85.85.0.0/15");
        String[] test = {address4.toCIDR(), address3.toCIDR(), address2.toCIDR()};
        this.router.getRoute(address1);
        this.router.getRoute(address2);
        this.router.getRoute(address3);
        this.router.getRoute(address4);
        String[] array = this.router.dumpCache();
        for (int i = 0; i < array.length; i++){
            assertEquals(test[i], array[i]);
        }
    }

    @Test
    public void updateCache(){
        try {
            router.loadRoutes("src/main/java/routes2.txt");
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException("Bad routes file name. Tests aborted");
        }

        IPAddress address1 = new IPAddress("24.98.23.25/29");
        IPAddress address2 = new IPAddress("24.0.78.25/30");
        IPAddress address3 = new IPAddress("85.43.23.25/31");
        this.router.getRoute(address1);
        this.router.getRoute(address2);
        this.router.getRoute(address3);
        this.router.getRoute(address2);
        String[] test = {address2.toCIDR(), address3.toCIDR(), address1.toCIDR()};
        String[] array = this.router.dumpCache();
        for (int i = 0; i < array.length; i++){
            assertEquals(test[i], array[i]);
        }
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown() {
        this.router = null;
    }
}
