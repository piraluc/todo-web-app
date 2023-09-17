package ch.cern.todo.api.models;

public class TaskCategoryResponseModel {

    private final long id;

    private final String name;

    private final String description;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public TaskCategoryResponseModel(final long id, final String name, final String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
