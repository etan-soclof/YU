package edu.yu.cs.intro.bank;

import edu.yu.cs.intro.bank.exceptions.*;
/*
public class Bank {
    private static final Bank INSTANCE = new Bank();

    private Bank(){
    }
*/

public class Bank {
    public static Bank INSTANCE = new Bank();

    public Bank(){
    }

    public static Bank getBank() {
        return INSTANCE;
    }

    private Patron[] bankPatrons = new Patron[1];
    private int numberOfPatrons = 0;
    private Account[] bankAccounts = new Account[1];
    private int numberOfAccounts = 0;
    private Stock[] stocksOnMarket = new Stock[1];
    private int numberOfStocks = 0;


    public static void main (String [] args) throws AuthenticationException, UnauthorizedActionException, InsufficientAssetsException {
        Bank bank = Bank.getBank();
        bank.addNewStockToMarket("A", 1);
        bank.addNewStockToMarket("B", 2);
        bank.addNewStockToMarket("C", 3);
        bank.addNewStockToMarket("D", 4);
        bank.addNewStockToMarket("E", 5);

        bank.createNewPatron("firstName0", "lastName0", 0, "userName0", "password0");
        bank.openSavingsAccount(0, "userName0", "password0");
        bank.openBrokerageAccount(0, "userName0", "password0");
        bank.depositCashIntoSavings(0, "userName0", "password0", 100);
        bank.transferFromSavingsToBrokerage(0, "userName0", "password0", 50);
        bank.purchaseStock(0, "userName0", "password0", "A", 5);
        bank.purchaseStock(0, "userName0", "password0", "B", 5);
        bank.purchaseStock(0, "userName0", "password0", "C", 5);
        Transaction [] transactions = bank.getTransactionHistory(0, "userName0", "password0");
        for (Transaction transaction : transactions){
            System.out.println(transaction.getTime());
        }
    }

    public void clear(){
    this.bankPatrons = new Patron[1];
    this.numberOfPatrons = 0;
    this.bankAccounts = new Account[1];
    this.numberOfAccounts = 0;
    this.stocksOnMarket = new Stock[1];
    this.numberOfStocks = 0;
    }
    /**
     * lists a new stock with the given symbol at the given price
     */
    public void addNewStockToMarket(String tickerSymbol, double sharePrice){
        Bank.Stock stock = new Bank.Stock (tickerSymbol, sharePrice);
        if (this.numberOfStocks == this.stocksOnMarket.length){
            this.stocksOnMarket = this.stockArrayLengthDoubler(this.stocksOnMarket);
        }
        stocksOnMarket[numberOfStocks] = stock;
        numberOfStocks++;
    }

    protected Bank.Stock [] stockArrayLengthDoubler(Bank.Stock[] array) {
        Bank.Stock [] copy = new Bank.Stock [(array.length)*2];
        for (int i = 0; i < array.length; i++){
            copy [i] = array [i];
        }
        return copy;
    }
    /**
     * return the stock object for the given stock ticker symbol
     */
    public Stock getStockBySymbol(String symbol){
        for (int i = 0; i < stocksOnMarket.length; i++){
            if (symbol.equals(this.stocksOnMarket[i].tickerSymbol)){
                return this.stocksOnMarket[i];
            }
        }
        return null;
    }
    
    /**
     * @return an array of all the stock ticker symbols owned by the patrons of this bank
     */
    public String[] getListOfAllStockTickerSymbols(){
      /*  int numberOfSymbols = 0;
        String [] symbolArray = new String [1];
        Bank.Stock [] stockArray = new Bank.Stock[1];

        for (int x = 0; x < this.numberOfStocks; x ++){
            for (int i = 0; i < this.numberOfAccounts; i++){
                if (bankAccounts[i] instanceof BrokerageAccount){
                    BrokerageAccount brokerageAccount = (BrokerageAccount)bankAccounts[i];
                    if (brokerageAccount != null){
                        if (brokerageAccount.getNumberOfShares(stocksOnMarket[x]) > 0){
                            if (numberOfSymbols == symbolArray.length){
                                symbolArray = this.stringArrayLengthDoubler(symbolArray);
                            }
                        symbolArray[numberOfSymbols] = stocksOnMarket[x].getTickerSymbol();
                        numberOfSymbols++;
                        break;
                        }
                    }
                }
            }       
        }*/

        String [] symbolArray = new String [this.numberOfStocks];
        for (int x = 0; x < this.numberOfStocks; x++){
            symbolArray[x] = stocksOnMarket[x].getTickerSymbol();
        }
        return symbolArray;
    }

