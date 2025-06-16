package uk.gov.hmcts.reform.dev.exception;

import uk.gov.hmcts.reform.dev.entity.TaskStatus;
import java.util.Arrays;
import java.util.stream.Collectors;

public class InvalidStatusException extends RuntimeException {
    public InvalidStatusException(String input, TaskStatus[] allowed) {
        super("Invalid task status: '" + input + "'. Allowed values: " +
                  Arrays.stream(allowed)
                      .map(Enum::name)
                      .collect(Collectors.joining(", "))
        );
    }
}
