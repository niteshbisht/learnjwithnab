package io.temporal.samples.springboot;

import io.temporal.client.WorkflowClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class SamplesController {

  @Autowired WorkflowClient client;

}
