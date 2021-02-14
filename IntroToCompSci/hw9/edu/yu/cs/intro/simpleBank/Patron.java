package edu.yu.cs.intro.simpleBank;

public class Patron {
    private final String firstName;
    private final String lastName;
    private final long socialSecurityNumber;
    private final String userName;
    private final String password;
    private BrokerageAccount brokerageAccount;
    private Account savingsAccount;

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

    protected String getUserName(){
        return this.userName;
    }

    protected String getPassword(){
        return this.password;
    }

    protected long getSocialSecurityNumber(){
        return this.socialSecurityNumber;
    }

    protected void addAccount(Account acct){
        if (acct instanceof BrokerageAccount){
            this.brokerageAccount = (BrokerageAccount) acct;
        }
        else {
            this.savingsAccount = acct;
        }
    }

    protected Account getAccount(long accountNumber) {
        if (accountNumber == this.savingsAccount.getAccountNumber()){
            return this.savingsAccount;
        }
        else if (accountNumber == this.brokerageAccount.getAccountNumber()){
            return this.brokerageAccount;
        }
        return null;
    }

    protected void setBrokerageAccount(BrokerageAccount account) {
        this.brokerageAccount = account;
    }

    protected void setSavingsAccount(Account account) {
        this.savingsAccount = account;
    }

    protected BrokerageAccount getBrokerageAccount() {
        return this.brokerageAccount;
    }

    protected Account getSavingsAccount() {
        return this.savingsAccount;
    }

    /**
     * total cash in savings + total cash in brokerage + total value of shares in brokerage
     * return 0 if the patron doesn't have any accounts
     */
    protected double getNetWorth(){
        return this.savingsAccount.getAvailableBalance() + this.brokerageAccount.getTotalBalance();
    }
}
