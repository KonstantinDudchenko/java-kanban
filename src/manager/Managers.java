package manager;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager(); // Возвращаем стандартную реализацию
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
