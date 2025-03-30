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

    Task createTask(Task task);

    void updateTask(Task task);

    void deleteTask(int id);

    // Методы для эпиков (task.Epic)
    List<Epic> getAllEpics();

    void deleteAllEpics();

    Epic getEpic(int id);

    Epic createEpic(Epic epic);

    void updateEpic(Epic epic);

    void deleteEpic(int id);

    // Методы для подзадач (task.Subtask)
    List<Subtask> getAllSubtasks();

    void deleteAllSubtasks();

    public Subtask getSubtask(int id);

    public Subtask createSubtask(Subtask subtask);

    public void updateSubtask(Subtask subtask);

    public void deleteSubtask(int id);

    //a. Получение списка всех подзадач определённого эпика.

    public List<Subtask> getSubtasksByEpicId(int epicId);

    List<Task> getHistory();
}