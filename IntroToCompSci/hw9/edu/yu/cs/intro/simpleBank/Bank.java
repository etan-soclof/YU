package edu.yu.cs.intro.simpleBank;

import edu.yu.cs.intro.simpleBank.exceptions.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.*;

public class Bank {

    private Set<Patron> bankPatrons;
    private Map<String,Double> stocksSymbolToPrice;
    private long numberOfAccounts = 0;
    /**
     * transaction history is no longer stored in each patron object. Instead, the bank maintains a Map of transactions, mapping each Patron to the List of transactions that the given patron has executed.
     */
    private Map<Patron,List<Transaction>> txHistoryByPatron;
    protected final double transactionFee;

    private static Bank INSTANCE;

    public Bank(double transactionFee){
        this.transactionFee = transactionFee;
        INSTANCE = this;
        bankPatrons = new HashSet<>();
        stocksSymbolToPrice = new HashMap<>();
        txHistoryByPatron = new HashMap<>();
    }

    public static Bank getBank(){
        return INSTANCE;
    }

    public void clear(){
    this.bankPatrons.clear();
    this.numberOfAccounts = 0;
    this.stocksSymbolToPrice.clear();
    this.txHistoryByPatron.clear();
    }

    /**
     * Lists a new stock with the given symbol at the given price
     * @return false if the stock was previously listed, true if it was added as a result of this call
     */
    protected boolean addNewStockToMarket(String tickerSymbol, double sharePrice){
        String word = tickerSymbol.toLowerCase();
        if (this.stocksSymbolToPrice.containsKey(word)){
            return false;
        }
        this.stocksSymbolToPrice.put(tickerSymbol, sharePrice);
        //if the stock is already listed, return false
        //otherwise, add the key-value pair to the stocksSymbolToPrice map and return true;
        return true;
    }

    /**
     * @return the stock price for the given stock ticker symbol. Return 0 if there is no such stock.
     */
    public double getStockPrice(String symbol){
        if (this.stocksSymbolToPrice.get(symbol) == null){
            return 0;
        }
        return this.stocksSymbolToPrice.get(symbol);
    }

    /**
     * @return a set of the stock ticker symbols listed in this bank
     */
    public Set<String> getAllStockTickerSymbols(){
        return stocksSymbolToPrice.keySet();
    }

    /**
     * @return the total number of shares of the given stock owned by all patrons combined
     * if there is no such Stock or if the tickerSymbol is empty or null, return 0
     */
    public int getNumberOfOutstandingShares(String tickerSymbol){
        int total = 0;
        if (this.stocksSymbolToPrice.get(tickerSymbol) == null){
            return 0;
        }
        for (Patron patron: bankPatrons){
            if (patron.getBrokerageAccount() != null){
                total += patron.getBrokerageAccount().getNumberOfShares(tickerSymbol);
            }
        }
        return total;
    }

    /**
     * @return the total number of shares of the given stock owned by all patrons combined multiplied by the price per share
     * if there is no such Stock or if the tickerSymbol is empty or null, return 0
     */
    public int getMarketCapitalization(String tickerSymbol){
        double total = this.stocksSymbolToPrice.get(tickerSymbol);
        return (int)(this.getNumberOfOutstandingShares(tickerSymbol)*total);
    }

    /**
     * @return all the cash in all savings accounts added up
     */
    public double getTotalSavingsInBank(){
        int total = 0;
        for (Patron patron: bankPatrons){
            if (patron.getSavingsAccount() != null){
                total += patron.getSavingsAccount().getAvailableBalance();
            }
        }
        return total;
    }

    /**
     * @return all the cash in all brokerage accounts added up
     */
    public double getTotalBrokerageCashInBank(){
        int total = 0;
        for (Patron patron: bankPatrons){
            if (patron.getBrokerageAccount() != null){
                total += patron.getBrokerageAccount().getAvailableBalance();
            }
        }
        return total;
    }

    /**
     * Creates a new Patron in the bank.
     */
    public void createNewPatron(String firstName, String lastName, long socialSecurityNumber, String userName, String password){
        Patron patron = new Patron (firstName, lastName, socialSecurityNumber, userName, password);
        this.bankPatrons.add(patron);
    }

