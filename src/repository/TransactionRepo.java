package repository;

import domain.Transaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionRepo {
    private final Map<String, List<Transaction>> transactionsByAccountNumber = new HashMap<>();
}
