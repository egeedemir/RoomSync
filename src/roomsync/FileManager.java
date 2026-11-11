
package roomsync;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class FileManager {

    public static boolean saveMembers(ArrayList<Member> members) {
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of("members.txt"))) {

            for (Member member : members) {
                writer.write(member.getId() + "," + member.getName());
                writer.newLine();
            }

            System.out.println("Members saved successfully!");
            return true;

        } catch (IOException e) {
            System.out.println("Error saving members: " + e.getMessage());
            return false;
        }
    }

    public static boolean saveExpenses(ArrayList<Expense> expenses) {
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of("expenses.txt"))) {

            for (Expense expense : expenses) {
                writer.write(
                        expense.getId() + "|" +
                                expense.getAmount() + "|" +
                                expense.getPaidBy().getId() + "|" +
                                expense.getDescription()
                );

                writer.newLine();
            }

            System.out.println("Expenses saved successfully!");
            return true;

        } catch (IOException e) {
            System.out.println("Error saving expenses: " + e.getMessage());
            return false;
        }
    }

    public static ArrayList<Member> loadMembers() {
        ArrayList<Member> members = new ArrayList<>();
        Path path = Path.of("members.txt");

        if (!Files.exists(path)) {
            return members;
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;

            while ((line = reader.readLine()) != null) {
                try {
                    String[] parts = line.split(",", 2);

                    if (parts.length != 2 || parts[1].trim().isEmpty()) {
                        throw new IllegalArgumentException();
                    }

                    int id = Integer.parseInt(parts[0].trim());
                    String name = parts[1].trim();

                    if (id <= 0) {
                        throw new IllegalArgumentException();
                    }

                    members.add(new Member(id, name));

                } catch (IllegalArgumentException e) {
                    System.out.println("Skipping invalid member: " + line);
                }
            }

        } catch (IOException e) {
            throw new IllegalStateException("Error loading members!", e);
        }

        return members;
    }

    public static ArrayList<Expense> loadExpenses(Room room) {
        ArrayList<Expense> expenses = new ArrayList<>();
        Path path = Path.of("expenses.txt");

        if (!Files.exists(path)) {
            return expenses;
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;

            while ((line = reader.readLine()) != null) {
                try {
                    String[] parts = line.split("\\|", 4);

                    if (parts.length != 4 || parts[3].trim().isEmpty()) {
                        throw new IllegalArgumentException();
                    }

                    int id = Integer.parseInt(parts[0].trim());
                    BigDecimal amount = new BigDecimal(parts[1].trim());
                    int memberId = Integer.parseInt(parts[2].trim());
                    String description = parts[3].trim();

                    Member paidBy = room.getMemberById(memberId);

                    if (id <= 0 || paidBy == null) {
                        throw new IllegalArgumentException();
                    }

                    expenses.add(new Expense(id, description, amount, paidBy));

                } catch (IllegalArgumentException e) {
                    System.out.println("Skipping invalid expense: " + line);
                }
            }

        } catch (IOException e) {
            throw new IllegalStateException("Error loading expenses!", e);
        }

        return expenses;
    }
}