    /**
     * @return the account number of the opened account
     */
    public long openSavingsAccount(long socialSecurityNumber, String userName, String password) {
        for (Patron patron: bankPatrons){
            if (patron.getSocialSecurityNumber() == socialSecurityNumber && patron.getUserName().equals(userName) && patron.getPassword().equals(password)){
                patron.setSavingsAccount(new Account(numberOfAccounts));
                numberOfAccounts++;
                break;
            }
        }
        return numberOfAccounts--;
    }

    /**
     * @return the account number of the opened account
     */
    public long openBrokerageAccount(long socialSecurityNumber, String userName, String password) {
        for (Patron patron: bankPatrons){
            if (patron.getSocialSecurityNumber() == socialSecurityNumber && patron.getUserName().equals(userName) && patron.getPassword().equals(password)){
                patron.setBrokerageAccount(new BrokerageAccount(numberOfAccounts));
                numberOfAccounts++;
                break;
            }
        }
        return numberOfAccounts--;
    }

    /**
     * Deposit cash into the given savings account
     */
    public void depositCashIntoSavings(long socialSecurityNumber, String userName, String password, double amount){
        for (Patron patron: bankPatrons){
            if (patron.getSocialSecurityNumber() == socialSecurityNumber && patron.getUserName().equals(userName) && patron.getPassword().equals(password)){
                try {
                    Transaction transaction = new Transaction (patron, Transaction.TRANSACTION_TYPE.DEPOSIT, patron.getSavingsAccount(), amount);
                    transaction.execute();
                    if (txHistoryByPatron.get(patron) == null){
                        List <Transaction> transactions = new ArrayList <Transaction>();
                        transactions.add(transaction);
                        txHistoryByPatron.put(patron, transactions); 
                    }
                    else {
                       txHistoryByPatron.get(patron).add(transaction);
                    }
                }
                catch (UnauthorizedActionException e){
                    System.out.println("never gonna happen");
                }
                catch (InsufficientAssetsException e){
                    System.out.println("never gonna happen");
                }

            }
        }
    }

    protected void depositCashIntoBrokerage(long socialSecurityNumber, String userName, String password, double amount){
        for (Patron patron: bankPatrons){
            if (patron.getSocialSecurityNumber() == socialSecurityNumber && patron.getUserName().equals(userName) && patron.getPassword().equals(password)){
                try {
                    Transaction transaction = new Transaction (patron, Transaction.TRANSACTION_TYPE.DEPOSIT, patron.getBrokerageAccount(), amount);
                    transaction.execute();
                    if (txHistoryByPatron.get(patron) == null){
                        List <Transaction> transactions = new ArrayList <Transaction>();
                        transactions.add(transaction);
                        txHistoryByPatron.put(patron, transactions); 
                    }
                    else {
                       txHistoryByPatron.get(patron).add(transaction);
                    }
                }
                catch (UnauthorizedActionException e){
                    System.out.println("never gonna happen");
                }
                catch (InsufficientAssetsException e){
                    System.out.println("never gonna happen");
                }

            }
        }
    }

