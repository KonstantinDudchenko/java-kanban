import manager.*;
import task.Epic;
import task.Subtask;
import task.Task;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();

        System.out.println("=== Создание задач ===");
        // Создаем две задачи
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        Task task2 = new Task("Задача 2", "Описание задачи 2");
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        // Создаем эпик с тремя подзадачами
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        Epic newEpic = taskManager.addEpic(epic1);

        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1");
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2");
        Subtask subtask3 = new Subtask("Подзадача 3", "Описание подзадачи 3");
        subtask1.setEpicId(newEpic.getId());
        subtask2.setEpicId(newEpic.getId());
        subtask3.setEpicId(newEpic.getId());

        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);

        // Создаем эпик без подзадач
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        taskManager.addEpic(epic2);

        System.out.println("Все задачи созданы успешно!\n");

        System.out.println(task1);
        System.out.println(subtask1);
        System.out.println(epic1);
        System.out.println(epic2);

        System.out.println("=== Запрос задач в разном порядке ===");
        // Запрашиваем задачи в разном порядке
        System.out.println("Запрос 1: Задача 1, Эпик 1, Подзадача 2");
        taskManager.getTask(task1.getId());
        taskManager.getEpic(epic1.getId());
        taskManager.getSubtask(subtask2.getId());
        printHistory(taskManager);

        System.out.println("\nЗапрос 2: Подзадача 3, Задача 2, Эпик 1");
        taskManager.getSubtask(subtask3.getId());
        taskManager.getTask(task2.getId());
        taskManager.getEpic(epic1.getId());
        printHistory(taskManager);

        System.out.println("\nЗапрос 3: Эпик 2, Задача 1, Подзадача 1");
        taskManager.getEpic(epic2.getId());
        taskManager.getTask(task1.getId());
        taskManager.getSubtask(subtask1.getId());
        printHistory(taskManager);

        System.out.println("\n=== Удаление задачи из истории ===");
        System.out.println("Удаляем Задачу 1");
        taskManager.deleteTask(task1.getId());
        printHistory(taskManager);

        System.out.println("\n=== Удаление эпика с подзадачами ===");
        taskManager.getSubtask(subtask1.getId());
        taskManager.getSubtask(subtask2.getId());
        taskManager.getSubtask(subtask3.getId());
        printHistory(taskManager);

        System.out.println("Удаляем Эпик 1 (с тремя подзадачами)");
        taskManager.deleteEpic(epic1.getId());
        printHistory(taskManager);
    }

    private static void printHistory(TaskManager taskManager) {
        List<Task> history = taskManager.getHistory();
        System.out.println("Текущая история:");
        if (history.isEmpty()) {
            System.out.println("История пуста");
        } else {
            for (Task task : history) {
                System.out.println("- " + task.toString());
            }
        }
        System.out.println("Проверка дубликатов: " + (hasDuplicates(history) ? "есть дубликаты!" : "дубликатов нет"));
    }

    private static boolean hasDuplicates(List<Task> history) {
        return history.stream()
                .map(Task::getId)
                .distinct()
                .count() != history.size();
    }
}