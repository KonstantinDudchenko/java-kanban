package manager;

import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryHistoryManagerTest {
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    private Task createTestTask(int id) {
        Task task = new Task("Task", "Desc");
        task.setId(id);
        return task;
    }

    @Test
    void addShouldAddTaskToHistory() {
        Task task = createTestTask(1);
        historyManager.add(task);

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task, history.getFirst());
    }

    @Test
    void addShouldNotAddNullTask() {
        historyManager.add(null);
        assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    void addShouldMoveExistingTaskToEnd() {
        Task task1 = createTestTask(1);
        Task task2 = createTestTask(2);
        Task task1Updated = createTestTask(1); // Та же id, но другой объект

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task1Updated);

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task2, history.get(1));
        assertEquals(task1Updated, history.get(0));
    }

    @Test
    void removeShouldDeleteTaskFromHistory() {
        Task task1 = createTestTask(1);
        Task task2 = createTestTask(2);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.remove(1);

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task2, history.getFirst());
    }

    @Test
    void removeNonExistentTaskShouldDoNothing() {
        Task task = createTestTask(1);
        historyManager.add(task);
        historyManager.remove(99); // Несуществующий ID

        assertEquals(1, historyManager.getHistory().size());
    }

    @Test
    void getHistoryShouldReturnEmptyListForEmptyHistory() {
        assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    void getHistoryShouldReturnTasksInCorrectOrder() {
        Task task1 = createTestTask(1);
        Task task2 = createTestTask(2);
        Task task3 = createTestTask(3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        List<Task> history = historyManager.getHistory();
        assertEquals(3, history.size());
        assertEquals(task1, history.get(2));
        assertEquals(task2, history.get(1));
        assertEquals(task3, history.get(0));
    }

    @Test
    void historyShouldNotContainDuplicates() {
        Task task = createTestTask(1);

        historyManager.add(task);
        historyManager.add(task);
        historyManager.add(task);

        assertEquals(1, historyManager.getHistory().size());
    }

    @Test
    void removeShouldWorkCorrectlyForFirstElement() {
        Task task1 = createTestTask(1);
        Task task2 = createTestTask(2);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.remove(1);

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task2, history.getFirst());
    }

    @Test
    void removeShouldWorkCorrectlyForLastElement() {
        Task task1 = createTestTask(1);
        Task task2 = createTestTask(2);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.remove(2);

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task1, history.getFirst());
    }

    @Test
    void removeShouldWorkCorrectlyForMiddleElement() {
        Task task1 = createTestTask(1);
        Task task2 = createTestTask(2);
        Task task3 = createTestTask(3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(2);

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task1, history.get(1));
        assertEquals(task3, history.get(0));
    }

    @Test
    void shouldRemoveSubtasksWithEpic() {
        Epic epic = new Epic("Epic1", "EpicDecsr");
        epic.setId(1);
        Subtask st1 = new Subtask("Subtask1", "SubtaskDescr1");
        st1.setId(2);
        Subtask st2 = new Subtask("Subtask1", "SubtaskDescr2");
        st2.setId(3);
        epic.addSubtask(2);
        epic.addSubtask(3);

        historyManager.add(st1);
        historyManager.add(st2);
        historyManager.add(epic);

        List<Task> history = historyManager.getHistory();
        assertEquals(3, history.size());

        historyManager.remove(1);
        List<Task> history2 = historyManager.getHistory();
        assertTrue(history2.isEmpty());
    }
}