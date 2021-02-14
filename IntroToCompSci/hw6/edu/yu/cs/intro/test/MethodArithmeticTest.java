package edu.yu.cs.intro.test;
import edu.yu.cs.intro.MethodArithmetic;
import org.junit.Test;
import static org.junit.Assert.*;

public class MethodArithmeticTest {

	@Test
    public void testAddNormal(){
        int[] values = {3,2,3,5};
        assertEquals("adding 2+3+5 did not work","2+3+5=10", MethodArithmetic.add(values));
    }
    
    @Test (expected = NullPointerException.class)
    public void testAddInvalid(){
    	MethodArithmetic.add(null);
    }
	
    @Test (expected = ArithmeticException.class)
    public void testAddBoundary(){
    	int [] values = {2, Integer.MAX_VALUE, 1};
    	MethodArithmetic.add(values);
    }

    @Test
    public void testSubtractNormal(){
        int[] values = {3,2,3,5};
        assertEquals("subtracting 2-3-5 did not work","2-3-5=-6", MethodArithmetic.subtract(values));
    }

    @Test (expected = NullPointerException.class)
    public void testSubtractInvalid(){
    	MethodArithmetic.subtract(null);
    }

    @Test (expected = ArithmeticException.class)
    public void testSubtractBoundary(){
    	int [] values = {2, Integer.MIN_VALUE, 1};
    	MethodArithmetic.subtract(values);
    }

    @Test 
    public void testMultiplyNormal (){
    	int[] values = {3,2,3,5};
        assertEquals("multiplying 2*3*5 did not work","2*3*5=30", MethodArithmetic.multiply(values));
    }

    @Test (expected = NullPointerException.class)
    public void testMultiplyInvalid(){
    	MethodArithmetic.multiply(null);
    }

    @Test (expected = ArithmeticException.class)
    public void testMultiplyBoundary() {
    	int [] values = {2, Integer.MAX_VALUE, 2};
    	MethodArithmetic.multiply(values);
    }

    @Test 
    public void testDivideNormal () {
    	int[] values = {3,50,2,5};
        assertEquals("dividng 50/2/5 did not work","50/2/5=5.0", MethodArithmetic.divide(values));
    }

	@Test (expected = ArithmeticException.class)
    public void testDivideInvalid() {
    	int [] values = {3, 10, 5, 0};
    	MethodArithmetic.divide(values);
    }

    @Test
    public void testDivideBoundary() {
    	int [] values = {2, -20, -2};
    	assertEquals("dividing -20/-2 did not work", "-20/-2=10.0", MethodArithmetic.divide(values));
    }

    @Test
    public void testIsArithmeticNormal() {
    	assertEquals("inputing add did not work", true, MethodArithmetic.isArithmetic("add"));
    }

    @Test
    public void testIsArithmeticBoundary() {
    	assertEquals("inputing bus did not yield false", false, MethodArithmetic.isArithmetic("bus"));
    }

    @Test (expected = NullPointerException.class)
    public void testIsArithmeticInvalid() {
    	MethodArithmetic.isArithmetic(null);
    }

    @Test
    public void testArrayLengthDoublerNormal() {
    	int [] test = {3, 1, 2, 3};
    	int [] result = MethodArithmetic.arrayLengthDoubler(test);
    	int [] match = {3, 1, 2, 3, 0, 0, 0, 0};
    	assertArrayEquals("Did not properly double array", match, result);
    }

    @Test (expected = NullPointerException.class)
    public void testArrayLengthDoublerInvalid () {
    	MethodArithmetic.arrayLengthDoubler(null);
    }

    @Test
    public void testArrayLengthDoublerBoundary() {
    	int [] test = new int [3];
    	int [] result = MethodArithmetic.arrayLengthDoubler(test);
    	int [] match = {0, 0, 0, 0, 0, 0};
    	assertArrayEquals("Did not properly double array", match, result);
    }

}