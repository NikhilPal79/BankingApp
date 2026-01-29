package service;

import domain.Account;
import domain.Transaction;
import exceptions.AccountNotFoundException;

import java.util.List;

public interface BankService {
    String openAccount(String name, String email, String accountType);
    List<Account> listAccounts();

    void deposit(String accountNumber, Double amount, String note) throws AccountNotFoundException;

    void withdraw(String accountNumber, Double amount, String note) throws AccountNotFoundException;

    void transfer(String fromAccount, String toAccount, Double amount, String note) throws AccountNotFoundException;

    List<Transaction> getStatement(String account);

    List<Account> searchAccountByCustomerName(String account);
}
