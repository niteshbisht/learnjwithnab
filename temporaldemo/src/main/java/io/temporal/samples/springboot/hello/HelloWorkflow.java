package io.temporal.samples.springboot.hello;

import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface HelloWorkflow {
  @WorkflowMethod
  String execute();

  @SignalMethod(name = "signalWorkflow")
  void signalWorkflow(String incomingHumanAction);

  @QueryMethod(name = "getCurrentStepIndex")
  int currentStepIndex();
}
