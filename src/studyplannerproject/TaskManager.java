package studyplannerproject;

import java.util.ArrayList;

public class TaskManager {

    private ArrayList<Task> tasks;

    public TaskManager() {

        tasks = new ArrayList<>();
    }

    public void addTask(Task task) {

        tasks.add(task);
    }

    public ArrayList<Task> getTasks() {

        return tasks;
    }

    public int getTotalTasks() {

        return tasks.size();
    }

    public int getPendingTasks() {

        int count = 0;

        for(Task t : tasks) {

            if(!t.isCompleted()) {

                count++;
            }
        }

        return count;
    }
}
