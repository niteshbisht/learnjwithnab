package io.temporal.samples.springboot.hello;

import io.temporal.samples.springboot.hello.model.StepActions;
import io.temporal.samples.springboot.hello.model.StepDefinitionItem;
import io.temporal.samples.springboot.hello.model.TaskType;
import io.temporal.samples.springboot.hello.model.WorkflowConfig;

import java.util.ArrayList;
import java.util.List;

public class DefaultStepDefinition {
    private static WorkflowConfig workflowConfig;
    static {
        workflowConfig = new WorkflowConfig();
        workflowConfig.setInitialStepId("INIT");
        final List<StepDefinitionItem> stepDefinitionItemList = new ArrayList<>();
        final var step1 = new StepDefinitionItem();
        step1.setStepId("INIT");
        final var step1Actions = new ArrayList<StepActions>();
        final var step1ActionItem1 = new StepActions();
        step1ActionItem1.setTargetStepId("EVALUATE");
        step1ActionItem1.setAction("EVALUATE");
        final var step1ActionItem2 = new StepActions();
        step1ActionItem2.setTargetStepId("EXPIRE");
        step1ActionItem2.setAction("EXPIRE");

        step1Actions.add(step1ActionItem1);
        step1Actions.add(step1ActionItem2);
        step1.setStepActions(step1Actions);
        step1.setSleepTime(20000);
        step1.setTaskType(TaskType.HUMAN);
        stepDefinitionItemList.add(step1);

        final var step2 = new StepDefinitionItem();
        step2.setStepId("EVALUATE");
        step2.setTaskType(TaskType.HUMAN);
        final var step2Actions = new ArrayList<StepActions>();
        final var step2ActionItem1 = new StepActions();
        step2ActionItem1.setTargetStepId("CLOSE");
        step2ActionItem1.setAction("CLOSE");
        final var step2ActionItem2 = new StepActions();
        step2ActionItem2.setTargetStepId("EXPIRE");
        step2ActionItem2.setAction("EXPIRE");
        step2Actions.add(step2ActionItem1);
        step2Actions.add(step2ActionItem2);
        step2.setStepActions(step2Actions);
        step2.setSleepTime(20000);
        stepDefinitionItemList.add(step2);

        final var step3 = new StepDefinitionItem();
        step3.setStepId("CLOSE");
        step3.setTaskType(TaskType.SYSTEM);
        final var step3Actions = new ArrayList<StepActions>();
        step3.setStepActions(step3Actions);
        stepDefinitionItemList.add(step3);

        final var step4 = new StepDefinitionItem();
        step4.setStepId("EXPIRE");
        step4.setTaskType(TaskType.SYSTEM);
        final var step4Actions = new ArrayList<StepActions>();
        step4.setStepActions(step4Actions);
        stepDefinitionItemList.add(step4);
        workflowConfig.setStepDefinitionItemList(stepDefinitionItemList);
    }

    public static WorkflowConfig getDefaultWorkflowConfig() {
        return workflowConfig;
    }
}
