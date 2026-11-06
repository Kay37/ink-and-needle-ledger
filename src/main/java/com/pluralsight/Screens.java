package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Comparator;

import static com.pluralsight.ColorCoding.PIPE;

public class Screens {

    //! Helper method to display welcome banner
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

    //! This is a method to display transactions / entries / reports & searches in a nicely arranged
    //! table. This cuts down on sout code in multiple switch cases & adds fixed widths to columns
    //! for clean, functional readability when displaying information to the user.
    public static void printTable(ArrayList<Transaction> transactionsToShow) {
        //! Code block for my table header (if not empty)

        if (transactionsToShow.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }
        //? Header printing
        System.out.printf("%3s  %-10s%s%-8s%s%-36s%s%-20s%s%11s%n", "#", "DATE", PIPE, "TIME", PIPE, "DESCRIPTION", PIPE, "VENDOR", PIPE, "AMOUNT");
        //? Header separation / divider
        System.out.println(ColorCoding.GRAY + "─".repeat(94) + ColorCoding.RESET);
        int tablePosition = 1;
        double footerMoneyIn = 0;
        double footerMoneyOut = 0;
        double footerNet = 0;
        //? Loop to then format and print each row / line in the ArrayList after the header.
        for (Transaction transaction : transactionsToShow) {
            if (transaction.checkIfDeposit()) {
                footerMoneyIn += transaction.getAmount();
            } else {
                footerMoneyOut += Math.abs(transaction.getAmount());
            }
            System.out.println(transaction.transactionDisplayFormat(tablePosition));
            tablePosition++;
        }
        footerNet = footerMoneyIn - footerMoneyOut;

        String footerNetColor;
        String amountSign;
        if (footerNet >= 0) {
            footerNetColor = ColorCoding.GREEN;
            amountSign = "";
        } else {
            footerNetColor = ColorCoding.RED;
            amountSign = "-";
        }
        String footerEntryWord;
        if (transactionsToShow.size() == 1) {
            footerEntryWord = "entry";
        } else {
            footerEntryWord = "entries";
        }
        System.out.println(ColorCoding.GRAY + "─".repeat(94) + ColorCoding.RESET);
        System.out.printf("%d %s %s Money In: $%,.2f %s Money Out: $%,.2f %s Net: %s%s$%,.2f%s%n", transactionsToShow.size(), footerEntryWord, PIPE, footerMoneyIn, PIPE, footerMoneyOut, PIPE, footerNetColor, amountSign, Math.abs(footerNet), ColorCoding.RESET);
    }

