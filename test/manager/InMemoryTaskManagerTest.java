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
    private Task testTask1;
    private Task testTask2;
    private Epic testEpic;
    private Subtask testSubtask;

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();

        // Инициализация тестовых данных
        testTask1 = new Task("Test Task 1", "Task description");
        testTask2 = new Task("Test Task 2", "Task description");

        testTask1.setStatus(TaskStatus.IN_PROGRESS);

        testEpic = new Epic("Test Epic", "Epic description");

        testSubtask = new Subtask("Test Subtask", "Subtask description");
    }

    @Test
    void addNewTask() {
        Task newTask = taskManager.addTask(testTask1);
        Task savedTask = taskManager.getTask(newTask.getId());

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(newTask, savedTask, "Задачи не совпадают.");

        List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(testTask1, tasks.get(0), "Задачи не совпадают.");
    }

    // проверьте, что экземпляры класса Task равны друг другу, если равен их id
    @Test
    void tasksWithSameIdShouldBeEqual() {
        testTask1.setId(1);
        testTask2.setId(1);

        assertEquals(testTask1, testTask2, "Задачи с одинаковым id должны быть равны");
    }

    // проверьте, что наследники класса Task равны друг другу, если равен их id
    @Test
    void taskSubtypesWithSameIdShouldBeEqual() {
        testSubtask.setId(1);
        testEpic.setId(1);

        assertEquals(testEpic, testSubtask, "Task и Subtask с одинаковым id должны быть равны");
    }

    // проверьте, что объект Epic нельзя добавить в самого себя в виде подзадачи
    @Test
    void epicCannotBeItsOwnSubtask() {
        Epic newEpic = taskManager.addEpic(testEpic);

        Subtask invalidSubtask = new Subtask("Invalid", "Description");
        invalidSubtask.setId(newEpic.getId());

        assertThrows(IllegalArgumentException.class, () -> taskManager.addSubtask(invalidSubtask));

    }

    // проверьте, что объект Subtask нельзя сделать своим же эпиком
    @Test
    void subtaskCannotReferenceItselfAsEpic() {
        testSubtask.setEpicId(testSubtask.getId());

        assertThrows(IllegalArgumentException.class,
                () -> taskManager.updateSubtask(testSubtask),
                "Подзадача не должна быть своим же эпиком");
    }

    // проверьте, что InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id;
    @Test
    void managerShouldStoreAndFindAllTaskTypes() {

        taskManager.addTask(testTask1);
        taskManager.addEpic(testEpic);
        testSubtask.setEpicId(testEpic.getId());
        taskManager.addSubtask(testSubtask);

        assertEquals(testTask1, taskManager.getTask(testTask1.getId()), "Должна находиться добавленная задача");
        assertEquals(testEpic, taskManager.getEpic(testEpic.getId()), "Должен находиться добавленный эпик");
        assertEquals(testSubtask, taskManager.getSubtask(testSubtask.getId()), "Должна находиться добавленная подзадача");
    }

    // Проверка отсутствия конфликтов id
    @Test
    void generatedAndManualIdsShouldNotConflict() {
        testSubtask.setId(100);
        taskManager.addTask(testSubtask);

        assertNotEquals(100, testSubtask.getId(),
                "ID назначается только менеджером, установка ID вручную игнорируется");

    }

    // Проверка неизменности задачи при добавлении
    @Test
    void taskShouldRemainUnchangedWhenAddedToManager() {

        Task newTask = taskManager.addTask(testTask1);

        assertEquals("Test Task 1", newTask.getName(), "Имя не должно изменяться");
        assertEquals("Task description", newTask.getDescription(), "Описание не должно изменяться");
        assertEquals(TaskStatus.IN_PROGRESS, newTask.getStatus(), "Статус не должен изменяться");
    }

    @Test
    void getHistory_ShouldReturnTaskViewHistory() {
        taskManager.addTask(testTask1);

        taskManager.getTask(testTask1.getId());

        List<Task> history = taskManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(testTask1, history.get(0));
    }
}