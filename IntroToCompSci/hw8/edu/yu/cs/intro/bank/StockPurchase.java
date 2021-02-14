package edu.yu.cs.intro.bank;

import edu.yu.cs.intro.bank.exceptions.InsufficientAssetsException;
import edu.yu.cs.intro.bank.exceptions.NoSuchAccountException;
import edu.yu.cs.intro.bank.exceptions.UnauthorizedActionException;

public class StockPurchase extends StockTransaction {
    /**
     * check that the given brokerage account has enough cash in it to but the "amount" number of shares of the given stock.
     * if not, throw InsufficientAssetsException
     */
    public StockPurchase(double amount, BrokerageAccount target, Patron patron, Bank.Stock stock) throws NoSuchAccountException, InsufficientAssetsException, UnauthorizedActionException {
        super(amount, target, patron, stock);
    }

    @Override
    protected long getTime() {
        return super.time;
    }

    @Override
    protected double getAmount() {
        return this.amount;
    }

    @Override
    protected Account getTarget() {
        return this.target;
    }

    @Override
    protected Patron getPatron() {
        return this.patron;
    }

    /**
     * check that the given brokerage account has enough cash in it to but the "amount" number of shares of the given stock. If not, throw InsufficientAssetsException.
     * Buy the stock, i.e. use the methods on the patron's brokerage account to make the purchase.
     * Set the time of transaction execution.
     */
    @Override
    protected void execute() throws InsufficientAssetsException {
        BrokerageAccount brokerageAccount = (BrokerageAccount) this.target;
        brokerageAccount.buyShares(this.stock, (int)this.amount);
        super.time = System.currentTimeMillis();
        target.addTransactionToHistory(this);
    }
}
