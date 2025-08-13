import java.util.Scanner;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TaskManager manager = new TaskManager();

        String filename = "tasks.txt";
        manager.loadFromFile(filename);

        while (true) {
            System.out.println("\n== Task Manager ==");
            System.out.println("1. Add Task");
            System.out.println("2. List Tasks");
            System.out.println("3. Mark Task as Done");
            System.out.println("4. Delete Task");
            System.out.println("5. Exit");
            System.out.println("6. Search & Filter Tasks");
            System.out.println("7. Sort Tasks by Due Date");
            System.out.println("8. Sort Tasks by Priority");
            System.out.println("9. View Overdue Tasks");

            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                	System.out.print("Enter title: ");
                	String title = scanner.nextLine();

                	System.out.print("Enter description: ");
                	String desc = scanner.nextLine();

                	System.out.print("Enter due date (yyyy-mm-dd): ");
                	String dateInput = scanner.nextLine();
                	LocalDate dueDate = LocalDate.parse(dateInput);

                	// Choose priority
                	System.out.print("Enter priority (LOW, MEDIUM, HIGH): ");
                	String priorityInput = scanner.nextLine().toUpperCase();
                	Priority priority = Priority.valueOf(priorityInput);

                	manager.addTask(title, desc, dueDate, priority);

                    break;
                case 2:
                    manager.listTasks();
                    break;
                case 3:
                    System.out.print("Enter task number to mark done: ");
                    int doneIndex = scanner.nextInt() - 1;
                    scanner.nextLine(); // Consume newline
                    manager.markTaskDone(doneIndex);
                    break;
                case 4:
                    System.out.print("Enter task number to delete: ");
                    int delIndex = scanner.nextInt() - 1;
                    scanner.nextLine(); // Consume newline
                    manager.deleteTask(delIndex);
                    break;
                case 5:
                    System.out.println("Saving tasks... Goodbye!");
                    manager.saveToFile(filename);  
                    scanner.close();               
                    return;
                case 6:
                    System.out.println("Search by: 1. Keyword  2. Status  3. Priority");
                    String filterOption = scanner.nextLine();

                    if (filterOption.equals("1")) {
                        System.out.print("Enter keyword to search: ");
                        String keyword = scanner.nextLine();
                        manager.searchByKeyword(keyword);
                    } else if (filterOption.equals("2")) {
                        System.out.print("Enter status (done/undone): ");
                        String status = scanner.nextLine();
                        boolean isDone = status.equalsIgnoreCase("done");
                        manager.searchByStatus(isDone);
                    } else if (filterOption.equals("3")) {
                        System.out.print("Enter priority (LOW, MEDIUM, HIGH): ");
                        String pr = scanner.nextLine().toUpperCase();
                        Priority searchPriority = Priority.valueOf(pr);
                        manager.searchByPriority(searchPriority);
                    } else {
                        System.out.println("Invalid filter option.");
                    }
                    break;
                case 7:
                    manager.sortByDueDate();
                    break;
                case 8:
                    manager.sortByPriority();
                    break;
                case 9:
                    manager.showOverdueTasks();
                    break;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}
