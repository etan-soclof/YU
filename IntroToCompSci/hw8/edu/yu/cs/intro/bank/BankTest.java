package edu.yu.cs.intro.bank;
import edu.yu.cs.intro.bank.exceptions.*;
import edu.yu.cs.intro.bank.*;
import org.junit.*;
import static org.junit.Assert.*;

public class BankTest {

	public Bank bank = Bank.getBank();

	@Before
	public void createPatrons() throws AuthenticationException, UnauthorizedActionException{
		//Creating Patrons
		bank.createNewPatron("firstName0", "lastName0", 0, "userName0", "password0");
		bank.createNewPatron("firstName1", "lastName1", 1, "userName1", "password1");
		bank.createNewPatron("firstName2", "lastName2", 2, "userName2", "password2");
		bank.createNewPatron("firstName3", "lastName3", 3, "userName3", "password3");
		bank.createNewPatron("firstName4", "lastName4", 4, "userName4", "password4");
		bank.createNewPatron("firstName5", "lastName5", 5, "userName5", "password5");
		//Creating Accounts
		bank.openSavingsAccount(0, "userName0", "password0");
		bank.openSavingsAccount(1, "userName1", "password1");
		bank.openSavingsAccount(2, "userName2", "password2");
		bank.openBrokerageAccount(0, "userName0", "password0");
		bank.openBrokerageAccount(2, "userName2", "password2");

		//Creating Stocks
		bank.addNewStockToMarket("A", 1);
		bank.addNewStockToMarket("B", 2);
		bank.addNewStockToMarket("C", 3);
		bank.addNewStockToMarket("D", 4);
		bank.addNewStockToMarket("E", 5);
	}

	@After
	public void clear(){
		bank.clear();
	}

	@Test
	public void testDepositCashIntoSavingsAndBrokerage() throws AuthenticationException, UnauthorizedActionException{
		bank.depositCashIntoSavings(0, "userName0", "password0", 5);
		bank.depositCashIntoSavings(1, "userName1", "password1", 1);
		assertEquals(5, bank.checkBalanceSavings(0, "userName0", "password0"), .05);
		assertEquals(1, bank.checkBalanceSavings(1, "userName1", "password1"), .05);
	}

	@Test
	public void testTransferFromSavingsToBrokerageAndViceVersa() throws AuthenticationException, UnauthorizedActionException, InsufficientAssetsException {
		bank.depositCashIntoSavings(0, "userName0", "password0", 5);
		bank.transferFromSavingsToBrokerage(0, "userName0", "password0", 5);
		assertEquals(5, bank.checkAvailableBalanceBrokerage(0, "userName0", "password0"), .05);
		bank.transferFromBrokerageToSavings(0, "userName0", "password0", 5);
		assertEquals(0, bank.checkAvailableBalanceBrokerage(0, "userName0", "password0"), .05);
		assertEquals(5, bank.checkBalanceSavings(0, "userName0", "password0"), .05);

	}

	@Test
	public void testWithdrawMethods() throws AuthenticationException, UnauthorizedActionException, InsufficientAssetsException {
		//Test Withdraw from savings
		bank.depositCashIntoSavings(0, "userName0", "password0", 5);
		assertEquals(5, bank.checkBalanceSavings(0, "userName0", "password0"), .05);
		bank.withdrawCashFromSavings(0, "userName0", "password0", 5);
		assertEquals(0, bank.checkBalanceSavings(0, "userName0", "password0"), .05);
		//Test Withdraw from Brokerage
		bank.depositCashIntoSavings(0, "userName0", "password0", 5);
		bank.transferFromSavingsToBrokerage(0, "userName0", "password0", 5);
		assertEquals(5, bank.checkAvailableBalanceBrokerage(0, "userName0", "password0"), .05);
		bank.withdrawCashFromBrokerage(0, "userName0", "password0", 5);
		assertEquals(0, bank.checkAvailableBalanceBrokerage(0, "userName0", "password0"), .05);
	}

	@Test
	public void testBuyStocks() throws AuthenticationException, UnauthorizedActionException, InsufficientAssetsException {
		bank.depositCashIntoSavings(0, "userName0", "password0", 100);
		bank.transferFromSavingsToBrokerage(0, "userName0", "password0", 50);
		bank.purchaseStock(0, "userName0", "password0", "A", 5);
		bank.purchaseStock(0, "userName0", "password0", "B", 5);
		bank.purchaseStock(0, "userName0", "password0", "C", 5);
		assertArrayEquals(new String [] {"A", "B", "C", null}, bank.getListOfAllStockTickerSymbols());
	}

