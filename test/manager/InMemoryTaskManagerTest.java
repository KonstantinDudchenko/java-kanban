package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private TaskManager taskManager;

    @BeforeEach
     void setUp() {
        taskManager = Managers.getDefault();
    }

    @Test
    void addNewTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        Task newTask = taskManager.createTask(task);

        Task savedTask = taskManager.getTask(newTask.getId());

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(newTask, savedTask, "Задачи не совпадают.");

        List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    // проверьте, что экземпляры класса Task равны друг другу, если равен их id
    @Test
    void tasksWithSameIdShouldBeEqual() {
        Task task1 = new Task("Task 1", "Description");
        task1.setId(1);
        Task task2 = new Task("Task 2", "Different description");
        task2.setId(1);

        assertEquals(task1, task2, "Задачи с одинаковым id должны быть равны");
    }

    // проверьте, что наследники класса Task равны друг другу, если равен их id
    @Test
    void taskSubtypesWithSameIdShouldBeEqual() {

        Subtask subtask = new Subtask("Subtask", "Description", 0);
        subtask.setId(1);

        Epic epic = new Epic("Epic", "Description");
        epic.setId(1);

        assertEquals(epic, subtask, "Task и Subtask с одинаковым id должны быть равны");
    }

    // проверьте, что объект Epic нельзя добавить в самого себя в виде подзадачи
    @Test
    void epicCannotBeItsOwnSubtask() {
        Epic epic = new Epic("Epic", "Description");
        taskManager.createEpic(epic);

        Subtask invalidSubtask = new Subtask("Invalid", "Description", epic.getId());
        invalidSubtask.setId(epic.getId());

        Subtask subtaskWithRightId = taskManager.createSubtask(invalidSubtask);

        assertNotEquals(subtaskWithRightId.getEpicId(), subtaskWithRightId.getId(), "ID не должны совпадать");
    }

    // проверьте, что объект Subtask нельзя сделать своим же эпиком
    @Test
    void subtaskCannotReferenceItselfAsEpic() {
        Epic epic = taskManager.createEpic(new Epic("Name", "Descr"));
        Subtask subtask = taskManager.createSubtask(new Subtask("Name", "Descr", epic.getId()));

        subtask.setEpicId(subtask.getId());

        assertThrows(IllegalArgumentException.class,
                () -> taskManager.updateSubtask(subtask),
                "Подзадача не должна быть своим же эпиком");
    }

    // проверьте, что InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id;
    @Test
    void managerShouldStoreAndFindAllTaskTypes() {
        Task task = new Task("Task", "Description");
        Epic epic = new Epic("Epic", "Description");
        Subtask subtask = new Subtask("Subtask", "Description", epic.getId());

        taskManager.createTask(task);
        taskManager.createEpic(epic);
        subtask.setEpicId(epic.getId());
        taskManager.createSubtask(subtask);

        assertEquals(task, taskManager.getTask(task.getId()), "Должна находиться добавленная задача");
        assertEquals(epic, taskManager.getEpic(epic.getId()), "Должен находиться добавленный эпик");
        assertEquals(subtask, taskManager.getSubtask(subtask.getId()), "Должна находиться добавленная подзадача");
    }

    // Проверка отсутствия конфликтов id
    @Test
    void generatedAndManualIdsShouldNotConflict() {
        Task taskWithId = new Task("Task", "Description");
        taskWithId.setId(100);
        Task taskWithNewID = taskManager.createTask(taskWithId);

        assertNotEquals(100, taskWithNewID.getId(),
                "ID назначается только менеджером, установка ID вручную игнорируется");

    }

    // Проверка неизменности задачи при добавлении
    @Test
    void taskShouldRemainUnchangedWhenAddedToManager() {
        Task original = new Task("Original", "Description");
        original.setStatus(TaskStatus.IN_PROGRESS);

        Task created = taskManager.createTask(original);

        assertEquals("Original", created.getName(), "Имя не должно изменяться");
        assertEquals("Description", created.getDescription(), "Описание не должно изменяться");
        assertEquals(TaskStatus.IN_PROGRESS, created.getStatus(), "Статус не должен изменяться");
    }
}