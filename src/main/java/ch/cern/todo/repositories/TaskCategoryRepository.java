package ch.cern.todo.repositories;

import ch.cern.todo.core.TaskCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskCategoryRepository extends JpaRepository<TaskCategory, Long> {

    TaskCategory findByName(String name);
}