    protected String [] stringArrayLengthDoubler(String[] array) {
        String [] copy = new String [(array.length)*2];
        for (int i = 0; i < array.length; i++){
            copy [i] = array [i];
        }
        return copy;
    }

    /**
     * @return the total number of shares of the given stock owned by all patrons combined
     * if there is no such Stock or if the tickerSymbol is empty or null, return 0
     */
    public int getNumberOfOutstandingShares(String tickerSymbol){
        boolean hasStock = false;
        Bank.Stock stock = stocksOnMarket[0];
        int total = 0;
        Bank.Stock [] stockArray = new Bank.Stock [1];
        BrokerageAccount brokerageAccount;
        for (int i = 0; i < numberOfStocks; i++){
            if (tickerSymbol.equals(stocksOnMarket[i].getTickerSymbol())){
                stock = stocksOnMarket[i];
                hasStock = true;
            }
        }
        if (!hasStock){
            return 0;
        }

        for (int i = 0; i < numberOfAccounts; i++){
            if (bankAccounts[i] instanceof BrokerageAccount){
                brokerageAccount = (BrokerageAccount) bankAccounts[i];
                total += brokerageAccount.getNumberOfShares(stock);
                }
            }
        if (hasStock){
            return total;
        }
        return 0;
    }

    /**
     * @return the total number of shares of the given stock owned by all patrons combined multiplied by the price per share
     * if there is no such Stock or if the tickerSymbol is empty or null, return 0
     */
    public int getMarketCapitalization(String tickerSymbol){
        int amountOfShares = this.getNumberOfOutstandingShares(tickerSymbol);
        double price = 0;
        if (amountOfShares == 0){
            return 0;
        }
        for (int i = 0; i < numberOfStocks; i++){
            if (tickerSymbol.equals(stocksOnMarket[i].getTickerSymbol())){
                price = stocksOnMarket[i].getSharePrice();
            }
        }
        return amountOfShares*(int) price;
    }

    /**
     * @return all the cash in all savings accounts added up
     */
    public double getTotalSavingsInBank(){
        int total = 0;
        for (int i =0; i < numberOfAccounts; i++){
            total += bankAccounts[i].getAvailableBalance();
        }
        return total;
    }

    /**
     * @return all the cash in all brokerage accounts added up
     */
    public double getTotalBrokerageCashInBank(){
        int total = 0;
        for (int i =0; i < numberOfAccounts; i++){
            if (bankAccounts[i] instanceof BrokerageAccount){
                total += bankAccounts[i].getAvailableBalance();
            }
        }
        return total;
    }

    /**
     * Creates a new Patron in the bank.
     * Throws an UnauthorizedActionException if a Patron already exists with that social security number.
     */
    protected void createNewPatron(String firstName, String lastName, long socialSecurityNumber, String userName, String password) throws UnauthorizedActionException {
        if (numberOfPatrons > 0){
            for (int i = 0; i < numberOfPatrons; i++){
                if (socialSecurityNumber == bankPatrons[i].getSocialSecurityNumber()){
                    throw new UnauthorizedActionException ("Patron already exists");
                }
            }
        }
        Patron patron = new Patron (firstName, lastName, socialSecurityNumber, userName, password);
        if (numberOfPatrons == bankPatrons.length){
            bankPatrons = this.patronArrayLengthDoubler(bankPatrons);
        }
        bankPatrons[numberOfPatrons] = patron;
        numberOfPatrons++;
    }

