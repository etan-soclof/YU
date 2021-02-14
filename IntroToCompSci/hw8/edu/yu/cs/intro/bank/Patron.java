package edu.yu.cs.intro.bank;

import edu.yu.cs.intro.bank.exceptions.NoSuchAccountException;

public class Patron {
    private final String firstName;
    private final String lastName;
    private final long socialSecurityNumber;
    private final String userName;
    private final String password;
    private BrokerageAccount brokerageAccount;
    private SavingsAccount savingsAccount;

    protected Patron(String firstName, String lastName, long socialSecurityNumber, String userName, String password){
        this.firstName = firstName;
        this.lastName = lastName;
        this.socialSecurityNumber = socialSecurityNumber;
        this.userName = userName;
        this.password = password;
    }
    protected String getFirstName(){
        return this.firstName;
    }
    protected String getLastName(){
        return this.lastName;
    }
    protected long getSocialSecurityNumber(){
        return this.socialSecurityNumber;
    }
    protected String getUserName(){
        return this.userName;
    }
    protected String getPassword(){
        return this.password;
    }
    protected void addAccount(Account acct){
        if (acct instanceof BrokerageAccount){
            brokerageAccount = (BrokerageAccount) acct;
        }
        if (acct instanceof SavingsAccount){
            savingsAccount = (SavingsAccount) acct;
        }
    }

    protected Account getAccount(long accountNumber) throws NoSuchAccountException {
        if (accountNumber == this.brokerageAccount.getAccountNumber()){
            return this.brokerageAccount;
        }
        else if (accountNumber == this.savingsAccount.getAccountNumber()){
            return this.savingsAccount;
        }
        else {
            throw new NoSuchAccountException ("No such account.");
        }
    }

    protected void setBrokerageAccount(BrokerageAccount account) {
        this.brokerageAccount = account;
    }

    protected void setSavingsAccount(SavingsAccount account) {
        this.savingsAccount = account;
    }

    protected BrokerageAccount getBrokerageAccount() {
        return this.brokerageAccount;
    }

    protected SavingsAccount getSavingsAccount() {
        return this.savingsAccount;
    }

    /**
     * total cash in savings + total cash in brokerage + total value of shares in brokerage
     * return 0 if the patron doesn't have any accounts
     */
    protected double getNetWorth(){
        if (this.brokerageAccount == null && this.savingsAccount == null){
            return 0;
        }
        else if (this.brokerageAccount != null && this.savingsAccount != null) {
            return this.savingsAccount.getTotalBalance() + this.brokerageAccount.getTotalBalance();
        }

        else if (this.brokerageAccount == null){
            return this.savingsAccount.getTotalBalance();
        }
        else{
            return this.brokerageAccount.getTotalBalance();
        }
    }
}
