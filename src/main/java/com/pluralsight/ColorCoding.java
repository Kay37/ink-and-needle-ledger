package com.pluralsight;

// ! This is a class with public static final variables for color coding output in the terminal.
public class ColorCoding {
    // ? This constant resets a String / text to normal terminal coloring
    public static final String RESET = "\u001B[0m";
    // ? I intend to use the GREEN constant for deposits / money in
    public static final String GREEN = "\u001B[32m";
    // ? I intend to use the RED constant for payments / money out
    public static final String RED = "\u001B[31m";
    // ? I intend to use the YELLOW constant for warning messages
    public static final String YELLOW = "\u001B[33m";
    // ? I intend to use the GRAY constant for table structure
    // ? So dividers / columns / ~ ~ ~ lines will be with a dimmer color
    // ? so as not to draw attention away from the information
    public static final String GRAY = "\u001B[90m";
    // ? I intend to use the PIPE constant for dividers between data / information. Pipe is "|" + GRAY.
    public static final String PIPE = ColorCoding.GRAY + "|" + ColorCoding.RESET;
}