    protected Patron [] patronArrayLengthDoubler(Patron[] array) {
        Patron [] copy = new Patron [(array.length)*2];
        for (int i = 0; i < array.length; i++){
            copy [i] = array [i];
        }
        return copy;
    }

    /**
     * @throws AuthenticationException if the user name or password doesn't match for the patron with the given social security number
     * @throws UnauthorizedActionException if the user already has a savings account
     * @retuns the account number of the opened account
     */
    public long openSavingsAccount( long socialSecurityNumber, String userName, String password) throws AuthenticationException,UnauthorizedActionException{
        int indexOfPatron = -1;
        for (int i = 0; i < numberOfPatrons; i++){
            if (bankPatrons[i].getSocialSecurityNumber() == socialSecurityNumber){
                indexOfPatron = i;
            }
        }
        if (indexOfPatron>=0){
            if (bankPatrons[indexOfPatron].getUserName().equals(userName) && bankPatrons[indexOfPatron].getPassword().equals(password)){
                Patron patron = bankPatrons[indexOfPatron];
                if (patron.getSavingsAccount() == null){
                    SavingsAccount account = new SavingsAccount (numberOfAccounts, patron);
                    patron.addAccount(account);
                    if (numberOfAccounts == bankAccounts.length){
                        bankAccounts = this.accountArrayLengthDoubler(bankAccounts);
                    }
                    bankAccounts[numberOfAccounts] = account;
                    numberOfAccounts++;
                }
                else {
                    throw new UnauthorizedActionException ("already has a savings account");
                }
            }
            else {
                throw new AuthenticationException ("username and password do not match");
            }
        }
        
        return numberOfAccounts - 1;
    }

    protected Account [] accountArrayLengthDoubler(Account[] array) {
        Account [] copy = new Account [(array.length)*2];
        for (int i = 0; i < array.length; i++){
            copy [i] = array [i];
        }
        return copy;
    }

    /**
     * @throws AuthenticationException if the user name or password doesn't match for the patron with the given social security number
     * @throws UnauthorizedActionException if the user already has a Brokerage account
     * @retuns the account number of the opened account
     */
    public long openBrokerageAccount(long socialSecurityNumber, String userName, String password) throws AuthenticationException,UnauthorizedActionException{

        int indexOfPatron = -1;
        for (int i = 0; i < numberOfPatrons; i++){
            if (bankPatrons[i].getSocialSecurityNumber() == socialSecurityNumber){
                indexOfPatron = i;
            }
        }
        if (indexOfPatron>=0){
            if (bankPatrons[indexOfPatron].getUserName().equals(userName) && bankPatrons[indexOfPatron].getPassword().equals(password)){
                Patron patron = bankPatrons[indexOfPatron];
                if (patron.getBrokerageAccount() == null){
                    BrokerageAccount account = new BrokerageAccount (numberOfAccounts, patron);
                    patron.addAccount(account);
                    if (numberOfAccounts == bankAccounts.length){
                        bankAccounts = this.accountArrayLengthDoubler(bankAccounts);
                    }
                    bankAccounts[numberOfAccounts] = account;
                    numberOfAccounts++;
                }
                else {
                    throw new UnauthorizedActionException ("already has a brokerage account");
                }
            }
            else {
                throw new AuthenticationException ("username and password do not match");
            }
        }
        
        return numberOfAccounts - 1;
    }

    /**
     * Deposit cash into the given savings account
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a savings account
     */
    public void depositCashIntoSavings(long socialSecurityNumber, String userName, String password, double amount) throws AuthenticationException,UnauthorizedActionException{
        Patron patron = bankPatrons[0];
        boolean hasPatron = false;
        for (int i = 0; i < numberOfPatrons; i++){
            if ((socialSecurityNumber == bankPatrons[i].getSocialSecurityNumber()) && (bankPatrons[i].getUserName().equals(userName)) && (bankPatrons[i].getPassword().equals(password))){
                patron = bankPatrons[i];
                hasPatron = true;
                break;
            }
        }
        if (!hasPatron){
            throw new AuthenticationException ("username and password do not match bank patron");
        }
        if (patron.getSavingsAccount() == null){
            throw new UnauthorizedActionException ("Savings account does not exist");
        }
        try {
            Deposit deposit = new Deposit(amount, patron.getSavingsAccount(), patron);
            deposit.execute();  
        }
        catch (NoSuchAccountException e){
            System.out.println("No such account");
        }
        catch (InsufficientAssetsException e){
            System.out.println("Insufficient Assets");
        }
    }

