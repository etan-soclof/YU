package edu.yu.cs.intro.simpleBank;
import edu.yu.cs.intro.simpleBank.exceptions.*;
import edu.yu.cs.intro.simpleBank.*;
import org.junit.*;
import static org.junit.Assert.*;

public class BankTest {

	public Bank bank = new Bank(1);

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
		bank.openBrokerageAccount(1, "userName1", "password1");

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
		bank.depositCashIntoSavings(1, "userName1", "password1", 2);
		assertEquals(4, bank.checkBalanceSavings(0, "userName0", "password0"), .05);
		assertEquals(1, bank.checkBalanceSavings(1, "userName1", "password1"), .05);
		assertEquals(5, bank.getTotalSavingsInBank(), .05);
	}


	@Test
	public void testWithdrawMethods() throws AuthenticationException, UnauthorizedActionException, InsufficientAssetsException {
		//Test Withdraw from savings
		bank.depositCashIntoSavings(0, "userName0", "password0", 5);
		assertEquals(4, bank.checkBalanceSavings(0, "userName0", "password0"), .05);
		bank.withdrawCashFromSavings(0, "userName0", "password0", 3);
		assertEquals(0, bank.checkBalanceSavings(0, "userName0", "password0"), .05);
		
	}

	@Test
	public void testStockTickerSymbolsAndPrice() {
		Object [] array = bank.getAllStockTickerSymbols().toArray();
		assertArrayEquals(new String [] {"A", "B", "C", "D", "E"}, array);
		assertEquals(1, bank.getStockPrice("A"), .05);
		assertEquals(2, bank.getStockPrice("B"), .05);
	}

	@Test
	public void testGetNumberOfOutstandingShares() throws AuthenticationException, UnauthorizedActionException, InsufficientAssetsException {
		bank.depositCashIntoBrokerage(0, "userName0", "password0", 100);
		bank.purchaseStock(0, "userName0", "password0", "A", 5);
		bank.depositCashIntoBrokerage(1, "userName1", "password1", 100);
		bank.purchaseStock(1, "userName1", "password1", "A", 5);
		assertEquals(10, bank.getNumberOfOutstandingShares("A"));
		assertEquals(10, bank.getMarketCapitalization("A"), .05);
		assertEquals(186, bank.getTotalBrokerageCashInBank(), .05);
		bank.withdrawCashFromBrokerage(0, "userName0", "password0", 92);
		assertEquals(0, bank.checkCashInBrokerage(0, "userName0", "password0"), .01);
		assertEquals(98, bank.checkTotalBalanceBrokerage(1, "userName1", "password1"), .01);
	}

	@Test
	public void testSellStock() throws AuthenticationException, UnauthorizedActionException, InsufficientAssetsException {
		bank.depositCashIntoBrokerage(0, "userName0", "password0", 100);
		bank.purchaseStock(0, "userName0", "password0", "A", 5);
		bank.sellStock(0, "userName0", "password0", "A", 4);
		assertEquals(1, bank.getNumberOfOutstandingShares("A"));
	}

	@Test
	public void testGetNetWorth() throws AuthenticationException, UnauthorizedActionException, InsufficientAssetsException {
		bank.depositCashIntoBrokerage(0, "userName0", "password0", 100);
		bank.purchaseStock(0, "userName0", "password0", "A", 5);
		bank.purchaseStock(0, "userName0", "password0", "B", 5);
		bank.depositCashIntoSavings(0, "userName0", "password0", 100);
		assertEquals(196, bank.getNetWorth(0, "userName0", "password0"), .01);
	}

	@Test
	public void testGetTransactionHistory() throws AuthenticationException, UnauthorizedActionException, InsufficientAssetsException {
		bank.depositCashIntoBrokerage(0, "userName0", "password0", 100);
		bank.purchaseStock(0, "userName0", "password0", "A", 5);
		bank.purchaseStock(0, "userName0", "password0", "B", 5);
		bank.sellStock(0, "userName0", "password0", "A", 4);
		bank.depositCashIntoSavings(0, "userName0", "password0", 5);
		bank.withdrawCashFromSavings(0, "userName0", "password0", 3);
		Transaction [] transactions = bank.getTransactionHistory(0, "userName0", "password0");

		/*
		int deposit = 0;
		int withdrawal = 0;
		int sale = 0;
		int buy = 0;
		for (int i = 0; i < transactions.length; i++){
			if (transactions[i].TRANSACTION_TYPE == DEPOSIT){
				deposit++;
			}
			if (transactions[i].TRANSACTION_TYPE == WITHDRAWAL){
				withdrawal++;
			}
			if (transactions[i].TRANSACTION_TYPE == BUYSTOCK){
				buy++;
			}
			if (transactions[i].TRANSACTION_TYPE == SELLSTOCK){
				sale++;
			}
		}
		*/
		assertEquals(6, transactions.length);
		/*
		assertEquals(1, withdrawal);
		assertEquals(2, buy);
		assertEquals(1, sale);
		*/
	}

/*
	@Test
	public void testBuyStocks() throws AuthenticationException, UnauthorizedActionException, InsufficientAssetsException {
		bank.depositCashIntoBrokerage(0, "userName0", "password0", 100);
		bank.purchaseStock(0, "userName0", "password0", "A", 5);
		bank.purchaseStock(0, "userName0", "password0", "B", 5);
		bank.purchaseStock(0, "userName0", "password0", "C", 5);
		assertArrayEquals(new String [] {"A", "B", "C", null}, bank.getListOfAllStockTickerSymbols());
	}

/*
	@Test (expected = InsufficientAssetsException.class)
	public void testSellStockException() throws AuthenticationException, UnauthorizedActionException, InsufficientAssetsException {
		bank.depositCashIntoBrokerage(0, "userName0", "password0", 100);
		bank.purchaseStock(0, "userName0", "password0", "A", 1);
		bank.sellStock(0, "userName0", "password0", "A", 6);
	}


	@Test
	public void testGetTotalSavingsInBank() throws AuthenticationException, UnauthorizedActionException, InsufficientAssetsException {
		bank.depositCashIntoBrokerage(0, "userName0", "password0", 100);
		bank.depositCashIntoBrokerage(2, "userName2", "password2", 100);
		bank.purchaseStock(0, "userName0", "password0", "A", 5);
		bank.purchaseStock(0, "userName0", "password0", "B", 1);
		bank.purchaseStock(2, "userName2", "password2", "A", 50);
		bank.purchaseStock(2, "userName2", "password2", "B", 1);
		assertEquals(141, bank.getTotalSavingsInBank(), .05);
		assertEquals(116, bank.getTotalBrokerageCashInBank(), .05);
		assertEquals(100, bank.getNetWorth(0, "userName0", "password0"), .05);
	}



*/




}
