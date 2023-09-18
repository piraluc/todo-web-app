package ch.cern.todo.services.exceptions;

public class DuplicateTaskCategoryNameException extends Exception {

    private static final String TASK_CATEGORY_WITH_SAME_NAME_ALREADY_EXISTS = "A task category with the same name already exists.";

    public DuplicateTaskCategoryNameException() {
        super(TASK_CATEGORY_WITH_SAME_NAME_ALREADY_EXISTS);
    }
}
