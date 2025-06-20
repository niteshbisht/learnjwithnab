package io.temporal.samples.springboot;

import io.temporal.api.common.v1.WorkflowExecution;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.samples.springboot.hello.HelloWorkflow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController("/client")
public class ClientController {

  @Autowired WorkflowClient client;

  @PostMapping("/create")
  ResponseEntity<String> createWorkflow(@RequestBody String jsonNode) {
    HelloWorkflow dslWorkflow
            = client.newWorkflowStub(HelloWorkflow.class, WorkflowOptions.newBuilder().setTaskQueue("dslqueue").build());
    WorkflowExecution start = WorkflowClient.start(dslWorkflow::execute);
    return ResponseEntity.ok(start.getWorkflowId());
  }

  @PostMapping("/take-action/{workflowId}")
  ResponseEntity<String> takeAction(@PathVariable String workflowId, @RequestBody String action) {
    HelloWorkflow helloWorkflow = client.newWorkflowStub(HelloWorkflow.class, workflowId);
    helloWorkflow.signalWorkflow(action);
    return ResponseEntity.ok(action);
  }

  @GetMapping("/getstep/{workflowId}")
  ResponseEntity<Map<String, Object>> getCurrentStep(@PathVariable String workflowId) {
    HelloWorkflow helloWorkflow = client.newWorkflowStub(HelloWorkflow.class, workflowId);
    return ResponseEntity.ok(Map.of("WorkflowId", workflowId, "step", helloWorkflow.currentStepIndex()));
  }
}
