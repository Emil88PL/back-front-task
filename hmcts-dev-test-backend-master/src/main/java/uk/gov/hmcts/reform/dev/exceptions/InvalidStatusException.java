package uk.gov.hmcts.reform.dev.exceptions;

import uk.gov.hmcts.reform.dev.domain.task.TaskStatus;
import java.util.Arrays;
import java.util.stream.Collectors;

public class InvalidStatusException extends RuntimeException {
    public InvalidStatusException(String input, TaskStatus[] allowed) {
        super("Invalid task status: '" + input + "'. Allowed values: "
                  + Arrays.stream(allowed)
                      .map(Enum::name)
                      .collect(Collectors.joining(", "))
        );
    }
}
