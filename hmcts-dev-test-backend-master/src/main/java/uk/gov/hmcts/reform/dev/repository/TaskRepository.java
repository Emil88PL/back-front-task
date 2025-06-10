package uk.gov.hmcts.reform.dev.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.hmcts.reform.dev.entity.TaskEntity;

public interface TaskRepository  extends JpaRepository<TaskEntity, Long> {
}
