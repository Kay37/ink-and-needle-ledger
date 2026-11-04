package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Transaction {

    private LocalDate date;
    private LocalTime time;
    private String description;
    private String vendor;
    private double amount;

    public Transaction(LocalDate date, LocalTime time, String description, String vendor, double amount) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }


    //! Helper Methods:
    //! Will help later and determine if an amount is a positive number or negative number so we can tell if it's a
    //! deposit or withdrawal when reading from the csv
    public boolean checkIfDeposit() {
        //? Returns true or false to whether the amount is greater than 0 or not. Greater than 0 = deposit.
        //? NOT greater than 0 means it's a payment.
        return this.amount > 0;
    }

    //! A helper method to format ledger entries for display to the screen / user (readable / assignment given format)
    public String transactionDisplayFormat(int entryPosition) {
        //? DateTimeFormatter so we can receive the format of the Date & Time that we want.
        DateTimeFormatter timeFormatter =
                DateTimeFormatter.ofPattern("HH:mm:ss");
        //? formattedTime variable that stores the current time and formats it.
        String formattedTime = this.getTime().format(timeFormatter);
        //? formattedString variable that takes the formatted current time and combines it with the other getters
        //? that the user will want to see for ledger entries when displaying them.
        String formattedString = String.format("%d) %s|%s|%s|%s|%.2f", entryPosition, this.getDate(), formattedTime, this.getDescription(), this.getVendor(), this.getAmount());
        return formattedString;
    }

    //!Building a method to easily format each line written into the transactions.csv file
    //! This will place the correct format & order for the assignment for date, then time, then description, then vendor,
    //! then amount.
    public String toCsvLine() {
        //? DateTimeFormatter so we can receive the format of the Date & Time that we want.
        DateTimeFormatter timeFormatter =
                DateTimeFormatter.ofPattern("HH:mm:ss");
        //? formattedTime variable that stores the current time and formats it.
        String formattedTime = time.format(timeFormatter);
        //? formattedAmount variable that stores the fashion in which we want amount to be displayed.
        String formattedAmount = String.format("%.2f", amount);
        //? returning correct format & order to wherever it is called (when we are writing a line to the file)
        return date + "|" + formattedTime + "|" + description + "|" + vendor + "|" + formattedAmount + "\n";
    }

}
