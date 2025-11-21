package Model;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Dashboard {
    private final ObjectProperty<LocalDate> date;
    private final IntegerProperty visitorCount;
    private final IntegerProperty productsSold;
    private final LongProperty dailyIncome;
    private final LongProperty dailyVisitorIncome; // Income from daily visitors only
    private final LongProperty beverageRevenue; // Revenue from beverage sales only

    // Constants
    public static final int DAILY_GYM_FEE = 10000; // Rp 10.000 per visitor

    public Dashboard() {
        this.date = new SimpleObjectProperty<>(LocalDate.now());
        this.visitorCount = new SimpleIntegerProperty(0);
        this.productsSold = new SimpleIntegerProperty(0);
        this.dailyIncome = new SimpleLongProperty(0);
        this.dailyVisitorIncome = new SimpleLongProperty(0);
        this.beverageRevenue = new SimpleLongProperty(0);
    }

    public Dashboard(LocalDate date, int visitorCount, int productsSold, long dailyIncome) {
        this.date = new SimpleObjectProperty<>(date);
        this.visitorCount = new SimpleIntegerProperty(visitorCount);
        this.productsSold = new SimpleIntegerProperty(productsSold);
        this.dailyIncome = new SimpleLongProperty(dailyIncome);
        this.dailyVisitorIncome = new SimpleLongProperty(0);
        this.beverageRevenue = new SimpleLongProperty(0);
    }
    
    public Dashboard(LocalDate date, int visitorCount, int productsSold, long dailyIncome, long dailyVisitorIncome, long beverageRevenue) {
        this.date = new SimpleObjectProperty<>(date);
        this.visitorCount = new SimpleIntegerProperty(visitorCount);
        this.productsSold = new SimpleIntegerProperty(productsSold);
        this.dailyIncome = new SimpleLongProperty(dailyIncome);
        this.dailyVisitorIncome = new SimpleLongProperty(dailyVisitorIncome);
        this.beverageRevenue = new SimpleLongProperty(beverageRevenue);
    }

    // Add visitor (automatically adds income)
    public void addVisitor() {
        visitorCount.set(visitorCount.get() + 1);
        dailyVisitorIncome.set(dailyVisitorIncome.get() + DAILY_GYM_FEE);
        dailyIncome.set(dailyIncome.get() + DAILY_GYM_FEE);
    }

    // Add beverage sale (with price)
    public void addBeverageSale(long beveragePrice) {
        productsSold.set(productsSold.get() + 1);
        beverageRevenue.set(beverageRevenue.get() + beveragePrice);
        dailyIncome.set(dailyIncome.get() + beveragePrice);
    }

    // Add product sale (with price) - legacy method
    public void addProductSale(long productPrice) {
        productsSold.set(productsSold.get() + 1);
        dailyIncome.set(dailyIncome.get() + productPrice);
    }

    // Add custom income
    public void addIncome(long amount) {
        dailyIncome.set(dailyIncome.get() + amount);
    }

    // Reset daily data
    public void resetDailyData() {
        visitorCount.set(0);
        productsSold.set(0);
        dailyIncome.set(0);
        dailyVisitorIncome.set(0);
        beverageRevenue.set(0);
    }
    
    // Getters for new properties
    public long getDailyVisitorIncome() {
        return dailyVisitorIncome.get();
    }
    
    public void setDailyVisitorIncome(long income) {
        this.dailyVisitorIncome.set(income);
    }
    
    public LongProperty dailyVisitorIncomeProperty() {
        return dailyVisitorIncome;
    }
    
    public long getBeverageRevenue() {
        return beverageRevenue.get();
    }
    
    public void setBeverageRevenue(long revenue) {
        this.beverageRevenue.set(revenue);
    }
    
    public LongProperty beverageRevenueProperty() {
        return beverageRevenue;
    }

    // Getters and Setters
    public LocalDate getDate() {
        return date.get();
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public int getVisitorCount() {
        return visitorCount.get();
    }

    public void setVisitorCount(int count) {
        this.visitorCount.set(count);
    }

    public IntegerProperty visitorCountProperty() {
        return visitorCount;
    }

    public int getProductsSold() {
        return productsSold.get();
    }

    public void setProductsSold(int count) {
        this.productsSold.set(count);
    }

    public IntegerProperty productsSoldProperty() {
        return productsSold;
    }

    public long getDailyIncome() {
        return dailyIncome.get();
    }

    public void setDailyIncome(long income) {
        this.dailyIncome.set(income);
    }

    public LongProperty dailyIncomeProperty() {
        return dailyIncome;
    }

    // Format currency
    public String getFormattedIncome() {
        return String.format("Rp %,d", dailyIncome.get());
    }

    @Override
    public String toString() {
        return "DashboardData{" +
                "date=" + date.get() +
                ", visitorCount=" + visitorCount.get() +
                ", productsSold=" + productsSold.get() +
                ", dailyIncome=Rp " + String.format("%,d", dailyIncome.get()) +
                '}';
    }
}