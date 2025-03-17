public class Main {
    public static void main(String[] args) {

        TaskManager manager = new TaskManager();

        Task task1 = new Task(0, "Task 1", "Description 1", TaskStatus.NEW);
        Task task2 = new Task(0, "Task 2", "Description 2", TaskStatus.NEW);
        manager.createTask(task1);
        manager.createTask(task2);

        Epic epic1 = new Epic(0, "Epic 1", "Description Epic 1", TaskStatus.NEW);
        manager.createEpic(epic1);
        Subtask subtask1 = new Subtask(0, "Subtask 1", "Description Subtask 1", TaskStatus.NEW, epic1.getId());
        Subtask subtask2 = new Subtask(0, "Subtask 2", "Description Subtask 2", TaskStatus.NEW, epic1.getId());
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);

        Epic epic2 = new Epic(0, "Epic 2", "Description Epic 2", TaskStatus.NEW);
        manager.createEpic(epic2);
        Subtask subtask3 = new Subtask(0, "Subtask 3", "Description Subtask 3", TaskStatus.NEW, epic2.getId());
        manager.createSubtask(subtask3);

        System.out.println("All tasks:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }

        System.out.println("\nAll epics:");
        for (Epic epic : manager.getAllEpics()) {
            System.out.println(epic);
        }

        System.out.println("\nAll subtasks:");
        for (Subtask subtask : manager.getAllSubtasks()) {
            System.out.println(subtask);
        }

        task1.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateTask(task1);

        subtask1.setStatus(TaskStatus.DONE);
        manager.updateSubtask(subtask1);

        subtask3.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateSubtask(subtask3);

        System.out.println("\nAfter status changes:");
        System.out.println("Task 1: " + manager.getTaskById(task1.getId()));
        System.out.println("Subtask 1: " + manager.getSubtaskById(subtask1.getId()));
        System.out.println("Epic 1: " + manager.getEpicById(epic1.getId()));
        System.out.println("Subtask 3: " + manager.getSubtaskById(subtask3.getId()));
        System.out.println("Epic 2: " + manager.getEpicById(epic2.getId()));

        manager.deleteTaskById(task1.getId());
        manager.deleteEpicById(epic1.getId());

        System.out.println("\nAfter deletion:");
        System.out.println("All tasks:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }

        System.out.println("\nAll epics:");
        for (Epic epic : manager.getAllEpics()) {
            System.out.println(epic);
        }

        System.out.println("\nAll subtasks:");
        for (Subtask subtask : manager.getAllSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("\nGet subtasks by epic id:");
        for (Subtask subtask : manager.getSubtasksByEpicId(epic2.getId())) {
            System.out.println(subtask);
        }

    }
}