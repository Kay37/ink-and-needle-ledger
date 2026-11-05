package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static com.pluralsight.ColorCoding.PIPE;

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

        //* formatting the description and vendor to fit within my width constraints below
        String formattedDescription = this.getDescription();
        if (formattedDescription.length() > 36) {
            formattedDescription = formattedDescription.substring(0, 35) + "…";
        }
        String formattedVendor = this.getVendor();
        if (formattedVendor.length() > 20) {
            formattedVendor = formattedVendor.substring(0, 19) + "…";
        }

        // * a number before a placeholder indicates a width amount. a positive number indicates a width pushing it to
        // * the right. -26 would be to the left.

        // * On a second pass for this method, I'm adding color coding to our formattedString. To do this I'm splitting
        // * up the formatted string into two sections - one all the way up until the amount's | and then everything
        // * after. Then I'm checking if the amount being printed is a deposit or a payment and applying
        // * green or red to the amount, depending on the type.
        String formattedStringMinusAmount = String.format("%3d) %10s%s%8s%s%-36s%s%-20s%s", entryPosition, this.getDate(), PIPE, formattedTime, PIPE, formattedDescription, PIPE, formattedVendor, PIPE);
        String formattedAmount = String.format("%,11.2f", this.getAmount());
        String amountColor;
        if (checkIfDeposit()) {
            amountColor = ColorCoding.GREEN;
        } else {
            amountColor = ColorCoding.RED;
        }
        return String.format("%s%s%s%s", formattedStringMinusAmount, amountColor, formattedAmount, ColorCoding.RESET);
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
