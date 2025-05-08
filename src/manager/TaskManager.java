package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.List;

public interface TaskManager {

    // Методы для задач (task.Task)
    List<Task> getAllTasks();

    void deleteAllTasks();

    Task getTask(int id);

    Task addTask(Task task);

    void updateTask(Task task);

    void deleteTask(int id);

    // Методы для эпиков (task.Epic)
    List<Epic> getAllEpics();

    void deleteAllEpics();

    Epic getEpic(int id);

    Epic addEpic(Epic epic);

    void updateEpic(Epic epic);

    void deleteEpic(int id);

    // Методы для подзадач (task.Subtask)
    List<Subtask> getAllSubtasks();

    void deleteAllSubtasks();

    Subtask getSubtask(int id);

    Subtask addSubtask(Subtask subtask);

    void updateSubtask(Subtask subtask);

    void deleteSubtask(int id);

    //a. Получение списка всех подзадач определённого эпика.

    List<Subtask> getSubtasksByEpicId(int epicId);

    List<Task> getHistory();
}