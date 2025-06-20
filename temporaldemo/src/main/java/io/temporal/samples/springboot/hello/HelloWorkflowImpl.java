package io.temporal.samples.springboot.hello;

import io.temporal.samples.springboot.hello.model.*;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.CancellationScope;
import io.temporal.workflow.CompletablePromise;
import io.temporal.workflow.Promise;
import io.temporal.workflow.Workflow;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@WorkflowImpl(taskQueues = "dslqueue")
public class HelloWorkflowImpl implements HelloWorkflow {
  private final WorkflowConfig workflowConfig = DefaultStepDefinition.getDefaultWorkflowConfig();
  enum EventTypes
  {
    WORKFLOW_SIGNALED,
    EXPIRED
  }
  private EventTypes currentEventType;
  private CompletablePromise workflowSignalPromise;
  private String incomingHumanAction;
  int currentActionIndex;
  @Override
  public String execute() {
    String initialStepId = workflowConfig.getInitialStepId();
    var done = false;
    currentActionIndex = findNextActionIndex(initialStepId);
    while(!done) {

      StepDefinitionItem stepDefinitionItem = workflowConfig.getStepDefinitionItemList().get(currentActionIndex);
      done = processStep(stepDefinitionItem);
      if(!done) {
        final List<Promise<Void>> promiseList = new ArrayList<>();
        CancellationScope scope = Workflow.newCancellationScope(
                () -> {
                  buildPromiseList(promiseList, stepDefinitionItem);
                }
        );
        log.info("Starting cancellation scope");

        log.info("Starting cancellation scope");
        scope.run();

        log.info("Waiting for the promise(s) to complete.");
        Promise.anyOf(promiseList).get();

        log.info("Ending cancellation scope");
        // This cancels the other promises that didn't fire
        scope.cancel();

        log.info("One of the promises completed!");
        processPromise(stepDefinitionItem);
        log.info("After processing the promise");
      }
    }
    return "done";
  }

  public boolean processStep(StepDefinitionItem stepDefinitionItem) {
    boolean done = false;
    TaskType taskType = stepDefinitionItem.getTaskType();
    switch (taskType) {
      case TaskType.HUMAN:
        log.info("Waiting for human task to finish");
        break;
      case TaskType.SYSTEM:
        done = stepDefinitionItem.getStepActions().isEmpty();
        if(!done) {
          // process step do nothing
        }
        break;
      default:
        log.info("No Step matched");
    }
    return done;
  }

  private void buildPromiseList( List<Promise<Void>> promiseList,StepDefinitionItem stepDefinitionItem) {
    int sleepTime = stepDefinitionItem.getSleepTime();
    if(sleepTime > 0) {
      Promise<Void> expirationPromise = Workflow.newTimer(Duration.ofSeconds(sleepTime));
      expirationPromise.thenApply((v)-> {
        this.currentEventType = EventTypes.EXPIRED;
        return null;
      });
      promiseList.add(expirationPromise);
    }
    workflowSignalPromise = Workflow.newPromise();
    workflowSignalPromise.thenApply((v)->{
        this.currentEventType = EventTypes.WORKFLOW_SIGNALED;
        return null;
    });
    promiseList.add(workflowSignalPromise);
  }

  private void processPromise(StepDefinitionItem stepDefinitionItem) {
    switch (currentEventType) {
      case WORKFLOW_SIGNALED:
        workflowSignalPromise = Workflow.newPromise();
        workflowSignalPromise.thenApply((v)-> {
          currentEventType = EventTypes.WORKFLOW_SIGNALED;
          return null;
        });
        log.info("Workflow signaled with incoming human action {}", this.incomingHumanAction);
        StepActions nextAction = getActionFromAction(stepDefinitionItem, this.incomingHumanAction);
        currentActionIndex = findNextActionIndex(nextAction.getTargetStepId());
        break;
      case EXPIRED:
        StepActions nextActionExpired = getActionFromAction(stepDefinitionItem, "EXPIRED");
        currentActionIndex = findNextActionIndex(nextActionExpired.getTargetStepId());
        break;
      default:
        throw new IllegalStateException("invalid state");
    }
  }

  StepActions getActionFromAction(StepDefinitionItem stepDefinitionItem, String action) {
    List<StepActions> stepActions = stepDefinitionItem.getStepActions();
    StepActions nextStepAction = stepActions.stream().filter(item -> item.getAction().equals(action)).findFirst().get();
    return nextStepAction;
  }

  private int findNextActionIndex(String stepId)
  {
    if (stepId == null || stepId.isEmpty())
      throw new IllegalArgumentException("StepId cannot be null or empty");

    List<StepDefinitionItem> stepDefinitionItemList = workflowConfig.getStepDefinitionItemList();
    for(int i=0;i<stepDefinitionItemList.size();i++ )
    {
      StepDefinitionItem stepDefinitionItem = stepDefinitionItemList.get(i);
      if (stepId.equals(stepDefinitionItem.getStepId()))
      {
        return i;
      }
    }
    throw new IllegalStateException("Unable to find next action with id " + stepId);
  }

  @Override
  public void signalWorkflow(String incomingHumanAction) {
    this.incomingHumanAction = incomingHumanAction;
    workflowSignalPromise.complete(null);
  }

  @Override
  public int currentStepIndex() {
    return currentActionIndex;
  }

}
