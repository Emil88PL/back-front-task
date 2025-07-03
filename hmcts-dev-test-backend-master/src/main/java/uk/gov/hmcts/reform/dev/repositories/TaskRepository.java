package uk.gov.hmcts.reform.dev.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.hmcts.reform.dev.domain.task.Task;
import java.util.List;

public interface TaskRepository  extends JpaRepository<Task, Long> {
    List<Task> findAllByOrderByDueDateTimeAsc();
}
