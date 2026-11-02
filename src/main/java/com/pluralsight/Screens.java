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
        //*This is a variable (boolean) to keep the menu looping
        boolean menuRunning = true;

        //* Looping the menu
        while (menuRunning) {
            System.out.printf("\nPlease choose an option below: ");
            System.out.printf("\n▪ D) Add Deposit\n▪ P) Make Payment (Debit)\n▪ L) Ledger - display the ledger screen\n▪ X) Exit - exit the application\n");
            String homeScreenChoice = userInput.nextLine().toLowerCase().trim();

            switch (homeScreenChoice) {
                case "d":
                    System.out.print("\nDeposit amount (e.g. 105.97): $");
                    double depositAmount = userInput.nextDouble();
                    while (depositAmount <= 0) {
                        System.out.print("\nPlease enter a positive amount (e.g. 105.97): $");
                        depositAmount = userInput.nextDouble();
                    }
                    userInput.nextLine();
                    System.out.print("\nWho is the deposit from?: ");
                    String depositVendor = userInput.nextLine().trim();

                    System.out.print("\nPlease describe this deposit: (N/A if you do not want to provide a description): ");
                    String depositDescription = userInput.nextLine().trim();

                    LocalDate depositDate = LocalDate.now();
                    LocalTime depositTime = LocalTime.now();
                    Transaction newDeposit = new Transaction(depositDate, depositTime, depositDescription, depositVendor, depositAmount);
                    inkAndNeedleTransactions.add(newDeposit);
                    TransactionManager.saveTransaction(newDeposit,  fileName);
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
                    System.out.print("\nPlease describe this payment: (N/A if you do not want to provide a description): ");
                    String paymentDescription = userInput.nextLine().trim();
                    LocalDate paymentDate = LocalDate.now();
                    LocalTime paymentTime = LocalTime.now();
                    paymentAmount *= -1;
                    Transaction newPayment = new Transaction(paymentDate, paymentTime, paymentDescription, paymentVendor, paymentAmount);
                    inkAndNeedleTransactions.add(newPayment);
                    TransactionManager.saveTransaction(newPayment, fileName);
                    break;
                default:

            }
        }
    }
}