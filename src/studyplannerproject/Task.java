package studyplannerproject;
import java.io.Serializable;
public class Task implements Serializable {

    private String title;
    private String type;
    private String deadline;
    private String priority;
    private String subject;
    private String status;

    private boolean completed;

    public Task(String title,
                String type,
                String deadline,
                String priority,
                String subject)
    {

        this.title = title;
        this.type = type;
        this.deadline = deadline;
        this.priority = priority;
        this.subject = subject;
        this.status = "Pending";

        completed = false;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getDeadline() {
        return deadline;
    }

    public String getPriority() {
        return priority;
    }

    public String getSubject() {
        return subject;
    }
    public String getStatus() {
    return status;
}

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    public void setStatus(String status) {
    this.status = status;
}
}
