package Controller;

import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class MainController {
    // ============================================
    // FXML Fields - Main Menu
    // ============================================
    @FXML
    private StackPane mainPane;
    
    // Sidebar buttons
    @FXML
    private javafx.scene.control.Button btnDashboard;
    @FXML
    private javafx.scene.control.Button btnAnalytics;
    @FXML
    private javafx.scene.control.Button btnMember;

    // ============================================
    // Controllers
    // ============================================
    private MemberController membershipController;
    private DashboardController dashboardController;
    private AnalyticsController analyticsController;

    // Timeline untuk auto-refresh status membership
    private Timeline statusRefreshTimeline;

    // ============================================
    // Initialize Method
    // ============================================
    @FXML
    public void initialize() {
        // Initialize controllers
        membershipController = new MemberController();
        dashboardController = new DashboardController();

        // Setup auto-refresh status setiap menit
        startStatusRefreshTimer();

        // Setup Button sidebar
        updateSidebarActive(null);
    }

    // Auto-refresh membership status setiap menit
    private void startStatusRefreshTimer() {
        statusRefreshTimeline = new Timeline(
                new javafx.animation.KeyFrame(Duration.minutes(1), event -> {
                    // Refresh membership status jika membership page sedang aktif
                    if (membershipController != null) {
                        membershipController.refreshTable();
                    }
                })
        );
        statusRefreshTimeline.setCycleCount(Timeline.INDEFINITE);
        statusRefreshTimeline.play();
    }

    // ============================================
    // Navigation Methods
    // ============================================
    private void setMainPane(String fxml) {
        try {
            Node node = FXMLLoader.load(getClass().getResource(fxml));

            if (node instanceof Region) {
                Region region = (Region) node;
                region.prefWidthProperty().bind(mainPane.widthProperty());
                region.prefHeightProperty().bind(mainPane.heightProperty());
                region.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
                region.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            }

            mainPane.getChildren().setAll(node);

        } catch (Exception e) {
            System.err.println("Error loading FXML: " + fxml);
            e.printStackTrace();
        }
    }

    public void memberAct(ActionEvent actionEvent) {
        setMainPane("/resources/fxml/Membership.fxml");
        updateSidebarActive("btnMember");

        Platform.runLater(() -> {
            Node memberNode = mainPane.getChildren().isEmpty() ? null : mainPane.getChildren().get(0);
            if (memberNode != null) {
                // Setup background image
                ImageView backgroundImage = (ImageView) memberNode.lookup("#memberBackgroundImage");
                if (backgroundImage != null) {
                    backgroundImage.fitWidthProperty().bind(mainPane.widthProperty());
                    backgroundImage.fitHeightProperty().bind(mainPane.heightProperty());
                    backgroundImage.setPreserveRatio(false);
                }

                // Setup membership table and controls using MembershipController
                membershipController.setupMemberTable(memberNode);
                membershipController.setupMemberControls(memberNode);
                membershipController.loadMemberData();
            }
        });
    }

    public void analyticsAct(ActionEvent actionEvent) {
        setMainPane("/resources/fxml/Analytics.fxml");
        updateSidebarActive("btnAnalytics");
        
        Platform.runLater(() -> {
            Node analyticsNode = mainPane.getChildren().isEmpty() ? null : mainPane.getChildren().get(0);
            if (analyticsNode != null) {
                // Setup background image
                ImageView backgroundImage = (ImageView) analyticsNode.lookup("#analyticsBackgroundImage");
                if (backgroundImage != null) {
                    backgroundImage.fitWidthProperty().bind(mainPane.widthProperty());
                    backgroundImage.fitHeightProperty().bind(mainPane.heightProperty());
                    backgroundImage.setPreserveRatio(false);
                }
                
                // Setup analytics controller if needed
                if (analyticsController == null) {
                    analyticsController = new AnalyticsController();
                }
                analyticsController.setupAnalytics(analyticsNode);
                // Always refresh when navigating to analytics page
                analyticsController.refreshAnalytics();
            }
        });
    }

    public void dashboardAct(ActionEvent actionEvent) {
        setMainPane("/resources/fxml/Dashboard.fxml");
        updateSidebarActive("btnDashboard");

        // Setup background image and dashboard
        Platform.runLater(() -> {
            Node dashboardNode = mainPane.getChildren().isEmpty() ? null : mainPane.getChildren().get(0);
            if (dashboardNode != null) {
                // Setup background image
                ImageView backgroundImage = (ImageView) dashboardNode.lookup("#dashboardBackgroundImage");
                if (backgroundImage != null) {
                    backgroundImage.fitWidthProperty().bind(mainPane.widthProperty());
                    backgroundImage.fitHeightProperty().bind(mainPane.heightProperty());
                    backgroundImage.setPreserveRatio(false);
                }

                // Setup dashboard with callback to refresh analytics
                dashboardController.setOnDataChangedCallback(() -> {
                    // Refresh analytics if it's currently displayed
                    if (analyticsController != null && mainPane.getChildren().size() > 0) {
                        Node currentPage = mainPane.getChildren().get(0);
                        if (currentPage != null && currentPage.lookup("#totalMembersLabel") != null) {
                            analyticsController.refreshAnalytics();
                        }
                    }
                });
                dashboardController.setupDashboard(dashboardNode);
            }
        });
    }
    
    // Update sidebar active button styling
    private void updateSidebarActive(String activeButtonId) {
        String activeStyle = "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 10; -fx-cursor: hand; -fx-font-weight: bold; -fx-font-size: 14;";
        String inactiveStyle = "-fx-background-color: transparent; -fx-text-fill: #333333; -fx-background-radius: 10; -fx-cursor: hand; -fx-font-weight: bold; -fx-font-size: 14; -fx-border-color: transparent;";

        // Jika activeButtonId null, set semua ke inactive
        if (activeButtonId == null) {
            if (btnDashboard != null) btnDashboard.setStyle(inactiveStyle);
            if (btnAnalytics != null) btnAnalytics.setStyle(inactiveStyle);
            if (btnMember != null) btnMember.setStyle(inactiveStyle);
            return;
        }

        // Set berdasarkan button yang aktif
        if (btnDashboard != null) {
            btnDashboard.setStyle(activeButtonId.equals("btnDashboard") ? activeStyle : inactiveStyle);
        }
        if (btnAnalytics != null) {
            btnAnalytics.setStyle(activeButtonId.equals("btnAnalytics") ? activeStyle : inactiveStyle);
        }
        if (btnMember != null) {
            btnMember.setStyle(activeButtonId.equals("btnMember") ? activeStyle : inactiveStyle);
        }
    }
}