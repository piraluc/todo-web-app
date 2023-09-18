package ch.cern.todo.services.exceptions;

public class TaskNotFoundException extends Exception {

    private static final String TASK_CANNOT_BE_FOUND = "The task cannot be found.";

    public TaskNotFoundException() {
        super(TASK_CANNOT_BE_FOUND);
    }
}
