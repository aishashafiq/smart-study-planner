# 📚 Smart Study Planner

A desktop productivity app for students, built with **Java Swing**. It helps you track tasks, subjects, study habits, and study time — all in one place, with a clean dark-friendly UI.

## ✨ Features

- 🔐 **Login screen** — simple username/password gate before entering the app
- 🗂️ **Task management** — add, view, complete, and delete tasks with subject, priority, and deadline
- 📘 **Subject tracker** — add and manage subjects your tasks belong to
- ⏱️ **Pomodoro Timer** — animated countdown ring, tracks completed sessions into your study hours
- ✅ **Habit Tracker** — check off daily habits (study, revision, exercise, reading notes)
- 📅 **Monthly Calendar** — visual overview of your study schedule
- 📝 **Daily To-Do List** — quick day-by-day task list separate from long-term tasks
- 🎵 **Study Playlist** — keep track of music/sounds you study with
- 🗒️ **Study Notes** — jot down and revisit notes per subject
- 📊 **Statistics & Progress Report** — completion percentage, pending/completed counts, progress bar
- 🌗 **Dashboard** — at-a-glance totals for tasks, exams, pending items, and study hours
- 🔔 **Sound alerts** — deadline reminders and Pomodoro session-complete chimes
- 🎨 **Custom theme (`UITheme`)** — centralized colors/fonts so the whole app looks consistent

## 🛠️ Tech Stack

- **Language:** Java
- **UI Framework:** Java Swing (`GroupLayout`)
- **JDK:** Eclipse Adoptium JDK 25
- **IDE:** Visual Studio Code
- **Persistence:** Java serialization (`.dat` files) via a custom `FileManager`

## 📁 Project Structure

```
studyplannerproject/
├── Main.java                 # Entry point
├── LoginForm.java            # Login screen (admin / 1234)
├── MainMenu.java             # Dashboard + navigation hub
├── AddTaskForm.java
├── ViewTasksForm.java
├── AddSubjectForm.java
├── PomodoroTimerForm.java
├── HabitTrackerForm.java
├── StudyPlaylistForm.java
├── MonthlyCalendarForm.java
├── DailyToDoForm.java
├── StudyNotesForm.java
├── GoalTrackerForm.java
├── ProgressReportForm.java
├── StatisticsForm.java
├── Task.java                 # Task model
├── TaskManager.java
├── DataStore.java             # In-memory app state
├── FileManager.java           # Save/load to disk
├── SoundPlayer.java            # Plays .wav alerts/notifications
└── UITheme.java                # Shared colors, fonts, button styles
```

## ▶️ Getting Started

### Prerequisites
- [Eclipse Adoptium JDK 25](https://adoptium.net/) (or any JDK 17+)
- [VS Code](https://code.visualstudio.com/) with the **Extension Pack for Java**

### Run it
1. Clone the repo:
   ```bash
   git clone https://github.com/<your-username>/smart-study-planner.git
   cd smart-study-planner
   ```
2. Open the folder in VS Code.
3. Run `studyplannerproject.Main` (via the Run button, or `F5` using the included `launch.json`).
4. Log in with:
   - **Username:** `admin`
   - **Password:** `1234`

## 💾 Data Storage

Task, subject, habit, and study-hour data are saved locally as `.dat` files (`tasks.dat`, `hours.dat`, `subjects.dat`, `habits.dat`) using Java serialization. These are created automatically on first save and are excluded from version control (see `.gitignore`).

## 🗺️ Roadmap

- [ ] Export progress reports to PDF
- [ ] Cloud sync / multi-device support
- [ ] Custom themes beyond dark/light
- [ ] Notifications outside the app window

## 📄 License

This project is open source. Feel free to fork it and adapt it for your own study workflow.

---

Built as a learning project to practice Java Swing, event-driven UI design, and file persistence.