    /**
     * transfer cash from the patron's savings account to his brokerage account
     * throws AuthenticationException if the SS#, username, and password don't match a bank patron
     * throws UnauthorizedActionException if the given patron does not have both a savings account and a brokerage account
     * throws InsufficientAssetsException if that amount of money is not present in the savings account
     */
    public void transferFromSavingsToBrokerage(long socialSecurityNumber, String userName, String password, double amount) throws AuthenticationException,UnauthorizedActionException, InsufficientAssetsException {
        Patron patron = bankPatrons[0];
        boolean hasPatron = false;
        for (int i = 0; i < numberOfPatrons; i++){
            if ((socialSecurityNumber == bankPatrons[i].getSocialSecurityNumber()) && (bankPatrons[i].getUserName().equals(userName)) && (bankPatrons[i].getPassword().equals(password))){
                patron = bankPatrons[i];
                hasPatron = true;
                break;
            }
        }
        if (!hasPatron){
            throw new AuthenticationException ("username and password do not match bank patron");
        }
        if ((patron.getSavingsAccount() == null) || (patron.getBrokerageAccount() == null)){
            throw new UnauthorizedActionException ("account does not exist");
        }
        
        try {
            Transfer transfer = new Transfer(amount, patron.getSavingsAccount(), patron.getBrokerageAccount(), patron);
            transfer.execute();
        }
        catch (NoSuchAccountException e){
            System.out.println("no such account");
        }
        

    }

    /**
     * transfer cash from the patron's savings account to his brokerage account
     * throws AuthenticationException if the SS#, username, and password don't match a bank patron
     * throws UnauthorizedActionException if the given patron does not have both a savings account and a brokerage account
     * throws InsufficientAssetsException if that amount of money is not present in CASH in the brokerage account
     */
    public void transferFromBrokerageToSavings(long socialSecurityNumber, String userName, String password, double amount) throws AuthenticationException,UnauthorizedActionException, InsufficientAssetsException{
        Patron patron = bankPatrons[0];
        boolean hasPatron = false;
        for (int i = 0; i < numberOfPatrons; i++){
            if ((socialSecurityNumber == bankPatrons[i].getSocialSecurityNumber()) && (bankPatrons[i].getUserName().equals(userName)) && (bankPatrons[i].getPassword().equals(password))){
                patron = bankPatrons[i];
                hasPatron = true;
                break;
            }
        }
        if (!hasPatron){
            throw new AuthenticationException ("username and password do not match bank patron");
        }
        if ((patron.getSavingsAccount() == null) || (patron.getBrokerageAccount() == null)){
            throw new UnauthorizedActionException ("account does not exist");
        }
        
        try {
            Transfer transfer = new Transfer(amount, patron.getBrokerageAccount(), patron.getSavingsAccount(), patron);
            transfer.execute();
        }
        catch (NoSuchAccountException e){
            System.out.println("no such account");
        }
        

    }

