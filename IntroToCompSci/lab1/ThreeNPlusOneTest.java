
import org.junit.Test;
import static org.junit.Assert.*;

public class ThreeNPlusOneTest {

	@Test
	public void test3(){
		assertEquals("did not yield proper results", "3,10,5,16,8,4,2,1", ThreeNPlusOne.generateThreeN(3));
	}

	@Test 
	public void test5(){
		assertEquals("did not yield proper results", "5,16,8,4,2,1", ThreeNPlusOne.generateThreeN(5));
	}

}