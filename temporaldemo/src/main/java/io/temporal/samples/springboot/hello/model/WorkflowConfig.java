package io.temporal.samples.springboot.hello.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WorkflowConfig {
    private String initialStepId;
    private List<StepDefinitionItem> stepDefinitionItemList;
}
