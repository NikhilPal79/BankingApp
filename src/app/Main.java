package app;

import domain.Customer;
import exceptions.AccountNotFoundException;
import exceptions.ValidationException;
import service.BankService;
import service.BankServiceImpl;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws AccountNotFoundException, ValidationException {
        Scanner scanner = new Scanner(System.in);

        BankService bankService = new BankServiceImpl();
        boolean running = true;
        System.out.println("Welcome to the Bank Management System");
        System.out.println("");

        while (running) {
            System.out.println("""
                    1- Open Account
                    2- Deposit
                    3- Withdraw
                    4- Transfer 
                    5- Account Statement 
                    6- List Account 
                    7- Search Accounts by Customer Name 
                    0- Exit """);
            System.out.println("Enter your choice: ");
            String choice = scanner.nextLine().trim();
            System.out.println("Your choice: " + choice);

            switch (choice) {
                case "1" -> openAccount(scanner, bankService);
                case "2" -> deposit(scanner, bankService);
                case "3" -> withdraw(scanner, bankService);
                case "4" -> transfer(scanner, bankService);
                case "5" -> accountStatement(scanner, bankService);
                case "6" -> listAccount(scanner, bankService);
                case "7" -> searchAccount(scanner, bankService);
                case "0" ->  running = false;
                }


            }
        }

    private static void openAccount(Scanner scanner, BankService bankService) throws AccountNotFoundException, ValidationException {
        System.out.println("Customer Name: ");
        String name =  scanner.nextLine().trim();
        System.out.println("Customer Email: ");
        String email =  scanner.nextLine().trim();
        System.out.println("Account Type (Saving/Current): ");
        String type =  scanner.nextLine().trim();
        System.out.println("Initial deposit (optional, blank for 0): ");
        String amountStr =  scanner.nextLine().trim();
        Double initialDeposit = Double.valueOf(amountStr);
        String accountNumber = bankService.openAccount(name, email, type);

        if (initialDeposit > 0)
            bankService.deposit(accountNumber,initialDeposit,"Initial Deposit");
        System.out.println( name + " has opened the " +  accountNumber  );

    }

    private static void deposit(Scanner scanner, BankService bankService) throws AccountNotFoundException {
        System.out.println("Account Number: ");
        String accountNumber = scanner.nextLine().trim();
        System.out.println("Amount: ");
        Double amount = Double.valueOf(scanner.nextLine().trim());
        bankService.deposit(accountNumber,amount,"Deposit");
        System.out.println(" You have Deposited: " + amount + " to "  + accountNumber) ;
    }

    private static void withdraw(Scanner scanner, BankService bankService) throws AccountNotFoundException {
        System.out.println("Account Number: ");
        String accountNumber = scanner.nextLine().trim();;
        System.out.println("Amount: ");
        Double amount = Double.valueOf(scanner.nextLine().trim());
        bankService.withdraw(accountNumber,amount," Withdrawn ");
        System.out.println(" You have Withdrawn: " + amount + " from " +  accountNumber);
    }

    private static void transfer(Scanner scanner, BankService bankService) throws AccountNotFoundException {
        System.out.println("From Account: ");
        String fromAccount = scanner.nextLine().trim();
        System.out.println("To Account: ");
        String toAccount = scanner.nextLine().trim();
        System.out.println("Amount: ");
        Double amount = Double.valueOf(scanner.nextLine().trim());
        bankService.transfer(fromAccount,toAccount,amount,"Transfer");
        System.out.println(" You have transferred: " + amount + " from " + fromAccount + " to "  + toAccount);

    }

    private static void accountStatement(Scanner scanner, BankService bankService) {
        System.out.println("Account Number: ");
        String account = scanner.nextLine().trim();
        bankService.getStatement(account).forEach(t-> {
                System.out.println(t.getTimestamp() + " | " + t.getType() + " | " + t.getAmount() + " | " + t.getNote());
        });

    }

    private static void listAccount(Scanner scanner, BankService bankService) {
        bankService.listAccounts().forEach(account -> {
            System.out.println(account.getAccountNumber() + " | " + account.getAccountType() + " | " + account.getBalance() ) ;
        });



    }

    private static void searchAccount(Scanner scanner, BankService bankService ) {
        System.out.println("Customer Name: ");
        String query = scanner.nextLine().trim();
        bankService.searchAccountByCustomerName(query).forEach(account ->
                System.out.println(account.getAccountNumber() + " | " +  account.getAccountType() + " | " +  account.getBalance() ));
    }
}