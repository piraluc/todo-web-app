package ch.cern.todo.services.exceptions;

public class TaskCategoryNotFoundException extends Exception {

    private static final String TASK_CATEGORY_CANNOT_BE_FOUND = "The task category cannot be found.";

    public TaskCategoryNotFoundException() {
        super(TASK_CATEGORY_CANNOT_BE_FOUND);
    }
}
