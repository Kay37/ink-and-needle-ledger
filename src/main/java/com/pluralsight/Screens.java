package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.String.format;

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
                   Welcome back! Today's date and time are %s
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
                System.out.print("Please choose an option below: ");
            }
            //* Printing options / cases to the user
            System.out.printf("\n▪ D) Add Deposit\n▪ P) Make Payment (Debit)\n▪ L) Ledger - display the ledger screen\n▪ X) Exit - exit the application\n");
            //* Recording the user's choice; converting it to lower case, trimming, then storing it in a String variable.
            String homeScreenChoice = userInput.nextLine().toLowerCase().trim();
            //* Switch statement for the different choices
            switch (homeScreenChoice) {
                case "d":
                    System.out.print("\nDeposit amount (e.g. 105.97): $");
                    double depositAmount = userInput.nextDouble();
                    //* A while loop for if the user enters a negative number. Will persist until they enter a positive #
                    while (depositAmount <= 0) {
                        System.out.print("\nPlease enter a positive amount (e.g. 105.97): $");
                        depositAmount = userInput.nextDouble();
                    }
                    //* Absorbing memory of enter key pressed when storing the user's Double response
                    userInput.nextLine();
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
                    break;
                case "p":
                    System.out.print("\nPayment amount (e.g. 120.00): $");
                    double paymentAmount = userInput.nextDouble();
                    while (paymentAmount <= 0) {
                        System.out.print("\nPlease enter a positive amount (e.g. 120.00): $");
                        paymentAmount = userInput.nextDouble();
                    }
                    userInput.nextLine();
                    System.out.print("\nWho is the payment to?: ");
                    String paymentVendor = userInput.nextLine().trim();
                    while (paymentVendor.length() == 0) {
                        System.out.print("Please enter a recipient for the payment.");
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
        System.out.println("Welcome to the ledger screen! Please choose an option below: ");
        boolean ledgerScreenRunning = true;
        while (ledgerScreenRunning) {
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
                    break;
                case "d":
                    break;
                case "p":
                    break;
                case "r":
                    break;
                case "h":
                    ledgerScreenRunning = false;
                    break;
                default:
                    System.out.println("Please enter a valid option. Refer to the letters next to the options.");
                    break;
            }
        }

//        o A) All - Display all entries
//        o D) Deposits - Display only the entries that are deposits into the account
//        o P) Payments - Display only the negative entries (or payments)
//        o R) Reports - A new screen that allows the user to run pre-defined reports or
//        to run a custom search
//▪ 1) Month To Date
//▪ 2) Previous Month
//▪ 3) Year To Date
//▪ 4) Previous Year
//▪ 5) Search by Vendor - prompt the user for the vendor name and
//        display all entries for that vendor
//▪ 0) Back - go back to the Ledger page
//        o H) Home - go back to the home page
    }
}