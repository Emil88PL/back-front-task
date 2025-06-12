package uk.gov.hmcts.reform.dev.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.hmcts.reform.dev.entity.TaskEntity;
import java.util.List;

public interface TaskRepository  extends JpaRepository<TaskEntity, Long> {
    List<TaskEntity> findAllByOrderByDueDateTimeAsc();
}
