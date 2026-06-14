# PitbullGym - Member Management System for Pitbull Gym

A Gym management application

### Key Features 

- **Member Management** - Add, Edit, Delete, View members
- **Live Search** - Real-time search by name or phone number
- **Membership Analytics** - Dashboard dengan statistics
- **Member Card** - Generate member card as PNG image
- **Status Tracking** - Active/Expired membership tracking
- **Data Persistence** - All data stored in MySQL database
- **Responsive UI** - Modern JavaFX interface

---

## Quick Start

### Prerequisites
```
✓ Java JDK 11+ (Project uses JDK 25)
✓ MySQL Server 5.7+
✓ MySQL Connector/J 8.0+
```

### Setup Steps

1. **Create Database**
   ```bash
   mysql -u root -p < setup_database.sql
   ```

2. **Add MySQL JDBC Driver**
   - Download: https://dev.mysql.com/downloads/connector/j/
   - Copy to: `lib/mysql-connector-java-8.0.33.jar`

3. **Configure Connection** (Edit `src/resources/config.properties`)
   ```properties
   db.host=localhost
   db.port=3306
   db.name=pitbullgym
   db.user=root
   db.password=
   ```

4. **Run Application**
   ```bash
   java -cp "lib/*:bin" Main
   ```

5. **Test Live Search**
   - Go to Member List page
   - Type member name → Real-time filter ✓

---

## Project Structure

```
PitbullGym/
├── lib/                          # External libraries
│   └── mysql-connector-java-8.0.33.jar
├── src/
│   ├── resources/
│   │   ├── config.properties     # ← Configure database here
│   │   ├── fxml/                 # JavaFX layouts
│   │   ├── Icons/                # Images
│   │   └── Styles/               # CSS
│   ├── DataAccess/
│   │   ├── DatabaseManager.java  # ← MySQL connection
│   │   └── MemberDoA.java        # ← Data access layer
│   ├── Model/
│   │   └── Member.java           # Data model
│   ├── Controller/
│   │   ├── MainController.java
│   │   └── MemberController.java # ← Live search logic
│   └── Main.java                 # Entry point
├── setup_database.sql            # ← Run this first
├── QUICK_SETUP.md                # Fast checklist
├── MYSQL_SETUP.md                # Detailed guide
├── MIGRATION_SUMMARY.md          # SQLite → MySQL
├── ARCHITECTURE.md               # System design
└── README.md                     # This file
```

---

## Main Features

### Live Search Member List
- **Real-time Search** - Type name or phone number, results update instantly
- **Add/Edit/Delete** - Full CRUD operations
- **Member Card** - Generate and save as image
- **Statistics** - Total, Active, Expired members count
- 
---

## 🔧 Configuration

Edit `src/resources/config.properties`:

```properties
# Localhost setup
db.host=localhost
db.port=3306
db.name=pitbullgym
db.user=root
db.password=

# Remote server (example)
# db.host=192.168.1.100
# db.user=gym_admin
# db.password=secure_pass
```

---

## 📊 Live Search Implementation

```
User Types in Search Field
        ↓
Text Listener (MemberController)
        ↓
searchMembers(keyword)
        ↓
MemberDoA.searchMembers(keyword)
        ↓
DatabaseManager.executeQuery()
        ↓
MySQL: SELECT * FROM members WHERE LOWER(name) LIKE '%keyword%' OR phone LIKE '%keyword%'
        ↓
Results updated in TableView
        ↓
Statistics refreshed automatically
```

---

## Troubleshooting

| Problem | Solution |
|---------|----------|
| "No suitable driver found" | Add MySQL JAR to classpath, check lib/ folder |
| "Access denied" | Check username/password in config.properties |
| "Unknown database" | Run setup_database.sql |
| "Connection refused" | MySQL service not running |

---

## Security Features

✅ Prepared Statements (SQL injection protection)  
✅ Parameter binding (type safety)  
✅ UNIQUE constraints (data integrity)  
✅ Proper connection handling

---

This is an OOP Project built with JavaFX, MySQL, and clean architecture principles.

Please support this project if you find it useful!
