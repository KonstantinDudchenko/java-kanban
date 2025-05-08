package manager;

import task.Epic;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final HashMap<Integer, Node> taskMap = new HashMap<>();
    private Node head;
    private Node tail;

    @Override
    public void add(Task task) {
        if (task == null) return;

        remove(task.getId());

        Node newNode = new Node(task);
        linkLast(newNode);
        taskMap.put(task.getId(), newNode);
    }

    @Override
    public void remove(int id) {
        Node node = taskMap.get(id);
        if (node == null) {
            return;
        }

        switch (node.task.getType()) {
            case TASK, SUBTASK:
                removeNode(node);
                taskMap.remove(id);
                break;

            case EPIC:
                Epic epic = (Epic) node.task;
                for (Integer i : epic.getSubtaskIds()) {
                    removeNode(taskMap.get(i));
                    taskMap.remove(i);
                }
                removeNode(node);
                taskMap.remove(id);
                break;
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private void linkLast(Node newNode) {
        if (tail == null) {
            head = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
        }
        tail = newNode;
    }

    private ArrayList<Task> getTasks() {
        ArrayList<Task> history = new ArrayList<>();
        Node current = tail;
        while (current != null) {
            history.add(current.task);
            current = current.prev;
        }
        return history;
    }

    private void removeNode(Node node) {
        if (node == null) {
            return;
        }
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }

        node.prev = null;
        node.next = null;
    }

    private static class Node {
        Task task;
        Node prev;
        Node next;

        Node(Task task) {
            this.task = task;
        }
    }
}
