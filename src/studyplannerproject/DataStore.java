package studyplannerproject;

import java.util.ArrayList;


public class DataStore {

   
    public static ArrayList<Task>   tasks    = new ArrayList<>();
    public static ArrayList<String> subjects = new ArrayList<>();

    public static double studyHours = 0;

    public static boolean studyHabit;
    public static boolean revisionHabit;
    public static boolean exerciseHabit;
    public static boolean notesHabit;

    public static int studyStreakDays = 0;
    public static String lastStudyDate = "";   // "dd-MM-yyyy"

    public static int dailyGoalMinutes = 120;  // default 2 h/day
}