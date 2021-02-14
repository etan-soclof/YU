package edu.yu.cs.intro.bank;

import edu.yu.cs.intro.bank.exceptions.InsufficientAssetsException;

import java.util.Arrays;

public abstract class Account {
    private final long accountNumber;
    private Patron patron;
    private Transaction[] txHistory = new Transaction[1];
    private int numberOfTx = 0;
    protected double cash = 0;

    public static void main (String [] args){
        Transaction [] transaction = new Transaction [0];
        
    }
    protected Account(long accountNumber, Patron patron){
        this.accountNumber = accountNumber;
        this.patron = patron;
    }

    protected long getAccountNumber() {
        return this.accountNumber;
    }

    protected Patron getPatron() {
        return this.patron;
    }
    /**
     * returns a copy of the txHistory array
     * why do you think we return a copy and not the original array?
     */
    protected Transaction[] getTransactionHistory(){
        return Arrays.copyOf(this.txHistory,this.txHistory.length);
    }
    protected int getNumberOfTx(){
    	return this.numberOfTx;
    }

    protected void depositCash(double amount){
        if(amount < 0){
            throw new IllegalArgumentException("can't deposit negative cash");
        }
        this.cash += amount;
    }
    //*************************************************
    //below are methods you must complete in this class
    //*************************************************
    /**add a tx to the tx history of this account*/
    protected void addTransactionToHistory(Transaction tx){
        if (this.numberOfTx == this.txHistory.length){
            txHistory = this.arrayLengthDoubler(this.txHistory);
        }
        boolean doesTxExist = false;
        for (int i = 0; i < this.txHistory.length; i++){
            if (tx.equals(txHistory[i])){
                doesTxExist = true;
            }
        }
        if (!doesTxExist){
            this.txHistory[numberOfTx] = tx;
            this.numberOfTx ++;
        }
    }

    protected void withdrawCash(double amount) throws InsufficientAssetsException{
        if (amount > this.cash){
            throw new InsufficientAssetsException ("not enough money in our account to withdraw");
        }
        this.cash -= amount;
    }

    protected Transaction[] arrayLengthDoubler(Transaction[] array) {
        Transaction [] copy = new Transaction [(array.length)*2];
        for (int i = 0; i < array.length; i++){
            copy [i] = array [i];
        }
        return copy;
    }
    //*********************************************************
    //below are abstract methods that subclasses must implement
    //*********************************************************
    protected abstract double getTotalBalance();
    protected abstract double getAvailableBalance();
}