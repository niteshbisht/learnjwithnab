package io.temporal.samples.springboot.hello.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StepDefinitionItem {
    private String stepId;
    private int sleepTime;
    private TaskType taskType;
    private List<StepActions> stepActions;
}