    /**
     * withdraw cash from the patron's savings account
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a savings account
     * throw InsufficientAssetsException if that amount of money is not present the savings account
     */
    public void withdrawCashFromSavings(long socialSecurityNumber, String userName, String password, double amount) throws AuthenticationException,UnauthorizedActionException,InsufficientAssetsException{
        Patron patron = bankPatrons[0];
        boolean hasPatron = false;
        for (int i = 0; i < numberOfPatrons; i++){
            if ((socialSecurityNumber == bankPatrons[i].getSocialSecurityNumber()) && (bankPatrons[i].getUserName().equals(userName)) && (bankPatrons[i].getPassword().equals(password))){
                patron = bankPatrons[i];
                hasPatron = true;
                break;
            }
        }
        if (!hasPatron){
            throw new AuthenticationException ("username and password do not match bank patron");
        }
        if (patron.getSavingsAccount() == null){
            throw new UnauthorizedActionException ("Savings account does not exist");
        }
        try{
            Withdrawal withdrawal = new Withdrawal (amount, patron.getSavingsAccount(), patron);
            withdrawal.execute();
        }
        catch (NoSuchAccountException e){
            System.out.println("no such account");
        }

    }
    /**
     * withdraw cash from the patron's brokerage account
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a brokerage account
     * throw InsufficientAssetsException if that amount of CASH is not present the brokerage account
     */
    public void withdrawCashFromBrokerage(long socialSecurityNumber, String userName, String password, double amount) throws AuthenticationException,UnauthorizedActionException,InsufficientAssetsException{
        Patron patron = bankPatrons[0];
        boolean hasPatron = false;
        for (int i = 0; i < numberOfPatrons; i++){
            if ((socialSecurityNumber == bankPatrons[i].getSocialSecurityNumber()) && (bankPatrons[i].getUserName().equals(userName)) && (bankPatrons[i].getPassword().equals(password))){
                patron = bankPatrons[i];
                hasPatron = true;
                break;
            }
        }
        if (!hasPatron){
            throw new AuthenticationException ("username and password do not match bank patron");
        }
        if (patron.getBrokerageAccount() == null){
            throw new UnauthorizedActionException ("account does not exist");
        }
        try{
            Withdrawal withdrawal = new Withdrawal (amount, patron.getBrokerageAccount(), patron);
            withdrawal.execute();
        }
        catch (NoSuchAccountException e){
            System.out.println("no such account");
        }
    }

    /**
     * check how much cash the patron has in his brokerage account
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a brokerage account
     */
    public double checkAvailableBalanceBrokerage(long socialSecurityNumber, String userName, String password) throws AuthenticationException,UnauthorizedActionException{
        Patron patron = bankPatrons[0];
        boolean hasPatron = false;
        for (int i = 0; i < numberOfPatrons; i++){
            if ((socialSecurityNumber == bankPatrons[i].getSocialSecurityNumber()) && (bankPatrons[i].getUserName().equals(userName)) && (bankPatrons[i].getPassword().equals(password))){
                patron = bankPatrons[i];
                hasPatron = true;
                break;
            }
        }
        if (!hasPatron){
            throw new AuthenticationException ("username and password do not match bank patron");
        }
        if (patron.getBrokerageAccount() == null){
            throw new UnauthorizedActionException ("account does not exist");
        }
        return patron.getBrokerageAccount().getAvailableBalance();
    }
    /**
     * check the total value of the patron's brokerage account
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a brokerage account
     */
    public double checkTotalBalanceBrokerage(long socialSecurityNumber, String userName, String password) throws AuthenticationException,UnauthorizedActionException{
        Patron patron = bankPatrons[0];
        boolean hasPatron = false;
        for (int i = 0; i < numberOfPatrons; i++){
            if ((socialSecurityNumber == bankPatrons[i].getSocialSecurityNumber()) && (bankPatrons[i].getUserName().equals(userName)) && (bankPatrons[i].getPassword().equals(password))){
                patron = bankPatrons[i];
                hasPatron = true;
                break;
            }
        }
        if (!hasPatron){
            throw new AuthenticationException ("username and password do not match bank patron");
        }
        if (patron.getBrokerageAccount() == null){
            throw new UnauthorizedActionException ("account does not exist");
        }
        return patron.getBrokerageAccount().getTotalBalance();
    }
    /**
     * check how much cash the patron has in his savings account
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a savings account
     */
    public double checkBalanceSavings(long socialSecurityNumber, String userName, String password) throws AuthenticationException,UnauthorizedActionException{
        Patron patron = bankPatrons[0];
        boolean hasPatron = false;
        for (int i = 0; i < numberOfPatrons; i++){
            if ((socialSecurityNumber == bankPatrons[i].getSocialSecurityNumber()) && (bankPatrons[i].getUserName().equals(userName)) && (bankPatrons[i].getPassword().equals(password))){
                patron = bankPatrons[i];
                hasPatron = true;
                break;
            }
        }
        if (!hasPatron){
            throw new AuthenticationException ("username and password do not match bank patron");
        }
        if (patron.getSavingsAccount() == null){
            throw new UnauthorizedActionException ("account does not exist");
        }
        return patron.getSavingsAccount().getTotalBalance();
    }

