import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import java.time.LocalDate;

public class MainApp extends Application {

    private TaskManager manager = new TaskManager();  // logic backend
    private ListView<Task> taskListView = new ListView<>();
    private static final String SAVE_FILE = "tasks.json";

    private boolean darkMode = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("🗂️ Personal Task Manager");

        // --- Input Fields ---
        TextField titleField = new TextField();
        titleField.setPromptText("Title");

        TextArea descField = new TextArea();
        descField.setPromptText("Description");
        descField.setPrefRowCount(2);

        DatePicker dueDatePicker = new DatePicker();

        ComboBox<Priority> priorityBox = new ComboBox<>();
        priorityBox.getItems().addAll(Priority.values());
        priorityBox.setValue(Priority.MEDIUM);

        // --- Buttons ---
        Button addButton = new Button("➕ Add Task");
        Button deleteButton = new Button("🗑️ Delete");
        Button markDoneButton = new Button("✅ Mark as Done");
        Button saveButton = new Button("💾 Save");
        Button loadButton = new Button("📂 Load");
        Button themeToggle = new Button("🌙 Dark Mode");

        // --- Search ---
        TextField searchField = new TextField();
        searchField.setPromptText("Search tasks...");

        Button searchButton = new Button("🔍 Search");
        Button showAllButton = new Button("👁 Show All");

        searchButton.setOnAction(e -> {
            String keyword = searchField.getText().trim().toLowerCase();
            filterTasks(keyword);
        });

        showAllButton.setOnAction(e -> {
            searchField.clear();
            refreshTaskList();
        });

        HBox searchBox = new HBox(10, searchField, searchButton, showAllButton);
        searchBox.setPadding(new Insets(10, 0, 0, 0));

        // --- List Cell Factory (Color-coded) ---
        taskListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Task> call(ListView<Task> listView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Task task, boolean empty) {
                        super.updateItem(task, empty);
                        if (empty || task == null) {
                            setText(null);
                        } else {
                            setText(task.toString());
                            setStyle("-fx-font-weight: bold;");
                            if (task.getPriority() == Priority.HIGH) {
                                setTextFill(Color.RED);
                            } else if (task.getPriority() == Priority.MEDIUM) {
                                setTextFill(Color.ORANGE);
                            } else {
                                setTextFill(Color.GREEN);
                            }
                        }
                    }
                };
            }
        });

        // --- Button Actions ---
        addButton.setOnAction(e -> {
            String title = titleField.getText();
            String desc = descField.getText();
            LocalDate dueDate = dueDatePicker.getValue();
            Priority priority = priorityBox.getValue();

            if (title.isEmpty() || dueDate == null) {
                showAlert("❗ Error", "Title and due date are required.");
                return;
            }

            Task newTask = new Task(title, desc, dueDate, priority);
            manager.addTask(newTask);
            refreshTaskList();
            titleField.clear(); descField.clear(); dueDatePicker.setValue(null);
        });

        deleteButton.setOnAction(e -> {
            Task selected = taskListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                manager.getTasks().remove(selected);
                refreshTaskList();
            }
        });

        markDoneButton.setOnAction(e -> {
            Task selected = taskListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selected.setDone(true);
                refreshTaskList();
            }
        });

        saveButton.setOnAction(e -> {
            manager.saveToDatabase();
            showAlert("Saved", "Tasks saved to database successfully.");
        });



        loadButton.setOnAction(e -> {
        	manager.initDatabase();
        	manager.loadFromDatabase();

            refreshTaskList();
            showAlert("📂 Loaded", "Tasks loaded successfully.");
        });
        themeToggle.setOnAction(e -> {
            darkMode = !darkMode;
            if (darkMode) {
                themeToggle.setText("☀ Light Mode");
                applyDarkTheme(stage.getScene());
            } else {
                themeToggle.setText("🌙 Dark Mode");
                applyLightTheme(stage.getScene());
            }
        });

        
        
        // --- Layout ---
        HBox buttons = new HBox(10, addButton, deleteButton, markDoneButton, saveButton, loadButton, themeToggle);
        VBox form = new VBox(10, titleField, descField, dueDatePicker, priorityBox, buttons);
        form.setPadding(new Insets(10));
        form.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #d3d3d3; -fx-border-radius: 5;");

        TitledPane inputPane = new TitledPane("📝 Add New Task", form);
       
        
        searchButton.setOnAction(e -> {
            String keyword = searchField.getText().trim().toLowerCase();
            filterTasks(keyword);
        });

        TitledPane searchPane = new TitledPane("🔎 Search", searchBox);

        TitledPane taskPane = new TitledPane("📋 Tasks", taskListView);

        VBox mainLayout = new VBox(10, inputPane, searchPane, taskPane);
        mainLayout.setPadding(new Insets(15));
        mainLayout.setStyle("-fx-background-color: #f0f4f8;");

        // --- Final Scene Setup ---
        Scene scene = new Scene(mainLayout, 650, 600);
        stage.setScene(scene);
        stage.show();

        // --- Load tasks initially ---
        manager.loadFromFile(SAVE_FILE);
        refreshTaskList();
        applyLightTheme(scene);
    }

    private void refreshTaskList() {
        taskListView.getItems().setAll(manager.getTasks());
    }

    private void filterTasks(String keyword) {
        if (keyword.isEmpty()) {
            refreshTaskList();
            return;
        }
        taskListView.getItems().setAll(
            manager.getTasks().stream()
                .filter(task ->
                    task.getTitle().toLowerCase().contains(keyword) ||
                    task.getDescription().toLowerCase().contains(keyword)
                )
                .toList()
        );
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    private void applyDarkTheme(Scene scene) {
        scene.getRoot().setStyle(
            "-fx-base: #2b2b2b; " +
            "-fx-control-inner-background: #3c3f41; " +
            "-fx-background: #2b2b2b; " +
            "-fx-text-fill: white; " +
            "-fx-accent: #0096C9;"
        );
    }

    private void applyLightTheme(Scene scene) {
        scene.getRoot().setStyle(""); // reset to default
    }

}
