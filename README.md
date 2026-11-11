# RoomSync

RoomSync is a Java console application designed to help roommates manage shared expenses, track individual balances, and calculate debt settlements.

The project focuses on object-oriented programming, financial calculations, and file handling using core Java.

## Features

- **Member Management:** Add and view household members with unique IDs.
- **Expense Tracking:** Record expenses with descriptions, amounts, and the member who paid.
- **Equal Expense Splitting:** Calculate each member's share of total household expenses.
- **Balance Calculation:** Track how much each member owes or should receive.
- **Debt Settlement:** Generate payment suggestions between debtors and creditors.
- **Accurate Money Handling:** Use `BigDecimal` to avoid floating-point errors and handle cent-level rounding.
- **File Persistence:** Save and reload members and expenses using local text files.
- **Input Validation:** Handle invalid menu selections, member IDs, names, and expense amounts.

## Technologies Used

- Java
- Java Collections (`ArrayList`, `HashMap`)
- Object-Oriented Programming (OOP)
- `BigDecimal`
- File I/O (`BufferedReader`, `BufferedWriter`)
- Exception Handling

## Project Structure

```text
RoomSync/
├── src/
│   └── roomsync/
│       ├── Main.java
│       ├── Member.java
│       ├── Room.java
│       ├── Expense.java
│       └── FileManager.java
├── .gitignore
└── README.md
```

## How It Works

1. Create household members.
2. Add shared expenses and select who paid.
3. View the total expenses and individual balances.
4. Generate suggested payments to settle outstanding debts.
5. Exit the application to save the data automatically.
6. Restart the application to reload previously saved members and expenses.

### Example

Three roommates share a total expense of 900 TL.

| Member | Amount Paid | Net Balance |
|---|---:|---:|
| Ege | 600 TL | +300 TL |
| Ali | 300 TL | 0 TL |
| Deniz | 0 TL | -300 TL |

RoomSync calculates the following settlement:

```text
===== DEBT SETTLEMENT =====
Deniz pays Ege 300.00 TL
```

Positive balances indicate money to be received, while negative balances indicate money owed.

## Running the Project

**Requirements:** Java JDK 21 or later.

### Using IntelliJ IDEA

1. Clone the repository.
2. Open the project in IntelliJ IDEA.
3. Configure the project SDK.
4. Run `src/roomsync/Main.java`.

### Using the Terminal

From the project directory:

```bash
javac -d out src/roomsync/*.java
java -cp out roomsync.Main
```

## Data Storage

RoomSync stores its data in two local files:

- `members.txt` — Household member IDs and names.
- `expenses.txt` — Expense IDs, amounts, payer IDs, and descriptions.

The files are loaded when the application starts and saved when the user exits normally.

These files are excluded from Git version control to avoid publishing personal expense data.

## Current Limitations

- Expenses are shared equally among all household members.
- Settlement suggestions are calculated but are not recorded as completed payments.
- The application currently uses a command-line interface.
- Data is saved on normal exit, not after every change.
- The text-file storage format is intended for simple local usage.

## Future Improvements

- Graphical user interface using Java Swing
- Custom expense splitting between selected members
- Payment history and completed settlement tracking
- Automated unit tests
- More robust local data storage

## Purpose

RoomSync was developed as a personal Java project to practice object-oriented design, data structures, financial calculations, and file persistence while building a practical application for shared living expenses.