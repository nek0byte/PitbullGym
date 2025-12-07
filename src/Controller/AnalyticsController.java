package Controller;

import DataAccess.DashboardDoA;
import DataAccess.MemberDoA;
import Model.Dashboard;
import javafx.scene.Node;
import javafx.scene.control.Label;

public class AnalyticsController {

    private Label totalMembersLabel;
    private Label totalDailyVisitorsLabel;
    private Label totalSubscriptionRevenueLabel;
    private Label totalDailyVisitorRevenueLabel;
    private Label totalFnbRevenueLabel;
    private Label combinedTotalRevenueLabel;

    private DashboardDoA dashboardDAO;
    private MemberDoA memberDAO;

    public AnalyticsController() {
        this.dashboardDAO = new DashboardDoA();
        this.memberDAO = new MemberDoA();
    }

    // Setup analytics from node lookup
    public void setupAnalytics(Node analyticsNode) {
        // Lookup all labels
        totalMembersLabel = (Label) analyticsNode.lookup("#totalMembersLabel");
        totalDailyVisitorsLabel = (Label) analyticsNode.lookup("#totalDailyVisitorsLabel");
        totalSubscriptionRevenueLabel = (Label) analyticsNode.lookup("#totalSubscriptionRevenueLabel");
        totalDailyVisitorRevenueLabel = (Label) analyticsNode.lookup("#totalDailyVisitorRevenueLabel");
        totalFnbRevenueLabel = (Label) analyticsNode.lookup("#totalFnBRevenueLabel");
        combinedTotalRevenueLabel = (Label) analyticsNode.lookup("#combinedTotalRevenueLabel");

        // Initial load
        refreshAnalytics();
    }

    // Refresh analytics data
    public void refreshAnalytics() {
        // Total registered members
        if (totalMembersLabel != null) {
            int totalMembers = memberDAO.getTotalMembers();
            totalMembersLabel.setText(String.valueOf(totalMembers));
        }

        // Total daily visitors (all time)
        if (totalDailyVisitorsLabel != null) {
            int totalVisitors = dashboardDAO.getTotalVisitors();
            totalDailyVisitorsLabel.setText(String.valueOf(totalVisitors));
        }

        // Total subscription revenue (from members)
        if (totalSubscriptionRevenueLabel != null) {
            long subscriptionRevenue = calculateSubscriptionRevenue();
            totalSubscriptionRevenueLabel.setText(String.format("Rp %,d", subscriptionRevenue));
        }

        // Total daily visitor revenue (all time)
        if (totalDailyVisitorRevenueLabel != null) {
            long dailyVisitorRevenue = dashboardDAO.getTotalDailyVisitorRevenue();
            totalDailyVisitorRevenueLabel.setText(String.format("Rp %,d", dailyVisitorRevenue));
        }

        // Total FnB revenue (all time) - UPDATE NAMA
        if (totalFnbRevenueLabel != null) { // dari totalBeverageRevenueLabel
            long fnbRevenue = dashboardDAO.getTotalFnbRevenue();
            totalFnbRevenueLabel.setText(String.format("Rp %,d", fnbRevenue));
        }

        // Combined total revenue
        if (combinedTotalRevenueLabel != null) {
            long subscriptionRevenue = calculateSubscriptionRevenue();
            long dailyVisitorRevenue = dashboardDAO.getTotalDailyVisitorRevenue();
            long fnbRevenue = dashboardDAO.getTotalFnbRevenue();
            long combined = subscriptionRevenue + dailyVisitorRevenue + fnbRevenue;
            combinedTotalRevenueLabel.setText(String.format("Rp %,d", combined));
        }
    }

    // Calculate subscription revenue from members
    private long calculateSubscriptionRevenue() {
        long revenue = 0;
        var members = memberDAO.getAllMembers();
        for (var member : members) {
            if ("Monthly".equals(member.getPlanType())) {
                revenue += 150000; // Rp 150k per month
            } else if ("Special".equals(member.getPlanType())) {
                revenue += 650000; // Rp 650k per month
            }
        }
        return revenue;
    }
}

