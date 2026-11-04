package com.pluralsight;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        //TODO: This Main class will start the app and call to each of my methods.

        //!Creating a scanner for accepting and storing user input
        Scanner userInput = new Scanner(System.in);
        //! Storing file name in a variable.
        String fileName = "transactions.csv";
        //! Using the loadTransactions method from the TransactionManager class to create a new ArrayList
        //! Called inkAndNeedleTransactions. It reads the lines from the transactions.csv, splits the lines
        //! and we store the different sections into variables and build a transaction out of those variables
        ArrayList<Transaction> inkAndNeedleTransactions = TransactionManager.loadTransactions(fileName);
        //! Calling my welcome banner method. It provides a welcome message w/ the date and time.
        Screens.welcomeBanner();
        //! Calling the homeScreen method and providing it a scanner, an ArrayList, and the transactions.csv
        //! This is a method for all of the user action menus / screens. homeScreen, ledgerScreen and reportsScreen are
        //! nested within.
        Screens.homeScreen(userInput, inkAndNeedleTransactions, fileName);

    } //! main method cBracket

} //! Main class cBracket
