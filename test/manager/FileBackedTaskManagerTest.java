package manager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileBackedTaskManagerTest {

    private File tempFile;
    private FileBackedTaskManager manager;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = File.createTempFile("tasks", ".csv");
        manager = new FileBackedTaskManager(tempFile);
    }

    @AfterEach
    void tearDown() {
        tempFile.delete();
    }

    @Test
    void shouldSaveAndLoadEmptyFile() throws IOException {
        // Сохраняем пустой менеджер
        manager.deleteEpic(1);

        // Загружаем из файла
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        // Проверяем, что загруженный менеджер пуст
        assertTrue(loadedManager.getAllTasks().isEmpty());
        assertTrue(loadedManager.getAllEpics().isEmpty());
        assertTrue(loadedManager.getAllSubtasks().isEmpty());

        // Проверяем содержимое файла
        String content = Files.readString(tempFile.toPath());
        assertEquals("id,type,name,status,description,epic", content.trim());
    }

    @Test
    void shouldSaveAndLoadTasks() {
        // Создаем тестовые задачи
        Task task = new Task("Test Task", "Description");
        Epic epic = new Epic("Test Epic", "Description");
        Subtask subtask = new Subtask("Test Subtask", "Description");

        // Добавляем  и сохраняем задачи
        manager.addTask(task);
        Epic newEpic = manager.addEpic(epic);
        subtask.setEpicId(newEpic.getId());
        manager.addSubtask(subtask);

        // Загружаем
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        // Проверяем задачи
        List<Task> tasks = loadedManager.getAllTasks();
        List<Epic> epics = loadedManager.getAllEpics();
        List<Subtask> subtasks = loadedManager.getAllSubtasks();

        assertEquals(1, tasks.size());
        assertEquals(1, epics.size());
        assertEquals(1, subtasks.size());

        assertEquals(task, tasks.get(0));
        assertEquals(epic, epics.get(0));
        assertEquals(subtask, subtasks.get(0));

        // Проверяем связь подзадачи с эпиком
        assertEquals(epic.getId(), subtasks.get(0).getEpicId());
    }


    @Test
    void shouldBeEquivalentToInMemoryManager() {
        // Создаем задачи
        Task task = new Task("Test Task", "Description");
        Epic epic = new Epic("Test Epic", "Description");
        Subtask subtask = new Subtask("Test Subtask", "Description");

        // Добавляем в оба менеджера
        TaskManager inMemoryManager = new InMemoryTaskManager();
        inMemoryManager.addTask(task);
        Epic newEpic = inMemoryManager.addEpic(epic);
        subtask.setEpicId(newEpic.getId());
        inMemoryManager.addSubtask(subtask);

        manager.addTask(task);
        manager.addEpic(epic);
        manager.addSubtask(subtask);

        // Проверяем эквивалентность
        assertEquals(inMemoryManager.getAllTasks(), manager.getAllTasks());
        assertEquals(inMemoryManager.getAllEpics(), manager.getAllEpics());
        assertEquals(inMemoryManager.getAllSubtasks(), manager.getAllSubtasks());
    }
}
