package manager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class  ManagersTest {

    //убедитесь, что утилитарный класс всегда возвращает проинициализированные
    // и готовые к работе экземпляры менеджеров;
    @Test
    void getDefaultShouldReturnInitializedTaskManager() {
        TaskManager manager = Managers.getDefault();

        assertNotNull(manager, "Менеджер задач не должен быть null");
    }

    @Test
    void getDefaultHistoryShouldReturnWorkingHistoryManager() {
        HistoryManager historyManager = Managers.getDefaultHistory();

        assertNotNull(historyManager, "Менеджер истории не должен быть null");
    }

}