package manager;

import org.junit.jupiter.api.Test;
import task.Task;
import task.TaskStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    HistoryManager historyManager = Managers.getDefaultHistory();

    @Test
    void shouldPreserveTaskStateWhenAddedToHistory() {
        // 1. Создаем и добавляем первоначальную версию задачи
        Task originalTask = new Task("Original", "Description");
        historyManager.add(originalTask);

        // 2. Меняем задачу
        originalTask.setStatus(TaskStatus.IN_PROGRESS);
        originalTask.setName("Modified");
        historyManager.add(originalTask); // Добавляем измененную версию

        // 3. Получаем историю
        List<Task> history = historyManager.getHistory();

        // 4. Проверяем, что в истории две разные версии
        assertEquals(2, history.size(), "История должна содержать 2 версии задачи");

        // Проверяем первоначальную версию (должна быть второй в списке)
        Task firstVersion = history.get(1);
        assertEquals("Original", firstVersion.getName(), "Первая версия должна сохранить оригинальное имя");
        assertEquals(TaskStatus.NEW, firstVersion.getStatus(), "Первая версия должна сохранить оригинальный статус");

        // Проверяем измененную версию (первая в списке)
        Task secondVersion = history.get(0);
        assertEquals("Modified", secondVersion.getName(), "Вторая версия должна сохранить измененное имя");
        assertEquals(TaskStatus.IN_PROGRESS, secondVersion.getStatus(), "Вторая версия должна сохранить измененный статус");
    }
}