    /**
     * buy shares of the given stock
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a brokerage account
     * throw InsufficientAssetsException if the required amount of CASH is not present in the brokerage account
     */
    public void purchaseStock(long socialSecurityNumber, String userName, String password, String tickerSymbol, int shares) throws AuthenticationException,UnauthorizedActionException,InsufficientAssetsException{
        Patron patron = bankPatrons[0];
        boolean hasPatron = false;
        for (int i = 0; i < numberOfPatrons; i++){
            if ((socialSecurityNumber == bankPatrons[i].getSocialSecurityNumber()) && (bankPatrons[i].getUserName().equals(userName)) && (bankPatrons[i].getPassword().equals(password))){
                patron = bankPatrons[i];
                hasPatron = true;
                break;
            }
        }
        if (!hasPatron){
            throw new AuthenticationException ("username and password do not match bank patron");
        }
        if (patron.getBrokerageAccount() == null){
            throw new UnauthorizedActionException ("account does not exist");
        }
        Stock stock = getStockBySymbol(tickerSymbol);
        try{
            StockPurchase purchase = new StockPurchase(shares, patron.getBrokerageAccount(), patron, stock);
            purchase.execute();
        }
        catch (NoSuchAccountException e){
            System.out.println("no such account");
        }
    }

    /**
     * sell shares of the given stock
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a brokerage account
     * throw InsufficientAssetsException if the patrong does not have the given number of shares of the given stock
     */
    public void sellStock(long socialSecurityNumber, String userName, String password, String tickerSymbol, int shares) throws AuthenticationException,UnauthorizedActionException,InsufficientAssetsException{
        Patron patron = bankPatrons[0];
        boolean hasPatron = false;
        for (int i = 0; i < numberOfPatrons; i++){
            if ((socialSecurityNumber == bankPatrons[i].getSocialSecurityNumber()) && (bankPatrons[i].getUserName().equals(userName)) && (bankPatrons[i].getPassword().equals(password))){
                patron = bankPatrons[i];
                hasPatron = true;
                break;
            }
        }
        if (!hasPatron){
            throw new AuthenticationException ("username and password do not match bank patron");
        }
        if (patron.getBrokerageAccount() == null){
            throw new UnauthorizedActionException ("account does not exist");
        }
        Stock stock = getStockBySymbol(tickerSymbol);
        try{
            StockSale sale = new StockSale(shares, patron.getBrokerageAccount(), patron, stock);
            sale.execute();
        }
        catch (NoSuchAccountException e){
            System.out.println("no such account");
        }
    }

    /**
     * check the net worth of the patron
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * return 0 if the patron doesn't have any accounts
     */
    public double getNetWorth(long socialSecurityNumber, String userName, String password) throws AuthenticationException{
        Patron patron = bankPatrons[0];
        boolean hasPatron = false;
        for (int i = 0; i < numberOfPatrons; i++){
            if ((socialSecurityNumber == bankPatrons[i].getSocialSecurityNumber()) && (bankPatrons[i].getUserName().equals(userName)) && (bankPatrons[i].getPassword().equals(password))){
                patron = bankPatrons[i];
                hasPatron = true;
                break;
            }
        }
        if (!hasPatron){
            throw new AuthenticationException ("username and password do not match bank patron");
        }
        return patron.getNetWorth();
    }

