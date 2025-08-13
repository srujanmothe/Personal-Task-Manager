import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {

    private static final String DB_URL = "jdbc:sqlite:tasks.db";

    public DatabaseHelper() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String sql = """
            CREATE TABLE IF NOT EXISTS tasks (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                description TEXT,
                dueDate TEXT,
                priority TEXT,
                isDone INTEGER
            );
        """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveTasks(List<Task> tasks) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            // Clear old data
            conn.createStatement().execute("DELETE FROM tasks");

            String insertSql = "INSERT INTO tasks (title, description, dueDate, priority, isDone) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insertSql);

            for (Task task : tasks) {
                pstmt.setString(1, task.getTitle());
                pstmt.setString(2, task.getDescription());
                pstmt.setString(3, task.getDueDate().toString());
                pstmt.setString(4, task.getPriority().name());
                pstmt.setInt(5, task.isDone() ? 1 : 0);
                pstmt.addBatch();
            }

            pstmt.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Task> loadTasks() {
        List<Task> tasks = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "SELECT title, description, dueDate, priority, isDone FROM tasks";
            ResultSet rs = conn.createStatement().executeQuery(sql);

            while (rs.next()) {
                String title = rs.getString("title");
                String description = rs.getString("description");
                LocalDate dueDate = LocalDate.parse(rs.getString("dueDate"));
                Priority priority = Priority.valueOf(rs.getString("priority"));
                boolean isDone = rs.getInt("isDone") == 1;

                Task task = new Task(title, description, dueDate, priority);
                task.setDone(isDone);
                tasks.add(task);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tasks;
    }
}