    /**
     * withdraw cash from the patron's savings account
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a savings account
     * throw InsufficientAssetsException if that amount of money is not present the savings account
     */
    public void withdrawCashFromSavings(long socialSecurityNumber, String userName, String password, double amount) throws AuthenticationException,UnauthorizedActionException,InsufficientAssetsException{
        Patron patron = null;
        boolean hasPatron = false;
        for (Patron patron1: bankPatrons){
            if (patron1.getSocialSecurityNumber() == socialSecurityNumber && patron1.getUserName().equals(userName) && patron1.getPassword().equals(password)){
                patron = patron1;
                hasPatron = true;
            }
        }
        if (hasPatron){
            if (patron.getSavingsAccount() == null){
                throw new UnauthorizedActionException ("Does not have savings account");
            }
            else {
                Transaction transaction = new Transaction (patron, Transaction.TRANSACTION_TYPE.WITHDRAW, patron.getSavingsAccount(), amount);
                transaction.execute();
                if (txHistoryByPatron.get(patron) == null){
                    List <Transaction> transactions = new ArrayList <Transaction>();
                    transactions.add(transaction);
                    txHistoryByPatron.put(patron, transactions); 
                }
                else {
                    txHistoryByPatron.get(patron).add(transaction);
                }
            }
        }
        else {
            throw new AuthenticationException ("Incorrect info");
        }
    }
    /**
     * withdraw cash from the patron's brokerage account
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a brokerage account
     * throw InsufficientAssetsException if that amount of CASH is not present the brokerage account
     */
    public void withdrawCashFromBrokerage(long socialSecurityNumber, String userName, String password, double amount) throws AuthenticationException,UnauthorizedActionException,InsufficientAssetsException{
        Patron patron = null;
        boolean hasPatron = false;
        for (Patron patron1: bankPatrons){
            if (patron1.getSocialSecurityNumber() == socialSecurityNumber && patron1.getUserName().equals(userName) && patron1.getPassword().equals(password)){
                patron = patron1;
                hasPatron = true;
            }
        }
        if (hasPatron){
            if (patron.getBrokerageAccount() == null){
                throw new UnauthorizedActionException ("Does not have a brokerage account");
            }
            else {
                Transaction transaction = new Transaction (patron, Transaction.TRANSACTION_TYPE.WITHDRAW, patron.getBrokerageAccount(), amount);
                transaction.execute();
                if (txHistoryByPatron.get(patron) == null){
                    List <Transaction> transactions = new ArrayList <Transaction>();
                    transactions.add(transaction);
                    txHistoryByPatron.put(patron, transactions); 
                }
                else {
                    txHistoryByPatron.get(patron).add(transaction);
                }
            }
        }
        else {
            throw new AuthenticationException ("Incorrect info");
        }
    }

    /**
     * check how much cash the patron has in his brokerage account
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a brokerage account
     */
    public double checkCashInBrokerage(long socialSecurityNumber, String userName, String password) throws AuthenticationException,UnauthorizedActionException{
        Patron patron = null;
        boolean hasPatron = false;
        for (Patron patron1: bankPatrons){
            if (patron1.getSocialSecurityNumber() == socialSecurityNumber && patron1.getUserName().equals(userName) && patron1.getPassword().equals(password)){
                patron = patron1;
                hasPatron = true;
            }
        }

        if (!hasPatron){
            throw new AuthenticationException ("Incorrect info");
        }
        if (patron.getBrokerageAccount() == null){
            throw new UnauthorizedActionException ("Does not have a brokerage account");
        }

        return patron.getBrokerageAccount().getAvailableBalance();
    }
    /**
     * check the total value of the patron's brokerage account
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a brokerage account
     */
    public double checkTotalBalanceBrokerage(long socialSecurityNumber, String userName, String password) throws AuthenticationException,UnauthorizedActionException{
        Patron patron = null;
        boolean hasPatron = false;
        for (Patron patron1: bankPatrons){
            if (patron1.getSocialSecurityNumber() == socialSecurityNumber && patron1.getUserName().equals(userName) && patron1.getPassword().equals(password)){
                patron = patron1;
                hasPatron = true;
            }
        }

        if (!hasPatron){
            throw new AuthenticationException ("Incorrect info");
        }
        if (patron.getBrokerageAccount() == null){
            throw new UnauthorizedActionException ("Does not have a brokerage account");
        }
        
        return patron.getBrokerageAccount().getTotalBalance();
    }
    /**
     * check how much cash the patron has in his savings account
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a savings account
     */
    public double checkBalanceSavings(long socialSecurityNumber, String userName, String password) throws AuthenticationException,UnauthorizedActionException{
        Patron patron = null;
        boolean hasPatron = false;
        for (Patron patron1: bankPatrons){
            if (patron1.getSocialSecurityNumber() == socialSecurityNumber && patron1.getUserName().equals(userName) && patron1.getPassword().equals(password)){
                patron = patron1;
                hasPatron = true;
            }
        }

        if (!hasPatron){
            throw new AuthenticationException ("Incorrect info");
        }
        if (patron.getSavingsAccount() == null){
            throw new UnauthorizedActionException ("Does not have a brokerage account");
        }

        return patron.getSavingsAccount().getAvailableBalance();
    }

