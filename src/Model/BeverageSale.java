package Model;

import java.time.LocalDate;

public class BeverageSale {
    private String beverageName;
    private long price;
    private LocalDate saleDate;
    
    public BeverageSale(String beverageName, long price, LocalDate saleDate) {
        this.beverageName = beverageName;
        this.price = price;
        this.saleDate = saleDate;
    }
    
    public String getBeverageName() {
        return beverageName;
    }
    
    public long getPrice() {
        return price;
    }
    
    public LocalDate getSaleDate() {
        return saleDate;
    }
}

