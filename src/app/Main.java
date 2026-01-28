package app;

import service.BankService;
import service.BankServiceImpl;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
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
                case "3" -> withdraw(scanner);
                case "4" -> transfer(scanner);
                case "5" -> accountStatement(scanner);
                case "6" -> listAccount(scanner, bankService);
                case "7" -> searchAccount(scanner);
                case "0" ->  running = false;
                }


            }
        }

    private static void openAccount(Scanner scanner, BankService bankService) {
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
        System.out.println( name + "has opened the " +  accountNumber  );

    }

    private static void deposit(Scanner scanner, BankService bankService) {
        System.out.println("Account Number: ");
        String accountNumber = scanner.nextLine().trim();
        System.out.println("Amount: ");
        Double amount = Double.valueOf(scanner.nextLine().trim());
        bankService.deposit(accountNumber,amount,"Deposit");
        System.out.println("Deposited: ");
    }

    private static void withdraw(Scanner scanner) {
    }

    private static void transfer(Scanner scanner) {
    }

    private static void accountStatement(Scanner scanner) {
    }

    private static void listAccount(Scanner scanner, BankService bankService) {
        bankService.listAccounts().forEach(account -> {
            System.out.println(account.getAccountNumber() + " | " + account.getAccountType() + " | " + account.getBalance() ) ;
        });



    }

    private static void searchAccount(Scanner scanner) {
    }
}