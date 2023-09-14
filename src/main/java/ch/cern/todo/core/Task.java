package ch.cern.todo.core;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Task {
    private static final String NAME_CANNOT_BE_NULL_OR_BLANK = "The name of a task cannot be null or blank.";
    private static final String DEADLINE_CANNOT_BE_NULL = "The deadline of a task cannot be null.";
    private static final String CATEGORY_CANNOT_BE_NULL = "The category of a task cannot be null.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String description;

    private LocalDateTime deadline;

    @ManyToOne(cascade = CascadeType.ALL)
    private TaskCategory category;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(NAME_CANNOT_BE_NULL_OR_BLANK);
        }

        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        if (deadline == null) {
            throw new IllegalArgumentException(DEADLINE_CANNOT_BE_NULL);
        }

        this.deadline = deadline;
    }

    public TaskCategory getCategory() {
        return category;
    }

    public void setCategory(TaskCategory category) {
        if (category == null) {
            throw new IllegalArgumentException(CATEGORY_CANNOT_BE_NULL);
        }

        this.category = category;
    }

    public Task() {

    }

    public Task(final String name, final String description, final LocalDateTime deadline, final TaskCategory category) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(NAME_CANNOT_BE_NULL_OR_BLANK);
        }

        if (deadline == null) {
            throw new IllegalArgumentException(DEADLINE_CANNOT_BE_NULL);
        }

        if (category == null) {
            throw new IllegalArgumentException(CATEGORY_CANNOT_BE_NULL);
        }

        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.category = category;
    }
}
