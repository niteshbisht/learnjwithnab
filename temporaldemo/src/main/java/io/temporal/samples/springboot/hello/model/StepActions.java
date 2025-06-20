package io.temporal.samples.springboot.hello.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StepActions {
    String id;
    String action;
    String targetStepId;
}
