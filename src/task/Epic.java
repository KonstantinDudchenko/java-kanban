package task;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> subtaskIds;

    public Epic(String name, String description) {
        super(name, description);
        this.subtaskIds = new ArrayList<>();
    }


    public Epic(int id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
        this.subtaskIds = new ArrayList<>();
    }

    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void addSubtask(int subtaskId) {

        subtaskIds.add(subtaskId);
    }

    public void removeSubtaskId(int subtaskId) {
        subtaskIds.remove((Integer) subtaskId);
    }

    public TaskType getType() {
        return TaskType.EPIC;
    }

    @Override
    public String toString() {
        return getId() + "," + getType() + "," + getName()
                + "," + getStatus() + "," + getDescription();
    }
}