package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Comparator;

public class Screens {

    //*Helper method to display welcome banner
    public static void welcomeBanner() {
        System.out.printf("""
                ╔════════════════════════════════════════════╗
                ║                                            ║
                ║         I N K   &   N E E D L E            ║
                ║         tattoo studio  ·  ledger           ║
                ║                                            ║
                ╚════════════════════════════════════════════╝
                ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ 
                Welcome back! Today's date and time: %s
                """, LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy',' HH:mm:ss")));
    }

    //*Method that will display the Home Screen when called / user chooses that action
    public static void homeScreen(Scanner userInput, ArrayList<Transaction> inkAndNeedleTransactions, String fileName) {

        //* Welcoming the user to the main menu
        System.out.printf("\nWelcome to the main menu! Please choose an option below: ");

        //*This is a variable (boolean) to keep the menu looping
        boolean homeScreenRunning = true;

        //* Variable to count how many times the home screen has repeated / looped (welcome message / sout timing)
        int homeScreenIteration = 0;
        //* Looping the menu
        while (homeScreenRunning) {
            if (homeScreenIteration >= 1) {
                System.out.print("Please choose an option below: \n");
            }
            //* Printing options / cases to the user
            System.out.printf("\n▪ D) Add Deposit\n▪ P) Make Payment (Debit)\n▪ L) Ledger - display the ledger screen\n▪ X) Exit - exit the application\n");
            //* Recording the user's choice; converting it to lower case, trimming, then storing it in a String variable.
            String homeScreenChoice = userInput.nextLine().toLowerCase().trim();
            //* Switch statement for the different choices
            switch (homeScreenChoice) {
                case "d":
                    System.out.print("\nDeposit amount (e.g. 105.97): $");
                    while (!userInput.hasNextDouble()) {
                        userInput.nextLine();
                        System.out.print("Please enter a number: $");
                    }
                    double depositAmount = userInput.nextDouble();
                    userInput.nextLine();
                    //* A while loop for if the user enters a negative number. Will persist until they enter a positive #
                    while (depositAmount <= 0) {
                        System.out.print("\nPlease enter a positive amount (e.g. 105.97): $");
                        while (!userInput.hasNextDouble()) {
                            userInput.nextLine();
                            System.out.print("Please enter a number: $");
                        }
                        depositAmount = userInput.nextDouble();
                        userInput.nextLine();
                    }

                    System.out.print("\nWho is the deposit from?: ");
                    String depositVendor = userInput.nextLine().trim();
                    while (depositVendor.length() == 0) {
                        System.out.print("Please designate who the deposit is from: ");
                        depositVendor = userInput.nextLine().trim();
                    }

                    System.out.print("\nPlease describe this deposit (optional): ");
                    String depositDescription = userInput.nextLine().trim();
                    if (depositDescription.length() == 0) {
                        depositDescription = "No Description";
                    }

                    LocalDate depositDate = LocalDate.now();
                    LocalTime depositTime = LocalTime.now();
                    Transaction newDeposit = new Transaction(depositDate, depositTime, depositDescription, depositVendor, depositAmount);
                    inkAndNeedleTransactions.add(newDeposit);
                    TransactionManager.saveTransaction(newDeposit, fileName);
                    System.out.printf("\nA new deposit amount of $%.2f has been saved.%n", depositAmount);
                    break;
                case "p":
                    System.out.print("\nPayment amount (e.g. 120.00): $");
                    while (!userInput.hasNextDouble()) {
                        userInput.nextLine();
                        System.out.print("Please enter a number: $");
                    }
                    double paymentAmount = userInput.nextDouble();
                    userInput.nextLine();

                    while (paymentAmount <= 0) {
                        System.out.print("\nPlease enter a positive amount (e.g. 120.00): $");
                        while (!userInput.hasNextDouble()) {
                            userInput.nextLine();
                            System.out.print("Please enter a number: $");
                        }
                        paymentAmount = userInput.nextDouble();
                        userInput.nextLine();
                    }

                    System.out.print("\nWho is the payment to?: ");
                    String paymentVendor = userInput.nextLine().trim();
                    while (paymentVendor.length() == 0) {
                        System.out.print("Please enter a recipient for the payment: ");
                        paymentVendor = userInput.nextLine().trim();
                    }

                    System.out.print("\nPlease describe this payment (optional): ");
                    String paymentDescription = userInput.nextLine().trim();
                    if (paymentDescription.length() == 0) {
                        paymentDescription = "No Description";
                    }
                    LocalDate paymentDate = LocalDate.now();
                    LocalTime paymentTime = LocalTime.now();
                    //* For payments we have to convert the number they provided to a negative number
                    //* Converting it ourselves instead of asking the user for the negative # saves confusion.
                    paymentAmount *= -1;
                    Transaction newPayment = new Transaction(paymentDate, paymentTime, paymentDescription, paymentVendor, paymentAmount);
                    inkAndNeedleTransactions.add(newPayment);
                    TransactionManager.saveTransaction(newPayment, fileName);
                    System.out.printf("\nA new payment amount of $%.2f has been saved.%n", (paymentAmount * -1));
                    break;
                case "l":
                    //* Call to ledgerScreen method for nested screen.
                    Screens.ledgerScreen(userInput, inkAndNeedleTransactions, fileName);
                    break;
                case "x":
                    System.out.println("Thank you for using this application!");
                    homeScreenRunning = false;
                    break;
                default:
                    //* Default option if the user inputs something other than what we have cases for.
                    System.out.println("Please enter a valid option. Refer to the letters next to the options.");
                    break;
            }
            homeScreenIteration++;
        }
    }

    public static void ledgerScreen(Scanner userInput, ArrayList<Transaction> inkAndNeedleTransactions, String fileName) {

        ArrayList<Transaction> newestFirstTransactions = new ArrayList<>(inkAndNeedleTransactions);
        newestFirstTransactions.sort(
                Comparator.comparing(Transaction::getDate).thenComparing(Transaction::getTime)
                        .thenComparing(Transaction::getDescription).reversed()
        );

        System.out.println("Welcome to the ledger screen! Please choose an option below: ");
        int ledgerScreenIteration = 0;
        boolean ledgerScreenRunning = true;
        while (ledgerScreenRunning) {
            if (ledgerScreenIteration >= 1) {
                System.out.print("Please choose an option below: \n");
            }
            System.out.print("""
                    ▪ A) All - Display all entries
                    ▪ D) Deposits - Display only the entries that are deposits into the account
                    ▪ P) Payments - Display only the entries that are payments from the account (negative entries)
                    ▪ R) Reports - Display a new screen that allows the user to run reports or run custom searches
                    ▪ H) Home - Return to the home screen / main menu
                    """);
            String ledgerScreenChoice = userInput.nextLine().toLowerCase().trim();
            switch (ledgerScreenChoice) {
                case "a":
                    System.out.println("Here is a list of all entries: ");
                    int transactionCounter = 1;
                    for (Transaction transaction : newestFirstTransactions) {
                        System.out.println(transaction.transactionDisplayFormat(transactionCounter));
                        transactionCounter++;
                    }
                    break;
                case "d":
                    System.out.println("Here is a list of all deposits: ");
                    int depositCounter = 1;
                    for (Transaction transaction : newestFirstTransactions) {
                        if (transaction.checkIfDeposit()) {
                            System.out.println(transaction.transactionDisplayFormat(depositCounter));
                            depositCounter++;
                        }

                    }
                    break;
                case "p":
                    System.out.println("Here is a list of all payments: ");
                    int paymentCounter = 1;
                    for (Transaction transaction : newestFirstTransactions) {
                        if (!transaction.checkIfDeposit()) {
                            System.out.println(transaction.transactionDisplayFormat(paymentCounter));
                            paymentCounter++;
                        }
                    }
                    break;
                case "r":
                    Screens.reportsScreen(userInput, newestFirstTransactions, fileName);
                    break;
                case "h":
                    ledgerScreenRunning = false;
                    break;
                default:
                    System.out.println("Please enter a valid option. Refer to the letters next to the options.");
                    break;
            }
            ledgerScreenIteration++;
        }

    }

    public static void reportsScreen(Scanner userInput, ArrayList<Transaction> newestFirstTransactions, String fileName) {
        System.out.println("Welcome to the reports screen! Please choose an option below: ");
        LocalDate today = LocalDate.now();
        LocalDate lastMonth = today.minusMonths(1);
        LocalDate lastYear = today.minusYears(1);
        int reportsScreenIteration = 0;
        boolean reportsScreenRunning = true;
        while (reportsScreenRunning) {
            if (reportsScreenIteration >= 1) {
                System.out.print("Please choose an option below: \n");
            }
            System.out.print("""
                    ▪ 1) Month To Date
                    ▪ 2) Previous Month
                    ▪ 3) Year To Date
                    ▪ 4) Previous Year
                    ▪ 5) Search by Vendor
                    ▪ 6) Custom Search
                    ▪ 0) Back - go back to the Ledger screen
                    """);
            while (!userInput.hasNextInt()) {
                userInput.nextLine();
                System.out.println("Please enter a valid choice/number. Refer to the numbered options for your choices.");
            }
            int reportsScreenChoice = userInput.nextInt();
            userInput.nextLine();
            switch (reportsScreenChoice) {
                case 1:
//                    Month To Date
                    int monthToDateCounter = 1;

                    for (Transaction transaction : newestFirstTransactions) {
                        LocalDate transactionsEntryDate = transaction.getDate();
                        if (transactionsEntryDate.getMonthValue() == today.getMonthValue()
                                && transactionsEntryDate.getYear() == today.getYear()) {
                            System.out.println(transaction.transactionDisplayFormat(monthToDateCounter));
                            monthToDateCounter++;
                        }
                    }
                    if (monthToDateCounter == 1) {
                        System.out.println("No transactions for the month to date.");
                    }
                    break;
                case 2:
//                    Previous Month
                    int previousMonthCounter = 1;
                    for (Transaction transaction : newestFirstTransactions) {
                        LocalDate transactionsEntryDate = transaction.getDate();
                        if (transactionsEntryDate.getMonthValue() == (lastMonth.getMonthValue())
                                && transactionsEntryDate.getYear() == lastMonth.getYear()) {
                            System.out.println(transaction.transactionDisplayFormat(previousMonthCounter));
                            previousMonthCounter++;
                        }
                    }
                    if (previousMonthCounter == 1) {
                        System.out.println("No transactions for the previous month.");
                    }
                    break;
                case 3:
//                    Year To Date
                    int yearToDateCounter = 1;
                    for (Transaction transaction : newestFirstTransactions) {
                        LocalDate transactionsEntryDate = transaction.getDate();
                        if (transactionsEntryDate.getYear() == today.getYear()) {
                            System.out.println(transaction.transactionDisplayFormat(yearToDateCounter));
                            yearToDateCounter++;
                        }
                    }
                    if (yearToDateCounter == 1) {
                        System.out.println("No transactions for the year to date.");
                    }
                    break;
                case 4:
//                    Previous Year
                    int previousYearCounter = 1;
                    for (Transaction transaction : newestFirstTransactions) {
                        LocalDate transactionsEntryDate = transaction.getDate();
                        if (transactionsEntryDate.getYear() == lastYear.getYear()) {
                            System.out.println(transaction.transactionDisplayFormat(previousYearCounter));
                            previousYearCounter++;
                        }
                    }
                    if (previousYearCounter == 1) {
                        System.out.println("No transactions for the previous year.");
                    }
                    break;
                case 5:
//                    Search By Vendor
                    System.out.print("\nEnter the vendor name you would like to search by: ");
                    String vendorName = userInput.nextLine().trim().toLowerCase();
                    while (vendorName.length() == 0) {
                        System.out.print("Please enter a vendor name: ");
                        vendorName = userInput.nextLine().trim().toLowerCase();
                    }
                    int searchByVendorCounter = 1;
                    for (Transaction transaction : newestFirstTransactions) {
                        if (transaction.getVendor().toLowerCase().contains(vendorName)) {
                            System.out.println(transaction.transactionDisplayFormat(searchByVendorCounter));
                            searchByVendorCounter++;
                        }
                    }
                    if (searchByVendorCounter == 1) {
                        System.out.println("No transactions for that vendor name.");
                    }
                    break;
                case 6:
//                    Custom Search
                    System.out.println("Please provide the following information for a custom search: ");
                    System.out.print("\nStart Date (yyyy-mm-dd, or press Enter to skip): ");
                    String cSearchStartDate = userInput.nextLine().trim();
                    boolean validStartDate = false;

                    while (!validStartDate) {
                        if (cSearchStartDate.isEmpty()) {
                            validStartDate = true;
                        } else {
                            try {
                                LocalDate.parse(cSearchStartDate);
                                validStartDate = true;
                            } catch (DateTimeParseException e) {
                                System.out.print("Please enter a valid date (yyyy-mm-dd) or press Enter to skip: ");
                                cSearchStartDate = userInput.nextLine().trim();
                            }
                        }
                    }


                    System.out.print("\nEnd Date (yyyy-mm-dd, or press Enter to skip): ");
                    String cSearchEndDate = userInput.nextLine().trim();
                    boolean validEndDate = false;

                    while (!validEndDate) {
                        if (cSearchEndDate.isEmpty()) {
                            validEndDate = true;
                        } else {
                            try {
                                LocalDate.parse(cSearchEndDate);
                                validEndDate = true;
                            } catch (DateTimeParseException e) {
                                System.out.print("Please enter a valid date (yyyy-mm-dd) or press Enter to skip: ");
                                cSearchEndDate = userInput.nextLine().trim();
                            }
                        }
                    }

                    System.out.print("\nDescription (or press Enter to skip): ");
                    String cSearchDescription = userInput.nextLine().trim().toLowerCase();

                    System.out.print("\nVendor (or press Enter to skip): ");
                    String cSearchVendor = userInput.nextLine().trim().toLowerCase();

                    System.out.print("\nDeposit or Payment? (or press Enter to skip): ");
                    String cSearchEntryType = userInput.nextLine().trim().toLowerCase();
                    while (!cSearchEntryType.equals("deposit") && !cSearchEntryType.equals("payment") && !cSearchEntryType.isEmpty()) {
                        System.out.print("Please answer 'Deposit' or 'Payment' letter for letter:");
                        cSearchEntryType = userInput.nextLine().trim().toLowerCase();
                    }

                    System.out.print("\nAmount (e.g. 50.00 or press Enter to skip): $");
                    String cSearchAmount = userInput.nextLine().trim();
                    boolean validSearchAmount = false;

                    while (!validSearchAmount) {
                        if (cSearchAmount.isEmpty()) {
                            validSearchAmount = true;
                        } else {
                            try {
                                Double.parseDouble(cSearchAmount);
                                validSearchAmount = true;
                            }  catch (NumberFormatException e) {
                                System.out.print("Please enter a valid amount in a xx.xx format (e.g. 59.99): ");
                                cSearchAmount = userInput.nextLine().trim();
                            }
                        }
                    }

                    int cSearchCounter = 1;
                    for (Transaction transaction : newestFirstTransactions) {
                        boolean isMatch = true;
                        //Description
                        if (cSearchDescription.length() > 0 && !transaction.getDescription().toLowerCase().contains(cSearchDescription)) {
                            isMatch = false;
                        }
                        //Vendor
                        if (cSearchVendor.length() > 0 && !transaction.getVendor().toLowerCase().contains(cSearchVendor)) {
                            isMatch = false;
                        }

                        //Deposit?
                        if (cSearchEntryType.equals("deposit")) {
                            if (!transaction.checkIfDeposit()) {
                                isMatch = false;
                            }
                        }
                        //Payment?
                        if (cSearchEntryType.equals("payment")) {
                            if (transaction.checkIfDeposit()) {
                                isMatch = false;
                            }
                        }

//                        Start Date:
                        if (!cSearchStartDate.isEmpty()) {
                            LocalDate startDate = LocalDate.parse(cSearchStartDate);
                            if (transaction.getDate().isBefore(startDate)) {
                                isMatch = false;
                            }
                        }
//                        End Date:
                        if (!cSearchEndDate.isEmpty()) {
                            LocalDate endDate = LocalDate.parse(cSearchEndDate);
                            if (transaction.getDate().isAfter(endDate)) {
                                isMatch = false;
                            }
                        }
//                        Amount:
                        if (!cSearchAmount.isEmpty()) {
                            double searchAmount = Double.parseDouble(cSearchAmount);
                            if (Math.abs(searchAmount) != (Math.abs(transaction.getAmount()))) {
                                isMatch = false;
                            }
                        }

                        if (isMatch) {
                            System.out.println(transaction.transactionDisplayFormat(cSearchCounter));
                            cSearchCounter++;
                        }

                    }
                    if (cSearchCounter == 1) {
                        System.out.println("No results for that custom search. ");
                    }
                    break;
                case 0:
                    reportsScreenRunning = false;
                    break;
                default:
                    System.out.println("Please enter a valid option. Refer to the numbers next to the options.");
                    break;
            }
            reportsScreenIteration++;
        }
    }

}