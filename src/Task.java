import java.time.LocalDate;

public class Task {
    private String title;
    private String description;
    private boolean isDone;
    private LocalDate dueDate;
    private Priority priority;

    // Required for Gson to deserialize
    public Task() {
    }

    public Task(String title, String description, LocalDate dueDate, Priority priority) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.isDone = false;
    }

    public void markDone() {
        isDone = true;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        this.isDone = done;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Priority getPriority() {
        return priority;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        String status = isDone ? "[✓]" : "[ ]";
        String overdueText = "";

        if (!isDone && dueDate != null && dueDate.isBefore(LocalDate.now())) {
            overdueText = " (Overdue!)";
        }

        return status + " " + title + " (Due: " + dueDate + ", Priority: " + priority + ")" + overdueText + " - " + description;
    }
}
