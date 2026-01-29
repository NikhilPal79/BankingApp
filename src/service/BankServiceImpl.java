package service;

import domain.Account;
import domain.Customer;
import domain.Transaction;
import domain.Type;
import exceptions.AccountNotFoundException;
import exceptions.InsufficientFundsException;
import exceptions.ValidationException;
import repository.AccountRepository;
import repository.CustomerRepo;
import repository.TransactionRepo;
import util.Validation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BankServiceImpl implements BankService {

    private final AccountRepository accountRepository = new AccountRepository();
    private final TransactionRepo transactionRepo = new TransactionRepo();
    private final CustomerRepo customerRepo = new CustomerRepo();

    private final Validation<String> validateName = name -> {
        if (name == null || name.isEmpty()) throw new ValidationException("Customer name is empty");
    };

    private final Validation<String> validatEmail = email -> {
        if (email == null || !email.contains("@")) throw new ValidationException("Customer email is empty");
    };

    private final Validation<String> validateType = type -> {
        if (type == null || (type.equalsIgnoreCase("SAVINGS")) || type.contains("CURRENT") )
            throw new ValidationException("Account Type should be SAVINGS or CURRENT");
    };

    @Override
    public String openAccount(String name, String email, String accountType) throws ValidationException {

        validateName.validate(name);
        validatEmail.validate(email);
        validateType.validate(accountType);

        String customerId = UUID.randomUUID().toString();
        // CHANGE LATER
        /*String accountNumber = UUID.randomUUID().toString();*/

        Customer c = new Customer(email, customerId, customerId);
        customerRepo.save(c);



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
    public void deposit(String accountNumber, Double amount, String note) throws AccountNotFoundException {
        Account account = accountRepository.findByNumber(accountNumber)
                        .orElseThrow(()-> new AccountNotFoundException(" Account not found: " + accountNumber));
        account.setBalance(account.getBalance() + amount);
        Transaction transaction = new Transaction(account.getAccountNumber(),amount, UUID.randomUUID().toString(), note, LocalDateTime.now(), Type.DEPOSIT);
        transactionRepo.add(transaction);

    }

    @Override
    public void withdraw(String accountNumber, Double amount, String note) throws AccountNotFoundException {
        Account account = accountRepository.findByNumber(accountNumber)
                .orElseThrow(()-> new AccountNotFoundException(" Account not found: " + accountNumber));
        if ( account.getBalance().compareTo(amount) < 0 ) {
            throw new InsufficientFundsException(" Insufficient Balance ");
        }
        account.setBalance(account.getBalance() - amount);
        Transaction transaction = new Transaction(account.getAccountNumber(),amount, UUID.randomUUID().toString(), note, LocalDateTime.now(), Type.WITHDRAW);
        transactionRepo.add(transaction);

    }


    @Override
    public void transfer(String fromAccount, String toAccount, Double amount, String note) throws AccountNotFoundException{
        if (fromAccount.equals(toAccount)) {
            throw new RuntimeException("Cant transfer to you account");
        }
        Account from = accountRepository.findByNumber(fromAccount)
                    .orElseThrow(() -> new AccountNotFoundException(" Account not found: " + fromAccount));


        Account to = accountRepository.findByNumber(toAccount)
                    .orElseThrow(() -> new AccountNotFoundException(" Account not found: " + toAccount));

        if (from.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(" Insufficient Balance ");
        }

            from.setBalance(from.getBalance() - amount);
            to.setBalance(to.getBalance() + amount);

            Transaction fromtransaction = new Transaction(from.getAccountNumber(),
                    amount, UUID.randomUUID().toString(), note, LocalDateTime.now(), Type.TRANSFER_OUT);
            transactionRepo.add(fromtransaction);

            Transaction totransaction = new Transaction(to.getAccountNumber(),
                    amount, UUID.randomUUID().toString(), note, LocalDateTime.now(), Type.TRANSFER_IN);
            transactionRepo.add(totransaction);

        }

    @Override
    public List<Transaction> getStatement(String account) {
        return transactionRepo.findByAccount(account).stream()
                .sorted(Comparator.comparing(Transaction::getTimestamp))
                .collect(Collectors.toList());
    }

    @Override
    public List<Account> searchAccountByCustomerName(String account) {

        String query = (account == null) ? "": account.toLowerCase();
        List<Account> result = new ArrayList<>();
        for (Customer c : customerRepo.findAll()) {
            if (c.getName().toLowerCase().contains(query))result.addAll(accountRepository.findByCustomerId(c.getId()));
        }
        result.sort(Comparator.comparing(Account::getAccountNumber));
        return result;
    }

    private String getAccountNumber() {
        int temp = accountRepository.findAll().size() + 1;
        String accountNumber = String.format("AC%06d", temp);
        return accountNumber;
    }

}


