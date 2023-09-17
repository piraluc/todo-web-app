package ch.cern.todo.api.models;

public class TaskCategoryRequestModel {

    private final String name;

    private final String description;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public TaskCategoryRequestModel(final String name, final String description) {
        this.name = name;
        this.description = description;
    }
}
