import java.io.*;
import java.util.*;
import java.time.LocalDate;
import java.lang.reflect.Type;
import java.sql.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonDeserializationContext;

public class TaskManager {
    private List<Task> tasks = new ArrayList<>();

    // ✅ Custom Gson with LocalDate support
    private final Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, typeOfT, context) -> LocalDate.parse(json.getAsString()))
        .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context) -> new JsonPrimitive(src.toString()))
        .setPrettyPrinting()
        .create();

    public void addTask(String title, String description, LocalDate dueDate, Priority priority) {
        tasks.add(new Task(title, description, dueDate, priority));
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void listTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks available.");
            return;
        }
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
    }

    public void markTaskDone(int index) {
        if (index >= 0 && index < tasks.size()) {
            tasks.get(index).markDone();
            System.out.println("Task marked as done.");
        } else {
            System.out.println("Invalid task number.");
        }
    }

    public void deleteTask(int index) {
        if (index >= 0 && index < tasks.size()) {
            tasks.remove(index);
            System.out.println("Task deleted.");
        } else {
            System.out.println("Invalid task number.");
        }
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void saveToFile(String filename) {
        try (Writer writer = new FileWriter(filename)) {
            gson.toJson(tasks, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFromFile(String filename) {
        File file = new File(filename);
        if (!file.exists() || file.length() == 0) {
            tasks = new ArrayList<>();
            return;
        }

        try (FileReader reader = new FileReader(file)) {
            Type taskListType = new TypeToken<List<Task>>() {}.getType();
            tasks = gson.fromJson(reader, taskListType);
            if (tasks == null) {
                tasks = new ArrayList<>();
            }
        } catch (Exception e) {
            System.err.println("⚠️ Failed to load tasks from file: " + e.getMessage());
            tasks = new ArrayList<>();
        }
    }

    // --- Search and Filter ---
    public void searchByKeyword(String keyword) {
        boolean found = false;
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            if (task.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                task.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                System.out.println((i + 1) + ". " + task);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No matching tasks found for keyword: " + keyword);
        }
    }

    public void searchByStatus(boolean isDone) {
        boolean found = false;
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            if (task.isDone() == isDone) {
                System.out.println((i + 1) + ". " + task);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No " + (isDone ? "completed" : "pending") + " tasks found.");
        }
    }

    public void searchByPriority(Priority priority) {
        boolean found = false;
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            if (task.getPriority() == priority) {
                System.out.println((i + 1) + ". " + task);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No tasks found with priority: " + priority);
        }
    }

    public void sortByDueDate() {
        tasks.sort(Comparator.comparing(Task::getDueDate));
        System.out.println("Tasks sorted by due date:");
        viewTasks();
    }

    public void viewTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }

        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
    }

    public void sortByPriority() {
        tasks.sort(Comparator.comparing(Task::getPriority));
        System.out.println("Tasks sorted by priority:");
        viewTasks();
    }

    public void showOverdueTasks() {
        boolean found = false;
        LocalDate today = LocalDate.now();

        System.out.println("📌 Overdue Tasks (Due before " + today + "):");

        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            if (!task.isDone() && task.getDueDate().isBefore(today)) {
                System.out.println((i + 1) + ". " + task);
                found = true;
            }
        }

        if (!found) {
            System.out.println("🎉 No overdue tasks. You're on track!");
        }
    }
    private final DatabaseHelper dbHelper = new DatabaseHelper();
    private static final String DB_URL = "jdbc:sqlite:tasks.db";
    // Initialize the DB (call this from constructor or main)
    public void initDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String sql = """
                CREATE TABLE IF NOT EXISTS tasks (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    title TEXT,
                    description TEXT,
                    isDone INTEGER,
                    dueDate TEXT,
                    priority TEXT
                );
            """;
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("⚠️ Error initializing DB: " + e.getMessage());
        }
    }
    public void saveToDatabase() {
        dbHelper.saveTasks(tasks);
    }

    public void loadFromDatabase() {
        tasks = dbHelper.loadTasks();
    }
    
}
