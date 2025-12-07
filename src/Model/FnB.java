package Model;

public class FnB {
    private String name;
    private long price;
    
    public FnB(String name, long price) {
        this.name = name;
        this.price = price;
    }
    
    public String getName() {
        return name;
    }
    
    public long getPrice() {
        return price;
    }
    
    @Override
    public String toString() {
        return name + " - Rp " + String.format("%,d", price);
    }
}

