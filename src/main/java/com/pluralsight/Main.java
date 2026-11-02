package com.pluralsight;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        //TODO: This Main class will start the app and call to each of my methods.
        Scanner userInput = new Scanner(System.in);
        String fileName = "transactions.csv";

        ArrayList<Transaction> inkAndNeedleTransactions = TransactionManager.loadTransactions(fileName);

        Screens.welcomeBanner();

        Screens.homeScreen(userInput, inkAndNeedleTransactions, fileName);

    } //! main method cBracket

} //! Main class cBracket
