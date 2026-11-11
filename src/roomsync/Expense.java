
package roomsync;

import java.math.BigDecimal;

public class Expense {
    private int id;
    private String description;
    private BigDecimal amount;
    private Member paidBy;

    public Expense(int id, String description, BigDecimal amount, Member paidBy) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0 || amount.scale() > 2) {
            throw new IllegalArgumentException("Invalid amount!");
        }

        if (paidBy == null) {
            throw new IllegalArgumentException("Member not found!");
        }

        this.id = id;
        this.description = description;
        this.amount = amount.setScale(2);
        this.paidBy = paidBy;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Member getPaidBy() {
        return paidBy;
    }

    @Override
    public String toString() {
        return id + " - " + description +
                " | " + amount + " TL" +
                " | Paid by " + paidBy.getName();
    }
}
