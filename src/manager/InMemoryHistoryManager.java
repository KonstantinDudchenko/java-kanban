package manager;

import task.Task;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private static final int MAX_HISTORY_SIZE = 10;

    // Хранилище для истории просмотров (максимум 10 элементов)
    private final Deque<Task> viewHistory = new ArrayDeque<>(MAX_HISTORY_SIZE);

    @Override
    public void add(Task task) {
        Task copy = new Task(task.getName(), task.getDescription());
        copy.setId(task.getId());
        copy.setStatus(task.getStatus());

        if (viewHistory.size() == MAX_HISTORY_SIZE) {
            viewHistory.removeLast();
        }
        viewHistory.addFirst(copy);
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(viewHistory);
    }
}
