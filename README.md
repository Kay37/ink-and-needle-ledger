# Ink & Needle Ledger

A command-line ledger for a tattoo studio, written in Java. It keeps track of every dollar
that comes in (tattoo sessions, booking deposits, tips, merch) and every dollar that goes out
(rent, ink, needles, supplies), and lets you look back at it with reports and searches.

Built as Capstone 1 for the Year Up United Java Focus Academy.

<!-- TODO: add a screenshot of the home screen once the starter screen is done -->
<!-- ![Ink & Needle Ledger home screen](images/home-screen.png) -->

## Features

### Home screen
| Option | What it does |
|---|---|
| **D) Add Deposit** | Asks for the deposit details and saves it to `transactions.csv` |
| **P) Make Payment** | Asks for the payment details and saves it as a negative amount |
| **L) Ledger** | Opens the ledger screen |
| **X) Exit** | Closes the app |

### Ledger screen (newest entries first)
| Option | What it does |
|---|---|
| **A) All** | Shows every transaction |
| **D) Deposits** | Shows only money coming in |
| **P) Payments** | Shows only money going out |
| **R) Reports** | Opens the reports screen |
| **H) Home** | Goes back to the home screen |

### Reports screen
| Option | What it does |
|---|---|
| **1) Month To Date** | Transactions from the 1st of this month until today |
| **2) Previous Month** | Every transaction from last month |
| **3) Year To Date** | Transactions from January 1st until today |
| **4) Previous Year** | Every transaction from last year |
| **5) Search by Vendor** | Every transaction for one vendor or client |
| **6) Custom Search** | Filter by any mix of dates, description, vendor, deposit or payment, and amount |
| **0) Back** | Goes back to the ledger screen |

## The transaction file

All transactions are read from and saved to `transactions.csv` in the project folder.
Each line is one transaction:

```
date|time|description|vendor|amount
2026-10-24|13:00:38|Blackwork sleeve session|Luis Castillo|490.00
2026-10-01|09:00:00|Studio rent|Riverside Property Mgmt|-1400.00
```

- Positive amounts are deposits, negative amounts are payments.
- The sample file holds made-up studio data from January 2025 to early November 2026,
  so every report has something to show.

## How to run it

**You need:** Java 17 or newer (the project uses Amazon Corretto 17) and IntelliJ IDEA.

1. Clone the repository:
   ```bash
   git clone https://github.com/Kay37/ink-and-needle-ledger.git
   ```
2. Open the `ink-and-needle-ledger` folder in IntelliJ IDEA.
3. Make sure `transactions.csv` is in the project folder (next to `pom.xml`).
4. Open `src/main/java/com/pluralsight/Main.java` and click the green **Run** arrow.
5. Pick an option by typing its letter or number and pressing **Enter**.

## How the code is organized

| Class | Its job |
|---|---|
| `Main` | Starts the app |
| `Transaction` | One transaction: date, time, description, vendor and amount |
| `TransactionManager` | Reads all transactions from the CSV file and saves new ones |
| `Screens` | The home, ledger and reports screens: menus, prompts and results |

## An interesting piece of code

**Custom Search** (reports option 6) filters by any mix of start date, end date, description,
vendor, deposit or payment, and amount, and every one of those can be skipped. The trick that
makes them all optional: each transaction starts out as a match, and each filter that was
filled in can only knock it out.

```java
for (Transaction transaction : newestFirstTransactions) {
    boolean isMatch = true;   // a match until a filter says otherwise

    if (!cSearchVendor.isEmpty() && !transaction.getVendor().toLowerCase().contains(cSearchVendor)) {
        isMatch = false;
    }
    if (!cSearchStartDate.isEmpty()) {
        LocalDate startDate = LocalDate.parse(cSearchStartDate);
        if (transaction.getDate().isBefore(startDate)) {
            isMatch = false;
        }
    }
    if (!cSearchAmount.isEmpty()) {
        double searchAmount = Double.parseDouble(cSearchAmount);
        if (Math.abs(searchAmount) != Math.abs(transaction.getAmount())) {
            isMatch = false;
        }
    }
    // ...description, end date, and deposit or payment work the same way

    if (isMatch) {
        customSearchList.add(transaction);
    }
}
printTable(customSearchList);
```

Why I like it:
- A skipped filter is never checked, so it can't knock anything out. Without this pattern I'd
  need an `if` for every combination of filters. Adding a new filter is just one more `if`.
- `Math.abs` on both sides means typing `320` also finds a `-320.00` payment.
- Bad dates and amounts are caught earlier with `try`/`catch` and asked for again, so the
  parsing inside the loop never crashes.

## What I learned

For this capstone, I learned a lot about organization and time management in coding.
In past exercises, I didn't have a clear method for deciding where to put my time and
effort. The Project Board helped a lot: I broke the work into clear tasks and moved each
one through stages until it was done.

I also learned to build an MVP (minimum viable product) first and polish it after.
Get it working first, then make it pretty. That helped tons.

## How I used AI

I wrote the Java code myself. I used Claude (an AI assistant) as a coach: it explained
error messages and Java ideas, asked questions to point me toward fixes, reviewed code
I had already written, and showed me similar examples when I was stuck on syntax. With my instructor's OK, Claude also wrote this README and generated
the sample data in `transactions.csv`.

## Author

Kimball ([@Kay37](https://github.com/Kay37))
