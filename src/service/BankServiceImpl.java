package service;

import domain.Account;
import domain.Transaction;
import repository.AccountRepository;
import repository.TransactionRepo;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class BankServiceImpl implements BankService {

    private final AccountRepository accountRepository = new AccountRepository();
    private final TransactionRepo transactionRepo = new TransactionRepo();

    @Override
    public String openAccount(String name, String email, String accountType) {
        String customerId = UUID.randomUUID().toString();
        // CHANGE LATER
        /*String accountNumber = UUID.randomUUID().toString();*/
        String accountNumber = getAccountNumber();
        Account account = new Account(accountNumber,accountType ,(double)0 );
        //SAVE

        accountRepository.save(account);
        return accountNumber;
    }

    @Override
    public List<Account> listAccounts() {
        return accountRepository.findAll().stream()
                .sorted(Comparator.comparing(Account::getAccountNumber))
                .collect(Collectors.toList());

    }

    @Override
    public void deposit(String accountNumber, Double amount, String deposit) {
        Account account = accountRepository.findByNumber(accountNumber)
                        .orElseThrow(()-> new RuntimeException(" Account not found: " + accountNumber));
        account.getBalance(Double.valueOf( account.getBalance() + deposit));
        Transaction transaction = new Transaction(accountNumber,amount, UUID.randomUUID().toString(), deposit);
        transactionRepo.add()

    }

    private String getAccountNumber() {
        int temp = accountRepository.findAll().size() +1;
        String accountNumber = String.format("AC%06d", temp);
        return accountNumber;
    }
}
