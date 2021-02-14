package edu.yu.cs.intro.simpleBank;

import edu.yu.cs.intro.simpleBank.exceptions.InsufficientAssetsException;
import java.util.*;

public class BrokerageAccount extends Account {
    private Map<String,Integer> stocksToNumberOfShares;

    protected BrokerageAccount(long accountNumber) {
        super(accountNumber);
        stocksToNumberOfShares = new HashMap<>();
    }

    protected int getNumberOfShares(String stock){
        if (this.stocksToNumberOfShares.get(stock) == null){
            return 0;
        }
        return this.stocksToNumberOfShares.get(stock);
    }
    /**
     * Buy the given amount of the given stock. Must have enough cash in the account to purchase them.
     * If there is enough cash, reduce cash and increase shares of the given stock
     * If there is not enough cash, throw an InsufficientAssetsException
     */
    protected void buyShares(String stock, int shares) throws InsufficientAssetsException {
        double price = Bank.getBank().getStockPrice(stock)*shares;
        if (this.getAvailableBalance() < price){
            throw new InsufficientAssetsException ("Insufficient Assets");
        }
        this.withdrawCash(price);
        this.stocksToNumberOfShares.put(stock, shares);
    }

    /**
     * Sell the given amount of the given stock. Must have enough shares in the account to sell.
     * If there are enough shares, reduce shares and increase cash.
     * If there are not enough shares, throw an InsufficientAssetsException
     */
    protected void sellShares(String stock, int shares) throws InsufficientAssetsException{
        int amount = stocksToNumberOfShares.get(stock);
        if (stocksToNumberOfShares.get(stock) == null || amount < shares){
            throw new InsufficientAssetsException ("Insufficient Assets");
        }
        if (amount - shares == 0){
            stocksToNumberOfShares.remove(stock);
        }
        stocksToNumberOfShares.put(stock, amount-shares);
    }

    /**
     * this method must return the total amount of cash + the total market value of all stocks owned.
     * The market value of a single stock is determined by multiplying the share price of the stock times the number of shares owned
     * @return
     */
    protected double getTotalBalance(){
        double total = 0;
        if (this.stocksToNumberOfShares.size() == 0){
            return this.getAvailableBalance();
        }
        Integer [] arrayAmount = stocksToNumberOfShares.values().toArray(new Integer[this.stocksToNumberOfShares.size()]);
        String [] arrayTicker = stocksToNumberOfShares.keySet().toArray(new String [this.stocksToNumberOfShares.size()]);
        for (int i = 0; i < arrayAmount.length; i++){
            total += arrayAmount[i] * (Bank.getBank().getStockPrice(arrayTicker[i]));
        }
        return total + this.getAvailableBalance();
    }

}