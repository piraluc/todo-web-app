package ch.cern.todo.core;

public class TaskCategory {
    private static final String NAME_CANNOT_BE_NULL_OR_BLANK = "The name of a task category cannot be null or blank.";

    private long id;

    private String name;

    private String description;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(NAME_CANNOT_BE_NULL_OR_BLANK);
        }

        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public TaskCategory(final String name, final String description) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(NAME_CANNOT_BE_NULL_OR_BLANK);
        }

        this.name = name;
        this.description = description;
    }
}