    /**
     * Get the transaction history on all of the patron's accounts, i.e. the transaction histories of both the savings account and
     * brokerage account (whichever of the two exist), combined. The merged history should be sorted in time order, from oldest to newest.
     * If the patron has no transactions in his history, return an array of length 0.
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     */
    public Transaction[] getTransactionHistory(long socialSecurityNumber, String userName, String password) throws AuthenticationException{
    	Patron patron = bankPatrons[0];
        boolean hasPatron = false;
        for (int i = 0; i < numberOfPatrons; i++){
            if ((socialSecurityNumber == bankPatrons[i].getSocialSecurityNumber()) && (bankPatrons[i].getUserName().equals(userName)) && (bankPatrons[i].getPassword().equals(password))){
                patron = bankPatrons[i];
                hasPatron = true;
                break;
            }
        }
        if (!hasPatron){
            throw new AuthenticationException ("username and password do not match bank patron");
        }
        BrokerageAccount brokerageAccount = patron.getBrokerageAccount();
        SavingsAccount savingsAccount = patron.getSavingsAccount();
        Transaction [] brokerageTxHistory = new Transaction [0];
        int brokerageNumberOfTx = 0;
        Transaction [] savingsTxHistory = new Transaction [0];
        int savingsNumberOfTx = 0;
        int numberOfTx = 0;
        Transaction [] transactionHistory = new Transaction [numberOfTx];
        
        if (brokerageAccount != null){
            brokerageTxHistory = brokerageAccount.getTransactionHistory();
            brokerageNumberOfTx =  brokerageAccount.getNumberOfTx();
        }

        if (savingsAccount != null){
            savingsTxHistory = savingsAccount.getTransactionHistory();
            savingsNumberOfTx = savingsAccount.getNumberOfTx();
        }
        
        numberOfTx = brokerageNumberOfTx + savingsNumberOfTx;
        transactionHistory = new Transaction [numberOfTx];

        if (numberOfTx == 0){
        	return transactionHistory;
        }

        if (brokerageTxHistory.length > 0){
            for (int i = 0; i < brokerageNumberOfTx; i++){
                transactionHistory[i] = brokerageTxHistory[i];
            }
        }
        if (savingsTxHistory.length > 0){
            for (int i = brokerageNumberOfTx; i < numberOfTx; i++){
                transactionHistory[i] = savingsTxHistory[i-brokerageNumberOfTx];
            }
        }
        
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

        
        for (int i = 0; i < numberOfTx; i++){
            for (int h = 0; h < numberOfTx; h++){
                if (i != h){
                    if (transactionHistory[i].equals(transactionHistory[h])){
                        for (int x = h; x < numberOfTx-1; x++){
                            transactionHistory[x] = transactionHistory[x+1];
                        }
                        Transaction [] transactionHistory1 = new Transaction [numberOfTx - 1];
                        for (int z = 0; z < transactionHistory1.length; z++){
                            transactionHistory1[z] = transactionHistory[z];
                        }
                        transactionHistory = transactionHistory1;
                        numberOfTx--;
                    }
                }
            }
        }

        return transactionHistory;
    }

    protected Transaction [] transactionArrayLengthDoubler(Transaction[] array) {
        Transaction [] copy = new Transaction [(array.length)*2];
        for (int i = 0; i < array.length; i++){
            copy [i] = array [i];
        }
        return copy;
    }

    public static class Stock {
        private final String tickerSymbol;
        private final double sharePrice;
        /**
         * Note that because this constructor is private, the Bank class is the only class that can create instances of Stock.
         * All other classes may refer to, i.e. have variables pointing to, Stock objects, but only Bank can create new Stock Objects.
         */
        private Stock(String tickerSymbol, double sharePrice){
            this.tickerSymbol = tickerSymbol;
            this.sharePrice = sharePrice;
        }
        public double getSharePrice(){
            return this.sharePrice;
        }
        public String getTickerSymbol(){
            return this.tickerSymbol;
        }
        public boolean equals (Bank.Stock stock){
            if (this.tickerSymbol.equals(stock.getTickerSymbol()) && this.sharePrice == stock.getSharePrice()){
                return true;
            }
            return false;
        }
    }
}