	@Test
	public void testSellStockAndGetListOfAllStockTickerSymbols() throws AuthenticationException, UnauthorizedActionException, InsufficientAssetsException {
		bank.depositCashIntoSavings(0, "userName0", "password0", 100);
		bank.transferFromSavingsToBrokerage(0, "userName0", "password0", 50);
		bank.purchaseStock(0, "userName0", "password0", "A", 5);
		bank.purchaseStock(0, "userName0", "password0", "B", 5);
		bank.purchaseStock(0, "userName0", "password0", "C", 5);
		bank.sellStock(0, "userName0", "password0", "A", 5);
		assertArrayEquals(new String [] {"B", "C"}, bank.getListOfAllStockTickerSymbols());
		bank.purchaseStock(0, "userName0", "password0", "A", 5);
		assertArrayEquals(new String [] {"A", "B", "C", null}, bank.getListOfAllStockTickerSymbols());
		bank.sellStock(0, "userName0", "password0", "B", 5);
		assertArrayEquals(new String [] {"A", "C"}, bank.getListOfAllStockTickerSymbols());
		bank.purchaseStock(0, "userName0", "password0", "B", 1);
		bank.purchaseStock(0, "userName0", "password0", "D", 1);
		bank.purchaseStock(0, "userName0", "password0", "E", 1);
		assertArrayEquals(new String [] {"A", "B", "C", "D", "E", null, null, null}, bank.getListOfAllStockTickerSymbols());
	}

	@Test (expected = InsufficientAssetsException.class)
	public void testSellStockException() throws AuthenticationException, UnauthorizedActionException, InsufficientAssetsException {
		bank.depositCashIntoSavings(0, "userName0", "password0", 100);
		bank.transferFromSavingsToBrokerage(0, "userName0", "password0", 50);
		bank.purchaseStock(0, "userName0", "password0", "A", 1);
		bank.sellStock(0, "userName0", "password0", "A", 6);
	}

	@Test
	public void testGetNumberOfOutstandingShares() throws AuthenticationException, UnauthorizedActionException, InsufficientAssetsException {
		bank.depositCashIntoSavings(0, "userName0", "password0", 100);
		bank.transferFromSavingsToBrokerage(0, "userName0", "password0", 100);
		bank.depositCashIntoSavings(2, "userName2", "password2", 100);
		bank.transferFromSavingsToBrokerage(2, "userName2", "password2", 100);
		bank.purchaseStock(0, "userName0", "password0", "A", 5);
		bank.purchaseStock(0, "userName0", "password0", "C", 1);
		bank.purchaseStock(2, "userName2", "password2", "A", 50);
		bank.purchaseStock(2, "userName2", "password2", "B", 1);
		assertEquals(55, bank.getNumberOfOutstandingShares("A"));
	}

	@Test
	public void testMarketCapitalization() throws AuthenticationException, UnauthorizedActionException, InsufficientAssetsException {
		bank.depositCashIntoSavings(0, "userName0", "password0", 100);
		bank.transferFromSavingsToBrokerage(0, "userName0", "password0", 100);
		bank.depositCashIntoSavings(2, "userName2", "password2", 100);
		bank.transferFromSavingsToBrokerage(2, "userName2", "password2", 100);
		bank.purchaseStock(0, "userName0", "password0", "A", 5);
		bank.purchaseStock(0, "userName0", "password0", "B", 1);
		bank.purchaseStock(2, "userName2", "password2", "A", 50);
		bank.purchaseStock(2, "userName2", "password2", "B", 1);
		assertEquals(4, bank.getMarketCapitalization("B"));
	}

	@Test
	public void testGetTotalSavingsInBank() throws AuthenticationException, UnauthorizedActionException, InsufficientAssetsException {
		bank.depositCashIntoSavings(0, "userName0", "password0", 100);
		bank.transferFromSavingsToBrokerage(0, "userName0", "password0", 75);
		bank.depositCashIntoSavings(2, "userName2", "password2", 100);
		bank.transferFromSavingsToBrokerage(2, "userName2", "password2", 100);
		bank.purchaseStock(0, "userName0", "password0", "A", 5);
		bank.purchaseStock(0, "userName0", "password0", "B", 1);
		bank.purchaseStock(2, "userName2", "password2", "A", 50);
		bank.purchaseStock(2, "userName2", "password2", "B", 1);
		assertEquals(141, bank.getTotalSavingsInBank(), .05);
		assertEquals(116, bank.getTotalBrokerageCashInBank(), .05);
		assertEquals(100, bank.getNetWorth(0, "userName0", "password0"), .05);
	}








}