    /**
     * buy shares of the given stock
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a brokerage account
     * throw InsufficientAssetsException if the required amount of CASH is not present in the brokerage account
     */
    public void purchaseStock(long socialSecurityNumber, String userName, String password, String tickerSymbol, int shares) throws AuthenticationException,UnauthorizedActionException,InsufficientAssetsException{
        Patron patron = null;
        boolean hasPatron = false;
        for (Patron patron1: bankPatrons){
            if (patron1.getSocialSecurityNumber() == socialSecurityNumber && patron1.getUserName().equals(userName) && patron1.getPassword().equals(password)){
                patron = patron1;
                hasPatron = true;
            }
        }

        if (!hasPatron){
            throw new AuthenticationException ("Incorrect info");
        }
        if (patron.getBrokerageAccount() == null){
            throw new UnauthorizedActionException ("Does not have a brokerage account");
        }

        Transaction transaction = new Transaction (patron, Transaction.TRANSACTION_TYPE.BUYSTOCK, patron.getBrokerageAccount(), shares);
        transaction.setStockSymbol(tickerSymbol);
        transaction.execute();
        if (txHistoryByPatron.get(patron) == null){
            List <Transaction> transactions = new ArrayList <Transaction>();
            transactions.add(transaction);
            txHistoryByPatron.put(patron, transactions); 
        }
        else {
            txHistoryByPatron.get(patron).add(transaction);
        }
    }

    /**
     * sell shares of the given stock
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a brokerage account
     * throw InsufficientAssetsException if the patron does not have the given number of shares of the given stock
     */
    public void sellStock(long socialSecurityNumber, String userName, String password, String tickerSymbol, int shares) throws AuthenticationException,UnauthorizedActionException,InsufficientAssetsException{
        Patron patron = null;
        boolean hasPatron = false;
        for (Patron patron1: bankPatrons){
            if (patron1.getSocialSecurityNumber() == socialSecurityNumber && patron1.getUserName().equals(userName) && patron1.getPassword().equals(password)){
                patron = patron1;
                hasPatron = true;
            }
        }

        if (!hasPatron){
            throw new AuthenticationException ("Incorrect info");
        }
        if (patron.getBrokerageAccount() == null){
            throw new UnauthorizedActionException ("Does not have a brokerage account");
        }

        Transaction transaction = new Transaction (patron, Transaction.TRANSACTION_TYPE.SELLSTOCK, patron.getBrokerageAccount(), shares);
        transaction.setStockSymbol(tickerSymbol);
        transaction.execute();
        if (txHistoryByPatron.get(patron) == null){
            List <Transaction> transactions = new ArrayList <Transaction>();
            transactions.add(transaction);
            txHistoryByPatron.put(patron, transactions); 
        }
        else {
            txHistoryByPatron.get(patron).add(transaction);
        }
    }

    /**
     * check the net worth of the patron
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * return 0 if the patron doesn't have any accounts
     */
    public double getNetWorth(long socialSecurityNumber, String userName, String password) throws AuthenticationException{
        Patron patron = null;
        boolean hasPatron = false;
        for (Patron patron1: bankPatrons){
            if (patron1.getSocialSecurityNumber() == socialSecurityNumber && patron1.getUserName().equals(userName) && patron1.getPassword().equals(password)){
                patron = patron1;
                hasPatron = true;
            }
        }

        if (!hasPatron){
            throw new AuthenticationException ("Incorrect info");
        }
        return patron.getNetWorth();
    }

        /**
     * transfer cash from the patron's savings account to his brokerage account
     * throws AuthenticationException if the SS#, username, and password don't match a bank patron
     * throws UnauthorizedActionException if the given patron does not have both a savings account and a brokerage account
     * throws InsufficientAssetsException if that amount of money is not present in the savings account
     */
    public void transferFromSavingsToBrokerage(long socialSecurityNumber, String userName, String password, double amount) throws AuthenticationException,UnauthorizedActionException, InsufficientAssetsException {
        Patron patron = null;
        boolean hasPatron = false;
        for (Patron patron1: bankPatrons){
            if (patron1.getSocialSecurityNumber() == socialSecurityNumber && patron1.getUserName().equals(userName) && patron1.getPassword().equals(password)){
                patron = patron1;
                hasPatron = true;
            }
        }
        if (!hasPatron){
            throw new AuthenticationException ("Incorrect info");
        }

        if (patron.getSavingsAccount() == null || patron.getBrokerageAccount() == null){
            throw new UnauthorizedActionException ("No accounts");
        }

        Transaction withdraw = new Transaction (patron, Transaction.TRANSACTION_TYPE.WITHDRAW, patron.getSavingsAccount(), amount);
        withdraw.execute();
        if (txHistoryByPatron.get(patron) == null){
            List <Transaction> transactions = new ArrayList <Transaction>();
            transactions.add(withdraw);
            txHistoryByPatron.put(patron, transactions); 
            }
        else {
            txHistoryByPatron.get(patron).add(withdraw);
        }

        Transaction deposit = new Transaction (patron, Transaction.TRANSACTION_TYPE.DEPOSIT, patron.getBrokerageAccount(), amount);
        deposit.execute();
        txHistoryByPatron.get(patron).add(deposit);
    }

