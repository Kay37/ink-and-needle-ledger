package com.pluralsight;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class TransactionManager {

    //! Method for reading from transactions.csv and storing the lines in an ArrayList
    public static ArrayList<Transaction> loadTransactions(String fileName) {

        //? Using an array list to store our inkAndNeedle transactions
        ArrayList<Transaction> transactionsList = new ArrayList<>();

        //? Using a fileReader wrapped in a BufferedReader to pull the info from transactions.csv / fileName String
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));

            //? Variable representing each line in the csv
            String csvLine;
            //? Reading and discarding header
            reader.readLine();
            //? Loop that goes line by line in the csv, splits the lines into sections, and stores info into variables
            //? Those variables are then used to build a new Transaction object and add it to our ArrayList
            while ((csvLine = reader.readLine()) != null) {
                String[] lineParts = csvLine.split("\\|");
                LocalDate transactionDate = LocalDate.parse(lineParts[0]);
                LocalTime transactionTime = LocalTime.parse(lineParts[1]);
                String transactionDescription = lineParts[2];
                String transactionVendor = lineParts[3];
                double transactionAmount = Double.parseDouble(lineParts[4]);
                Transaction transaction = new Transaction(transactionDate, transactionTime, transactionDescription, transactionVendor, transactionAmount);
                transactionsList.add(transaction);
            }
            //? Closing FileReader because we are a good person
            reader.close();
            //? Catch portion of the try & catch in case main argument / code we want to run fails.
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Error reading file: " + fileName);
        }
        //? Returning the ArrayList to wherever it was called from
        return transactionsList;
    }

    //! Method to write new transactions (deposits or payments) to the fileName / transactions.csv
    //! will be singular "newTransaction"
    public static void saveTransaction(Transaction newTransaction, String fileName) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
            //? FileWriter will call the toCsvLine method to format the new transaction correctly and then write it to
            //? the file
            writer.write(newTransaction.toCsvLine());
            //? We are good people so we close our writer.
            writer.close();
            //? Catching error / exception if it cannot be written to the file.
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Error writing new transaction to file: " + fileName);
        }
    }

}
