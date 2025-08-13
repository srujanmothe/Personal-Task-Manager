# 📌 Personal Task Manager (JavaFX + SQLite + Gson)

A simple yet powerful **JavaFX-based Personal Task Manager** that lets you create, manage, search, and sort your tasks.  
Now enhanced with **SQLite database integration** for persistent storage and **JSON export/import** using Gson.

---

## ✨ Features

- ✅ Add, view, edit, and delete tasks
- ✅ Mark tasks as completed
- ✅ Sort by **due date** or **priority**
- ✅ Search by **keyword**, **status**, or **priority**
- ✅ View **overdue tasks**
- 🌙 Dark mode (optional theme)
- 💾 Persistent storage in **SQLite Database**
- 📤 Export and import tasks in **JSON** format

---

## 🛠️ Tech Stack

- **Java** (JDK 21+ recommended)
- **JavaFX** (UI framework)
- **SQLite** (Database)
- **Gson** (JSON processing)

---
## 📖 Project Overview

The Personal Task Manager is a JavaFX-based desktop application designed to help users efficiently manage their daily tasks.
It provides a clean and interactive graphical interface where users can add, view, edit, delete, and search tasks with ease.
Tasks are organized with attributes like title, description, due date, and priority, and can be marked as completed when finished.

To ensure data persistence, the application integrates with an SQLite database for permanent storage of tasks.
In addition, Gson is used to export and import tasks in JSON format, enabling easy backup and sharing of task data.

🎯 Objectives

Provide a simple yet effective task management tool for daily productivity.
Allow sorting and filtering of tasks to quickly find important or overdue work.
Ensure persistent storage so tasks remain available between application runs.
Support data portability via JSON export/import.

🧩 System Workflow

User adds/edits tasks in the JavaFX UI.
TaskManager class processes and stores task details.
SQLite database stores all tasks permanently.
Gson handles JSON serialization/deserialization for backups.
Application supports search, sort, and overdue checks.

📌 Key Advantages

Cross-platform: Runs on any system with Java and JavaFX.
Lightweight: Minimal resource usage while providing all essential features.
Data safety: Uses both database storage and optional JSON backups.
User-friendly: Simple interface for quick task management.

