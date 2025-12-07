package Model;

import java.time.LocalDate;

public class FnBSale {
    private String fnbName; // rename dari beverageName
    private long price;
    private LocalDate saleDate;
    private int quantity;      
    private long totalPrice;

    public FnBSale(String fnbName, long price, LocalDate saleDate, int quantity, long totalPrice) {
        this.fnbName = fnbName;
        this.price = price;
        this.saleDate = saleDate;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public String getFnbName() { return fnbName; }
    public long getPrice() { return price; }
    public LocalDate getSaleDate() { return saleDate; }
    public int getQuantity() { return quantity; }
    public long getTotalPrice() { return totalPrice; }

    @Override
    public String toString() {
        return String.format("%s x%d = Rp %,d", fnbName, quantity, totalPrice);
    }
}