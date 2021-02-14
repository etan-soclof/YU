package edu.yu.cs.intro.bank;

import edu.yu.cs.intro.bank.exceptions.InsufficientAssetsException;

public class BrokerageAccount extends Account {
    private double transactionFee;
    /**an array of stocks owned*/
    private Bank.Stock[] stocksOwned = new Bank.Stock[1];
    /**the number of shares of each stock owned by this account*/
    private int[] numberOfShares = new int [1];
    protected int numberOfStocks= 0;

    protected BrokerageAccount(long accountNumber, Patron patron) {
        super(accountNumber,patron);
    }

    protected int getNumberOfShares(Bank.Stock stock){
        if (numberOfStocks > 0){
            for (int i = 0; i < numberOfStocks; i++){
                if (stock.getTickerSymbol().equals(this.stocksOwned[i].getTickerSymbol())){
                    return numberOfShares[i];
                }
            }
        }
        return 0;
    }
    /**
     * Buy the given amount of the given stock. Must have enough cash in the account to purchase them.
     * If there is enough cash, reduce cash and increase shares of the given stock
     * If there is not enough cash, throw an InsufficientAssetsException
     */
    protected void buyShares(Bank.Stock stock, int shares) throws InsufficientAssetsException {
        if (stock != null){
            double price = stock.getSharePrice();
            price *= shares;
            boolean ownsStock = false;
            if (this.getAvailableBalance()>=price){
                if (numberOfStocks>0){
                    for (int i = 0; i < this.numberOfStocks; i++){
                        if (stock.equals(this.stocksOwned[i])){
                            System.out.println(this.numberOfShares[i]);
                            this.numberOfShares[i] += shares;
                            ownsStock = true;
                            break;
                        }
                    }
                }
                if (!ownsStock){
                    if (this.numberOfStocks == this.stocksOwned.length){
                        this.stocksOwned = stockArrayLengthDoubler(this.stocksOwned);
                        this.numberOfShares = intArrayLengthDoubler(this.numberOfShares);
                    }
                    this.stocksOwned[numberOfStocks] = stock;
                    this.numberOfShares[numberOfStocks] = shares;
                    this.numberOfStocks++;
                }
                super.cash -= price;
            }
            else {
                throw new InsufficientAssetsException ("Insufficient Assets");
            }
        }
    }

    /**
     * Sell the given amount of the given stock. Must have enough shares in the account to sell.
     * If there are enough shares, reduce shares and increase cash.
     * If there are not enough shares, throw an InsufficientAssetsException
     */
    protected void sellShares(Bank.Stock stock, int shares) throws InsufficientAssetsException{
        int indexOfStock = -1;
        boolean ownsStock = false;
        for (int i = 0; i < numberOfStocks; i++){
            if (stock.equals(this.stocksOwned[i])){
                indexOfStock = i;
                break;
            }
        }
        if (this.numberOfShares[indexOfStock] < shares){
            throw new InsufficientAssetsException ("Not enough shares of stock");
        }
        else if (this.numberOfShares[indexOfStock] == shares){
            numberOfStocks--;
            for (int i = indexOfStock; i < numberOfStocks; i++){
                this.stocksOwned[i] = this.stocksOwned[i + 1];
                this.numberOfShares[i] = this.numberOfShares[i + 1];
            }
        }
        else {
            this.numberOfShares[indexOfStock] -= shares;
        }
        super.cash += stock.getSharePrice()*shares;
    }

    protected Bank.Stock [] stockArrayLengthDoubler(Bank.Stock[] array) {
        Bank.Stock [] copy = new Bank.Stock [(array.length)*2];
        for (int i = 0; i < array.length; i++){
            copy [i] = array [i];
        }
        return copy;
    }

    protected int [] intArrayLengthDoubler(int[] array) {
        int [] copy = new int [(array.length)*2];
        for (int i = 0; i < array.length; i++){
            copy [i] = array [i];
        }
        return copy;
    }

    /**
     * this method must return the total amount of cash + the total market value of all stocks owned.
     * The market value of a single stock is determined by multiplying the share price of the stock times the number of shares owned
     * @return
     */
    @Override
    protected double getTotalBalance(){
        double total = super.cash;
        for (int i = 0; i < stocksOwned.length; i++){
            total = total + (stocksOwned[i].getSharePrice()*numberOfShares[i]);
        }

        return total;
    }

    /**
     * this method must return total cash
     * @return
     */
    @Override
    protected double getAvailableBalance() {
        return super.cash;
    }
}