package DataAccess;

import Model.Dashboard;
import Model.FnBSale; // Rename dari BeverageSale
import java.time.LocalDate;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DashboardDoA {
    private static final String VISITORS_TABLE = "dashboard_visitors";
    private static final String FNB_SALES_TABLE = "fnb_sales";

    public DashboardDoA() {
        ensureDatabaseConnection();
    }

    private void ensureDatabaseConnection() {
        if (!DatabaseManager.isConnected()) {
            DatabaseManager.initialize();
        }
    }

    // Get today's data
    public Dashboard getTodayData() {
        LocalDate today = LocalDate.now();

        try {
            // Check if data exists for today
            String checkQuery = "SELECT * FROM " + VISITORS_TABLE + " WHERE date = ?";
            ResultSet rs = DatabaseManager.executeQuery(checkQuery, today.toString());

            if (rs.next()) {
                LocalDate date = LocalDate.parse(rs.getString("date"));
                int visitorCount = rs.getInt("visitor_count");
                long visitorIncome = rs.getLong("visitor_income");
                
                // Ambil data FnB untuk hari ini dari tabel fnb_sales
                Dashboard dashboard = new Dashboard(date, visitorCount, 0, 0, visitorIncome, 0);
                
                // Hitung FnB data secara dinamis
                updateFnBDataForDashboard(dashboard, today);
                
                return dashboard;
            } else {
                // Create new entry for today
                String insertQuery = "INSERT INTO " + VISITORS_TABLE +
                        " (date, visitor_count, visitor_income) VALUES (?, 0, 0)";
                DatabaseManager.executeUpdate(insertQuery, today.toString());

                return new Dashboard(today, 0, 0, 0, 0, 0);
            }
        } catch (SQLException e) {
            System.err.println("✗ Error getting today's data: " + e.getMessage());
            return new Dashboard(today, 0, 0, 0, 0, 0);
        }
    }

    private void updateFnBDataForDashboard(Dashboard dashboard, LocalDate date) {
        try {
            String query = "SELECT COUNT(*) as count, SUM(price * quantity) as total " +
                          "FROM " + FNB_SALES_TABLE + " WHERE sale_date = ?";
            
            ResultSet rs = DatabaseManager.executeQuery(query, date.toString());
            
            if (rs.next()) {
                int fnbSold = rs.getInt("count");
                long fnbRevenue = rs.getLong("total");
                
                // Update dashboard object
                dashboard.setFnBSold(fnbSold);
                dashboard.setFnBRevenue(fnbRevenue);
                
                // Update daily income (visitor income + fnb revenue)
                long totalIncome = dashboard.getDailyVisitorIncome() + fnbRevenue;
                dashboard.setDailyIncome(totalIncome);
            }
        } catch (SQLException e) {
            System.err.println("✗ Error calculating FnB data: " + e.getMessage());
        }
    }

    // Add FnB sale (rename dari addBeverageSale)
    public boolean addFnbSale(String fnbName, long price) {
        return addFnbSale(fnbName, price, 1); // Default quantity = 1
    }
    public boolean addFnbSale(String fnbName, long price, int quantity) {
        try {
            LocalDate today = LocalDate.now();
            long totalPrice = price * quantity;

            // Add to FnB sales table
            String query = "INSERT INTO " + FNB_SALES_TABLE +
                    " (fnb_name, price, sale_date, quantity, total_price) VALUES (?, ?, ?, ?, ?)";

            int rows = DatabaseManager.executeUpdate(query, 
                    fnbName, price, today.toString(), quantity, totalPrice);

            if (rows > 0) {
                System.out.println("✓ FnB sale added: " + fnbName + 
                                 " x" + quantity + " = Rp " + String.format("%,d", totalPrice));
                return true;
            }
        } catch (SQLException e) {
            System.err.println("✗ Failed to add FnB sale: " + e.getMessage());
        }
        return false;
    }

    public int getTotalFnBSold() {
        try {
            String query = "SELECT SUM(quantity) as total FROM " + FNB_SALES_TABLE;
            ResultSet rs = DatabaseManager.executeQuery(query);

            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("✗ Error getting total FnB sold: " + e.getMessage());
        }
        return 0;
    }

    public List<FnBSale> getFnBSalesByDateRange(LocalDate startDate, LocalDate endDate) {
        List<FnBSale> sales = new ArrayList<>();
        
        try {
            String query = "SELECT * FROM " + FNB_SALES_TABLE + 
                          " WHERE sale_date BETWEEN ? AND ? ORDER BY sale_date DESC";
            
            ResultSet rs = DatabaseManager.executeQuery(query, 
                    startDate.toString(), endDate.toString());
            
            while (rs.next()) {
                String fnbName = rs.getString("fnb_name");
                long price = rs.getLong("price");
                LocalDate saleDate = LocalDate.parse(rs.getString("sale_date"));
                int quantity = rs.getInt("quantity");
                long totalPrice = rs.getLong("total_price");
                
                // Anda mungkin perlu memperbarui model FnBSale untuk include quantity
                FnBSale sale = new FnBSale(fnbName, price, saleDate, quantity, totalPrice);
                sales.add(sale);
            }
        } catch (SQLException e) {
            System.err.println("✗ Error getting FnB sales by date range: " + e.getMessage());
        }
        
        return sales;
    }

    // Add visitor
    public boolean addVisitor() {
        try {
            LocalDate today = LocalDate.now();
            Dashboard todayData = getTodayData();

            int newCount = todayData.getVisitorCount() + 1;
            long newIncome = todayData.getDailyVisitorIncome() + Dashboard.DAILY_GYM_FEE;

            String updateQuery = "UPDATE " + VISITORS_TABLE +
                    " SET visitor_count = ?, visitor_income = ? WHERE date = ?";

            int rows = DatabaseManager.executeUpdate(updateQuery,
                    newCount, newIncome, today.toString());

            return rows > 0;
        } catch (SQLException e) {
            System.err.println("✗ Failed to add visitor: " + e.getMessage());
            return false;
        }
    }

    // Get total visitors (all time)
    public int getTotalVisitors() {
        try {
            String query = "SELECT SUM(visitor_count) as total FROM " + VISITORS_TABLE;
            ResultSet rs = DatabaseManager.executeQuery(query);

            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("✗ Error getting total visitors: " + e.getMessage());
        }
        return 0;
    }

    // Get total FnB revenue (all time)
    public long getTotalFnbRevenue() {
        try {
            String query = "SELECT SUM(total_price) as total FROM " + FNB_SALES_TABLE;
            ResultSet rs = DatabaseManager.executeQuery(query);

            if (rs.next()) {
                return rs.getLong("total");
            }
        } catch (SQLException e) {
            System.err.println("✗ Error getting total FnB revenue: " + e.getMessage());
        }
        return 0;
    }

    // Get total daily visitor revenue (all time)
    public long getTotalDailyVisitorRevenue() {
        try {
            String query = "SELECT SUM(visitor_income) as total FROM " + VISITORS_TABLE;
            ResultSet rs = DatabaseManager.executeQuery(query);

            if (rs.next()) {
                return rs.getLong("total");
            }
        } catch (SQLException e) {
            System.err.println("✗ Error getting total daily visitor revenue: " + e.getMessage());
        }
        return 0;
    }
}