    /**
     * transfer cash from the patron's savings account to his brokerage account
     * throws AuthenticationException if the SS#, username, and password don't match a bank patron
     * throws UnauthorizedActionException if the given patron does not have both a savings account and a brokerage account
     * throws InsufficientAssetsException if that amount of money is not present in CASH in the brokerage account
     */
    public void transferFromBrokerageToSavings(long socialSecurityNumber, String userName, String password, double amount) throws AuthenticationException,UnauthorizedActionException, InsufficientAssetsException{
        Patron patron = null;
        boolean hasPatron = false;
        for (Patron patron1: bankPatrons){
            if (patron1.getSocialSecurityNumber() == socialSecurityNumber && patron1.getUserName().equals(userName) && patron1.getPassword().equals(password)){
                patron = patron1;
                hasPatron = true;
            }
        }
        if (!hasPatron){
            throw new AuthenticationException ("Incorrect info");
        }

        if (patron.getSavingsAccount() == null || patron.getBrokerageAccount() == null){
            throw new UnauthorizedActionException ("No accounts");
        }

        Transaction withdraw = new Transaction (patron, Transaction.TRANSACTION_TYPE.WITHDRAW, patron.getBrokerageAccount(), amount);
        withdraw.execute();
        if (txHistoryByPatron.get(patron) == null){
            List <Transaction> transactions = new ArrayList <Transaction>();
            transactions.add(withdraw);
            txHistoryByPatron.put(patron, transactions); 
            }
        else {
            txHistoryByPatron.get(patron).add(withdraw);
        }

        Transaction deposit = new Transaction (patron, Transaction.TRANSACTION_TYPE.DEPOSIT, patron.getSavingsAccount(), amount);
        deposit.execute();
        txHistoryByPatron.get(patron).add(deposit);
    }

    /**
     * Get the transaction history on all of the patron's accounts, i.e. the transaction histories of both the savings account and
     * brokerage account (whichever of the two exist), combined. The merged history should be sorted in time order, from oldest to newest.
     * If the patron has no transactions in his history, return an array of length 0.
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     */
    public Transaction[] getTransactionHistory(long socialSecurityNumber, String userName, String password) throws AuthenticationException{
        Patron patron = null;
        boolean hasPatron = false;
        for (Patron patron1: bankPatrons){
            if (patron1.getSocialSecurityNumber() == socialSecurityNumber && patron1.getUserName().equals(userName) && patron1.getPassword().equals(password)){
                patron = patron1;
                hasPatron = true;
            }
        }

        if (!hasPatron){
            throw new AuthenticationException ("Incorrect info");
        }

        if (txHistoryByPatron.get(patron) == null || txHistoryByPatron.get(patron).size() == 0){
            return new Transaction [0];
        }
        Transaction [] transactionHistory = (Transaction []) txHistoryByPatron.get(patron).toArray(new Transaction [txHistoryByPatron.size()]);
        for (int i = 0; i < transactionHistory.length; i++){
            Transaction min = transactionHistory[i];
            Transaction ph = transactionHistory[i];
            int indexOfMin = i;
            for (int h = i; h < transactionHistory.length; h++){
                if (min.getTime() > transactionHistory[h].getTime()){
                    min = transactionHistory[h];
                    indexOfMin = h;
                }
            }
            ph = transactionHistory[i];
            transactionHistory[i] = min;
            transactionHistory[indexOfMin] = ph;
        }
        return transactionHistory;
    }
}


