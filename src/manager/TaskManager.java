package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.List;

public interface TaskManager {

    // Методы для задач (task.Task)
    public List<Task> getAllTasks();

    public void deleteAllTasks();

    public Task getTask(int id);

    public Task createTask(Task task);

    public void updateTask(Task task);

    public void deleteTask(int id);

    // Методы для эпиков (task.Epic)
    public List<Epic> getAllEpics();

    public void deleteAllEpics();

    public Epic getEpic(int id);

    public Epic createEpic(Epic epic);

    public void updateEpic(Epic epic);

    public void deleteEpic(int id);

    // Методы для подзадач (task.Subtask)
    public List<Subtask> getAllSubtasks();

    public void deleteAllSubtasks();

    public Subtask getSubtask(int id);

    public Subtask createSubtask(Subtask subtask);

    public void updateSubtask(Subtask subtask);

    public void deleteSubtask(int id);

    //a. Получение списка всех подзадач определённого эпика.

    public List<Subtask> getSubtasksByEpicId(int epicId);
}