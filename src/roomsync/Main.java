
package roomsync;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Room room = new Room("Our House");

        int nextMemberId = 1;
        int nextExpenseId = 1;
        boolean running = true;

        // Load saved members
        ArrayList<Member> savedMembers = FileManager.loadMembers();

        for (Member member : savedMembers) {
            try {
                room.addMember(member);

                if (member.getId() >= nextMemberId) {
                    nextMemberId = member.getId() + 1;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Skipping duplicate member: " + member.getName());
            }
        }

        // Load saved expenses
        ArrayList<Expense> savedExpenses = FileManager.loadExpenses(room);

        for (Expense expense : savedExpenses) {
            try {
                room.addExpense(expense);

                if (expense.getId() >= nextExpenseId) {
                    nextExpenseId = expense.getId() + 1;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Skipping duplicate expense: " + expense.getId());
            }
        }

        System.out.println("RoomSync loaded successfully!");

        while (running) {
            System.out.println("\n===== ROOMSYNC =====");
            System.out.println("1. Add Member");
            System.out.println("2. Show Members");
            System.out.println("3. Add Expense");
            System.out.println("4. Show Expenses");
            System.out.println("5. Show Balances");
            System.out.println("6. Settle Debts");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            int choice;

            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter member name: ");
                    String name = scanner.nextLine().trim();

                    if (name.isEmpty() || name.contains(",")) {
                        System.out.println("Invalid member name!");
                        break;
                    }

                    boolean exists = false;

                    for (Member member : room.getMembers()) {
                        if (member.getName().equalsIgnoreCase(name)) {
                            exists = true;
                            break;
                        }
                    }

                    if (exists) {
                        System.out.println("This member already exists!");
                        break;
                    }

                    room.addMember(new Member(nextMemberId, name));
                    nextMemberId++;

                    System.out.println("Member added successfully!");
                    break;

                case 2:
                    room.showMembers();
                    break;

                case 3:
                    if (room.getMembers().isEmpty()) {
                        System.out.println("Add a member first!");
                        break;
                    }

                    System.out.print("Enter expense description: ");
                    String description = scanner.nextLine().trim();

                    if (description.isEmpty()) {
                        System.out.println("Description cannot be empty!");
                        break;
                    }

                    System.out.print("Enter amount: ");
                    BigDecimal amount;

                    try {
                        amount = new BigDecimal(scanner.nextLine().trim());

                        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                            System.out.println("Amount must be positive!");
                            break;
                        }

                        if (amount.scale() > 2) {
                            System.out.println("Amount cannot have more than 2 decimal places!");
                            break;
                        }

                        amount = amount.setScale(2, RoundingMode.UNNECESSARY);

                    } catch (NumberFormatException e) {
                        System.out.println("Invalid amount!");
                        break;
                    }

                    System.out.println("\nMembers:");
                    room.showMembers();

                    System.out.print("Who paid? Enter member ID: ");
                    int memberId;

                    try {
                        memberId = Integer.parseInt(scanner.nextLine().trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid member ID!");
                        break;
                    }

                    Member paidBy = room.getMemberById(memberId);

                    if (paidBy == null) {
                        System.out.println("Member not found!");
                        break;
                    }

                    room.addExpense(new Expense(
                            nextExpenseId,
                            description,
                            amount,
                            paidBy
                    ));

                    nextExpenseId++;

                    System.out.println("Expense added successfully!");
                    break;

                case 4:
                    System.out.println("\n===== EXPENSES =====");
                    room.showExpenses();
                    break;

                case 5:
                    room.showBalances();
                    break;

                case 6:
                    room.settleDebts();
                    break;

                case 0:
                    boolean membersSaved = FileManager.saveMembers(room.getMembers());
                    boolean expensesSaved = FileManager.saveExpenses(room.getExpenses());

                    if (membersSaved && expensesSaved) {
                        System.out.println("All data saved!");
                    } else {
                        System.out.println("Some data could not be saved!");
                    }

                    running = false;
                    System.out.println("Goodbye!");
                    break;

                default:
                    System.out.println("Invalid option!");
            }
        }

        scanner.close();
    }
}
