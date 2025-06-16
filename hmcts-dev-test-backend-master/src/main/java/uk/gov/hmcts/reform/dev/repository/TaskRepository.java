package uk.gov.hmcts.reform.dev.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.hmcts.reform.dev.entity.Task;
import java.util.List;

public interface TaskRepository  extends JpaRepository<Task, Long> {
    List<Task> findAllByOrderByDueDateTimeAsc();
}
