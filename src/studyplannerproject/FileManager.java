package studyplannerproject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class FileManager {
    private static final String TASKS_FILE   = "tasks.dat";
    private static final String HOURS_FILE   = "hours.dat";
    private static final String SUBJECTS_FILE = "subjects.dat";
    private static final String HABITS_FILE  = "habits.dat";

    public static void saveData() {

        saveTasks();
        saveHours();
        saveSubjects();
        saveHabits();
    }

    private static void saveTasks() {
        try {
            ObjectOutputStream out =
                new ObjectOutputStream(
                    new FileOutputStream(TASKS_FILE));
            out.writeObject(DataStore.tasks);
            out.close();
            System.out.println("Tasks saved.");
        } catch (Exception e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }

    private static void saveHours() {
        try {
            ObjectOutputStream out =
                new ObjectOutputStream(
                    new FileOutputStream(HOURS_FILE));
            out.writeDouble(DataStore.studyHours);
            out.close();
            System.out.println("Study hours saved.");
        } catch (Exception e) {
            System.out.println("Error saving hours: " + e.getMessage());
        }
    }

    private static void saveSubjects() {
        try {
            ObjectOutputStream out =
                new ObjectOutputStream(
                    new FileOutputStream(SUBJECTS_FILE));
            out.writeObject(DataStore.subjects);
            out.close();
            System.out.println("Subjects saved.");
        } catch (Exception e) {
            System.out.println("Error saving subjects: " + e.getMessage());
        }
    }

    private static void saveHabits() {
        try {
            ObjectOutputStream out =
                new ObjectOutputStream(
                    new FileOutputStream(HABITS_FILE));

            // save all 4 habits as a simple boolean array
            boolean[] habits = {
                DataStore.studyHabit,
                DataStore.revisionHabit,
                DataStore.exerciseHabit,
                DataStore.notesHabit
            };
            out.writeObject(habits);
            out.close();
            System.out.println("Habits saved.");
        } catch (Exception e) {
            System.out.println("Error saving habits: " + e.getMessage());
        }
    }

    public static void loadData() {

        loadTasks();
        loadHours();
        loadSubjects();
        loadHabits();
    }

    @SuppressWarnings("unchecked")
    private static void loadTasks() {
        try {
            ObjectInputStream in =
                new ObjectInputStream(
                    new FileInputStream(TASKS_FILE));
            DataStore.tasks = (ArrayList<Task>) in.readObject();
            in.close();
            System.out.println("Tasks loaded: " + DataStore.tasks.size());
        } catch (Exception e) {
            System.out.println("No saved tasks found – starting fresh.");
            DataStore.tasks = new ArrayList<>();
        }
    }

    private static void loadHours() {
        try {
            ObjectInputStream in =
                new ObjectInputStream(
                    new FileInputStream(HOURS_FILE));
            DataStore.studyHours = in.readDouble();
            in.close();
            System.out.println("Study hours loaded: " + DataStore.studyHours);
        } catch (Exception e) {
            System.out.println("No saved hours found – starting at 0.");
            DataStore.studyHours = 0;
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadSubjects() {
        try {
            ObjectInputStream in =
                new ObjectInputStream(
                    new FileInputStream(SUBJECTS_FILE));
            DataStore.subjects = (ArrayList<String>) in.readObject();
            in.close();
            System.out.println("Subjects loaded: " + DataStore.subjects.size());
        } catch (Exception e) {
            System.out.println("No saved subjects found – starting fresh.");
            DataStore.subjects = new ArrayList<>();
        }
    }

    private static void loadHabits() {
        try {
            ObjectInputStream in =
                new ObjectInputStream(
                    new FileInputStream(HABITS_FILE));
            boolean[] habits = (boolean[]) in.readObject();
            DataStore.studyHabit    = habits[0];
            DataStore.revisionHabit = habits[1];
            DataStore.exerciseHabit = habits[2];
            DataStore.notesHabit    = habits[3];
            in.close();
            System.out.println("Habits loaded.");
        } catch (Exception e) {
            System.out.println("No saved habits found – all set to false.");
            DataStore.studyHabit    = false;
            DataStore.revisionHabit = false;
            DataStore.exerciseHabit = false;
            DataStore.notesHabit    = false;
        }
    }
}