    //! Method that will display the Home Screen when called / user chooses that action.
    //! It's public and static so that it can be called from anywhere, and it's a void because all it does is print
    //! information / run code it doesn't return anything.
    public static void homeScreen(Scanner userInput, ArrayList<Transaction> inkAndNeedleTransactions, String fileName) {

        //? Welcoming the user to the main menu
        System.out.print("\nWelcome to the main menu! Please choose an option below: ");

        //? This is a variable (boolean) to keep the menu looping
        boolean homeScreenRunning = true;

        //? Variable to count how many times the home screen has repeated / looped (welcome message / system out timing)
        int homeScreenIteration = 0;
        //? Looping the menu
        while (homeScreenRunning) {
            if (homeScreenIteration >= 1) {
                System.out.print("Please choose an option below: \n");
            }
            //? Printing options / cases to the user
            System.out.print("\n▪ D) Add Deposit\n▪ P) Make Payment (Debit)\n▪ L) Ledger - display the ledger screen\n▪ X) Exit - exit the application\n");
            //? Recording the user's choice; converting it to lower case, trimming, then storing it in a String variable.
            String homeScreenChoice = userInput.nextLine().toLowerCase().trim();
            //? Switch statement for the different choices
            switch (homeScreenChoice) {
                case "d":
                    System.out.print("\nDeposit amount (e.g. 105.97): $");
                    while (!userInput.hasNextDouble()) {
                        userInput.nextLine();
                        System.out.print("Please enter a number: $");
                    }
                    double depositAmount = userInput.nextDouble();
                    userInput.nextLine();
                    //? A while loop for if the user enters a negative number. Will persist until they enter a positive #
                    while (depositAmount <= 0) {
                        System.out.print("\nPlease enter a positive amount (e.g. 105.97): $");
                        //? Nested while loop within our previous while loop. This one checks to see if the user input
                        //? was in fact a Double like we want. If not (like a letter), it will refuse it and ask again.
                        //? Does this until the userInput hasNextDouble.
                        while (!userInput.hasNextDouble()) {
                            userInput.nextLine();
                            System.out.print("Please enter a number: $");
                        }
                        depositAmount = userInput.nextDouble();
                        userInput.nextLine();
                    }

                    System.out.print("\nWho is the deposit from?: ");
                    String depositVendor = userInput.nextLine().trim();
                    while (depositVendor.isEmpty()) {
                        System.out.print("Please designate who the deposit is from: ");
                        depositVendor = userInput.nextLine().trim();
                    }

                    System.out.print("\nPlease describe this deposit (optional): ");
                    String depositDescription = userInput.nextLine().trim();
                    if (depositDescription.isEmpty()) {
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
                    while (paymentVendor.isEmpty()) {
                        System.out.print("Please enter a recipient for the payment: ");
                        paymentVendor = userInput.nextLine().trim();
                    }

                    System.out.print("\nPlease describe this payment (optional): ");
                    String paymentDescription = userInput.nextLine().trim();
                    if (paymentDescription.isEmpty()) {
                        paymentDescription = "No Description";
                    }
                    LocalDate paymentDate = LocalDate.now();
                    LocalTime paymentTime = LocalTime.now();
                    //* For payments, we have to convert the number they provided to a negative number
                    //* Converting it ourselves instead of asking the user for the negative # saves confusion.
                    paymentAmount *= -1;
                    Transaction newPayment = new Transaction(paymentDate, paymentTime, paymentDescription, paymentVendor, paymentAmount);
                    inkAndNeedleTransactions.add(newPayment);
                    TransactionManager.saveTransaction(newPayment, fileName);
                    System.out.printf("\nA new payment amount of $%.2f has been saved.%n", (paymentAmount * -1));
                    break;
                case "l":
                    //* Call to ledgerScreen method for nested screen.
                    Screens.ledgerScreen(userInput, inkAndNeedleTransactions);
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

    //! A method for the ledgerScreen that you can choose from the homeScreen. We call to it from within the homeScreen
    //! method.
    //! It's public and static so that it can be called from anywhere, and it's a void because all it does is print
    //! information / run code it doesn't return anything.
    public static void ledgerScreen(Scanner userInput, ArrayList<Transaction> inkAndNeedleTransactions) {

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
                    printTable(newestFirstTransactions);
                    break;
                case "d":
                    System.out.println("Here is a list of all deposits: ");
                    ArrayList<Transaction> depositsOnly = new ArrayList<>();
                    for (Transaction transaction : newestFirstTransactions) {
                        if (transaction.checkIfDeposit()) {
                            depositsOnly.add(transaction);
                        }
                    }
                    printTable(depositsOnly);
                    break;
                case "p":
                    System.out.println("Here is a list of all payments: ");
                    ArrayList<Transaction> paymentsOnly = new ArrayList<>();
                    for (Transaction transaction : newestFirstTransactions) {
                        if (!transaction.checkIfDeposit()) {
                            paymentsOnly.add(transaction);
                        }
                    }
                    printTable(paymentsOnly);
                    break;
                case "r":
                    Screens.reportsScreen(userInput, newestFirstTransactions);
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

    //! Same deal as the ledgerScreen method. reportsScreen is nested within the ledgerScreen.
    //! It's public and static so that it can be called from anywhere, and it's a void because all it does is print
    //! information / run code it doesn't return anything.
    public static void reportsScreen(Scanner userInput, ArrayList<Transaction> newestFirstTransactions) {
        System.out.println("Welcome to the reports screen! Please choose an option below: ");

        //
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

                    // ? Create an ArrayList for us to feed to our printTable to do the printing from that method
                    ArrayList<Transaction> monthToDateList = new ArrayList<>();

                    for (Transaction transaction : newestFirstTransactions) {
                        LocalDate transactionsEntryDate = transaction.getDate();
                        if (transactionsEntryDate.getMonthValue() == today.getMonthValue()
                                && transactionsEntryDate.getYear() == today.getYear()) {
                            monthToDateList.add(transaction);
                        }
                    }
                    // ? Use printTable to display our ArrayList in the way we want
                    printTable(monthToDateList);
                    break;
                case 2:

//                    Previous Month

                    // ? Creating an ArrayList for us to feed to our printTable method.
                    ArrayList<Transaction> previousMonthList = new ArrayList<>();

                    for (Transaction transaction : newestFirstTransactions) {
                        LocalDate transactionsEntryDate = transaction.getDate();
                        if (transactionsEntryDate.getMonthValue() == (lastMonth.getMonthValue())
                                && transactionsEntryDate.getYear() == lastMonth.getYear()) {
                            previousMonthList.add(transaction);
                        }
                    }

                    // ? Use printTable to display our ArrayList in the way we want
                    printTable(previousMonthList);
                    break;

                case 3:

//                    Year To Date

                    // ? Create an ArrayList for us to feed to our printTable to do the printing from that method
                    ArrayList<Transaction> yearToDateList = new ArrayList<>();
                    for (Transaction transaction : newestFirstTransactions) {
                        LocalDate transactionsEntryDate = transaction.getDate();
                        if (transactionsEntryDate.getYear() == today.getYear()) {
                            yearToDateList.add(transaction);
                        }
                    }

                    // ? Use printTable to display our ArrayList in the way we want
                    printTable(yearToDateList);
                    break;

                case 4:
//                    Previous Year

                    // ? Create an ArrayList for us to feed to our printTable to do the printing from that method
                    ArrayList<Transaction> previousYearList = new ArrayList<>();
                    for (Transaction transaction : newestFirstTransactions) {
                        LocalDate transactionsEntryDate = transaction.getDate();
                        if (transactionsEntryDate.getYear() == lastYear.getYear()) {
                            previousYearList.add(transaction);
                        }
                    }

                    // ? Use printTable to display our ArrayList in the way we want
                    printTable(previousYearList);
                    break;
                case 5:
//                    Search By Vendor

                    System.out.print("\nEnter the vendor name you would like to search by: ");
                    String vendorName = userInput.nextLine().trim().toLowerCase();
                    while (vendorName.isEmpty()) {
                        System.out.print("Please enter a vendor name: ");
                        vendorName = userInput.nextLine().trim().toLowerCase();
                    }

                    // ? Create an ArrayList for us to feed to our printTable to do the printing from that method
                    ArrayList<Transaction> searchByVendorList = new ArrayList<>();
                    for (Transaction transaction : newestFirstTransactions) {
                        if (transaction.getVendor().toLowerCase().contains(vendorName)) {
                            searchByVendorList.add(transaction);
                        }
                    }

                    // ? Use printTable to display our ArrayList in the way we want
                    printTable(searchByVendorList);
                    break;

                case 6:
//                    Custom Search

                    System.out.println("Please provide the following information for a custom search: ");

                    // ? Printing to the user to provide a start date (format example given) or press Enter to skip
                    // ? Enter to skip leaves that search filter blank in their end custom search.
                    System.out.print("\nStart Date (yyyy-mm-dd, or press Enter to skip): ");

                    // ? Storing their answer in a String, and removing leading / trailing spaces.
                    String cSearchStartDate = userInput.nextLine().trim();

                    // ? Creating a boolean for input validation.
                    boolean validStartDate = false;

                    // ? While boolean validStartDate is false (false to start) - run the below code:
                    // ? if their answer is empty, we will store this as them pressing Enter to skip and thus their
                    // ? answer is a valid start date / option.
                    while (!validStartDate) {
                        if (cSearchStartDate.isEmpty()) {
                            validStartDate = true;

                            // ? If their answer is NOT empty, try to LocalDate parse their answer and if it runs
                            // ? successfully then they provided a valid date format. Then mark the boolean true
                        } else {
                            try {
                                LocalDate.parse(cSearchStartDate);
                                validStartDate = true;

                                // ? For the catch portion of this try/catch, if their answer is not empty
                                // ? but it does not parse correctly, then they entered an invalid date or typo.
                                // ? Prompting them to retry or press enter to skip.
                            } catch (DateTimeParseException e) {
                                System.out.print("Please enter a valid date (yyyy-mm-dd) or press Enter to skip: ");
                                cSearchStartDate = userInput.nextLine().trim();
                            }
                        }
                    }

                    // ? For end date explanation, refer to start date comments.
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

                    // ? Asking the user for a description for their custom search, and storing the answer.
                    System.out.print("\nDescription (or press Enter to skip): ");

                    // ? converting their input to lowercase, so when the transaction's description getter returns
                    // ? in lower case we don't have to worry about case mismatch.
                    String cSearchDescription = userInput.nextLine().trim().toLowerCase();

                    // ? Asking the user for a vendor for their custom search, and storing the answer.
                    System.out.print("\nVendor (or press Enter to skip): ");

                    // ? converting their input to lowercase, so when the transaction's vendor getter returns
                    // ? in lower case we don't have to worry about case mismatch.
                    String cSearchVendor = userInput.nextLine().trim().toLowerCase();

                    // ? Asking the user if they are searching for a deposit or a payment, or skipping this filter.
                    // ? Storing their answer in a variable, trimming it, and converting it to lower case so that
                    // ? if they type 'deposit' instead of 'Deposit' we do not have issues.
                    System.out.print("\nDeposit or Payment? (or press Enter to skip): ");
                    String cSearchEntryType = userInput.nextLine().trim().toLowerCase();

                    // ? While their answer for deposit or payment does not equal "deposit" and does not equal "payment"
                    // ? and is not empty, we can infer that they have not pressed enter to skip, and have not answered
                    // ? with either of the two options (maybe a typo, maybe a number, maybe intentional trying to
                    // ? produce an error. In this case, we will re prompt them until they give us one of the acceptable
                    // ? formats.
                    while (!cSearchEntryType.equals("deposit") && !cSearchEntryType.equals("payment") && !cSearchEntryType.isEmpty()) {
                        System.out.print("Please answer 'Deposit' or 'Payment' letter for letter:");
                        cSearchEntryType = userInput.nextLine().trim().toLowerCase();
                    }

                    System.out.print("\nAmount (e.g. 50.00 or press Enter to skip): $");
                    String cSearchAmount = userInput.nextLine().trim();

                    // ? This is the same idea as the start / end date boolean while loop
                    boolean validSearchAmount = false;

                    // ? While validSearchAmount is false (false to start), check if their answer is empty.
                    // ? If empty, we are accepting that as a valid input (Enter to skip)
                    while (!validSearchAmount) {
                        if (cSearchAmount.isEmpty()) {
                            validSearchAmount = true;

                            // ? Else, if their input is NOT empty, try to parse their answer for a double.
                            // ? If it parses correctly, their answer is acceptable and the while loop will end
                        } else {
                            try {
                                Double.parseDouble(cSearchAmount);
                                validSearchAmount = true;

                                // ? If it does NOT parse correctly, they input a wrong format, had a typo, or put
                                // ? a letter or something other. Will continue to prompt them until they provide
                                // ? a valid answer.
                            } catch (NumberFormatException e) {
                                System.out.print("Please enter a valid amount in a xx.xx format (e.g. 59.99): ");
                                cSearchAmount = userInput.nextLine().trim();
                            }
                        }
                    }

                    ArrayList<Transaction> customSearchList = new ArrayList<>();

                    // ? Run this loop for every transaction within our newestFirstTransactions ArrayList.
                    for (Transaction transaction : newestFirstTransactions) {

                        // ? For every transaction, boolean isMatch is only true at the start of each loop / pass
                        // ? isMatch is the flag for pass/fail from the bouncers (the if statement checks)
                        // ? that will kick out transactions that do not pass the check (set isMatch = false)
                        boolean isMatch = true;

                        //Description

                        // ? If their description input is NOT empty, run the nested if statement.
                        // ? If the description input IS empty, we will not run the nested if statement that decides
                        // ? whether that transaction is included in the search. That does not mean the transaction will
                        // ? be included in the result - it still has to get past the other checks.
                        if (!cSearchDescription.isEmpty())

                            // ? if the transaction's description (in lower case) does NOT contain the user's
                            // ? description search, isMatch = false.
                            if (!transaction.getDescription().toLowerCase().contains(cSearchDescription)) {

                                // ? isMatch = false means the bouncer is kicking this transaction out - it is not a
                                // ? match to the user's description input.
                                isMatch = false;
                            }

                        //Vendor

                        // ? If the user's search input for the vendor is NOT empty, AND the transaction's vendor
                        // ? (in lower case) does NOT contain the user's vendor search, then isMatch = false.
                        if (!cSearchVendor.isEmpty() && !transaction.getVendor().toLowerCase().contains(cSearchVendor)) {

                            // ? if isMatch = false, it does not match the user's search input and the transaction will
                            // ? not be included in the search result
                            isMatch = false;
                        }

                        //Deposit?

                        // ? If the user's entry type search (that we converted to lower case earlier) equals "deposit"
                        // ? (they want deposits) then will run the nested if statement.
                        if (cSearchEntryType.equals("deposit")) {

                            // ? If the transaction, running the checkIfDeposit method, (is the amount greater than 0)
                            // ? returns false then isMatch = false
                            // ? In layman's terms, if the transaction being checked in this loop is not a deposit
                            // ? (negative number) then the bouncer is kicking it out because the user wants deposits,
                            // ? not payments.
                            if (!transaction.checkIfDeposit()) {

                                // ? if isMatch = false, it does not match the user's search input and the transaction will
                                // ? not be included in the search result
                                isMatch = false;
                            }
                        }

                        //Payment?

                        // ? If the user's entry type search (that we converted to lower case earlier) equals "payment"
                        // ? (they want payments) then will run the nested if statement.
                        if (cSearchEntryType.equals("payment")) {

                            // ? If the transaction, running the checkIfDeposit method, (is the amount greater than 0)
                            // ? returns TRUE (is greater than 0) then isMatch = false;
                            // ? so in layman's terms, if the transaction being checked in this loop
                            // ? is a deposit (positive number) then the bouncer is kicking it out because the user
                            // ? wants payments, not deposits.
                            if (transaction.checkIfDeposit()) {

                                // ? if isMatch = false, it does not match the user's search input and the transaction will
                                // ? not be included in the search result
                                isMatch = false;
                            }
                        }

//                        Start Date:

                        // ? if the user's start date input is NOT empty, then try and LocalDate parse their answer
                        // ? and store it in LocalDate variable startDate.
                        if (!cSearchStartDate.isEmpty()) {
                            LocalDate startDate = LocalDate.parse(cSearchStartDate);

                            // ? After parsing, if the current transaction's date is before the start date,
                            // ? isMatch = false which means this transaction will not be included in the search
                            // ? result.
                            if (transaction.getDate().isBefore(startDate)) {
                                isMatch = false;
                            }
                        }

//                        End Date:

                        // ? Same idea for the end date. If the user's end date input is NOT empty, then try and
                        // ? LocalDate parse their answer and store it in LocalDate variable endDate.
                        if (!cSearchEndDate.isEmpty()) {
                            LocalDate endDate = LocalDate.parse(cSearchEndDate);

                            // ? After parsing, if the current transaction's date is after the end date, isMatch = false
                            // ? which means this transaction will not be included in the search result.
                            if (transaction.getDate().isAfter(endDate)) {
                                isMatch = false;
                            }
                        }

//                        Amount:

                        // ? If the user's search amount input is NOT empty, then try and parseDouble their answer
                        // ? and store it in searchAmount variable as a double.
                        if (!cSearchAmount.isEmpty()) {
                            double searchAmount = Double.parseDouble(cSearchAmount);

                            // ? After parsing, if the current transaction's amount (Math.abs turns the value to positive)
                            // ? is not equal to the user's input, then isMatch = false. This means this transaction
                            // ? will not be included in the search result.
                            if (Math.abs(searchAmount) != (Math.abs(transaction.getAmount()))) {
                                isMatch = false;
                            }
                        }

                        // ? After running the transactions through all checks, find the ones that returned isMatch = true
                        // ? For all transactions that are a match, add them to the empty customSearchList ArrayList.
                        if (isMatch) {
                            customSearchList.add(transaction);
                        }

                    }

                    printTable(customSearchList);
                    break;

                // ? If the user selects 0, the loop for the reportsScreen will end.
                case 0:
                    reportsScreenRunning = false;
                    break;

                // ? If the user does not select from the valid options, it will prompt them to choose a valid
                // ? choice
                default:
                    System.out.println("Please enter a valid option. Refer to the numbers next to the options.");
                    break;
            }
            reportsScreenIteration++;
        }
    }

}