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

## Main Features

### Live Search Member List
- **Real-time Search** - Type name or phone number, results update instantly
- **Add/Edit/Delete** - Full CRUD operations
- **Member Card** - Generate and save as image
- **Statistics** - Total, Active, Expired members count
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

This is an OOP course project built with Java, JavaFX and MySQL.
