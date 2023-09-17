package ch.cern.todo.api.models;

import java.time.LocalDateTime;

public class TaskRequestModel {

    private final String name;

    private final String description;

    private final LocalDateTime deadline;

    private final TaskCategoryRequestModel category;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public TaskCategoryRequestModel getCategory() {
        return category;
    }

    public TaskRequestModel(final String name, final String description, final LocalDateTime deadline, final TaskCategoryRequestModel category) {
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.category = category;
    }
}
