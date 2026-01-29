package repository;

import domain.Transaction;

import java.util.*;

public class TransactionRepo {
    private final Map<String, List<Transaction>> transactionsByAccountNumber = new HashMap<>();




    public void add(Transaction transaction) {
        List<Transaction> list = transactionsByAccountNumber.computeIfAbsent(transaction.getAccountNumber(), k -> new ArrayList<>());
        list.add(transaction);

    }

    public List<Transaction> findByAccount(String accountNumber) {
        return new  ArrayList<>(transactionsByAccountNumber.getOrDefault(accountNumber, Collections.emptyList()) );
    };
}
