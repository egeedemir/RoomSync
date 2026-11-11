
package roomsync;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

public class Room {
    private String name;
    private ArrayList<Member> members;
    private ArrayList<Expense> expenses;

    public Room(String name) {
        this.name = name;
        members = new ArrayList<>();
        expenses = new ArrayList<>();
    }

    public void addMember(Member member) {
        for (Member m : members) {
            if (m.getId() == member.getId() ||
                    m.getName().equalsIgnoreCase(member.getName())) {
                throw new IllegalArgumentException("Member already exists!");
            }
        }

        members.add(member);
    }

    public void showMembers() {
        for (Member m : members) {
            System.out.println(m);
        }
    }

    public ArrayList<Member> getMembers() {
        return new ArrayList<>(members);
    }

    public Member getMemberById(int id) {
        for (Member member : members) {
            if (member.getId() == id) {
                return member;
            }
        }

        return null;
    }

    public void addExpense(Expense expense) {
        if (getMemberById(expense.getPaidBy().getId()) == null) {
            throw new IllegalArgumentException("Member not found!");
        }

        for (Expense e : expenses) {
            if (e.getId() == expense.getId()) {
                throw new IllegalArgumentException("Expense already exists!");
            }
        }

        expenses.add(expense);
    }

    public ArrayList<Expense> getExpenses() {
        return new ArrayList<>(expenses);
    }

    public void showExpenses() {
        for (Expense e : expenses) {
            System.out.println(e);
        }
    }

    public BigDecimal getTotalExpenses() {
        BigDecimal total = BigDecimal.ZERO;

        for (Expense e : expenses) {
            total = total.add(e.getAmount());
        }

        return total;
    }

    public BigDecimal getTotalPaid(Member member) {
        BigDecimal total = BigDecimal.ZERO;

        for (Expense e : expenses) {
            if (e.getPaidBy().getId() == member.getId()) {
                total = total.add(e.getAmount());
            }
        }

        return total;
    }

    public BigDecimal getShare(Member member) {
        if (members.isEmpty()) {
            return BigDecimal.ZERO.setScale(2);
        }

        int index = -1;

        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).getId() == member.getId()) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            throw new IllegalArgumentException("Member not found!");
        }

        long cents = getTotalExpenses().movePointRight(2).longValueExact();

        long baseShare = cents / members.size();
        long remainder = cents % members.size();

        long memberShare = baseShare;

        if (index < remainder) {
            memberShare++;
        }

        return BigDecimal.valueOf(memberShare, 2);
    }

    public BigDecimal getBalance(Member member) {
        return getTotalPaid(member).subtract(getShare(member));
    }

    public void showBalances() {
        System.out.println("\n===== BALANCES =====");

        for (Member member : members) {
            BigDecimal balance = getBalance(member);

            if (balance.compareTo(BigDecimal.ZERO) > 0) {
                System.out.println(member.getName() + ": +" + balance + " TL");
            } else {
                System.out.println(member.getName() + ": " + balance + " TL");
            }
        }
    }

    public void settleDebts() {
        ArrayList<Member> debtors = new ArrayList<>();
        ArrayList<Member> creditors = new ArrayList<>();
        HashMap<Integer, BigDecimal> balances = new HashMap<>();

        for (Member member : members) {
            BigDecimal balance = getBalance(member);

            balances.put(member.getId(), balance);

            if (balance.compareTo(BigDecimal.ZERO) < 0) {
                debtors.add(member);
            } else if (balance.compareTo(BigDecimal.ZERO) > 0) {
                creditors.add(member);
            }
        }

        System.out.println("\n===== DEBT SETTLEMENT =====");

        if (debtors.isEmpty()) {
            System.out.println("No debts!");
            return;
        }

        int i = 0;
        int j = 0;

        while (i < debtors.size() && j < creditors.size()) {
            Member debtor = debtors.get(i);
            Member creditor = creditors.get(j);

            BigDecimal debt = balances.get(debtor.getId()).abs();
            BigDecimal credit = balances.get(creditor.getId());

            BigDecimal payment = debt.min(credit);

            System.out.println(
                    debtor.getName() + " pays " +
                            creditor.getName() + " " +
                            payment + " TL"
            );

            debt = debt.subtract(payment);
            credit = credit.subtract(payment);

            balances.put(debtor.getId(), debt.negate());
            balances.put(creditor.getId(), credit);

            if (debt.compareTo(BigDecimal.ZERO) == 0) {
                i++;
            }

            if (credit.compareTo(BigDecimal.ZERO) == 0) {
                j++;
            }
        }
    }
}
