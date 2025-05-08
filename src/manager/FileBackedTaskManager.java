package manager;

import exceptions.ManagerSaveException;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;
import task.TaskType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;
    private final String TASK_FIELDS = "id,type,name,status,description,epic";

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    private void save() {
        try {
            List<String> lines = new ArrayList<>();
            lines.add(TASK_FIELDS);  // Заголовок CSV

            for (Task task : getAllTasks()) {
                lines.add(task.toString());
            }

            for (Task subtask : getAllSubtasks()) {
                lines.add(subtask.toString());
            }

            for (Task epic : getAllEpics()) {
                lines.add(epic.toString());
            }

            Files.write(file.toPath(), lines);
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка сохранения в файл", exception);
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);

        try {
            String content = Files.readString(file.toPath());
            String[] lines = content.split("\n");

            // Пропускаем заголовок и обрабатываем задачи
            for (int i = 1; i < lines.length; i++) {
                Task task = fromString(lines[i]);
                if (task != null) {
                    switch (task.getType()) {
                        case TASK:
                            fileBackedTaskManager.tasks.put(task.getId(), task);
                            break;
                        case EPIC:
                            Epic newEpic = (Epic) task;
                            fileBackedTaskManager.epics.put(newEpic.getId(), newEpic);
                            break;
                        case SUBTASK:
                            Subtask newSubtask = (Subtask) task;
                            fileBackedTaskManager.subtasks.put(newSubtask.getId(), newSubtask);
                            break;
                    }
                }
            }
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка загрузки из файла", exception);
        }
        return fileBackedTaskManager;
    }

    private static Task fromString(String value) {
        String trimmedValue = value.trim();
        String[] parts = trimmedValue.split(",");

        int id = Integer.parseInt(parts[0]);
        TaskType type = TaskType.valueOf(parts[1]);
        String name = parts[2];
        TaskStatus status = TaskStatus.valueOf(parts[3]);
        String description = parts[4];
        String epicId = parts.length > 5 ? parts[5] : "";

        switch (type) {
            case TASK:
                return new Task(id, name, description, status);
            case EPIC:
                return new Epic(id, name, description, status);
            case SUBTASK:
                return new Subtask(id, name, description, status,
                        Integer.parseInt(epicId));
            default:
                return null;
        }
    }


    @Override
    public Task addTask(Task task) {
        Task newTask = super.addTask(task);
        save();
        return newTask;
    }

    @Override
    public Epic addEpic(Epic epic) {
        Epic newEpic = super.addEpic(epic);
        save();
        return newEpic;
    }

    @Override
    public Subtask addSubtask(Subtask subtask) {
        Subtask newSubtask = super.addSubtask(subtask);
        save();
        return newSubtask;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }
}
