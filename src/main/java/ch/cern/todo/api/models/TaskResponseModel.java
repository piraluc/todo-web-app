package ch.cern.todo.api.models;

import java.time.LocalDateTime;

public class TaskResponseModel {
    
    private final Long id;

    private final String name;

    private final String description;

    private final LocalDateTime deadline;

    private final TaskCategoryResponseModel category;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public TaskCategoryResponseModel getCategory() {
        return category;
    }

    public TaskResponseModel(Long id, final String name, final String description, final LocalDateTime deadline, final TaskCategoryResponseModel category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.category = category;
    }
}
